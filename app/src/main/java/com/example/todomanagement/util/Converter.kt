package com.example.todomanagement.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class Converter {
    companion object {
        fun convertDateTimeToMillSec(date: Long, hour: Int, minute: Int): Long {
            //TODO debug发现东八区已自动算在内，故hour - 8，适配国内的时间规范
            //TODO 在current时就已经+8,对应8点
            val minuteSum: Long = (hour - 8).toLong() * 60 + minute.toLong()
            val secondsSum: Long = minuteSum * 60
            val millSec: Long = secondsSum * 1000
            return millSec + date
        }

        fun convertMillSecToDateTime(millSec: Long?): DateTime {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)
            sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
            var date: Date? = null
            try {
                date = sdf.parse(millSec.toString())
            } catch (e: Exception) {
                Timber.e(e, "date为空！可能传入参数有误")
            }
            //输出date
            date?.let {
                return DateTime((date.day + 8).toLong(), date.hours, date.minutes)
            }
            return DateTime()
        }

        fun formatDateTimeString(millSec: Long?): String {
            val date = millSec?.let { Date(it) }
            val format = SimpleDateFormat("提醒时间被设定为：yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            return format.format(date)
        }

        fun formatDateTimeStringOneDay(millSec: Long?): String {
            val date = millSec?.let { Date(it) }
            val format = SimpleDateFormat("HH 时 mm 分", Locale.CHINA)
            return format.format(date)
        }

        fun getTomorrowInMillSec(today: Long): Long {
            val hours: Long = 24
            val minutes = hours * 60
            val seconds = minutes * 60
            val millSeconds = seconds * 1000
            return today + millSeconds
        }
    }
}