package com.zhongpin.mvvm_android.ui.me

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.repository.BaseRepository
import com.zhongpin.mvvm_android.base.viewstate.State
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.network.dataConvert

class MineRepository(private val loadState: MutableLiveData<State>): BaseRepository() {

    suspend fun getUserInfo(): UserInfoResponse? {
        return apiService.getUserInfo().dataConvert(loadState)
    }


}