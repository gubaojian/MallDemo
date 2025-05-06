package com.zhongpin.mvvm_android.ui.mine.company.shouhuo

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.repository.BaseRepository
import com.zhongpin.mvvm_android.base.viewstate.State
import com.zhongpin.mvvm_android.bean.AddressListItemResponse
import com.zhongpin.mvvm_android.network.BaseResponse
import com.zhongpin.mvvm_android.network.showLoadingState


class AddressListRepository(private val loadState: MutableLiveData<State>): BaseRepository() {


    suspend fun getEntReceiveAddressList(entId:Long): BaseResponse<List<AddressListItemResponse>>{
        return apiService.getEntReceiveAddressList(entId).showLoadingState(loadState)
    }


    suspend fun deleteReceiveAddress(id: Long): BaseResponse<Boolean> {
        return apiService.deleteReceiveAddress(id)
    }


}