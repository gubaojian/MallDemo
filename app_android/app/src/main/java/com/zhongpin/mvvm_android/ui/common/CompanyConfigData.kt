package com.zhongpin.mvvm_android.ui.common

object CompanyConfigData {
    val companyTypes = arrayOf("造纸厂", "纸板厂", "纸箱厂", "耗材厂商", "纸箱使用单位")
    val feedbackTypes = arrayOf("让步接收", "申请折扣", "申请退货", "申请退货+补偿", "申请挑货")

    fun getCompanyType(entType:Int):String {
        if(entType < companyTypes.size
            && entType >= 0) {
            return companyTypes.get(entType)
        }
        return  ""
    }
}