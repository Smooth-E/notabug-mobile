package com.smoothie.notabug

import android.app.Dialog
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.smoothie.notabug.view.InformativeBottomSheet

class LoginActivity : AppCompatActivity() {

    private lateinit var rootViewGroup : ViewGroup

    private lateinit var buttonLogin : Button;
    private lateinit var buttonRegister : Button
    private lateinit var buttonSkipLogin : Button

    private lateinit var textInputUsername : TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    private fun loginWithSharedPrefs() {
        val preferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val username = preferences.getString("login", null)
        val password = preferences.getString("password", null)
        if (username.isNullOrEmpty() or password.isNullOrEmpty()) {
            Log.w("loginWithSharedPrefs", "Password or username is empty.")
            return
        }
        val loadingDialog = InformativeBottomSheet(
            this,
            R.drawable.ic_baseline_open_in_browser_24,
            R.string.label_logging_in,
            R.string.description_logging_in
        )
        loadingDialog.show()
        Thread {
            try {
                Utilities.post(
                    "https://notabug.org/user/login",
                    "user_name=$username&password=$password"
                )
                this.runOnUiThread {
                    loadingDialog.dismiss()
                    startActivity(Intent(this, AuthorizedHubActivity::class.java))
                    finish()
                }
            }
            catch (exception: Exception) {
                this.runOnUiThread {
                    exception.printStackTrace()
                    loadingDialog.dismiss()
                }
            }
        }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        rootViewGroup = findViewById(R.id.view_root)

        buttonLogin = findViewById(R.id.button_login)
        buttonRegister = findViewById(R.id.button_register)
        buttonSkipLogin = findViewById(R.id.button_skip_login)

        textInputUsername = findViewById(R.id.text_input_username)
        textInputPassword = findViewById(R.id.text_input_password)

        Utilities.initialize()

        val centeredDialogStyle = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

        buttonLogin.setOnClickListener {
            if (textInputUsername.editText!!.text.isEmpty() or textInputPassword.editText!!.text.isEmpty()) {
                Snackbar.make(rootViewGroup, R.string.description_empty_fields, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            MaterialAlertDialogBuilder(this, centeredDialogStyle)
                .setIcon(R.drawable.ic_baseline_admin_panel_settings_24 )
                .setTitle(R.string.label_credentials_handling_dialog)
                .setMessage(R.string.description_credentials_handling_dialog)
                .setNeutralButton(R.string.action_cancel) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(R.string.action_proceed) { dialog, _ ->
                    val username = textInputUsername.editText?.text.toString()
                    val password = textInputPassword.editText?.text.toString()
                    val editor = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit()
                    editor.putString("login", username)
                    editor.putString("password", password)
                    editor.apply()
                    loginWithSharedPrefs()
                    dialog.dismiss()
                }
                .show()
        }

        buttonRegister.setOnClickListener {
            MaterialAlertDialogBuilder(this, centeredDialogStyle)
                .setIcon(R.drawable.ic_baseline_open_in_browser_24)
                .setTitle(R.string.label_register_dialog)
                .setMessage(R.string.description_register_dialog)
                .setNeutralButton(R.string.action_cancel) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(R.string.action_open_browser) { dialog, _ ->
                    this.startActivity(Intent(ACTION_VIEW).setData(Uri.parse("https://notabug.org/user/sign_up")))
                    dialog.dismiss()
                }
                .show()
        }

        buttonSkipLogin.setOnClickListener {
            MaterialAlertDialogBuilder(this, centeredDialogStyle)
                .setIcon(R.drawable.ic_baseline_self_improvement_24)
                .setTitle(R.string.label_skip_login_dialog)
                .setMessage(R.string.description_skip_login_dialog)
                .setNeutralButton(R.string.action_cancel) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(R.string.action_continue) { dialog, _ ->
                    dialog.dismiss()
                    this.startActivity(Intent(this, AnonymousHubActivity::class.java))
                    this.finish()
                }
                .show()
        }

        loginWithSharedPrefs()
    }

}
