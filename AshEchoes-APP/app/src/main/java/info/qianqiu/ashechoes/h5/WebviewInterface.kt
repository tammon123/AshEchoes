package info.qianqiu.ashechoes.h5

import android.content.Intent
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import info.qianqiu.ashechoes.ImportActivity
import info.qianqiu.ashechoes.MainActivity
import info.qianqiu.ashechoes.utils.toast
import java.net.URLDecoder

class WebviewInterface(
    private val activity: AppCompatActivity,
    private val listener: OnTokenReceivedListener
) {
    @JavascriptInterface
    fun uuid(data: String) {
        var userUid = data
        if (userUid != MainActivity.getData("uid")) {
            toast(activity, "登录成功")
            MainActivity.setData("uid", userUid)
            MainActivity.setData("clearZlWebview", "1")
            MainActivity.setData("userOpenId", "")
        }
    }

    @JavascriptInterface
    fun logout(data: String) {
        toast(activity, "已退出登录")
        MainActivity.setData("uid", "")
        MainActivity.setData("clearZlWebview", "1")
        MainActivity.setData("userOpenId", "")
    }

    //点击了主页的导入数据按钮
    @JavascriptInterface
    fun toImportDataPage() {
        val intent = Intent(activity, ImportActivity::class.java)
        activity.startActivity(intent)
    }

    //其实就是判断烛龙那边官方是否登录
    @JavascriptInterface
    fun initOpenId(token: String) {
        Log.e("aeTest", "12333")
        MainActivity.setData("userOpenId", "login")
        toast(activity, "登录数据初始化完成")
        // 需要通知调用者，开始抓取数据
        // 抓取指定URL的Cookie
        listener.onTokenReceived(token)
        Log.e("aeTest", "Cookies for $ImportActivity.WEB_COOKIE_URL: $token")
    }
}
