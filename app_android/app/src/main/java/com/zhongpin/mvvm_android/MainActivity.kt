package com.zhongpin.mvvm_android

import android.os.Bundle
import android.util.SparseArray
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import com.zhongpin.app.R
import com.zhongpin.app.R.*
import com.kongzue.tabbar.Tab
import com.kongzue.tabbar.TabBarView
import com.kongzue.tabbar.interfaces.OnTabChangeListener
import com.zhongpin.mvvm_android.base.view.BaseActivity
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.app.databinding.ActivityMainBinding
import com.zhongpin.mvvm_android.ui.test.SettingFragment
import com.zhongpin.mvvm_android.ui.home.HomeFragment
import com.zhongpin.mvvm_android.ui.me.MineFragment

//import com.zwb.mvvm_mall.ui.cart.view.CartFragment
//import com.zwb.mvvm_mall.ui.classify.view.ClassifyFragment
//import com.zwb.mvvm_mall.ui.home.view.HomeFragment
//import com.zwb.mvvm_mall.ui.mine.view.MineFragment
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity  : BaseActivity() {
    private var mLastIndex: Int = -1
    private val mFragmentSparseArray = SparseArray<Fragment>()
    // 当前显示的 fragment
    private var mCurrentFragment: Fragment? = null
    private var mLastFragment: Fragment? = null
    private lateinit var binding: ActivityMainBinding;


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        super.onCreate(savedInstanceState)
    }

    override fun setContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun initView() {
        super.initView()
        switchFragment(Constant.HOME)
        initBottomNavigation()
        initTabs()
    }

    private fun initTabs() {
        val tabs:ArrayList<Tab>  = ArrayList();
        tabs.add(Tab(this@MainActivity, "首页", R.mipmap.tab_home).setFocusIcon(this, R.mipmap.tab_home_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        tabs.add(Tab(this@MainActivity, "订单", R.mipmap.tab_order).setFocusIcon(this, R.mipmap.tab_order_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        tabs.add(Tab(this@MainActivity, "通知", R.mipmap.tab_notify).setFocusIcon(this, R.mipmap.tab_notify_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        tabs.add(Tab(this@MainActivity, "我的", R.mipmap.tab_my).setFocusIcon(this, R.mipmap.tab_my_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        binding.tabs.setTab(tabs)

        binding.tabs.setOnTabChangeListener(
            object: OnTabChangeListener {
                override fun onTabChanged(v: View?, index: Int): Boolean {switchFragment(index)
                  return false;
                }
            }
        )
    }

    private fun initBottomNavigation() {
        binding.navView.itemIconTintList = null
        binding.navView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                id.menu_home -> {
                    switchFragment(Constant.HOME)
                    return@setOnNavigationItemSelectedListener true
                }
                id.menu_classify -> {
                    switchFragment(Constant.CLASSIFY)
                    return@setOnNavigationItemSelectedListener true
                }
                id.menu_cart -> {
                    switchFragment(Constant.CART)
                    return@setOnNavigationItemSelectedListener true
                }
                id.menu_mine -> {
                    switchFragment(Constant.MINE)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun switchFragment(index: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        // 将当前显示的fragment和上一个需要隐藏的fragment分别加上tag, 并获取出来
        // 给fragment添加tag,这样可以通过findFragmentByTag找到存在的fragment，不会出现重复添加
        mCurrentFragment = fragmentManager.findFragmentByTag(index.toString())
        mLastFragment = fragmentManager.findFragmentByTag(mLastIndex.toString())
        // 如果位置不同
        if (index != mLastIndex) {
            if (mLastFragment != null) {
                transaction.hide(mLastFragment!!)
            }
            if (mCurrentFragment == null) {
                mCurrentFragment = getFragment(index)
                transaction.add(id.content, mCurrentFragment!!, index.toString())
            } else {
                transaction.show(mCurrentFragment!!)
            }
        }

        // 如果位置相同或者新启动的应用
        if (index == mLastIndex) {
            if (mCurrentFragment == null) {
                mCurrentFragment = getFragment(index)
                transaction.add(id.content, mCurrentFragment!!, index.toString())
            }
        }
        transaction.commit()
        mLastIndex = index
    }

    private fun getFragment(index: Int): Fragment {
        var fragment: Fragment? = mFragmentSparseArray.get(index)
        if (fragment == null) {
            when (index) {
                Constant.HOME -> fragment = HomeFragment.newInstance("home")
                Constant.CLASSIFY -> fragment = SettingFragment.newInstance("2")
                Constant.CART -> fragment = SettingFragment.newInstance("3")
                Constant.MINE -> fragment = MineFragment.newInstance("mine")
            }

            if (fragment == null) {
                fragment = SettingFragment.newInstance("1")
            }

            mFragmentSparseArray.put(index, fragment)
        }
        return fragment!!
    }
}
