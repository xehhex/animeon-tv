package com.animeon.tv.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.regex.Pattern

object StreamResolver {

    private val client = OkHttpClient.Builder().build()

    /**
     * Resolves an episode link (e.g. Kodik link) into a playable HLS (.m3u8) or MP4 stream URL.
     */
    suspend fun resolveStreamUrl(episodeUrl: String): String = withContext(Dispatchers.IO) {
        val targetUrl = if (episodeUrl.startsWith("//")) "https:$episodeUrl" else episodeUrl
        
        // If it's already a direct video stream
        if (targetUrl.endsWith(".m3u8") || targetUrl.endsWith(".mp4")) {
            return@withContext targetUrl
        }

        try {
            // Kodik resolver: fetch iframe page
            val req = Request.Builder()
                .url(targetUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .header("Referer", "https://animeon.cc/")
                .build()

            client.newCall(req).execute().use { response ->
                val html = response.body?.string() ?: return@withContext targetUrl

                // Look for urlParams or .m3u8 directly in scripts
                val m3u8Matcher = Pattern.compile("""https?://[^\s"'<>]+\.m3u8""").matcher(html)
                if (m3u8Matcher.find()) {
                    return@withContext m3u8Matcher.group(0)
                }

                // Look for video links in json configurations
                val videoSrcMatcher = Pattern.compile("""["'](https?:\\?/\\?/[^"']+\.m3u8[^"']*)["']""").matcher(html)
                if (videoSrcMatcher.find()) {
                    return@withContext videoSrcMatcher.group(1).replace("\\/", "/")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback to the target URL directly
        targetUrl
    }
}
