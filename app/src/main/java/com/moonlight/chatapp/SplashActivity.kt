package com.moonlight.chatapp

import android.os.Bundle

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

//    private fun updateUI(user: FirebaseUser?) {
//        if (user == null) {
//            startActivity(Intent(this, LoginActivity::class.java))
//        } else {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//        finish()
//    }
}
