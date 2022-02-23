package com.wind.billypos.view.ui.transaction

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentEditTransactionBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.transaction.adapter.EditItemsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.wind.billypos.view.ui.transaction.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.bottom_fragment_print_invoice.*
import timber.log.Timber

class EditTransactionFragment : BaseFragment<FragmentEditTransactionBinding, TransactionViewModel>() {

    private val transactionViewModel: TransactionViewModel by viewModel()

    private val args: EditTransactionFragmentArgs by navArgs()

    private var transactionHead = TransactionHead()
    private var items = listOf<TransactionBody>()

    override val bindingVariable: Int
        get() = BR.transactionViewModel
    override val layoutId: Int
        get() = R.layout.fragment_edit_transaction
    override val viewModel: TransactionViewModel
        get() = transactionViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val printItemsAdapter = EditItemsAdapter(editItemClick = { _, transactionBody, _ ->
          //  findNavController().navigateSafe(EditTransactionFragmentDirections.actionEditTransactionBottomSheetDialogFragmentToEditItemBottomSheetDialogFragment())
        })

        recyclerViewItemsPrint.adapter = printItemsAdapter

        val observerTransactionHead = Observer<TransactionHead> {
            transactionHead = it
            viewDataBinding?.transactionHead = transactionHead
            viewModel.getItems(trnHeadUUID = transactionHead.uuid!!)
        }

        val observerItems = Observer<List<TransactionBody>> {
            items = it
            printItemsAdapter.submitList(items)
        }


        viewModel.mutableLiveDataTransactionHead.observe(viewLifecycleOwner, observerTransactionHead)
        viewModel.mutableLiveDataItems.observe(viewLifecycleOwner, observerItems)

        viewModel.setTransactionHead(trnHead = args.transactionHead)
    }

}