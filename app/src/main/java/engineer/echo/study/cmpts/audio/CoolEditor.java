package engineer.echo.study.cmpts.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Message;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import engineer.echo.study.cmpts.audio.callback.CoolEditorListener;
import engineer.echo.study.cmpts.audio.helper.CoolUtil;
import engineer.echo.study.cmpts.audio.helper.ProgressHandler;

import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PAUSE;
import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PLAY;
import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PLAY_PROGRESS;
import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PREPARE;
import static engineer.echo.study.cmpts.audio.CoolCode.PAUSE;
import static engineer.echo.study.cmpts.audio.CoolCode.PLAY;
import static engineer.echo.study.cmpts.audio.CoolCode.SEEK;
import static engineer.echo.study.cmpts.audio.CoolCode.STOP;

/**
 * CoolEditor.java
 * Info: 音频播放、并支持音频美化(会返回每一帧数据，需自行实现)
 * 0. 名字由来：Adobe CoolEditor
 * 1. 支持 PCM 录音文件播放
 * 2. 实时进度回调
 * 3. 时长解析
 * 4. 支持波形解析波形数组
 *
 * mPlayer = CoolEditor.Builder("lee.pcm")
 *                 .isStereo(true)
 *                 .autoPlay(true)
 *                 .loop(true)
 *                 .sampleRate(44100)
 *                 .audioFormat(AudioFormat.ENCODING_PCM_16BIT)
 *                 .listener(object : CoolEditorListener {
 *                     override fun onProgressChanged(percent: Float, current: Float, total: Float) {
 *
 *                     }
 *
 *                     override fun onPlayStateChanged(playState: Int) {
 *
 *                     }
 *
 *                     override fun onPrepareState(ready: Boolean) {
 *
 *                     }
 *                 }).build()
 */
public class CoolEditor implements IEditor {

    // ===========================================================
    // companion object
    // ===========================================================

    private static final String TAG = "MTAudioEffect";

    // ===========================================================
    // Fields
    // ===========================================================
    /**
     * 音频文件、注意：
     * 计算指定了播放文件
     * 仍然需要指定 采样率、单声道或双声道
     */
    private String mAudioPath;
    /**
     * 构造时指定空背景音频时长(单位秒)
     */
    private int mAudioInputDuration;
    /**
     * 通道数
     * MONO单声道(1)、STEREO双声道(2)
     */
    private int mChannelNum;
    /**
     * 采样率 主流 44100、16000
     */
    private int mSampleRateInHz;
    /**
     * 详细请参考{@link AudioFormat#ENCODING_PCM_16BIT}
     */
    private int mAudioFormat;
    /**
     * 是否自动播放
     */
    private boolean mAutoPlay;
    /**
     * 是否循环播放
     */
    private boolean mLoop;

    private AudioTrack mAudioTrack;
    private ScheduledExecutorService mScheduledPool;
    /**
     * 播放线程锁
     */
    private final Object mLock = new Object();
    /**
     * 播放控制信号
     */
    private AtomicInteger mSignal = new AtomicInteger(PLAY);

    private byte[] mAudioData;
    private int mBufferSize;
    private int mChannelConfig;

    private CoolEditorListener mListener;
    private ProgressHandler mHandler;

    private volatile double mProgress = 0;
    private AtomicLong mDuration = new AtomicLong(0);
    private AtomicBoolean mReady = new AtomicBoolean(false);

    private CoolEditor() {

    }

    private CoolEditor(@NonNull Builder builder) {
        mAudioPath = builder.audioPath;
        mAudioInputDuration = builder.duration;
        mSampleRateInHz = builder.sampleRate;
        mAudioFormat = builder.audioFormat;
        mChannelNum = builder.isStereo ? 2 : 1;
        mChannelConfig = builder.isStereo ? AudioFormat.CHANNEL_OUT_STEREO : AudioFormat.CHANNEL_OUT_MONO;
        mLoop = builder.loop;
        mAutoPlay = builder.autoPlay;
        mListener = builder.listener;
        mHandler = new ProgressHandler(mListener);
        mScheduledPool = Executors.newSingleThreadScheduledExecutor();
        build();
    }

    // ===========================================================
    // Override Methods
    // ===========================================================

