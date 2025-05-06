package com.zhongpin.mvvm_android.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.app.databinding.FragmentHomeBinding
import com.zhongpin.lib_base.utils.ActivityStackManager
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.lib_base.utils.EventBusUtils
import com.zhongpin.mvvm_android.MainActivity
import com.zhongpin.mvvm_android.bean.LoginEvent
import com.zhongpin.mvvm_android.bean.TokenExpiredEvent
import com.zhongpin.mvvm_android.common.login.LoginUtils
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
class HomeFragment : BaseVMFragment<HomeFragmentViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    private lateinit var binding: FragmentHomeBinding;

    private var data: MutableList<HomeItemEntity> = mutableListOf()
    private lateinit var listAdapter: HomeListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }


    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(activity, binding.content)

        listAdapter = HomeListAdapter(mActivity, data)

        binding.refreshLayout.setEnableRefresh(true)
        binding.refreshLayout.setRefreshHeader(ClassicsHeader(getContext()))
        binding.refreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding.refreshLayout.finishRefresh()
                },
                2000
            )
        }

        binding.mainRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.mainRecyclerView.adapter = listAdapter

        registerDefaultLoad(binding.refreshLayout, Constant.COMMON_KEY)
    }

    override fun initData() {
        super.initData()
        mViewModel.loadBannerCo()
        /**
        Handler().postDelayed({
            mViewModel.loadSeckillGoodsData()
        }, 2000)*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(loginEvent : LoginEvent){
        initData();
    }

    override fun initDataObserver() {
        mViewModel.mBannerData.observe(viewLifecycleOwner, Observer {
            showSuccess(Constant.COMMON_KEY)
            it?.let {
                setBannerData(it)
            }
            //showLoading("dddd")
            //showError("dddd", "dddd")
        })

        val nameObserver = Observer<String> { it ->
            // Update the UI, in this case, a TextView.
            //nameTextView.text = newName
        }
        /**
        mViewModel.mSeckillGoods.observe(viewLifecycleOwner, Observer {
            showSuccess(Constant.COMMON_KEY)
            mHAdapter.setNewData(it)
            mainRefreshLayout.finishRefresh()
        })*/
    }

    private fun setBannerData(list: List<UserInfoResponse>) {
        if (data.isEmpty() || (data.get(0).type != 1)) {
            data.add(0, HomeItemEntity(list, 1))
            listAdapter.notifyItemInserted(0)
        } else {
            data.set(0, HomeItemEntity(list, 1))
            listAdapter.notifyItemChanged(0)
        }
        binding.text.setText("" + list.toString() + " \n " +   param1 )
        //mHeaderView.mBanner.adapter = BannerImageAdapter(list)
        //mHeaderView.mBanner.addBannerLifecycleObserver(this)
        //mHeaderView.mBanner.indicator = CircleIndicator(activity)
        //mHeaderView.mBanner.setBannerRound2(20f)
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