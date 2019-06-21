package engineer.echo.study.ui.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.googlecode.protobuf.format.JsonFormat
import engineer.echo.easylib.alphaVisible
import engineer.echo.easyprinter.CommandBox.toPrintByte
import engineer.echo.easyprinter.Config
import engineer.echo.easyprinter.Config.Companion.bondState
import engineer.echo.easyprinter.Config.Companion.connectionState
import engineer.echo.easyprinter.Config.Companion.localState
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.oneactivity.annotation.Configuration
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.study.cmpts.bottomIn
import engineer.echo.study.cmpts.bottomOut
import engineer.echo.study.databinding.PrinterBinding
import java.nio.charset.Charset

@Configuration(theme = R.style.Theme_AppCompat_Light)
class PrinterFragment : MasterFragment(), PrinterContract.IView {

    companion object {
        fun goto(fragment: MasterFragment) {
            Request(PrinterFragment::class.java).also {
                fragment.startFragment(it)
            }
        }

        fun BluetoothDevice.majorIcon(): Int {
            return when (bluetoothClass.majorDeviceClass) {
                BluetoothClass.Device.Major.IMAGING -> R.string.iconPrinter
                BluetoothClass.Device.Major.PHONE -> R.string.iconMobile
                BluetoothClass.Device.Major.COMPUTER -> R.string.iconLaptop
                else -> R.string.iconBluetooth
            }
        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("deviceShow")
        @JvmStatic
        fun onDeviceSelectShow(textView: TextView, device: BluetoothDevice?) {
            device?.apply {
                val hint = if (bluetoothClass.majorDeviceClass != BluetoothClass.Device.Major.IMAGING) {
                    "Not a Printer"
                } else {
                    ""
                }
                textView.text = "Name=%s\nAddress=%s\n%s".format(name, address, hint)
            }
        }
    }

    private lateinit var mBinding: PrinterBinding
    private val mAdapter = DeviceListAdapter(this)
    private lateinit var mViewModel: PrinterContract.IVModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(PrinterViewModel::class.java)
        mViewModel.onCreate(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_printer, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            code = "Welcome to EasyPrinter.\nUUID=${Config.UNIQUE_ID}"
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
                if (layoutActionPrinter.alphaVisible()) {
                    layoutActionPrinter.bottomOut()
                    return@setOnClickListener
                }
                layoutPopupPrinterApp.bottomOut()
            }
            tvClearPrinter.setOnClickListener {
                syntaxProtobufApp.code.text.clear()
            }
            rcvDevicePrinter.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }

            tvPrintPrinter.setOnClickListener {
                if (EasyPrinter.get().isDiscovering()) {
                    EasyPrinter.get().cancelDiscovery()
                    appendCode("cancelDiscovery First.")
                    return@setOnClickListener
                }
                device?.apply {
                    val user = C.newUser("Printer打印机")
                    mBinding.code = JsonFormat.printToString(user).also {
                        EasyPrinter.get().startPrintTask(this, it.toByteArray(Charset.forName("GB2312")))
                    }
                }
            }
            tvPrintBitmapPrinter.setOnClickListener {
                device?.apply {
                    resources.getDrawable(R.drawable.android).toBitmap().toPrintByte().also {
                        EasyPrinter.get().startPrintTask(this, it)
                    }
                }
            }
            tvConnectPrinter.setOnClickListener {
                if (EasyPrinter.get().isDiscovering()) {
                    EasyPrinter.get().cancelDiscovery()
                    appendCode("cancelDiscovery First.")
                    return@setOnClickListener
                }
                device?.apply {
                    if (!EasyPrinter.get().isConnected(this)) {
                        EasyPrinter.get().startConnectTask(this)
                    } else {
                        appendCode("Printer is connected.")
                    }
                }
            }
        }

        mViewModel.observeDeviceList(this, Observer {
            mAdapter.setList(it)
        })
        EasyPrinter.get().observeDiscovery(this, Observer {
            appendCode(it.state.state)
        })
        EasyPrinter.get().observeBond(this, Observer {
            appendCode("${it.state.bondState()}\nbondDevice=\n${it.device?.address}\n-${it.device?.name}")
        })
        EasyPrinter.get().observeConnection(this, Observer {
            appendCode("${it.state.connectionState()}\nconnectDevice:\n${it.device?.address}-${it.device?.name}")
        })
        EasyPrinter.get().observeLocalState(this, Observer {
            appendCode(it.state.localState())
        })
    }

    private fun appendCode(content: String) {
        if (mBinding.syntaxProtobufApp.code.lineCount > 25) {
            mBinding.code = content
        } else {
            val before = mBinding.code
            mBinding.code = "$before\n$content"
        }
    }

    override fun onDestroy() {
        if (EasyPrinter.get().isDiscovering()) {
            EasyPrinter.get().cancelDiscovery()
        }
        EasyPrinter.get().stopAllTask()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (mBinding.layoutActionPrinter.alphaVisible()) {
            mBinding.layoutActionPrinter.bottomOut()
            return
        }
        if (mBinding.layoutPopupPrinterApp.alphaVisible()) {
            mBinding.layoutPopupPrinterApp.bottomOut()
            return
        }
        super.onBackPressed()

    }

    override fun onDeviceItemClick(device: BluetoothDevice?) {
        device?.let {
            if (EasyPrinter.get().isBonded(it)) {
                mBinding.device = it
                mBinding.layoutActionPrinter.bottomIn()
            } else {
                if (!EasyPrinter.get().isBonding(it)) {
                    EasyPrinter.get().createBondTo(it)
                }
            }
        }
    }
}