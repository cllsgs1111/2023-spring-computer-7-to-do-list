package com.example.todomanagement.util

import android.graphics.Paint
import android.os.Build
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.todomanagement.database.Category
import com.example.todomanagement.database.Task
import com.example.todomanagement.ui.oneday.OneDayAdapter
import com.example.todomanagement.ui.overview.OverviewAdapter
import com.example.todomanagement.ui.overview.OverviewFragment
import com.example.todomanagement.ui.tasklist.TaskListAdapter
import com.example.todomanagement.ui.tasklist.TaskListFragment

/**
 *设置数据绑定，其中app：overview_item在布局代码中引用为名，具体见布局代码,只能给overview布局使用
 */
@BindingAdapter("app:overview_items")
fun setItemsInOverview(listView: RecyclerView, items: List<Task>?) {
    items?.let {
        (listView.adapter as OverviewAdapter).submitList(items)
    }
}

/**
 *设置recycler view数据绑定
 */
@BindingAdapter("app:oneday_items")
fun setItemsInOneDay(listView: RecyclerView, items: List<Task>?) {
    items?.let {
        (listView.adapter as OneDayAdapter).submitList(items)
    }
}

/**
 *设置recycler view数据绑定
 */
@BindingAdapter("app:tasklist_items")
fun setItemsInTaskList(listView: RecyclerView, items: List<Category>?) {
    items?.let {
        (listView.adapter as TaskListAdapter).submitList(items)
    }
}

@BindingAdapter("app:completedTask")
fun setStyle(textView: TextView, enabled: Boolean) {
    if (enabled) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

/**
 * 长按删除task item
 */
@RequiresApi(Build.VERSION_CODES.R)
@BindingAdapter("app:onLongClick_task")
fun LinearLayout.setOnLongClickListener(item: Task) {
    setOnLongClickListener {
        it.findFragment<OverviewFragment>().deleteTask(item)
        return@setOnLongClickListener true
    }
}

/**
 * 长按删除category
 */
@BindingAdapter("app:onLongClick_category")
fun TextView.setOnLongClickListener(item: Category) {
    setOnLongClickListener {
        //it.findFragment<TaskListFragment>().deleteTask(item)
        return@setOnLongClickListener true
    }
}

@BindingAdapter("app:oneday_time_string")
fun TextView.setSleepQualityString(item: Task) {
    text = Converter.formatDateTimeStringOneDay(item.endTimeMilli)
}

