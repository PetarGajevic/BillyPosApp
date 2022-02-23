package com.wind.billypos.view.ui.transaction.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wind.billypos.R
import com.wind.billypos.databinding.RowTransactionBinding
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.PaymentMethod
import com.wind.billypos.view.model.Configuration
import com.wind.billypos.view.model.TransactionHead
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.lang.String
import java.util.*


class TransactionAdapter(
    private val context: Context,
    private val configuration: Configuration,
    val showTransactionListener: ShowTransactionListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var date: LocalDate = LocalDate.now()
    var lastDate: LocalDate = LocalDate.now()
    val TYPE_DATE = 2000
    val ITEMS_LIST = 100
    private var dateList: List<TransactionHead?>? = null

    private var transactions: MutableList<RecyclerViewObject> = ArrayList()
    private var transactionInfator: LayoutInflater? = null

    fun setItems(items: List<TransactionHead>){
        transactions = ArrayList()
        var mDate: LocalDate? = null

        for (itm in items) {
            if (mDate == null || mDate != itm.dayCreated) {
                mDate = itm.dayCreated
                transactions.add(RecyclerViewObject(TYPE_DATE, itm))
            }
            transactions.add(RecyclerViewObject(ITEMS_LIST, itm))
        }
        notifyDataSetChanged()
    }

    fun setDates(it: List<TransactionHead?>?) {
        dateList = it
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val transactionView: View
        transactionInfator = LayoutInflater.from(context)
        return if (viewType == TYPE_DATE) {
            transactionView =
                transactionInfator?.inflate(R.layout.row_transaction_date, parent, false)!!
            DateViewHolder(transactionView)
        } else {
            transactionView =
                transactionInfator?.inflate(R.layout.row_transaction, parent, false)!!
            TransactionViewHolder(transactionView)
        }
    }

     class TransactionViewHolder(transactionView: View) :
        RecyclerView.ViewHolder(transactionView) {
         val invoiceAmountView: TextView
         val status: TextView
         val itemPaymentMethod: ImageView
         val transactionClient: ImageView
         val invoiceTimeView: TextView

        init {
            invoiceAmountView = transactionView.findViewById(R.id.itemPrice)
            invoiceTimeView = transactionView.findViewById(R.id.itemDate)
            status = transactionView.findViewById(R.id.statusTransaction)
            itemPaymentMethod = transactionView.findViewById(R.id.itemPaymentMethod)
            transactionClient = transactionView.findViewById(R.id.transactionClient)
        }
    }

    class DateViewHolder(dateView: View) :
        RecyclerView.ViewHolder(dateView) {
         val invoiceDateView: TextView

        init {
            invoiceDateView = dateView.findViewById(R.id.itemTransactionDate)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return transactions.get(position).viewType
    }



    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val current = transactions.get(position)

        if(current.viewType == TYPE_DATE){
            val date = current.objectAny

            (holder as DateViewHolder).invoiceDateView.text = date.dayCreated.toString()
        }else{
            val currentTransaction = current.objectAny
            Timber.e("Current transaction %s", currentTransaction.transactionState)

            val time = currentTransaction.dayCreated
            (holder as TransactionViewHolder).invoiceAmountView.text = String.format(
                "%s %.02f",
                configuration.company?.currencyCode,
                currentTransaction.totalWithVat
            )
            (holder as TransactionViewHolder).status.setText(currentTransaction.getTransactionStateLabel())

            if (currentTransaction.transactionState == FiscalisationState.FISCALIZED) {
                transactionInfator?.getContext()?.getResources()?.let {
                    (holder as TransactionViewHolder).status.setBackgroundColor(
                        it.getColor(R.color.lightSuccess)
                    )
                }
                Timber.e("FiscalisationState.FISCALIZED")
            } else {
                transactionInfator?.getContext()?.getResources()?.let {
                    (holder as TransactionViewHolder).status.setBackgroundColor(
                        it.getColor(R.color.gray_100)
                    )
                }
                Timber.e("NOT FiscalisationState.FISCALIZED")
            }

            holder.invoiceTimeView.text = time.toString()

            if (currentTransaction.paymentMethod === PaymentMethod.CASH) {
                holder.itemPaymentMethod.setImageResource(R.drawable.ic_baseline_money_24px)
            } else if (currentTransaction.paymentMethod === PaymentMethod.CREDIT_CARD) {
                holder.itemPaymentMethod.setImageResource(R.drawable.ic_payment_cc)
            }

            if (currentTransaction.idCustomer.isNotEmpty()) {
                holder.transactionClient.visibility = View.VISIBLE
            } else {
                holder.transactionClient.visibility = View.GONE
            }


             holder.itemView.setOnClickListener { showTransactionListener.onTransactionClick(currentTransaction) }
        }

    }

    override fun getItemCount(): Int = transactions.size
    }



interface ShowTransactionListener{
    fun onTransactionClick(trnHead: TransactionHead)
}