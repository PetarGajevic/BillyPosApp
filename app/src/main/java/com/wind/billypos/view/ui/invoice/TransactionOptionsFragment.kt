package com.wind.billypos.view.ui.invoice

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseBottomSheetDialogFragment
import com.wind.billypos.databinding.BottomFragmentTransactionOptionsBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.invoice.viewmodel.TransactionOptionsViewModel
import kotlinx.android.synthetic.main.bottom_fragment_transaction_options.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TransactionOptionsFragment :
    BaseBottomSheetDialogFragment<BottomFragmentTransactionOptionsBinding, TransactionOptionsViewModel>() {

    private val transactionOptionsViewModel: TransactionOptionsViewModel by viewModel()
    private val homeSharedViewModel: HomeSharedViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.transactionOptionsViewModel
    override val layoutId: Int
        get() = R.layout.bottom_fragment_transaction_options
    override val viewModel: TransactionOptionsViewModel
        get() = transactionOptionsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnChooseCustomer.setOnClickListener {
            findNavController().navigateSafe(
                TransactionOptionsFragmentDirections.actionTransactionOptionsFragmentToCustomerFragment(
                    chooseCustomer = true,
                    trnHead = homeSharedViewModel.getTransactionHead()
                )
            )
        }

        imgCloseFragment.setOnClickListener {
            dismiss()
        }

    }

}