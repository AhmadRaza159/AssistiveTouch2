package com.example.assistivetouch

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterClass(var myList: List<ResolveInfo>, var cntx: Context, val clickInterface:InterfaceClass) :RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {
    class ViewHolderClass (itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_all_apps, parent, false)
        return ViewHolderClass(v)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        Glide.with(cntx).load(myList[position].loadIcon(cntx.packageManager)).into(holder.itemView.findViewById<ImageView>(R.id.item_all_app_img))
        holder.itemView.findViewById<TextView>(R.id.item_all_app_txt).text = myList[position].loadLabel(cntx.packageManager)
        holder.itemView.setOnClickListener{
            clickInterface.onClick(myList[position])
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }

}