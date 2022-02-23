package com.wind.billypos.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wind.billypos.R

abstract class BaseBottomSheetDialogFragment<T : ViewDataBinding, V : BaseViewModel> : BottomSheetDialogFragment() {

    var viewDataBinding: T? = null
    private var mViewModel: V? = null


    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.isHideable = false
        }

        return bottomSheetDialog
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel.mutableLiveDataErrorHandler.observe(
//            viewLifecycleOwner,
//            BaseErrorNetworkObserver(requireActivity(), viewModel)
//        )
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return performDataBinding(inflater, container)
    }

    private fun performDataBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding?.setVariable(bindingVariable, mViewModel)
        viewDataBinding?.executePendingBindings()
        viewDataBinding?.lifecycleOwner = this
        return viewDataBinding!!.root
    }

}