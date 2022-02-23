package com.wind.billypos.view.ui.sales

import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentSalesReportBinding
import com.wind.billypos.view.ui.sales.viewmodel.SalesReportViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SalesReportFragment: BaseFragment<FragmentSalesReportBinding, SalesReportViewModel>() {

    private val salesReportViewModel: SalesReportViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.salesReportViewModel
    override val layoutId: Int
        get() = R.layout.fragment_sales_report
    override val viewModel: SalesReportViewModel
        get() = salesReportViewModel
}