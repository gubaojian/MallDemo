package com.zhongpin.mvvm_android.network

/**
 * Created with Android Studio.
 * Description: 返回数据基类
 * @date: 2020/02/24
 * Time: 16:04
 */

open class BaseResponse<T>(var data: T?,
                           var success: Boolean = false,
                           var code : Int = -1,
                           var msg: String = "")