package engineer.echo.study.cmpts.audio.helper;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import engineer.echo.study.cmpts.audio.callback.CoolEditorListener;

import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PAUSE;
import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PLAY;
import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PLAY_PROGRESS;
import static engineer.echo.study.cmpts.audio.CoolCode.MSG_PREPARE;
import static engineer.echo.study.cmpts.audio.CoolCode.PAUSE;
import static engineer.echo.study.cmpts.audio.CoolCode.PLAY;

/**
 * ProgressHandler.java
 * Info: 处理CoolEditor状态回调
 */
public class ProgressHandler extends Handler {
    private WeakReference<CoolEditorListener> mListener;

    public ProgressHandler(CoolEditorListener listener) {
        super();
        mListener = new WeakReference<>(listener);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mListener != null && mListener.get() != null) {
            switch (msg.what) {
                case MSG_PLAY_PROGRESS:
                    if (msg.obj instanceof float[]) {
                        float[] data = (float[]) msg.obj;
                        mListener.get().onProgressChanged(data[0], data[1], data[2]);
                    }
                    break;
                case MSG_PLAY:
                    mListener.get().onPlayStateChanged(PLAY);
                    break;
                case MSG_PAUSE:
                    mListener.get().onPlayStateChanged(PAUSE);
                    break;
                case MSG_PREPARE:
                    mListener.get().onPrepareState(msg.arg1 == 1);
                    break;
            }
        }
    }
}
