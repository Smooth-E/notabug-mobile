package com.smoothie.notabug

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.SurfaceColors
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
                    val bottomSheet = InformativeBottomSheet()
                    bottomSheet.iconResource = R.drawable.ic_baseline_open_in_browser_24
                    bottomSheet.headerText = getString(R.string.label_logging_in)
                    bottomSheet.supportiveText = getString(R.string.description_logging_in)
                    bottomSheet.isCancelable = false
                    bottomSheet.show(supportFragmentManager)
                    Thread {
                        try {
                            Utilities.post(
                                "https://notabug.org/users/login",
                                "user_name=${textInputUsername.editText?.text}&password=${textInputPassword.editText?.text}"
                            )
                            startActivity(Intent(this, AuthorizedHubActivity::class.java))
                            finish()
                        }
                        catch (exception: Exception) {
                            exception.printStackTrace()
                            bottomSheet.dismiss()
                        }
                    }
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
                    // TODO: Implement anonymous browsing
                    dialog.dismiss()
                    this.startActivity(Intent(this, AnonymousHubActivity::class.java))
                    this.finish()
                }
                .show()
        }
    }
}
