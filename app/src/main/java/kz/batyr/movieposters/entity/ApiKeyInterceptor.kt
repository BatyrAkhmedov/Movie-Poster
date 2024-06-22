package kz.batyr.movieposters.entity

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class ApiKeyInterceptor () : Interceptor {

    companion object {
        private const val API_KEY2 = "78697973-51fe-4ecd-8247-fbfb56280f96"
        private const val API_KEY3 = "5f8af297-b130-4202-b20c-63a52a8fc711"
        private const val API_KEY = "649630ce-acdb-4e4d-97f2-78099bfac628"
        private const val API_KEY4 = "8a1f756a-6dff-45ed-8336-d980a51e7731"
        private const val API_KEY5 = "cb10be9f-4cdb-4020-a46a-3a04b34d829f"
        private val apiKeys = listOf(API_KEY, API_KEY2, API_KEY3, API_KEY4, API_KEY5)
        private var currentApiKeyIndex = 0
    }

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        var  request = chain.request().newBuilder()
            .header("X-API-KEY", apiKeys[currentApiKeyIndex])
            .build()
        var response = chain.proceed(request)
        if (response.code == 402) currentApiKeyIndex+=1

//        if (apiLimitReached(chain)) {
//            currentApiKeyIndex += 1
//            request = request.newBuilder()
//                .header("X-API-KEY", apiKeys[currentApiKeyIndex])
//                .build()
//            response.close()
//            response = chain.proceed(request)
//        }


       return response

    }

}