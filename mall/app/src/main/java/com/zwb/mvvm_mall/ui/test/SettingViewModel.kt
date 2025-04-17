package com.zwb.mvvm_mall.ui.test

import androidx.lifecycle.MutableLiveData
import com.zwb.mvvm_mall.base.vm.BaseViewModel
import com.zwb.mvvm_mall.bean.BannerResponse
import com.zwb.mvvm_mall.network.initiateRequest

class SettingViewModel  : BaseViewModel<SettingRepository>() {

    var mBannerData: MutableLiveData<List<BannerResponse>> = MutableLiveData()
    fun loadBannerCo() {
        initiateRequest({
            mBannerData.value = mRepository.loadBannerCo()
        }, loadState)
    }

}