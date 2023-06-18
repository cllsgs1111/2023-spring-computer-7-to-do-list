package com.example.todomanagement.ui.oneday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.todomanagement.R
import com.example.todomanagement.databinding.FragmentOnedayBinding
import timber.log.Timber


class OneDayFragment : Fragment() {

    private lateinit var viewModel: OneDayViewModel

    private lateinit var binding: FragmentOnedayBinding

    private lateinit var listAdapter: OneDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(
                this,
                OneDayViewModelFactory(requireNotNull(activity).application)
            ).get(OneDayViewModel::class.java)
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_oneday, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = OneDayAdapter(viewModel)
            binding.recyclerViewOneday.adapter = listAdapter
            //添加Android自带的分割线
            binding.recyclerViewOneday.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    DividerItemDecoration.VERTICAL
                )
            )
        } else {
            Timber.e("试图设置adapter时ViewModel尚未初始化。")
        }
    }
}