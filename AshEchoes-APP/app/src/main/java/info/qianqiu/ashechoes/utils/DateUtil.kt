package info.qianqiu.ashechoes.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 时间戳转换为格式化时间
 */
fun timestampToStr(time: Long): String {
    val date: Date = Date(time * 1000L)
    // 定义日期格式
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // 格式化日期
    val formattedDate = sdf.format(date)
    return formattedDate
}

/**
 * 获取当天开始的秒数
 */
fun getStartOfDayInSeconds(): Long {
    val calendar = Calendar.getInstance()
    // 设置时间为当天的开始，即 00:00:00
    val chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai")
    calendar.timeZone = chinaTimeZone
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis / 1000
}

/**
 * 获取卡池开始的时间 1月12号
 */
fun getInitPoolDaySeconds(): Long {
    val calendar = Calendar.getInstance()
    val chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai")
    calendar.timeZone = chinaTimeZone
    // 设置年、月、日为指定的日期
    // 注意：月份是从 0 开始计数的，所以1月对应的是 0
    calendar.set(2024, 0, 12)
    // 设置时间为当天的开始，即 00:00:00
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis / 1000
}