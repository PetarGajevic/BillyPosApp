package com.wind.billypos.view.ui.customer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentCustomerBinding
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.customer.adapter.CustomerAdapter
import com.wind.billypos.view.ui.customer.viewmodel.CustomerViewModel
import kotlinx.android.synthetic.main.fragment_customer.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CustomerFragment: BaseFragment<FragmentCustomerBinding, CustomerViewModel>() {

    private val customerViewModel: CustomerViewModel by viewModel()

    private val args : CustomerFragmentArgs by navArgs()

    override val bindingVariable: Int
        get() = BR.customerViewModel
    override val layoutId: Int
        get() = R.layout.fragment_customer
    override val viewModel: CustomerViewModel
        get() = customerViewModel

    private var isChooseCustomer = false
    private var trnHead = TransactionHead()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        isChooseCustomer = args.chooseCustomer

        if(args.trnHead != null){
            trnHead = args.trnHead!!
        }


        val customerAdapter =
            CustomerAdapter(
                showCustomerClick = { _, customer, _ ->
                    if(isChooseCustomer){
                        findNavController().navigateSafe(CustomerFragmentDirections.actionCustomerFragmentToCartFragment(customer = customer, transactionHead = trnHead))

                    }else{
                        findNavController().navigateSafe(CustomerFragmentDirections.actionCustomerFragmentToCustomerDetailsFragment(customer))
                    }
                }
            )

        rvCustomers.adapter = customerAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                searchCustomer(input = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchCustomer(input = query)
                return false
            }
        })

        val observerCustomers = Observer<List<Customer>> {
            it.let(customerAdapter::submitList)
        }

        viewModel.mutableLiveDataCustomers.observe(viewLifecycleOwner, observerCustomers)

        viewModel.getCustomers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_customers, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_create -> {
                findNavController().navigateSafe(CustomerFragmentDirections.actionCustomerFragmentToAddCustomerFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchCustomer(input: String?){
        viewModel.searchCustomers(input = input)
    }

}