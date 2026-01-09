package com.future.mvvm.base.vm

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.future.mvvm.base.common.CommonUtils
import com.future.mvvm.base.repository.BaseRepository
import com.future.mvvm.base.state.State

open class BaseViewModel<R: BaseRepository> : ViewModel() {
    val loadState by lazy {
        MutableLiveData<State>()
    }

    val dialogState by lazy {
        MutableLiveData<State>();
    }

    val mRespostory by lazy {
        CommonUtils.getClass<R>(this).getDeclaredConstructor().newInstance()
    }

    override fun onCleared() {
        super.onCleared()
    }

}