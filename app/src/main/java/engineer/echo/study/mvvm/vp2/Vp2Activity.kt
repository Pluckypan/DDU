package engineer.echo.study.mvvm.vp2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import engineer.echo.study.R
import engineer.echo.study.databinding.ActivityVp2Binding
import engineer.echo.study.mvvm.vp2.fragment.SimpleFragment
import kotlin.math.abs
import kotlin.math.min

class Vp2Activity : AppCompatActivity(), Vp2Contract.IView {

    private lateinit var viewModel: Vp2Contract.IViewModel
    private lateinit var binding: ActivityVp2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            Vp2ViewModel.Factory(intent.extras, application)
        ).get(Vp2ViewModel::class.java)

        title = "日本传统色"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_vp2)
        (binding.viewPager.getChildAt(0) as? RecyclerView)?.let {
            it.setPadding(180, 120, 180, 120)
            it.clipToPadding = false
        }
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = DATA.size

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0, 1, 2 -> SimpleFragment.newInstance(
                        DATA[position].first,
                        DATA[position].second
                    )
                    else -> Fragment()
                }
            }

        }
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.setCurrentItem(DATA.size - 2, false)
        binding.viewPager.setPageTransformer(CompositePageTransformer().also {
            it.addTransformer(MarginPageTransformer(60))
            it.addTransformer(CardPageTransformer())
        })


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab: TabLayout.Tab, i: Int ->
            tab.text = DATA[i].first
        }.attach()

        binding.apply {
            lifecycleOwner = this@Vp2Activity
            view = this@Vp2Activity
            viewModel = this@Vp2Activity.viewModel
        }
    }

    private class CardPageTransformer : ViewPager2.PageTransformer {

        override fun transformPage(page: View, position: Float) {
            val absPos = abs(position)
            val scale = (1.0f - absPos * 0.1f)
            val alphaRatio = (1.0f - absPos * 0.5f)
            page.apply {
                scaleX = scale
                scaleY = scale
                alpha = alphaRatio
            }
        }

    }

    override fun toast(view: View) {
        Toast.makeText(this, "Hello EasyMVVM.${view.javaClass.simpleName}", Toast.LENGTH_LONG)
            .show()
    }

    companion object {

        private val DATA = arrayListOf(
            "素鼠" to Color.parseColor("#787D7B"),
            "胡桃" to Color.parseColor("#947A6D"),
            "老竹" to Color.parseColor("#6A8372")
        )

        @BindingAdapter("easyJobData")
        @JvmStatic
        fun onBindJob(textView: TextView, job: Long?) {
            textView.text = if (job != null) "EasyJob $job" else "Easy MVVM"
        }

        fun goto(activity: Activity) {
            Intent(activity, Vp2Activity::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}
