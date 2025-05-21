package com.zhongpin.mvvm_android.network
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.zhongpin.lib_base.utils.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.zhongpin.app.BuildConfig
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.SPreference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.concurrent.TimeUnit


/**
 * Created with Android Studio.
 * Description:
 * @date: 2020/02/24
 * Time: 16:56
 */

class RetrofitFactory private constructor() {
    private val retrofit : Retrofit

    fun <T> create(clazz: Class<T>) : T {
        return retrofit.create(clazz)
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(initOkHttpClient())
            .build()
    }

    companion object {
        val instance by lazy {
            RetrofitFactory()
        }
    }

    private fun initOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder();
        builder.readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(initLoggingIntercept())
        }
        builder.addInterceptor(initCookieIntercept())
            .addInterceptor(initLoginIntercept())
            .addInterceptor(initCommonInterceptor())
        return builder.build()
    }
    private fun initLoggingIntercept(): Interceptor {
        return HttpLoggingInterceptor { message ->
            try {
                if (message.length < 1024*4) {
                    LogUtils.e("OKHttp-----", message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtils.e("OKHttp-----", message)
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun initCookieIntercept(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            response
        }
    }

    private fun initLoginIntercept(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            val token = SPUtils.getInstance().getString(Constant.TOKEN_KEY,"")
            if(token != null && token.isNotEmpty()){
                builder.addHeader("token", token)
                LogUtils.e("OKHttp-----request token ", token)
            }else {
                LogUtils.e("OKHttp-----request token ", "token is empty")
            }
            val response = chain.proceed(builder.build())
            response
        }
    }

    private fun initCommonInterceptor(): Interceptor {
        return Interceptor { chain ->
            val deviceNameStr = DeviceUtils.getManufacturer().plus("_")
                .plus(DeviceUtils.getModel())
            val request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "UTF-8")
                .addHeader("device-type", "android")
                .addHeader("app-version", AppUtils.getAppVersionName())
                .addHeader("device-id", DeviceUtils.getUniqueDeviceId())
                .addHeader("device-os-version", DeviceUtils.getSDKVersionName().toString())
                .addHeader("device-name",  URLEncoder.encode(deviceNameStr, "UTF-8"))
                .build()
            chain.proceed(request)
        }
    }

    private fun parseCookie(it: List<String>): String {
        if(it.isEmpty()){
            return ""
        }

        val stringBuilder = StringBuilder()

        it.forEach { cookie ->
            stringBuilder.append(cookie).append(";")
        }

        if(stringBuilder.isEmpty()){
            return ""
        }
        //末尾的";"去掉
        return stringBuilder.deleteCharAt(stringBuilder.length - 1).toString()
    }

    private fun saveCookie(domain: String?, parseCookie: String) {
        domain?.let {
            var resutl :String by SPreference("cookie", parseCookie)
            resutl = parseCookie
        }
    }
}