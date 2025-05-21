package com.zhongpin.mvvm_android.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.zhongpin.app.databinding.HomeHeaderViewItemBinding
import com.zhongpin.app.databinding.ListHomeFabuScanItemBinding
import com.zhongpin.app.databinding.ListHomeNewsContentItemBinding
import com.zhongpin.app.databinding.ListHomeNewsTitleItemBinding
import com.zhongpin.app.databinding.ListHomePingtaiDataItemBinding

class HomeListAdapter(val mFragment: HomeFragment, val mData: MutableList<HomeItemEntity>)
    : BaseMultiItemAdapter<HomeItemEntity>(mData) {

    class BuyScanVH(
        parent: ViewGroup,
        val binding: ListHomeFabuScanItemBinding = ListHomeFabuScanItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    class PingTaiDataVH(
        parent: ViewGroup,
        val binding: ListHomePingtaiDataItemBinding = ListHomePingtaiDataItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    class NewsTitleVH(
        parent: ViewGroup,
        val binding: ListHomeNewsTitleItemBinding = ListHomeNewsTitleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    class NewsItemVH(
        parent: ViewGroup,
        val binding: ListHomeNewsContentItemBinding = ListHomeNewsContentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    class BannerVH(
        parent: ViewGroup,
        val binding: HomeHeaderViewItemBinding = HomeHeaderViewItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    init {
        addItemType(BUY_SCAN_TYPE,  object : OnMultiItemAdapterListener<HomeItemEntity, BuyScanVH> {
                override fun onBind(holder: BuyScanVH, position: Int, item: HomeItemEntity?) {
                    holder.binding.scan.setOnClickListener {
                        mFragment.onClickScan();
                    }
                    holder.binding.fabu.setOnClickListener {
                        mFragment.onClickFaBu();
                    }
                }
                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): BuyScanVH {
                   return BuyScanVH(parent)
                }

            }
        ).addItemType(PINGTAI_DATA_TYPE,  object : OnMultiItemAdapterListener<HomeItemEntity, PingTaiDataVH> {
                override fun onBind(holder: PingTaiDataVH, position: Int, item: HomeItemEntity?) {

                }

                override fun onCreate(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): PingTaiDataVH {
                    return PingTaiDataVH(parent)
                }

          }
        ).addItemType(NEWS_TITLE_TYPE,  object : OnMultiItemAdapterListener<HomeItemEntity, NewsTitleVH> {
            override fun onBind(holder: NewsTitleVH, position: Int, item: HomeItemEntity?) {

            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): NewsTitleVH {
                return NewsTitleVH(parent)
            }

        }
        ).addItemType(NEWS_ITEM_TYPE,  object : OnMultiItemAdapterListener<HomeItemEntity, NewsItemVH> {
            override fun onBind(holder: NewsItemVH, position: Int, item: HomeItemEntity?) {

            }
            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): NewsItemVH {
                return NewsItemVH(parent)
            }

        }
        ).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            list[position].type
        }
    }


    companion object {
        const val BUY_SCAN_TYPE = 0
        const val PINGTAI_DATA_TYPE = 1
        const val NEWS_TITLE_TYPE = 2
        const val NEWS_ITEM_TYPE = 3
        const val IMAGE_TYPE = 10
    }

}