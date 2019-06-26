package engineer.echo.study.ui.costom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment

class RecyclerViewFragment : BaseFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(RecyclerViewFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun getTitle(): Int {
        return R.string.label_recyclerview_app
    }
}