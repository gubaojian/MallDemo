package com.zhongpin.mvvm_android.common.login

import com.blankj.utilcode.util.SPUtils
import com.zhongpin.mvvm_android.common.utils.Constant

object LoginUtils {

    fun hasLogin():Boolean {
        val token = SPUtils.getInstance().getString(Constant.TOKEN_KEY,"")
        return token.isNotEmpty()
    }
}