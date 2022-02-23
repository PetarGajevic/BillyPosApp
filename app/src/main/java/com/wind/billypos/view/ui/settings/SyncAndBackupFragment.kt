package com.wind.billypos.view.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.wind.billypos.R
import com.wind.billypos.BR
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentSyncAndBackupBinding
import com.wind.billypos.view.ui.settings.viewmodel.SyncAndBackupViewModel
import kotlinx.android.synthetic.main.fragment_sync_and_backup.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SyncAndBackupFragment: BaseFragment<FragmentSyncAndBackupBinding, SyncAndBackupViewModel>() {
    private val syncAndBackupViewModel : SyncAndBackupViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.syncAndBackupViewModel
    override val layoutId: Int
        get() = R.layout.fragment_sync_and_backup
    override val viewModel: SyncAndBackupViewModel
        get() = syncAndBackupViewModel

    private var allTransactions = 0
    private var syncedTransactions = 0
    private var fiscalizedTransactions = 0

    private var allCashBalance = 0
    private var syncedCashBalance = 0
    private var fiscalizedCashBalance = 0

    private var allItems = 0
    private var syncedItems = 0

    private var allCustomers = 0
    private var syncedCustomers = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observerAllTransactions = Observer<Int> {
            allTransactions = it
            txtTotalTransactionsLocally.text = String.format(getString(R.string.local_transactions_report), allTransactions)
            viewModel.getFiscalizedTransactions()
            viewModel.getSyncedTransactions()
        }

        val observerAllCashBalance = Observer<Int> {
            allCashBalance = it
            txtTotalCashBalanceLocally.text = String.format(getString(R.string.local_cash_balance_report), allCashBalance)
            viewModel.getFiscalizedCashBalance()
            viewModel.getSyncedCashBalance()
        }

        val observerAllItems = Observer<Int> {
            allItems = it
            viewModel.getSyncedItems()
        }

        val observerAllCustomers = Observer<Int> {
            allCustomers = it
            viewModel.getSyncedCustomers()
        }

        val observerSyncedTransactions = Observer<Int> {
            syncedTransactions = it
            txtTotalTransactionsSynced.text = String.format(getString(R.string.synced_transactions_report), syncedTransactions, allTransactions)
        }

        val observerSyncedCashBalance = Observer<Int> {
            syncedCashBalance = it
            txtTotalCashBalanceSynced.text = String.format(getString(R.string.synced_transactions_report), syncedCashBalance, allCashBalance)
        }

        val observerSyncedItems = Observer<Int> {
            syncedItems = it
            txtTotalItemsLocally.text = String.format(getString(R.string.local_items_report), syncedItems, allItems)
        }

        val observerSyncedCustomers = Observer<Int> {
            syncedCustomers = it
            txtTotalCustomersLocally.text = String.format(getString(R.string.local_customers_report), syncedCustomers, allCustomers)
        }

        val observerFiscalizedTransactions = Observer<Int> {
            fiscalizedTransactions = it
            txtTotalTransactionsFiscalised.text = String.format(getString(R.string.fiscalised_transactions_report), fiscalizedTransactions, allTransactions)
        }

        val observerFiscalizedCashBalance = Observer<Int> {
            fiscalizedCashBalance = it
            txtTotalCashBalanceFiscalised.text = String.format(getString(R.string.fiscalised_transactions_report), fiscalizedCashBalance, allCashBalance)
        }

        // All
        viewModel.mutableLiveDataAllTransactions.observe(viewLifecycleOwner, observerAllTransactions)
        viewModel.mutableLiveDataAllCashBalance.observe(viewLifecycleOwner, observerAllCashBalance)
        viewModel.mutableLiveDataAllItems.observe(viewLifecycleOwner, observerAllItems)
        viewModel.mutableLiveDataAllCustomers.observe(viewLifecycleOwner, observerAllCustomers)

        // Synced
        viewModel.mutableLiveDataSyncedTransactions.observe(viewLifecycleOwner, observerSyncedTransactions)
        viewModel.mutableLiveDataSyncedCashBalance.observe(viewLifecycleOwner, observerSyncedCashBalance)
        viewModel.mutableLiveDataSyncedItems.observe(viewLifecycleOwner, observerSyncedItems)
        viewModel.mutableLiveDataSyncedCustomers.observe(viewLifecycleOwner, observerSyncedCustomers)

        // Fiscalized
        viewModel.mutableLiveDataFiscalizedTransactions.observe(viewLifecycleOwner, observerFiscalizedTransactions)
        viewModel.mutableLiveDataFiscalizedCashBalance.observe(viewLifecycleOwner, observerFiscalizedCashBalance)


        viewModel.getAllTransactions()
        viewModel.getAllCashBalance()
        viewModel.getAllItems()
        viewModel.getAllCustomers()
    }

}