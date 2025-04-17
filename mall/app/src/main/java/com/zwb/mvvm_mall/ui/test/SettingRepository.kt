package com.zwb.mvvm_mall.ui.test

import androidx.lifecycle.MutableLiveData
import com.zwb.mvvm_mall.base.repository.BaseRepository
import com.zwb.mvvm_mall.base.viewstate.State
import com.zwb.mvvm_mall.bean.BannerResponse
import com.zwb.mvvm_mall.network.dataConvert

class SettingRepository(private val loadState: MutableLiveData<State>): BaseRepository() {

    suspend fun loadBannerCo(): List<BannerResponse> {
        return apiService.loadBannerCo().dataConvert(loadState)
    }


}