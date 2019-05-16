package engineer.echo.study

import android.os.Bundle
import engineer.echo.oneactivity.core.MasterCompatActivity
import engineer.echo.oneactivity.core.FragmentMaster
import engineer.echo.oneactivity.core.Request


class MainActivity : MasterCompatActivity() {

    private lateinit var mMaster: FragmentMaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMaster = fragmentMaster
        mMaster.install(R.id.app_main_container, Request(MainFragment::class.java), true)
    }
}
