package com.zhongpin.mvvm_android.biz.utils

import com.zhongpin.mvvm_android.bean.UserInfoResponse

object UserInfoUtil {
    var userInfo: UserInfoResponse? = null

    fun maskPhone(phone:String?):String {
        if (phone == null || phone.length < 11) {
            return phone ?: ""
        }
        val sb = StringBuilder(phone);
        sb.replace(3, 7, "****")
        return sb.toString();
    }
}