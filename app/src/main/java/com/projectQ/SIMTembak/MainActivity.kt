package com.projectQ.SIMTembak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import org.json.JSONArray

import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object CONSTANTS
{
    const val maxPageNo = 3
}

class MainActivity : AppCompatActivity() {
    var maxPageNo = 3
    var jSONString : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        maxPageNo= CONSTANTS.maxPageNo
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        var buttonX: Button = findViewById(R.id.buttonX)
//        buttonX.visibility=View.INVISIBLE

/*        var pageno = Array(maxPageNo) {1..maxPageNo}

        this.intent.putExtra("pageno", pageno);
        this.intent.putExtra("currentpage", 1);*/

        intent = Intent(this@MainActivity, Main2Activity::class.java);

//        var useranswers=CharArray(maxPageNo)
        var useranswers: CharArray = CharArray(maxPageNo)
        for (i in useranswers.indices)
            useranswers.set(i, ' ')

        var correctanswers: CharArray = CharArray(maxPageNo)
        for (i in correctanswers.indices)
            correctanswers.set(i, ' ')

//        Toast.makeText(applicationContext, Arrays.toString(useranswers), Toast.LENGTH_SHORT).show();

        intent.putExtra("useranswers", useranswers)
        intent.putExtra("correctanswers", correctanswers)

        var str : String = ""
        thread(start = true)
        {
            str = webResource("http://quiz.ptgoldensun.co/main.php")

            jSONString=str

            var jSONArray = JSONArray(jSONString)
            var jSONObject = jSONArray.getJSONObject(0)
            runOnUiThread {
        //                    Toast.makeText(applicationContext, "the JSON is "+str, Toast.LENGTH_SHORT).show()
                intent.putExtra("jSONString", jSONString)

                buttonX.visibility=View.VISIBLE
//                Toast.makeText(applicationContext, jSONObject.getString("photo").replace("\\", ""), Toast.LENGTH_SHORT).show()
            }

/*            var bmp = webImageResource(jSONObject.getString("photo").replace("\\", ""))

            runOnUiThread {
                intent.putExtra("photo", bmp)
                Toast.makeText(applicationContext, "image loaded", Toast.LENGTH_SHORT).show()
            }*/
        }

//        Toast.makeText(applicationContext, "UI thread is empty"+jSONString, Toast.LENGTH_SHORT).show();

/*        fab.setOnClickListener{ view->Snackbar.make(view, "this is after webResource thread exits "+jSONString, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()}*/

        buttonX.setOnClickListener (
            {
                startActivity(intent)
            })

/*        var bla : String = ""
        runBlocking<Unit>
        {
            thread(start = true)
            {
                bla = webResource("http://quiz.ptgoldensun.co/main.php")
//            Toast.makeText(applicationContext, "Start", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(applicationContext, bla, Toast.LENGTH_SHORT).show();
        }*/

//           bla.await()

//            Toast.makeText(applicationContext, bla.toString(), Toast.LENGTH_SHORT).show();

//        str.start()

//        Toast.makeText(applicationContext, str.toString(), Toast.LENGTH_SHORT).show();

        /*
        runBlocking<Unit>
        {
            val str = async { webResource("http://quiz.ptgoldensun.co/main.php") }

            Toast.makeText(applicationContext, str.await(), Toast.LENGTH_SHORT).show();
        }
        */

//        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show();
//        web.execute()
        //On final page will put the next as first
//        startActivity(intent)
    }

    private fun webResource(address: String) : String
    //https://stackoverflow.com/questions/29802323/android-with-kotlin-how-to-use-httpurlconnection
    {
        var result: String = "";

        try
        {
            var url = URL(address)
            var connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.doInput = true

//        Toast.makeText(applicationContext, connection.responseMessage, Toast.LENGTH_SHORT).show();

            if (connection.responseCode==200)
            {
                var stream = BufferedInputStream(connection.inputStream)
                var reader = BufferedReader(InputStreamReader(stream))

                result=reader.readLine()
            }
        }
        catch (e: Exception)
        {
            result=e.toString()
        }

//        connection.disconnect();
        return result
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}