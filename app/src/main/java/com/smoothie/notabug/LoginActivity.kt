package com.smoothie.notabug

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.smoothie.notabug.anonymous.AnonymousHubActivity
import com.smoothie.notabug.authorized.AuthorizedHubActivity
import com.smoothie.notabug.view.InformativeBottomSheet
import org.jsoup.Jsoup

class LoginActivity : AppCompatActivity() {

    private lateinit var rootViewGroup : ViewGroup

    private lateinit var buttonLogin : Button;
    private lateinit var buttonRegister : Button
    private lateinit var buttonSkipLogin : Button

    private lateinit var textInputUsername : TextInputLayout
    private lateinit var textInputPassword: TextInputLayout

    private val centeredDialogStyle: Int =
        com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

    private fun loginWithSharedPrefs() {
        val preferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val anonymousBrowsing = preferences
            .getBoolean(Utilities.SharedPrefsNames.ANONYMOUS_BROWSING_ENABLED, false)
        val username = preferences.getString(Utilities.SharedPrefsNames.USERNAME, null)
        val password = preferences.getString(Utilities.SharedPrefsNames.PASSWORD, null)
        if (anonymousBrowsing) {
            startActivity(Intent(this, AnonymousHubActivity::class.java))
            finish()
            return
        }
        else if (username.isNullOrEmpty() or password.isNullOrEmpty()) {
            Log.d("loginWithSharedPrefs", "Password or username is empty.")
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
                val string = Utilities.post(
                    "https://notabug.org/user/login",
                    "user_name=$username&password=$password"
                )
                val errorLoggingIn = Jsoup.parse(string)
                    .select(".ui.negative.message").size > 0
                this.runOnUiThread { loadingDialog.dismiss() }
                if (errorLoggingIn) {
                    Log.d("loginWithSharedPrefs", "Error logging in!")
                    preferences.edit()
                        .remove(Utilities.SharedPrefsNames.USERNAME)
                        .remove(Utilities.SharedPrefsNames.PASSWORD)
                        .apply()
                    this.runOnUiThread {
                        MaterialAlertDialogBuilder(this, centeredDialogStyle)
                            .setIcon(R.drawable.ic_round_report_problem_24)
                            .setTitle(R.string.label_failed_to_login)
                            .setMessage(R.string.description_error_logging_in)
                            .setPositiveButton(R.string.action_okay) { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                }
                else {
                    this.runOnUiThread {
                        startActivity(Intent(this, AuthorizedHubActivity::class.java))
                        finish()
                    }
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

        buttonLogin.setOnClickListener {
            if (textInputUsername.editText!!.text.isEmpty() or
                textInputPassword.editText!!.text.isEmpty()) {
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
                    val editor =
                        getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit()
                    editor.remove(Utilities.SharedPrefsNames.ANONYMOUS_BROWSING_ENABLED)
                    editor.putString(Utilities.SharedPrefsNames.USERNAME, username)
                    editor.putString(Utilities.SharedPrefsNames.PASSWORD, password)
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
                    this.startActivity(Intent(ACTION_VIEW)
                        .setData(Uri.parse("https://notabug.org/user/sign_up")))
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
                    val editor =
                        getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit()
                    editor.putBoolean("anonymous", true)
                    editor.apply()
                    this.startActivity(Intent(this, AnonymousHubActivity::class.java))
                    this.finish()
                }
                .show()
        }

        loginWithSharedPrefs()
    }

}
