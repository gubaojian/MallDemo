package com.zhongpin.mvvm_android.ui.home

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.network.initiateRequest

class HomeFragmentViewModel  : BaseViewModel<HomeRepository>() {

    var mBannerData: MutableLiveData<List<UserInfoResponse>?> = MutableLiveData()
    fun loadBannerCo() {
        initiateRequest({
            mBannerData.value = mRepository.loadBannerCo()
        }, loadState)
    }

}