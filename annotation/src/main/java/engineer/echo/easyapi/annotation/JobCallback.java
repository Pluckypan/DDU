package engineer.echo.easyapi.annotation;

public interface JobCallback {
    void onJobState(State state, int total, int current, int progress);
}
