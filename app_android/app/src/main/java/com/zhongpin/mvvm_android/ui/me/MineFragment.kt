package com.zhongpin.mvvm_android.ui.me

import android.app.Activity
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NotificationUtils
import com.bumptech.glide.Glide
import com.google.zxing.client.android.Intents
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zhongpin.app.BuildConfig
import com.zhongpin.app.R
import com.zhongpin.app.databinding.FragmentMineBinding
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.mvvm_android.base.view.BaseVMFragment
import com.zhongpin.mvvm_android.bean.LoginEvent
import com.zhongpin.mvvm_android.bean.TokenExpiredEvent
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.biz.utils.UserInfoUtil
import com.zhongpin.mvvm_android.common.login.LoginUtils
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.service.UserNotificationUtil
import com.zhongpin.mvvm_android.ui.buy.PublishBuyActivity
import com.zhongpin.mvvm_android.ui.debug.DebugActivity
import com.zhongpin.mvvm_android.ui.login.LoginActivity
import com.zhongpin.mvvm_android.ui.mine.company.CompanyListActivity
import com.zhongpin.mvvm_android.ui.notify.setting.NotifySettingActivity
import com.zhongpin.mvvm_android.ui.photo.preview.PhonePreviewerActivity
import com.zhongpin.mvvm_android.ui.scan.ScanCaptureActivity
import com.zhongpin.mvvm_android.ui.shouhuo.result.ShuoHuoSuccessActivity
import com.zhongpin.mvvm_android.ui.verify.company.CompanyVerifyActivity
import com.zhongpin.mvvm_android.ui.verify.person.PersonVerifyActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.atomic.AtomicInteger


/**
 * A simple [Fragment] subclass.
 * Use the [MineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@EventBusRegister
class MineFragment : BaseVMFragment<MineViewModel>() {
    private val ARG_PARAM1 = "param1"

    private var param1: String? = null

    private lateinit var mBinding: FragmentMineBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = FragmentMineBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }


    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(activity, mBinding.content)

        mBinding.versionName.text = "版本号：" + AppUtils.getAppVersionName()
        if (BuildConfig.DEBUG) {
            mBinding.versionName.setOnClickListener {
                val intent = Intent(activity, DebugActivity::class.java)
                startActivity(intent)
            }
        }

        mBinding.loginOut.setOnClickListener {
            confirmLoginOut()
        }

        mBinding.notifySettingContainer.setOnClickListener {
            val intent = Intent(activity, NotifySettingActivity::class.java)
            startActivity(intent)
        }

        mBinding.myCompanyContainer.setOnClickListener {
            val intent = Intent(activity, CompanyListActivity::class.java)
            startActivity(intent)
        }

        mBinding.fabuBuyContainer.setOnClickListener {
            val intent = Intent(activity, PublishBuyActivity::class.java)
            startActivity(intent)
        }

        mBinding.companyAmount.text = HtmlCompat.fromHtml("已添加 <big><font color='#333333'><b>3</b></font></big> 家企业", HtmlCompat.FROM_HTML_MODE_LEGACY)

        mBinding.publishBuyDesc.text = HtmlCompat.fromHtml("已发布 <big><font color='#333333'><b>2</b></font></big> 次采购", HtmlCompat.FROM_HTML_MODE_LEGACY)


        mBinding.refreshLayout.setEnableRefresh(true)
        mBinding.refreshLayout.setRefreshHeader(ClassicsHeader(requireActivity()))
        mBinding.refreshLayout.setOnRefreshListener {
            mViewModel.getUserInfo()
        }
        registerDefaultLoad(mBinding.content, Constant.COMMON_KEY)
    }


    override fun initDataObserver() {
        mViewModel.mUserInfoData.observe(viewLifecycleOwner, Observer {
            if (it.success) {
                showSuccess(Constant.COMMON_KEY)
                UserInfoUtil.userInfo = it.data
                val userInfoResponse = it.data;
                userInfoResponse?.let {
                    if (!TextUtils.isEmpty(it.nickName)) {
                        mBinding.userNick.text = it.nickName
                        mBinding.userNick.visibility = View.VISIBLE;
                    } else {
                        mBinding.userNick.visibility = View.GONE;
                    }
                    mBinding.userPhone.text = "手机号：" + UserInfoUtil.maskPhone(it.mobile)
                    if (!TextUtils.isEmpty(it.headImage)) {
                        Glide.with(this@MineFragment)
                            .load(it.headImage)
                            .placeholder(mBinding.mineAvatar.drawable)
                            .into(mBinding.mineAvatar)
                    } else {
                        mBinding.mineAvatar.setImageResource(R.mipmap.ic_user_default)
                    }
                }
            }
            mBinding.refreshLayout.finishRefresh()
        })
    }


    override fun initData() {
        super.initData()
        mViewModel.getUserInfo()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(loginEvent : LoginEvent){
        if (loginEvent.isLogin) {
            initData();
        }
    }

    fun  confirmLoginOut() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("确认退出登录吗？")
        builder.setNeutralButton("取消", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })
        builder.setPositiveButton("确认", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                loginOut()
            }

        })
        builder.show()
    }

    fun loginOut() {
        EventBusUtils.postEvent(TokenExpiredEvent(true));
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        @JvmStatic
        fun newInstance(param1: String) =
            MineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}