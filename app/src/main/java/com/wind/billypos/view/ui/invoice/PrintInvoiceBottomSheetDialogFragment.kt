package com.wind.billypos.view.ui.invoice

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.wind.billypos.BR
import com.wind.billypos.BuildConfig
import com.wind.billypos.R
import com.wind.billypos.base.BaseBottomSheetDialogFragment
import com.wind.billypos.databinding.BottomFragmentPrintInvoiceBinding
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.company_nuis
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.invoice.adapter.PrintItemsAdapter
import com.wind.billypos.view.ui.invoice.viewmodel.PrintInvoiceViewModel
import kotlinx.android.synthetic.main.bottom_fragment_print_invoice.*
import net.glxn.qrgen.android.QRCode
import net.glxn.qrgen.core.scheme.Url
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class PrintInvoiceBottomSheetDialogFragment: BaseBottomSheetDialogFragment<BottomFragmentPrintInvoiceBinding, PrintInvoiceViewModel>() {

    private val printInvoiceViewModel: PrintInvoiceViewModel by viewModel()

    private val args: PrintInvoiceBottomSheetDialogFragmentArgs by navArgs()

    private lateinit var syncPref: SharedPreferences

    lateinit var mBlueAdapter: BluetoothAdapter
    private var aAdapter: ArrayAdapter<*>? = null
    private var lstvw: ListView? = null

    private val PERMISSION_BLUETOOTH = 1
    private val REQUEST_ENABLE_BT = 0

    private var transactionHead = TransactionHead()
    private var items = listOf<TransactionBody>()
    private var qrLink = ""

    override val bindingVariable: Int
        get() = BR.printInvoiceViewModel
    override val layoutId: Int
        get() = R.layout.bottom_fragment_print_invoice
    override val viewModel: PrintInvoiceViewModel
        get() = printInvoiceViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        syncPref = PreferenceHelper.customPreference((activity as AppCompatActivity))

//        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()

        val printItemsAdapter = PrintItemsAdapter()

        recyclerViewItemsPrint.adapter = printItemsAdapter

        imgPrintClose.setOnClickListener {
            dismiss()
        }

        // Get paired devices.
        // Get paired devices.
//        val pairedDevices: Set<BluetoothDevice> = mBlueAdapter.bondedDevices
//        if (pairedDevices.size > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (device in pairedDevices) {
//                val deviceName = device.name
//                val deviceHardwareAddress = device.address // MAC address
//            }
//        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
            }
        }



        btnReceiptPrint.setOnClickListener {
            if(mBlueAdapter.isEnabled){
                printBluetooth()
            }else{
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                resultLauncher.launch(intent)
            }
        }

        val observerTransactionHead = Observer<TransactionHead> {
            transactionHead = it
            viewDataBinding?.transactionHead = transactionHead
            viewModel.getItems(trnHeadUUID = transactionHead.uuid!!)
            qrLink = generateQRCodeLink(transactionHead)
            updateQRCode()
            val qrCode = generateQRCodeLink(transactionHead)
            Timber.e("Qr code %s", qrCode)
        }

        val observerItems = Observer<List<TransactionBody>> {
            items = it
            printItemsAdapter.submitList(items)
        }


        viewModel.mutableLiveDataTransactionHead.observe(viewLifecycleOwner, observerTransactionHead)
        viewModel.mutableLiveDataItems.observe(viewLifecycleOwner, observerItems)

        viewModel.setTransactionHead(trnHead = args.transactionHead)

    }

    private fun updateQRCode(){
        val urlToVerify = Url()
        urlToVerify.url = qrLink

        val qrBitmap = QRCode.from(urlToVerify)
            .withSize(225, 225)
            .bitmap()

        imgPrintQR.setImageBitmap(qrBitmap)

        txtVerifyUrl.text = qrLink
    }

