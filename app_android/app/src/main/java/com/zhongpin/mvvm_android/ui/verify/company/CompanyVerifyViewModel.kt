package com.zhongpin.mvvm_android.ui.verify.company

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.network.BaseResponse
import com.zhongpin.mvvm_android.network.initiateRequest

class CompanyVerifyViewModel  : BaseViewModel<CompanyVerifyRepository>() {

    var mBannerData: MutableLiveData<List<UserInfoResponse>> = MutableLiveData()

    fun loadBannerCo() {
        initiateRequest({
            mBannerData.value = mRepository.loadBannerCo()
        }, loadState)
    }


    fun sendVerifyCode(mobile:String) : MutableLiveData<BaseResponse<Boolean>> {
        val mLoginData: MutableLiveData<BaseResponse<Boolean>> = MutableLiveData()
        initiateRequest({
            mLoginData.value = mRepository.sendVerifyCode(mobile);
        }, loadState)
        return mLoginData
    }

}