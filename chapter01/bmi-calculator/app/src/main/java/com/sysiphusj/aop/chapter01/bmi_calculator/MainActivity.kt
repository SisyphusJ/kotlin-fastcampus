package com.sysiphusj.aop.chapter01.bmi_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEditText: EditText = findViewById(R.id.heightEditText)
        val weightEditText = findViewById<EditText>(R.id.weightEditText)

        val resultButton = findViewById<Button>(R.id.resultButton)

        resultButton.setOnClickListener {

            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {
                Toast.makeText(this, "빈 값이 있다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                val height: Int = heightEditText.text.toString().toInt()
                val weight: Int = weightEditText.text.toString().toInt()

                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)

                Log.d("MainActivity", "height : $height weight : $weight")

            }
        }

    }
}