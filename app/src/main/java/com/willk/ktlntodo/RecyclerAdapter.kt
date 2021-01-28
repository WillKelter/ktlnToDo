package com.willk.ktlntodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.item.view.*

class RecyclerAdapter(private val mDataset: ArrayList<Task>, private val recClickListener: RecClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val checkBox:CheckBox = itemView.findViewById(R.id.checkBox)
            val bt_del:ImageButton = itemView.findViewById(R.id.bt_del)
            val cl_card:ConstraintLayout = itemView.findViewById(R.id.cl_card)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        holder.bt_del.visibility = View.INVISIBLE
        return ViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.text = mDataset[position].taskTtl
        holder.checkBox.isChecked = mDataset[position].isChecked
        holder.checkBox.isEnabled = !mDataset[position].isChecked
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked -> recClickListener.onCheck(holder.adapterPosition, holder.checkBox.isChecked)
        }
        holder.cl_card.setOnLongClickListener(View.OnLongClickListener {holder.bt_del.visibility = View.VISIBLE
            true })
        holder.bt_del.setOnClickListener(View.OnClickListener { recClickListener.onDel(holder.adapterPosition)
            holder.bt_del.visibility = View.INVISIBLE})
    }
}
