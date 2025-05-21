package com.zhongpin.mvvm_android.ui.mine.company.dizhi

import androidx.lifecycle.MutableLiveData
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.AddressListItemResponse
import com.zhongpin.mvvm_android.network.BaseResponse
import com.zhongpin.mvvm_android.network.initiateRequest

class AddressListViewModel  : BaseViewModel<AddressListRepository>() {

    val mData: MutableLiveData<BaseResponse<List<AddressListItemResponse>>> = MutableLiveData()

    fun getEntReceiveAddressList(entId:Long) : MutableLiveData<BaseResponse<List<AddressListItemResponse>>> {
        initiateRequest({
            mData.value = mRepository.getEntReceiveAddressList(entId)
        }, loadState)
        return mData;
    }



    fun deleteReceiveAddress(id: Long) : MutableLiveData<BaseResponse<Boolean>> {
        val mLiveData: MutableLiveData<BaseResponse<Boolean>> = MutableLiveData()
        initiateRequest({
            mLiveData.value = mRepository.deleteReceiveAddress(id);
        }, loadState)
        return mLiveData
    }



}