package com.zhongpin.mvvm_android.ui.me

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.repository.BaseRepository
import com.zhongpin.mvvm_android.base.viewstate.State
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.network.BaseResponse
import com.zhongpin.mvvm_android.network.dataConvert
import com.zhongpin.mvvm_android.network.requireLogin
import com.zhongpin.mvvm_android.network.showLoadingState

class MineRepository(private val loadState: MutableLiveData<State>): BaseRepository() {

    suspend fun getUserInfo(): BaseResponse<UserInfoResponse> {
        return apiService.getUserInfo().requireLogin().showLoadingState(loadState)
    }


}