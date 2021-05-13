package com.app.todo.fragments.list

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.todo.R
import com.app.todo.model.Task
import kotlinx.android.synthetic.main.task_row.view.*
import java.util.*


/**
 * Adapter for list.
 * List elements can be displayed as specified in this class.
 * We have special `row.xml` file that specify style for a row of a task list.
 * Class filling template `row.xml` with values from database. ( currItem)
 */
class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var taskList = emptyList<Task>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    /**
     * Add functionality and data to row template for each element in list.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currItem = taskList[position]
        holder.itemView.taskName.text = currItem.name
        holder.itemView.textViewDesc.text = currItem.description
        holder.itemView.textViewDate.text =
            addZeroPrefix(currItem.date.get(Calendar.DAY_OF_MONTH).toString()) +
                    "/" + addZeroPrefix(currItem.date.get(Calendar.MONTH).toString()) +
                    "/" + addZeroPrefix(currItem.date.get(Calendar.YEAR).toString())

        holder.itemView.textViewTime.text = currItem.date.get(Calendar.HOUR_OF_DAY).toString() +
                ":" + addZeroPrefix(currItem.date.get(Calendar.MINUTE).toString())


        setBackgroundColor(holder.itemView, currItem.priority)
        val resources: Resources = holder.itemView.context.resources
        val resourceId = resources.getIdentifier(
            currItem.type, "drawable",
            holder.itemView.context.packageName
        )

        holder.itemView.iconRowImageView.setImageResource(resourceId)

        holder.itemView.rowLayout.setOnLongClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currItem)
            holder.itemView.findNavController().navigate(action)

            true
        }

    }

    /**
     * Functionality to change color of list- element background.
     */
    private fun setBackgroundColor(itView: View, priority: String) {
        when {
            priority.toUpperCase(Locale.getDefault()) == "HIGH" -> {
                itView.background.setColorFilter(
                    Color.parseColor("#293250"),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            priority.toUpperCase(Locale.getDefault()) == "MEDIUM" -> {
                itView.background.setColorFilter(
                    Color.parseColor("#FFD55A"),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
            priority.toUpperCase(Locale.getDefault()) == "LOW" -> {
                itView.background.setColorFilter(
                    Color.parseColor("#6DD47E"),
                    PorterDuff.Mode.SRC_ATOP
                )

            }
        }

    }

    /**
     * Append data to adapter.
     */
    fun setData(task: List<Task>) {
        this.taskList = task
        notifyDataSetChanged()
    }

    /**
     * If string has length equal to 1, then adds zero prefix to string.
     */
    private fun addZeroPrefix(string: String): String {
        return if (string.length == 1)
            "0$string"
        else
            string
    }


}