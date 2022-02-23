package com.wind.billypos.view.ui.operators

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.interfaces.YesNoDialogInterface
import com.wind.billypos.databinding.FragmentOperatorsBinding
import com.wind.billypos.utils.ConfirmAlert
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.view.model.Operator
import com.wind.billypos.view.ui.item.ItemFragmentDirections
import com.wind.billypos.view.ui.item.adapter.ItemAdapter
import com.wind.billypos.view.ui.operators.adapter.OperatorAdapter
import com.wind.billypos.view.ui.operators.viewmodel.OperatorViewModel
import kotlinx.android.synthetic.main.fragment_edit_operator.*
import kotlinx.android.synthetic.main.fragment_items.*
import kotlinx.android.synthetic.main.fragment_operators.*
import kotlinx.android.synthetic.main.fragment_operators.searchView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class OperatorFragment : BaseFragment<FragmentOperatorsBinding, OperatorViewModel>() {

    private val operatorViewModel: OperatorViewModel by viewModel()

    override val bindingVariable: Int
        get() = BR.operatorViewModel
    override val layoutId: Int
        get() = R.layout.fragment_operators
    override val viewModel: OperatorViewModel
        get() = operatorViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val operatorAdapter =
            OperatorAdapter(
                showOperatorClick = { _, operator, _ ->
                    if(operator.isActive == true){
                        findNavController().navigateSafe(
                            OperatorFragmentDirections.actionOperatorsFragmentToOperatorDetailsFragment(
                                operator = operator
                            )
                        )
                    }else{
                        Toast.makeText(requireContext(), getString(R.string.not_editable_operator), Toast.LENGTH_LONG).show()
                    }
                },
                enableOperatorClick = { _, operator, _ ->
                    enableDisableOperatorDialog(
                        operator = operator.copy(isActive = true),
                        message = getString(R.string.activate_operator)
                    )
                },
                disableOperatorClick = { _, operator, _ ->
                    enableDisableOperatorDialog(
                        operator = operator.copy(isActive = false),
                        message = getString(R.string.deactivate_operator)
                    )
                }
            )

        rvOperators.adapter = operatorAdapter

//        Search Operators
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchOperators(input = newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchOperators(input = query)
                return false
            }
        })

        val observerOperators = Observer<List<Operator>> {
            it.let(operatorAdapter::submitList)
        }

        val observerUpdateOperator = Observer<Boolean> {
            viewModel.searchOperators(input = searchView.query.toString())
        }

        viewModel.mutableLiveDataOperators.observe(viewLifecycleOwner, observerOperators)
        viewModel.mutableLiveDataUpdateOperator.observe(viewLifecycleOwner, observerUpdateOperator)

        viewModel.getOperators()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_operator -> {
                findNavController().navigateSafe(OperatorFragmentDirections.actionOperatorsFragmentToAddOperatorFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_operator, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchOperators(input: String?) {
        viewModel.searchOperators(input = input)
    }

    private fun enableDisableOperatorDialog(operator: Operator, message: String) {
        ConfirmAlert(requireContext(), object : YesNoDialogInterface {
            override fun onConfirm(dialog: AlertDialog) {
                viewModel.updateOperator(operator = operator)
            }

            override fun onCancel(dialog: AlertDialog) {}
        }).showDialog(
            message,
            resources.getString(R.string.attention)
        )
    }

}