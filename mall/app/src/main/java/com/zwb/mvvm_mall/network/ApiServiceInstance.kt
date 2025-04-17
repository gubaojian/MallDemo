package com.zwb.mvvm_mall.network

object ApiServiceInstance {
    val apiService: ApiService by lazy {
        RetrofitFactory.instance.create(ApiService::class.java)
    }
}