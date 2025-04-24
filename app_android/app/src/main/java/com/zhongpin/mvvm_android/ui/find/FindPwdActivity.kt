package com.zhongpin.mvvm_android.ui.find

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zhongpin.app.databinding.ActivityFindPwdBinding
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.ui.setpwd.FindSetPwdActivity

class FindPwdActivity : BaseVMActivity<FindPwdViewModel>() {


    private lateinit var mBinding: ActivityFindPwdBinding;
    private lateinit var mLoadingDialog: LoadingDialog
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityFindPwdBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(this, mBinding.content)
        mLoadingDialog = LoadingDialog(this, false)
        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.btnLogin.setOnClickListener {
            setAndCheck()
        }

        countDownTimer =  object: CountDownTimer(60*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val leftSecond = millisUntilFinished/1000;
                if (leftSecond > 0) {
                    mBinding.getVerifyCode.text = "" + (leftSecond) + "秒后重发"
                }
            }

            override fun onFinish() {
                mBinding.getVerifyCode.setEnabled(true)
                mBinding.getVerifyCode.text = "获取验证码"
            }
        }
        mBinding.getVerifyCode.setOnClickListener {
            if (mBinding.editUsername.text.isNotEmpty()) {
                mViewModel.sendVerifyCode(mBinding.editUsername.text.trim().toString()).observe(this){
                    if (it.success) {
                        mBinding.getVerifyCode.setEnabled(false)
                        countDownTimer.start()
                    } else {
                        Toast.makeText(applicationContext,"操作失败," + it.msg, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext,"请输入手机号", Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
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
        if (mBinding.editUsername.text.isNotEmpty() and mBinding.editVerifyCode.text.isNotEmpty()) {
            goSetPwdActivity()
        } else {
            Toast.makeText(applicationContext,"手机号或验证码为空", Toast.LENGTH_LONG).show()
        }
    }

    fun  goSetPwdActivity() {
        val intent = Intent(this, FindSetPwdActivity::class.java)
        intent.putExtra("code", mBinding.editUsername.text.trim().toString())
        intent.putExtra("mobile", mBinding.editVerifyCode.text.trim().toString())
        startActivity(intent)
        finish()
    }

}