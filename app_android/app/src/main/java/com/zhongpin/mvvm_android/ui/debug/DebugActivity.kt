package com.zhongpin.mvvm_android.ui.debug

import android.app.Activity
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.NotificationUtils
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.google.zxing.client.android.Intents
import com.zhongpin.app.R
import com.zhongpin.app.databinding.ActivityDebugBinding
import com.zhongpin.app.databinding.ActivityPingzhiFeedbackBinding
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.bean.CompanyInfoChangeEvent
import com.zhongpin.mvvm_android.bean.CompanyListItemResponse
import com.zhongpin.mvvm_android.bean.FeedbackEvent
import com.zhongpin.mvvm_android.bean.TokenExpiredEvent
import com.zhongpin.mvvm_android.common.login.LoginUtils
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.network.ApiService
import com.zhongpin.mvvm_android.service.UserNotificationUtil
import com.zhongpin.mvvm_android.ui.buy.PublishBuyActivity
import com.zhongpin.mvvm_android.ui.common.CompanyConfigData
import com.zhongpin.mvvm_android.ui.feedback.PingZhiFeedbackActivity
import com.zhongpin.mvvm_android.ui.feedback.chooseorder.FeedbackChooseOrderActivity
import com.zhongpin.mvvm_android.ui.login.LoginActivity
import com.zhongpin.mvvm_android.ui.mine.company.CompanyListActivity
import com.zhongpin.mvvm_android.ui.photo.preview.PhonePreviewerActivity
import com.zhongpin.mvvm_android.ui.scan.ScanCaptureActivity
import com.zhongpin.mvvm_android.ui.shouhuo.ConfirmShuoHuoActivity
import com.zhongpin.mvvm_android.ui.shouhuo.result.ShuoHuoSuccessActivity
import com.zhongpin.mvvm_android.ui.utils.IntentUtils
import com.zhongpin.mvvm_android.ui.verify.company.CompanyVerifyActivity
import com.zhongpin.mvvm_android.ui.verify.person.PersonVerifyActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.atomic.AtomicInteger


class DebugActivity : BaseVMActivity<DebugViewModel>() {


    private lateinit var mBinding: ActivityDebugBinding;


    private var selectFeedbackType:Int = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        //android:windowSoftInputMode="adjustResize"
        // 键盘view整体上移，暂时不用沉浸式导航栏
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityDebugBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        mViewModel.loadState.observe(this, {
            dismissLoadingDialog()
        })
        super.initView()
        StatusBarUtil.setMargin(this, mBinding.content)
        registerDefaultLoad(mBinding.loadContainer, ApiService.USER_AUTH_INFO)

        mBinding.ivBack.setOnClickListener { finish() }

        val binding = mBinding;
        val activity = this@DebugActivity;
        val mActivity = this@DebugActivity;
        binding.login.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.verify.setOnClickListener {
            val intent = Intent(activity, PersonVerifyActivity::class.java)
            startActivity(intent)
        }

        binding.entVerify.setOnClickListener {
            val intent = Intent(activity, CompanyVerifyActivity::class.java)
            startActivity(intent)
        }

        binding.companyList.setOnClickListener {
            LoginUtils.ensureLogin(activity) {
                val intent = Intent(activity, CompanyListActivity::class.java)
                startActivity(intent)
            }
        }

        binding.photo.setOnClickListener {
            val intent = Intent(activity, PhonePreviewerActivity::class.java)
            intent.putExtra("imageUrls", arrayOf<String>(
                "https://img.alicdn.com/bao/uploaded/i2/2268175280/O1CN01wvhOlY1osIBwjOCOP_!!2268175280.jpg_460x460q90.jpg_.webp",
                "https://img.alicdn.com/bao/uploaded/i3/3928142771/O1CN01tzdOzK1WLANtDlnTJ_!!3928142771.jpg_460x460q90.jpg_.webp"))
            startActivity(intent)
        }

        val id = AtomicInteger();
        binding.notify.setOnClickListener {
            NotificationUtils.notify(id.incrementAndGet(), {
                    param ->
                val intent = Intent(mActivity, ShuoHuoSuccessActivity::class.java)
                param.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("众品")
                    .setContentText("众品通知服务")
                    .setContentIntent(PendingIntent.getActivity(mActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE))
                    .setAutoCancel(true)
            })
        }

        binding.start.setOnClickListener {
            UserNotificationUtil.startService()
        }

        binding.stop.setOnClickListener {
            UserNotificationUtil.stopService()
        }

        binding.scan2.setOnClickListener {
            val intent = Intent(activity, ScanCaptureActivity::class.java)
            startActivityForResult(intent, 100)
        }

        binding.tokenExpired.setOnClickListener {
            EventBusUtils.postEvent(TokenExpiredEvent(true));
        }

        mBinding.confirmShouhuo.setOnClickListener {
            val intent = Intent(activity, ConfirmShuoHuoActivity::class.java)
            startActivity(intent)
        }

        mBinding.confirmFeedbackChooseOrder.setOnClickListener {
            val intent = Intent(activity, FeedbackChooseOrderActivity::class.java)
            startActivity(intent)
        }

        mBinding.confirmFeedback.setOnClickListener {
            val intent = Intent(activity, PingZhiFeedbackActivity::class.java)
            startActivity(intent)
        }

        mBinding.addBuyXuqiu.setOnClickListener {
            val intent = Intent(activity, PublishBuyActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.mUserAuthInfoData.observe(this) {
            showSuccess(Constant.COMMON_KEY)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getUserAuthInfoData()
    }



    private var mLoadingDialog: LoadingDialog? = null
    /**
     * show 加载中
     */
    fun showLoadingDialog() {
        dismissLoadingDialog()
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog(this, false)
        }
        mLoadingDialog?.showDialogV2(this)
    }

    /**
     * dismiss loading dialog
     */
    fun dismissLoadingDialog() {
        mLoadingDialog?.dismissDialogV2()
        mLoadingDialog = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(applicationContext,"扫描结果," + data?.getStringExtra(Intents.Scan.RESULT), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}