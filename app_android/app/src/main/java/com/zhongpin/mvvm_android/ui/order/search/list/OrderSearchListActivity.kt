package com.zhongpin.mvvm_android.ui.order.search.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zhongpin.app.databinding.ActivityCompanyListBinding
import com.zhongpin.app.databinding.ActivityOrderSearchListBinding
import com.zhongpin.app.databinding.ActivityOrderSearchQueryBinding
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.base.view.BaseVMActivity
import com.zhongpin.mvvm_android.bean.CompanyInfoChangeEvent
import com.zhongpin.mvvm_android.bean.CompanyListItemResponse
import com.zhongpin.mvvm_android.bean.OrderSearchQuery
import com.zhongpin.mvvm_android.network.ApiService
import com.zhongpin.mvvm_android.ui.mine.company.detail.CompanyDetailActivity
import com.zhongpin.mvvm_android.ui.order.search.OrderSearchQueryActivity
import com.zhongpin.mvvm_android.ui.utils.IntentUtils
import com.zhongpin.mvvm_android.ui.verify.company.CompanyVerifyActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@EventBusRegister
class OrderSearchListActivity : BaseVMActivity<OrderSearchListViewModel>() {


    private lateinit var mBinding: ActivityOrderSearchListBinding;

    private var searchQuery: OrderSearchQuery? = null;

    private var mDatas:MutableList<CompanyListItemResponse> = mutableListOf()
    private lateinit var listAdapter: OrderSearchListAdapter
    private var mPageMoreNo:Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent != null) {
            searchQuery = IntentUtils.getSerializableExtra(intent, "query", OrderSearchQuery::class.java)
        }
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }


    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = ActivityOrderSearchListBinding.inflate(layoutInflater, container, false)
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
        showSearchQuery()
        val startSearchQueryLauncher: ActivityResultLauncher<OrderSearchQuery?> = registerForActivityResult(
            object  : ActivityResultContract<OrderSearchQuery?,  OrderSearchQuery?>() {
                override fun createIntent(context: Context, input: OrderSearchQuery?): Intent {
                    overridePendingTransition(0, 0)
                    val intent = Intent(this@OrderSearchListActivity, OrderSearchQueryActivity::class.java)
                    intent.putExtra("query", searchQuery)
                    return  intent
                }

                override fun parseResult(resultCode: Int, intent: Intent?): OrderSearchQuery? {
                    var query: OrderSearchQuery?  = null;
                    if (intent != null && resultCode == Activity.RESULT_OK) {
                        query = IntentUtils.getSerializableExtra(intent,"query", OrderSearchQuery::class.java);
                    }
                    return  query;
                }

            },
            ActivityResultCallback {
                if (it != null) {
                    searchQuery = it;
                    searchOrderByQuery();
                }
            }
        )

        mBinding.searchBar.setOnClickListener {
            startSearchQueryLauncher.launch(searchQuery)
        }

        mBinding.refreshLayout.setEnableRefresh(true)
        mBinding.refreshLayout.setEnableLoadMore(true)
        mBinding.refreshLayout.setRefreshHeader(ClassicsHeader(this))
        mBinding.refreshLayout.setRefreshFooter(ClassicsFooter(this))
        mBinding.refreshLayout.setOnRefreshListener {
            mViewModel.getFirstPageCompanyList()
        }
        mBinding.refreshLayout.setOnLoadMoreListener {
            mViewModel.getCompanyListMore(mPageMoreNo)
        }

        listAdapter = OrderSearchListAdapter(this, mDatas)
        listAdapter.setOnItemClickListener {
            adapter, view, position ->
            val companyListItem = mDatas.get(position)
            val intent = Intent(this@OrderSearchListActivity, CompanyDetailActivity::class.java)
            intent.putExtra("companyListItem", companyListItem)
            startActivity(intent)
        }
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.adapter = listAdapter
    }

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.mFirstPageData.observe(this) {
            if(it.success) {
                mPageMoreNo = 2
                val records = it.data?.records
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
        mViewModel.mMorePageData.observe(this) {
            if(it.success) {
                mPageMoreNo++
                val records = it.data?.records
                if (!records.isNullOrEmpty()) {
                    mDatas.addAll(records)
                    mBinding.refreshLayout.finishLoadMore(1000)
                    listAdapter.notifyDataSetChanged()
                } else {
                    mBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                mBinding.refreshLayout.finishLoadMoreWithNoMoreData()
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getFirstPageCompanyList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(infoEvent : CompanyInfoChangeEvent){
        if (infoEvent.isChange) {
            mViewModel.getFirstPageCompanyList()
        }
    }

    fun showSearchQuery() {
        val query = searchQuery?.toQuery();
        if (query != null && query.isNotEmpty()) {
            mBinding.searchQuery.text = query
        } else {
            mBinding.searchQuery.text = "查询全部订单"
        }
    }

    fun searchOrderByQuery() {
        showSearchQuery()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}