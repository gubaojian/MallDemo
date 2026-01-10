package com.future.mvvm.base.view

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.future.mvvm.base.common.CommonConstant
import com.future.mvvm.base.common.CommonUtils
import com.future.mvvm.base.state.State
import com.future.mvvm.base.state.StateType
import com.future.mvvm.base.vm.BaseViewModel
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir


abstract class BaseVMActivity<VM: BaseViewModel<*>> : BaseActivity() {

    protected lateinit var mViewModel: VM

    private var loadKeys: MutableList<String> = ArrayList()
    private var loadService:LoadService<*>? = null

    override fun setContentView() {
        val inflater = LayoutInflater.from(this)
        val rootView = createContentViewByBinding(inflater)
        setContentView(rootView)
        mViewModel = getActivityViewModelProvider(this).get(CommonUtils.getClass(this))
    }

    abstract fun createContentViewByBinding(inflater: LayoutInflater): View;


    override fun initView() {
        super.initView()
        mViewModel.loadState.observe(this) {

        }
        initDataObserver()
    }

    fun initDataObserver() {

    }



    override fun initData() {
        super.initData()
    }

    fun showSuccess(key:String) {

    }

    fun showEmpty(key:String) {
        
    }

    fun showError(key:String, msg:String) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        if (loadKeys.contains(key)
            || CommonConstant.COMMOM_KEY == key
            || TextUtils.isEmpty(key)
            || loadKeys.size == 1) {

        }
    }


    private val observer by lazy {
        Observer<State> {
            it?.let {
                var errorMsg = it.message;
                if (TextUtils.isEmpty(errorMsg)) {
                    errorMsg = "网络异常"
                }
                when (it.code){
                     StateType.SUCCESS -> showSuccess(it.urlKey)
                     StateType.ERROR -> showError(it.urlKey, errorMsg)
                    StateType.NETWORK_ERROR -> showError(it.urlKey, errorMsg)
                    StateType.EMPTY -> showEmpty(it.urlKey)
                    else -> {}
                }
            }
        }
    }

}