package androidx.core.app

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

open class GodActivity : AppCompatActivity() {

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        // KEYCODE_POWER KEYCODE_VOLUME_DOWN
        Log.i("Plucky", "dispatchKeyEvent $event")
        return super.dispatchKeyEvent(event)
    }

    override fun onStart() {
        super.onStart()
        ScreenshotContentObserver.startObserve()
        Log.i("Plucky", "onStart")
    }

    override fun onStop() {
        super.onStop()
        ScreenshotContentObserver.stopObserve()
        Log.i("Plucky", "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Plucky", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Plucky", "onResume")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GodReceiver.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        GodReceiver.unregister(this)
    }
}