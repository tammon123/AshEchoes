package info.qianqiu.ashechoes.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL

fun sendGetRequest(urlString: String): String {
    val url = URL(urlString)
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"
        setRequestProperty("Accept", "*")
        try {
            // 读取响应
            val responseCode = responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line).append("\n")
                }
                reader.close()
                return response.toString()
            } else {
                return "error"
            }
        } finally {
            disconnect()
        }
    }
}

fun sendPostRequest(urlString: String, jsonInputString: String): String {
    val url = URL(urlString)
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        setRequestProperty("Content-Type", "application/json; utf-8")
        setRequestProperty("Accept", "application/json")
        doOutput = true // Triggers POST.
        readTimeout = 120000
        connectTimeout = 120000
        try {
            // Write the JSON data to the output stream.
            val writer = PrintWriter(getOutputStream(), true)
            writer.println(jsonInputString)
            // Get the response.
            val responseCode = responseCode
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return response.toString()
            } else {
                return "error"
            }
        } finally {
            disconnect()
        }
    }
}

fun downloadApk(context: Context, downloadUrl: String, appName:String) {
    val request = DownloadManager.Request(Uri.parse(downloadUrl))
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setTitle("白荆小助手更新")  // 设置下载标题
        .setDescription("正在下载最新版本")  // 设置下载描述
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$appName.apk")  // 设置保存路径
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)  // 下载完成后显示通知
        .setAllowedOverMetered(true)  // 允许使用移动数据下载
        .setAllowedOverRoaming(true)  // 允许漫游时下载

    val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    manager.enqueue(request)
}