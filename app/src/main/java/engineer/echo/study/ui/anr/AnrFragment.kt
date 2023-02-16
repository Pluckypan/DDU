package engineer.echo.study.ui.anr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import engineer.echo.easylib.forbidScreen
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.cmpts.apm.AnrWatchDog
import engineer.echo.study.cmpts.apm.FpsHelper
import engineer.echo.study.databinding.FragmentAnrBinding

class AnrFragment : BaseFragment() {

    private var binding: FragmentAnrBinding? = null
    private val anrWatchDog by lazy {
        AnrWatchDog()
    }

    private val fpsHelper by lazy {
        FpsHelper()
    }

    override fun getTitle(): Int {
        return R.string.label_anr
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAnrBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.tvStop?.performClick()
        binding?.tvStopFps?.performClick()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tvStart?.setOnClickListener {
            anrWatchDog.start()
        }
        binding?.tvStop?.setOnClickListener {
            anrWatchDog.stop()
        }
        binding?.tvStartFps?.setOnClickListener {
            fpsHelper.start()
        }
        binding?.tvStopFps?.setOnClickListener {
            fpsHelper.stop()
        }
        binding?.tvScreenShot?.setOnClickListener {
            it.isSelected = !it.isSelected
            binding?.tvScreenShot?.text = if (it.isSelected) {
                "禁止截屏"
            } else {
                "允许截屏"
            }
            activity?.window?.forbidScreen(it.isSelected)
        }
    }

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(AnrFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }
}