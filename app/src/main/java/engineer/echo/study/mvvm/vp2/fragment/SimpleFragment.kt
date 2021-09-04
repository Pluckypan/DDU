package engineer.echo.study.mvvm.vp2.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import engineer.echo.study.databinding.FragmentSimpleBinding

class SimpleFragment : Fragment() {

    private var color: Int = Color.DKGRAY
    private lateinit var title: String
    private var binding: FragmentSimpleBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate $title")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy $title")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        title = arguments?.getString(ARGS_TITLE) ?: "NULL"
        color = arguments?.getInt(ARGS_COLOR) ?: Color.DKGRAY
        Log.i(TAG, "onAttach $title")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume $title")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause $title")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView $title")
        return FragmentSimpleBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView $title")
        binding = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i(TAG, "onHiddenChanged $title hidden=$hidden")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.i(TAG, "setUserVisibleHint $title isVisibleToUser=$isVisibleToUser")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated $title")
        binding?.tvSimple?.text = title
        binding?.cvRoot?.setCardBackgroundColor(color)
    }

    companion object {

        private const val ARGS_TITLE = "ARGS_TITLE"
        private const val ARGS_COLOR = "ARGS_COLOR"
        private const val TAG = "SimpleFragment"

        fun newInstance(title: String, color: Int): SimpleFragment {
            return SimpleFragment().also {
                it.arguments = Bundle().apply {
                    putString(ARGS_TITLE, title)
                    putInt(ARGS_COLOR, color)
                }
            }
        }
    }
}