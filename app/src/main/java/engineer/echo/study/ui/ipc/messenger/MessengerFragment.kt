package engineer.echo.study.ui.ipc.messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import engineer.echo.imessenger.send.SendManager.Companion.KEY_FOR_MESSENGER_MEM
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.App
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.cmpts.MMKVUtils
import engineer.echo.study.cmpts.bottomIn
import engineer.echo.study.cmpts.bottomOut
import engineer.echo.study.databinding.MessengerBinding
import engineer.echo.study.ui.ipc.messenger.ReceiverService.Companion.KEY_REPLY

/**
 *  MessengerFragment.kt
 *  Info: Messenger跨进程通信
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/11 - 9:38 AM
 *  more about me: http://www.1991th.com
 */
class MessengerFragment : BaseFragment() {

    companion object {
        private const val TAG = "MessengerFragment"
        private const val KEY_PROCESS = "key_for_process"
        private const val KEY_USER = "key_for_user"
        private const val KEY_FOR_LIVE_BUS = "key_for_livebus"
        fun goto(fragment: MasterFragment) {
            fragment.startFragment(Request(MessengerFragment::class.java).apply {

            })
        }

        fun getPackageName(): String {
            return MMKVUtils.getSharedPreferencesValue(TAG, KEY_PROCESS, App.getApp().packageName)
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
            val mem = it.getString(KEY_FOR_MESSENGER_MEM)
            val cur = mBinding.info ?: "..."
            mBinding.info = "$cur\nReceive($mem):\n}"

        })
        SenderService.observeReply().observe(this, Observer {
            val cur = mBinding.info ?: ""
            val reply = it.getString(KEY_REPLY)
            mBinding.info = "$cur\nReply:$reply"

        })
        mBinding.tvSendMessenger.setOnClickListener {
            SenderService.getSenderManager()?.apply {
                mBinding.info = "Send:\n"
                sendMessage(Bundle().apply {
                    putByteArray(KEY_USER, null)
                })
            }
        }
        getPackageName().also {
            mBinding.etServerProcessApp.setText(it)
        }
        mBinding.tvEditServerMessenger.setOnClickListener {
            mBinding.etServerProcessApp.apply {
                if (translationY == 0f) {
                    bottomOut()
                    MMKVUtils.setSharedPreferences(TAG, KEY_PROCESS, text.toString())
                } else {
                    bottomIn()
                }
            }
        }
        mBinding.tvLivebusMessenger.setOnClickListener {

        }
        LiveEventBus.get().with(KEY_FOR_LIVE_BUS, ByteArray::class.java).observe(this, Observer {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SenderService.register(App.getApp())
    }

    override fun onDestroy() {
        super.onDestroy()
        SenderService.unregister(App.getApp())
    }

    override fun getTitle(): Int {
        return R.string.label_messenger_app
    }
}