package com.zhongpin.mvvm_android

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import com.zhongpin.lib_base.app.ActivityLifecycleCallbacksImpl
import com.zhongpin.lib_base.app.LoadModuleProxy
import com.zhongpin.mvvm_android.app.AppLifecycleListener
import com.zhongpin.mvvm_android.common.callback.EmptyCallBack
import com.zhongpin.mvvm_android.common.callback.ErrorCallBack
import com.zhongpin.mvvm_android.common.callback.LoadingCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MyApplication: Application(), ViewModelStoreOwner {


    lateinit var mAppViewModelStore : ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }

    private val mLoadModuleProxy by lazy(mode = LazyThreadSafetyMode.NONE) { LoadModuleProxy() }


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE: MyApplication

        // 全局Context
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var application: MyApplication
    }

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MyApplication.context = base
        MyApplication.application = this
        mLoadModuleProxy.onAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        mAppViewModelStore = ViewModelStore()

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleListener())
        // 全局监听 Activity 生命周期
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())

        //config loadSir
        configLoadSir()

        configMMKV()
        // 策略初始化第三方依赖
        initDepends()
    }

    /**
     * 初始化第三方依赖
     */
    private fun initDepends() {
        // 开启一个 Default Coroutine 进行初始化不会立即使用的第三方
        mCoroutineScope.launch(Dispatchers.Default) {
            mLoadModuleProxy.initByBackstage()
        }
        // 前台初始化
        val allTimeMillis = measureTimeMillis {
            val depends = mLoadModuleProxy.initByFrontDesk()
            var dependInfo: String
            depends.forEach {
                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
                Log.d("BaseApplication", "initDepends: $dependInfo : $dependTimeMillis ms")
            }
        }
        Log.d("BaseApplication", "初始化完成 $allTimeMillis ms")
    }


    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }


    fun getAppViewModelProvider(activity: Activity): ViewModelProvider {
        return ViewModelProvider(
            activity.applicationContext as MyApplication,
            (activity.applicationContext as MyApplication).getAppFactory(activity)
        )
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory as ViewModelProvider.Factory
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException("Your activity/fragment is not yet attached to " + "Application. You can't request ViewModel before onCreate call.")
    }

    private fun configLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(LoadingCallBack())
            .addCallback(ErrorCallBack())
            .addCallback(EmptyCallBack())
            .commit()
    }

    private fun configMMKV() {
        MMKV.initialize(this);
    }
}