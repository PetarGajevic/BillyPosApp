package com.wind.billypos.view.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.wind.billypos.BR
import com.wind.billypos.R
import com.wind.billypos.base.BaseFragment
import com.wind.billypos.databinding.FragmentExportUnfiscalisedBinding
import com.wind.billypos.view.model.CashBalance
import com.wind.billypos.view.model.TransactionHead
import com.wind.billypos.view.ui.settings.viewmodel.ExportUnfiscalisedVideModel
import kotlinx.android.synthetic.main.fragment_export_unfiscalised.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.collections.ArrayList

class ExportUnfiscalisedFragment : BaseFragment<FragmentExportUnfiscalisedBinding, ExportUnfiscalisedVideModel>() {

    private val exportUnfiscalisedVideModel : ExportUnfiscalisedVideModel by viewModel()

    override val bindingVariable: Int
        get() = BR.exportUnfiscalisedVideModel
    override val layoutId: Int
        get() = R.layout.fragment_export_unfiscalised
    override val viewModel: ExportUnfiscalisedVideModel
        get() = exportUnfiscalisedVideModel

    private var transactionHeadList = listOf<TransactionHead>()
    private var cashBalanceList = listOf<CashBalance>()

    private var saveZIPDirectory: File? = null

    val ZIP_DIR = "/billyPOS/zip_dir/"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observerCashBalanceList = Observer<List<CashBalance>> {
            cashBalanceList = it
        }

        val observerTransactionHeadList = Observer<List<TransactionHead>> {
            transactionHeadList = it
        }

        exportInvoicesBtn.setOnClickListener {
            exportUnfiscalised()
        }

        viewModel.mutableLiveDataCashBalanceList.observe(viewLifecycleOwner, observerCashBalanceList)
        viewModel.mutableLiveDataTransactionHeadList.observe(viewLifecycleOwner, observerTransactionHeadList)

        viewModel.findAllNotFiscalisedInvoices()
        viewModel.findAllNotFiscalisedCashBalance()
    }

    @SuppressLint("SimpleDateFormat")
    private fun exportUnfiscalised(){

        initDirectoryZIP()

//        val dateFormat =  SimpleDateFormat("yyyyMMddHHmmss")
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateFormat =  SimpleDateFormat("yyyyMMddHHmmss")
        val zipFilesIIC = ArrayList<TransactionHead>()
        val zipFilesCashDeposit = ArrayList<CashBalance>()

        for(transaction in transactionHeadList){
            zipFilesIIC.add(transaction)
        }

        for(cashBalance in cashBalanceList){
            zipFilesCashDeposit.add(cashBalance)
        }

        val timeStamp = dateFormat.format(Calendar.getInstance().time)
        val zipFolderLocation = saveZIPDirectory?.path ?: ""
        val zipFileName = "$zipFolderLocation/${timeStamp}_request.zip"

        // Define output stream
        val fos = FileOutputStream(zipFileName)
        val zos = ZipOutputStream(fos)

        try {
            for(transaction in zipFilesIIC) {
                val fileXML = "$zipFolderLocation/${transaction.iic}.xml"
                if(!File(fileXML).exists()){
                    Log.d("IICFILES", "NOT_FOUND ${transaction.iic}.xml")
//                    continue
                }
                Log.d("IICFILES", "ATTACHED ${transaction.iic}.xml")
                val zipEntry = ZipEntry("${transaction.createdAt.format(formatter)}_${transaction.iic}_request.xml")
                zos.putNextEntry(zipEntry)

                val fis = FileInputStream(fileXML)
                val buffer = ByteArray(1024)

                var length: Int = 0
                while (fis.read(buffer).also { length = it } > 0) {
                    zos.write(buffer, 0, length)
                }
                zos.closeEntry()
                fis.close()
            }

            for(cashAction in zipFilesCashDeposit) {
                val fileXML = "$zipFolderLocation/${cashAction.responseUUID}.xml"
                if(!File(fileXML).exists()){
                    Log.d("IICFILES", "NOT_FOUND ${cashAction.responseUUID}.xml")
//                    continue
                }
                Log.d("IICFILES", "ATTACHED ${cashAction.responseUUID}.xml")
                val zipEntry = ZipEntry("${cashAction.createdAt?.format(formatter)}_deposit_request.xml")
                zos.putNextEntry(zipEntry)

                val fis = FileInputStream(fileXML)
                val buffer = ByteArray(1024)

                var length: Int = 0
                while (fis.read(buffer).also { length = it } > 0) {
                    zos.write(buffer, 0, length)
                }
                zos.closeEntry()
                fis.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            // unable to write zip
//            _mZipFileCreation.postValue(
//                CreatingZipFileResult(loading = false, zipFile = null)
//            )
            return
        } finally {
            zos.close()
        }

        try {
            for(transaction in zipFilesIIC) {
                Log.d("IICFILES", "DELETED ${transaction.iic}.xml")
                val fileXML = "$zipFolderLocation/${transaction.iic}.xml"
                val file = File(fileXML)
                file.delete()
            }

            for(cashAction in zipFilesCashDeposit) {
                Log.d("IICFILES", "DELETED ${cashAction.responseUUID}.xml")
                val fileXML = "$zipFolderLocation/${cashAction.responseUUID}.xml"
                val file = File(fileXML)
                file.delete()
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    private fun initDirectoryZIP(){
        saveZIPDirectory = File(requireContext().getExternalFilesDir(null).toString() + ZIP_DIR)
        if(saveZIPDirectory != null) {
            if (!saveZIPDirectory!!.exists() || !saveZIPDirectory!!.isDirectory()) {
                val isCreated = saveZIPDirectory!!.mkdirs()
                Log.d("ZIP_FOLDER", isCreated.toString())
            }
            Log.d("ZIP_FOLDER_EXIST", saveZIPDirectory!!.exists().toString())
            Log.d("ZIP_FOLDER_isDirectory", saveZIPDirectory!!.isDirectory().toString())
        }
    }

}