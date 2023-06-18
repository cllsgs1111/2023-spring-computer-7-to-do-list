package com.example.todomanagement.ui.tasklist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.todomanagement.R
import com.example.todomanagement.database.Category
import com.example.todomanagement.databinding.FragmentTasklistBinding
import com.example.todomanagement.util.EventObserver
import timber.log.Timber


class TaskListFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel

    private lateinit var binding: FragmentTasklistBinding

    private lateinit var listAdapter: TaskListAdapter

    private lateinit var rootView:View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(TaskListViewModel::class.java)
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_tasklist, container, false)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = this.viewModel

        binding.addTaskFab.setOnClickListener {
            inputCategory()
        }
        rootView =
            LayoutInflater.from(context).inflate(R.layout.fragment_tasklist, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setupListAdapter()
        //setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.openOverviewEvent.observe(viewLifecycleOwner, EventObserver {
            openOverviewFragment(it)
        })
    }
    private fun openOverviewFragment(it: String) {
        val action = TaskListFragmentDirections.actionNavigationTasklistToNavigationOverview(it.toLong())
        findNavController().navigate(action)
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = TaskListAdapter(viewModel)
            binding.recyclerViewTasklist.adapter = listAdapter
            //添加Android自带的分割线
            binding.recyclerViewTasklist.addItemDecoration(
                    DividerItemDecoration(
                            activity,
                            DividerItemDecoration.VERTICAL
                    )
            )
        } else {
            Timber.e("试图设置adapter时ViewModel尚未初始化。")
        }
    }

    private fun inputCategory() {
        val input = EditText(context)

        input.inputType = InputType.TYPE_CLASS_TEXT
        val builder = AlertDialog.Builder(context)
        builder.setTitle("输入一个类别")
                .setMessage("为了将任务分类，请输入类别")
                .setPositiveButton("保存") { dialog, which ->
                    viewModel.addCategory(input.text.toString())
                }
                .setView(input)
                .show()
    }

    fun deleteTask(item: Category) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("确定要删除 " + item.title + " 吗")
                .setPositiveButton("确定") { dialog, which ->
                    viewModel.deleteTask(item.id)
                }
                .show()
    }
}