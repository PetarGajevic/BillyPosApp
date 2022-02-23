package com.wind.billypos.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.wind.billypos.utils.LocaleManager

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel>: Fragment() {

    var hasInitializedRootView = false
    var viewDataBinding: T? = null
    private var mViewModel: BaseViewModel? = null


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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let { LocaleManager.setLocale(it) }
        return performDataBinding(inflater, container)
    }

    private fun performDataBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding?.setVariable(bindingVariable, mViewModel)
        viewDataBinding?.executePendingBindings()
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        return viewDataBinding!!.root
    }

    companion object {
        const val REFRESH_DATA = "REFRESH_DATA"
        const val RESULT = "RESULT"
    }

}