package engineer.echo.study.cmpts.audio.callback;


import engineer.echo.study.cmpts.audio.CoolCode;

/**
 * CoolEditorListener.java
 * Info: CoolEditor处理接口
 */
public interface CoolEditorListener {

    /**
     * 进度回调
     *
     * @param percent 百分比 如 0.3
     * @param current 当前时间 ms
     * @param total   总时间 ms
     */
    void onProgressChanged(float percent, float current, float total);

    /**
     * 回调当前播放状态
     *
     * @param playState {@link CoolCode.Signal}
     */
    void onPlayStateChanged(@CoolCode.Signal int playState);

    /**
     * 回调基准音轨 音频解析 状态
     */
    void onPrepareState(boolean ready);
}
