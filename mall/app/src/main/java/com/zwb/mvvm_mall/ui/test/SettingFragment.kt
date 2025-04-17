package com.zwb.mvvm_mall.ui.test

import android.os.Bundle
import android.os.Handler
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youth.banner.indicator.CircleIndicator
import com.zwb.mvvm_mall.R
import com.zwb.mvvm_mall.base.view.BaseFragment
import com.zwb.mvvm_mall.bean.BannerResponse
import com.zwb.mvvm_mall.common.utils.Constant
import com.zwb.mvvm_mall.common.utils.StatusBarUtil
import com.zwb.mvvm_mall.databinding.ActivityMainBinding
import com.zwb.mvvm_mall.databinding.FragmentSettingBinding
import com.zwb.mvvm_mall.base.view.BaseVMFragment as BaseVMFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : BaseVMFragment<SettingViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSettingBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }


    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(activity, binding.content)
    }

    override fun initData() {
        mViewModel.loadBannerCo()
        /**
        Handler().postDelayed({
            mViewModel.loadSeckillGoodsData()
        }, 2000)*/
    }


    override fun initDataObserver() {
        mViewModel.mBannerData.observe(viewLifecycleOwner, Observer {
            setBannerData(it)
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

    private fun setBannerData(list: List<BannerResponse>) {
        binding.text.setText(list.toString())
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
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}