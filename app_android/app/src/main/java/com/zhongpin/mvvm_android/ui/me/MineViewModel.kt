package com.zhongpin.mvvm_android.ui.me

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.network.initiateRequest

class MineViewModel  : BaseViewModel<MineRepository>() {

    var mBannerData: MutableLiveData<List<UserInfoResponse>> = MutableLiveData()
    fun loadBannerCo() {
        initiateRequest({
            mBannerData.value = mRepository.loadBannerCo()
        }, loadState)
    }

}