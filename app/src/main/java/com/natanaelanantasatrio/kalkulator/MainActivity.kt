package com.natanaelanantasatrio.kalkulator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.natanaelanantasatrio.kalkulator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    // Objek binding untuk mengakses view dalam layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate layout dan set sebagai content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi berbagai setup tombol
        setupDarkModeToggle()
        setupNumericButtons()
        setupOperatorButtons()
        setupClearButton()
        setupBackspaceButton()
        setupEqualsButton()
    }

    // Setup fungsi toggle mode gelap
    private fun setupDarkModeToggle() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // Beralih antara mode gelap dan terang
            val mode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    // Setup tombol numerik (0-9 dan titik desimal)
    private fun setupNumericButtons() {
        // Daftar tombol numerik dan nilai yang sesuai
        val numericButtons = listOf(
            binding.textNumberZero to "0",
            binding.textNumberOne to "1",
            binding.textNumberTwo to "2",
            binding.textNumberThree to "3",
            binding.textNumberFour to "4",
            binding.textNumberFive to "5",
            binding.textNumberSix to "6",
            binding.textNumberSeven to "7",
            binding.textNumberEight to "8",
            binding.textNumberNine to "9",
            binding.textComa to "."
        )
        // Set onClickListener untuk setiap tombol numerik
        numericButtons.forEach { (button, value) ->
            button.setOnClickListener { appendOnExpressions(value, true) }
        }
    }

    // Setup tombol operator (+, -, *, /, (, dan ))
    private fun setupOperatorButtons() {
        // Daftar tombol operator dan nilai yang sesuai
        val operatorButtons = listOf(
            binding.textIncrease to " + ", binding.textSubtraction to " - ",
            binding.textMultiplication to " * ", binding.textDistribution to " / ",
            binding.textOpen to "(", binding.textClose to ")"
        )
        // Set onClickListener untuk setiap tombol operator
        operatorButtons.forEach { (button, value) ->
            button.setOnClickListener { appendOnExpressions(value, false) }
        }
    }

    // Setup tombol clear untuk menghapus ekspresi dan hasil
    private fun setupClearButton() {
        binding.textClear.setOnClickListener {
            binding.tvExpressions.text = ""
            binding.tvResult.text = ""
        }
    }

    // Setup tombol backspace untuk menghapus karakter terakhir
    private fun setupBackspaceButton() {
        binding.backSpaceButton.setOnClickListener {
            val string = binding.tvExpressions.text.toString()
            if (string.isNotEmpty()) {
                // Menghapus karakter terakhir dari ekspresi
                binding.tvExpressions.text = string.dropLast(1)
            }
            binding.tvResult.text = ""
        }
    }

    // Setup tombol equals untuk mengevaluasi ekspresi
    private fun setupEqualsButton() {
        binding.textTogetherWith.setOnClickListener {
            try {
                // Membuat dan mengevaluasi ekspresi matematika
                val expression = ExpressionBuilder(binding.tvExpressions.text.toString()).build()
                val result = expression.evaluate()
                val longResult = result.toLong()
                // Menampilkan hasil
                binding.tvResult.text = if (result == longResult.toDouble()) {
                    longResult.toString()
                } else {
                    result.toString()
                }
            } catch (e: Exception) {
                // Log setiap pengecualian
                Log.d("Exception", "Message: ${e.message}")
            }
        }
    }

    // Menambahkan teks ke dalam ekspresi
    private fun appendOnExpressions(string: String, canClear: Boolean) {
        if (canClear && binding.tvResult.text.isNotEmpty()) {
            // Menghapus ekspresi jika hasil tidak kosong
            binding.tvExpressions.text = ""
        }

        if (canClear) {
            // Menghapus hasil dan menambahkan string ke ekspresi
            binding.tvResult.text = ""
            binding.tvExpressions.append(string)
        } else {
            // Menambahkan hasil dan string ke ekspresi
            binding.tvExpressions.append(binding.tvResult.text)
            binding.tvExpressions.append(string)
            binding.tvResult.text = ""
        }
    }
}
