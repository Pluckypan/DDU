package engineer.echo.study.ui.arch.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import engineer.echo.easylib.formatLog
import engineer.echo.easylib.printLine
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.databinding.RxjavaBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random

/**
 *  RxJavaFragment.kt
 *  Info: RxJavaFragment.kt
 *  Created by Plucky(plucky@echo.engineer) on 2019/7/12 - 7:20 PM
 *  more about me: http://www.1991th.com
 */
class RxJavaFragment : BaseFragment(), RxJavaView {

    companion object {
        private const val TAG = "RxJavaFragment"
        fun goto(fragment: BaseFragment) {
            Request(RxJavaFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: RxjavaBinding

    override fun getTitle(): Int {
        return R.string.label_rxjava_arch
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rxjava, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.iView = this
    }

    @SuppressLint("CheckResult")
    override fun onToListClick(view: View) {
        Observable.create<String> {
            it.onNext(TAG)
            it.onComplete()
        }.flatMap {
            if (TAG.isNotEmpty()) {
                Observable.just(1, 2, 3, 4, 5, 6)
            } else {
                Observable.error(Exception("empty"))
            }
        }.flatMap {
            Observable.just((it * 10).toString())
        }.flatMap {
            Observable.just("$it-${Thread.currentThread().id}")
        }.toList().toObservable().subscribe({
            "onErrorReturnClick success size=%s".formatLog(TAG, it.size)
        }, {
            "onErrorReturnClick error %s".formatLog(TAG, it.message)
        })
    }

    @SuppressLint("CheckResult")
    override fun onErrorReturnClick(view: View) {
        Observable.just(1, 2, 3, 4, 5, 6, 7).flatMap {
            if (it % 3 == 0) {
                Observable.error(Exception(""))
            } else {
                Observable.just(it)
            }.onErrorReturnItem(10010)
        }.onErrorReturnItem(10086).subscribe({
            "onErrorReturnClick success size=%s".formatLog(TAG, it)
        }, {
            "onErrorReturnClick error %s".formatLog(TAG, it.message)
        })
    }

    @SuppressLint("CheckResult")
    override fun onDownloadClick(view: View) {
        Observable.just("Alan Walker").flatMap {
            val arr = it.toCharArray().toTypedArray()
            Observable.fromArray(*arr)
        }.flatMap {
            val t = Random.nextInt(1000)
            Thread.sleep(t*1L)
            Observable.just(it.plus("#$t"))
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                "Download doOnSubscribe".printLine(TAG)
            }
            .doOnComplete {
                "Download doOnComplete".printLine(TAG)
            }
            .doOnDispose {
                "Download doOnDispose".printLine(TAG)
            }
            .doOnTerminate {
                "Download doOnTerminate".printLine(TAG)
            }
            .subscribe({
                "Downloading %s".formatLog(TAG, it)
            }, {
                "Downloading error %s".formatLog(TAG, it.message)
            })
    }
}