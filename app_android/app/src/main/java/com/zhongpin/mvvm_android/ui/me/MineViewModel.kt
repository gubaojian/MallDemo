package com.zhongpin.mvvm_android.ui.me

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.biz.utils.UserInfoUtil
import com.zhongpin.mvvm_android.network.initiateRequest

class MineViewModel  : BaseViewModel<MineRepository>() {

    var mUserInfoData: MutableLiveData<UserInfoResponse> = MutableLiveData()

    fun getUserInfoCo() {
        initiateRequest({
            mUserInfoData.value = mRepository.getUserInfo()
            UserInfoUtil.userInfo = mUserInfoData.value
        }, loadState)
    }

}