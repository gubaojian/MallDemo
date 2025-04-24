package com.zhongpin.mvvm_android.common.callback

import com.kingja.loadsir.callback.Callback
import com.zhongpin.app.R

class EmptyCallBack :Callback(){
    override fun onCreateView(): Int = R.layout.layout_empty
}