package com.zhongpin.mvvm_android.ui.register

import android.content.DialogInterface
import android.content.Intent
import android.os.Build.*
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Html.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SPUtils
import com.zhongpin.app.BuildConfig
import com.zhongpin.app.databinding.ActivityRegisterBinding
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.bean.LoginEvent
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.ui.login.LoginActivity
import com.zhongpin.mvvm_android.ui.verify.person.PersonVerifyActivity
import com.zhongpin.mvvm_android.ui.web.WebActivity
import org.greenrobot.eventbus.EventBus

class RegisterActivity : BaseVMActivity<RegisterViewModel>() {


    private lateinit var mBinding: ActivityRegisterBinding;
    private lateinit var countDownTimer: CountDownTimer


    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityRegisterBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(this, mBinding.content)
        mViewModel.loadState.observe(this, {
            dismissLoadingDialog()
        })
        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.btnLogin.setOnClickListener {
            checkAndRegister()
        }
        val hasAccountHtmlText =
            "已有账号？<u>立即登录</u>"
        val hasAccountSpannedText = if (VERSION.SDK_INT >= VERSION_CODES.N) {
            fromHtml(hasAccountHtmlText, FROM_HTML_MODE_COMPACT)
        } else {
            fromHtml(hasAccountHtmlText)
        }
        mBinding.hasAccountTip.text = hasAccountSpannedText

        mBinding.hasAccountTip.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val htmlText =
            "阅读并同意 <font color='#57C248'>《用户服务协议》</font>"
        val spannedText = if (VERSION.SDK_INT >= VERSION_CODES.N) {
            fromHtml(htmlText, FROM_HTML_MODE_COMPACT)
        } else {
            fromHtml(htmlText)
        }
        mBinding.tvProtocol.text = spannedText
        mBinding.tvProtocol.setOnClickListener {
            val intent = Intent(this@RegisterActivity, WebActivity::class.java)
            intent.putExtra("title","用户服务协议")
            intent.putExtra("url","https://www.baidu.com")
            startActivity(intent)
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

    fun checkAndRegister(){
        if(mBinding.cbProtocol.isChecked){
            realRegister()
        }else{
            val builder = AlertDialog.Builder(this@RegisterActivity)
            builder.setMessage("我已阅读并同意《用户服务协议》")
            builder.setNeutralButton("取消", object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }

            })
            builder.setPositiveButton("确认", object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    mBinding.cbProtocol.isChecked = true
                    realRegister()
                }

            })
            builder.show()
        }
    }

    /**
     * show 加载中
     */
    fun showLoadingDialog() {
        dismissLoadingDialog()
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog(this, false)
        }
        mLoadingDialog?.showDialog(this, false)
    }

    /**
     * dismiss loading dialog
     */
    fun dismissLoadingDialog() {
        mLoadingDialog?.dismissDialog()
        mLoadingDialog = null
    }

    fun realRegister(){
        if (mBinding.editUsername.text.isNotEmpty()
            and mBinding.editPassword.text.isNotEmpty()
            and mBinding.editVerifyCode.text.isNotEmpty()) {
            if (mBinding.editPassword.text.trim().length < 6) {
                Toast.makeText(applicationContext,"密码长度最小6位", Toast.LENGTH_LONG).show()
                return
            }
            showLoadingDialog()
            mViewModel.register(
                mBinding.editUsername.text.trim().toString(),
                mBinding.editPassword.text.trim().toString(),
                mBinding.editVerifyCode.text.trim().toString(),
                mBinding.editUserNick.text.trim().toString()
            ).observe(this) {
                dismissLoadingDialog()
                if (it.success) {
                    Toast.makeText(applicationContext,"注册成功", Toast.LENGTH_LONG).show()
                    realLogin()
                } else {
                    Toast.makeText(applicationContext,"注册失败," + it.msg, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(applicationContext,"用户名或密码或验证码为空", Toast.LENGTH_LONG).show()
        }
    }

    fun realLogin(){
         mViewModel.login(
            mBinding.editUsername.text.trim().toString(),
            mBinding.editPassword.text.trim().toString(),
            ""
        ).observe(this) {
            dismissLoadingDialog()
            if (it.success && it.data != null) {
                val data = it.data
                data?.let {
                    if (BuildConfig.DEBUG) {
                        Log.d("LoginActivity", "LoginActivity login success " + it.token)
                    }
                    SPUtils.getInstance().put(Constant.TOKEN_KEY, it.token)
                    SPUtils.getInstance().put(Constant.LOGIN_MOBILE_KEY, it.token)

                    Toast.makeText(applicationContext,"登录成功", Toast.LENGTH_LONG).show()
                    EventBus.getDefault().post(LoginEvent(true))
                    goVerifyActivity()
                    finish()
                }
            } else {
                EventBus.getDefault().post(LoginEvent(false))
                Toast.makeText(applicationContext,"登录失败," + it.msg, Toast.LENGTH_LONG).show()
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun goVerifyActivity() {
        val intent = Intent(this@RegisterActivity, PersonVerifyActivity::class.java)
        startActivity(intent)
    }

}