package engineer.echo.study.ui.arch.coroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import engineer.echo.easylib.formatLog
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.CoroutinesBinding
import kotlinx.coroutines.*

/**
 *  CoroutinesFragment.kt
 *  Info: CoRoutines KotlinX 协程 more like 轻量级线程
 *  https://www.kotlincn.net/docs/reference/coroutines-overview.html
 *  Created by Plucky(plucky@echo.engineer) on 2019/6/25 - 8:35 PM
 *  more about me: http://www.1991th.com
 */
class CoroutinesFragment : BaseFragment(), CoroutinesContract.IView {

    companion object {

        fun goto(fragment: MasterFragment) {
            Request(CoroutinesFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: CoroutinesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<CoroutinesBinding>(
            inflater,
            R.layout.fragment_coroutines,
            container,
            false
        ).also {
            mBinding = it
            mBinding.iView = this
            mBinding.code = "+"
            return it.root
        }
    }

    override fun onHelloClick() = runBlocking {
        mBinding.let {
            GlobalScope.launch {
                it.code += "\npre:${Thread.currentThread().name}"
                delay(3456)
                it.code += "\naft:${Thread.currentThread().name}\ncoroutines"
            }
            it.code = "runBlocking Hello.${Thread.currentThread().name}"
        }
    }


    // 错误的示范 ❌
    fun signinWith() = runBlocking {
        "signinWith %s".formatLog("Coroutines", Thread.currentThread().name)
        val token = getToken()
        val user = getUserInfo(token)
        setText(user)
    }

    fun setText(user: String) = GlobalScope.launch {
        "setText %s".formatLog("Coroutines", Thread.currentThread().name)
        mBinding.code = user
    }

    suspend fun getToken() = GlobalScope.async {
        "getToken %s".formatLog("Coroutines", Thread.currentThread().name)
        Thread.sleep(1000)
        "Token=AK47"
    }.await()

    suspend fun getUserInfo(token: String) = GlobalScope.async {
        "getUserInfo %s".formatLog("Coroutines", Thread.currentThread().name)
        Thread.sleep(600)
        "User(name=Plucky,age=100,$token)"
    }.await()


    override fun onHiClick() {
        signinWith()
    }

    private fun test() = runBlocking {
        GlobalScope.launch {
            "test() runBlocking %s".formatLog("Coroutines", Thread.currentThread().name)
            val ip = Helper.getLocation()?.getQueryLocation() ?: "beijing"
            val weather = Helper.getWeather(ip)
            Thread.sleep(2345)
            "test() launch %s".formatLog("Coroutines", Thread.currentThread().name)
            mBinding.code = weather?.getWeather("xxx")
        }
    }


    override fun onWeatherClick() {
        test()
    }

    override fun getTitle(): Int {
        return R.string.label_coroutines_arch
    }
}