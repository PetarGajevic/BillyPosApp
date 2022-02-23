package com.wind.billypos.view.ui.customer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentCustomerDetailsBinding
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.ui.customer.viewmodel.CustomerDetailsViewModel
import kotlinx.android.synthetic.main.fragment_add_customer.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class CustomerDetailsFragment : BaseFragment<FragmentCustomerDetailsBinding, CustomerDetailsViewModel>() {

    private val customerDetailsViewModel: CustomerDetailsViewModel by viewModel()

    private val args: CustomerDetailsFragmentArgs by navArgs()

    private lateinit var customer: Customer

    override val bindingVariable: Int
        get() = BR.customerDetailsViewModel
    override val layoutId: Int
        get() = R.layout.fragment_customer_details
    override val viewModel: CustomerDetailsViewModel
        get() = customerDetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


        viewDataBinding?.customer = args.customer
        customer = args.customer

        val observerCustomer = Observer<Customer> {
            it?.let {
                if(it.hasChanges()){
                    when {
                        it.isSuccess() -> {
                            viewModel.sendCustomer(customer = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isCustomerNameIncorrect() -> {
                            customerName.error = getString(R.string.field_is_required)
                        }
                        it.isCustomerNUISIncorrect() -> {
                            customerNUIS.error = getString(R.string.field_is_required)
                        }
                        it.isCustomerEmailIncorrect() -> {
                            customerEmail.error = getString(R.string.field_is_required)
                        }
                        it.areDataCorrect() -> {
                            Timber.e("data correct")
                            viewModel.updateCustomer(customer = it)
                        }
                    }
                }
            }
        }

        val observerSync = Observer<Boolean> {
            it?.let{
                if(it){
                    val customer = viewModel.getCustomer()?.copy(
                        isSync = true,
                        lastServerSync = LocalDateTime.now()
                    )
                    if (customer != null) {
                        viewModel.insertUpdatedCustomer(customer = customer)
                    }
                }else{
                    val customer = viewModel.getCustomer()?.copy(
                        isSync = false
                    )
                    if (customer != null) {
                        viewModel.insertUpdatedCustomer(customer = customer)
                    }
                }
            }
        }

        val observerUpdated = Observer<Boolean> {
            if(it){
                Toast.makeText(requireContext(), getString(R.string.customer_saved_success), Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(), getString(R.string.customer_saved_error), Toast.LENGTH_LONG).show()
            }
            findNavController().popBackStack()
        }

        viewModel.mutableLiveDataCustomer.observe(viewLifecycleOwner, observerCustomer)
        viewModel.mutableLiveDataSync.observe(viewLifecycleOwner, observerSync)
        viewModel.mutableLiveDataUpdated.observe(viewLifecycleOwner, observerUpdated)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_customer_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveCustomer()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveCustomer() {
        val customer = customer.copy(
            name = customerName.text.toString(),
            email = customerEmail.text.toString(),
            phone = customerPhone.text.toString(),
            address = customerAddress.text.toString(),
            city = customerCity.text.toString() ,
            country = customerCountry.text.toString(),
            idType = "ID",
            idNum = customerNUIS.text.toString(),
            idCompany = 0,
            deviceId = 12345678910,
            userID = 1
        )


//        val customer = Customer(
//            name = "Test",
//            email = "Test@gmail.com",
//            address = "Test Adres",
//            city = "Tirane",
//            country = "Albania",
//            idType = "Test id type",
//            idNum = "Test id num",
//            idCompany = 0,
//            deviceId = 12345678910,
//            userID = 1
//        )

        viewModel.setCustomer(customer = customer)
    }


}