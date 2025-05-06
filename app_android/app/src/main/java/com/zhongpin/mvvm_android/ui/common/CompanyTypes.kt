package com.zhongpin.mvvm_android.ui.common

object CompanyTypes {
    val companyTypes = arrayOf("造纸厂", "纸板厂", "纸箱厂", "耗材厂商", "纸箱使用单位")

    fun getCompanyType(entType:Int):String {
        if(entType < companyTypes.size
            && entType >= 0) {
            return companyTypes.get(entType)
        }
        return  ""
    }
}