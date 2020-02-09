package engineer.echo.easyapi.job

import engineer.echo.easyapi.State

interface JobCallback {

    fun onJobState(state: State = State.Idle, total: Long = 0, current: Long = 0, progress: Int = 0)
}