package com.zwb.mvvm_mall.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.youth.banner.indicator.CircleIndicator
import com.zwb.mvvm_mall.bean.BannerResponse
import com.zwb.mvvm_mall.databinding.HomeHeaderViewItemBinding

class HomeListAdapter(val mActivity: AppCompatActivity, data: MutableList<HomeItemEntity>)
    : BaseQuickAdapter<HomeItemEntity, HomeListAdapter.VH>(data) {


    override fun onBindViewHolder(
        holder: HomeListAdapter.VH,
        position: Int,
        item: HomeItemEntity?
    ) {
        item?.let {
            if (it.type == 1) {
                holder.binding.apply {
                    mBanner.setIndicator(CircleIndicator(mActivity))
                    mBanner.setAdapter(
                        GlideBannerImageAdapter(
                        it.data as List<BannerResponse>
                       )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): HomeListAdapter.VH {
        return VH(parent)
    }

    class VH(
        parent: ViewGroup,
        val binding: HomeHeaderViewItemBinding = HomeHeaderViewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

}