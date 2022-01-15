package com.sysiphusj.aop.part01.secret_diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker

class MainActivity : AppCompatActivity() {

    private val numberPicker1: NumberPicker by lazy {
        findViewById(R.id.numberPicker1)
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById(R.id.numberPicker2)
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById(R.id.numberPicker3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}