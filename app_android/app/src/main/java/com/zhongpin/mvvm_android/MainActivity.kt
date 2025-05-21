package com.zhongpin.mvvm_android

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NotificationUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.kongzue.tabbar.Tab
import com.kongzue.tabbar.interfaces.OnTabChangeListener
import com.zhongpin.app.R
import com.zhongpin.app.R.*
import com.zhongpin.app.databinding.ActivityMainBinding
import com.zhongpin.lib_base.utils.ActivityStackManager
import com.zhongpin.lib_base.utils.EventBusRegister
import com.zhongpin.mvvm_android.base.view.BaseActivity
import com.zhongpin.mvvm_android.bean.LoginEvent
import com.zhongpin.mvvm_android.bean.TokenExpiredEvent
import com.zhongpin.mvvm_android.common.login.LoginUtils
import com.zhongpin.mvvm_android.common.utils.Constant
import com.zhongpin.mvvm_android.common.utils.StatusBarUtil
import com.zhongpin.mvvm_android.ui.home.HomeFragment
import com.zhongpin.mvvm_android.ui.me.MineFragment
import com.zhongpin.mvvm_android.ui.notify.NotifyListFragment
import com.zhongpin.mvvm_android.ui.order.OrderListFragment
import com.zhongpin.mvvm_android.ui.test.SettingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.String


//import com.zwb.mvvm_mall.ui.cart.view.CartFragment
//import com.zwb.mvvm_mall.ui.classify.view.ClassifyFragment
//import com.zwb.mvvm_mall.ui.home.view.HomeFragment
//import com.zwb.mvvm_mall.ui.mine.view.MineFragment
//import kotlinx.android.synthetic.main.activity_main.*

@EventBusRegister
class MainActivity  : BaseActivity() {
    private var mLastIndex: Int = -1
    private val mFragmentSparseArray = SparseArray<Fragment>()
    // 当前显示的 fragment
    private var mCurrentFragment: Fragment? = null
    private var mLastFragment: Fragment? = null
    private lateinit var binding: ActivityMainBinding;

    private var selectTab = Constant.HOME;


    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        if (intent != null) {
            selectTab = intent.getIntExtra("selectTab",Constant.HOME)
        }
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
        if (selectTab != Constant.HOME
            && selectTab >= 0
            && selectTab <= Constant.MINE) {
            mSharedViewModel.viewModelScope.launch {
                delay(300)
                val child =  binding.tabs.getChild(selectTab);
                child?.performClick()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshUser(tokenEvent : TokenExpiredEvent){
        if (tokenEvent.isExpired) {
            LoginUtils.clearToken()
            ActivityStackManager.backToSpecifyActivity(MainActivity::class.java)
            if (selectTab != Constant.HOME) {
                val homeTab = binding.tabs.getChild(0);
                homeTab?.performClick()
            }
            LoginUtils.toLogin(this)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(loginEvent: LoginEvent) {
        if (loginEvent.isLogin) {
            if (!NotificationUtils.areNotificationsEnabled()) {
                requestNotificationPermission()
            }
        }
    }


    private fun initTabs() {
        val tabs:ArrayList<Tab>  = ArrayList();
        tabs.add(Tab(this@MainActivity, "首页", com.zhongpin.app.R.mipmap.tab_home).setFocusIcon(this, com.zhongpin.app.R.mipmap.tab_home_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        tabs.add(Tab(this@MainActivity, "订单", com.zhongpin.app.R.mipmap.tab_order).setFocusIcon(this, com.zhongpin.app.R.mipmap.tab_order_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        tabs.add(Tab(this@MainActivity, "通知", com.zhongpin.app.R.mipmap.tab_notify).setFocusIcon(this, com.zhongpin.app.R.mipmap.tab_notify_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        tabs.add(Tab(this@MainActivity, "我的", com.zhongpin.app.R.mipmap.tab_my).setFocusIcon(this, com.zhongpin.app.R.mipmap.tab_my_current));            //使用 setFocusIcon(bitmap/drawable/resId) 来添加选中时的第二套图标
        binding.tabs.setTab(tabs)


        binding.tabs.setOnTabChangeListener(
            object: OnTabChangeListener {
                override fun onTabChanged(v: View?, index: Int): Boolean {
                    if (Constant.NOTIFY == index
                        || Constant.ORDER == index
                        || Constant.MINE == index) {
                        if (!LoginUtils.hasLogin()) {
                            LoginUtils.toLogin(this@MainActivity);
                            return true;
                        }
                    }
                    selectTab = index;
                    switchFragment(index)
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
                    switchFragment(Constant.ORDER)
                    return@setOnNavigationItemSelectedListener true
                }
                id.menu_cart -> {
                    switchFragment(Constant.NOTIFY)
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
                Constant.ORDER -> fragment = OrderListFragment.newInstance("order")
                Constant.NOTIFY -> fragment = NotifyListFragment.newInstance("notify")
                Constant.MINE -> fragment = MineFragment.newInstance("mine")
            }

            if (fragment == null) {
                fragment = SettingFragment.newInstance("1")
            }

            mFragmentSparseArray.put(index, fragment)
        }
        return fragment!!
    }

    private fun requestNotificationPermission() {
        XXPermissions.with(this@MainActivity)
            .permission(Permission.POST_NOTIFICATIONS)
            .request(OnPermissionCallback { permissions, allGranted ->
                if (!allGranted) {
                    return@OnPermissionCallback
                }
            })
    }
}
