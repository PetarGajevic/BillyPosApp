package com.wind.billypos.view.ui.customer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentAddCustomerBinding
import com.wind.billypos.view.model.Customer
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.ui.customer.viewmodel.AddCustomerViewModel
import kotlinx.android.synthetic.main.fragment_add_customer.*
import kotlinx.android.synthetic.main.fragment_item_add.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*

class AddCustomerFragment : BaseFragment<FragmentAddCustomerBinding, AddCustomerViewModel>() {

    private val addCustomerViewModel: AddCustomerViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.addCustomerViewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_customer
    override val viewModel: AddCustomerViewModel
        get() = addCustomerViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

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
                            viewModel.insertCustomer(customer = it)
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
                        viewModel.updateCustomer(customer = customer)
                    }
                }else{
                    Toast.makeText(requireContext(), getString(R.string.customer_saved_error), Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }

        val observerUpdated = Observer<Boolean> {
            if(it){
                Toast.makeText(requireContext(), getString(R.string.customer_saved_success), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), getString(R.string.customer_saved_error), Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack()
        }

        viewModel.mutableLiveDataCustomer.observe(viewLifecycleOwner, observerCustomer)
        viewModel.mutableLiveDataSync.observe(viewLifecycleOwner, observerSync)
        viewModel.mutableLiveDataUpdated.observe(viewLifecycleOwner, observerUpdated)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_customer, menu)
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
        val customer = Customer(
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