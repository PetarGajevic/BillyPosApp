package com.wind.billypos.view.ui.transaction

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.remote.model.RemoteFiscaliseInvoiceRequest
import com.wind.billypos.databinding.FragmentTransactionBinding
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.utils.formatQuantity
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.utils.sameTaxes
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.Configuration
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.model.report.TransactionsTodayOverview
import com.wind.billypos.view.ui.cashbalance.CashBalanceFragmentDirections
import com.wind.billypos.view.ui.transaction.adapter.ShowTransactionListener
import com.wind.billypos.view.ui.transaction.adapter.TransactionAdapter
import com.wind.billypos.view.ui.transaction.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_transaction.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber


class TransactionFragment : BaseFragment<FragmentTransactionBinding, TransactionViewModel>(),
    ShowTransactionListener {

    private val transactionViewModel: TransactionViewModel by viewModel()

    private var mConfigurations: Configuration? = Configuration()

    override val bindingVariable: Int
        get() = BR.transactionViewModel
    override val layoutId: Int
        get() = R.layout.fragment_transaction
    override val viewModel: TransactionViewModel
        get() = transactionViewModel

    private var unfiscalizedTransactionsList = listOf<TransactionHead>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mConfigurations =
            PreferenceHelper.customPreference(activity as AppCompatActivity).configurations

        val transactionAdapter = TransactionAdapter(
            requireContext(),
            mConfigurations!!, this
        )

        rvTransactions.adapter = transactionAdapter


        val observerTransactionHeadList = Observer<List<TransactionHead>> {
            transactionAdapter.setItems(it)
        }

        val observerTransactionsTodayOverview = Observer<TransactionsTodayOverview> {
            totalPriceOverview.text = String.format(
                resources.getString(R.string.total_sales_overview),
                mConfigurations!!.company?.currencyCode,
                it.total,
                it.transactions
            )
        }

        val observerUnfiscalizedTransactionHeadList = Observer<List<TransactionHead>> {
            unfiscalizedTransactionsList = it
        }

        viewModel.mutableLiveDataTransactionsTodayOverview.observe(
            viewLifecycleOwner,
            observerTransactionsTodayOverview
        )
        viewModel.mutableLiveDataTransactionHeadList.observe(
            viewLifecycleOwner,
            observerTransactionHeadList
        )
        viewModel.mutableLiveDataUnfiscalizedTransactionHeadList.observe(
            viewLifecycleOwner,
            observerUnfiscalizedTransactionHeadList
        )

        viewModel.todayTotalSales()
        viewModel.getAllTransactions()
        viewModel.getAllUnfiscalizedTransactions()
    }

    override fun onTransactionClick(trnHead: TransactionHead) {
        findNavController().navigateSafe(
            TransactionFragmentDirections.actionTransactionFragmentToTransactionDetailsFragment(
                transaction = trnHead
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_transactions, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_send_unsynced -> {
                true
            }
            R.id.action_send_unfiscalised -> {
                fiscalizeTransactions(transactionList = unfiscalizedTransactionsList)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fiscalizeTransactions(transactionList: List<TransactionHead>) {
        // Take current time
        val currentTime = ZonedDateTime.now(ZoneId.of("Europe/Budapest")).format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        )
        transactionList.map { transactionHead ->
            viewModel.signDocument(
                RemoteFiscaliseInvoiceRequest(
                    body = RemoteFiscaliseInvoiceRequest.SoapBodyFiscalization(
                        registerInvoiceRequest = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest(
                            header = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.HeaderFiscalization(
                                sendDateTime = currentTime,
                                uuid = transactionHead.uuid
                            ),
                            invoice = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice(
                                businUnitCode = "bb123bb123",//mConfigurations?.address?.businUnitCode,
                                issueDateTime = currentTime,
                                IIC = transactionHead.iic,
                                IICSignature = viewModel.getIKOF()?.second,
                                invNum = "1/2021/dc769ca240",//"${transactionHead.invoiceNum}/${ZonedDateTime.now().year}/${mConfigurations?.device?.tcrCode}",
                                invOrdNum = 1,//transactionHead.invoiceNum,
                                isIssuerInVAT = true,
                                isReverseCharge = false,
                                isSimplifiedInv = false,
                                operatorCode = "oo123oo123", //mConfigurations?.userData?.operatorCode,
                                softCode = "ss123ss123",//mConfigurations?.softCode,
                                TCRCode = "dc769ca240",//mConfigurations?.device?.tcrCode,
                                totPrice = transactionHead.totalWithVat.formatReceipt(),
                                totPriceWoVAT = transactionHead.total.formatReceipt(),
                                totVATAmt = transactionHead.vatAmount.formatReceipt(),
                                typeOfInv = "CASH",
//                                correctiveInv = if (isCorrective) {
//                                    RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.CorrectiveInv(
//                                        IICRef = transactionHead.iic,
//                                        issueDateTime = transactionHead.receiptDateTime,
//                                        type = "CORRECTIVE"
//                                    )
//                                } else {
//                                    null
//                                },
                                payMethods = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods(
                                    payMethod = transactionHead.paymentMethods?.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.PayMethods.PayMethod(
                                            amt = it.amount?.formatReceipt(),
                                            type = it.paymentMethod.toString()
                                        )
                                    }!!
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
                                    item = transactionHead.items?.map {
                                        RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.Items.Item(
                                            code = it.id.toString(),
                                            name = it.itemName,
                                            pa = it.totalWithVat?.formatReceipt(),//it.pa()?.formatReceipt(),
                                            pb = it.total?.formatReceipt(),//it.pb()?.formatReceipt(),
                                            quantity = it.quantity?.formatQuantity(),
                                            rabat = it.rabat?.formatReceipt(),
                                            measureUnit = it.item?.itemUnit,
                                            upb = it.unitPrice?.formatReceipt(),
                                            upa = it.itemPriceWithDiscount?.formatReceipt(),//it.upa()?.formatReceipt(),
                                            va = it.vatAmount?.formatReceipt(),//it.va()?.formatReceipt(),
                                            vatRate = it.vatRate?.formatReceipt()
                                        )
                                    }!!
                                ),
                                sameTaxes = RemoteFiscaliseInvoiceRequest.RegisterInvoiceRequest.Invoice.SameTaxes(
                                    tax = sameTaxes(transactionHead.items).map {
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

}