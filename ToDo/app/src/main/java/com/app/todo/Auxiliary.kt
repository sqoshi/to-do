package com.app.todo

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.app.todo.notifications.NotificationReceiver
import java.util.*

class Auxiliary {
    companion object {


        enum class SortType {
            TYPE, DATE, NAME
        }

        /**
         * Return calendar with specified date.
         */
        fun getCalendarWithDate(
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int
        ): Calendar {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            return calendar
        }

        /**
         * set Up notification for updated task.
         */
        fun setUpNotification(
            activity: Activity?,
            hour: Int,
            minute: Int,
            day: Int,
            month: Int,
            year: Int
        ) {
            val calendar: Calendar = getCalendarWithDate(year, month, day, hour, minute)

            if (calendar.time < Date()) calendar.add(Calendar.DAY_OF_MONTH, 1)
            val intent = Intent(activity?.applicationContext, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                activity?.applicationContext,
                (0..2147483647).random(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager =
                activity?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }


        /**
         * Display dialog within user can choose icon for a new task.
         */
        fun displayDialog(context: Context, iconImageView: ImageView) {
            val dialogView: View =
                LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)


            val dialog: AlertDialog = AlertDialog.Builder(context)
                .setTitle("Choose icon")
                .setMessage("Set icon for task.")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .create()

            val o1: LinearLayout = dialogView.findViewById(R.id.pets_option)
            o1.setOnClickListener {
                dialogListItemOnClick(iconImageView, dialog, R.drawable.ic_baseline_pets_24)
                iconImageView.tag = "ic_baseline_pets_24"
            }
            val o2: LinearLayout = dialogView.findViewById(R.id.school_option)
            o2.setOnClickListener {
                dialogListItemOnClick(iconImageView, dialog, R.drawable.ic_baseline_school_24)
                iconImageView.tag = "ic_baseline_school_24"

            }
            val o3: LinearLayout = dialogView.findViewById(R.id.work_option)
            o3.setOnClickListener {
                dialogListItemOnClick(iconImageView, dialog, R.drawable.ic_baseline_work_24)
                iconImageView.tag = "ic_baseline_work_24"

            }
            val o4: LinearLayout = dialogView.findViewById(R.id.person_option)
            o4.setOnClickListener {
                dialogListItemOnClick(iconImageView, dialog, R.drawable.ic_baseline_person_24)
                iconImageView.tag = "ic_baseline_person_24"

            }
            dialog.show()
        }

        /**
         * When icon is clicked inside special dialog setup preview and hide dialog.
         */
        private fun dialogListItemOnClick(view: ImageView, dialog: AlertDialog, draw: Int) {
            view.setImageResource(draw)
            dialog.dismiss()
        }

        /**
         * Set xml elements' values to passed arguments.
         */
        fun setValues(
            resources: Resources,
            activity: Activity?,
            view: View?,
            datePicker: DatePicker?,
            timePicker: TimePicker?,
            iconImageView: ImageView?,
            editTextName: EditText?,
            editTextDesc: EditText?,
            date: Calendar,
            desc: String,
            type: String,
            name: String,
        ) {
            if (view != null) {
                datePicker?.init(
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH),
                    null
                )

                datePicker?.minDate = System.currentTimeMillis() - 1000


                timePicker?.setIs24HourView(true)
                timePicker?.hour = date.get(Calendar.HOUR_OF_DAY)
                timePicker?.minute = date.get(Calendar.MINUTE)


                editTextDesc?.setText(desc)
                editTextName?.setText(name)

                val resourceId = resources.getIdentifier(
                    type, "drawable",
                    activity?.packageName
                )
                iconImageView?.setImageResource(resourceId)
                iconImageView?.tag = type
            }

        }

        /**
         * Set up spinner and adding functionality as displaying dialog on element click.
         */
        fun installSpinner(
            context: Context,
            view: View,
            resources: Resources,
            spinnerId: Int,
            spinnerItem: Spinner,
            priorityInitial: String
        ) {
            val priorities = resources.getStringArray(R.array.priorities)

            val spinner = view.findViewById<Spinner>(spinnerId)//R.id.prioritySpinnerUpdate
            if (spinner != null) {
                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_item, priorities
                )
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?, position: Int, id: Long
                    ) {
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
                if (priorityInitial != "")
                    spinnerItem.setSelection(adapter.getPosition(priorityInitial))
                else
                    spinnerItem.setSelection(0)
            }
        }

    }
}