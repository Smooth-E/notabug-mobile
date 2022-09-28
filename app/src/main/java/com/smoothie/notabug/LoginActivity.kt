package com.smoothie.notabug

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var rootViewGroup : ViewGroup

    private lateinit var buttonLogin : Button;
    private lateinit var buttonRegister : Button
    private lateinit var buttonSkipLogin : Button

    private lateinit var textInputUsername : TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        rootViewGroup = findViewById(R.id.view_root)

        buttonLogin = findViewById(R.id.button_login)
        buttonRegister = findViewById(R.id.button_register)
        buttonSkipLogin = findViewById(R.id.button_skip_login)

        textInputUsername = findViewById(R.id.text_input_username)
        textInputPassword = findViewById(R.id.text_input_password)

        buttonLogin.setOnClickListener {
            if (textInputUsername.editText!!.text.isEmpty() or textInputPassword.editText!!.text.isEmpty()) {
                Snackbar.make(rootViewGroup, R.string.description_empty_fields, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: Implement login
        }
    }
}