    public void build() {
        if (mAutoPlay) {
            //标记播放
            play();
        }
        mScheduledPool.submit(new Runnable() {
            @Override
            public void run() {
                buildAudioTrack();
                boolean ready = makeSureInputData();
                mReady.set(ready);
                int index = 0;
                float allFrames = getAllFrames();
                float total = byteToTime(allFrames);
                mDuration.set(Math.round(total));
                sendPrepareState(ready);
                Log.d(TAG, "ready=" + ready + " allFrames=" + allFrames + " total=" + total);
                if (!ready || allFrames == 0) {
                    mSignal.set(STOP);
                    sendPlayOrPauseMessage(false);
                    return;
                }
                while (true) {
                    //停止信号 直接退出循环 结束线程
                    if (mSignal.get() == STOP) {
                        break;
                    }

                    //Seek信号 Seek完成继续播放
                    if (mSignal.get() == SEEK) {
                        synchronized (mLock) {
                            float expect = (float) mProgress * allFrames;
                            int size = (int) (expect / mBufferSize);
                            index = size * mBufferSize;
                            mSignal.set(PLAY);
                            sendPlayOrPauseMessage(true);
                            continue;
                        }
                    }

                    if (mSignal.get() == PLAY) {
                        //处于播放状态就一直读写数据
                        byte[] musicData = CoolUtil.copyRange(mAudioData, index, mBufferSize);
                        index += mBufferSize;

                        float percent = (index / allFrames);
                        if (mHandler != null) {
                            Message message = new Message();
                            message.what = MSG_PLAY_PROGRESS;
                            message.obj = new float[]{percent, byteToTime(index), total};
                            mHandler.sendMessage(message);
                        }
                        //播放到头了
                        if (musicData == null || musicData.length == 0) {
                            if (mLoop) {
                                index = 0;
                                continue;
                            } else {
                                //播放结束 发送暂停状态
                                sendPlayOrPauseMessage(false);
                                break;
                            }
                        }
                        byte[] result = musicData;
                        if (result != null) {
                            // MODE_STREAM 模式下需要先play再write
                            // MODE_STATIC 模式下需要先write再play，源码中 MODE_STATIC 模式下 write会将 mState置为 STATE_INITIALIZED 状态
                            playInner();
                            boolean success = write(result);
                            if (!success) {
                                //播放器已经停止播放了 发送暂停状态
                                sendPlayOrPauseMessage(false);
                                break;
                            }
                        } else {
                            //播放器已经停止播放了 发送暂停状态
                            sendPlayOrPauseMessage(false);
                            break;
                        }
                    } else {
                        //暂停信号 暂停状态下 线程挂起 待Play、Seek、Stop时唤醒线程
                        synchronized (mLock) {
                            try {
                                mLock.wait();
                            } catch (InterruptedException e) {
                                Log.e(TAG, e.getMessage());
                                //挂起失败 则重新挂起
                            }
                        }
                    }
                }

            }
        });
    }

    @MainThread
    @Override
    public void play() {
        synchronized (mLock) {
            //开启播放时应该让播放线程唤起
            mSignal.set(PLAY);
            if (mListener != null) {
                mListener.onPlayStateChanged(PLAY);
            }
            mLock.notifyAll();
        }
    }

    @MainThread
    @Override
    public void pause() {
        synchronized (mLock) {
            mSignal.set(PAUSE);
            if (mListener != null) {
                mListener.onPlayStateChanged(PAUSE);
            }
            mLock.notifyAll();
        }
    }

    @Override
    public void seekTo(float progress) {
        synchronized (mLock) {
            if (mSignal.get() == SEEK) return;
            //让播放线程唤起执行seek
            mSignal.set(SEEK);
            mProgress = progress;
            mLock.notifyAll();
        }
    }

