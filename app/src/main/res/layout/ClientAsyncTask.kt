package layout

import android.os.AsyncTask
import com.gmail.akakom16.eko.kotlinjadwalsholat.MainActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ClientAsyncTask (private val mContext: MainActivity, postExecuteListener: OnPostExecuteListener) :
    AsyncTask<String, String, String>() {
    val CONNECTON_TIMEOUT_MILLISECONDS = 60000
    private val mPostExecuteListener: OnPostExecuteListener = postExecuteListener

    interface OnPostExecuteListener {
        fun onPostExecute(result: String)
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        if (mPostExecuteListener != null) {
            mPostExecuteListener.onPostExecute(result)
        }
    }

    override fun doInBackground(vararg urls: String?): String {
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(urls[0])
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
            urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS
            var inString = streamToString(urlConnection.inputStream)
            return inString
        } catch (ex: Exception) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }
        return ""
    }

    fun streamToString(inputStream: InputStream): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""
        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (line != null)
            inputStream.close()
        } catch (ex: Exception) {
        }
        return result
    }
}