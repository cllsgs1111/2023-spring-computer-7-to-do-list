package com.example.todomanagement.ui.overview

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.todomanagement.R
import com.example.todomanagement.database.Task
import com.example.todomanagement.databinding.FragmentOverviewBinding
import com.example.todomanagement.util.EventObserver
import com.example.todomanagement.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class OverviewFragment : Fragment() {

    private lateinit var viewModel: OverviewViewModel

    private lateinit var binding: FragmentOverviewBinding

    private lateinit var listAdapter: OverviewAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this, OverviewModelFactory
                (requireNotNull(activity).application)).get(OverviewViewModel::class.java)

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_overview, container, false
        )
        binding.lifecycleOwner = this
        binding.viewmodel = this.viewModel

        //设置fab点击响应，导航到addFragment
        binding.addTaskFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_all_to_addFragment)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        setupListAdapter()
        setupNavigation()
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            //viewModel.showEditResultMessage()
        }
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = OverviewAdapter(viewModel)
            binding.recyclerViewOverview.adapter = listAdapter
            //添加Android自带的分割线
            binding.recyclerViewOverview.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    DividerItemDecoration.VERTICAL

                )
            )
        } else {
            Timber.e("试图设置adapter时ViewModel尚未初始化。")
        }
    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d("TAG", "setupNavigation: " +it)
            openTaskDetails(it)
        })
    }

    private fun openTaskDetails(taskId: String) {
        val action = OverviewFragmentDirections.actionNavigationAllToModifyFragment(taskId)
        findNavController().navigate(action)
    }

    fun deleteTask(item: Task) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("确定要删除 " + item.title + " 吗")
                .setMessage(item.description)
                .setPositiveButton("确定") { dialog, which ->
                    viewModel.deleteTask(item.id)
                }
                .show()
    }
}