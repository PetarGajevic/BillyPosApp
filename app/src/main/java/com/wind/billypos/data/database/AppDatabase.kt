package com.wind.billypos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wind.billypos.data.dao.*
import com.wind.billypos.data.local.model.*
import com.wind.billypos.data.local.model.typeconverter.DataConverter
import com.wind.billypos.data.local.model.typeconverter.ListConverter


@Database(
    entities = [LocalConfiguration::class, LocalCompany::class, LocalCashBalance::class,
        LocalFiscalDigitalKey::class, LocalSubCategory::class, LocalCategory::class, LocalItem::class, LocalTransactionBody::class,
        LocalTransactionHead::class, LocalTransactionPayment::class, LocalCustomer::class, LocalLicense::class, LocalEntityPoint::class,
        LocalOperator::class], version = 1, exportSchema = false
)
@TypeConverters(DataConverter::class, ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val loginDao: LoginDao
    abstract val cashBalanceDao: CashBalanceDao
    abstract val companyDao: CompanyDao
    abstract val invoiceDao: InvoiceDao
    abstract val transactionDao: TransactionDao
    abstract val cartPaymentDao: CartPaymentDao
    abstract val itemDao: ItemDao
    abstract val customerDao: CustomerDao
    abstract val licenceDao: LicenceDao
    abstract val syncDao: SyncDao
    abstract val syncAndBackupDao: SyncAndBackupDao
    abstract val exportUnfiscalisedDao: ExportUnfiscalisedDao
    abstract val orderInvoiceDao: OrderInvoiceDao
    abstract val operatorDao: OperatorDao
    abstract val categoryDao: CategoryDao
    abstract val entityPointDao: EntityPointDao
}