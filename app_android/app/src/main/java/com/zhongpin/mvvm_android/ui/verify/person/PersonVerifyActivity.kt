package com.zhongpin.mvvm_android.ui.verify.person

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.zhongpin.app.BuildConfig
import com.zhongpin.app.databinding.ActivityPersonVerifyBinding
import com.zhongpin.lib_base.utils.LogUtils
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.photo.selector.GlideEngine
import com.zhongpin.mvvm_android.ui.verify.company.CompanyVerifyActivity
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import top.zibin.luban.OnNewCompressListener
import java.io.File
import java.util.Locale


class PersonVerifyActivity : BaseVMActivity<PersonVerifyViewModel>() {


    private lateinit var mBinding: ActivityPersonVerifyBinding;
    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityPersonVerifyBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(this, mBinding.content)
        mLoadingDialog = LoadingDialog(this, false)
        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.btnNext.setOnClickListener {
            //setAndCheck()
            val intent = Intent(this@PersonVerifyActivity, CompanyVerifyActivity::class.java)
            startActivity(intent)
        }

        mBinding.ivRightTitle.setOnClickListener {
            finish()
        }

        mBinding.idCardFront.setOnClickListener {
            PictureSelector.create(this@PersonVerifyActivity)
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

        mBinding.idCardBack.setOnClickListener {
            PictureSelector.create(this@PersonVerifyActivity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCompressEngine(object: CompressFileEngine {
                    override fun onStartCompress(
                        context: Context?,
                        source: java.util.ArrayList<Uri>?,
                        call: OnKeyValueResultCallbackListener?
                    ) {
                        if (source == null || source.isEmpty()) {
                            return;
                        }
                        Luban.with(this@PersonVerifyActivity)
                            .load(source)
                            .ignoreBy(100).setCompressListener(
                            object : OnNewCompressListener {
                                override fun onStart() {

                                }

                                override fun onSuccess(source: String?, compressFile: File?) {
                                    if (call != null) {
                                        if (compressFile != null) {
                                            LogUtils.d(
                                                "PersonVerifyActivity",
                                                "PersonVerifyActivity compressFile " + source + " compress " + compressFile?.absolutePath
                                                        + " length " + compressFile!!.length() / 1024
                                            )
                                        }
                                        call.onCallback(source, compressFile?.absolutePath);
                                    }
                                }

                                override fun onError(source: String?, e: Throwable?) {
                                    if (call != null) {
                                        call.onCallback(source, null);
                                    }
                                }
                            }
                        ).launch();
                    }

                })
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
                        val filePath = localMedia.compressPath ?: localMedia.realPath;
                        if (filePath.isNullOrEmpty()) {
                            return
                        }
                        uploadBackImage(filePath)
                    }

                    override fun onCancel() {
                    }
                })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun  uploadFrontImage(filePath: String) {

        Glide.with(this@PersonVerifyActivity)
            .load(Uri.fromFile(File(filePath)))
            .placeholder(mBinding.idCardFront.drawable)
            .into(mBinding.idCardFront)
    }

    fun  uploadBackImage(filePath: String) {

        Glide.with(this@PersonVerifyActivity)
            .load(Uri.fromFile(File(filePath)))
            .placeholder(mBinding.idCardBack.drawable)
            .into(mBinding.idCardBack)
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


}