package com.projectQ.SIMTembak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import kotlin.concurrent.thread


class Main2Activity : AppCompatActivity() {
    var maxPageNo = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        maxPageNo= CONSTANTS.maxPageNo

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main2)

//        Toast.makeText(getApplicationContext(), "Creation", Toast.LENGTH_LONG).show();
    }

    override fun onStart()
    {
        super.onStart()

//        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_LONG).show();

        var next: Button = findViewById<Button>(R.id.next)
        next.visibility=View.INVISIBLE;

        var pageno= this.intent.getIntExtra("pageno", 0);

        var page_name: TextView = findViewById<TextView>(R.id.page_name);

        var jSONString : String = this.intent.getStringExtra("jSONString")

        var jSONArray: JSONArray = JSONArray()
        try
        {
            jSONArray = JSONArray(jSONString)
        }
        catch (e: Exception)
        {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
        }

//        Toast.makeText(applicationContext, jSONArray.get(0).toString(), Toast.LENGTH_SHORT).show();


        var nextPage: Intent
//        intent=this.intent.getParcelableExtra("next")
        if (pageno==maxPageNo-1)
        {
            nextPage = Intent(this@Main2Activity, Main3Activity::class.java);
            nextPage.putExtra("pageno", pageno+1)
            nextPage.putExtra("jSONString", jSONString)

            next.setOnClickListener { startActivity(nextPage) };
            next.text = "Finished";
        }
        else
        {
            nextPage = Intent(this@Main2Activity, Main2Activity::class.java);
            nextPage.putExtra("pageno", pageno+1)
            nextPage.putExtra("jSONString", jSONString)

            next.setOnClickListener { startActivity(nextPage) }
        }

        var mainImage: ImageView = findViewById<ImageView>(R.id.mainImage)

//        var bitmap: Bitmap = BitmapFactory.decodeFile("./res/drawable/photo_1.jpg")

//        var jString = loadJSONfromAsset(pageno)

        var jSON: JSONObject = jSONArray.getJSONObject(pageno)
//        Toast.makeText(applicationContext, jSON.getString("question"), Toast.LENGTH_SHORT).show();

        page_name.text = jSON.getString("question")

        //Loading Bitmap
//        var resource: Int =resources.getIdentifier(jSON.getString("photo"), "drawable", packageName)
//        mainImage.setImageResource(resource)

        var choices= jSON.getJSONObject("choices")
        var correctanswers : CharArray = this.intent.getCharArrayExtra("correctanswers")
        correctanswers[pageno]=jSON.getString("answer")[0]

        nextPage.putExtra("correctanswers", correctanswers)

        var address = jSON.getString("photo")
        address = address.replace("\\", "", true)
//        Toast.makeText(applicationContext, address, Toast.LENGTH_SHORT).show();

        thread (start=true)
        {
            try
            {
                var bmp = webImageResource(address)
                runOnUiThread(
                {
//                    Toast.makeText(applicationContext, "change image", Toast.LENGTH_SHORT).show();
                    mainImage.setImageBitmap(bmp)
                })
            }
            catch (e: Exception)
            {
                runOnUiThread(
                {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show();
                })
            }
        }

        for (c in 'A'..'C')
        {
            var id = resources.getIdentifier("choice_$c", "id", packageName)
            var radio = findViewById<RadioButton>(id);
            radio.setOnClickListener{RadioButtonClick(radio, nextPage, c, pageno)}
//            page_name.text="$id "
            radio.text=choices.getString("$c")
        }
//        mainImage.setImageBitmap(bitmap)

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

    private fun RadioButtonClick(view: View, nextPage: Intent, choice: Char, pageno: Int)
    {
        var radio: RadioButton = view as RadioButton

        if (radio.isChecked)
        {
            var useranswers : CharArray = this.intent.getCharArrayExtra("useranswers")

            useranswers[pageno]=choice
            nextPage.putExtra("useranswers", useranswers);

            var next: Button = findViewById<Button>(R.id.next)
            next.visibility=View.VISIBLE
//            Toast.makeText(applicationContext, Arrays.toString(useranswers), Toast.LENGTH_SHORT).show();
        }
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

    override fun onStop()
    {
        super.onStop()

        finish()
    }
}