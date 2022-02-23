package com.wind.billypos.view.ui.transaction

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseBottomSheetDialogFragment
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.BottomFragmentEditItemBinding
import com.wind.billypos.utils.setNavigationResult
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.ui.invoice.viewmodel.TransactionItemViewModel
import com.wind.billypos.view.ui.transaction.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.bottom_fragment_transaction_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditItemBottomSheetDialogFragment : BaseBottomSheetDialogFragment<BottomFragmentEditItemBinding, TransactionViewModel>() {

    private val transactionViewModel: TransactionViewModel by viewModel()

    private val args: EditItemBottomSheetDialogFragmentArgs by navArgs()

    override val bindingVariable: Int
        get() = BR.transactionItemViewModel
    override val layoutId: Int
        get() = R.layout.bottom_fragment_edit_item
    override val viewModel: TransactionViewModel
        get() = transactionViewModel

    private lateinit var keyboardButtons: java.util.ArrayList<View>
    private var isFirstTime: Boolean = true
    private var transactionItem: TransactionBody? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            // setTransactionItem(trnBody = args.transactionBody)


        keyboardButtons = getViewsByTag(view as ViewGroup, "tag_keyboard")

        for(keyboard in keyboardButtons){
            keyboard.setOnClickListener {
                if(isFirstTime){
                    edtBottomItemQuantity.setText("0", TextView.BufferType.EDITABLE)
                    isFirstTime = !isFirstTime
                }
                if(it is MaterialButton) {
                    val nextNumber = it.text.toString()
                    val currentNumber = edtBottomItemQuantity.text.toString()
                    var number = ""

                    if(currentNumber.indexOf(".") != -1 && nextNumber == "."){
                        return@setOnClickListener
                    }

                    if(currentNumber != "0") {
                        number = currentNumber.plus(nextNumber)
                    }else{
                        number = nextNumber
                    }
                    edtBottomItemQuantity.setText(number, TextView.BufferType.EDITABLE)
                }
            }
        }

        keyboardBackspace.setOnClickListener {
            if(edtBottomItemQuantity == null){
                return@setOnClickListener
            }
            var text = "0"
            if(edtBottomItemQuantity.text?.length!! > 0){
                text = edtBottomItemQuantity.text?.substring(0, edtBottomItemQuantity.text?.length?.minus(1)!!).toString()
            }
            if(text.isEmpty()){
                text = "0"
            }

            edtBottomItemQuantity.setText(text, TextView.BufferType.EDITABLE)
        }

        keyboardApply.setOnClickListener {
            val quantity = edtBottomItemQuantity.text?.toString()?.toDouble()
            if(quantity == null || quantity <= 0.0){
                return@setOnClickListener
            }
            transactionItem?.quantity = quantity
         //   viewModel.updateTransactionItem(trnBody = transactionItem)
        }

        btnDeleteItem.setOnClickListener {
         //   viewModel.deleteTransactionItem(trnBody = transactionItem)
        }

        val observerTransactionItem = Observer<TransactionBody>{
            transactionItem = it
            edtBottomItemQuantity.setText(it.quantity.toString())
        }

        val observerUpdate = Observer<Boolean>{
            if(it){
                setNavigationResult(BaseFragment.RESULT, BaseFragment.REFRESH_DATA)
                dismiss()
            } else{
                Timber.e("error")
            }
        }

        val observerDelete = Observer<Int> {
            if(it==1){
                setNavigationResult(BaseFragment.RESULT, BaseFragment.REFRESH_DATA)
                dismiss()
            }
        }

        viewModel.mutableLiveDataTransactionBody.observe(viewLifecycleOwner, observerTransactionItem)
        viewModel.mutableLiveDataUpdate.observe(viewLifecycleOwner, observerUpdate)
        viewModel.mutableLiveDataDelete.observe(viewLifecycleOwner, observerDelete)

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

  //  private fun setTransactionItem(trnBody: TransactionBody) = viewModel.setTransactionItem(trnBody = trnBody)

}