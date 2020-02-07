package engineer.echo.study.cmpts.audio;

/**
 * IEditor.java
 * Info: CoolEditor对外接口
 */
public interface IEditor {

    void play();

    void pause();

    /**
     * 定位至百分比位置
     *
     * @param progress (0.0f~1.0f)
     */
    void seekTo(float progress);

    /**
     * 释放所有的资源
     */
    void release();

    /**
     * 基准音轨是否就绪
     *
     * @return boolean
     */
    boolean hasReady();

    /**
     * 基准音轨时长
     *
     * @return ms
     */
    long getDuration();

    /**
     * 是否处于 播放状态 (播放信号)
     *
     * @return boolean
     */
    boolean isPlaying();
}