    @Override
    public void release() {
        synchronized (mLock) {
            mSignal.set(STOP);
            mLock.notifyAll();
        }
        releaseInner();
        if (mAudioData != null) {
            mAudioData = null;
        }
        mListener = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mScheduledPool != null) {
            mScheduledPool.shutdownNow();
        }
    }

    @Override
    public boolean hasReady() {
        return mReady.get();
    }

    @Override
    public long getDuration() {
        return mDuration.get();
    }

    @Override
    public boolean isPlaying() {
        return mSignal.get() == PLAY;
    }

    // ===========================================================
    // Define Methods
    // ===========================================================

    /**
     * 将文件byte大小转换为 时间长度
     *
     * @param byteLength byte PCM 文件的 byte 数组
     * @return ms
     */
    private float byteToTime(float byteLength) {
        int bit = getAudioEncodingBit();
        return CoolUtil.byteToTime(byteLength, mChannelNum, mSampleRateInHz, bit);
    }

    private int getAudioEncodingBit() {
        // TODO 暂时只支持 AudioFormat.ENCODING_PCM_16BIT 后续扩展
        return mAudioFormat == AudioFormat.ENCODING_PCM_16BIT ? 16 : 16;
    }

    private void sendPlayOrPauseMessage(boolean play) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = play ? MSG_PLAY : MSG_PAUSE;
            mHandler.sendMessage(message);
        }
    }

    private void sendPrepareState(boolean ready) {
        if (mHandler != null) {
            Message message = new Message();
            message.what = MSG_PREPARE;
            message.arg1 = ready ? 1 : 0;
            mHandler.sendMessage(message);
        }
    }

    @WorkerThread
    private boolean makeSureInputData() {
        if (mAudioData == null || mAudioData.length == 0) {
            if (mAudioInputDuration > 0) {
                //如果指定使用空背景 则以时间反推
                mAudioData = CoolUtil.timeToByte(mAudioInputDuration * 1000, mChannelNum, mSampleRateInHz, getAudioEncodingBit());
            } else if (CoolUtil.isAudioExist(mAudioPath)) {
                mAudioData = CoolUtil.getAudioByteArray(mAudioPath);
            }
        }
        return mAudioData != null && mAudioData.length > 0;
    }

    private void buildAudioTrack() {
        pauseInner();
        stopInner();
        releaseInner();
        mBufferSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRateInHz,
                mChannelConfig, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);
    }

    private void pauseInner() {
        if (mAudioTrack != null) {
            if (!hasInit()) return;
            try {
                mAudioTrack.pause();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void playInner() {
        if (mAudioTrack != null) {
            if (!hasInit()) return;
            try {
                mAudioTrack.play();
            } catch (Exception e) {
                //Log.e(TAG, e);
            }
        }
    }

    private void stopInner() {
        if (mAudioTrack != null) {
            if (!hasInit()) return;
            try {
                mAudioTrack.stop();
            } catch (Exception e) {
                //Log.e(TAG, e);
            }
        }
    }

    private void releaseInner() {
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    @WorkerThread
    private boolean write(@NonNull byte[] result) {
        if (mAudioTrack == null) return false;
        try {
            return mAudioTrack.write(result, 0, result.length) >= 0;
        } catch (Exception e) {
            //Log.e(TAG, e);
            return false;
        }
    }

    private int getAllFrames() {
        if (mAudioData == null) return 0;
        return mAudioData.length;
    }

    private boolean hasInit() {
        if (mAudioTrack == null) return false;
        return mAudioTrack.getState() == AudioTrack.STATE_INITIALIZED;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class Builder {
        private String audioPath;
        private int duration = -1;
        private boolean isStereo = true;
        private int sampleRate = 44100;
        private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        private boolean loop = true;
        private boolean autoPlay = true;

        private CoolEditorListener listener;

        /**
         * 通过录音文件创建,整个音轨的长度以这个为准
         *
         * @param audioPath String
         */
        public Builder(String audioPath) {
            this.audioPath = audioPath;
        }

        /**
         * 创建指定时长的空音轨,默认为-1 表示不创建空音轨
         *
         * @param duration s 为了控制最小长度，以秒为单位
         */
        public Builder(int duration) {
            this.duration = duration;
        }

        public Builder isStereo(boolean stereo) {
            this.isStereo = stereo;
            return this;
        }

        public Builder sampleRate(int rate) {
            this.sampleRate = rate;
            return this;
        }

        /**
         * 目前仅仅支持 ENCODING_PCM_16BIT
         *
         * @param format 详细请参考{@link AudioFormat#ENCODING_PCM_16BIT}
         * @return this
         */
        public Builder audioFormat(int format) {
            this.audioFormat = format;
            return this;
        }

        public Builder loop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public Builder autoPlay(boolean auto) {
            this.autoPlay = auto;
            return this;
        }

        public Builder listener(CoolEditorListener listener) {
            this.listener = listener;
            return this;
        }

        public IEditor build() {
            return new CoolEditor(this);
        }
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
}
