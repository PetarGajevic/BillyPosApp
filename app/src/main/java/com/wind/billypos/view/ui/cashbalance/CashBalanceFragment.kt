package com.wind.billypos.view.ui.cashbalance

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalance
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositRequest
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositResponse
import com.wind.billypos.databinding.FragmentCashBalanceBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.utils.getNavigationResult
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.ui.cashbalance.viewmodel.CashBalanceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.math.abs

class CashBalanceFragment : BaseFragment<FragmentCashBalanceBinding, CashBalanceViewModel>() {

    private val cashBalanceViewModel: CashBalanceViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.cashBalanceViewModel
    override val layoutId: Int
        get() = R.layout.fragment_cash_balance
    override val viewModel: CashBalanceViewModel
        get() = cashBalanceViewModel

    private var unfiscalizedCashBalanceList = listOf<CashBalance>()

    private var notSyncedCashBalance = listOf<CashBalance>()
    private var countCashBalance = 0
    private var syncingCashBalance = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNavigationResult(REFRESH_DATA)?.observe(viewLifecycleOwner) { _ ->
            viewModel.updateCashBalanceView()
            viewModel.getUnfiscalizedCashBalance()
            viewModel.getNotSyncedCashBalance()
        }

        val observerUnfiscalizedCashBalance = Observer<List<CashBalance>> {
            Timber.e("Lista cash balance %s", it)
            unfiscalizedCashBalanceList = it
        }

        val observerSignedDocument = Observer<String> {
            it?.let {
                viewModel.fiscalCashDeposit(cashDeposit = it)
            }
        }

        val observerCashBalanceResponse = Observer<RemoteCashDepositResponse.SoapBodyCashDepositResponse?> {
                it?.let{
                    viewModel.findCashBalanceByUUID(cashDeposit = it)
                }
        }

        //        Synchronization for Cash Balance

        val observerNotSyncedCashBalance = Observer<List<CashBalance>> {
            notSyncedCashBalance = it
            Timber.e("Lista ne sihronizovanih %s", notSyncedCashBalance)
        }

        val observerSyncedCashBalance = Observer<CashBalance?> {
            countCashBalance++
            it?.let {
                viewModel.findCashBalanceByUUID(cashBalance = it)
            }

            if (countCashBalance == notSyncedCashBalance.size) {
                syncingCashBalance = false
                countCashBalance = 0
                Timber.e("Zavrsio sihronizaciju")
            }

        }

        val observerGetCashBalanceResponse = Observer<List<RemoteCashBalance>> {
            viewModel.insertCashBalance(cashBalance = it)
        }

        val observerSendCashBalance = Observer<Boolean> {
            if (!notSyncedCashBalance.isNullOrEmpty()) {
                notSyncedCashBalance.map {
                    viewModel.sendCashBalance(cashBalance = it)
                }
            } else {
                syncingCashBalance = false
            }
        }

        viewModel.mutableLiveDataUnfiscalizedCashBalance.observe(viewLifecycleOwner, observerUnfiscalizedCashBalance)
        viewModel.mutableLiveDataSignedDocument.observe(viewLifecycleOwner, observerSignedDocument)
        viewModel.mutableLiveDataCashBalanceResponse.observe(viewLifecycleOwner, observerCashBalanceResponse)

        viewModel.mutableLiveDataNotSyncedCashBalance.observe(
            viewLifecycleOwner,
            observerNotSyncedCashBalance
        )
        viewModel.mutableLiveDataSyncedCashBalance.observe(
            viewLifecycleOwner,
            observerSyncedCashBalance
        )
        viewModel.mutableLiveDataGetCashBalanceResponse.observe(
            viewLifecycleOwner,
            observerGetCashBalanceResponse
        )
        viewModel.mutableLiveDataSendCashBalance.observe(
            viewLifecycleOwner,
            observerSendCashBalance
        )

        viewModel.getUnfiscalizedCashBalance()
        viewModel.getNotSyncedCashBalance()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_cash_balance -> {
                findNavController().navigateSafe(CashBalanceFragmentDirections.actionCashBalanceFragmentToCashBalanceBottomSheetDialogFragment())
                true
            }
            R.id.action_send_unsynced -> {
                viewModel.getCashBalance()
                true
            }
            R.id.action_send_unfiscalised -> {
                fiscalizeCashBalance(cashBalanceList = unfiscalizedCashBalanceList)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cash_balance, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun fiscalizeCashBalance(cashBalanceList: List<CashBalance>){
            cashBalanceList.map {
                viewModel.signDocument(
                    RemoteCashDepositRequest(
                        body = RemoteCashDepositRequest.SoapBodyCashDeposit(
                            registerCashDepositRequest = RemoteCashDepositRequest.RegisterCashDepositRequest(
                                header = RemoteCashDepositRequest.RegisterCashDepositRequest.HeaderCashDeposit(
                                    UUID = it.uuid
                                ),
                                cashDeposit = RemoteCashDepositRequest.RegisterCashDepositRequest.CashDeposit(
                                    cashAmt = abs(it.cashAmount!!).formatReceipt(),
                                    issuerNUIS = "L82210031Q",
                                    operation = it.operation,
                                    TCRCode = "wx026fv564"
                                )
                            )
                        )
                    ), RemoteCashDepositRequest.ELEMENT_TO_SIGN
                )
            }
    }

}