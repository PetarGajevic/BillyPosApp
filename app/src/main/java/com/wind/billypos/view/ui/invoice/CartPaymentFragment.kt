package com.wind.billypos.view.ui.invoice

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.remote.model.RemoteFiscaliseInvoiceRequest
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.data.remote.model.summary.RemoteFiscaliseSummaryInvoiceRequest
import com.wind.billypos.databinding.FragmentCartPaymentBinding
import com.wind.billypos.utils.*
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.PaymentMethodTypeSType
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.invoice.viewmodel.CartPaymentViewModel
import kotlinx.android.synthetic.main.fragment_cart_payment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*


class CartPaymentFragment : BaseFragment<FragmentCartPaymentBinding, CartPaymentViewModel>() {

    private val cartPaymentViewModel: CartPaymentViewModel by viewModel()
    private val homeSharedViewModel: HomeSharedViewModel by viewModel()

    private val args: CartPaymentFragmentArgs by navArgs()
    private var mConfigurations: Configuration? = Configuration()

    private var amountPayWithCashInput: Double = 0.0
    private var amountPayWithCash: Double = 0.0
    private var amountPayWithCC: Double = 0.0
    private var amountPayWithVoucher: Double = 0.0
    private var totalAmount: Double = 0.0
    private var changeAmountValue: Double = 0.0
    private var isCashCheck: Boolean = true
    private var isCCCheck: Boolean = false;
    private var isPVoucherCheck: Boolean = false
    private var transactionHead: TransactionHead = TransactionHead()
    private val payMethodList: MutableList<TransactionPayment> = mutableListOf()
    private var items: List<TransactionBody> = listOf()
    private var summaryItems: List<TransactionBody> = listOf()
    private var iicList: List<TransactionHead>? = listOf()
    private var idCustomer = ""
    private var idEntity = ""
    private var isOrder = false

    private var isFiscalized = false
    private var isSync = false
    private var isCorrective = false

    override val bindingVariable: Int
        get() = BR.cartPaymentViewModel
    override val layoutId: Int
        get() = R.layout.fragment_cart_payment
    override val viewModel: CartPaymentViewModel
        get() = cartPaymentViewModel


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (args.transactionHead != null) {
            homeSharedViewModel.setTransactionHead(args.transactionHead!!)
        }


        mConfigurations = activity?.let { PreferenceHelper.customPreference(it).configurations }
        // viewModel.setTransactionHead(args.transactionHead)


//        Set row click listener for credit card
        llCash.setOnClickListener { checkboxCach.isChecked = true }

        txtCachValuePayment.setOnFocusChangeListener { _, b ->
            if (b) {
                checkboxCach.isChecked = true
            }
        }

//        Set row click listener for cash
        llCreditCard.setOnClickListener { checkboxCC.isChecked = true }

        txtCCValuePayment.setOnFocusChangeListener { _, b ->
            if (b) {
                checkboxCC.isChecked = true
            }
        }

