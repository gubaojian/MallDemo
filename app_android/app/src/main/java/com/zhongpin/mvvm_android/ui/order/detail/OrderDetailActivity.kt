package com.zhongpin.mvvm_android.ui.order.detail

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.sum.glide.setUrl
import com.zhongpin.app.R.*
import com.zhongpin.app.databinding.ActivityOrderDetailBinding
import com.zhongpin.lib_base.utils.BannerUtils
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.bean.CompanyInfoChangeEvent
import com.zhongpin.mvvm_android.bean.CompanyListItemResponse
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.ui.common.CompanyConfigData
import com.zhongpin.mvvm_android.ui.mine.company.edit.EditCompanyVerifyActivity
import com.zhongpin.mvvm_android.ui.mine.company.dizhi.AddressListActivity
import com.zhongpin.mvvm_android.ui.order.detail.timeline.OrderStatusTimelineAdapter
import com.zhongpin.mvvm_android.ui.order.detail.timeline.OrderStatusTimelineModel
import com.zhongpin.mvvm_android.ui.order.fahuodetail.FaHuoDetailActivity
import com.zhongpin.mvvm_android.ui.photo.preview.PhonePreviewerActivity
import com.zhongpin.mvvm_android.ui.utils.IntentUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@EventBusRegister
class OrderDetailActivity : BaseVMActivity<OrderDetailViewModel>() {


    private lateinit var mBinding: ActivityOrderDetailBinding;

    private var orderListItem: CompanyListItemResponse? = null

    private val timelineModels: MutableList<OrderStatusTimelineModel> = mutableListOf()

    private lateinit var mOrderStatusTimelineAdapter: OrderStatusTimelineAdapter;


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        if (intent != null) {
            orderListItem = IntentUtils.getSerializableExtra(intent, "orderListItem", CompanyListItemResponse::class.java)
        }
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityOrderDetailBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        mViewModel.loadState.observe(this, {
            dismissLoadingDialog()
        })
        super.initView()
        StatusBarUtil.setMargin(this, mBinding.content)
        mBinding.ivBack.setOnClickListener { finish() }

        mBinding.fahuoDetail.setOnClickListener {
            val intent = Intent(this@OrderDetailActivity, FaHuoDetailActivity::class.java)
            intent.putExtra("orderListItem", orderListItem)
            startActivity(intent)
        }
        //BannerUtils.setBannerRound(mBinding.yingYeZhiZhao, SizeUtils.dp2px(8.0f).toFloat())
        val item = orderListItem;
        if (item == null) {
            Toast.makeText(applicationContext,"请传入公司详情信息", Toast.LENGTH_LONG).show()
            return;
        }

