package com.wind.billypos.view.ui.operators

import android.app.AlertDialog
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
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.databinding.FragmentEditOperatorBinding
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.enums.Permission
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.operators.viewmodel.EditOperatorViewModel
import kotlinx.android.synthetic.main.fragment_edit_operator.*
import kotlinx.android.synthetic.main.fragment_edit_operator.approveInvoiceSwitch
import kotlinx.android.synthetic.main.fragment_edit_operator.createItemSwitch
import kotlinx.android.synthetic.main.fragment_edit_operator.editExchangeRateSwitch
import kotlinx.android.synthetic.main.fragment_edit_operator.issueCashInvoiceSwitch
import kotlinx.android.synthetic.main.fragment_edit_operator.issueNonCashEinvoiceSwitch
import kotlinx.android.synthetic.main.fragment_edit_operator.issueWtnSwitch
import kotlinx.android.synthetic.main.fragment_edit_operator.operatorCode
import kotlinx.android.synthetic.main.fragment_edit_operator.operatorFirstName
import kotlinx.android.synthetic.main.fragment_edit_operator.operatorLastName
import kotlinx.android.synthetic.main.fragment_edit_operator.purchaseEinvoiceSwitch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditOperatorFragment : BaseFragment<FragmentEditOperatorBinding, EditOperatorViewModel>() {

    private val editOperatorViewModel: EditOperatorViewModel by viewModel()

    private val args: EditOperatorFragmentArgs by navArgs()

    var editPin: Boolean = false
    var editOperatorCode: Boolean = false
    private var operator = Operator()
    private var listOfPermissions = mutableListOf<String>()

    override val bindingVariable: Int
        get() = BR.editOperatorViewModel
    override val layoutId: Int
        get() = R.layout.fragment_edit_operator
    override val viewModel: EditOperatorViewModel
        get() = editOperatorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewDataBinding?.operator = args.operator
        operator = args.operator
        checkPermissions(operator.permissions)
        listOfPermissions = operator.permissions?.toMutableList()!!


        pinSwitch.setOnCheckedChangeListener { _, check ->
            if (check) {
                ConfirmAlert(requireContext(), object : YesNoDialogInterface {
                    override fun onConfirm(dialog: AlertDialog) {
                        showTextFields(visibility = true)
                    }

                    override fun onCancel(dialog: AlertDialog) {
                        pinSwitch.isChecked = false
                    }
                }).showDialog(
                    resources.getString(R.string.edit_pin_attention),
                    resources.getString(R.string.attention)
                )
            } else {
                showTextFields(visibility = false)
            }
        }


        operatorCode.setOnClickListener {
            if (!operatorCode.isFocusableInTouchMode) {
                ConfirmAlert(requireContext(), object : YesNoDialogInterface {
                    override fun onConfirm(dialog: AlertDialog) {
                        operatorCode.isFocusableInTouchMode = true
                    }

                    override fun onCancel(dialog: AlertDialog) {
                    }
                }).showDialog(
                    resources.getString(R.string.edit_operator_code_attention),
                    resources.getString(R.string.attention)
                )
            }
        }

        val observerOperator = Observer<Operator> {
            it?.let {
                if (it.hasChanges()) {
                    when {
                        it.isSuccess() -> {
                            Timber.e("Spremno za slanje na server %s", it)
                            viewModel.sendUpdateOperator(operator = it)
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
                            viewModel.updateOperator(operator = it)
                        }
                    }
                }
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
                if (editPin) {
                    viewModel.checkPin(pin = newPin.text.toString())
                } else {
                    Timber.e("listOfPermissions %s", listOfPermissions)
                    viewModel.setOperator(
                        args.operator.copy(
                            businUnit = 405,
                            cashRegister = 225,
                            firstName = operatorFirstName.text.toString(),
                            lastName = operatorLastName.text.toString(),
                            operatorCode = operatorCode.text.toString(),
                            nationalIdNo = national_id_no.text.toString(),
                            permissions = listOfPermissions
                        )
                    )
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
                viewModel.setOperator(
                    args.operator.copy(
                        businUnit = 405,
                        cashRegister = 225,
                        firstName = operatorFirstName.text.toString(),
                        lastName = operatorLastName.text.toString(),
                        operatorCode = operatorCode.text.toString(),
                        pin = newPin.text.toString(),
                        nationalIdNo = national_id_no.text.toString(),
                        permissions = listOfPermissions
                    )
                )
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
            Timber.e("approveInvoiceSwitch %s", listOfPermissions)
            if (b)
                listOfPermissions.add(Permission.APPROVE_INVOICE)
            else
                listOfPermissions.remove(Permission.APPROVE_INVOICE)

        }

        issueNonCashEinvoiceSwitch.setOnCheckedChangeListener { _, b ->
            Timber.e("issueNonCashEinvoiceSwitch %s", listOfPermissions)
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

        val observerSyncOperator = Observer<String> {
            if (it == "OK") {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.operator_saved_success),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.operator_saved_error),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            }
        }

        viewModel.mutableLiveDataOperator.observe(viewLifecycleOwner, observerOperator)
        viewModel.mutableLiveDataSyncOperator.observe(viewLifecycleOwner, observerSyncOperator)
        viewModel.mutableLiveDataCheckOperatorCode.observe(
            viewLifecycleOwner,
            observerCheckOperatorCode
        )
        viewModel.mutableLiveDataCheckPin.observe(viewLifecycleOwner, observerCheckPin)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_operator, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                saveOperator(operator = args.operator)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTextFields(visibility: Boolean) {
        oldPin.isFocusableInTouchMode = visibility
        newPin.isFocusableInTouchMode = visibility
        newConfirmPin.isFocusableInTouchMode = visibility
        oldPin.setText("")
        newPin.setText("")
        newConfirmPin.setText("")
        newConfirmPin.clearFocus()
        newPin.clearFocus()
        oldPin.clearFocus()
        editPin = visibility
    }


    private fun saveOperator(operator: Operator) {
        if (editPin) {
            if (operator.pin == oldPin.text.toString()) {
                if (newPin.text.toString() == newConfirmPin.text.toString()) {
                    viewModel.checkOperatorCode(
                        operatorCode = operatorCode.text.toString(),
                        id = operator.id
                    )
                } else {
                    Toast.makeText(context, getString(R.string.same_pin_error), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(context, getString(R.string.old_pin_error), Toast.LENGTH_LONG).show()
            }
        } else {
            viewModel.checkOperatorCode(
                operatorCode = operatorCode.text.toString(),
                id = operator.id
            )
        }
    }

    private fun checkPermissions(listOfPermissions: List<String>?) {
        if (listOfPermissions?.contains(Permission.ISSUE_CASH_INVOICE) == true) {
            issueCashInvoiceSwitch.isChecked = true
        }
        if (listOfPermissions?.contains(Permission.PURCHASE_EINVOICE) == true) {
            purchaseEinvoiceSwitch.isChecked = true
        }
        if (listOfPermissions?.contains(Permission.APPROVE_INVOICE) == true) {
            approveInvoiceSwitch.isChecked = true
        }
        if (listOfPermissions?.contains(Permission.ISSUE_NONCASH_EINVOICE) == true) {
            issueNonCashEinvoiceSwitch.isChecked = true
        }
        if (listOfPermissions?.contains(Permission.ISSUE_WTN) == true) {
            issueWtnSwitch.isChecked = true
        }
        if (listOfPermissions?.contains(Permission.CREATE_ITEM) == true) {
            createItemSwitch.isChecked = true
        }
        if (listOfPermissions?.contains(Permission.EDIT_EXCHANGE_RATE) == true) {
            editExchangeRateSwitch.isChecked = true
        }
    }


}