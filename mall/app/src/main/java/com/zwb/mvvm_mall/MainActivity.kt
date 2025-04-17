package com.zwb.mvvm_mall

import android.util.SparseArray
import android.view.MenuItem
import android.view.Window
import androidx.fragment.app.Fragment
import com.zwb.mvvm_mall.R.*
import com.zwb.mvvm_mall.base.view.BaseActivity
import com.zwb.mvvm_mall.common.utils.Constant
import com.zwb.mvvm_mall.databinding.ActivityMainBinding
import com.zwb.mvvm_mall.ui.test.SettingFragment

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


    override val layoutId  = layout.activity_main
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
    }

    private fun initBottomNavigation() {
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
                //Constant.HOME -> fragment = HomeFragment.newInstance()
                //Constant.CLASSIFY -> fragment = ClassifyFragment.newInstance()
                //Constant.CART -> fragment = CartFragment.newInstance()
                //Constant.MINE -> fragment = MineFragment.newInstance()
            }

            if (fragment == null) {
                fragment = SettingFragment.newInstance("", "")
            }

            mFragmentSparseArray.put(index, fragment)
        }
        return fragment!!
    }
}
