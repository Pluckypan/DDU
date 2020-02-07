package engineer.echo.study.cmpts.audio.extension;


import androidx.annotation.WorkerThread;

/**
 * IExtension.java
 * Info: 用于按顺序处理每一音频帧
 */
public interface IExtension {

    /**
     * @param index      当前数据帧的位置(基于PCM源)
     * @param bufferSize 当前读取块的大小
     * @param current    栈顶的帧数据
     * @return 经过该切面处理的帧数据
     */
    @WorkerThread
    byte[] handleFrame(int index, int bufferSize, byte[] current);
}
