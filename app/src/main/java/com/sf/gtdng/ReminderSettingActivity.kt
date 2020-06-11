package com.sf.gtdng

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sf.gtdng.broadcast.AlarmReceiver
import com.sf.gtdng.fragment.DatePickerDialogFragment
import com.sf.gtdng.fragment.TimePickerDialogFragment
import kotlinx.android.synthetic.main.activity_setting_reminder.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created By Fahmi on 11/06/20
 */

class ReminderSettingActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialogFragment.DialogDateListener, TimePickerDialogFragment.DialogTimeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_reminder)

        alarmReceiver = AlarmReceiver()

        btn_once_date.setOnClickListener(this)
        btn_once_time.setOnClickListener(this)
        btn_set_once_alarm.setOnClickListener(this)
        btn_cancel_repeating_alarm.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_once_date -> {
                val datePickerFragment = DatePickerDialogFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_once_time -> {
                val timePickerFragmentOne = TimePickerDialogFragment()
                timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_set_once_alarm -> {
                val onceDate = tv_once_date.text.toString()
                val onceTime = tv_once_time.text.toString()
                val onceMessage = edt_once_message.text.toString()
                alarmReceiver.setOneTimeAlarm(
                    this,
                    AlarmReceiver.TYPE_ONE_TIME,
                    onceDate,
                    onceTime,
                    onceMessage
                )
            }
            R.id.btn_cancel_repeating_alarm -> alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_ONE_TIME)
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        tv_once_date.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (tag) {
            TIME_PICKER_ONCE_TAG -> tv_once_time.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }
}