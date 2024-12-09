package com.example.albertmontserrat_uf1_act15_calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var display: EditText
    private var currentInput = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        // Inicialitzar botons
        val buttons = listOf(
            Pair(R.id.button0, "0"), Pair(R.id.button1, "1"), Pair(R.id.button2, "2"),
            Pair(R.id.button3, "3"), Pair(R.id.button4, "4"), Pair(R.id.button5, "5"),
            Pair(R.id.button6, "6"), Pair(R.id.button7, "7"), Pair(R.id.button8, "8"),
            Pair(R.id.button9, "9"), Pair(R.id.buttonAdd, "+"), Pair(R.id.buttonSubtract, "-"),
            Pair(R.id.buttonMultiply, "*"), Pair(R.id.buttonDivide, "/"), Pair(R.id.buttonDot, ".")
        )

        // Assignar listeners als botons
        buttons.forEach { pair ->
            findViewById<Button>(pair.first).setOnClickListener { onButtonClick(pair.second) }
        }

        // Botons especials
        findViewById<Button>(R.id.buttonEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.buttonClear).setOnClickListener { clearDisplay() }
    }

    private fun onButtonClick(symbol: String) {
        currentInput.append(symbol)
        display.setText(currentInput)
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(currentInput.toString())
            currentInput.clear().append(result)
            display.setText(result.toString())

        } catch (e: Exception) {
            display.setText("Error")
        }
    }

    private fun clearDisplay() {
        currentInput.clear()
        display.setText("")
    }

    private fun evaluateExpression(expression: String): Double {
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<Char>()
        var currentNumber = StringBuilder()

        expression.forEach { char ->
            when (char) {
                in '0'..'9', '.' -> currentNumber.append(char)
                '+', '-', '*', '/' -> {
                    numbers.add(currentNumber.toString().toDouble())
                    operators.add(char)
                    currentNumber = StringBuilder()
                }
                else -> throw IllegalArgumentException("Invalid character: $char")
            }
        }
        if (currentNumber.isNotEmpty()) {
            numbers.add(currentNumber.toString().toDouble())
        }

        // Handling multiplication and division first
        var i = 0
        while (i < operators.size) {
            when (operators[i]) {
                '*' -> {
                    numbers[i] = numbers[i] * numbers[i + 1]
                    numbers.removeAt(i + 1)
                    operators.removeAt(i)
                }
                '/' -> {
                    numbers[i] = numbers[i] / numbers[i + 1]
                    numbers.removeAt(i + 1)
                    operators.removeAt(i)
                }
                else -> i++
            }
        }

        // Handling addition and subtraction
        i = 0
        while (i < operators.size) {
            when (operators[i]) {
                '+' -> {
                    numbers[i] = numbers[i] + numbers[i + 1]
                    numbers.removeAt(i + 1)
                    operators.removeAt(i)
                }
                '-' -> {
                    numbers[i] = numbers[i] - numbers[i + 1]
                    numbers.removeAt(i + 1)
                    operators.removeAt(i)
                }
            }
        }

        return numbers[0]
    }
}
