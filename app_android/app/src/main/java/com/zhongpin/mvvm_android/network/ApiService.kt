package com.zhongpin.mvvm_android.network

import com.google.gson.JsonElement
import com.zhongpin.mvvm_android.bean.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/***
 * 数据url：https://mockapi.eolink.com/JiPqtefd9325e3466dc720a8c0ad3364c4f791e227debcd/banner/json
 * */
interface ApiService {


    @GET(USER_INFO)
    @Deprecated("已废弃")
    suspend fun getUserInfoCo(): BaseResponse<List<UserInfoResponse>>

    @GET(USER_INFO)
    suspend fun getUserInfo(): BaseResponse<UserInfoResponse>


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


    @Multipart
    @POST(IDENTIFY_ID_CARD)
    suspend fun identifyIdCard(@Part file: MultipartBody.Part): BaseResponse<IdCardInfoResponse>


    @Multipart
    @POST(UPLOAD_IMAGE)
    suspend fun uploadImage(@Part file: MultipartBody.Part): BaseResponse<String>


    @POST(SUBMIT_USER_INFO_AUTH)
    suspend fun submitUserInfoAuth(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>

    @POST(SUBMIT_ENT_INFO_AUTH)
    suspend fun submitEntInfoAuth(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>

    @POST(EDIT_ENT_INFO_AUTH)
    suspend fun editEntInfoAuth(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>

    @GET(DELETE_ENT_INFO_AUTH)
    suspend fun deleteEntInfoAuth(@Query("id") id: Long): BaseResponse<Boolean>



    @GET(SELECT_ADDRESS_POI_INFO)
    suspend fun getLntLngInfo(@Query("address") address: String): BaseResponse<LatLntResponse>


    @Multipart
    @POST(ENT_INFO_IDENTIFY)
    suspend fun identifyEntCard(@Part file: MultipartBody.Part): BaseResponse<EntInfoResponse>


    @POST(COMPANY_LIST)
    suspend fun getCompanyList(@Body parameters:HashMap<String,Any>): BaseResponse<CompanyListResponse>

    @GET(USER_AUTH_INFO)
    suspend fun getUserAuthInfo(): BaseResponse<UserInfoAuthResponse>

    @GET(ADDRESS_LIST)
    suspend fun getEntReceiveAddressList(@Query("entId") entId: Long): BaseResponse<List<AddressListItemResponse>>

    @POST(ADD_RECEIVE_ADDRESS)
    suspend fun addReceiveAddress(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>


    @POST(UPDATE_RECEIVE_ADDRESS)
    suspend fun updateReceiveAddress(@Body parameters:HashMap<String,Any>): BaseResponse<Boolean>


    @GET(DELETE_RECEIVE_ADDRESS)
    suspend fun deleteReceiveAddress(@Query("id") id: Long): BaseResponse<Boolean>



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
        const val IDENTIFY_ID_CARD ="/mini/identifyIdCard"

        const val UPLOAD_IMAGE  ="/mini/uploadImage"

        const val SUBMIT_USER_INFO_AUTH = "/mini/submitUserInfoAuth"

        //提交企业认证
        const val SUBMIT_ENT_INFO_AUTH = "/mini/submitEntInfoAuth"

        const val EDIT_ENT_INFO_AUTH = "/mini/updateEntInfoAuth"
        const val DELETE_ENT_INFO_AUTH = "/mini/deleteEntInfoAuth"


        //获取经纬度
        const val SELECT_ADDRESS_POI_INFO = "/mini/selectExactInfo"

        const val ENT_INFO_IDENTIFY = "/mini/identify"


        const val COMPANY_LIST = "/mini/selectEntInfoAuthPage"


        const val ADDRESS_LIST = "/mini/selectReceiveAddressList"


        const val ADD_RECEIVE_ADDRESS = "/mini/addReceiveAddress"

        const val UPDATE_RECEIVE_ADDRESS = "/mini/updateReceiveAddress"

        const val DELETE_RECEIVE_ADDRESS = "/mini/deleteReceiveAddress"



        const val USER_AUTH_INFO = "/mini/selectUserInfoAuth"

    }

}