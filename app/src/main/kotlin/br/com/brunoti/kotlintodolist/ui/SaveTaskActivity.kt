package br.com.brunoti.kotlintodolist.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.brunoti.kotlintodolist.R.string.label_new_reminder
import br.com.brunoti.kotlintodolist.R.string.label_edit_reminder
import br.com.brunoti.kotlintodolist.databinding.ActivitySaveTaskBinding
import br.com.brunoti.kotlintodolist.datasource.ReminderDataSource
import br.com.brunoti.kotlintodolist.extensions.format
import br.com.brunoti.kotlintodolist.extensions.text
import br.com.brunoti.kotlintodolist.model.Reminder
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class SaveTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaveTaskBinding
    private val builderDatePicker = MaterialDatePicker.Builder.datePicker()
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaveTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (intent.hasExtra(REMINDER_ID)) {
            val reminderId = intent.getIntExtra(REMINDER_ID, 0)

            ReminderDataSource.findById(reminderId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
                binding.btnSaveTask.setText(label_edit_reminder)
            }
        } else {
            binding.btnSaveTask.setText(label_new_reminder)
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {

            if (binding.tilDate.text != "") {
                val date = SimpleDateFormat("dd/MM/yyyy").parse(binding.tilDate.text)

                builderDatePicker.setSelection(date?.time)
            }
            datePicker = builderDatePicker.build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1

                binding.tilDate.text = Date(it + offset).format()
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            var time: List<Int> = listOf(0, 0)

            if (binding.tilHour.text != "") {
                time = binding.tilHour.text.split(":").map { it.toInt() }
            }

            timePicker = MaterialTimePicker.Builder()
                .setHour(time[0])
                .setMinute(time[1])
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSaveTask.setOnClickListener {
            if (binding.tilTitle.text.isNotBlank()) {
                val reminder = Reminder(
                    title = binding.tilTitle.text,
                    date = binding.tilDate.text,
                    hour = binding.tilHour.text,
                    id = intent.getIntExtra(REMINDER_ID, 0)
                )

                ReminderDataSource.insertReminder(reminder)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                binding.mtvMessage.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val REMINDER_ID = "reminder_id"
    }
}
