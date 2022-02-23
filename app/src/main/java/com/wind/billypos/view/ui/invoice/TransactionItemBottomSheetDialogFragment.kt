package com.wind.billypos.view.ui.invoice

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseBottomSheetDialogFragment
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.BottomFragmentTransactionItemBinding
import com.wind.billypos.utils.setNavigationResult
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.invoice.viewmodel.TransactionItemViewModel
import kotlinx.android.synthetic.main.bottom_fragment_transaction_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TransactionItemBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<BottomFragmentTransactionItemBinding, TransactionItemViewModel>() {

    private val transactionItemViewModel: TransactionItemViewModel by viewModel()
    private val homeSharedViewModel: HomeSharedViewModel by viewModel()
    private val args: TransactionItemBottomSheetDialogFragmentArgs by navArgs()
    private lateinit var keyboardButtons: java.util.ArrayList<View>
    private var isFirstTime: Boolean = true
    private var transactionItem: TransactionBody? = null

    override val bindingVariable: Int
        get() = BR.transactionItemViewModel
    override val layoutId: Int
        get() = R.layout.bottom_fragment_transaction_item
    override val viewModel: TransactionItemViewModel
        get() = transactionItemViewModel

    private lateinit var observerDismiss: Observer<Boolean>
    private lateinit var observerUpdate: Observer<Boolean>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTransactionItem(trnBody = args.transactionItem)

        keyboardButtons = getViewsByTag(view as ViewGroup, "tag_keyboard")

        for (keyboard in keyboardButtons) {
            keyboard.setOnClickListener {
                if (isFirstTime) {
                    edtBottomItemQuantity.setText("0", TextView.BufferType.EDITABLE)
                    isFirstTime = !isFirstTime
                }
                if (it is MaterialButton) {
                    val nextNumber = it.text.toString()
                    val currentNumber = edtBottomItemQuantity.text.toString()
                    var number = ""

                    if (currentNumber.indexOf(".") != -1 && nextNumber == ".") {
                        return@setOnClickListener
                    }

                    number = if (currentNumber != "0") {
                        currentNumber.plus(nextNumber)
                    } else {
                        nextNumber
                    }
                    edtBottomItemQuantity.setText(number, TextView.BufferType.EDITABLE)
                }
            }
        }

        keyboardBackspace.setOnClickListener {
            if (edtBottomItemQuantity == null) {
                return@setOnClickListener
            }
            var text = "0"
            if (edtBottomItemQuantity.text?.length!! > 0) {
                text = edtBottomItemQuantity.text?.substring(
                    0,
                    edtBottomItemQuantity.text?.length?.minus(1)!!
                ).toString()
            }
            if (text.isEmpty()) {
                text = "0"
            }

            edtBottomItemQuantity.setText(text, TextView.BufferType.EDITABLE)
        }

        keyboardApply.setOnClickListener {
            val quantity = edtBottomItemQuantity.text?.toString()?.toDouble()
            if (quantity == null || quantity <= 0.0) {
                return@setOnClickListener
            }
            transactionItem?.quantity = quantity

            homeSharedViewModel.updateTransactionItem(trnBody = transactionItem)
            //  viewModel.updateTransactionItem(trnBody = transactionItem)
        }

        btnDeleteItem.setOnClickListener {
            // viewModel.deleteTransactionItem(trnBody = transactionItem)
            Timber.e("Transaction list before %s", homeSharedViewModel.getTransactions())
            homeSharedViewModel.deleteItem(trnBody = transactionItem)
        }

        val observerTransactionItem = Observer<TransactionBody> {
            transactionItem = it
            edtBottomItemQuantity.setText(it.quantity.toString())
        }

        observerUpdate = Observer<Boolean> {
            Timber.e("Update")
            it?.let {
                if (it) {
                    setNavigationResult(BaseFragment.RESULT, BaseFragment.REFRESH_DATA)
                    dismiss()
                }
                homeSharedViewModel.mutableLiveDataUpdateItem.value = null
            }
        }


        observerDismiss = Observer<Boolean> {
            Timber.e("Delete")
            it?.let {
                if (it) {
                    setNavigationResult(BaseFragment.RESULT, BaseFragment.REFRESH_DATA)
                    dismiss()
                }
                homeSharedViewModel.mutableLiveDataDeleteItem.value = null
            }
        }

        viewModel.mutableLiveDataTransactionBody.observe(
            viewLifecycleOwner,
            observerTransactionItem
        )
        homeSharedViewModel.mutableLiveDataDeleteItem.observe(viewLifecycleOwner, observerDismiss)
        homeSharedViewModel.mutableLiveDataUpdateItem.observe(viewLifecycleOwner, observerUpdate)

    }

    private fun getViewsByTag(root: ViewGroup?, tag: String?): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root?.childCount
        for (i in 0 until childCount!!) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag))
            }

            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                views.add(child)
            }

        }
        return views
    }

    private fun setTransactionItem(trnBody: TransactionBody) =
        viewModel.setTransactionItem(trnBody = trnBody)


}