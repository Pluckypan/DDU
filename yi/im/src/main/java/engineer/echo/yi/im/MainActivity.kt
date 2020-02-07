package engineer.echo.yi.im

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        fun goto(context: Context) {
            Intent(context, MainActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}
