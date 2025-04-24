package com.zhongpin.lib_base.utils.callback

import com.kingja.loadsir.callback.Callback
import com.zhongpin.app.R

class EmptyCallback :Callback(){
    override fun onCreateView(): Int = R.layout.layout_empty
}