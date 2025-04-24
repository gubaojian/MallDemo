package com.zhongpin.mvvm_android.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.base.repository.BaseRepository
import com.zhongpin.mvvm_android.base.viewstate.State
import com.zhongpin.mvvm_android.base.viewstate.StateType
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Created with Android Studio.
 * Description:数据解析扩展函数
 * @CreateDate: 2020/4/19 17:35
 */

fun <T> BaseResponse<T>.dataConvert(
    loadState: MutableLiveData<State>,
    urlKey:String = ""
): T? {
    return when (success) {
        true -> {
            if (data is List<*>) {
                if ((data as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY,urlKey))
                }
            }
            loadState.postValue(State(StateType.SUCCESS,urlKey))
            data
        }
        else -> {
            loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
            data
        }
    }
}


fun <T : BaseRepository> BaseViewModel<T>.initiateRequest(
    block: suspend () -> Unit,
    loadState: MutableLiveData<State>
) {
    viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
        }.onFailure {
            NetExceptionHandle.handleException(it, loadState)
        }
    }
}