        btnConfirmPayment.setOnClickListener {
            if (transactionHead.uuid == null) {
                //TODO add some logic here
                return@setOnClickListener
            }
            if (isCashCheck && isCCCheck) {
                if ((amountPayWithCash + amountPayWithCC) > transactionHead.totalWithVat) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.payment_card_total_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
            if (isCCCheck) {
                if (amountPayWithCC > transactionHead.totalWithVat) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.payment_card_total_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }
            setProgressBar(true)
            transactionHead = transactionHead.copy(
                items = items,
                idCustomer = idCustomer,
                idEntity = idEntity,
                isOrderInvoice = isOrder
            )

//            TODO(Check corrective if it is working)
//            if (isCorrective) {
//                deletePaymentMethods(transactionUUID = transactionHead.uuid)
//            } else {
            setPaymentMethods()
            viewModel.insertTransactionHead(trnHead = transactionHead)
//            }

        }


        val totalAmountFormat = String.format(
            "%.2f",
            transactionHead.totalWithVat
        ) + " " + mConfigurations!!.company?.currencyCode
        invoiceTotalAmount?.text = totalAmountFormat
        // totalOrderInvoice?.text = totalAmountFormat

        sumOfPayment.text = String.format(
            "%.2f",
            transactionHead.totalWithVat
        ) + " " + mConfigurations!!.company?.currencyCode
        changeAmount.text = String.format(
            "%.2f",
            0.0
        ) + " " + mConfigurations!!.company?.currencyCode
        txtCachValuePayment?.setText(String.format("%.2f", transactionHead.totalWithVat))
        txtCachValuePayment?.setSelectAllOnFocus(true)

        txtCCValuePayment?.setText("0.0")
        txtSVoucherValuePayment?.text = "0.0"
        txtCCValuePayment?.isEnabled = true
        txtCartCharge.text = String.format(
            resources.getString(R.string.change_overview),
            0.0,
            mConfigurations!!.company?.currencyCode
        )

        txtCachValuePayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val cleanString = p0.toString().replace("[,]".toRegex(), ".")
                amountPayWithCash = if (cleanString.isEmpty()) 0.0 else cleanString.toDouble()
                //  calculateSumOfPayment()
                sumInsertedAmount()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        txtCCValuePayment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val cleanString = p0.toString().replace("[,]".toRegex(), ".")
                amountPayWithCC = if (cleanString.isEmpty()) 0.0 else cleanString.toDouble()
                //   calculateDiffCash()
                sumInsertedAmount()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        checkboxCach.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                paymentMethodCashImg.setImageResource(R.drawable.ic_baseline_money_selected)
                paymentMethodCashTxt.setTextColor(resources.getColor(R.color.colorPrimary))
                isCashCheck = true
            } else {
                paymentMethodCashImg.setImageResource(R.drawable.ic_baseline_money_24px)
                paymentMethodCashTxt.setTextColor(resources.getColor(R.color.gray_dark))
                isCashCheck = false
                val cleanString = "0.0"
                amountPayWithCash = cleanString.toDouble()
                txtCachValuePayment.setText(cleanString)
                sumInsertedAmount()
            }
        }


        checkboxCC.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                paymentMethodCCImg.setImageResource(R.drawable.ic_payment_cc_selected)
                paymentMethodCCTxt.setTextColor(resources.getColor(R.color.colorPrimary))
                isCCCheck = true
                txtCCValuePayment?.isEnabled = true
                txtCCValuePayment?.setSelectAllOnFocus(true)
            } else {
                isCCCheck = false;
                paymentMethodCCImg.setImageResource(R.drawable.ic_payment_cc)
                paymentMethodCCTxt.setTextColor(resources.getColor(R.color.gray_dark))
                txtCCValuePayment?.isEnabled = false
                val cleanString = "0.0"
                amountPayWithCC = cleanString.toDouble()
                txtCCValuePayment.setText(cleanString)
                sumInsertedAmount()
            }
        }


        checkboxSVoucher.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isPVoucherCheck = true;
                paymentMethodSVoucherImg.setImageResource(R.drawable.ic_local_offer_selected)
                paymentMethodSVoucherTxt.setTextColor(resources.getColor(R.color.colorPrimary))

                calculateSVoucher()
            } else {
                isPVoucherCheck = false;
                paymentMethodSVoucherImg.setImageResource(R.drawable.ic_local_offer_24px)
                paymentMethodSVoucherTxt.setTextColor(resources.getColor(R.color.gray_dark))
                calculateSVoucher()
            }
        }

        invoiceTotalAmount.text = args.transactionHead?.totalWithVat.toString()

        val observerTransactionHead = Observer<TransactionHead> {
            transactionHead = it
            viewModel.insertTransactionBodyList(trnBodyList = items)
            signDocument()
        }

        val observerSharedTransactionHead = Observer<TransactionHead?> { head ->
            transactionHead = head
            iicList = transactionHead.orders
            Timber.e("transactionHead: %s", transactionHead)
            amountPayWithCashInput = transactionHead.totalWithVat
            totalAmount = transactionHead.totalWithVat
            if (transactionHead.isSummaryInvoice) {
//                viewModel.getInvoiceIIC(idEntity = transactionHead.idEntity)
//                iicList = trnHeadList
//                Timber.e("iicList %s", iicList)
                val listOfIds = mutableListOf<String?>()
                transactionHead.orders?.forEach { listOfIds.add(it.uuid) }
                Timber.e("listOfIds %s", listOfIds)
                viewModel.getSummaryTransactionBodies(id = listOfIds)
            }
            // viewModel.getItems(transactionHead.uuid)
        }

        val observerItems = Observer<MutableList<TransactionBody>?> {
            items = it
            Timber.e("Itemi %s", items.toString())
        }

        val observerSignedDocument = Observer<String> {
            if (it == "error") {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_configure_cert_details),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.fiscalReceipt(receipt = it)
            }

        }

        val observerFiscalDigitalKey = Observer<Company.FiscalDigitalKey> {
            generateIKOF()
        }

        val observerIKOF = Observer<Pair<String, String>> {
            // TODO Treba sacuvati iic u transaction head
        }

        val observerJIKR = Observer<String> {
            if (it.contains("error")) {
                Timber.e("Neuspjesna fiskalizacija")
                transactionHead = transactionHead.copy(
                    iic = viewModel.getIKOF()?.first
                )
                viewModel.sendReceipt(transactionHead = transactionHead)
                Timber.e("Send REciept %s", transactionHead)
//                viewModel.updateTransactionHead(trnHead = trnHead)
//                findNavController().navigateSafe(
//                    CartPaymentFragmentDirections.actionCartPaymentFragmentToCartDoneFragment(
//                        transactionHead = trnHead
//                    )
//                )
            } else {
                Timber.e("Uspjesna fiskalizacija")
                isFiscalized = true

                transactionHead = transactionHead.copy(
                    iic = viewModel.getIKOF()?.first,
                    isFiscalized = true,
                    fiscalFIC = it,
                    fiscalizedAt = LocalDateTime.now(),
                    transactionState = FiscalisationState.FISCALIZED,
                )
                viewModel.sendReceipt(transactionHead = transactionHead)
                Timber.e("Send REciept %s", transactionHead)
//                viewModel.updateTransactionHead(trnHead = trnHead)
            }

        }

        val observerTransactionHeadId = Observer<Long> {

        }

