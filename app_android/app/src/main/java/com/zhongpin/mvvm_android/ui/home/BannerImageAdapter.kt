package com.zhongpin.mvvm_android.ui.home

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils
import com.zhongpin.app.R.*
import com.zhongpin.mvvm_android.bean.UserInfoResponse

class BannerImageAdapter (imageUrls: List<UserInfoResponse>) : BannerAdapter<UserInfoResponse, BannerImageAdapter.ImageHolder>(imageUrls)  {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        //通过裁剪实现圆角
        BannerUtils.setBannerRound(imageView, SizeUtils.dp2px(16.0f).toFloat())
        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder?, data: UserInfoResponse?, position: Int, size: Int) {
        Glide.with(holder!!.itemView)
            .load(mipmap.ic_home_banner)
            .placeholder(mipmap.ic_home_banner)
            .error(mipmap.ic_home_banner)
            .into(holder.imageView)
    }


    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view as ImageView
    }
}