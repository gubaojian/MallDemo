package com.future.mvvm.base.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.ActionMode
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.future.mvvm.base.annotation.EventBusRegister

abstract class BaseActivity : AppCompatActivity() {

    private var mActivityViewStoreProvider: ViewModelProvider? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView()
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {
            //
        }
        initView();
        initData();
    }

    open fun initView() {}
    open fun initData() {}
    open fun reload() = initView()
    abstract fun setContentView();


    protected fun getActivityViewModelProvider(activity: AppCompatActivity): ViewModelProvider {
        if (mActivityViewStoreProvider == null) {
            mActivityViewStoreProvider = ViewModelProvider(activity)
        }
        return mActivityViewStoreProvider as ViewModelProvider;
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {

        }
        super.onDestroy()
    }
}