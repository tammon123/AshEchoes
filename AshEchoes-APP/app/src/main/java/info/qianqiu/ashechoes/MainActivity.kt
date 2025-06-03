package info.qianqiu.ashechoes

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.http.SslError
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import info.qianqiu.ashechoes.databinding.ActivityMainBinding
import info.qianqiu.ashechoes.h5.OnTokenReceivedListener
import info.qianqiu.ashechoes.h5.WebviewInterface
import info.qianqiu.ashechoes.utils.dialog
import info.qianqiu.ashechoes.utils.downloadApk
import info.qianqiu.ashechoes.utils.getDeviceHeight
import info.qianqiu.ashechoes.utils.getStatusBarHeight
import info.qianqiu.ashechoes.utils.sendGetRequest
import info.qianqiu.ashechoes.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class MainActivity : AppCompatActivity(), OnTokenReceivedListener {

    private lateinit var binding: ActivityMainBinding

    private val baseU: String = "https://bjhl.qianqiu.info?aesVersion="

    //    private val baseU: String = "http://192.168.100.123:881/?aesVersion="
    private var baseUrl: String = ""
    private lateinit var webview: WebView
    private var isPageLoading = false
    private var H5_VERSION_API = "https://bjhl.qianqiu.info/api/h5/v";
    private var APP_VERSION_API = "https://bjhl.qianqiu.info/api/app/v";

    @SuppressLint("StaticFieldLeak")
    companion object {
        lateinit var instance: MainActivity
        fun setData(param: String, data: String) {
            val sharedPreferences: SharedPreferences =
                instance.getSharedPreferences("asData", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(param, data)
            editor.apply() // 或者使用 editor.commit() 来同步保存
        }

        fun getData(param: String): String? {
            val sharedPreferences: SharedPreferences =
                instance.getSharedPreferences("asData", MODE_PRIVATE)
            val value = sharedPreferences.getString(param, "")
            return value
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        initCustomStyle()
        checkAppUpdate()
        baseUrl =
            baseU + getAppVersionCode() + "&nav=" + getStatusBarHeight(instance) / getDeviceHeight(
                instance
            )
        webview = binding.webview
        isPageLoading = false
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        val recorder = WebviewInterface(this, this)
        webview.addJavascriptInterface(recorder, "recorder")
        webview.webViewClient = object : WebViewClient() {
            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (!isPageLoading) {
                    isPageLoading = true
                    val data = JSONObject(sendGetRequest(H5_VERSION_API))
                    val version = data.getString("data")
                    val s = getData("ht-ae-version") ?: "0"
                    if (version != s) {
                        reloadWebview(version)
                    }
                } else {
                    super.onPageFinished(view, url)
                }
            }
        }
        webview.loadUrl(baseUrl)
        downloadShareImgHandle()
    }

    override fun onResume() {
        super.onResume()
        baseUrl =
            baseU + getAppVersionCode() + "&nav=" + getStatusBarHeight(instance) / getDeviceHeight(
                instance
            )
        Log.e("aeTest", baseUrl)
        callback.isEnabled = true
        // 添加回调到宿主 Activity 的 onBackPressedDispatcher 中
        instance.onBackPressedDispatcher.addCallback(this, callback)
        if (::webview.isInitialized) {
            var data = JSONObject(sendGetRequest(H5_VERSION_API))
            val version = data.getString("data")
            val s = getData("ht-ae-version") ?: "0"
            if (version != s) {
                reloadWebview(version)
            }
        }
    }

    private fun reloadWebview(version: String) {
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
        setData("ht-ae-version", version)
        webview.reload()
        webview.loadUrl(baseUrl)
    }

    private fun downloadShareImgHandle() {
        webview.setOnLongClickListener {
            val result = webview.hitTestResult ?: return@setOnLongClickListener false
            if (result.type == android.webkit.WebView.HitTestResult.IMAGE_TYPE ||
                result.type == android.webkit.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
            ) {
                val let = result.extra?.let { it1 -> saveImage(it1) }
                if (let.equals("true")) {
                    toast(instance, "已保存，请前往相册查看或分享")
                } else if (let.equals("ignore")) {
                    println("忽略")
                } else {
                    toast(instance, "保存失败")
                }
                return@setOnLongClickListener true
            } else {
                return@setOnLongClickListener false
            }
        }
    }

    // 保存图片方法
    private fun saveImage(data: String): String {
        try {
            var imageBitmap: Bitmap? = null
            //根据系统API判断是否为图片链接
            var realStr = data
            if (!realStr.contains("base64")) {
                return "ignore"
            }
            if (realStr.contains(",")) {
                realStr = realStr.split(",")[1]
            }
            val array = android.util.Base64.decode(realStr, android.util.Base64.NO_WRAP)
            imageBitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
            if (imageBitmap != null) {
                return saveBitmapToGallery(imageBitmap)
            } else {
                return "ignore"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO 保存失败
            return "false"
        }
    }

    private fun saveBitmapToGallery(bitmap: Bitmap): String {
        val bitName = "${UUID.randomUUID()}.jpg"
        val saveFile = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            bitName
        )
        val fos = FileOutputStream(saveFile)
        try {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)) {
                fos.flush()
            }
            MediaScannerConnection.scanFile(
                instance,
                arrayOf(saveFile.absolutePath),
                arrayOf("image/jpeg")
            ) { _, _ ->
            }
            //保存成功 Toast提示
            return "true"
        } catch (e: Exception) {
            //TODO 保存失败
        } finally {
            fos.close()
        }
        return "false"
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (webview.canGoBack()) {
                webview.goBack()
            } else {
                // 如果 WebView 没有可回退的历史，则允许默认行为（如关闭 Fragment 或 Activity）
                isEnabled = false
                instance.onBackPressedDispatcher.onBackPressed()
            }
        }
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

    private fun checkAppUpdate() {
        try {
            val vData = sendGetRequest(APP_VERSION_API)
            val vd = JSONObject(vData)
            val cappVersion = getAppVersionCode()
            if (vd.getLong("data") > cappVersion) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result =
                        dialog(
                            instance,
                            "有新版本",
                            "是否前往下载最新安装包",
                            "前往",
                            "取消"
                        ).await()
                    if (result) {
                        downloadApk(instance, vd.getString("a"), "aes" + vd.getLong("app"))
                        toast(instance, "已提交下载请求，请查看顶部状态栏~")
                    } else {
                        toast(instance, "取消更新")
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "检查更新失败~", Toast.LENGTH_LONG).show()
        }
    }

    private fun getAppVersionCode(): Long {
        return try {
            val pInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            pInfo.longVersionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1L
        }
    }

    override fun onTokenReceived(token: String?) {
        TODO("Not yet implemented")
    }
}