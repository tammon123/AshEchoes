package info.qianqiu.ashechoes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.net.http.SslError
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import info.qianqiu.ashechoes.databinding.ActivityImportBinding
import info.qianqiu.ashechoes.h5.OnTokenReceivedListener
import info.qianqiu.ashechoes.h5.WebviewInterface
import info.qianqiu.ashechoes.utils.dialog
import info.qianqiu.ashechoes.utils.getInitPoolDaySeconds
import info.qianqiu.ashechoes.utils.getStartOfDayInSeconds
import info.qianqiu.ashechoes.utils.sendGetRequest
import info.qianqiu.ashechoes.utils.timestampToStr
import info.qianqiu.ashechoes.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class ImportActivity : AppCompatActivity(), OnTokenReceivedListener {
    private lateinit var binding: ActivityImportBinding
    private lateinit var webview: WebView
    private lateinit var instance: Activity
    private lateinit var OFFICIAL_WEB_URL: String
    private val BASE_API = "bjhl.qianqiu.info/api"
    private val QUERY_LAST_STATUS_API: String = "http://$BASE_API/pool/query/last/"
    private val DELETE_API: String = "http://$BASE_API/pool/data/delete/"
    private val POST_DATA_API = "http://$BASE_API/pool/token/"
    private val ZL_APP_AIM_POINT_URL = "http://bjhl.qianqiu.info/zlAppAimPoint.json"
    private var startSecond = getInitPoolDaySeconds()
    private var todaySecond = getStartOfDayInSeconds()

    companion object {
        lateinit var WEB_COOKIE_URL: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        binding = ActivityImportBinding.inflate(layoutInflater)
        webview = binding.webviewzl
        initCustomStyle()
        initLoadingDialog()
        val initFuncStep = initFuncStep()
        if (!initFuncStep) {
            webview.alpha = 0f
            return
        }
        initWebview();
    }

    /**
     * 初始化状态校验
     */
    private fun initFuncStep(): Boolean {
        val uid = MainActivity.getData("uid") ?: ""
        if (uid.isEmpty()) {
            toast(this, "请返回上一页，并登录")
            return false
        }
        // 设置删除点击事件监听器
        val myButton: Button = binding.deleteButton
        myButton.setOnClickListener {
            // 处理点击事件
            CoroutineScope(Dispatchers.Main).launch {
                val result = dialog(
                    context = instance,
                    title = "真的要删除？",
                    content = "真的要删除所有数据尝试重新导入数据吗？",
                    sureText = "确认删除",
                    cancelText = "取消"
                ).await()
                if (result) {
                    toast(
                        instance,
                        "已提交删除指令，请关闭APP等待一会儿重新启动-关闭APP-关闭APP-关闭APP"
                    )
                    val r = sendGetRequest("$DELETE_API$uid")
                    if ("error" == r) {
                        toast(instance, "删除失败，请稍后再试~")
                    }
                    MainActivity.setData("userOpenId", "")
                } else {
                    toast(instance, "取消删除操作~")
                }
            }
        }
        // 判断最后一次更新的时间
        var sendGetRequest: String = sendGetRequest("$QUERY_LAST_STATUS_API$uid")
        Log.e("aeTest", sendGetRequest)
        sendGetRequest = sendGetRequest.replace(Regex("\\s+"), "")
        if ("0" != sendGetRequest) {
            try {
                startSecond = sendGetRequest.toLong()
            } catch (e: Exception) {
                toast(instance, "网络连接失败~请退出重进")
                return false
            }
        }
        // 是今天就不管
        if (startSecond >= todaySecond) {
            // 创建Date对象
            val dateStr = timestampToStr(startSecond)
            toast(instance, "上次更新为：$dateStr,已经是最新数据了~")
            return false
        }

        return true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebview() {
        val recorder = WebviewInterface(this, this)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.userAgentString = "PC"
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.addJavascriptInterface(recorder, "recorder")
        val sendGetRequest = sendGetRequest(ZL_APP_AIM_POINT_URL)
        if (sendGetRequest == "error") {
            toast(this, "获取配置文件失败，请重试或删除并重新导入数据")
            return
        }
        val aimJson = JSONObject(sendGetRequest)
        val aimPoint = aimJson.getString("a")
        val splitPointStr = aimJson.getString("s1")
        OFFICIAL_WEB_URL = aimJson.getString("o")
        WEB_COOKIE_URL = aimJson.getString("co")
        webview.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request != null) {
                    if (request.url.toString() == aimPoint) {
                        // 创建URL对象
                        val url = URL(aimPoint)
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"
                        // 获取响应码
                        val responseCode = connection.responseCode
                        // 如果响应码是成功的（200范围）
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val originalInputStream: InputStream =
                                connection.inputStream // 你的 InputStream 实例
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            val buffer = ByteArray(1024)
                            var length: Int
                            while (originalInputStream.read(buffer).also { length = it } != -1) {
                                byteArrayOutputStream.write(buffer, 0, length)
                            }
                            // 获取字节数组
                            val byteArray = byteArrayOutputStream.toByteArray()
                            // 将字节数组转换为字符串（假设是文本内容）
                            val originalContent = String(byteArray, Charset.forName("GBK"))
                            val indexOf =
                                originalContent.indexOf(splitPointStr)
                            val oldIndexContent = originalContent.substring(0, indexOf)
                            val oldIndexEndContent =
                                originalContent.substring(indexOf, originalContent.length)
                            // 修改内容
                            val modifiedContent =
                                "$oldIndexContent;window.sashData=utils.data;if(typeof(recorder)=='object')recorder.initOpenId(res.token);$oldIndexEndContent"
                            // 将修改后的内容转换回字节数组
                            val modifiedByteArray =
                                modifiedContent.toByteArray(Charset.forName("GBK"))
                            // 使用修改后的字节数组创建新的 ByteArrayInputStream
                            val newInputStream = ByteArrayInputStream(modifiedByteArray)
                            // 如果userOpenId不为空，说明已经登录了，透明化窗口即可
                            // 创建 WebResourceResponse 并传递新的 ByteArrayInputStream
                            return WebResourceResponse(
                                "application/x-javascript",
                                "GBK",
                                newInputStream
                            )
                        } else {
                            println("GET request not worked")
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val s = MainActivity.getData("clearZlWebview") ?: "0"
                if ("1" == s) {
                    reloadWebview()
                } else {
                    super.onPageFinished(view, url)
                }
            }

            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }
        }
        webview.loadUrl(OFFICIAL_WEB_URL)
    }

    private fun reloadWebview() {
        // 清除缓存
        webview.clearCache(true)
        webview.clearHistory()

        // 清除 Cookie
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()

        // 清除表单数据和密码
        webview.clearFormData()
        webview.clearSslPreferences()

        // 清除 WebStorage
        webview.clearCache(true)
        webview.clearHistory()
        MainActivity.setData("clearZlWebview", "0")
        webview.reload()
        webview.loadUrl(OFFICIAL_WEB_URL)
    }

    /**
     * 初始化全局的状态
     */
    private fun initCustomStyle() {
        // 隐藏布局的顶部
        this.supportActionBar?.hide()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        // 透明状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = option or vis
        window.statusBarColor = Color.TRANSPARENT
        // 设置状态栏为亮色模式，字体变为深色
        // 获取WindowInsetsControllerCompat对象
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        setContentView(binding.root)
    }

    @SuppressLint("StaticFieldLeak")
    private lateinit var loadingDialog: AlertDialog
    private fun initLoadingDialog() {
        val builder =
            AlertDialog.Builder(this, R.style.FullScreenSemiTransparentDialog)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.loading, null)
        builder.setView(view)
        builder.setCancelable(false) // 使对话框不可取消
        loadingDialog = builder.create()
        // 确保对话框是全屏且透明的
        // 确保对话框是全屏且黑色半透明的
        loadingDialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawableResource(R.drawable.semi_transparent_black)
        }
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // 设置背景透明
    }

    private fun showLoadingDialog() {
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    private fun hideLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    override fun onTokenReceived(token: String?) {
        if (token != null) {
            fetchDataAndShowResult(token)
        }
    }

    private fun fetchDataAndShowResult(token: String) = CoroutineScope(Dispatchers.Main).launch {
        showLoadingDialog()

        val url = POST_DATA_API + MainActivity.getData("uid") + "?token=" + token

        // 在后台线程中执行网络请求
        val result = withContext(Dispatchers.IO) {
            sendGetRequest(url)
        }

        // 更新UI
        if (result != "error") {
            toast(instance, "数据同步成功，请返回上一页>抽卡分析中查看")
        } else {
            toast(instance, "数据同步失败，请反馈给开发者")
        }

        hideLoadingDialog()
    }

}