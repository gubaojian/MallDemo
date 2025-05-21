package com.zhongpin.mvvm_android.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.client.android.Intents
import com.scwang.smart.refresh.header.ClassicsHeader
import com.youth.banner.indicator.CircleIndicator
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.app.databinding.FragmentHomeBinding
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.mvvm_android.bean.LoginEvent
import com.zhongpin.mvvm_android.common.login.LoginUtils
import com.zhongpin.mvvm_android.ui.buy.PublishBuyActivity
import com.zhongpin.mvvm_android.ui.scan.ScanCaptureActivity
import com.zhongpin.mvvm_android.ui.shouhuo.ConfirmShuoHuoActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import com.zhongpin.mvvm_android.base.view.BaseVMFragment as BaseVMFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@EventBusRegister
class HomeFragment : BaseVMFragment<HomeViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    private lateinit var mBinding: FragmentHomeBinding;

    private var data: MutableList<HomeItemEntity> = mutableListOf()
    private lateinit var homeListAdapter: HomeListAdapter

    private lateinit var startScanLauncher: ActivityResultLauncher<Void?>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = mBinding.root
        return view
    }


    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(activity, mBinding.homeTopBanner)

        initBanner()

        mBinding.refreshLayout.setEnableRefresh(true)
        mBinding.refreshLayout.setRefreshHeader(ClassicsHeader(getContext()))
        mBinding.refreshLayout.setOnRefreshListener {
            refreshPageData()
        }
        data.add(HomeItemEntity(data = null, type = HomeListAdapter.Companion.BUY_SCAN_TYPE))
        data.add(HomeItemEntity(data = null, type = HomeListAdapter.Companion.PINGTAI_DATA_TYPE))
        data.add(HomeItemEntity(data = null, type = HomeListAdapter.Companion.NEWS_TITLE_TYPE))
        data.add(HomeItemEntity(data = null, type = HomeListAdapter.Companion.NEWS_ITEM_TYPE))
        data.add(HomeItemEntity(data = null, type = HomeListAdapter.Companion.NEWS_ITEM_TYPE))
        data.add(HomeItemEntity(data = null, type = HomeListAdapter.Companion.NEWS_ITEM_TYPE))
        homeListAdapter = HomeListAdapter(this@HomeFragment, data)
        mBinding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        mBinding.homeRecyclerView.adapter = homeListAdapter


        startScanLauncher = registerForActivityResult(
            object  : ActivityResultContract<Void?, String?>() {
                override fun createIntent(context: Context, input: Void?): Intent {
                    val intent = Intent(mActivity, ScanCaptureActivity::class.java)
                    return  intent
                }

                override fun parseResult(resultCode: Int, intent: Intent?): String? {
                    var qr: String?  = null;
                    if (intent != null && resultCode == Activity.RESULT_OK) {
                        qr = intent.getStringExtra(Intents.Scan.RESULT)
                    }
                    return qr;
                }

            },
            ActivityResultCallback {
                if (it != null) {
                    handleScanQrCode(it);
                }
            }
        )
    }

    override fun initDataObserver() {
        mViewModel.mUserInfoData.observe(viewLifecycleOwner, Observer {
            mBinding.refreshLayout.finishRefresh();
        })

    }

    override fun initData() {
        super.initData()
        if (LoginUtils.hasLogin()) {
            mViewModel.getUserInfo()
        }
    }

    private fun refreshPageData() {
        if (LoginUtils.hasLogin()) {
            mViewModel.getUserInfo()
        } else {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    mBinding.refreshLayout.finishRefresh()
                },
                2000
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(loginEvent : LoginEvent){
        if (loginEvent.isLogin) {
            mViewModel.getUserInfo()
        }
    }



    private fun setBannerData(list: List<UserInfoResponse>) {
        if (data.isEmpty() || (data.get(0).type != 1)) {
            data.add(0, HomeItemEntity(list, 1))
            homeListAdapter.notifyItemInserted(0)
        } else {
            data.set(0, HomeItemEntity(list, 1))
            homeListAdapter.notifyItemChanged(0)
        }
        //mHeaderView.mBanner.adapter = BannerImageAdapter(list)
        //mHeaderView.mBanner.addBannerLifecycleObserver(this)
        //mHeaderView.mBanner.indicator = CircleIndicator(activity)
        //mHeaderView.mBanner.setBannerRound2(20f)
    }

    private fun initBanner() {
        mBinding.banner.setIndicator(CircleIndicator(mActivity))
        val imageUrls: MutableList<UserInfoResponse> = mutableListOf();
        imageUrls.add(UserInfoResponse(
            id =  10,
            mobile =  "",
            headImage =  "",
            nickName =  ""
        ))
        imageUrls.add(UserInfoResponse(
            id =  10,
            mobile =  "",
            headImage =  "",
            nickName =  ""
        ))
        mBinding.banner.setAdapter(BannerImageAdapter(imageUrls), true);
    }

    fun onClickScan() {
        LoginUtils.ensureLogin(activity) {
            startScanLauncher.launch(null);
        }
    }

    fun onClickFaBu() {
        LoginUtils.ensureLogin(activity) {
            val intent = Intent(activity, PublishBuyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleScanQrCode(qr: String) {
        Toast.makeText(requireActivity().applicationContext,"扫描结果," + qr, Toast.LENGTH_LONG).show()
        val intent = Intent(activity, ConfirmShuoHuoActivity::class.java)
        startActivity(intent)
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}