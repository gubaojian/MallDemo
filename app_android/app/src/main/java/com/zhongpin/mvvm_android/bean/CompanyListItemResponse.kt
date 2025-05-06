package com.zhongpin.mvvm_android.bean

/**
 * https://space-64stfp.w.eolink.com/home/api-studio/inside/p346wIBec8b27b4efe594eed716ffa6a4764f833feb25fd/api/3148316/detail/55762714?spaceKey=space-64stfp
 * */
data class CompanyListItemResponse(var id : Long,
                                   var userId : Long,
                                   var name:String,
                                   var legal:String,
                                   var identify:String,
                                   var license:String,
                                   var address:String,
                                   var unite:String,
                                   var mobile:String,
                                   var bankAccount:String?,
                                   var bankName:String?,
                                   var describe:String?,
                                   var handStatus:Int,
                                   var status:Int):java.io.Serializable {
    companion object {
        private const val serialVersionUID = 20180617104400L
    }
}