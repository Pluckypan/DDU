package engineer.echo.study.ui.printer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.R
import engineer.echo.study.cmpts.bottomIn
import engineer.echo.study.cmpts.bottomOut
import engineer.echo.study.databinding.PrinterBinding

@Configuration(theme = R.style.Theme_AppCompat_Light)
class PrinterFragment : MasterFragment() {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(PrinterFragment::class.java).also {
                fragment.startFragment(it)
            }
        }
    }

    private lateinit var mBinding: PrinterBinding
    private val mAdapter = DeviceListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_printer, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            code = "Welcome to EasyPrinter."
            tvOpenPrinter.setOnClickListener {
                EasyPrinter.get().enable()
            }
            tvClosePrinter.setOnClickListener {
                EasyPrinter.get().disable()
            }
            tvStartDiscoveryPrinter.setOnClickListener {
                if (!EasyPrinter.get().isDiscovering()) {
                    EasyPrinter.get().startDiscovery()
                }
                layoutPopupPrinterApp.bottomIn()
            }
            tvStopDiscoveryPrinter.setOnClickListener {
                if (EasyPrinter.get().isDiscovering()) {
                    EasyPrinter.get().cancelDiscovery()
                }
            }
            layoutPopupPrinterApp.setOnClickListener {
                layoutPopupPrinterApp.bottomOut()
            }
            tvClearPrinter.setOnClickListener {
                syntaxProtobufApp.code.text.clear()
            }
            rcvDevicePrinter.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        }
    }
}