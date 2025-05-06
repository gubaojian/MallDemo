package com.zhongpin.mvvm_android.bean

/**
 * https://space-64stfp.w.eolink.com/home/api-studio/inside/p346wIBec8b27b4efe594eed716ffa6a4764f833feb25fd/api/3181096/detail/55805752?spaceKey=space-64stfp
 * */
data class AddressListItemResponse(var id : Long,
                                   var entId : Long,
                                   var province:String?,
                                   var city:String?,
                                   var region:String?,
                                   var address:String?,
                                   var abbr:String?,
                                   var longitude:String?,
                                   var latitude:String?,
                                   var name:String?,
                                   var mobile:String?):java.io.Serializable {
    companion object {
        private const val serialVersionUID = 20250440102700L
    }
}