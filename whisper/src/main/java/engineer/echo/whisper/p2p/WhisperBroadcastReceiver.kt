package engineer.echo.whisper.p2p

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

abstract class WhisperBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION = "engineer.echo.whisper.server.action"
        private const val KEY_DATA = "key_for_receive_data"

        fun register(context: Context, receiver: WhisperBroadcastReceiver) {
            context.registerReceiver(receiver, IntentFilter(ACTION))
        }

        fun unregister(context: Context, receiver: WhisperBroadcastReceiver) {
            context.unregisterReceiver(receiver)
        }

        fun sendData(context: Context, bytes: ByteArray) {
            Intent(ACTION).apply {
                putExtra(KEY_DATA, bytes)
            }.also {
                context.sendBroadcast(it)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            onReceiveData(it.getByteArrayExtra(KEY_DATA))
        }
    }

    abstract fun onReceiveData(bytes: ByteArray?)
}