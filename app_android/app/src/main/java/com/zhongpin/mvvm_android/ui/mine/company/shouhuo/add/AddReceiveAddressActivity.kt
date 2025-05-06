package com.zhongpin.mvvm_android.ui.mine.company.shouhuo.add

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.gzuliyujiang.wheelpicker.AddressPicker
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.zhongpin.app.BuildConfig
import com.zhongpin.app.databinding.ActivityAddReceiveAddressBinding
import com.zhongpin.app.databinding.ActivityCompanyVerifyBinding
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.lib_base.utils.LogUtils
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.bean.AddressInfoChangeEvent
import com.zhongpin.mvvm_android.bean.CompanyInfoChangeEvent
import com.zhongpin.mvvm_android.bean.EntInfoResponse
import com.zhongpin.mvvm_android.bean.LatLntResponse
import com.zhongpin.mvvm_android.photo.selector.GlideEngine
import com.zhongpin.mvvm_android.ui.utils.AreaUtil
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


/**
 * 添加企业认证
 * */
class AddReceiveAddressActivity : BaseVMActivity<AddReceiveAddressViewModel>(), OnAddressPickedListener {


    private lateinit var mBinding: ActivityAddReceiveAddressBinding;
    private lateinit var mLoadingDialog: LoadingDialog

    private var yingYeZhiZhaoPath:String? = null;
    private var yingYeZhiZhaoUrl:String? = null;
    private var latLntResponse: LatLntResponse? = null

    private var entId:Long = -1;


    override fun onCreate(savedInstanceState: Bundle?) {
        //android:windowSoftInputMode="adjustResize"
        // 键盘view整体上移，暂时不用沉浸式导航栏
        //StatusBarUtil.immersive(this)
        if (intent != null) {
            entId = intent.getLongExtra("entId", -1)
        }
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityAddReceiveAddressBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        super.initView()
        //StatusBarUtil.setMargin(this, mBinding.content)
        mLoadingDialog = LoadingDialog(this, false)
        mBinding.ivBack.setOnClickListener { finish() }



        mBinding.chooseShouhuoAreaContainer.setOnClickListener {
            val picker = AddressPicker(this)
            picker.setAddressMode(AddressMode.PROVINCE_CITY_COUNTY)
            picker.setDefaultValue("", "", "")
            picker.setOnAddressPickedListener(this)
            picker.wheelLayout.setOnLinkageSelectedListener { first, second, third ->
                picker.titleView.text = AreaUtil.toArea(
                    picker.firstWheelView.formatItem(first),
                    picker.secondWheelView.formatItem(second),
                    picker.thirdWheelView.formatItem(third)
                )
            }
            picker.show()
        }

        mBinding.autoChooseJingweidu.setOnClickListener {
            if (TextUtils.isEmpty(getAreaText())) {
                Toast.makeText(applicationContext,"请选择省市区", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val address = mBinding.chooseShouhuoAreaText.text.trim().toString() +  mBinding.editShouHuoDetail.text.trim().toString()
            mViewModel.getLntLngInfo(address).observe(this@AddReceiveAddressActivity) {
                if (it.success ) {
                    latLntResponse = it.data
                    mBinding.chooseJingweiduEditText.setText( "" + (it.data?.longitude  ?: "") +  "/" + (it.data?.latitude ?: ""))

                    val lntlats = mBinding.chooseJingweiduEditText.text.trim().toString().split("/")
                    LogUtils.d("CompanyVerifyActivity ", "CompanyVerifyActivity lntlats " + lntlats)
                }
            }
        }

        mBinding.btnSubmit.setOnClickListener {
            checkAndSubmit()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }




    fun checkAndSubmit(){
        submitFormInfo()
    }

    /**
     * show 加载中
     */
    fun showLoadingDialog() {
        mLoadingDialog.showDialog(this, false)
    }

    /**
     * dismiss loading dialog
     */
    fun dismissLoadingDialog() {
        mLoadingDialog.dismissDialog()
    }


    //https://space-64stfp.w.eolink.com/home/api-studio/inside/p346wIBec8b27b4efe594eed716ffa6a4764f833feb25fd/api/3181096/detail/55805747?spaceKey=space-64stfp
    fun submitFormInfo(){
        val parameter:HashMap<String,Any> = hashMapOf()
        parameter["entId"] = entId

        val receiveAddress:HashMap<String,Any?> = hashMapOf()
        receiveAddress["name"] = mBinding.editShouHuoName.text.trim().toString()
        receiveAddress["mobile"] = mBinding.editShouHuoShouJi.text.trim().toString()
        receiveAddress["address"] = mBinding.editShouHuoDetail.text.trim().toString()
        receiveAddress["abbr"] = mBinding.editShouHuoShort.text.trim().toString()

        if (mBinding.chooseJingweiduEditText.text.isNotEmpty()) {
            val lntlats = mBinding.chooseJingweiduEditText.text.trim().toString().split("/")
            if(lntlats.size == 2) {
                receiveAddress["longitude"] =  lntlats[0]
                receiveAddress["latitude"] = lntlats[1]
            } else {
                dismissLoadingDialog()
                Toast.makeText(applicationContext, "经度/纬度，格式度不合法", Toast.LENGTH_LONG).show()
                return
            }
        }
        receiveAddress["province"] = mProvince?.name
        receiveAddress["city"] = mCity?.name
        receiveAddress["region"] = mCounty?.name
        parameter["receiveAddressList"] = arrayOf(receiveAddress);
        showLoadingDialog()
        mViewModel.addReceiveAddress(parameter).observe(this) {
            dismissLoadingDialog()
            if (it.success) {
                EventBusUtils.postEvent(AddressInfoChangeEvent(true))
                showTipDialog()
            } else {
                Toast.makeText(applicationContext,"提交失败 " + it.msg, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showTipDialog() {
        val builder = AlertDialog.Builder(this@AddReceiveAddressActivity)
        builder.setTitle("信息提交成功")
        builder.setMessage("您填写的收货地址信息已提交。")
        builder.setPositiveButton("我知道了", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                finish()
            }

        })
        builder.show()
    }

    override fun onAddressPicked(
        province: ProvinceEntity?,
        city: CityEntity?,
        county: CountyEntity?
    ) {
        if (province == null || city == null || county == null) {
            return;
        }

        //Toast.makeText(this, province.toString() + " " + city + " " + county, Toast.LENGTH_SHORT).show()

        val address = AreaUtil.toArea(
            province.name,
            city.name,
            county.name
        )
        mProvince = province;
        mCity = city
        mCounty = county
        mBinding.chooseShouhuoAreaText.text = address
    }

    private fun getAreaText():String {
        if(!mBinding.chooseShouhuoAreaText.text.toString().contains("请选择")) {
            return mBinding.chooseShouhuoAreaText.text.trim()
                .toString()
        }
        return ""
    }

    private var mProvince: ProvinceEntity? = null;
    private var mCity: CityEntity? = null;
    private var mCounty: CountyEntity? = null;


}