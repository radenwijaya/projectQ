package com.projectQ.SIMTembak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*

class Main3Activity : AppCompatActivity()
{
    var maxPageNo = 3
    lateinit var texts : Array<TextView>
    lateinit var buttons : Array<Button>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        maxPageNo= CONSTANTS.maxPageNo

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        texts=Array(maxPageNo){TextView(this)}
        buttons=Array(maxPageNo){Button(this)}

        for (i in 0..maxPageNo-1)
        {
            val tl= resources.getIdentifier("table_layout", "id", packageName)
            val tableLayout: TableLayout = findViewById<TableLayout>(tl)

            val tableRow: TableRow = TableRow(this)

            tableRow.addView(texts[i]);
            tableRow.addView(buttons[i]);

            tableLayout.addView(tableRow);
        }
    }

    override fun onStart()
    {
        super.onStart()

        var correctanswers : CharArray = this.intent.getCharArrayExtra("correctanswers")

        var useranswers : CharArray = this.intent.getCharArrayExtra("useranswers")

        var explainpage = Array(useranswers.count()) { Intent(this@Main3Activity, Main4Activity::class.java) }

        var jSONString=this.intent.getStringExtra("jSONString")

        for (i in correctanswers.indices)
        {
//            var tv_id = resources.getIdentifier("question_$i", "id", packageName)
//            var bt_id = resources.getIdentifier("button_$i", "id", packageName)

//            Toast.makeText(applicationContext, "$tv_id $bt_id", Toast.LENGTH_SHORT).show();

            var button: Button = buttons[i]
            var textView: TextView = texts[i]
//            var button: Button = findViewById<Button>(bt_id)
//            var textView: TextView = findViewById<TextView>(tv_id)

            if (correctanswers[i]==useranswers[i])
            {
                button.visibility = View.INVISIBLE
                textView.text = "$i Betul!"
            }
            else
            {
                button.visibility = View.VISIBLE
                button.text = "Cek Jawaban"
                textView.text = "$i Salah!"
            }

            explainpage[i].putExtra("pageno", i)
            explainpage[i].putExtra("useranswer", useranswers[i])
            explainpage[i].putExtra("jSONString", jSONString)
            button.setOnClickListener { startActivity(explainpage[i]) }
        }
    }
}
