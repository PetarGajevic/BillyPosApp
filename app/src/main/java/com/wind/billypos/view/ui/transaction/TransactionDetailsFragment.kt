package com.wind.billypos.view.ui.transaction

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.data.remote.model.RemoteFiscaliseInvoiceRequest
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.databinding.FragmentTransactionDetailsBinding
import com.wind.billypos.utils.*
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.invoice.CartPaymentFragmentDirections
import com.wind.billypos.view.ui.transaction.adapter.TransactionDetailsAdapter
import com.wind.billypos.view.ui.transaction.viewmodel.TransactionDetailsViewModel
import kotlinx.android.synthetic.main.fragment_transaction_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*
import kotlin.math.abs

class TransactionDetailsFragment :
    BaseFragment<FragmentTransactionDetailsBinding, TransactionDetailsViewModel>() {

    private val transactionDetailsViewModel: TransactionDetailsViewModel by viewModel()
    private val args: TransactionDetailsFragmentArgs by navArgs()

    private var mConfigurations: Configuration? = Configuration()

    private var isCorrective = true

    private var transactionHead: TransactionHead = TransactionHead()
    private var transactionPaymentList: List<TransactionPayment> = listOf()
    private var items: List<TransactionBody> = listOf()
    private var invoiceNum: Long = 1
    private var fiscalFIC: String = ""
    val stornTransactionList = mutableListOf<TransactionBody>()

    override val bindingVariable: Int
        get() = BR.transactionDetailsViewModel
    override val layoutId: Int
        get() = R.layout.fragment_transaction_details
    override val viewModel: TransactionDetailsViewModel
        get() = transactionDetailsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mConfigurations =
            PreferenceHelper.customPreference(activity as AppCompatActivity).configurations

        setTransactionHead(trnHead = args.transaction)


        val transactionDetailsAdapter = TransactionDetailsAdapter()

        recyclerViewTransDetails.adapter = transactionDetailsAdapter


        val observerTransactionBodyList = Observer<List<TransactionBody>> {
            it.let(transactionDetailsAdapter::submitList)
            items = it
        }

        val observerTransactionHead = Observer<TransactionHead> {
            viewModel.getTransactionsBody()
            if(it.idTransactionParent.isNotEmpty()){
                Timber.e("usao")
                btnPosEditTransaction.visibility = View.GONE
                btnPosCheckoutSave.visibility = View.GONE
                btnCancelTransaction.visibility = View.GONE
            }
        }


        val observerTransactionPaymentList = Observer<List<TransactionPayment>> {
            Timber.e("Lista paymenta: %s", it)
            transactionPaymentList = it
        }

        btnPosCheckoutPrint.setOnClickListener {
            findNavController().navigateSafe(
                TransactionDetailsFragmentDirections.actionTransactionDetailsFragmentToPrintInvoiceBottomSheetDialogFragment(
                    transactionHead = transactionHead
                )
            )
        }

        btnPosEditTransaction.setOnClickListener {
            if(transactionHead.isFiscalized != true){
                Toast.makeText(requireContext(), getString(R.string.first_fiscalize_invoice), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            ConfirmAlert(requireContext(), object : YesNoDialogInterface {
                override fun onConfirm(dialog: AlertDialog) {
                    findNavController().navigateSafe(
                        TransactionDetailsFragmentDirections.actionTransactionDetailsFragmentToInvoiceFragment(
                            transactionHead = transactionHead
                        )
                    )
                }
                override fun onCancel(dialog: AlertDialog) {}
            }).showDialog(
                resources.getString(R.string.are_you_sure_to_edit_invoice),
                resources.getString(R.string.info_title)
            )

        }

        btnPosCheckoutSave.setOnClickListener {
            if (transactionHead.id == null || transactionHead.isFiscalized == true) {

                return@setOnClickListener
            }
            ConfirmAlert(requireContext(), object : YesNoDialogInterface {
                override fun onConfirm(dialog: AlertDialog) {
                    signDocument(invType = "INVOICE", isCorrective = false)
                    enableDisableClick(isEnabled = false)
                }
                override fun onCancel(dialog: AlertDialog) {}
            }).showDialog(
                resources.getString(R.string.are_you_sure_to_fiscalize_invoice),
                resources.getString(R.string.info_title)
            )

            //setPaymentMethods()
            // signDocument()
        }

        btnCancelTransaction.setOnClickListener {
//            if(!transactionHead.canBeReversed()){
//                return@setOnClickListener
//            }
            ConfirmAlert(requireContext(), object : YesNoDialogInterface {
                override fun onConfirm(dialog: AlertDialog) {
                    signDocument(invType = "CORRECTIVE", isCorrective = true)
                    enableDisableClick(isEnabled = false)
                }
                override fun onCancel(dialog: AlertDialog) {}
            }).showDialog(
                resources.getString(R.string.are_you_sure_to_storno_invoice),
                resources.getString(R.string.info_title)
            )

        }

        val observerFiscalKey = Observer<Company.FiscalDigitalKey> {
            generateIKOF()
        }

        val observerSignedDocument = Observer<String> {
            viewModel.fiscalReceipt(receipt = it)
        }

        // Invoice Order Number
        val observerInvoiceNum = Observer<Long> {
            invoiceNum = it
        }

        val observerJIKR = Observer<String> {
            if (it.contains("error")) {
                val trnHead = transactionHead.copy(
                    iic = viewModel.getIKOF()?.first
                )
                viewModel.updateTransactionHead(trnHead = trnHead)
                findNavController().navigateSafe(
                    CartPaymentFragmentDirections.actionCartPaymentFragmentToCartDoneFragment(
                        transactionHead = trnHead
                    )
                )
            } else {
                Timber.e("Uspjesna fiskalizacija")
                fiscalFIC = it
                var trnHead = TransactionHead()
                val listItem = mutableListOf<TransactionBody>()
                if(isCorrective){
                    for (item in transactionHead.items!!){
                        listItem.add(
                            item.copy(
                                total = -item.total!!,
                                totalWithVat = -item.totalWithVat!!,
                                vatAmount = -item.vatAmount!!,
                                quantity = -item.quantity!!
                            ))
                    }

                    trnHead = transactionHead.copy(
                        iic = viewModel.getIKOF()?.first,
                        isFiscalized = true,
                        fiscalFIC = it,
                        fiscalizedAt = LocalDateTime.now(),
                        transactionState = FiscalisationState.FISCALIZED,
                        total = -transactionHead.total,
                        totalWithVat = -transactionHead.totalWithVat,
                        vatAmount = -transactionHead.vatAmount,
                        items = listItem
                    )
                }else{
                    trnHead = transactionHead.copy(
                        iic = viewModel.getIKOF()?.first,
                        isFiscalized = true,
                        fiscalFIC = it,
                        fiscalizedAt = LocalDateTime.now(),
                        transactionState = FiscalisationState.FISCALIZED,
                    )
                }

                viewModel.sendReceipt(transactionHead = trnHead)
                Timber.e("Send REciept %s", trnHead)
//                viewModel.updateTransactionHead(trnHead = trnHead)
            }
        }

        val observerInsertedTransactionHead = Observer<Boolean> {
            if (it) {
                viewModel.insertTransactionList(trnList = stornTransactionList)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), getString(R.string.failed_to_storn), Toast.LENGTH_LONG).show()
            }
        }

        val observerRemoteTransactionHeadResponse = Observer<RemoteTransactionHeadResponse?> {
            if (it.status != "OK") {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.network_unavailable),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (isCorrective) {
                    val UUID = UUID.randomUUID().toString()
                    Timber.e("Transaction head before %s", transactionHead)
                    val trnHead = transactionHead.copy(
                        id = null,
                        uuid = UUID,
                        idTransactionParent = transactionHead.uuid!!,
                        reversedAt = LocalDateTime.now(),
                        transactionState = FiscalisationState.FISCALIZED,
                        total = - abs(transactionHead.total),
                        totalWithVat = - abs(transactionHead.totalWithVat),
                        vatAmount = - abs(transactionHead.vatAmount),
                        invoiceNo = invoiceNum,
                        isSync = true
                    )

                    for (i in items.indices) {
                        var item = items[i].copy(
                            id = null,
                            itemPriceWithDiscount = - abs(items[i].itemPriceWithDiscount),
                            itemPrice = - abs(items[i].itemPrice!!),
                            total = - abs(items[i].total!!),
                            totalWithVat = - abs(items[i].totalWithVat!!),
                            vatAmount = - abs(items[i].vatAmount!!),
                            idTransactionHead = UUID
                        )
                        stornTransactionList.add(item)
                    }

                    viewModel.insertTransactionHead(trnHead = trnHead)

                    val oldTrnHead = transactionHead.copy(
                        isReversed = true,
                        reversedAt = LocalDateTime.now(),
                        transactionState = FiscalisationState.STORNO,
                    )
                    Timber.e("Transaction head after %s", transactionHead)
                    viewModel.updateTransactionHead(trnHead = oldTrnHead)


                    Toast.makeText(
                        requireContext(),
                        getString(R.string.invoice_storn_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val trnHead = transactionHead.copy(
                        isFiscalized = true,
                        fiscalFIC = fiscalFIC,
                        fiscalizedAt = LocalDateTime.now(),
                        transactionState = FiscalisationState.FISCALIZED,
                        isSync = true
                    )
                    viewModel.updateTransactionHead(trnHead = trnHead)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.invoice_fiscalized_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            enableDisableClick(isEnabled = true)
        }

        val observerError = Observer<Throwable> {
            if (it != null) {
                enableDisableClick(isEnabled = true)
                Toast.makeText(requireContext(), getString(R.string.network_unavailable), Toast.LENGTH_LONG).show()
            }
        }



        viewModel.mutableLiveDataTransactionBodyList.observe(
            viewLifecycleOwner,
            observerTransactionBodyList
        )
        viewModel.mutableLiveDataTransactionPaymentList.observe(
            viewLifecycleOwner,
            observerTransactionPaymentList
        )
        viewModel.mutableLiveDataTransactionHead.observe(
            viewLifecycleOwner,
            observerTransactionHead
        )
        viewModel.mutableLiveDataFiscalDigitalKey.observe(viewLifecycleOwner, observerFiscalKey)
        viewModel.mutableLiveDataInsertedTransactionHead.observe(
            viewLifecycleOwner,
            observerInsertedTransactionHead
        )
        viewModel.mutableLiveDataJIKR.observe(viewLifecycleOwner, observerJIKR)
        viewModel.mutableLiveDataSignedDocument.observe(viewLifecycleOwner, observerSignedDocument)
        viewModel.mutableLiveDataInvoiceNum.observe(viewLifecycleOwner, observerInvoiceNum)
        viewModel.mutableLiveDataRemoteTransactionHeadResponse.observe(
            viewLifecycleOwner,
            observerRemoteTransactionHeadResponse
        )

        viewModel.mutableLiveDataErrorHandler.observe(viewLifecycleOwner, observerError)

        viewModel.getFiscalKey()
        viewModel.getLastInvoiceNum()
    }

    private fun setTransactionHead(trnHead: TransactionHead) {
        viewDataBinding?.transaction = args.transaction
        transactionHead = trnHead
        viewModel.setTransactionHead(trnHead = trnHead)
        viewModel.getPaymentMethods(trnHeadUUID = trnHead.uuid!!)
        isCorrective = if (trnHead.isFiscalized == true) {
            viewModel.setFiscalizeStatus(status = getString(R.string.transaction_state_fiscalized))
            true
        } else {
            viewModel.setFiscalizeStatus(status = getString(R.string.transaction_state_not_fiscalized))
            false
        }
    }

    private fun signDocument(invType: String, isCorrective: Boolean) {
        // Take current time
        val currentTime = ZonedDateTime.now(ZoneId.of("Europe/Budapest")).format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        )
        // Sign receipt
        viewModel.signDocument(
            RemoteFiscaliseInvoiceRequest(
                body = RemoteFiscaliseInvoiceRequest.SoapBodyFiscalization(
                    registerInvoiceRequest = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest(
                        header = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.HeaderFiscalization(
                            subseqDelivType = "NOINTERNET",
                            sendDateTime = currentTime,
                            uuid = transactionHead.uuid
                        ),
                        invoice = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice(
                            businUnitCode = "bb123bb123",//mConfigurations?.address?.businUnitCode,
                            issueDateTime = transactionHead.receiptDateTime,
                            IIC = viewModel.getIKOF()?.first,
                            IICSignature = viewModel.getIKOF()?.second,
                            invNum = "${transactionHead.invoiceNo?.toInt()}/${ZonedDateTime.now().year}/dc769ca240",//"${transactionHead.invoiceNum}/${ZonedDateTime.now().year}/${mConfigurations?.device?.tcrCode}",
                            invOrdNum = transactionHead.invoiceNo?.toInt(),//transactionHead.invoiceNum,
                            isIssuerInVAT = true,
                            isReverseCharge = false,
                            isSimplifiedInv = false,
                            operatorCode = "oo123oo123", //mConfigurations?.userData?.operatorCode,
                            softCode = "ss123ss123",//mConfigurations?.softCode,
                            TCRCode = "dc769ca240",//mConfigurations?.device?.tcrCode,
                            totPrice = if (isCorrective) "-" + transactionHead.totalWithVat.formatReceipt() else transactionHead.totalWithVat.formatReceipt(),
                            totPriceWoVAT = if (isCorrective) "-" + transactionHead.total.formatReceipt() else transactionHead.total.formatReceipt(),
                            totVATAmt = if (isCorrective) "-" + transactionHead.vatAmount.formatReceipt() else transactionHead.vatAmount.formatReceipt(),
                            typeOfInv = "CASH",
                            correctiveInv = if (isCorrective) {
                                RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.CorrectiveInv(
                                    IICRef = transactionHead.iic,
                                    issueDateTime = transactionHead.receiptDateTime,
                                    type = invType
                                )
                            } else {
                                null
                            },
                            payMethods = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods(
                                payMethod = if (isCorrective) {
                                    transactionPaymentList.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods.PayMethod(
                                            amt = "-" + it.amount?.formatReceipt(),
                                            type = it.paymentMethod.toString()
                                        )
                                    }
                                } else {
                                    transactionPaymentList.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods.PayMethod(
                                            amt = it.amount?.formatReceipt(),
                                            type = it.paymentMethod.toString()
                                        )
                                    }
                                }
                            ),
                            seller = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Seller(
                                address = mConfigurations?.address?.fullAddress,
                                country = "ALB",//mConfigurations?.address?.country,
                                idNum = "L82210031Q",//mConfigurations?.company?.nuis,
                                idType = "NUIS",
                                name = "Seller Name",//mConfigurations?.address?.name,
                                town = "Seller Town",//mConfigurations?.address?.city
                            ),
                            items = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Items(
                                item = if (isCorrective) {
                                    items.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Items.Item(
//                                            code = it.id.toString(),
//                                            name = it.itemName,
//                                            pa = "-" + it.pa()?.formatReceipt(),
//                                            pb = "-" + it.pb()?.formatReceipt(),
//                                            quantity = "-" + it.quantity?.formatQuantity(),
//                                            rabat = it.rabat?.formatReceipt(),
//                                            measureUnit = it.itemUnit,
//                                            upb = it.upb()?.formatReceipt(),//it.itemPrice?.formatReceipt(),
//                                            upa = it.upa()?.formatReceipt(),
//                                            va = "-" + it.vatAmount?.formatReceipt(),
//                                            vatRate = it.vatRate?.formatReceipt()
                                            code = it.id.toString(),
                                            name = it.itemName,
                                            pa = "-" + it.totalWithVat?.formatReceipt(),//it.pa()?.formatReceipt(),
                                            pb = "-" + it.total?.formatReceipt(),//it.pb()?.formatReceipt(),
                                            quantity = "-" + it.quantity?.formatQuantity(),
                                            rabat = "0",//it.rabat?.formatReceipt(),
                                            measureUnit = it.item?.itemUnit,
                                            upb = it.unitPrice?.formatReceipt(),//it.upb()?.formatReceipt(),
                                            upa = it.unitPriceWithVat?.formatReceipt(),//it.upa()?.formatReceipt(),
                                            va = "-" + it.vatAmount?.formatReceipt(),//it.va()?.formatReceipt(),
                                            vatRate = it.vatRate?.formatReceipt()
                                        )
                                    }
                                } else {
                                    items.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Items.Item(
//                                            code = it.id.toString(),
//                                            name = it.itemName,
//                                            pa = it.pa().formatReceipt(),
//                                            pb = it.pb().formatReceipt(),
//                                            quantity = it.quantity?.formatQuantity(),
//                                            rabat = it.rabat?.formatReceipt(),
//                                            measureUnit = it.itemUnit, //it.itemUnit,
//                                            upb = it.itemPrice?.formatReceipt(),
//                                            upa = it.upa().formatReceipt(),
//                                            va = it.vatAmount?.formatReceipt(),
//                                            vatRate = it.vatRate?.formatReceipt()
                                            code = it.id.toString(),
                                            name = it.itemName,
                                            pa = it.totalWithVat?.formatReceipt(),//it.pa()?.formatReceipt(),
                                            pb = it.total?.formatReceipt(),//it.pb()?.formatReceipt(),
                                            quantity = it.quantity?.formatQuantity(),
                                            rabat = "0",//it.rabat?.formatReceipt(),
                                            measureUnit = it.item?.itemUnit,
                                            upb = it.unitPrice?.formatReceipt(),//it.upb()?.formatReceipt(),
                                            upa = it.unitPriceWithVat?.formatReceipt(),//it.upa()?.formatReceipt(),
                                            va = it.vatAmount?.formatReceipt(),//it.va()?.formatReceipt(),
                                            vatRate = it.vatRate?.formatReceipt()
                                        )
                                    }
                                }
                            ),
                            sameTaxes = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.SameTaxes(
                                tax = sameTaxes(items).map {
                                    RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.SameTaxes.SameTax(
                                        numOfItems = it.numOfItems,
                                        priceBefVAT = if (isCorrective) "-" + it.priceBefVAT?.formatReceipt() else it.priceBefVAT?.formatReceipt(),
                                        VATAmt = if (isCorrective) "-" + it.vatAmt?.formatReceipt() else it.vatAmt?.formatReceipt(),
                                        VATRate = it.vatRate?.formatReceipt()
                                    )
                                }
                            )
                        )
                    )
                )
            ), elementToSign = RemoteFiscaliseInvoiceRequest.ELEMENT_TO_SIGN
        )
    }

    private fun generateIKOF() {
        viewModel.generateIKOF(
            mConfigurations?.iicData(
                createdDateTime = ZonedDateTime.now(ZoneId.of("Europe/Budapest")).format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
                ).toString(),
                invoiceNumber = transactionHead.invoiceNo.toString(),
                totalPrice = transactionHead.totalWithVat.formatReceipt()
            ) ?: ""
        )
    }

    private fun enableDisableClick(isEnabled: Boolean){
        btnPosCheckoutSave.isEnabled = isEnabled
        btnPosEditTransaction.isEnabled = isEnabled
        btnCancelTransaction.isEnabled = isEnabled
        btnPosCheckoutPrint.isEnabled = isEnabled
        if(isEnabled){
            loadingSyncTransaction.visibility = View.GONE
        }else{
            loadingSyncTransaction.visibility = View.VISIBLE
        }
    }

}