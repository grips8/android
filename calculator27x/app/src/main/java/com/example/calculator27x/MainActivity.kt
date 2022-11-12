package com.example.calculator27x

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private var firstNumber: Double = 0.0
    private var secondNumber: Double = 0.0
    private var previousAction: String = "noAction"
    private var justCalculated: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.zero).setOnClickListener { numberPressed(0) }
        findViewById<Button>(R.id.one).setOnClickListener { numberPressed(1) }
        findViewById<Button>(R.id.two).setOnClickListener { numberPressed(2) }
        findViewById<Button>(R.id.three).setOnClickListener { numberPressed(3) }
        findViewById<Button>(R.id.four).setOnClickListener { numberPressed(4) }
        findViewById<Button>(R.id.five).setOnClickListener { numberPressed(5) }
        findViewById<Button>(R.id.six).setOnClickListener { numberPressed(6) }
        findViewById<Button>(R.id.seven).setOnClickListener { numberPressed(7) }
        findViewById<Button>(R.id.eight).setOnClickListener { numberPressed(8) }
        findViewById<Button>(R.id.nine).setOnClickListener { numberPressed(9) }
        findViewById<Button>(R.id.ret).setOnClickListener { binaryActionPressed("return") }
        findViewById<Button>(R.id.mult).setOnClickListener { binaryActionPressed("mult") }
        findViewById<Button>(R.id.minus).setOnClickListener { binaryActionPressed("minus") }
        findViewById<Button>(R.id.plus).setOnClickListener { binaryActionPressed("plus") }
        findViewById<Button>(R.id.divide).setOnClickListener { binaryActionPressed("divide") }
        findViewById<Button>(R.id.log).setOnClickListener { unaryActionPressed("log") }
        findViewById<Button>(R.id.neg).setOnClickListener { unaryActionPressed("neg") }
        findViewById<Button>(R.id.mod).setOnClickListener { binaryActionPressed("mod") }
        findViewById<Button>(R.id.power).setOnClickListener { binaryActionPressed("power") }
        findViewById<Button>(R.id.clear).setOnClickListener { clear() }

    }

    fun clear() {
        previousAction = "noAction"
        firstNumber = 0.0
        secondNumber = 0.0
        justCalculated = true
        findViewById<TextView>(R.id.display).text = "0"
    }

    fun numberPressed(number: Int) {
        if (justCalculated) {
            findViewById<TextView>(R.id.display).text = number.toString()
            justCalculated = false
        }
        else {
            findViewById<TextView>(R.id.display).append(number.toString())
        }
    }

    fun binaryActionPressed(currentAction: String) {
        val display = findViewById<TextView>(R.id.display)
        if (previousAction == "noAction") {
            firstNumber = display.text.toString().toDouble()
        }
        else {
            secondNumber = display.text.toString().toDouble()
            calculate()
        }
        justCalculated = true
        if (currentAction != "return")
            previousAction = currentAction
        else
            previousAction = "noAction"
    }

    fun unaryActionPressed(currentAction: String) {
        val display = findViewById<TextView>(R.id.display)
        when (currentAction) {
            "log" -> {
                display.text = kotlin.math.log10(display.text.toString().toDouble()).toString()
            }
            "neg" -> {
                display.text = (display.text.toString().toDouble() * -1).toString()
            }
        }
    }

    fun calculate() {
        val display = findViewById<TextView>(R.id.display)
        when (previousAction) {
            "mult" -> {
                firstNumber *= secondNumber
            }
            "plus" -> {
                firstNumber += secondNumber
            }
            "minus" -> {
                firstNumber -= secondNumber
            }
            "divide" -> {
                firstNumber /= secondNumber
            }
            "mod" -> {
                firstNumber %= secondNumber
            }
            "power" -> {
                firstNumber = firstNumber.pow(secondNumber)
            }
        }
        display.text = firstNumber.toString()
    }
}