package ir.ag.stack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.ag.stack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun postfixToInfix(postfix: String): String {
            val stack = mutableListOf<String>()
            for (char in postfix) {
                when{
                    char.isLetterOrDigit() -> stack.add(char.toString())
                    else -> {
                        val operand2 = stack.removeAt(stack.lastIndex)
                        val operand1 = stack.removeAt(stack.lastIndex)
                        stack.add("($operand1$char$operand2)")
                    }
                }
            }
            return if (stack.isNotEmpty()) stack[0] else "Invalid postfix expression"
        }

        fun infixToPostfix(infix: String): String {
            val stack = mutableListOf<Char>()
            val postfix = StringBuilder()
            val precedence = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2, '^' to 3)

            for (char in infix) {
                when {
                    char.isLetterOrDigit() -> postfix.append(char)
                    char == '(' -> stack.add(char)
                    char == ')' -> {
                        while (stack.isNotEmpty() && stack.last() != '(') {
                            postfix.append(stack.removeAt(stack.lastIndex))
                        }
                        stack.removeAt(stack.lastIndex)
                    }
                    char in precedence.keys -> {
                        while (stack.isNotEmpty() && stack.last() != '(' && precedence[stack.last()]!! >= precedence[char]!!) {
                            postfix.append(stack.removeAt(stack.lastIndex))
                        }
                        stack.add(char)
                    }
                }
            }
            while (stack.isNotEmpty()) {
                postfix.append(stack.removeAt(stack.lastIndex))
            }
            return postfix.toString()
        }
        binding.btnPostfixToInfix.setOnClickListener {
            val postfix = binding.textFieldPostfix.editText?.text.toString()
            if (postfix.isNotEmpty()) {
                val infix = postfixToInfix(postfix)
                binding.tvResultPostfix.text = "Result: $infix"
            }else{
                binding.tvResultPostfix.text = "Result: Invalid postfix expression"
            }
        }
        binding.btnInfixToPostfix.setOnClickListener {
            val infix = binding.textFieldInfix.editText?.text.toString()
            if (infix.isNotEmpty()) {
                val postfix = infixToPostfix(infix)
                binding.tvResultInfix.text = "Result: $postfix"
            }else{
                binding.tvResultInfix.text = "Result: Invalid infix expression"
            }
        }
        binding.btnClear.setOnClickListener {
            binding.textFieldPostfix.editText?.text?.clear()
            binding.textFieldInfix.editText?.text?.clear()
            binding.tvResultPostfix.text = "Result: "
            binding.tvResultInfix.text = "Result: "
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}