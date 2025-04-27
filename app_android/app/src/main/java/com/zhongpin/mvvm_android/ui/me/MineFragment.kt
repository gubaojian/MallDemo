package com.zhongpin.mvvm_android.ui.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureActivity
import com.scwang.smart.refresh.header.ClassicsHeader
import com.zhongpin.app.databinding.FragmentMineBinding
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.mvvm_android.base.view.BaseVMFragment
import com.zhongpin.mvvm_android.bean.LoginEvent
import com.zhongpin.mvvm_android.bean.UserInfoResponse
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.ui.login.LoginActivity
import com.zhongpin.mvvm_android.ui.photo.preview.PhonePreviewerActivity
import com.zhongpin.mvvm_android.ui.scan.ScanCaptureActivity
import com.zhongpin.mvvm_android.ui.verify.company.CompanyVerifyActivity
import com.zhongpin.mvvm_android.ui.verify.person.PersonVerifyActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@EventBusRegister
class MineFragment : BaseVMFragment<MineViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    private lateinit var binding: FragmentMineBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun createContentViewByBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentMineBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }


    override fun initView() {
        super.initView()
        StatusBarUtil.setMargin(activity, binding.content)

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

        binding.photo.setOnClickListener {
            val intent = Intent(activity, PhonePreviewerActivity::class.java)
            intent.putExtra("imageUrls", arrayOf<String>(
                "https://img.alicdn.com/bao/uploaded/i2/2268175280/O1CN01wvhOlY1osIBwjOCOP_!!2268175280.jpg_460x460q90.jpg_.webp",
                "https://img.alicdn.com/bao/uploaded/i3/3928142771/O1CN01tzdOzK1WLANtDlnTJ_!!3928142771.jpg_460x460q90.jpg_.webp"))
            startActivity(intent)
        }

        binding.scan.setOnClickListener {
            val intent = Intent(activity, CaptureActivity::class.java)
            startActivityForResult(intent, 100)
        }

        binding.scan2.setOnClickListener {
            val intent = Intent(activity, ScanCaptureActivity::class.java)
            startActivityForResult(intent, 100)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity!!.applicationContext,"扫描结果," + data?.getStringExtra(Intents.Scan.RESULT), Toast.LENGTH_LONG).show()

        }
    }

    override fun initData() {
        mViewModel.getUserInfoCo()
        /**
        Handler().postDelayed({
            mViewModel.loadSeckillGoodsData()
        }, 2000)*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(loginEvent : LoginEvent){
        if (loginEvent.isLogin) {
            initData();
        }
    }


    override fun initDataObserver() {
        mViewModel.mUserInfoData.observe(viewLifecycleOwner, Observer {
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

    private fun setBannerData(list: UserInfoResponse) {
        binding.text.setText("" + list.toString() + " \n " +   param1 + "test" )
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
            MineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}