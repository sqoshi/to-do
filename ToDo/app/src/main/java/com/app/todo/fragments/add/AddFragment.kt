package com.app.todo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.todo.Auxiliary
import com.app.todo.Auxiliary.Companion.getCalendarWithDate
import com.app.todo.Auxiliary.Companion.displayDialog
import com.app.todo.Auxiliary.Companion.setUpNotification
import com.app.todo.Auxiliary.Companion.setValues
import com.app.todo.R
import com.app.todo.model.Task
import com.app.todo.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.util.*

/**
 * Fragment responsible for adding a new task to database.
 */
class AddFragment : Fragment() {
    private lateinit var mTaskViewModel: TaskViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View? = inflater.inflate(R.layout.fragment_add, container, false)
        if (view != null) {
            mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
            view.buttonAdd.setOnClickListener {
                insertDataToDatabase()
            }

            view.iconImageView.tag = "ic_baseline_school_24"

            view.iconImageView.setOnClickListener {
                displayDialog(requireContext(), view.iconImageView)
            }

            view.timePicker.setIs24HourView(true)
            view.datePicker.minDate = System.currentTimeMillis() - 1000

            Auxiliary.installSpinner(
                requireContext(),
                view,
                resources,
                R.id.prioritySpinner,
                view.prioritySpinner,
                ""
            )

        }
        return view

    }


    /**
     * Handling save on rotation change.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (iconImageView != null && datePicker != null && timePicker != null) {
            outState.putString("type", iconImageView.tag.toString())
            outState.putInt("year", datePicker.year)
            outState.putInt("month", datePicker.month)
            outState.putInt("day", datePicker.dayOfMonth)
            outState.putInt("hour", timePicker.hour)
            outState.putInt("minute", timePicker.minute)
            outState.putString("desc", editTextDesc.text.toString())
            outState.putString("name", editTextName.text.toString())
            outState.putString("priority", prioritySpinner.selectedItem.toString())
        }

    }

    /**
     * Handling load on rotation change.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            setValues(
                resources,
                activity,
                view,
                view?.datePicker,
                view?.timePicker,
                view?.iconImageView,
                view?.editTextName,
                view?.editTextDesc,
                getCalendarWithDate(
                    savedInstanceState.getInt("year"),
                    savedInstanceState.getInt("month"),
                    savedInstanceState.getInt("day"),
                    savedInstanceState.getInt("hour"),
                    savedInstanceState.getInt("minute"),
                ),
                savedInstanceState.getString("desc").toString(),
                savedInstanceState.getString("type").toString(),
                savedInstanceState.getString("name").toString(),
            )

        }

    }


    /**
     * Inserting a new Task to a database.
     */
    private fun insertDataToDatabase() {
        val desc = editTextDesc.text.toString()
        val name = editTextName.text.toString()
        val priority = prioritySpinner.selectedItem
        val type = iconImageView.tag.toString()
        val year = datePicker.year
        val month = datePicker.month + 1
        val day = datePicker.dayOfMonth
        val hour = timePicker.hour
        val minute = timePicker.minute

        val calendar: Calendar = getCalendarWithDate(year, month, day, hour, minute)

        if (inputCheck(name)) {
            val task = Task(
                0,
                name = name,
                description = desc,
                type = type,
                priority = priority.toString(),
                date = calendar
            )

            setUpNotification(
                activity = activity,
                year = year,
                month = month,
                day = day,
                hour = hour,
                minute = minute - 1
            )

            mTaskViewModel.addTask(task)
            Toast.makeText(requireContext(), "Task added.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else
            Toast.makeText(
                requireContext(),
                "Please input the name of the task.",
                Toast.LENGTH_SHORT
            ).show()
    }


    /**
     * Check if data passed by user is correct and enough to insert.
     */
    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }


}

