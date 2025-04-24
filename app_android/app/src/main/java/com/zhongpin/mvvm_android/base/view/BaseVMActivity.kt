package com.zhongpin.mvvm_android.base.view

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.zhongpin.mvvm_android.base.viewstate.State
import com.zhongpin.mvvm_android.base.viewstate.StateType
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.common.callback.EmptyCallBack
import com.zhongpin.mvvm_android.common.callback.ErrorCallBack
import com.zhongpin.mvvm_android.common.callback.LoadingCallBack
import com.zhongpin.mvvm_android.common.callback.PlaceHolderCallBack
import com.zhongpin.mvvm_android.common.utils.CommonUtils
import com.zhongpin.mvvm_android.common.utils.Constant

abstract class BaseVMActivity<VM : BaseViewModel<*>> : BaseActivity(),BaseView {

    protected lateinit var mViewModel: VM
    private var loadService: LoadService<*>? = null
    private var loadKeys:MutableList<String> = ArrayList()

    abstract fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?):View;


    override fun setContentView(){
        val inflater = LayoutInflater.from(this)
        val rootView = createContentViewByBinding(inflater, null)
        setContentView(rootView)
        mViewModel = getActivityViewModelProvider(this).get(CommonUtils.getClass(this))
    }



    override fun initView() {
        mViewModel.loadState.observe(this,observer)
        initDataObserver()

    }

    fun registerPlaceHolderLoad(view: View,placeHolderLayoutID:Int){
        val loadSir = LoadSir.Builder()
            .addCallback(PlaceHolderCallBack(placeHolderLayoutID))
            .addCallback(EmptyCallBack())
            .addCallback(ErrorCallBack())
            .setDefaultCallback(PlaceHolderCallBack::class.java)
            .build()
        loadService =  loadSir.register(view) {  reLoad() }
    }

    override fun registerDefaultLoad(view: View, key:String){
        if(!TextUtils.isEmpty(key))loadKeys.add(key)
        loadService =  LoadSir.getDefault().register(view) {  reLoad() }
    }

    override fun initData() {
        if(loadKeys.size>0)
            showLoading(loadKeys[0])
    }

    open fun initDataObserver() {}

    fun showPlaceHolder() {
        loadService?.showCallback(PlaceHolderCallBack::class.java)
    }

    override fun showLoading(key:String) {
        if(loadKeys.contains(key) || Constant.COMMON_KEY == key){
            loadService?.showCallback(LoadingCallBack::class.java)
        }
    }

    override fun showSuccess(key:String) {
        if(loadKeys.contains(key) || Constant.COMMON_KEY == key){
            loadService?.showCallback(SuccessCallback::class.java)
        }
    }

    override fun showEmpty(key:String) {
        if(loadKeys.contains(key) || Constant.COMMON_KEY == key){
            loadService?.showCallback(EmptyCallBack::class.java)
        }
    }

    override fun showError(msg: String,key:String) {
        if (!TextUtils.isEmpty(msg)){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        if(loadKeys.contains(key) || Constant.COMMON_KEY == key){
            loadService?.showCallback(ErrorCallBack::class.java)
        }
    }

    private val observer by lazy {
        Observer<State> {
            it?.let {
                when {
                    it.code == StateType.SUCCESS -> showSuccess(it.urlKey)
                    it.code == StateType.ERROR -> showError("网络异常",it.urlKey)
                    it.code == StateType.NETWORK_ERROR -> showError("网络异常",it.urlKey)
                    it.code == StateType.EMPTY -> showEmpty(it.urlKey)
                }
            }
        }
    }
}