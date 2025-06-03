package info.qianqiu.ashechoes.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
fun dialog(
    context: Context,
    title: String,
    content: String,
    sureText: String = "确认",
    cancelText: String? = null
): Deferred<Boolean> = GlobalScope.async(Dispatchers.Main) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(content)

    // 设置确认按钮点击事件
    var result = false

    builder.setPositiveButton(sureText) { _, _ ->
        result = true
    }
    builder.setNegativeButton(cancelText) { dialog, which ->
        result = false
    }

    // 显示对话框，并等待用户交互
    val alertDialog = builder.create()

    alertDialog.show()

    // 暂停当前协程，直到对话框被关闭
    withContext(NonCancellable) {
        while (alertDialog.isShowing) {
            delay(100L)
        }
    }

    result

}

fun toast(context: Context, content: String) {
    Toast.makeText(
        context,
        content,
        Toast.LENGTH_SHORT
    ).show()
}