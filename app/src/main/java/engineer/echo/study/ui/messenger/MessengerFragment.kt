package engineer.echo.study.ui.messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.App
import engineer.echo.study.C
import engineer.echo.study.C.Companion.toUser
import engineer.echo.study.R
import engineer.echo.study.databinding.MessengerBinding

/**
 *  MessengerFragment.kt
 *  Info: Messenger跨进程通信
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/11 - 9:38 AM
 *  more about me: http://www.1991th.com
 */
@Configuration(theme = R.style.Theme_AppCompat_Light)
class MessengerFragment : MasterFragment() {

    companion object {
        private const val KEY_USER = "key_for_user"
        fun goto(fragment: MasterFragment) {
            fragment.startFragment(Request(MessengerFragment::class.java).apply {

            })
        }
    }

    private lateinit var mBinding: MessengerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_messenger, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ReceiverService.observeReceive().observe(this, Observer {
            val user = it.getByteArray(KEY_USER)?.toUser()
            mBinding.info = "Receive:\n${user?.toString() ?: "failed"}"

        })
        SenderService.observeReply().observe(this, Observer {

        })
        mBinding.tvSendMessenger.setOnClickListener {
            SenderService.getSenderManager()?.apply {
                val user = C.newUser()
                mBinding.info = "Send:\n$user"
                sendMessage(Bundle().apply {
                    putByteArray(KEY_USER, user.toByteArray())
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SenderService.register(App.getApp())
    }

    override fun onDestroy() {
        super.onDestroy()
        SenderService.unregister(App.getApp())
    }
}