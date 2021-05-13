package com.app.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.todo.Auxiliary.Companion.displayDialog
import com.app.todo.Auxiliary.Companion.getCalendarWithDate
import com.app.todo.Auxiliary.Companion.installSpinner
import com.app.todo.Auxiliary.Companion.setUpNotification
import com.app.todo.Auxiliary.Companion.setValues
import com.app.todo.R
import com.app.todo.model.Task
import com.app.todo.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.util.*

/**
 * Responsible for editing existing in database task.
 */
class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view?.iconImageViewUpdate?.tag = args.currentTask.type
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)


        view.iconImageViewUpdate.setOnClickListener {
            displayDialog(requireContext(), view.iconImageViewUpdate)
        }

        view.buttonUpdate.setOnClickListener {
            updateItem()
        }

        setHasOptionsMenu(true)
        view.timePickerUpdate.setIs24HourView(true)

        installSpinner(
            requireContext(),
            view,
            resources,
            R.id.prioritySpinnerUpdate,
            view.prioritySpinnerUpdate,
            args.currentTask.priority
        )
        return view
    }

    /**
     * Saves data on orientation change.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (iconImageViewUpdate != null &&
            datePickerUpdate != null &&
            timePickerUpdate != null &&
            editTextUpdateDesc != null &&
            editTextUpdateName != null &&
            prioritySpinnerUpdate != null
        ) {
            outState.putString("type", iconImageViewUpdate.tag.toString())
            outState.putInt("year", datePickerUpdate.year)
            outState.putInt("month", datePickerUpdate.month)
            outState.putInt("day", datePickerUpdate.dayOfMonth)
            outState.putInt("hour", timePickerUpdate.hour)
            outState.putInt("minute", timePickerUpdate.minute)
            outState.putString("desc", editTextUpdateDesc.text.toString())
            outState.putString("name", editTextUpdateName.text.toString())
            outState.putString("priority", prioritySpinnerUpdate.selectedItem.toString())
        }
    }

    /**
     * Loads data on orientation change.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            setValues(
                resources,
                activity,
                view,
                view?.datePickerUpdate,
                view?.timePickerUpdate,
                view?.iconImageViewUpdate,
                view?.editTextUpdateName,
                view?.editTextUpdateDesc,
                getCalendarWithDate(
                    savedInstanceState.getInt("year"),
                    savedInstanceState.getInt("month"),
                    savedInstanceState.getInt("day"),
                    savedInstanceState.getInt("hour"),
                    savedInstanceState.getInt("minute"),
                ),
                savedInstanceState.getString("desc").toString(),
                savedInstanceState.getString("type").toString(),
                savedInstanceState.getString("name").toString()
            )
        } else {
            setValues(
                resources,
                activity,
                view,
                view?.datePickerUpdate,
                view?.timePickerUpdate,
                view?.iconImageViewUpdate,
                view?.editTextUpdateName,
                view?.editTextUpdateDesc,
                getCalendarWithDate(
                    args.currentTask.date.get(Calendar.YEAR),
                    args.currentTask.date.get(Calendar.MONTH),
                    args.currentTask.date.get(Calendar.DAY_OF_MONTH),
                    args.currentTask.date.get(Calendar.HOUR_OF_DAY),
                    args.currentTask.date.get(Calendar.MINUTE),
                ),
                args.currentTask.description,
                args.currentTask.type,
                args.currentTask.name,
            )
        }
    }


    /**
     * Updates task in database after providing changes.
     */
    private fun updateItem() {
        val name = editTextUpdateName.text.toString()
        val desc = editTextUpdateDesc.text.toString()
        val type = iconImageViewUpdate.tag.toString()
        val priority = prioritySpinnerUpdate.selectedItem.toString()

        val year = datePickerUpdate.year
        val month = datePickerUpdate.month
        val day = datePickerUpdate.dayOfMonth
        val hour = timePickerUpdate.hour
        val minute = timePickerUpdate.minute
        val calendar: Calendar = getCalendarWithDate(year, month, day, hour, minute)


        if (inputCheck(name, desc, type)) {
            val updatedTask =
                Task(
                    args.currentTask.id,
                    name = name,
                    description = desc,
                    type = type,
                    priority = priority,
                    date = calendar
                )

            mTaskViewModel.updateTask(updatedTask)
            setUpNotification(
                activity = activity,
                year = year,
                month = month,
                day = day,
                hour = hour,
                minute = minute - 1
            )
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Successfully updated task", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(requireContext(), "Please, change any field.", Toast.LENGTH_SHORT).show()

    }

    /**
     * Check if data passed by user is correct and enough to insert.
     */
    private fun inputCheck(name: String, desc: String, type: String): Boolean {
        return (args.currentTask.description != desc || args.currentTask.name != name || args.currentTask.type != type)
    }

    /**
     * Add menu icon to delete current task.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    /**
     * On menu `delete` option click adding task deletion functionality.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteTask()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Remove task from a database.
     */
    private fun deleteTask() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTaskViewModel.deleteTask(task = args.currentTask)
            Toast.makeText(
                requireContext(),
                "Successfully removed ${args.currentTask.name}.",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setTitle("Delete ${args.currentTask.name}")
        builder.setMessage("Are you sure you want to delete ${args.currentTask.name}?")
        builder.create()
        builder.show()
    }


}