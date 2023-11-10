package com.example.afteruts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.afteruts.Model.DataItem
import com.example.afteruts.databinding.ListPostAdapterBinding
import androidx.appcompat.widget.SearchView


data class MahasiswaAdapter(
    private var listPosts: List<DataItem?>?,
    private val onListItemClick: (DataItem?)-> Unit
): RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>(){

    inner class ViewHolder(val ListPostsAdapterBinding : ListPostAdapterBinding) :
        RecyclerView.ViewHolder(ListPostsAdapterBinding.root) {
        fun onBindItem(dataItem2: DataItem?){
            ListPostsAdapterBinding.nim.text = dataItem2?.nim.toString()
            ListPostsAdapterBinding.name.text = dataItem2?.namalengkap
            ListPostsAdapterBinding.datepost.text = dataItem2?.usia
            ListPostsAdapterBinding.gender.text = dataItem2?.gender
            ListPostsAdapterBinding.alamat.text = dataItem2?.alamat
            ListPostsAdapterBinding.root.setOnClickListener{
                onListItemClick(dataItem2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
           ListPostAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = listPosts?.get(position)
        dataItem?.let {
            holder.onBindItem(it)
        }
    }


    override fun getItemCount(): Int {
        return listPosts?.size ?: 0

    }


}