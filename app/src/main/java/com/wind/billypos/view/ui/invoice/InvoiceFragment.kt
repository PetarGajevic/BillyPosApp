package com.wind.billypos.view.ui.invoice

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.databinding.FragmentInvoiceBinding
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.DeviceId
import com.wind.billypos.utils.IOnBackPressed
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.utils.PreferenceHelper.customPreference
import com.wind.billypos.utils.PreferenceHelper.deviceId
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.*
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.MainActivity
import com.wind.billypos.view.ui.invoice.adapter.CategoryAdapter
import com.wind.billypos.view.ui.invoice.adapter.ItemGridAdapter
import com.wind.billypos.view.ui.invoice.adapter.SubCategoryAdapter
import com.wind.billypos.view.ui.invoice.viewmodel.InvoiceViewModel
import kotlinx.android.synthetic.main.fragment_invoice.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class InvoiceFragment : BaseFragment<FragmentInvoiceBinding, InvoiceViewModel>() {

    private val invoiceViewModel: InvoiceViewModel by viewModel()
    private val homeSharedViewModel: HomeSharedViewModel by viewModel()

    private var transactionBodyList = mutableListOf<TransactionBody>()

    private var mItemsSelected: ArrayList<Item> = ArrayList()
    private lateinit var transactionHead: TransactionHead
    private var invoiceNum: Long = 1
    private lateinit var configuration: Configuration
    private var correctiveTransactionHead: TransactionHead? = null
    private var correctiveTransaction: Boolean = false

    private val args: InvoiceFragmentArgs by navArgs()

    override val bindingVariable: Int
        get() = BR.invoiceViewModel
    override val layoutId: Int
        get() = R.layout.fragment_invoice
    override val viewModel: InvoiceViewModel
        get() = invoiceViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // New Sale
        if (args.newSale) {
            startNewSale()
        }
        arguments?.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        if(args.order != null){
            homeSharedViewModel.setEntityPoint(entityPoint = args.order!!)
        }

        // Corrective Invoice
        if (args.transactionHead != null) {
            correctiveTransactionHead = args.transactionHead
            correctiveTransaction = true
            setCorrectiveTransactionHead(trnHead = correctiveTransactionHead!!)
            homeSharedViewModel.getTransactionBodyList(correctiveTransactionHead!!.uuid)
            homeSharedViewModel.mutableLiveDataIsCorrective.value = true
        }

        // Invoice Order Number
        val observerInvoiceNum = Observer<Long> {
            invoiceNum = it
        }


        // Get Configuration
        PreferenceHelper.customPreference(requireActivity()).deviceId =
            DeviceId(requireContext()).myDeviceId()
        val prefs = customPreference(requireActivity())
        configuration = prefs.configurations


        // Checkout Click listener
        btnPosCheckout.setOnClickListener {
            if (this::transactionHead.isInitialized && transactionBodyList.size > 0) {
                findNavController().navigateSafe(
                    InvoiceFragmentDirections.actionInvoiceFragmentToCartFragment(
                        transactionHead = homeSharedViewModel.getTransactionHead()!!
                    )
                )
            }
        }

        // Clear items
        btnPosClear.setOnClickListener {
            startNewSale()
        }

        // Show all items
        cvAllItems.setOnClickListener {
            getAllItems()
            viewModel.hideSubCategories()
        }

        val categoryAdapter = CategoryAdapter(
            categoryClick = { id, _, _ ->
                getSubCategories(id = id)
                getItemsByCategory(id = id)
            }
        )

        val subCategoryAdapter = SubCategoryAdapter(
            subCategoryClick = { id, _, _ ->
                getItemsBySubCategory(subId = id)
            }
        )

        val itemGridAdapter = ItemGridAdapter(
            itemClick = { _, item, _ ->
                onItemClick(item = item)
            }
        )

        recyclerViewCategories.adapter = categoryAdapter
        recyclerViewSubCategories.adapter = subCategoryAdapter
        recyclerview_pos.adapter = itemGridAdapter

        val observerCategories = Observer<List<Category>> {
            it.let(categoryAdapter::submitList)
        }

        val observerSubCategories = Observer<List<SubCategory>> {
            it.let(subCategoryAdapter::submitList)
        }

        val observerItems = Observer<List<Item>> {
            it.let(itemGridAdapter::submitList)
        }

        val observerTransactionHeader = Observer<TransactionHead> {
            transactionHead = it.copy(
                invoiceNo = invoiceNum
            )
            Timber.e("Transaction Head %s", transactionHead)
        }

        val observerTransactionBodyList = Observer<MutableList<TransactionBody>?> { list ->
            Timber.e("observerTransactionBodyList %s", list)
            transactionBodyList = list
            var itemsQuantity = 0.0
            list.map {
                it.quantity?.let { it1 -> itemsQuantity = itemsQuantity.plus(it1) }
            }
            btnPosCheckout.text = itemsQuantity.toString()
        }


        viewModel.mutableLiveDataCategories.observe(viewLifecycleOwner, observerCategories)
        viewModel.mutableLiveDataSubCategories.observe(viewLifecycleOwner, observerSubCategories)
        viewModel.mutableLiveDataItems.observe(viewLifecycleOwner, observerItems)
        viewModel.mutableLiveDataInvoiceNum.observe(viewLifecycleOwner, observerInvoiceNum)
        homeSharedViewModel.mutableLivaDataTransactionHead.observe(
            viewLifecycleOwner,
            observerTransactionHeader
        )
        homeSharedViewModel.mutableLiveDataTransactionBodyList.observe(
            viewLifecycleOwner,
            observerTransactionBodyList
        )

        getAllCategories()
        getAllItems()
        viewModel.getLastInvoiceNum()
    }

    private fun getSubCategories(id: Long) = viewModel.getSubCategories(id = id)

    private fun getItemsByCategory(id: Long) = viewModel.getItemsByCategory(id = id)

    private fun getAllItems() = viewModel.getAllItems()

    private fun getAllCategories() = viewModel.getAllCategories()

    private fun getItemsBySubCategory(subId: Long) = viewModel.getItemsBySubCategory(subId = subId)

    private fun onItemClick(item: Item) {
        addSelectedItem(item = item)

        if (!this::transactionHead.isInitialized) {
            Timber.e("Nije inicijalizovan")
            transactionHead = TransactionHead()
            transactionHead = transactionHead.copy(
                idCompany = configuration.company?.id,
                idDevice = configuration.device?.id,
                idAddress = configuration.address?.id,
                idUser = configuration.userData?.id,
                total = 0.0,
                totalWithVat = 0.0,
                invoiceNo = invoiceNum,
                deviceTCR = configuration.device?.tcrCode,
                invoiceNum = transactionHead.nextInvoiceNo()
            )
        }

        homeSharedViewModel.insert(trnHead = transactionHead, item = item)
        //insertTransactionHead(trnHead = transactionHead, item = item)
    }

    private fun addSelectedItem(item: Item?) {
        homeSharedViewModel.addSelectedItem(item = item)
        viewModel.addSelectedItem(item = item)
    }

    private fun insertTransactionHead(trnHead: TransactionHead, item: Item) =
        viewModel.insert(trnHead = trnHead, item = item)


    private fun startNewSale() {
        Timber.e("Start new sale")
        homeSharedViewModel.setTransactionBodyList()
        homeSharedViewModel.setTransactionHead(TransactionHead())
        homeSharedViewModel.setCustomer(null)
        homeSharedViewModel.setEntityPoint(null)
        correctiveTransaction = false
    }

    private fun setCorrectiveTransactionHead(trnHead: TransactionHead) =
        homeSharedViewModel.setCorrectiveTransactionHead(trnHead = trnHead)

    private fun setItemsSelected(items: List<Item>) =
        viewModel.setItemsSelected(items = items)

}