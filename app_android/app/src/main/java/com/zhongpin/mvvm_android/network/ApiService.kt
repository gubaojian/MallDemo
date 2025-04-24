package com.zhongpin.mvvm_android.network

import com.zhongpin.mvvm_android.bean.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/***
 * 数据url：https://mockapi.eolink.com/JiPqtefd9325e3466dc720a8c0ad3364c4f791e227debcd/banner/json
 * */
interface ApiService {


    @GET(USER_INFO)
    suspend fun getUserInfoCo(): BaseResponse<List<UserInfoResponse>>

    @POST(LOGIN)
    suspend fun loginCo(@Body parameters:HashMap<String,Any>): BaseResponse<LoginResponse>

    @POST(REGISTER)
    suspend fun registerCo(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>

    /**
     *      register(0, "mini:register", "账号注册"),
     *
     *     updatePassword(1, "mini:updatePassword", "登录成功密码修改"),
     *
     *     resetPassword(2, "mini:resetPassword", "未登录密码修改"),
     *
     *     loginOften(3,"mini:loginOften","登录频繁");
     * */
    @POST(SEND_VERIFY_CODE)
    suspend fun sendVerifyCo(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>

    @POST(RESET_PASSWORD)
    suspend fun resetPassword(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>


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
        const val LOGIN = "/mini/login"
        const val REGISTER = "/mini/register"
        const val SEND_VERIFY_CODE = "/mini/sendCode"
        const val RESET_PASSWORD = "/mini/resetPassword"
        const val USER_INFO = "/mini/selectUserInfo"


        //send code
        const val VALID_CODE_URL = "${HOST_URL}/mbff/validateSmsByForgot"

        const val CHANGE_PASSWORD_URL = HOST_URL +"/mbff/passwordByForgot"

        const val User_Info_Url = HOST_URL +"/mbff/social/user/info"

        const val ALITOKEN_URL = HOST_URL + "/mbff/getAliFusionAuthToken"
        const val FusionLogin_URL = HOST_URL +"/mbff/verifyAliFusionAuthToken"
    }

}