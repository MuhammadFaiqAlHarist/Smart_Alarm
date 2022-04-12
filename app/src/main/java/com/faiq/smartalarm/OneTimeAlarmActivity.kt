package com.faiq.smartalarm

import AlarmService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.faiq.data.Alarm
import com.faiq.data.local.AlarmDB
import com.faiq.data.local.AlarmDao
import com.faiq.smartalarm.databinding.ActivityOneTimeAlarmBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OneTimeAlarmActivity : AppCompatActivity(), DatePickerFragment.DateDialogListener,
    TimePickerFragment.TimeDialogListener {

    private var _binding: ActivityOneTimeAlarmBinding? = null
    private val binding get() = _binding as ActivityOneTimeAlarmBinding

    private var alarmDao: AlarmDao? = null

    private var _alarmService: AlarmService? = null
    private val alarmService get() = _alarmService as AlarmService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOneTimeAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(this)
        alarmDao = db.alarmDao()

        _alarmService = AlarmService()
        initView()
    }

    private fun initView() {
        binding.apply {

            btnSetDateOneTime.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, "DatePickerDialog")
            }


            btnSetTimeOneTime.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, "DatePickerDialog")
            }

            btnAddSetOneTimeAlarm.setOnClickListener {
                val date = tvOnceDate.text.toString()
                val time = tvOnceTime.text.toString()
                val message = tvNoteOneTime.text.toString()


                if (date != "Date" && time != "Time") {
                    alarmService.setOneTimeAlarm(applicationContext, 1, date, time, message)
                    CoroutineScope(Dispatchers.IO).launch {
                        alarmDao?.addAlarm(
                            Alarm(
                                0,
                                date,
                                time,
                                message,
                                AlarmService.TYPE_ONE_TIME
                            )
                        )
                        Log.i("AddAlarm", "Success set alarm on $date $time with message $message")
                        finish()
                    }
                } else {
                    Toast.makeText(applicationContext, "Set Your Date & Time", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    override fun onDialogSet(tag: String?, year: Int, month: Int, dayofMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayofMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.tvOnceDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String, hourOfDay: Int, minute: Int) {
        binding.tvOnceTime.text = timeFormatter(hourOfDay, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}