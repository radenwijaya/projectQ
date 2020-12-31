package com.projectQ.SIMTembak

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import kotlin.concurrent.thread

class Main4Activity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
    }

    override fun onStart()
    {
        super.onStart()

        var useranswer: Char = this.intent.getCharExtra("useranswer", 'X');
        var pageno: Int = this.intent.getIntExtra("pageno", 0);

//        Toast.makeText(applicationContext, "$useranswer {1+$pageno}", Toast.LENGTH_SHORT).show();

        var jSONString=this.intent.getStringExtra("jSONString")
        var jSONArray = JSONArray(jSONString)

        var jSON: JSONObject = jSONArray.getJSONObject(pageno)

//        var resource: Int =resources.getIdentifier(jSON.getString("photo"), "drawable", packageName)

        var question: TextView = findViewById<TextView>(R.id.question)
        question.text=jSON.getString("question")

        var correct: TextView = findViewById<TextView>(R.id.correct)
//
        var choices= jSON.getJSONObject("choices")
        correct.text="Jawaban Benar : "+jSON.getString("answer")+". "+choices.getString(jSON.getString("answer"))

        var your : TextView = findViewById<TextView>(R.id.your)
        your.text="Jawaban Anda : "+useranswer+". "+choices.getString("$useranswer")

        var reason : TextView = findViewById<TextView>(R.id.reason)
        reason.text=jSON.getString("explain")

        var mainImage: ImageView = findViewById<ImageView>(R.id.imageView)
        //var resource: Int =resources.getIdentifier(jSON.getString("photo"), "drawable", packageName)
        thread (start=true)
        {
            try {
                var bmp = webImageResource(jSON.getString("photo").replace("\\", ""))

                runOnUiThread(
                {
                    mainImage.setImageBitmap(bmp)
                })
            }
            catch (e : Exception)
            {
                runOnUiThread(
                    {
                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
                    })
            }
        }
    }

    private fun webImageResource(address: String) : Bitmap
    //https://stackoverflow.com/questions/29802323/android-with-kotlin-how-to-use-httpurlconnection
    {
        var url = URL(address)

        var stream = BufferedInputStream(url.openStream())
        var bmp = BitmapFactory.decodeStream(stream)

//        connection.disconnect();
        return bmp
    }

    private fun loadJSONfromAsset(pageno: Int) : String
    {
        var JSON : String = "";

        var id: Int =resources.getIdentifier("input_$pageno", "raw", packageName)

        if (id!=0)
        {
            var stream = resources.openRawResource(id)

            var streamReader = InputStreamReader(stream, "UTF-8")
            var reader = BufferedReader(streamReader);

            var text:List<String> = reader.readLines()

            for (line in text)
            {
                JSON+=line
            }
        }

        return JSON;
    }
}