package com.furture.cronet

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log.i
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.furture.cronet.ui.theme.CronetTheme
import org.chromium.net.CronetEngine
import org.chromium.net.CronetEngine.Builder
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.WritableByteChannel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CronetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private const val TAG = "MyUrlRequestCallback"

class MyUrlRequestCallback : UrlRequest.Callback() {
    private val BYTE_BUFFER_CAPACITY_BYTES: Int = 64 * 1024

    private val bytesReceived = ByteArrayOutputStream()
    private val receiveChannel: WritableByteChannel = Channels.newChannel(bytesReceived)


    override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
        i(TAG, "onFailed method called.")
    }

    override fun onRedirectReceived(request: UrlRequest, p1: UrlResponseInfo, p2: String) {
        i(TAG, "onRedirectReceived method called.")
        request.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest, p1: UrlResponseInfo) {
        i(TAG, "onResponseStarted method called.")

        request.read(ByteBuffer.allocateDirect(BYTE_BUFFER_CAPACITY_BYTES));
    }

    override fun onReadCompleted(request: UrlRequest, info: UrlResponseInfo, byteBuffer: ByteBuffer) {
        i(TAG, "onReadCompleted method called.")

        // The byte buffer we're getting in the callback hasn't been flipped for reading,
        // so flip it so we can read the content.
        byteBuffer.flip();
        try {
            receiveChannel.write(byteBuffer)
        } catch (e: IOException) {
            i(TAG, "IOException during ByteBuffer read. Details: ", e)
        }
        // Reset the buffer to prepare it for the next read
        byteBuffer.clear();

        // Continue reading the request
        request.read(byteBuffer);
    }

    override fun onSucceeded(request: UrlRequest, info: UrlResponseInfo) {
        i(TAG, "onSucceeded method called." + info.allHeaders)

        val bodyBytes = bytesReceived.toByteArray()

        i(TAG, String(bodyBytes));

    }

    override fun onCanceled(p0: UrlRequest, p1: UrlResponseInfo?) {
        i(TAG, "onCanceled method called.")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current;
    Text(
        text = "Hello $name!",
        modifier = modifier.clickable {
            Toast.makeText(context, "hello world", Toast.LENGTH_SHORT).show();
            //https://github.com/GoogleChromeLabs/cronet-sample/blob/master/android/app/src/main/java/com/google/samples/cronet_sample/CronetApplication.java
            //https://github.com/google/cronet-transport-for-okhttp
            val myBuilder = Builder(context)
            myBuilder.enableQuic(true)
            myBuilder.enableHttp2(true)
            myBuilder.enableBrotli(true)
            myBuilder.setUserAgent("CronetSampleApp")
            val cronetEngine: CronetEngine = myBuilder.build()
             val requestBuilder = cronetEngine.newUrlRequestBuilder(
                "https://www.taobao.com/",
                MyUrlRequestCallback(),
                AsyncTask.THREAD_POOL_EXECUTOR
            )
            val request: UrlRequest = requestBuilder.build()
            request.start();

        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CronetTheme {
        Greeting("Android")
    }
}