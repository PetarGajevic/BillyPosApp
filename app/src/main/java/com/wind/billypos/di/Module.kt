package com.wind.billypos.di

import com.wind.billypos.data.database.AppDatabase
import com.wind.billypos.view.ui.login.repository.LoginRepository
import com.wind.billypos.view.ui.login.viewmodel.LoginViewModel
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import com.wind.billypos.data.dao.*
import com.wind.billypos.data.remote.api.*
import com.wind.billypos.data.remote.interceptor.ServiceInterceptor
import com.wind.billypos.utils.PreferenceHelper
import com.wind.billypos.utils.PreferenceHelper.token
import com.wind.billypos.utils.network.TLS12SocketFactory
import com.wind.billypos.view.ui.HomeSharedViewModel
import com.wind.billypos.view.ui.cashbalance.repository.CashBalanceRepository
import com.wind.billypos.view.ui.cashbalance.viewmodel.AddCashBalanceViewModel
import com.wind.billypos.view.ui.cashbalance.viewmodel.CashBalanceViewModel
import com.wind.billypos.view.ui.category.repository.CategoryRepository
import com.wind.billypos.view.ui.category.repository.SubCategoryRepository
import com.wind.billypos.view.ui.category.viewmodel.*
import com.wind.billypos.view.ui.certificate.repository.CertificateRepository
import com.wind.billypos.view.ui.certificate.viewmodel.CertificateSetupViewModel
import com.wind.billypos.view.ui.customer.repository.CustomerRepository
import com.wind.billypos.view.ui.customer.viewmodel.AddCustomerViewModel
import com.wind.billypos.view.ui.customer.viewmodel.CustomerDetailsViewModel
import com.wind.billypos.view.ui.customer.viewmodel.CustomerViewModel
import com.wind.billypos.view.ui.home.HomeRepository
import com.wind.billypos.view.ui.home.viewmodel.HomeViewModel
import com.wind.billypos.view.ui.invoice.repository.*
import com.wind.billypos.view.ui.invoice.viewmodel.*
import com.wind.billypos.view.ui.item.repository.ItemRepository
import com.wind.billypos.view.ui.item.viewmodel.AddItemViewModel
import com.wind.billypos.view.ui.item.viewmodel.EditItemViewModel
import com.wind.billypos.view.ui.item.viewmodel.ItemDetailsViewModel
import com.wind.billypos.view.ui.item.viewmodel.ItemViewModel
import com.wind.billypos.view.ui.operators.repository.OperatorRepository
import com.wind.billypos.view.ui.operators.viewmodel.AddOperatorViewModel
import com.wind.billypos.view.ui.operators.viewmodel.EditOperatorViewModel
import com.wind.billypos.view.ui.operators.viewmodel.OperatorViewModel
import com.wind.billypos.view.ui.order.repository.EntityPointRepository
import com.wind.billypos.view.ui.order.repository.OrderInvoiceRepository
import com.wind.billypos.view.ui.order.viewmodel.EntityPointViewModel
import com.wind.billypos.view.ui.order.viewmodel.OrderInvoiceViewModel
import com.wind.billypos.view.ui.sales.viewmodel.SalesReportViewModel
import com.wind.billypos.view.ui.settings.repository.ExportUnfiscalisedRepository
import com.wind.billypos.view.ui.settings.repository.FiscalisationRepository
import com.wind.billypos.view.ui.settings.repository.SyncAndBackupRepository
import com.wind.billypos.view.ui.settings.repository.SyncRepository
import com.wind.billypos.view.ui.settings.viewmodel.*
import com.wind.billypos.view.ui.setup.repository.LicenceSetupRepository
import com.wind.billypos.view.ui.setup.viewmodel.LicenceSetupViewModel
import com.wind.billypos.view.ui.transaction.repository.TransactionDetailsRepository
import com.wind.billypos.view.ui.transaction.repository.TransactionSalesRepository
import com.wind.billypos.view.ui.transaction.viewmodel.TransactionDetailsViewModel
import com.wind.billypos.view.ui.transaction.viewmodel.TransactionViewModel
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

