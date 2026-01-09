package com.future.mvvm.base.state

import androidx.annotation.StringRes

enum class StateType {
    SUCCESS,
    ERROR,
    EMPTY,
    NETWORK_ERROR,
    TIP
}

data class State(
    var code:StateType,
    var urlKey:String = "",
    var message:String= "",
    @field:StringRes var tip:Int = 0
)