//    private fun generateQRCodeLink(trnHead: TransactionHead) : String{
//        return   BuildConfig.BASE_QR_URL + "?iic=" + trnHead.iic +
//                "&tin=" + syncPref.company_nuis +
//                "&crtd=" + trnHead.receiptDateTime +
//                "&ord=" + trnHead.invoiceNo.toString() +
//                "&bu=" + syncPref.configurations.address?.businUnitCode +
//                "&cr=" + syncPref.configurations.device?.tcrCode +
//                "&sw=" + syncPref.configurations.softCode +
//                "&prc=" + trnHead.totalWithVat
//    }

    private fun generateQRCodeLink(trnHead: TransactionHead) : String{
        return   BuildConfig.BASE_QR_URL + "?iic=" + trnHead.iic +
                "&tin=" + "L82210031Q" +
                "&crtd=" + trnHead.receiptDateTime +
                "&ord=" + trnHead.invoiceNo.toString() +
                "&bu=" + "lr991hz335" +
                "&cr=" + "dc769ca240" +
                "&sw=" + "ss123ss123" +
                "&prc=" + trnHead.totalWithVat
    }

    private fun checkPairedDevices(){
        val pairedDevices: Set<BluetoothDevice> = mBlueAdapter.bondedDevices
        val list = mutableListOf<String>()
        for (device in pairedDevices) {
            val devicename = device.name
            val macAddress = device.address
            list.add("Name: " + devicename + "MAC Address: " + macAddress)
        }
        Timber.e("Lista uredjaja %s", list)
//                lstvw = findViewById(R.id.deviceList) as ListView
//                aAdapter = ArrayAdapter<Any?>(
//                    ApplicationProvider.getApplicationContext(),
//                    android.R.layout.simple_list_item_1,
//                    list
//                )
//                lstvw.setAdapter(aAdapter)
    }

    private fun printBluetooth() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(Manifest.permission.BLUETOOTH),
                PERMISSION_BLUETOOTH
            )
            Timber.e("Nema permisiju")
        } else {
            Timber.e("Ima permisiju")
            //printIt80mm()
         printIt()
        }
    }


    fun printIt(){
        val qrCode = generateQRCodeLink(transactionHead)
        Timber.e("Qr code %s", qrCode)
        var orderNumber = 1
        var itemsToPrint = """"""
        items.forEach {
            itemsToPrint += """
        [L]${orderNumber++}  ${it.itemName}
        [L]${it.quantity} x ${it.itemPriceWithDiscount}[R]${it.totalWithVat}
        """
        }
        Timber.e("Item %s", itemsToPrint)
        val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
        var printValue = """
        [L]
        [C]<font size='big'>Tax Invoice</font>
        [L]
        [C]--------------------------------
        [L]
        [L]<b>Seller:</b>[L]Nexia Al Consulting shp
        [L]<b>Address:</b>[L]Filial Qendror
        [L]<b>NUIS:</b>[L]L82210031Q
        [C]================================
        [L]<b>Data/time:</b>[R]17/09/2021; 14:33:46
        [L]<b>Invoice no:</b>[R]125/2021/dc769ca240
        [L]<b>Operator code:</b>[R]dr959ez502
        [L]<b>Business Unit Code:</b>[R]lr991hz335
        [L]<b>Payment method:</b>[R]BANKNOTE
        [C]================================
        [L]<b>No.  Item</b>
        [L]<b>     Quantity X Price</b>
        [C]================================
        """
        printValue += """
        $itemsToPrint
        [L]
        [C]================================
        [L]Before VAT:[R]${transactionHead.total}
        [L]VAT:[R]${transactionHead.totalWithVat}
        [L]
        [R]-------
        [R]<font size='tall'>${transactionHead.totalWithVat}</font>
        [C]================================
        [L]<b>Rate</b>[R]<b>Vat</b>
        [C]--------------------------------
        [L]20%[R]${transactionHead.vatAmount}
        [C]--------------------------------
        [L]<b>NSLF: ${transactionHead.fiscalFIC}</b>
        [L]<b>NIVF: ${transactionHead.uuid}</b>
        [L]
        [C]<qrcode size='30'>${qrCode}</qrcode>
        [C] scan to verify
        [L]
        [C] www.nexia.al
        """
        printer
            .printFormattedTextAndCut(
                printValue.trimIndent()
            )
    }

