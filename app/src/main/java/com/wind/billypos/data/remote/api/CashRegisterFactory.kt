package com.wind.billypos.data.remote.api

import android.app.Application
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import com.wind.billypos.BuildConfig
import com.wind.billypos.utils.network.TLS12SocketFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
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
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


//class CashRegisterFactory private constructor(
//    private val application: Application
//) {
//
//    private var sRetrofit: Retrofit? = null
//
//    fun getClient(): Retrofit {
//        if (sRetrofit == null) {
//            synchronized(Retrofit::class.java) {
//                val interceptor = HttpLoggingInterceptor()
//                interceptor.level =
//                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//
//                // Create a trust manager that does not validate certificate chains
//                val trustAllCerts =
//                    object : X509TrustManager {
//                        @Throws(CertificateException::class)
//                        override fun checkClientTrusted(
//                            chain: Array<X509Certificate>,
//                            authType: String
//                        ) {
//                        }
//
//                        @Throws(CertificateException::class)
//                        override fun checkServerTrusted(
//                            chain: Array<X509Certificate>,
//                            authType: String
//                        ) {
//                        }
//
//                        override fun getAcceptedIssuers(): Array<X509Certificate> {
//                            return arrayOf()
//                        }
//                    }
//
//                val sc = SSLContext.getInstance("TLSv1.2")
//                sc.init(null, arrayOf<X509TrustManager>(trustAllCerts), SecureRandom())
//
//                val client = OkHttpClient.Builder()
//                    .addInterceptor(interceptor)
//                    .sslSocketFactory(TLS12SocketFactory(sc.socketFactory), trustAllCerts)
////                    .followRedirects(true)
////                    .followSslRedirects(true)
////                    .retryOnConnectionFailure(true)
////                    .cache(null)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .connectTimeout(60, TimeUnit.SECONDS)
////                    .enableTLS120OnPreLollipop()
//                    .build()
//
//                val gson = GsonBuilder()
//                    .registerTypeAdapter(
//                        LocalDateTime::class.java,
//                        JsonDeserializer<Any?> { json, type, _ ->
//                            try {
//                                LocalDateTime.parse(
//                                    json.asJsonPrimitive.asString,
//                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                                )
//                            } catch (e: DateTimeParseException) {
//                                ZonedDateTime.parse(
//                                    json.asJsonPrimitive.asString
//                                ).toLocalDateTime()
//                            }
//                        })
//                    .registerTypeAdapter(
//                        LocalDateTime::class.java,
//                        JsonSerializer<LocalDateTime> { date, _, _ ->
//                            try {
//                                if (date != null) JsonPrimitive(date.format(DateTimeFormatter.ISO_DATE_TIME)) else null
//                            } catch (e: DateTimeParseException) {
//                                if (date != null) JsonPrimitive(date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)) else null
//                            }
//
//                        })
//                    .create()
//                val gsonConverterFactory = GsonConverterFactory.create(gson)
//                sRetrofit = Retrofit.Builder()
//                    .baseUrl(BuildConfig.BASE_URL)
//                    .client(client)
//                    .addConverterFactory(MultipleConverterFactory.Builder()
//                        .setXmlConverterFactory(TikXmlConverterFactory.create())
//                        .setJsonConverterFactory(gsonConverterFactory)
//                        .build())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build()
//            }
//        }
//        return sRetrofit!!
//    }
//
//    class MultipleConverterFactory(private val factories: Map<Class<*>, Converter.Factory>) : Converter.Factory() {
//
//        override fun responseBodyConverter(
//            type: Type,
//            annotations: Array<Annotation>,
//            retrofit: Retrofit
//        ): Converter<ResponseBody, *>? {
//            return annotations.mapNotNull { factories[it.annotationClass.javaObjectType] }
//                .getOrNull(0)
//                ?.responseBodyConverter(type, annotations, retrofit)
//        }
//
//        class Builder {
//
//            private val factories = hashMapOf<Class<*>, Converter.Factory>()
//
//            fun setXmlConverterFactory(converterFactory: Converter.Factory): Builder {
//                factories[TaxApi.XML::class.java] = converterFactory
//                return this
//            }
//
//            fun setJsonConverterFactory(converterFactory: Converter.Factory): Builder {
//                factories[TaxApi.JSON::class.java] = converterFactory
//                return this
//            }
//
//            @Suppress("unused")
//            fun addCustomConverterFactory(
//                annotation: Class<out Annotation>,
//                converterFactory: Converter.Factory
//            ): Builder {
//                factories[annotation] = converterFactory
//                return this
//            }
//
//            fun build(): MultipleConverterFactory {
//                return MultipleConverterFactory(factories)
//            }
//        }
//    }
//
//    companion object {
//        private var INSTANCE: CashRegisterFactory? = null
//
//        private val lock = Any()
//
//        fun getInstance(application: Application): CashRegisterFactory {
//            synchronized(lock) {
//                if (INSTANCE == null) {
//                    INSTANCE = CashRegisterFactory(application)
//                }
//                return INSTANCE!!
//            }
//        }
//    }
//
//}