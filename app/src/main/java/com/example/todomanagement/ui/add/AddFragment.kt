package com.example.todomanagement.ui.add

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.todomanagement.R
import com.example.todomanagement.databinding.FragmentAddBinding
import com.example.todomanagement.ui.overview.OverviewFragmentDirections
import com.example.todomanagement.util.Event
import com.example.todomanagement.util.setupSnackbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class AddFragment : Fragment() {

    private lateinit var viewModel: AddViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        //初始化view model
        viewModel = ViewModelProvider(this,
                AddViewModelFactory(requireNotNull(this.activity).application))
                .get(AddViewModel::class.java)

        val binding: FragmentAddBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add, container, false)
        // 设置life cycle owner
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewmodel = this.viewModel

        //设置fab和 日期时间选择控件
        binding.btnDateTimePicker.setOnClickListener {
            pickTime()
            pickDate()
        }

        //创建通知channel
        createChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.notification_channel_name)
        )

        binding.saveTaskFab.setOnClickListener { v->
            //val action = AddFragmentDirections.actionAddFragmentToNavigationAll(taskId)
            //findNavController().navigate(action)
        }

        return binding.root
    }

    /**
     * 在onCreateView之后创建
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()


    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)


    }

    private fun pickTime() {
        val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(MaterialTimePicker.STYLE_NORMAL)
                .setMinute(MaterialTimePicker.STYLE_NORMAL)
                .build()

        timePicker.show(childFragmentManager, "选择时间")
        timePicker.addOnPositiveButtonClickListener {
            //获取时间
            viewModel.time.value!!.minute = timePicker.minute
            viewModel.time.value!!.hour = timePicker.hour
        }
    }

    private fun pickDate() {
        val datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("选择日期")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

        datePicker.show(childFragmentManager, "选择")
        //获取日期
        datePicker.addOnPositiveButtonClickListener {
            viewModel.time.value!!.date = datePicker.selection!!
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    //TODO 自定义importance为high
                    NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "新任务提醒"

            val notificationManager = requireActivity().getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}