//        TODO(Ako je sumarni updejtuj ordere da su placeni)
        val observerRemoteTransactionHeadResponse =
            Observer<RemoteTransactionHeadResponse?> { headResponse ->
                headResponse?.let {
                    isSync = true
                    transactionHead = transactionHead.copy(
                        isSync = isSync,
                        lastServerSync = LocalDateTime.now()
                    )
                }

                if (transactionHead.isSummaryInvoice) {
                    Timber.e("Sumrani je")
                    iicList = iicList?.map {
                        it.copy(isPaid = true)
                    }!!
                    viewModel.updateOrders(transactionHeadList = iicList!!)
                    viewModel.updateTransactionHead(trnHead = transactionHead.copy(isPaid = true))
                } else {
                    Timber.e("Nije sumarni")
                    viewModel.updateTransactionHead(trnHead = transactionHead)
                    setProgressBar(false)
                    findNavController().navigateSafe(
                        CartPaymentFragmentDirections.actionCartPaymentFragmentToCartDoneFragment(
                            transactionHead = transactionHead
                        )
                    )
                }


            }

        val observerIsCorrective = Observer<Boolean?> {
            isCorrective = it
        }

        val observerCheckPayment = Observer<Boolean> {
            if (it) {
                setPaymentMethods()
                viewModel.insertTransactionHead(trnHead = transactionHead)
            }
        }

        val observerCustomer = Observer<Customer?> {
            it?.let {
                idCustomer = it.uuid
            }
        }

        val observerEntityPoint = Observer<EntityPoint?> {
            it?.let {
                idEntity = it.uuid.toString()
                isOrder = true
            }
        }

        val observerIICList = Observer<List<TransactionHead>> { trnHeadList ->

        }

        val observerSummaryItems = Observer<List<TransactionBody>> {
            summaryItems = it
            Timber.e("summaryItems %s", summaryItems)
        }

        val observerUpdatedOrders = Observer<Boolean> {
            setProgressBar(false)
            findNavController().navigateSafe(
                CartPaymentFragmentDirections.actionCartPaymentFragmentToCartDoneFragment(
                    transactionHead = transactionHead
                )
            )
        }

        viewModel.mutableLiveDataSignedDocument.observe(viewLifecycleOwner, observerSignedDocument)
        viewModel.mutableLiveDataSummaryItems.observe(viewLifecycleOwner, observerSummaryItems)
        viewModel.mutableLiveDataUpdatedOrders.observe(viewLifecycleOwner, observerUpdatedOrders)
        homeSharedViewModel.mutableLiveDataIsCorrective.observe(
            viewLifecycleOwner,
            observerIsCorrective
        )
        viewModel.mutableLiveDataCheckPayment.observe(viewLifecycleOwner, observerCheckPayment)
        homeSharedViewModel.mutableLiveDataTransactionBodyList.observe(
            viewLifecycleOwner,
            observerItems
        )
        viewModel.mutableLivaDataTransactionHeadId.observe(
            viewLifecycleOwner,
            observerTransactionHeadId
        )
        viewModel.mutableLiveDataFiscalDigitalKey.observe(
            viewLifecycleOwner,
            observerFiscalDigitalKey
        )
        viewModel.mutableLiveDataJIKR.observe(viewLifecycleOwner, observerJIKR)
        viewModel.mutableLiveDataIKOF.observe(viewLifecycleOwner, observerIKOF)
        viewModel.mutableLiveDataIICList.observe(viewLifecycleOwner, observerIICList)
        viewModel.mutableLiveDataRemoteTransactionHeadResponse.observe(
            viewLifecycleOwner,
            observerRemoteTransactionHeadResponse
        )
        homeSharedViewModel.mutableLivaDataTransactionHead.observe(
            viewLifecycleOwner,
            observerSharedTransactionHead
        )
        viewModel.mutableLivaDataTransactionHead.observe(
            viewLifecycleOwner,
            observerTransactionHead
        )
        homeSharedViewModel.mutableLiveDataCustomer.observe(viewLifecycleOwner, observerCustomer)
        homeSharedViewModel.mutableLiveDataEntityPoint.observe(
            viewLifecycleOwner,
            observerEntityPoint
        )
        viewModel.getFiscalKey()
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

    @SuppressLint("SetTextI18n")
    private fun sumInsertedAmount() {
        val totalSumOfPayment = amountPayWithCash + amountPayWithCC + amountPayWithVoucher
        sumOfPayment.text =
            String.format("%.2f", totalSumOfPayment) + " " + mConfigurations!!.company?.currencyCode
        calculateChange(totalSumOfPayment)
    }

    @SuppressLint("SetTextI18n")
    private fun calculateChange(sumAmount: Double) {
        val changeAmountCal = totalAmount - sumAmount
        changeAmount.text =
            String.format("%.2f", changeAmountCal) + " " + mConfigurations!!.company?.currencyCode
        changeAmountValue = changeAmountCal
        if (changeAmountCal > 0.0) {
            btnConfirmPayment.isEnabled = false
            btnConfirmPayment.setBackgroundColor(resources.getColor(R.color.gray_light))
            changeAmount.setTextColor(resources.getColor(R.color.red))
        } else {
            btnConfirmPayment.isEnabled = true
            btnConfirmPayment.setBackgroundColor(resources.getColor(R.color.colorAccent))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateSumOfPayment() {
        val totalSumOfPayment = amountPayWithCash + amountPayWithCC + amountPayWithVoucher;
        sumOfPayment.setText(
            String.format(
                "%.2f",
                totalSumOfPayment
            ) + " " + mConfigurations!!.company?.currencyCode
        )
        calculateChangeAmount(totalSumOfPayment)
    }

    @SuppressLint("SetTextI18n")
    private fun calculateChangeAmount(totalSumOfPayment: Double) {
        val changeAmountCal = totalAmount - totalSumOfPayment
        Timber.e("Change amount cal $changeAmountCal Total Amount $totalAmount Total sum of payment $totalSumOfPayment")
        changeAmountValue = changeAmountCal;
        changeAmount.setText(
            String.format(
                "%.2f",
                changeAmountCal
            ) + " " + mConfigurations!!.company?.currencyCode
        )
        if (changeAmountCal > 0.0) {
            btnConfirmPayment.isEnabled = false
            btnConfirmPayment.setBackgroundColor(resources.getColor(R.color.gray_light))
            changeAmount.setTextColor(resources.getColor(R.color.red))
        } else {
            btnConfirmPayment.isEnabled = true
            btnConfirmPayment.setBackgroundColor(resources.getColor(R.color.colorAccent))
        }
    }

    private fun calculateDiffCash() {
        val diffToPay = totalAmount - amountPayWithCC
        amountPayWithCash = diffToPay
        //   txtCachValuePayment.setText(String.format("%.2f", diffToPay))
        calculateSumOfPayment()
    }

    private fun calculateValueCash() {
        amountPayWithCash = 0.0;
        if (isCashCheck) {
            amountPayWithCash = totalAmount - amountPayWithCC - amountPayWithVoucher
            txtCachValuePayment.setText(String.format("%.2f", amountPayWithCash))
        } else {
            txtCachValuePayment.setText(String.format("%.2f", amountPayWithCash))
        }
    }

    private fun calculateValueCC() {
        amountPayWithCC = 0.0
        if (isCCCheck) {
            amountPayWithCC = totalAmount - amountPayWithCash - amountPayWithVoucher
            txtCCValuePayment.setText(String.format("%.2f", amountPayWithCC))
        } else {
            txtCCValuePayment.setText(String.format("%.2f", amountPayWithCC))
        }
    }

    private fun calculateSVoucher() {
        amountPayWithVoucher = 0.0;
        if (isPVoucherCheck) {
            amountPayWithVoucher = totalAmount - amountPayWithCash - amountPayWithCC
            txtSVoucherValuePayment.setText(String.format("%.2f", amountPayWithVoucher))
            txtSVoucherValuePayment.height = 16;

            nrSVoucher.visibility = View.VISIBLE;
        } else {
            nrSVoucher.visibility = View.GONE;
            txtSVoucherValuePayment.height = 56;
            txtSVoucherValuePayment.setText(String.format("%.2f", amountPayWithVoucher))
        }
        calculateSumOfPayment()
    }


    private fun setPaymentMethods() {
        val transactionPayment = TransactionPayment()
        Timber.e("Change amount value %s", changeAmountValue)
        if (isCashCheck && amountPayWithCash > 0) {
            val trnPayment = transactionPayment.copy(
                uuid = UUID.randomUUID().toString(),
                amount = amountPayWithCash + changeAmountValue,
                paymentMethod = PaymentMethodTypeSType.BANKNOTE,
                idTransactionHead = transactionHead.uuid
            )
            Timber.e("Trn payment %s ", trnPayment)
            payMethodList.add(trnPayment)
            Timber.e("Trn payment  list %s ", payMethodList)
            viewModel.insertPayment(trnPayment = trnPayment)
            transactionHead = transactionHead.copy(
                paymentMethods = payMethodList
            )
        }
        if (isCCCheck && amountPayWithCC > 0) {
            val trnPayment = transactionPayment.copy(
                uuid = UUID.randomUUID().toString(),
                amount = amountPayWithCC,
                paymentMethod = PaymentMethodTypeSType.CARD,
                idTransactionHead = transactionHead.uuid,
                paymentDetails = "1234123412341234"
            )
            payMethodList.add(trnPayment)
            viewModel.insertPayment(trnPayment = trnPayment)
            transactionHead = transactionHead.copy(
                paymentMethods = payMethodList
            )
        }
        if (isPVoucherCheck) {
            val trnPayment = transactionPayment.copy(
                uuid = UUID.randomUUID().toString(),
                amount = amountPayWithVoucher,
                paymentMethod = PaymentMethodTypeSType.SVOUCHER,
                idTransactionHead = transactionHead.uuid,
                paymentDetails = nrSVoucher.text.toString()
            )
            payMethodList.add(trnPayment)
            viewModel.insertPayment(trnPayment = trnPayment)
            transactionHead = transactionHead.copy(
                paymentMethods = payMethodList
            )
        }
        Timber.e("Payment methods: %s", payMethodList.toString())
    }

    private fun signDocument() {
        // Take current time
        Timber.e("transactionHead.orders: %s", transactionHead.orders)
        val currentTime = ZonedDateTime.now(ZoneId.of("Europe/Budapest")).format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        )
        if (transactionHead.isSummaryInvoice) {
            viewModel.signDocument(
                RemoteFiscaliseSummaryInvoiceRequest(
                    body = RemoteFiscaliseSummaryInvoiceRequest.SoapSummaryBodyFiscalization(
                        registerSummaryInvoiceRequest = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest(
                            header = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.HeaderSummaryFiscalization(
                                sendDateTime = currentTime,
                                uuid = transactionHead.uuid
                            ),
                            invoice = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice(
                                businUnitCode = "lr991hz335",//mConfigurations?.address?.businUnitCode,
                                issueDateTime = currentTime,
                                IIC = viewModel.getIKOF()?.first,
                                IICSignature = viewModel.getIKOF()?.second,
                                invNum = "${transactionHead.invoiceNo?.toInt()}/${ZonedDateTime.now().year}/dc769ca240",//"${transactionHead.invoiceNum}/${ZonedDateTime.now().year}/${mConfigurations?.device?.tcrCode}",
                                invOrdNum = transactionHead.invoiceNo?.toInt(),//transactionHead.invoiceNum,
                                isIssuerInVAT = true,
                                isReverseCharge = false,
                                isSimplifiedInv = false,
                                operatorCode = "dc769ca240", //mConfigurations?.userData?.operatorCode,
                                softCode = "ss123ss123",//mConfigurations?.softCode,
                                TCRCode = "dc769ca240",//mConfigurations?.device?.tcrCode,
                                totPrice = transactionHead.totalWithVat.formatReceipt(),
                                totPriceWoVAT = transactionHead.total.formatReceipt(),
                                totVATAmt = transactionHead.vatAmount.formatReceipt(),
                                typeOfInv = "CASH",
                                payMethods =
                                RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummaryPayMethods(
                                    payMethod = payMethodList.map {
                                        RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummaryPayMethods.SummaryPayMethod(
                                            amt = it.amount?.formatReceipt(),
                                            type = it.paymentMethod.toString()
                                        )
                                    }
                                ),
                                seller = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummarySeller(
                                    address = mConfigurations?.address?.fullAddress,
                                    country = "ALB",//mConfigurations?.address?.country,
                                    idNum = "L82210031Q",//mConfigurations?.company?.nuis,
                                    idType = "NUIS",
                                    name = "Seller Name",//mConfigurations?.address?.name,
                                    town = "Seller Town",//mConfigurations?.address?.city
                                ),
                                items = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummaryItems(
                                    item = summaryItems.map {
                                        RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummaryItems.SummaryItem(
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
                                ),
                                sameTaxes = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummarySameTaxes(
                                    tax = sameTaxes(summaryItems).map {
                                        RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SummarySameTaxes.SummarySameTax(
                                            numOfItems = it.numOfItems,
                                            priceBefVAT = it.priceBefVAT?.formatReceipt(),
                                            VATAmt = it.vatAmt?.formatReceipt(),
                                            VATRate = it.vatRate?.formatReceipt()
                                        )
                                    }
                                ),
                                sumInvIICRefs = RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SumInvIICRefs(
                                    sumInvIICRef = iicList?.map {
                                        RemoteFiscaliseSummaryInvoiceRequest.RegisterSummaryInvoiceRequest.SummaryInvoice.SumInvIICRefs.SumInvIICRef(
                                            iic = it.iic,
                                            issueDateTime = it.receiptDateTime
                                        )
                                    }!!
                                )
                            )
                        )
                    )
                ),
                elementToSign = RemoteFiscaliseSummaryInvoiceRequest.ELEMENT_TO_SIGN
            )
        } else {
            // Sign receipt
            viewModel.signDocument(
                RemoteFiscaliseInvoiceRequest(
                    body = RemoteFiscaliseInvoiceRequest.SoapBodyFiscalization(
                        registerInvoiceRequest = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest(
                            header = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.HeaderFiscalization(
                                sendDateTime = currentTime,
                                uuid = transactionHead.uuid
                            ),
                            invoice = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice(
                                businUnitCode = "lr991hz335",//mConfigurations?.address?.businUnitCode,
                                issueDateTime = currentTime,
                                IIC = viewModel.getIKOF()?.first,
                                IICSignature = viewModel.getIKOF()?.second,
                                invNum = "${transactionHead.invoiceNo?.toInt()}/${ZonedDateTime.now().year}/dc769ca240",//"${transactionHead.invoiceNum}/${ZonedDateTime.now().year}/${mConfigurations?.device?.tcrCode}",
                                invOrdNum = transactionHead.invoiceNo?.toInt(),//transactionHead.invoiceNum,
                                isIssuerInVAT = true,
                                isReverseCharge = false,
                                isSimplifiedInv = false,
                                operatorCode = "dc769ca240", //mConfigurations?.userData?.operatorCode,
                                softCode = "ss123ss123",//mConfigurations?.softCode,
                                TCRCode = "dc769ca240",//mConfigurations?.device?.tcrCode,
                                totPrice = transactionHead.totalWithVat.formatReceipt(),
                                totPriceWoVAT = transactionHead.total.formatReceipt(),
                                totVATAmt = transactionHead.vatAmount.formatReceipt(),
                                typeOfInv = "CASH",
                                correctiveInv = if (isCorrective) {
                                    RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.CorrectiveInv(
                                        IICRef = transactionHead.iic,
                                        issueDateTime = transactionHead.receiptDateTime,
                                        type = "CORRECTIVE"
                                    )
                                } else {
                                    null
                                },
                                payMethods = if (!isOrder) {
                                    RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods(
                                        payMethod = payMethodList.map {
                                            RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods.PayMethod(
                                                amt = it.amount?.formatReceipt(),
                                                type = it.paymentMethod.toString()
                                            )
                                        }
                                    )
                                } else {
                                    RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods(
                                        payMethod = payMethodList.map {
                                            RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods.PayMethod(
                                                amt = transactionHead.totalWithVat.formatReceipt(),
                                                type = "ORDER"
                                            )
                                        }
                                    )
                                },
                                seller = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Seller(
                                    address = mConfigurations?.address?.fullAddress,
                                    country = "ALB",//mConfigurations?.address?.country,
                                    idNum = "L82210031Q",//mConfigurations?.company?.nuis,
                                    idType = "NUIS",
                                    name = "Seller Name",//mConfigurations?.address?.name,
                                    town = "Seller Town",//mConfigurations?.address?.city
                                ),
                                items = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Items(
                                    item = items.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Items.Item(
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
                                ),
                                sameTaxes = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.SameTaxes(
                                    tax = sameTaxes(items).map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.SameTaxes.SameTax(
                                            numOfItems = it.numOfItems,
                                            priceBefVAT = it.priceBefVAT?.formatReceipt(),
                                            VATAmt = it.vatAmt?.formatReceipt(),
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
    }


    private fun deletePaymentMethods(transactionUUID: String?) =
        viewModel.deletePaymentMethods(transactionUUID = transactionUUID)

    private fun setProgressBar(visible: Boolean) {
        viewModel.mutableLiveDataProgressBar.value = visible
        btnConfirmPayment.isEnabled = !visible
    }

}