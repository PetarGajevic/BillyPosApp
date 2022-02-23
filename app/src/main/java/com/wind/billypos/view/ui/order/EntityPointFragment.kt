package com.wind.billypos.view.ui.order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentEntityPointBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.utils.formatTwoDecimal
import com.wind.billypos.view.model.EntityPoint
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.MainActivity
import com.wind.billypos.view.ui.order.adapter.TransactionOrderAdapter
import com.wind.billypos.view.ui.order.viewmodel.EntityPointViewModel
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_entity_point.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EntityPointFragment : BaseFragment<FragmentEntityPointBinding, EntityPointViewModel>() {

    private val entityPointViewModel: EntityPointViewModel by viewModel()

    private var hasOpenedDay = false
    private var invoiceNo: Long = 1
    var entity = EntityPoint()

    private var isOrderSelected = false
    private var selectedOrdersList = mutableListOf<TransactionHead>()
    private var transactionList = mutableListOf<TransactionHead>()

    private lateinit var transactionOrderAdapter: TransactionOrderAdapter

    override val bindingVariable: Int
        get() = BR.entityPointViewModel
    override val layoutId: Int
        get() = R.layout.fragment_entity_point
    override val viewModel: EntityPointViewModel
        get() = entityPointViewModel

    private val args: EntityPointFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entity = args.entityPoint
        activity?.title = entity.entityName

        transactionOrderAdapter = TransactionOrderAdapter(
            orderTransactionClick = { _, trnHead, index ->
//                Selecting orders by click
                if (isOrderSelected) {
                    if (trnHead.isOrderSelected) {
                        val transactionHead = trnHead.copy(isOrderSelected = false)
                        selectedOrdersList.remove(trnHead)
                        transactionList[index] = transactionHead

                        if (selectedOrdersList.isEmpty()) {
                            mbNewOrderInvoice.setText(R.string.new_order_invoice)
                            isOrderSelected = false
                        }
                    } else {
                        val transactionHead = trnHead.copy(isOrderSelected = true)
                        transactionList[index] = transactionHead
                        selectedOrdersList.add(transactionHead)
                    }
                    updateAdapterList(transactionList)
                }else{
                    findNavController().navigateSafe(EntityPointFragmentDirections.actionEntityPointFragmentToTransactionDetailsFragment(transaction = trnHead))
                }
            },
            orderTransactionLongClick = { _, trnHead, index ->
//              Selecting first order by long click
                isOrderSelected = true
                val transactionHead = trnHead.copy(isOrderSelected = true)
                selectedOrdersList.add(transactionHead)
                transactionList[index] = transactionHead
                mbNewOrderInvoice.setText(R.string.close_selection)
                updateAdapterList(transactionList)
            },
            orderTransactionInfoClick = { _, _, _ ->
                Toast.makeText(requireContext(), getString(R.string.this_is_order_invoice), Toast.LENGTH_LONG).show()
            }
        )

        addDecorLine()

        recyclerViewTransDetails.adapter = transactionOrderAdapter

        mbNewOrderInvoice.setOnClickListener {
            if (!hasOpenedDay) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.has_open_day_error),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

//            Close selection click
            if(isOrderSelected){
                transactionList = transactionList.map {
                    it.copy(isOrderSelected = false)
                } as MutableList<TransactionHead>

                updateAdapterList(transactionList)
                selectedOrdersList.removeAll(selectedOrdersList)
                isOrderSelected = false
                mbNewOrderInvoice.setText(R.string.new_order_invoice)
                return@setOnClickListener
            }


            findNavController().navigateSafe(
                EntityPointFragmentDirections.actionEntityPointFragmentToInvoiceFragment(
                    newSale = true,
                    order = entity
                )
            )
        }

        mbPayOrder.setOnClickListener {
            if (!hasOpenedDay) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.has_open_day_error),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if(transactionList.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    getString(R.string.first_create_new_order),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }


            if(isOrderSelected){
                val transactionHead = createSummaryInvoice(selectedOrdersList)
                findNavController().navigateSafe(EntityPointFragmentDirections.actionEntityPointFragmentToCartPaymentFragment(transactionHead = transactionHead))
            }else{
                val transactionHead = createSummaryInvoice(transactionList)
                findNavController().navigateSafe(EntityPointFragmentDirections.actionEntityPointFragmentToCartPaymentFragment(transactionHead = transactionHead))
            }
        }

        val observerHasOpenedDay = Observer<Boolean> {
            hasOpenedDay = it
        }

        // Invoice Order Number
        val observerInvoiceNum = Observer<Long> {
            invoiceNo = it
        }

        val observerEntityTransactionList = Observer<List<TransactionHead>> { list ->
            list.let(transactionOrderAdapter::submitList)
            transactionList = list.toMutableList()
//            Count number of transactions
            transactionHeadQuantity.text = list.size.toString()

//            Count total of transactions
            val total = list.sumOf { it.totalWithVat }
            transactionsTotal.text = total.toString()
        }

        viewModel.mutableLiveDataHasOpenedDay.observe(viewLifecycleOwner, observerHasOpenedDay)
        viewModel.mutableLiveDataInvoiceNum.observe(viewLifecycleOwner, observerInvoiceNum)
        viewModel.mutableLiveDataEntityTransactionList.observe(
            viewLifecycleOwner,
            observerEntityTransactionList
        )

        viewModel.hasOpenDay()
        viewModel.getEntityTransactions(idEntity = entity.uuid)
        viewModel.getLastInvoiceNum()
    }


    private fun addDecorLine() {
        // initialize an instance of linear layout manager
        val layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        ).apply {
            // specify the layout manager for recycler view
            recyclerViewTransDetails.layoutManager = this
        }

        // initialize an instance of divider item decoration
        DividerItemDecoration(
            requireContext(), // context
            layoutManager.orientation
        ).apply {
            // add divider item decoration to recycler view
            // this will show divider line between items
            recyclerViewTransDetails.addItemDecoration(this)
        }
    }


    private fun updateAdapterList(transactionList: List<TransactionHead>) {
        transactionList.let(transactionOrderAdapter::submitList)
        transactionOrderAdapter.notifyDataSetChanged()
    }

    private fun createSummaryInvoice(transactionList: List<TransactionHead>): TransactionHead {
        var total = 0.0
        var totalWithVat = 0.0
        var vatAmount = 0.0
        transactionList.map {
            total += it.total
            totalWithVat += it.totalWithVat
            vatAmount += it.vatAmount
        }

        return TransactionHead(
            total = total.formatTwoDecimal(),
            totalWithVat = totalWithVat.formatTwoDecimal(),
            vatAmount = vatAmount.formatTwoDecimal(),
            invoiceNo = invoiceNo,
            idEntity = entity.uuid!!,
            isSummaryInvoice = true,
            orders = transactionList
        )
    }

}