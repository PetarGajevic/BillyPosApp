package com.wind.billypos.view.ui.invoice

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.res.TypedArrayUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentCartBinding
import com.wind.billypos.utils.WrapContentLinearLayoutManager
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.utils.getNavigationResult
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.invoice.adapter.TransactionBodyAdapter
import com.wind.billypos.view.ui.invoice.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>() {

    private val cartViewModel: CartViewModel by viewModel()
    private val homeSharedViewModel: HomeSharedViewModel by viewModel()
    private val args: CartFragmentArgs by navArgs()

    private var transactionList: MutableList<TransactionBody>? = null

    private var positionItem = 0

    override val bindingVariable: Int
        get() = BR.cartViewModel
    override val layoutId: Int
        get() = R.layout.fragment_cart
    override val viewModel: CartViewModel
        get() = cartViewModel

    private var quantity = 0.0



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPosCheckoutSave.setOnClickListener {
            findNavController().navigateSafe(
                CartFragmentDirections.actionCartFragmentToCartPaymentFragment(
                    transactionHead = homeSharedViewModel.getTransactionHead()
                )
            )
        }

        imgClearSelectedCustomer.setOnClickListener {
            homeSharedViewModel.setCustomer(customer = null)
        }

        btnPosCheckoutOptions.setOnClickListener {
            findNavController().navigateSafe(CartFragmentDirections.actionCartFragmentToTransactionOptionsFragment())
        }

        setTransactionHead(args.transactionHead)

        if(args.customer != null){
            homeSharedViewModel.setCustomer(customer = args.customer)
        }

        val transactionBodyAdapter = TransactionBodyAdapter(
            transactionBodyClick = { _, transactionBody, position ->
                positionItem = position
                findNavController().navigateSafe(
                    CartFragmentDirections.actionCartFragmentToTransactionItemBottomSheetDialogFragment(
                        transactionItem = transactionBody
                    )
                )
            }
        )

        // initialize an instance of linear layout manager
        val layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        ).apply {
            // specify the layout manager for recycler view
            recyclerview_pos_cart.layoutManager = this
        }

        // initialize an instance of divider item decoration
        DividerItemDecoration(
            requireContext(), // context
            layoutManager.orientation
        ).apply {
            // add divider item decoration to recycler view
            // this will show divider line between items
            recyclerview_pos_cart.addItemDecoration(this)
        }



        recyclerview_pos_cart.adapter = transactionBodyAdapter


        transactionList = homeSharedViewModel.getTransactions()!!
        Timber.e("TList %s" , transactionList)
        if (transactionList.isNullOrEmpty()) {
            findNavController().popBackStack()
        }
        transactionList.let(transactionBodyAdapter::submitList)
        quantity = 0.0
        transactionList?.map {
            quantity += it.quantity!!
        }
        setTotalOnButton(quantity = quantity, total = args.transactionHead.totalWithVat)


        val observerTransactionBodyList = Observer<List<TransactionBody>> {
            Timber.e("Usao na tran bod")
            transactionList = homeSharedViewModel.getTransactions()!!
            if (transactionList.isNullOrEmpty()) {
                findNavController().popBackStack()
            }
            transactionBodyAdapter.submitList(null)
            transactionList.let(transactionBodyAdapter::submitList)
            quantity = 0.0
            transactionList?.map {
                quantity += it.quantity!!
            }
            setTotalOnButton(quantity = quantity, total = args.transactionHead.totalWithVat)
        }


        val observerTransactionUpdatedBodyList = Observer<List<TransactionBody>> {
            updateTransactionHead(it)
        }

        val observerUpdatedList = Observer<List<TransactionBody>> {
            it.map {
                Timber.e("Cijena: %s", it.itemPriceWithDiscount)
            }
            updateTransactionHead(it)
        }

        val observerRefreshList = Observer<Boolean> {
            Timber.e("Observer refresh List")
            setTotalOnButton(
                quantity = quantity,
                total = viewModel.getTransactionHead()?.totalWithVat!!
            )
        }

        val observerCustomer = Observer<Customer?> {
            Timber.e("Customer %s", it)
            if(it != null){
                txtClientName.text = it.name
                layChosenCustomer.visibility = View.VISIBLE
            }else{
                layChosenCustomer.visibility = View.GONE
            }
        }


        getNavigationResult(REFRESH_DATA)?.observe(viewLifecycleOwner) { _ ->
            transactionList = homeSharedViewModel.getTransactions()
            Timber.e("Transaction list before %s", transactionList)
            if (transactionList.isNullOrEmpty()) {
                Timber.e("Usao u pop back stack")
                findNavController().popBackStack()
            }
            transactionList.let(transactionBodyAdapter::submitList)
            transactionBodyAdapter.notifyDataSetChanged()
            quantity = 0.0
            transactionList?.map {
                quantity += it.quantity!!
            }
            homeSharedViewModel.calculateTransactionHead(homeSharedViewModel.getTransactions()!!)
            setTotalOnButton(quantity = quantity, total = homeSharedViewModel.getTransactionHead()?.totalWithVat!!)
        }

//        viewModel.mutableLiveDataCallTransactionBody.observe(
//            viewLifecycleOwner,
//            observerCallTransactionBody
//        )
        viewModel.mutableLiveDataTransactionBodyList.observe(
            viewLifecycleOwner,
            observerTransactionBodyList
        )
        viewModel.mutableLiveDataTransactionUpdatedBodyList.observe(
            viewLifecycleOwner,
            observerTransactionUpdatedBodyList
        )
        viewModel.mutableLiveDataRefreshList.observe(viewLifecycleOwner, observerRefreshList)
        viewModel.mutableLiveDataTransactionUpdatedBodyList.observe(
            viewLifecycleOwner,
            observerUpdatedList
        )
        homeSharedViewModel.mutableLiveDataCustomer.observe(viewLifecycleOwner, observerCustomer)

        homeSharedViewModel.getTransactions()
    }


    private fun setTransactionHead(trnHead: TransactionHead?) =
        viewModel.setTransactionHead(trnHead = trnHead)

    private fun getTransactionHeadUUID() = viewModel.getTransactionHeadUUID()

    private fun getTransactions(transactionUUID: String?) =
        viewModel.getTransactions(transactionUUID = transactionUUID)

    @SuppressLint("StringFormatMatches")
    private fun setTotalOnButton(quantity: Double, total: Double) {
        val text: String = getString(R.string.btn_cart_total, quantity.toString(), total.toString())
        btnPosCheckoutSave.text = text
    }

    private fun updateTransactionHead(trnBodyList: List<TransactionBody>) {
        var total = 0.0
        var totalWithVat = 0.0
        var vatAmount = 0.0
        trnBodyList.map { trnBody ->
            total += trnBody.total!!
            totalWithVat += trnBody.totalWithVat!!
            vatAmount += trnBody.total - trnBody.totalWithVat
            Timber.e("Body  %s", trnBody)
        }
        Timber.e("Total  $total Total with vat $totalWithVat vat amount $vatAmount")
        val trnHead = viewModel.getTransactionHead()
        val transaction = trnHead?.copy(
            total = total,
            totalWithVat = totalWithVat,
            vatAmount = vatAmount
        )
        Timber.e("Prije set updated %s", transaction)
        viewModel.setUpdatedTransactionHead(trnHead = transaction)
        //viewModel.insertTransactionHead(trnHead = transaction)
    }

}