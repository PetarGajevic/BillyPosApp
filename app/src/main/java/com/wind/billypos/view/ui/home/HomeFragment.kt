package com.wind.billypos.view.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentHomeBinding
import com.wind.billypos.utils.DeviceId
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.token
import com.wind.billypos.utils.extensions.navigateSafe
import com.wind.billypos.utils.getNavigationResult
import com.wind.billypos.view.model.License
import com.wind.billypos.view.ui.home.viewmodel.HomeViewModel
import com.wind.billypos.view.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.time.LocalDate


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private val homeViewModel : HomeViewModel by viewModel()
    private var isStartDay: Boolean = false

    override val bindingVariable: Int
        get() = BR.homeViewModel
    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModel: HomeViewModel
        get() = homeViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val hasOpenedDayObserver = Observer<Boolean>{
            Timber.e("Is start day %s", it)
            isStartDay = it
            if(isStartDay){
                btnOpenDay.visibility = View.GONE
            }
        }



        val observerHasLicence = Observer<License?> {
            it?.let{
                val expiryDate: CharSequence? = it.expiryDate
                if(it.state == "UNAVAILABLE" && org.threeten.bp.LocalDate.parse(expiryDate) < org.threeten.bp.LocalDate.now()){
                    Timber.e("Usao u has licence")
                }else{
                    Timber.e("Nije usao u has licence")
                }
            }
        }

        val pref = PreferenceHelper.customPreference(activity as AppCompatActivity)

        Timber.e("Token: %s", pref.token)

        getNavigationResult(REFRESH_DATA)?.observe(viewLifecycleOwner) { _ ->
            homeViewModel.hasOpenedDay(deviceId = DeviceId(requireContext()).myDeviceId())
        }

        btnOpenPOS.setOnClickListener {
            if(isStartDay)
                findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToInvoiceFragment(newSale = true))
            else
                findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToCashBalanceBottomSheetDialogFragment())
        }
        btnOpenTransactions.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToTransactionFragment()) }
        btnOpenItems.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToItemFragment()) }
        btnOpenCashBalance.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToCashBalanceFragment()) }
        btnOpenCustomers.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToCustomerFragment()) }
        btnOpenOrderActivity.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToOrderInvoiceFragment()) }
        btnSalesReport.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToSalesReportFragment()) }
        btnOpenDay.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToCashBalanceBottomSheetDialogFragment()) }
        btnOpenOperators.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToOperatorsFragment()) }
        btnOpenCategory.setOnClickListener { findNavController().navigateSafe(HomeFragmentDirections.actionHomeFragmentToCategoryFragment()) }


        homeViewModel.mutableLiveDataHasOpenedDay.observe(viewLifecycleOwner, hasOpenedDayObserver)
        homeViewModel.mutableLiveDataHasLicence.observe(viewLifecycleOwner, observerHasLicence)

        homeViewModel.hasOpenedDay(deviceId = DeviceId(requireContext()).myDeviceId())
        viewModel.getLicence()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}