//    fun printIt(){
//        val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
//        printer
//            .printFormattedTextAndCut(
//                """
//        [L]
//        [C]<font size='big'>Tax Invoice</font>
//        [L]
//        [C]--------------------------------
//        [L]
//        [L]<b>Seller:</b>[L]Nexia Al Consulting shp
//        [L]<b>Address:</b>[L]Filial Qendror
//        [L]<b>NUIS:</b>[L]L82210031Q
//        [C]================================
//        [L]<b>Data/time:</b>[R]17/09/2021; 14:33:46
//        [L]<b>Invoice no:</b>[R]125/2021/dc769ca240
//        [L]<b>Operator code:</b>[R]dr959ez502
//        [L]<b>Business Unit Code:</b>[R]lr991hz335
//        [L]<b>Payment method:</b>[R]BANKNOTE
//        [C]================================
//        [L]<b>No.  Item</b>
//        [L]<b>     Quantity X Price</b>
//        [C]================================
//        [L]
//        [L]<b>1 CocaColaZero2</b>
//        [L]     1.0 x 300.0[R]300.0
//        [L]<b>2 Vizite Neurologjike</b>
//        [L]     2.0 x 3600.0[R]7200.0
//        [L]
//        [C]================================
//        [L]Before VAT:[R]6950.0
//        [L]VAT:[R]1390.0
//        [L]
//        [R]-------
//        [R]<font size='tall'>Total: 8340.0</font>
//        [C]================================
//        [L]<b>Rate</b>[R]<b>Vat</b>
//        [C]--------------------------------
//        [L]20%[R]1390.0
//        [C]--------------------------------
//        [L]<b>NSLF: C34123123131-21331-231231-231dd-12313asdasdada</b>
//        [L]<b>NIVF: C34123123131-21331-231231-231dd-12313asdasdada</b>
//        [L]
//        [C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>
//        [C] scan to verify
//        [L]
//        [C] www.nexia.al
//        """.trimIndent()
//            )
//    }

    fun printIt80mm(){
        val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 72f, 48)
        printer
            .printFormattedTextAndCut(
                """
        [L]
        [C]<font size='big'>Tax Invoice</font>
        [L]
        [C]--------------------------------
        [L]
        [L]<b>Seller:</b>[L]Nexia Al Consulting shp
        [L]<b>Address:</b>[L]Filial Qendror
        [L]<b>NUIS:</b>[L]L82210031Q
        [C]================================
        [L]<b>Data/time:</b>[R]17/09/2021; 14:33:46
        [L]<b>Invoice no:</b>[R]125/2021/dc769ca240
        [L]<b>Operator code:</b>[R]dr959ez502
        [L]<b>Business Unit Code:</b>[R]lr991hz335
        [L]<b>Payment method:</b>[R]BANKNOTE
        [C]================================
        [L]<b>No.  Item</b>
        [L]<b>     Quantity X Price</b>
        [C]================================
        [L]
        [L]<b>1 CocaColaZero2</b>
        [L]     1.0 x 300.0[R]300.0
        [L]<b>2 Vizite Neurologjike</b>
        [L]     2.0 x 3600.0[R]7200.0
        [L]
        [C]================================
        [L]Before VAT:[R]6950.0
        [L]VAT:[R]1390.0
        [L]
        [R]-------
        [R]<font size='tall'>Total: 8340.0</font>
        [C]================================
        [L]<b>Rate</b>[R]<b>Vat</b>
        [C]--------------------------------
        [L]20%[R]1390.0
        [C]--------------------------------
        [L]<b>NSLF: C34123123131-21331-231231-231dd-12313asdasdada</b>
        [L]<b>NIVF: C34123123131-21331-231231-231dd-12313asdasdada</b>
        [L]
        [C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>
        [C] scan to verify
        [L]
        [C] www.nexia.al
        """.trimIndent()
            )
    }




}