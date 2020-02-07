package engineer.echo.study.cmpts.audio.helper;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import engineer.echo.study.cmpts.audio.CoolCode;

/**
 * CoolUtil.java
 * Info: CoolEditor工具类
 */
public class CoolUtil {

    public static final String TAG = CoolCode.TAG;

    /**
     * 将文件byte大小转换为 时间长度
     * 16bit 2Byte
     * 双声道 需要除以2
     *
     * @param byteLength     byte PCM 文件的 byte 数组
     * @param channelNum     单声道 1;双声道 2
     * @param sampleRateInHz 采样率 44100;16000
     * @param encodingPcmBit 位深 16bit 8bit
     * @return ms
     */
    public static float byteToTime(float byteLength,
                                   int channelNum,
                                   int sampleRateInHz,
                                   int encodingPcmBit) {
        float divider = channelNum == 2 ? 2f : 1f;
        return byteLength * 1000L / sampleRateInHz / (encodingPcmBit / 8f) / divider;
    }

    /**
     * 创建指定时长的空音频
     *
     * @param time           ms
     * @param channelNum     单声道 1;双声道 2
     * @param sampleRateInHz 采样率 44100;16000
     * @param encodingPcmBit 位深 16bit 8bit
     * @return byte[]
     */
    public static byte[] timeToByte(int time,
                                    int channelNum,
                                    int sampleRateInHz,
                                    int encodingPcmBit) {
        float divider = channelNum == 2 ? 2f : 1f;
        int length = Math.round(time * divider * (encodingPcmBit / 8f) * sampleRateInHz / 1000);
        return new byte[length];
    }

    /**
     * 将音频流保存为文件
     *
     * @param data     byte[]
     * @param filePath String
     * @return boolean
     */
    public static boolean byteToFile(byte[] data, String filePath) {
        if (data == null) {
            return false;
        }
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data, 0, data.length);
            os.flush();
        } catch (IOException e) {
            Log.e(TAG, "byteToFile write error.");
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, "byteToFile close error.");
                }
            }
        }
        return true;
    }

    /**
     * 将音频文件转换为 byte[]
     *
     * @param filePath 文件路径
     * @return byte[]
     */
    public static byte[] getAudioByteArray(String filePath) {
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(new FileInputStream(filePath));
            return toByteArray(inputStream);
        } catch (Exception e) {
            return null;
        } finally {
            closeInputStream(inputStream);
        }
    }

    /**
     * 从 InputStream 读取 byte[]
     *
     * @param input InputStream
     * @return byte[]
     * @throws IOException e
     */
    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 释放资源
     *
     * @param input InputStream
     */
    private static void closeInputStream(InputStream input) {
        if (input == null) return;
        try {
            input.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static boolean isAudioExist(String audioPath) {
        return !TextUtils.isEmpty(audioPath) && new File(audioPath).exists();
    }

    /**
     * 读取 byte[] 区间
     *
     * @param audioByteArray 待读取完整的 byte[]
     * @param offset         相对于起始点的位置
     * @param count          读取的长度
     * @return byte[]
     */
    public static byte[] copyRange(byte[] audioByteArray, int offset, int count) {
        if (audioByteArray == null) {
            return null;
        }
        try {
            int total = audioByteArray.length;
            int start = offset;
            int end = start + count;
            if (start > total) start = total;
            if (end > total) end = total;
            return Arrays.copyOfRange(audioByteArray, start, end);
        } catch (Exception e) {
            Log.e(TAG, "copyRange error.");
        }
        return null;
    }
}
