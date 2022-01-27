package com.munidigital.bc2201.challengfinal

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.munidigital.bc2201.challengfinal.login.LogActivity
import com.munidigital.bc2201.challengfinal.login.LoginViewModel
import com.munidigital.bc2201.challengfinal.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DURATION: Long = 2000 // Setea el tiempo del splash en ms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        displayAppVersion()
        val viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModelLogin.state.value?.let {
                when{
                        (!it.loginError && it.user != null)->
                            startActivity(Intent(this, MainActivity::class.java))

                    else -> startActivity(Intent(this, LogActivity::class.java))
                }
            }
            finish()
        }, SPLASH_DURATION)

    }

    private fun displayAppVersion() {
        try {
            val version = this.packageManager.getPackageInfo(this.packageName, 0).versionName
            findViewById<TextView>(R.id.tv_versioname).text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}