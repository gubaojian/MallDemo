package com.zhongpin.lib_base.utils.callback

import com.kingja.loadsir.callback.Callback
import com.zhongpin.app.R

class ErrorCallback :Callback(){
    override fun onCreateView(): Int = R.layout.layout_error
}