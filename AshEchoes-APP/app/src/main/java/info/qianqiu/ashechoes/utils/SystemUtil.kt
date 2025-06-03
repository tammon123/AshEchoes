package info.qianqiu.ashechoes.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("InternalInsetResource")
fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        context.resources.getDimensionPixelSize(resourceId)
    } else {
        // 如果资源ID不存在，则返回一个默认值（通常是25dp）
        val defaultStatusBarHeight = 25
        Math.round(defaultStatusBarHeight * context.resources.displayMetrics.density)
    }
}
fun getDeviceHeight(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return displayMetrics.heightPixels * 1.0F
}