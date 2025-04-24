package com.zhongpin.mvvm_android.ui.verify.company

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
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
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.zhongpin.app.BuildConfig
import com.zhongpin.app.databinding.ActivityCompanyVerifyBinding
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.photo.selector.GlideEngine
import java.io.File


class CompanyVerifyActivity : BaseVMActivity<CompanyVerifyViewModel>(), OnAddressPickedListener {


    private lateinit var mBinding: ActivityCompanyVerifyBinding;
    private lateinit var mLoadingDialog: LoadingDialog
    private var selectCompanyType:Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        //android:windowSoftInputMode="adjustResize"
        // 键盘view整体上移，暂时不用沉浸式导航栏
        //StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityCompanyVerifyBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        super.initView()
        //StatusBarUtil.setMargin(this, mBinding.content)
        mLoadingDialog = LoadingDialog(this, false)
        mBinding.ivBack.setOnClickListener { finish() }

        val companyTypes = arrayOf("造纸厂", "纸板厂", "纸箱厂", "耗材厂商", "纸箱使用单位")
        mBinding.chooseCompanyTypeContainer.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("选择单位类别")

            // 设置对话框内容为单选列表项，默认选中第一项（索引为0）
            builder.setSingleChoiceItems(companyTypes, selectCompanyType, DialogInterface.OnClickListener { dialog, which -> // 当用户选择一个单位类别时，更新文本视图显示所选单位
                    mBinding.chooseCompanyTypeText.text = companyTypes[which]
                    selectCompanyType = which
                    dialog.dismiss()
                })
            // 添加取消按钮
            builder.setNegativeButton("取消",DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })


            // 显示对话框
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


        mBinding.yingYeZhiZhao.setOnClickListener {
            PictureSelector.create(this@CompanyVerifyActivity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1)
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: ArrayList<LocalMedia?>) {
                        if (result.isNullOrEmpty()) {
                            return;
                        }
                        val localMedia = result[0];
                        if (localMedia == null) {
                            return;
                        }
                        if(BuildConfig.DEBUG) {
                            //content://
                            Log.e("photo", "photo selector " + localMedia.path + " cut path " + localMedia.cutPath)
                        }
                        val filePath = localMedia.realPath ?: "";
                        if (filePath.isNullOrEmpty()) {
                            return
                        }
                        uploadFrontImage(filePath)
                    }

                    override fun onCancel() {
                    }
                })
        }

        mBinding.chooseShouhuoAreaContainer.setOnClickListener {
            val picker = AddressPicker(this)
            picker.setAddressMode(AddressMode.PROVINCE_CITY_COUNTY)
            picker.setDefaultValue("浙江省", "杭州市", "西湖区")
            picker.setOnAddressPickedListener(this)
            picker.wheelLayout.setOnLinkageSelectedListener { first, second, third ->
                picker.titleView.text = String.format(
                    "%s%s%s",
                    picker.firstWheelView.formatItem(first),
                    picker.secondWheelView.formatItem(second),
                    picker.thirdWheelView.formatItem(third)
                )
            }
            picker.show()
        }

        mBinding.btnLater.setOnClickListener {
            finish()
        }

        mBinding.btnSubmit.setOnClickListener {
            setAndCheck()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun  uploadFrontImage(filePath: String) {
        Glide.with(this@CompanyVerifyActivity)
            .load(Uri.fromFile(File(filePath)))
            .placeholder(mBinding.yingYeZhiZhao.drawable)
            .into(mBinding.yingYeZhiZhao)
    }


    fun setAndCheck(){
        realSetPassword()
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

    fun realSetPassword(){

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

        val address = String.format(
            "%s%s%s",
            province.name,
            city.name,
            county.name
        )
        mBinding.chooseShouhuoAreaText.text = address
    }


}