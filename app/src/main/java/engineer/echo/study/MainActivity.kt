package engineer.echo.study

import android.os.Bundle
import engineer.echo.oneactivity.core.FragmentMaster
import engineer.echo.oneactivity.core.IMasterFragment
import engineer.echo.oneactivity.core.MasterCompatActivity
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.ui.ipc.messenger.ReceiverService
import engineer.echo.study.ui.main.MainFragment


class MainActivity : MasterCompatActivity(), FragmentMaster.FragmentLifecycleCallbacks {

    private lateinit var mMaster: FragmentMaster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMaster = fragmentMaster
        mMaster.install(R.id.app_main_container, Request(MainFragment::class.java), true)
        mMaster.registerFragmentLifecycleCallbacks(this)
        ReceiverService.start(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMaster.unregisterFragmentLifecycleCallbacks(this)
        ReceiverService.stop(this)
    }

    override fun onFragmentViewCreated(fragment: IMasterFragment?) {

    }

    override fun onFragmentStopped(fragment: IMasterFragment?) {

    }

    override fun onFragmentCreated(fragment: IMasterFragment?, savedInstanceState: Bundle?) {

    }

    override fun onFragmentResumed(fragment: IMasterFragment?) {

    }

    override fun onFragmentAttached(fragment: IMasterFragment?) {

    }

    override fun onFragmentDestroyed(fragment: IMasterFragment?) {

    }

    override fun onFragmentSaveInstanceState(fragment: IMasterFragment?, outState: Bundle?) {

    }

    override fun onFragmentStarted(fragment: IMasterFragment?) {

    }

    override fun onFragmentActivated(fragment: IMasterFragment?) {
        if (fragment is BaseFragment) {
            setTitle(fragment.getTitle())
        }
    }

    override fun onFragmentPaused(fragment: IMasterFragment?) {

    }

    override fun onFragmentDetached(fragment: IMasterFragment?) {

    }

    override fun onFragmentDeactivated(fragment: IMasterFragment?) {

    }
}
