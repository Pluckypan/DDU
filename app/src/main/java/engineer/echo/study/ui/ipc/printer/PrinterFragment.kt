package engineer.echo.study.ui.ipc.printer

import android.annotation.SuppressLint
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.pm.ActivityInfo
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.googlecode.protobuf.format.JsonFormat
import engineer.echo.easylib.alphaVisible
import engineer.echo.easyprinter.Config
import engineer.echo.easyprinter.Config.Companion.bondState
import engineer.echo.easyprinter.Config.Companion.connectionState
import engineer.echo.easyprinter.Config.Companion.localState
import engineer.echo.easyprinter.EasyPrinter
import engineer.echo.easyprinter.command.CommandBox.epsonQrcode
import engineer.echo.easyprinter.command.CommandBox.toBarcode
import engineer.echo.easyprinter.command.CommandBox.toPrintByte
import engineer.echo.oneactivity.core.MasterFragment
import engineer.echo.oneactivity.core.Request
import engineer.echo.study.C
import engineer.echo.study.R
import engineer.echo.study.cmpts.BaseFragment
import engineer.echo.study.cmpts.bottomIn
import engineer.echo.study.cmpts.bottomOut
import engineer.echo.study.databinding.PrinterBinding
import engineer.echo.study.ui.ipc.printer.Bill.Companion.toBytes
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class PrinterFragment : BaseFragment(), PrinterContract.IView {

    companion object {
        private val FMT_DATE = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
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
                val hint =
                    if (bluetoothClass.majorDeviceClass != BluetoothClass.Device.Major.IMAGING) {
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
        mViewModel = ViewModelProvider(this).get(PrinterViewModel::class.java)
        mViewModel.onCreate(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                print {
                    val user = C.newUser("Printer打印机")
                    mBinding.code = JsonFormat.printToString(user)
                    mBinding.code!!.toByteArray(Charset.forName("GB2312"))
                }
            }
            tvPrintBitmapPrinter.setOnClickListener {
                print {
                    resources.getDrawable(R.drawable.android).toBitmap().toPrintByte()
                }
            }
            tvPrintBarcodePrinter.setOnClickListener {
                print {
                    "HH20190621200".toBarcode()
                }
            }
            tvPrintQrcodePrinter.setOnClickListener {
                print {
                    "http://www.echo.engineer".epsonQrcode()
                }
            }
            tvPrintOrderPrinter.setOnClickListener {
                print {
                    val logo = resources.getDrawable(R.drawable.android).toBitmap()
                    Bill(
                        logo,
                        "红火烧烤",
                        "利郎分店",
                        "0x201906222145",
                        FMT_DATE.format(Date()),
                        "13107800917",
                        "晋江洪山创业园202号",
                        "谢谢惠顾,欢迎再次光临!",
                        arrayListOf(
                            Product("鸡腿", 6.0f, 3),
                            Product("鸡翅", 8.0f, 1),
                            Product("生蚝", 10.0f, 2),
                            Product("骨肉相连", 10.0f, 5),
                            Product("雪津啤酒", 3.0f, 12),
                            Product("大瓶可乐", 6.0f, 1),
                            Product("灌装百威", 4.0f, 4)
                        )
                    ).toBytes()
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
            tvShowTablePrinter.setOnClickListener {
                printerViewApp.bottomIn()
            }
            tvPrintTablePrinter.setOnClickListener {
                if (EasyPrinter.get().isDiscovering()) {
                    EasyPrinter.get().cancelDiscovery()
                    appendCode("cancelDiscovery First.")
                    return@setOnClickListener
                }
                device?.apply {
                    printerViewApp.getTablePrinter().print(this, maxWidth = 400)
                }
            }
            tvOrientationPrinter.setOnClickListener {
                if (resources.configuration.orientation == ORIENTATION_PORTRAIT) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }
            tvViewPrinter.setOnClickListener {
                if (EasyPrinter.get().isDiscovering()) {
                    EasyPrinter.get().cancelDiscovery()
                    appendCode("cancelDiscovery First.")
                    return@setOnClickListener
                }
                device?.apply {
                    printerViewApp.print(this)
                }
            }
            printerViewApp.setOnClickListener {
                if (printerViewApp.getOrientation() == 90f) {
                    printerViewApp.setOrientation(0f)
                } else {
                    printerViewApp.setOrientation(90f)
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
        mBinding.apply {
            if (printerViewApp.alphaVisible()) {
                printerViewApp.bottomOut()
                return
            }
            if (layoutActionPrinter.alphaVisible()) {
                layoutActionPrinter.bottomOut()
                return
            }
            if (layoutPopupPrinterApp.alphaVisible()) {
                layoutPopupPrinterApp.bottomOut()
                return
            }
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

    private fun print(action: (() -> ByteArray)) {
        if (EasyPrinter.get().isDiscovering()) {
            EasyPrinter.get().cancelDiscovery()
            appendCode("cancelDiscovery First.")
            return
        }
        mBinding.device?.apply {
            action.invoke()
            EasyPrinter.get().startPrintTask(this, action.invoke())
        }
    }

    override fun getTitle(): Int {
        return R.string.label_bluetooth_printer_app
    }
}