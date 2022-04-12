package com.faiq.smartalarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.faiq.data.Alarm
import com.faiq.smartalarm.databinding.RowItemAlarmBinding

class AlarmAdapter:
    RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

    var listAlarm: ArrayList<Alarm> = arrayListOf()

    inner class MyViewHolder(val binding: RowItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply {
            itemDateAlarm.text = alarm.date
            itemTimeAlarm.text = alarm.time
            itemNoteAlarm.text = alarm.message
        }
    }

    //    override fun getItemCount() = listAlarm.size
//
////    fun setData(list: List<Alarm>){
////        listAlarm.clear()
////        listAlarm.addAll(list)
////        notifyDataSetChanged()
////    }
//
//    }

    fun setData(list: List<Alarm>) {
        val alarmDiffUtil = AlarmDiffUtil(listAlarm, list)
        val alarmDiffUtilResult = DiffUtil.calculateDiff(alarmDiffUtil)
        listAlarm.clear()
        listAlarm.addAll(list)
        alarmDiffUtilResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = listAlarm.size

}

