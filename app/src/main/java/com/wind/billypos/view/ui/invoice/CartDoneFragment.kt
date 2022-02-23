package com.wind.billypos.view.ui.invoice

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentCartDoneBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.invoice.viewmodel.CartDoneViewModel
import kotlinx.android.synthetic.main.fragment_cart_done.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartDoneFragment: BaseFragment<FragmentCartDoneBinding, CartDoneViewModel>() {

    private val cartDoneViewModel: CartDoneViewModel by viewModel()
    private val homeSharedViewModel: HomeSharedViewModel by viewModel()

    private val args: CartDoneFragmentArgs by navArgs()
    private var transactionHead = TransactionHead()

    override val bindingVariable: Int
        get() = BR.cartDoneViewModel
    override val layoutId: Int
        get() = R.layout.fragment_cart_done
    override val viewModel: CartDoneViewModel
        get() = cartDoneViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setTransactionHead(trnHead = args.transactionHead)


        btnRecipt.setOnClickListener {
            findNavController().navigateSafe(CartDoneFragmentDirections.actionCartDoneFragmentToPrintInvoiceBottomSheetDialogFragment(transactionHead = transactionHead))
        }

        btnNewSale.setOnClickListener {
            if(transactionHead.isSummaryInvoice || transactionHead.isOrderInvoice){
                findNavController().popBackStack(R.id.orderInvoiceFragment, false)
            }else{
                findNavController().popBackStack(R.id.invoiceFragment, false)
            }
        }

        val observerTransactionHead = Observer<TransactionHead>{
            transactionHead = it
        }

        viewModel.mutableLiveDataTransactionHead.observe(viewLifecycleOwner, observerTransactionHead)

    }

    private fun setTransactionHead(trnHead: TransactionHead){
        viewDataBinding?.transactionHead = trnHead
        viewModel.setTransactionHead(trnHead = trnHead)
    }
}