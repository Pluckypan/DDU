package engineer.echo.easyapi.annotation;

public interface JobCallback {
    void onJobState(State state, long total, long current, int progress);
}
