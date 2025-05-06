package com.zhongpin.mvvm_android.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.base.repository.BaseRepository
import com.zhongpin.mvvm_android.base.viewstate.State
import com.zhongpin.mvvm_android.base.viewstate.StateType
import com.zhongpin.mvvm_android.base.vm.BaseViewModel
import com.zhongpin.mvvm_android.bean.TokenExpiredEvent
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
    if (data == null) {
        loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
        return data
    }
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


fun <T> BaseResponse<T>.showLoadingState(
    loadState: MutableLiveData<State>,
    urlKey:String = ""
): BaseResponse<T> {
    if (data == null) {
        loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
        return this
    }
    return when (success) {
        true -> {
            if (data is List<*>) {
                if ((data as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY,urlKey))
                }
            }
            loadState.postValue(State(StateType.SUCCESS,urlKey))
            this
        }
        else -> {
            loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
            this
        }
    }
}

fun <T> BaseResponse<T>.showLoadingStateRequireLogin(
    loadState: MutableLiveData<State>,
    urlKey:String = ""
): BaseResponse<T> {
    if (code == 403) {
        EventBusUtils.postEvent(TokenExpiredEvent(true))
    }
    if (data == null) {
        loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
        return this
    }
    return when (success) {
        true -> {
            if (data is List<*>) {
                if ((data as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY,urlKey))
                }
            }
            loadState.postValue(State(StateType.SUCCESS,urlKey))
            this
        }
        else -> {
            loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
            this
        }
    }
}


fun <T> BaseResponse<T>.requireLogin(): BaseResponse<T> {
    if (code == 403) { //过滤登录过期错误码
        EventBusUtils.postEvent(TokenExpiredEvent(true))
    }
    return this;
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
