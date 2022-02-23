package com.wind.billypos.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor(private var token: String?) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("No-Authentication") == null) {

            //or use Token Function
            if (token?.isNotEmpty() == true) {
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                 //   .addHeader("Authorization", finalToken)
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhcHBpb3NAZ21haWwuY29tIiwicm9sZXMiOlsiT1BFUkFUT1JfTU9CSUxFIl0sImV4cCI6MTYzMjEyNzU2NX0.NWD6-Mhh2Kg8QsButvrL_PJ9q3bvk8cjq3d0HJSfiNC7S5_rYqoWlO71N-PLzdsFD2xz2ECWGl3KOQqOP8GAEw")
                    .build()
            }

        }

        return chain.proceed(request)
    }
}