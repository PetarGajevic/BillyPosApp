package com.wind.billypos.view.ui.order

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentOrderInvoiceBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.EntityPoint
import com.wind.billypos.view.ui.order.adapter.OrderInvoiceAdapter
import com.wind.billypos.view.ui.order.viewmodel.OrderInvoiceViewModel
import kotlinx.android.synthetic.main.fragment_order_invoice.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class OrderInvoiceFragment: BaseFragment<FragmentOrderInvoiceBinding, OrderInvoiceViewModel>() {

    private val orderInvoiceViewModel: OrderInvoiceViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.orderInvoiceViewModel
    override val layoutId: Int
        get() = R.layout.fragment_order_invoice
    override val viewModel: OrderInvoiceViewModel
        get() = orderInvoiceViewModel

    private var entityList = listOf<EntityPoint>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderInvoiceAdapter = OrderInvoiceAdapter(
            orderInvoiceClick = { _, entity, _ ->
                findNavController().navigateSafe(OrderInvoiceFragmentDirections.actionOrderInvoiceFragmentToEntityPointFragment(entityPoint = entity))
            }
        )

        recyclerview_entityPoint.adapter = orderInvoiceAdapter

        val observerEntityPoints = Observer<List<EntityPoint>> { entityPoints ->
            entityList = entityPoints

            entityPoints.forEach {
                viewModel.checkFreeEntityPoints(entityPointId = it.uuid)
            }
        }


        val observerEntityPointIsOpen = Observer<String> { uuid ->
             entityList.find { entityPoint ->
                entityPoint.uuid == uuid
            }?.isOpen = false

            entityList.let(orderInvoiceAdapter::submitList)
            orderInvoiceAdapter.notifyDataSetChanged()
        }

        viewModel.mutableLiveDataEntityPointList.observe(viewLifecycleOwner, observerEntityPoints)
        viewModel.mutableLiveDataEntityPointIsOpen.observe(viewLifecycleOwner, observerEntityPointIsOpen)

        viewModel.getEntityPoints()
    }
}