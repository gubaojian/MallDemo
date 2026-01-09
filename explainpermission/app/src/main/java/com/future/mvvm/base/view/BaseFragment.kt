package com.future.mvvm.base.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.future.mvvm.base.annotation.EventBusRegister
import java.util.zip.Inflater
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

abstract class BaseFragment : Fragment() {

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mContext: Context;
    private var mFragmentViewModelProvider: ViewModelProvider?= null
    private var mActivityViewModelProvider: ViewModelProvider?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
        mContext = requireContext()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //shared app view model.
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setContentView(inflater, container, savedInstanceState)
    }


    abstract fun setContentView(inflater: LayoutInflater, container: ViewGroup?, saveInstace: Bundle?): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventBusTime = measureTimeMillis {
            if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {

            }
        }
    }


    protected fun getFragmentViewModelProvider(frament: Fragment): ViewModelProvider {
        if (mFragmentViewModelProvider == null) {
            mFragmentViewModelProvider = ViewModelProvider(frament)
        }
        return mFragmentViewModelProvider as ViewModelProvider
    }

    protected fun getActivityViewModeLProvider(activity: AppCompatActivity): ViewModelProvider {
        if (mActivityViewModelProvider == null) {
            mActivityViewModelProvider = ViewModelProvider(activity)
        }
        return mActivityViewModelProvider as ViewModelProvider
    }


    override fun onDestroyView() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {

        }
        super.onDestroyView()
    }




}