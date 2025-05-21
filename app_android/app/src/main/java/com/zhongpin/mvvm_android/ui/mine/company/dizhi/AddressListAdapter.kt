package com.zhongpin.mvvm_android.ui.mine.company.dizhi

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.zhongpin.app.databinding.ListAddressItemBinding
import com.zhongpin.mvvm_android.bean.AddressListItemResponse
import com.zhongpin.mvvm_android.ui.utils.AreaUtil

fun interface OnDeleteItemCallback {
    fun  onDelete(position: Int, item: AddressListItemResponse)
}

fun interface OnEditItemCallback {
    fun  onEdit(position: Int, item: AddressListItemResponse)
}

class AddressListAdapter(val mActivity: AppCompatActivity,
                         val onDeleteItemAction : OnDeleteItemCallback,
                         val onEditItemAction : OnEditItemCallback,
                         data: MutableList<AddressListItemResponse>)
    : BaseQuickAdapter<AddressListItemResponse, AddressListAdapter.VH>(data) {


    override fun onBindViewHolder(
        holder: AddressListAdapter.VH,
        position: Int,
        item: AddressListItemResponse?
    ) {
        item?.let {
            holder.binding.apply {
                var shortAdressText = it?.abbr;
                if (TextUtils.isEmpty(shortAdressText )) {
                     shortAdressText  = it.address
                }
                if (TextUtils.isEmpty(shortAdressText )) {
                    shortAdressText  = "收货地址简称"
                }
                shortAdress.text = shortAdressText
                area.text = AreaUtil.toArea(it.province, it.city, it.region)
                detailAddress.text = it?.address ?: ""
                jingWeiDu.text = "${it.longitude} ${it.latitude}"
                name.text = it.name ?: ""
                mobile.text = it.mobile ?: ""
                deleteButtonContainer.setOnClickListener {
                    onDeleteItemAction.onDelete(position, item)
                }
                editButtonContainer.setOnClickListener {
                    onEditItemAction.onEdit(position, item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): AddressListAdapter.VH {
        return VH(parent)
    }

    class VH(
        parent: ViewGroup,
        val binding: ListAddressItemBinding = ListAddressItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

}