package engineer.echo.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.core.MasterFragment

class MainFragment : MasterFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container,false)
    }
}