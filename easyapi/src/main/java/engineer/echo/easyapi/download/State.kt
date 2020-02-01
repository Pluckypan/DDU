package engineer.echo.easyapi.download

enum class State {
    Idle,
    OnStart,
    OnProgress,
    OnFinish,
    OnFail,
    OnCancel
}