package com.wind.billypos.view.ui.cashbalance

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseBottomSheetDialogFragment
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceResponse
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositRequest
import com.wind.billypos.databinding.BottomFragmentCashBalanceBinding
import com.wind.billypos.utils.DeviceId
import com.wind.billypos.utils.PreferenceHelper.configurations
import com.wind.billypos.utils.PreferenceHelper.customPreference
import com.wind.billypos.utils.enums.CashBalanceType
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.formatReceipt
import com.wind.billypos.utils.setNavigationResult
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.Configuration
import com.wind.billypos.view.ui.cashbalance.viewmodel.AddCashBalanceViewModel
import kotlinx.android.synthetic.main.bottom_fragment_cash_balance.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class CashBalanceBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<BottomFragmentCashBalanceBinding, AddCashBalanceViewModel>() {

    private val addCashBalanceViewModel: AddCashBalanceViewModel by viewModel()

    private lateinit var keyboardButtons: java.util.ArrayList<View>

    private var cashBalanceType: String = CashBalanceType.BALANCE
    private lateinit var configuration: Configuration
    private lateinit var hasOpenedDay: List<CashBalance>

    var totalAmount = 0.00
    private var transactionsAmount = 0.0
    private var isFiscalized = false
    private var isSync = false
    private var hasLicense = false

    override val bindingVariable: Int
        get() = BR.addCashBalanceViewModel
    override val layoutId: Int
        get() = R.layout.bottom_fragment_cash_balance
    override val viewModel: AddCashBalanceViewModel
        get() = addCashBalanceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration = customPreference((activity as AppCompatActivity)).configurations

        keyboardButtons = getViewsByTag(view as ViewGroup, "tag_keyboard")

        imgCashBalanceClose.setOnClickListener { dismiss() }


        val observerCashBalance = Observer<CashBalance> {
            it?.let {
                if (it.hasChanges()) {
                    when {
                        it.isSuccess() -> {
                            // viewModel.sendCashBalance(cashBalance = it)
                            setProgressBar(true)
                            viewModel.signDocument(
                                RemoteCashDepositRequest(
                                    body = RemoteCashDepositRequest.SoapBodyCashDeposit(
                                        registerCashDepositRequest = RemoteCashDepositRequest.RegisterCashDepositRequest(
                                            header = RemoteCashDepositRequest.RegisterCashDepositRequest.HeaderCashDeposit(
                                                UUID = it.uuid
                                            ),
                                            cashDeposit = RemoteCashDepositRequest.RegisterCashDepositRequest.CashDeposit(
                                                cashAmt = abs(it.cashAmount!!).formatReceipt(),
                                                issuerNUIS = "L82210031Q",
                                                operation = it.operation,
                                                TCRCode = "wx026fv564"
                                            )
                                        )
                                    )
                                ), RemoteCashDepositRequest.ELEMENT_TO_SIGN
                            )
                        }
                        it.hasError() -> {
                            setProgressBar(false)
                            //   dismiss()
                        }
                        it.areDataCorrect() -> {
                            viewModel.insertCashBalance(cashBalance = it)
                        }
                    }
                }
            }
        }

        val observerSignedDocument = Observer<String> {
            it?.let {
                viewModel.fiscalCashDeposit(cashDeposit = it)
            }
        }

        val observerRemoteCashBalance = Observer<RemoteCashBalanceResponse?> {
            isSync = it != null
            Timber.e("Fiskalizovan i sihronizovan %s %s", isFiscalized, isSync)
            val fiscalizationState =  if(isFiscalized){
                FiscalisationState.FISCALIZED
            }else{
                FiscalisationState.NOT_FISCALIZED
            }

            val cashDeposit = viewModel.getCashBalance()?.copy(
                cashBalanceState = fiscalizationState,
                isFiscalised = isFiscalized,
                isSync = isSync
            )
            if (cashDeposit != null) {
                viewModel.updateCashBalance(cashBalance = cashDeposit)
            }
            setProgressBar(false)
            setNavigationResult(BaseFragment.RESULT, BaseFragment.REFRESH_DATA)
            dismiss()
        }

        val observerOpenedDay = Observer<Boolean> { hasOpenDay ->
            cashBalanceType = if (hasOpenDay)
                CashBalanceType.IN
            else
                CashBalanceType.BALANCE
        }

        val observerError = Observer<Throwable> {
            Timber.e(it)
//            setNavigationResult(BaseFragment.RESULT, BaseFragment.REFRESH_DATA)
//            dismiss()
        }

        val observerCashBalanceFCDC = Observer<String> {
            val cashBalance = viewModel.getCashBalance()?.copy(
                fcdc = it
            )
            isFiscalized = it.isNotEmpty()
            viewModel.sendCashBalance(cashBalance = cashBalance)
        }


        // Numbers click event
        for (keyboard in keyboardButtons) {
            keyboard.setOnClickListener {
                if (it is MaterialButton) {
                    var number = (edtBottomItemQuantity.text.toString().toLong() * 1).toString()
                        .plus(it.text.toString().toLong()).toLong()
                    if (cashBalanceType == CashBalanceType.OUT) {
                        number = abs(number) * (-1)
                    }
                    edtBottomItemQuantity.setText(number.toString(), TextView.BufferType.EDITABLE)
                }
            }
        }

        // Backspace click event
        keyboardBackspace.setOnClickListener {
            if (edtBottomItemQuantity == null) {
                return@setOnClickListener
            }
            var text = "0"
            if (edtBottomItemQuantity.text?.length!! > 0) {
                text = edtBottomItemQuantity.text?.substring(
                    0,
                    edtBottomItemQuantity.text?.length?.minus(1)!!
                ).toString()
            }
            if (text.isEmpty()) {
                text = "0"
            }

            try {
                val textInt = text.toLong() * 1L
                edtBottomItemQuantity.setText(textInt.toString(), TextView.BufferType.EDITABLE)
            } catch (ex: NumberFormatException) {
                edtBottomItemQuantity.setText("0", TextView.BufferType.EDITABLE)
            } catch (ex: Exception) {
                edtBottomItemQuantity.setText("0", TextView.BufferType.EDITABLE)
            }
        }

        // Apply click event
        keyboardApply.setOnClickListener {
            if(!hasLicense){
                Toast.makeText(requireContext(), getString(R.string.certificate_setup), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            txtCashBalanceError.text = ""
            edtBottomItemQuantity.text?.toString()?.toDouble() ?: return@setOnClickListener

//            val amount = abs(edtBottomItemQuantity.text.toString().toDouble())
            val amount = edtBottomItemQuantity.text.toString().toDouble()

//            TODO("CASH OUT NE SMIJE BITI VECI OD UKUPNOG NOVCA U KASI")
//            if (cashBalanceType == CashBalanceType.OUT) {
//                if ((totalAmount + transactionsAmount) < abs(amount)) {
//                    txtCashBalanceError.text =
//                        getString(R.string.msg_amount_grater_than_total_available)
//                    return@setOnClickListener
//                }
//            }
            val cashObj = CashBalance()
            val cashBalance = cashObj.copy(
                uuid = UUID.randomUUID().toString(),
                cashAmount = amount,
                notes = cashBalanceType,//CashBalanceType.IN,
                operation = cashBalanceType, //CashBalanceType.IN,
                userId = configuration.userData?.id,
                nuuis = viewModel.getFiscalDigitalKey()?.pib,
                deviceId = DeviceId(requireContext()).myDeviceId(),
                cashBalanceState = FiscalisationState.NOT_FISCALIZED,
                createdAt = LocalDate.now()
            )
            // Setuje cash Balance
            viewModel.setCashBalance(cashBalance = cashBalance)

        }

        cashBalanceTypeAddFunds.setOnClickListener {
            cashBalanceType = CashBalanceType.IN
            clearBalanceTypeView()
            cashBalanceTypeAddFunds.setCardBackgroundColor(resources.getColor(R.color.blueDark))
            viewSelectedIN.visibility = View.VISIBLE
        }

        cashBalanceTypeWithdraw.setOnClickListener {
            cashBalanceType = CashBalanceType.OUT
            clearBalanceTypeView()
            cashBalanceTypeWithdraw.setCardBackgroundColor(resources.getColor(R.color.blueDark))
            viewSelectedOUT.visibility = View.VISIBLE
        }

        val observerHasCertificate = Observer<Boolean> {
            hasLicense = it
        }


        viewModel.mutableLiveDataCashBalance.observe(viewLifecycleOwner, observerCashBalance)
        viewModel.mutableLiveDataSignedDocument.observe(viewLifecycleOwner, observerSignedDocument)
        viewModel.mutableLiveDataHasOpenedDay.observe(viewLifecycleOwner, observerOpenedDay)
        viewModel.mutableLiveDataErrorHandler.observe(viewLifecycleOwner, observerError)
        viewModel.mutableLiveDataCashBalanceFCDC.observe(
            viewLifecycleOwner,
            observerCashBalanceFCDC
        )
        viewModel.mutableLiveDataRemoteCashBalance.observe(
            viewLifecycleOwner,
            observerRemoteCashBalance
        )
        viewModel.mutableLiveDataHasCertificate.observe(this, observerHasCertificate)

        viewModel.hasOpenDay(DeviceId(requireContext()).myDeviceId())
        viewModel.getCertificate()
    }


    private fun getViewsByTag(root: ViewGroup?, tag: String?): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root?.childCount
        for (i in 0 until childCount!!) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag))
            }

            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                views.add(child)
            }

        }
        return views
    }

    private fun clearBalanceTypeView() {
        cashBalanceTypeAddFunds.setCardBackgroundColor(resources.getColor(R.color.gray_700))
        cashBalanceTypeWithdraw.setCardBackgroundColor(resources.getColor(R.color.gray_700))
        cashBalanceTypeOpenDay.setCardBackgroundColor(resources.getColor(R.color.gray_700))

        viewSelectedBalance.visibility = View.INVISIBLE
        viewSelectedIN.visibility = View.INVISIBLE
        viewSelectedOUT.visibility = View.INVISIBLE

        updateAmountView()
    }

    private fun updateAmountView() {
        var amount = abs(edtBottomItemQuantity.text.toString().toLong() * 1)

        when (cashBalanceType) {
            CashBalanceType.OUT -> {
                amount = "-$amount".toLong()
            }
        }
        edtBottomItemQuantity.setText(amount.toString(), TextView.BufferType.EDITABLE)

    }

    private fun setProgressBar(visible: Boolean) {
        viewModel.mutableLiveDataProgressBar.value = visible
    }

//    fun setCashBalanceRequestMapper(cashBalance: CashBalance): RemoteCashBalanceRequest {
//        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss")
//        return RemoteCashBalanceRequest(
//            id = cashBalance.uuid,
//            cashAmount = cashBalance.cashAmount,
//            operation = cashBalance.operation,
//            responseUUID = cashBalance.responseUUID,
//            changeDateTime = cashBalance.changeDateTime?.format(formatter),
//            fcdc = cashBalance.fcdc
//        )
//    }

}