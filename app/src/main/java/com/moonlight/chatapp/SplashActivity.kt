package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import cn.bmob.v3.BmobUser


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        updateUI()
    }

    private fun updateUI() {
        val bmobUser = BmobUser.getCurrentUser(User::class.java)
        if (bmobUser != null) {
            // 允许用户使用应用
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}
