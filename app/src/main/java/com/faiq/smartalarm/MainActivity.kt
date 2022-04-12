package com.faiq.smartalarm

import AlarmService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faiq.data.Alarm
import com.faiq.data.local.AlarmDB
import com.faiq.data.local.AlarmDao
import com.faiq.smartalarm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var alarmDao: AlarmDao? = null
    private var alarmAdapter: AlarmAdapter? = null

    private var alarmService = AlarmService()

    override fun onResume() {
        super.onResume()
        alarmDao?.getAlarm()?.observe(this) {
            alarmAdapter?.setData(it)
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            val alarm = alarmDao?.getAlarm()
//            withContext(Dispatchers.Main) {
//                alarm?.let { alarmAdapter?.setData(it) }
//            }
//            Log.i("setAlarm", "getAlarm : alarm with $alarm")
//        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(applicationContext)
        alarmDao = db.alarmDao()

        alarmAdapter = AlarmAdapter()

        alarmService = AlarmService()

        initView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvReminderAlarm.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alarmAdapter
            swipeToDelete(this)
        }
    }

    private fun initView() {
        binding.apply {

            cvSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, OneTimeAlarmActivity::class.java))
            }
            cvSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, OneTimeAlarmActivity::class.java))
            }
            tcTimeClock.format24Hour
        }
        tv_date_today.format24Hour
    }

    //        getTimeToday()
//    }
//    private fun getTimeToday(){
//        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
//        val time = sdf.format(Calendar.getInstance().time)
//
//        binding.tvTimeToday.text = time
//    }
    private fun swipeToDelete(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteAlarm = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    deleteAlarm?.let { alarmDao?.deleteAlarm(it)
                    Log.i("DeleteAlarm", "onSwiped: deleteAlarm $deleteAlarm")
                    }
                }
                val alarmType = deleteAlarm?.type
                alarmType?.let { alarmService?.cancelAlarm(baseContext, it) }
                alarmAdapter?.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(recyclerView)
    }

}
// hapus notify item remove