        timelineModels.add(OrderStatusTimelineModel("已接单", "2025-02-12 08:00", OrderStatusTimelineModel.DONE))
        timelineModels.add(OrderStatusTimelineModel("已发货", "2025-02-12 08:00", OrderStatusTimelineModel.ACTIVE))
        timelineModels.add(OrderStatusTimelineModel("驾驶员已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))
        timelineModels.add(OrderStatusTimelineModel("客户已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))


        timelineModels.add(OrderStatusTimelineModel("已接单", "2025-02-12 08:00", OrderStatusTimelineModel.DONE))
        timelineModels.add(OrderStatusTimelineModel("已发货", "2025-02-12 08:00", OrderStatusTimelineModel.ACTIVE))
        timelineModels.add(OrderStatusTimelineModel("驾驶员已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))
        timelineModels.add(OrderStatusTimelineModel("客户已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))

        timelineModels.add(OrderStatusTimelineModel("已接单", "2025-02-12 08:00", OrderStatusTimelineModel.DONE))
        timelineModels.add(OrderStatusTimelineModel("已发货", "2025-02-12 08:00", OrderStatusTimelineModel.ACTIVE))
        timelineModels.add(OrderStatusTimelineModel("驾驶员已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))
        timelineModels.add(OrderStatusTimelineModel("客户已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))

        timelineModels.add(OrderStatusTimelineModel("已接单", "2025-02-12 08:00", OrderStatusTimelineModel.DONE))
        timelineModels.add(OrderStatusTimelineModel("已发货", "2025-02-12 08:00", OrderStatusTimelineModel.ACTIVE))
        timelineModels.add(OrderStatusTimelineModel("驾驶员已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))
        timelineModels.add(OrderStatusTimelineModel("客户已确认", "2025-02-12 08:00", OrderStatusTimelineModel.UN_DONE))


        val  layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mOrderStatusTimelineAdapter = OrderStatusTimelineAdapter(timelineModels);
        mBinding.timelineRecyclerView.layoutManager = layoutManager;
        mBinding.timelineRecyclerView.adapter = mOrderStatusTimelineAdapter;

        /**
        mBinding.name.text = item.name
        mBinding.address.text = item.address
        mBinding.xinyongCode.text = item.unite
        mBinding.legal.text = item.legal
        mBinding.mobile.text = item.mobile
        mBinding.naShuiCode.text = item.identify

        if (item.status == 0) {
            mBinding.renZhenStatus.text = "待审核"
            mBinding.renZhenStatus.setTextColor(Color.parseColor("#FFA826"))
        } else if (item.status == 2) {
            mBinding.renZhenStatus.text = "认证失败"
            mBinding.renZhenStatus.setTextColor(Color.parseColor("#D34545"))
        } else {
            mBinding.renZhenStatus.text = "已认证"
            mBinding.renZhenStatus.setTextColor(Color.parseColor("#57C248"))
        }

        if (item.status == 1) { //已认证，不删除，只能编辑。
            mBinding.deleteButtonContainer.visibility = View.GONE
            mBinding.bottomButtonLine.visibility = View.GONE
        }

        mBinding.showHuoDiZhiContainer.setOnClickListener {
            val intent = Intent(this@OrderDetailActivity, AddressListActivity::class.java)
            intent.putExtra("entId", item.id)
            startActivity(intent)
        }

        mBinding.deleteButtonContainer.setOnClickListener {
            showDeleteConfirmDialog()
        }

        mBinding.editButtonContainer.setOnClickListener {
            val intent = Intent(this@OrderDetailActivity, EditCompanyVerifyActivity::class.java)
            intent.putExtra("companyListItem", item)
            startActivity(intent)
        }

        mBinding.yingYeZhiZhao.setUrl(item.license)
        mBinding.yingYeZhiZhaoContainer.setOnClickListener {
            val intent = Intent(this@OrderDetailActivity, PhonePreviewerActivity::class.java)
            intent.putExtra("imageUrls", arrayOf<String>(item.license ?: ""))
            startActivity(intent)
        }

        mBinding.bankContainer.setOnClickListener {
            val isOpen:Boolean = (mBinding.bankContainer.tag ?: false) as Boolean
            if (isOpen) {
                mBinding.bankCode.text = "***************"
                mBinding.bankName.text = "*******"
                mBinding.bankEye.setImageResource(mipmap.detail_see_open)
                mBinding.bankContainer.tag = false
            } else {
                mBinding.bankCode.text = item.bankAccount ?: ""
                mBinding.bankName.text = item.bankName ?: ""
                mBinding.bankEye.setImageResource(mipmap.detail_see_close)
                mBinding.bankContainer.tag = true
            }
        }
        mBinding.companyType.text = CompanyConfigData.getCompanyType(item.entType ?: -1)
        //registerDefaultLoad(mBinding.loadContainer, ApiService.USER_AUTH_INFO)
        */
    }

    override fun initDataObserver() {
        super.initDataObserver()
        /**
        mViewModel.mUserAuthInfoData.observe(this) {
            showSuccess(ApiService.USER_AUTH_INFO)
            mBinding.companyType.text = CompanyConfigData.getCompanyType(it.data?.entType ?: -1)
        }*/
    }

    override fun initData() {
        super.initData()
        //mViewModel.getUserAuthInfoData()
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(infoEvent : CompanyInfoChangeEvent){
        if (infoEvent.isChange) {
            finish()
        }
    }

    fun showDeleteConfirmDialog() {
        val builder = AlertDialog.Builder(this@OrderDetailActivity)
        builder.setMessage("确认删除吗？")
        builder.setNeutralButton("取消", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })
        builder.setPositiveButton("确认", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                deleteEntInfo()
            }

        })
        builder.show()
    }

    //https://space-64stfp.w.eolink.com/home/api-studio/inside/p346wIBec8b27b4efe594eed716ffa6a4764f833feb25fd/api/3148316/detail/55818666?spaceKey=space-64stfp
    fun deleteEntInfo(){
        showLoadingDialog()
         val id  = orderListItem?.id ?: 0
        mViewModel.deleteEntInfoAuth(id).observe(this) {
            dismissLoadingDialog()
            if (it.success) {
                EventBusUtils.postEvent(CompanyInfoChangeEvent(true))
                finish()
            } else {
                Toast.makeText(applicationContext,"删除失败 " + it.msg, Toast.LENGTH_LONG).show()
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
    }


}