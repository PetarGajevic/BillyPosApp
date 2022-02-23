package com.wind.billypos.view.ui.operators

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.R
import com.wind.billypos.BR
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentAddOperatorBinding
import com.wind.billypos.utils.enums.Permission
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.operators.viewmodel.AddOperatorViewModel
import com.wind.billypos.view.ui.operators.viewmodel.OperatorViewModel
import kotlinx.android.synthetic.main.fragment_add_operator.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddOperatorFragment : BaseFragment<FragmentAddOperatorBinding, AddOperatorViewModel>() {

    private val addOperatorViewModel: AddOperatorViewModel by viewModel()

    private var listOfPermissions = mutableListOf<String>()

    override val bindingVariable: Int
        get() = BR.addOperatorViewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_operator
    override val viewModel: AddOperatorViewModel
        get() = addOperatorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val observerOperator = Observer<Operator> {
            it?.let {
                if (it.hasChanges()) {
                    when {
                        it.isSuccess() -> {
                            Timber.e("Spremno za slanje na server %s", it)
                            viewModel.sendOperator(operator = it)
                        }
                        it.hasError() -> {
                            Timber.e("Network error")
                        }
                        it.isFirstNameIncorrect() -> {
                            Toast.makeText(
                                context,
                                getString(R.string.operator_first_name_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        it.isLastNameIncorrect() -> {
                            Toast.makeText(
                                context,
                                getString(R.string.operator_last_name_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        it.isOperatorCodeIncorrect() -> {
                            Toast.makeText(
                                context,
                                getString(R.string.operator_code_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        it.isPinIncorrect() -> {
                            Toast.makeText(
                                context,
                                getString(R.string.pin_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        it.isPinLengthIncorrect() -> {
                            Toast.makeText(
                                context,
                                getString(R.string.pin_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        it.isNationalIdNoIncorrect() -> {
                            Toast.makeText(
                                context,
                                getString(R.string.national_id_no_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        it.areDataCorrect() -> {
                            viewModel.insertOperator(operator = it)
                        }
                    }
                }
            }
        }

        val observerCheckPin = Observer<Int> {
            if (it == 1) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.pin_already_exists),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.checkOperatorCode(operatorCode = operatorCode.text.toString())
            }
        }

        val observerCheckOperatorCode = Observer<Int> {
            if (it == 1) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.operator_code_already_exists),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val operator = Operator(
                    businUnit = 405,
                    cashRegister = 225,
                    firstName = operatorFirstName.text.toString(),
                    lastName = operatorLastName.text.toString(),
                    operatorCode = operatorCode.text.toString(),
                    pin = pin.text.toString(),
                    nationalIdNo = national_id_no.text.toString(),
                    permissions = listOfPermissions
                )
                if (pin.text.toString() == confirm_pin.text.toString()) {
                    viewModel.setOperator(operator = operator)
                } else {
                    Toast.makeText(context, getString(R.string.same_pin_error), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        val observerSyncOperator = Observer<String> {
            if(it == "OK"){
                Toast.makeText(requireContext(), getString(R.string.operator_saved_success), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(), getString(R.string.operator_saved_error), Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }



        issueCashInvoiceSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.ISSUE_CASH_INVOICE)
            else
                listOfPermissions.remove(Permission.ISSUE_CASH_INVOICE)
        }

        purchaseEinvoiceSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.PURCHASE_EINVOICE)
            else
                listOfPermissions.remove(Permission.PURCHASE_EINVOICE)
        }

        approveInvoiceSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.APPROVE_INVOICE)
            else
                listOfPermissions.remove(Permission.APPROVE_INVOICE)

        }

        issueNonCashEinvoiceSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.ISSUE_NONCASH_EINVOICE)
            else
                listOfPermissions.remove(Permission.ISSUE_NONCASH_EINVOICE)
        }

        issueWtnSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.ISSUE_WTN)
            else
                listOfPermissions.remove(Permission.ISSUE_WTN)
        }

        createItemSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.CREATE_ITEM)
            else
                listOfPermissions.remove(Permission.CREATE_ITEM)
        }

        editExchangeRateSwitch.setOnCheckedChangeListener { _, b ->
            if (b)
                listOfPermissions.add(Permission.EDIT_EXCHANGE_RATE)
            else
                listOfPermissions.remove(Permission.EDIT_EXCHANGE_RATE)
        }

        viewModel.mutableLiveDataOperator.observe(viewLifecycleOwner, observerOperator)
        viewModel.mutableLiveDataCheckPin.observe(viewLifecycleOwner, observerCheckPin)
        viewModel.mutableLiveDataSyncOperator.observe(viewLifecycleOwner, observerSyncOperator)
        viewModel.mutableLiveDataCheckOperatorCode.observe(
            viewLifecycleOwner,
            observerCheckOperatorCode
        )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_operator -> {
                saveOperator()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_operator, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun saveOperator() {
        viewModel.checkPin(pin = pin.text.toString())
    }

}