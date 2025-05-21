package com.zhongpin.mvvm_android.ui.me

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.biz.utils.UserInfoUtil
import com.zhongpin.mvvm_android.network.BaseResponse
import com.zhongpin.mvvm_android.network.initiateRequest

class MineViewModel  : BaseViewModel<MineRepository>() {

    var mUserInfoData: MutableLiveData<BaseResponse<UserInfoResponse>> = MutableLiveData()

    fun getUserInfo() {
        initiateRequest({
            val response = mRepository.getUserInfo();
            mUserInfoData.value = response;
            if (response.success) {
                UserInfoUtil.userInfo = response.data
            }
        }, loadState)
    }

}