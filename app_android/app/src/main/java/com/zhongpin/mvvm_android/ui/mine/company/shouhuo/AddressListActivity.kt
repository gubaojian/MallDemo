package com.zhongpin.mvvm_android.ui.mine.company.shouhuo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zhongpin.app.databinding.ActivityAddressListBinding
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.lib_base.view.LoadingDialog
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.bean.AddressInfoChangeEvent
import com.zhongpin.mvvm_android.bean.AddressListItemResponse
import com.zhongpin.mvvm_android.bean.CompanyInfoChangeEvent
import com.zhongpin.mvvm_android.network.ApiService
import com.zhongpin.mvvm_android.ui.mine.company.detail.CompanyDetailActivity
import com.zhongpin.mvvm_android.ui.mine.company.shouhuo.add.AddReceiveAddressActivity
import com.zhongpin.mvvm_android.ui.mine.company.shouhuo.edit.EditReceiveAddressActivity
import com.zhongpin.mvvm_android.ui.verify.company.CompanyVerifyActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.zhongpin.mvvm_android.bean.AddressInfoChangeEvent as AddressInfoChangeEvent1


@EventBusRegister
class AddressListActivity : BaseVMActivity<AddressListViewModel>() {


    private lateinit var mBinding: ActivityAddressListBinding;


    private var mDatas:MutableList<AddressListItemResponse> = mutableListOf()
    private lateinit var listAdapter: AddressListAdapter

    private var entId:Long = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        //android:windowSoftInputMode="adjustResize"
        // 键盘view整体上移，暂时不用沉浸式导航栏
        StatusBarUtil.immersive(this)
        if (intent != null) {
            entId = intent.getLongExtra("entId", -1)
        }
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityAddressListBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }

    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(this, mBinding.content)
        registerDefaultLoad(mBinding.refreshLayout, ApiService.COMPANY_LIST)
        mBinding.ivBack.setOnClickListener {
            finish()
        }

        mBinding.addCompany.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddReceiveAddressActivity::class.java)
            intent.putExtra("entId", entId)
            startActivity(intent)
        }

        mBinding.refreshLayout.setEnableRefresh(true)
        mBinding.refreshLayout.setEnableLoadMore(false)
        mBinding.refreshLayout.setRefreshHeader(ClassicsHeader(this))
        mBinding.refreshLayout.setRefreshFooter(ClassicsFooter(this))
        mBinding.refreshLayout.setOnRefreshListener {
            mViewModel.getEntReceiveAddressList(entId)
        }

        listAdapter = AddressListAdapter(this, {
                                               pos, item ->
            onDelete(pos, item)
        },{
          pos, item ->
            onEdit(pos, item)
        }, mDatas)
        listAdapter.setOnItemClickListener {
            adapter, view, position ->
            val addressListItem = mDatas.get(position)
            //val intent = Intent(this@AddressListActivity, CompanyDetailActivity::class.java)
            //intent.putExtra("companyListItem", companyListItem)
            //startActivity(intent)
        }
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.adapter = listAdapter
    }

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.mData.observe(this) {
            if(it.success) {
                val records = it.data;
                if (records.isNullOrEmpty()) {
                    showEmpty(ApiService.COMPANY_LIST)
                    mDatas.clear()
                } else {
                   showSuccess(ApiService.COMPANY_LIST)
                   mDatas.clear()
                   mDatas.addAll(records)
                }
                listAdapter.notifyDataSetChanged()
            }
            mBinding.refreshLayout.finishRefresh()
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getEntReceiveAddressList(entId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(infoEvent : AddressInfoChangeEvent1){
        if (infoEvent.isChange) {
            mViewModel.getEntReceiveAddressList(entId)
        }
    }

    fun  onDelete(position: Int, item: AddressListItemResponse) {
        val builder = AlertDialog.Builder(this@AddressListActivity)
        builder.setMessage("确认删除吗？")
        builder.setNeutralButton("取消", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })
        builder.setPositiveButton("确认", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                deleteAddressItem(item)
            }

        })
        builder.show()
    }

    fun onEdit(position: Int, item: AddressListItemResponse) {
        val intent = Intent(this@AddressListActivity, EditReceiveAddressActivity::class.java)
        intent.putExtra("addressItem", item)
        startActivity(intent)
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

    //https://space-64stfp.w.eolink.com/home/api-studio/inside/p346wIBec8b27b4efe594eed716ffa6a4764f833feb25fd/api/3148316/detail/55818666?spaceKey=space-64stfp
    fun deleteAddressItem(item: AddressListItemResponse){
        showLoadingDialog()
        val id  = item.id ?: 0
        mViewModel.deleteReceiveAddress(id).observe(this) {
            dismissLoadingDialog()
            if (it.success) {
                EventBusUtils.postEvent(AddressInfoChangeEvent(true))
            } else {
                Toast.makeText(applicationContext,"删除失败 " + it.msg, Toast.LENGTH_LONG).show()
            }
        }
    }


}