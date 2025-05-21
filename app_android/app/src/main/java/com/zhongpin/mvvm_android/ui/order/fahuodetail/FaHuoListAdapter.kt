package com.zhongpin.mvvm_android.ui.order.fahuodetail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.zhongpin.app.R.*
import com.zhongpin.app.databinding.ListCompanyItemBinding
import com.zhongpin.app.databinding.ListFahuoItemBinding
import com.zhongpin.mvvm_android.bean.CompanyListItemResponse
import com.zhongpin.mvvm_android.ui.feedback.PingZhiFeedbackActivity

class FaHuoListAdapter(val mActivity: FaHuoDetailActivity, data: MutableList<CompanyListItemResponse>)
    : BaseQuickAdapter<CompanyListItemResponse, FaHuoListAdapter.VH>(data) {


    override fun onBindViewHolder(
        holder: FaHuoListAdapter.VH,
        position: Int,
        item: CompanyListItemResponse?
    ) {

        item?.let {
            /**
            holder.binding.apply {
                name.text = it?.name ?: ""
                address.text = it?.address ?: ""
                legal.text = it?.legal ?: ""
                if (it.status == 0) {
                    statusText.text = "待审核"
                    statusText.setTextColor(Color.parseColor("#FFA826"))
                    statusText.setBackgroundResource(drawable.bg_company_verify_status_wait)
                } else if (it.status == 1) {
                    statusText.text = "已认证"
                    statusText.setTextColor(Color.parseColor("#57C248"))
                    statusText.setBackgroundResource(drawable.bg_company_verify_status_ok)
                } else if (it.status == 2) {
                    statusText.text = "认证失败"
                    statusText.setTextColor(Color.parseColor("#D34545"))
                    statusText.setBackgroundResource(drawable.bg_company_verify_status_failed)
                } else {
                    statusText.text = "已认证"
                    statusText.setTextColor(Color.parseColor("#57C248"))
                    statusText.setBackgroundResource(drawable.bg_company_verify_status_ok)
                }
            }*/
            holder.binding.feedbackButton.setOnClickListener {
                val intent = Intent(mActivity, PingZhiFeedbackActivity::class.java)
                mActivity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): FaHuoListAdapter.VH {
        return VH(parent)
    }

    class VH(
        parent: ViewGroup,
        val binding: ListFahuoItemBinding = ListFahuoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

}