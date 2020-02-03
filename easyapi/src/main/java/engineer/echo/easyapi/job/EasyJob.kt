package engineer.echo.easyapi.job

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EasyJob(val group: String = "engineer.echo.easyapi", val module: String = "job") {

}