package com.wind.billypos.view.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.wind.billypos.R
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalance
import com.wind.billypos.data.remote.model.customer.RemoteCustomer
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHead
import com.wind.billypos.data.remote.model.item.RemoteGetItemResponse
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.settings.viewmodel.SyncViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SyncFragment : PreferenceFragmentCompat() {

    private val syncViewModel: SyncViewModel by viewModel()

    private val subCategoriesToSync = mutableListOf<SubCategory>()

    private var notSyncedItems = listOf<Item>()
    private var countItems = 0
    private var syncingItems = false

    private var notSyncedCustomers = listOf<Customer>()
    private var countCustomers = 0
    private var syncingCustomers = false

    private var notSyncedCashBalance = listOf<CashBalance>()
    private var countCashBalance = 0
    private var syncingCashBalance = false

    private var notSyncedInvoices = listOf<TransactionHead>()
    private var countInvoices = 0
    private var syncingInvoices = false

    private var syncingCategories = false

    private var syncingEntityPoints = false

    private var notSyncedOperators = listOf<Operator>()
    private var countOperators = 0
    private var syncingOperators = false

    private var isSyncing = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.sync_preferences, rootKey)

        findPreference<Preference>("sync_key_all")!!.setOnPreferenceClickListener {

            val msg = getString(R.string.sync_all_text)
            val title = getString(R.string.attention)

            context?.let { it1 ->
                ConfirmAlert(it1, object : YesNoDialogInterface {
                    override fun onConfirm(dialog: AlertDialog) {

                    }

                    override fun onCancel(dialog: AlertDialog) {
                    }

                }).showDialog(msg, title)
            }

            true
        }

        findPreference<Preference>("sync_key_items")!!.setOnPreferenceClickListener {

            val msg = getString(R.string.sync_product);
            val title = getString(R.string.attention)
            if (!syncingItems) {
                context?.let { it1 ->
                    ConfirmAlert(it1, object : YesNoDialogInterface {
                        override fun onConfirm(dialog: AlertDialog) {
                            getItems()
                        }

                        override fun onCancel(dialog: AlertDialog) {
                        }
                    }).showDialog(msg, title)
                }
            }
            true
        }

        findPreference<Preference>("sync_key_customers")!!.setOnPreferenceClickListener {

            val msg = getString(R.string.sync_customers)
            val title = getString(R.string.attention)
            if (!syncingCustomers) {
                context?.let { it1 ->
                    ConfirmAlert(it1, object : YesNoDialogInterface {
                        override fun onConfirm(dialog: AlertDialog) {
                            getCustomers()
                        }

                        override fun onCancel(dialog: AlertDialog) {
                        }
                    }).showDialog(msg, title)
                }
            }
            true
        }

        findPreference<Preference>("sync_key_categories")!!.setOnPreferenceClickListener {

            val msg = getString(R.string.sync_category)
            val title = getString(R.string.attention)
            if (!syncingCategories) {
                context?.let { it1 ->
                    ConfirmAlert(it1, object : YesNoDialogInterface {
                        override fun onConfirm(dialog: AlertDialog) {
                            getCategories()
                        }

                        override fun onCancel(dialog: AlertDialog) {}
                    }).showDialog(msg, title)
                }
            }
            true
        }

        findPreference<Preference>("sync_key_transactions")!!.setOnPreferenceClickListener {

            val msg = getString(R.string.sync_transaction)
            val title = getString(R.string.attention)
            context?.let { it1 ->
                ConfirmAlert(it1, object : YesNoDialogInterface {
                    override fun onConfirm(dialog: AlertDialog) {

                    }

                    override fun onCancel(dialog: AlertDialog) {
                    }

                }).showDialog(msg, title)
            }
            true
        }

        findPreference<Preference>("sync_key_cash")!!.setOnPreferenceClickListener {

            val msg = getString(R.string.sync_cash);
            val title = getString(R.string.attention)
            if (!syncingCashBalance) {
                context?.let { it1 ->
                    ConfirmAlert(it1, object : YesNoDialogInterface {
                        override fun onConfirm(dialog: AlertDialog) {
                            getCashBalance()
                        }

                        override fun onCancel(dialog: AlertDialog) {
                        }

                    })
                        .showDialog(msg, title)
                }
            }
            true
        }

        findPreference<Preference>("sync_key_entity_point")!!.setOnPreferenceClickListener {
            val msg = getString(R.string.sync_entity_msg)
            val title = getString(R.string.attention)
            if (!syncingEntityPoints) {
                context?.let { it1 ->
                    ConfirmAlert(it1, object : YesNoDialogInterface {
                        override fun onConfirm(dialog: AlertDialog) {
                            getEntityPoints()
                        }

                        override fun onCancel(dialog: AlertDialog) {}

                    }).showDialog(msg, title)
                }
            }
            true
        }

        findPreference<Preference>("sync_key_operators")!!.setOnPreferenceClickListener {
            val msg = getString(R.string.sync_operators_msg)
            val title = getString(R.string.attention)
            if (!syncingEntityPoints) {
                context?.let { it1 ->
                    ConfirmAlert(it1, object : YesNoDialogInterface {
                        override fun onConfirm(dialog: AlertDialog) {
                            getOperators()
                        }

                        override fun onCancel(dialog: AlertDialog) {}

                    }).showDialog(msg, title)
                }
            }
            true
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Synchronization for items

        val observerNotSyncedItems = Observer<List<Item>> {
            notSyncedItems = it
        }

        val observerSyncedItem = Observer<Item?> {
            countItems++
            it?.let {
                syncViewModel.findItemById(item = it)
            }
            Timber.e("Count Items %s , Not sync items %s", countItems, notSyncedItems.size)
            if (countItems == notSyncedItems.size) {
                findPreference<Preference>("sync_key_items")!!.title =
                    getString(R.string.sync_items)
                syncingItems = false
                isSyncing = false
                countItems = 0
            }
        }

        val observerGetItemsResponse = Observer<List<RemoteGetItemResponse>> {
            syncViewModel.insertItems(items = it)
        }

        val observerSendItems = Observer<Boolean> {
            if (!notSyncedItems.isNullOrEmpty()) {
                notSyncedItems.map {
                    syncViewModel.sendItem(item = it)
                }
            } else {
                findPreference<Preference>("sync_key_items")!!.title =
                    getString(R.string.sync_items)
                syncingItems = false
                isSyncing = false
            }
        }

        val observerErrorHandler = Observer<Throwable> {
            Timber.e("Throwable %s", it)
        }

        syncViewModel.mutableLiveDataErrorHandler.observe(viewLifecycleOwner, observerErrorHandler)

        syncViewModel.mutableLiveDataNotSyncedItems.observe(
            viewLifecycleOwner,
            observerNotSyncedItems
        )
        syncViewModel.mutableLiveDataSyncedItem.observe(viewLifecycleOwner, observerSyncedItem)
        syncViewModel.mutableLiveDataGetItemsResponse.observe(
            viewLifecycleOwner,
            observerGetItemsResponse
        )
        syncViewModel.mutableLiveDataSendItems.observe(viewLifecycleOwner, observerSendItems)
        syncViewModel.getNotSyncedItems()

        // Synchronization for customers

        val observerNotSyncedCustomers = Observer<List<Customer>> {
            notSyncedCustomers = it
        }

        val observerSyncedCustomer = Observer<Customer?> {
            countCustomers++
            it?.let {
                syncViewModel.findCustomerByUUID(customer = it)
            }

            if (countCustomers == notSyncedCustomers.size) {
                findPreference<Preference>("sync_key_customers")!!.title =
                    getString(R.string.sync_customers)
                syncingCustomers = false
                isSyncing = false
                countCustomers = 0
            }

        }

        val observerGetCustomersResponse = Observer<List<RemoteCustomer>> {
            syncViewModel.insertCustomers(customers = it)
        }

        val observerSendCustomers = Observer<Boolean> {
            if (!notSyncedCustomers.isNullOrEmpty()) {
                notSyncedCustomers.map {
                    syncViewModel.sendCustomer(customer = it)
                }
            } else {
                findPreference<Preference>("sync_key_customers")!!.title =
                    getString(R.string.sync_customers)
                syncingCustomers = false
                isSyncing = false
            }
        }

        syncViewModel.mutableLiveDataNotSyncedCustomers.observe(
            viewLifecycleOwner,
            observerNotSyncedCustomers
        )
        syncViewModel.mutableLiveDataSyncedCustomer.observe(
            viewLifecycleOwner,
            observerSyncedCustomer
        )
        syncViewModel.mutableLiveDataGetCustomersResponse.observe(
            viewLifecycleOwner,
            observerGetCustomersResponse
        )
        syncViewModel.mutableLiveDataSendCustomers.observe(
            viewLifecycleOwner,
            observerSendCustomers
        )
        syncViewModel.getNotSyncedCustomers()

//     Synchronization for Categories

        val observerCategories = Observer<List<Category>?> {
            it?.let { categories ->
                categories.map { category ->
                    category.subcategories.map { subCategory ->
                        subCategoriesToSync.add(subCategory)
                    }
                }
                syncViewModel.insertCategories(categories = categories)
            }
        }

        val observerInsertedCategories = Observer<Boolean> {
            if (!subCategoriesToSync.isNullOrEmpty()) {
                syncViewModel.insertSubCategories(subCategories = subCategoriesToSync)
            } else {
                findPreference<Preference>("sync_key_categories")!!.title =
                    getString(R.string.sync_categories)
                syncingCategories = false
                isSyncing = false
            }
        }

        val observerInsertedSubCategories = Observer<Boolean> {
            findPreference<Preference>("sync_key_categories")!!.title =
                getString(R.string.sync_categories)
            syncingCategories = false
            isSyncing = false
        }

        syncViewModel.mutableLiveDataCategories.observe(viewLifecycleOwner, observerCategories)
        syncViewModel.mutableLiveDataInsertedSubCategories.observe(
            viewLifecycleOwner,
            observerInsertedSubCategories
        )
        syncViewModel.mutableLiveDataInsertedCategories.observe(
            viewLifecycleOwner,
            observerInsertedCategories
        )

        //        Synchronization for Cash Balance

        val observerNotSyncedCashBalance = Observer<List<CashBalance>> {
            notSyncedCashBalance = it
        }

        val observerSyncedCashBalance = Observer<CashBalance?> {
            countCashBalance++
            it?.let {
                syncViewModel.findCashBalanceByUUID(cashBalance = it)
            }

            if (countCashBalance == notSyncedCashBalance.size) {
                findPreference<Preference>("sync_key_cash")!!.title =
                    getString(R.string.sync_cash_balanc)
                syncingCashBalance = false
                isSyncing = false
                countCashBalance = 0
            }

        }

        val observerGetCashBalanceResponse = Observer<List<RemoteCashBalance>> {
            syncViewModel.insertCashBalance(cashBalance = it)
        }

        val observerSendCashBalance = Observer<Boolean> {
            if (!notSyncedCashBalance.isNullOrEmpty()) {
                notSyncedCashBalance.map {
                    syncViewModel.sendCashBalance(cashBalance = it)
                }
            } else {
                findPreference<Preference>("sync_key_cash")!!.title =
                    getString(R.string.sync_cash_balanc)
                syncingCashBalance = false
                isSyncing = false
            }
        }

        syncViewModel.mutableLiveDataNotSyncedCashBalance.observe(
            viewLifecycleOwner,
            observerNotSyncedCashBalance
        )
        syncViewModel.mutableLiveDataSyncedCashBalance.observe(
            viewLifecycleOwner,
            observerSyncedCashBalance
        )
        syncViewModel.mutableLiveDataGetCashBalanceResponse.observe(
            viewLifecycleOwner,
            observerGetCashBalanceResponse
        )
        syncViewModel.mutableLiveDataSendCashBalance.observe(
            viewLifecycleOwner,
            observerSendCashBalance
        )
        syncViewModel.getNotSyncedCashBalance()

        //        Synchronization for Invoices

        val observerNotSyncedInvoices = Observer<List<TransactionHead>> {
            notSyncedInvoices = it
        }

        val observerSyncedInvoice = Observer<TransactionHead?> {
            countInvoices++
            it?.let {
                syncViewModel.findInvoiceByUUID(invoice = it)
            }

            if (countInvoices == notSyncedInvoices.size) {
                findPreference<Preference>("sync_key_transactions")!!.title =
                    getString(R.string.sync_transactions)
                syncingInvoices = false
                isSyncing = false
                countInvoices = 0
            }

        }

        val observerGetInvoicesResponse = Observer<List<RemoteTransactionHead>> {
            syncViewModel.insertInvoices(invoices = it)
        }

        val observerSendInvoices = Observer<Boolean> {
            if (!notSyncedInvoices.isNullOrEmpty()) {
                notSyncedInvoices.map {
                    syncViewModel.sendInvoice(invoice = it)
                }
            } else {
                findPreference<Preference>("sync_key_transactions")!!.title =
                    getString(R.string.sync_transactions)
                syncingInvoices = false
                isSyncing = false
            }
        }

        syncViewModel.mutableLiveDataNotSyncedInvoices.observe(
            viewLifecycleOwner,
            observerNotSyncedInvoices
        )
        syncViewModel.mutableLiveDataSyncedInvoice.observe(
            viewLifecycleOwner,
            observerSyncedInvoice
        )
//        syncViewModel.mutableLiveDataGetInvoicesResponse.observe(
//            viewLifecycleOwner,
//            observerGetInvoicesResponse
//        )
        syncViewModel.mutableLiveDataSendInvoices.observe(
            viewLifecycleOwner,
            observerSendInvoices
        )
        syncViewModel.getNotSyncedInvoices()

        //     Synchronization for Entity Points

        val observerEntityPoints = Observer<List<EntityPoint>?> {
            syncViewModel.insertEntityPoints(entityPoints = it)
        }


        val observerInsertedEntityPoints = Observer<Boolean> {
            findPreference<Preference>("sync_key_entity_point")!!.title =
                getString(R.string.sync_entity_point)
            syncingCategories = false
            isSyncing = false
        }



        syncViewModel.mutableLiveDataEntityPoints.observe(viewLifecycleOwner, observerEntityPoints)
        syncViewModel.mutableLiveDataInsertedEntityPoints.observe(
            viewLifecycleOwner,
            observerInsertedEntityPoints
        )

//        Synchronization for Operators

        val observerOperators = Observer<List<Operator>?> {
            if (it != null) {
                syncViewModel.insertOperators(operators = it)
            }
        }


        val observerInsertedOperators = Observer<Boolean> {
            findPreference<Preference>("sync_key_operators")!!.title =
                getString(R.string.sync_operators)
            syncingOperators = false
            isSyncing = false
        }

        syncViewModel.mutableLiveDataOperators.observe(viewLifecycleOwner, observerOperators)
        syncViewModel.mutableLiveDataInsertedOperators.observe(
            viewLifecycleOwner,
            observerInsertedOperators
        )

    }

    private fun getItems() {
        findPreference<Preference>("sync_key_items")!!.title = getString(
            R.string.sync_items
        ) + " : syncing"
        syncingItems = true
        isSyncing = true
        syncViewModel.getItems()
    }

    private fun getCustomers() {
        findPreference<Preference>("sync_key_customers")!!.title = getString(
            R.string.sync_customers
        ) + " : syncing"
        syncingCustomers = true
        isSyncing = true
        syncViewModel.getCustomers()
    }

    private fun getCategories() {
        findPreference<Preference>("sync_key_categories")!!.title = getString(
            R.string.sync_categories
        ) + " : syncing"
        syncingCategories = true
        isSyncing = true
        syncViewModel.getCategories()
    }

    private fun getCashBalance() {
        findPreference<Preference>("sync_key_cash")!!.title = getString(
            R.string.sync_cash_balanc
        ) + " : syncing"
        syncingCashBalance = true
        isSyncing = true
        syncViewModel.getCashBalance()
    }

    private fun getEntityPoints() {
        findPreference<Preference>("sync_key_entity_point")!!.title = getString(
            R.string.sync_entity_point
        ) + " : syncing"
        syncingEntityPoints = true
        isSyncing = true
        syncViewModel.getEntityPoints()
    }

    private fun getOperators() {
        findPreference<Preference>("sync_key_operators")!!.title = getString(
            R.string.sync_operators
        ) + " : syncing"
        syncingOperators = true
        isSyncing = true
        syncViewModel.getOperators()
    }

}