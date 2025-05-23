package com.zwb.mvvm_mall.network

import com.zwb.mvvm_mall.bean.*
import com.zwb.mvvm_mall.common.utils.Constant.PROJECT
import retrofit2.http.GET

/***
 * 数据url：https://mockapi.eolink.com/JiPqtefd9325e3466dc720a8c0ad3364c4f791e227debcd/banner/json
 * */
interface ApiService {


    @GET("$PROJECT/banner/json")
    suspend fun loadBannerCo(): BaseResponse<List<BannerResponse>>
/**
    @GET("$PROJECT/home/getGoodsClass")
    suspend fun getGoodsClassCo() : BaseResponse<List<ClassifyEntity>>

    @GET("$PROJECT/home/getSeckillGoodsList")
    suspend fun getSeckillGoodsList() : BaseResponse<List<GoodsEntity>>



    @GET("$PROJECT/home/getBoutiqueGoodsList")
    suspend fun getBoutiqueGoodsList() : BaseResponse<List<GoodsEntity>>

    @GET("$PROJECT/home/getCartList")
    suspend fun getCartList() : BaseResponse<List<CartGoodsEntity>>

    @GET("$PROJECT/home/getSeckillGoodsList")
    suspend fun getCartLikeGoods() : BaseResponse<List<CartLikeGoodsEntity>>

    @GET("$PROJECT/search/getSearchTags")
    suspend fun getSearchTags() : BaseResponse<List<SearchTagEntity>>

    @GET("$PROJECT/search/getSearchHotTags")
    suspend fun getSearchHotTags() : BaseResponse<List<SearchHotEntity>>

    @GET("$PROJECT/comment/getCommentList")
    suspend fun getCommentList() : BaseResponse<List<CommentEntity>>

    @GET("$PROJECT/goods/getFilterAttrs")
    suspend fun getFilterAttrs() : BaseResponse<List<GoodsAttrFilterEntity>>

    */

    companion object {
        const val HOST_URL = "https://dev.zwb.com"
        const val Login_Url = HOST_URL +"/mbff/login"

        //send code
        const val VALID_CODE_URL = "${HOST_URL}/mbff/validateSmsByForgot"

        const val CHANGE_PASSWORD_URL = HOST_URL +"/mbff/passwordByForgot"

        const val User_Info_Url = HOST_URL +"/mbff/social/user/info"

        const val ALITOKEN_URL = HOST_URL + "/mbff/getAliFusionAuthToken"
        const val FusionLogin_URL = HOST_URL +"/mbff/verifyAliFusionAuthToken"
    }

}