// Dependency Injection ViewModel
val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { InvoiceViewModel(get()) }
    viewModel { SettingsViewModel() }
    viewModel { SyncAndBackupViewModel(get()) }
    viewModel { ExportUnfiscalisedVideModel(get()) }
    viewModel { FiscalisationViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
    viewModel { ItemViewModel(get()) }
    viewModel { CashBalanceViewModel(get()) }
    viewModel { AddCashBalanceViewModel(get()) }
    viewModel { CustomerViewModel(get()) }
    viewModel { AddCustomerViewModel(get()) }
    viewModel { OrderInvoiceViewModel(get()) }
    viewModel { SalesReportViewModel() }
    viewModel { CertificateSetupViewModel(get()) }
    viewModel { CustomerDetailsViewModel(get()) }
    viewModel { ItemDetailsViewModel(get()) }
    viewModel { AddItemViewModel(get()) }
    viewModel { EditItemViewModel(get()) }
    viewModel { TransactionDetailsViewModel(get()) }
    viewModel { CartPaymentViewModel(get()) }
    viewModel { CartDoneViewModel() }
    viewModel { PrintInvoiceViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { TransactionItemViewModel(get()) }
    viewModel { LicenceSetupViewModel(get()) }
    viewModel { SyncViewModel(get()) }
    viewModel { TransactionOptionsViewModel() }
    viewModel { OperatorViewModel(get()) }
    viewModel { AddOperatorViewModel(get()) }
    viewModel { EditOperatorViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { AddCategoryViewModel(get()) }
    viewModel { AddSubCategoryViewModel(get()) }
    viewModel { CategoryDetailsViewModel(get()) }
    viewModel { SubCategoryDetailsViewModel(get()) }
    viewModel { EditCategoryViewModel(get()) }
    viewModel { EntityPointViewModel(get()) }

    single { HomeSharedViewModel(get()) }
}


// Dependency Injection Repository
val repositoryModule = module {
    single { LoginRepository(get(), get(), get()) }
    single { FiscalisationRepository() }
    single { CashBalanceRepository(get(), get(), get()) }
    single { CertificateRepository(get()) }
    single { HomeRepository(get(), get()) }
    single { InvoiceRepository(get()) }
    single { CartRepository(get()) }
    single { TransactionRepository(get()) }
    single { CartPaymentRepository(get(), get(), get()) }
    single { TransactionSalesRepository(get()) }
    single { TransactionDetailsRepository(get(), get(), get()) }
    single { PrintInvoiceRepository(get()) }
    single { ItemRepository(get(), get()) }
    single { CustomerRepository(get(), get()) }
    single { LicenceSetupRepository(get(), get()) }
    single { SyncRepository(get(), get()) }
    single { HomeSharedRepository(get()) }
    single { SyncAndBackupRepository(get()) }
    single { ExportUnfiscalisedRepository(get()) }
    single { OrderInvoiceRepository(get()) }
    single { OperatorRepository(get(), get()) }
    single { CategoryRepository(get(), get()) }
    single { SubCategoryRepository(get(), get()) }
    single { EntityPointRepository(get()) }
}


// Dependency Injection  Database
val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "billy_pos_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    fun provideLoginDao(database: AppDatabase): LoginDao =
        database.loginDao

    fun provideCashBalanceDao(database: AppDatabase): CashBalanceDao =
        database.cashBalanceDao

    fun provideCompanyDao(database: AppDatabase): CompanyDao =
        database.companyDao

    fun provideInvoiceDao(database: AppDatabase): InvoiceDao =
        database.invoiceDao

    fun provideTransactionDao(database: AppDatabase): TransactionDao =
        database.transactionDao

    fun provideCartPaymentDao(database: AppDatabase): CartPaymentDao =
        database.cartPaymentDao

    fun provideItemDao(database: AppDatabase): ItemDao =
        database.itemDao

    fun provideCustomerDao(database: AppDatabase): CustomerDao =
        database.customerDao

    fun provideLicenceDao(database: AppDatabase): LicenceDao =
        database.licenceDao

    fun provideSyncDao(database: AppDatabase): SyncDao =
        database.syncDao

    fun provideSyncAndBackupDao(database: AppDatabase): SyncAndBackupDao =
        database.syncAndBackupDao

    fun provideExportUnfiscalisedDao(database: AppDatabase): ExportUnfiscalisedDao =
        database.exportUnfiscalisedDao

    fun provideOrderInvoiceDao(database: AppDatabase): OrderInvoiceDao =
        database.orderInvoiceDao

    fun provideOperatorDao(database: AppDatabase): OperatorDao =
        database.operatorDao

    fun provideCategoryDao(database: AppDatabase): CategoryDao =
        database.categoryDao

    fun provideEntityPointDao(database: AppDatabase): EntityPointDao =
        database.entityPointDao

    single { provideDatabase(androidApplication()) }
    single { provideLoginDao(get()) }
    single { provideCashBalanceDao(get()) }
    single { provideCompanyDao(get()) }
    single { provideInvoiceDao(get()) }
    single { provideTransactionDao(get()) }
    single { provideCartPaymentDao(get()) }
    single { provideItemDao(get()) }
    single { provideCustomerDao(get()) }
    single { provideLicenceDao(get()) }
    single { provideSyncDao(get()) }
    single { provideSyncAndBackupDao(get()) }
    single { provideExportUnfiscalisedDao(get()) }
    single { provideOrderInvoiceDao(get()) }
    single { provideOperatorDao(get()) }
    single { provideCategoryDao(get()) }
    single { provideEntityPointDao(get()) }
}


// Dependency Injection  API
val apiModule = module {
    fun provideLoginApi(retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    fun provideCashBalanceApi(retrofit: Retrofit): CashBalanceApi =
        retrofit.create(CashBalanceApi::class.java)

    fun provideTaxApi(retrofit: Retrofit): TaxApi =
        retrofit.create(TaxApi::class.java)

    fun provideTransactionApi(retrofit: Retrofit): TransactionApi =
        retrofit.create(TransactionApi::class.java)

    fun provideItemApi(retrofit: Retrofit): ItemApi =
        retrofit.create(ItemApi::class.java)

    fun provideCustomerApi(retrofit: Retrofit): CustomerApi =
        retrofit.create(CustomerApi::class.java)

    fun provideLicenceApi(retrofit: Retrofit): LicenceApi =
        retrofit.create(LicenceApi::class.java)

    fun provideSyncApi(retrofit: Retrofit): SyncApi =
        retrofit.create(SyncApi::class.java)

    fun provideCategoryApi(retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    fun provideOperatorApi(retrofit: Retrofit): OperatorApi =
        retrofit.create(OperatorApi::class.java)

    single { provideLoginApi(get(named("base_login_url"))) }
    single { provideCashBalanceApi(get(named("base_url"))) }
    single { provideTaxApi(get(named("base_tax_url"))) }
    single { provideTransactionApi(get(named("base_url"))) }
    single { provideItemApi(get(named("base_url"))) }
    single { provideCustomerApi(get(named("base_url"))) }
    single { provideLicenceApi(get(named("base_url"))) }
    single { provideSyncApi(get(named("base_url"))) }
    single { provideCategoryApi(get(named("base_url"))) }
    single { provideOperatorApi(get(named("base_url"))) }
}


 // Dependency Injection  Shared Preferences
val preferencesModule = module {

    fun provideSettingsPreferences(application: Application): SharedPreferences =
        application.applicationContext.getSharedPreferences(PreferenceHelper.CUSTOM_PREF_NAME, Context.MODE_PRIVATE)

    single { provideSettingsPreferences(get()) }
}


// Dependency Injection Retrofit
val retrofitModule = module{

    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
//        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    fun provideToken(app: Application): String? {
        return PreferenceHelper.customPreference(app.applicationContext).token
    }

    val gson = GsonBuilder()
        .registerTypeAdapter(
            LocalDateTime::class.java,
            JsonDeserializer<Any?> { json, type, _ ->
                try {
                    LocalDateTime.parse(
                        json.asJsonPrimitive.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    )
                } catch (e: DateTimeParseException) {
                    ZonedDateTime.parse(
                        json.asJsonPrimitive.asString
                    ).toLocalDateTime()
                }
            })
        .registerTypeAdapter(
            LocalDateTime::class.java,
            JsonSerializer<LocalDateTime> { date, _, _ ->
                try {
                    if (date != null) JsonPrimitive(date.format(DateTimeFormatter.ISO_DATE_TIME)) else null
                } catch (e: DateTimeParseException) {
                    if (date != null) JsonPrimitive(date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)) else null
                }

            })
        .create()


    // Create a trust manager that does not validate certificate chains
    val trustAllCerts =
        object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    val sc = SSLContext.getInstance("TLSv1.2")
    sc.init(null, arrayOf<X509TrustManager>(trustAllCerts), SecureRandom())


    single(named("base_login_url")) {

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            // .addInterceptor(ServiceInterceptor(provideToken(get())))
            .build()

        Retrofit.Builder()
            .baseUrl(com.wind.billypos.BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }



    single(named("base_url")) {

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Accept", "*/*")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", provideToken(get())!!)
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(com.wind.billypos.BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }
    val gsonConverterFactory = GsonConverterFactory.create(gson)

    single(named("base_tax_url")) {

        val xmlClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .sslSocketFactory(TLS12SocketFactory(sc.socketFactory), trustAllCerts)
            .build()

        Retrofit.Builder()
            .baseUrl(com.wind.billypos.BuildConfig.BASE_TAX_URL)
          //  .addConverterFactory(TikXmlConverterFactory.create())
            .addConverterFactory(MultipleConverterFactory.Builder()
                .setXmlConverterFactory(TikXmlConverterFactory.create())
                .setJsonConverterFactory(gsonConverterFactory)
                .build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(xmlClient)
            .build()
    }

}



class MultipleConverterFactory(private val factories: Map<Class<*>, Converter.Factory>) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return annotations.mapNotNull { factories[it.annotationClass.javaObjectType] }
            .getOrNull(0)
            ?.responseBodyConverter(type, annotations, retrofit)
    }

    class Builder {

        private val factories = hashMapOf<Class<*>, Converter.Factory>()

        fun setXmlConverterFactory(converterFactory: Converter.Factory): Builder {
            factories[TaxApi.XML::class.java] = converterFactory
            return this
        }

        fun setJsonConverterFactory(converterFactory: Converter.Factory): Builder {
            factories[TaxApi.JSON::class.java] = converterFactory
            return this
        }

        @Suppress("unused")
        fun addCustomConverterFactory(
            annotation: Class<out Annotation>,
            converterFactory: Converter.Factory
        ): Builder {
            factories[annotation] = converterFactory
            return this
        }

        fun build(): MultipleConverterFactory {
            return MultipleConverterFactory(factories)
        }
    }
}