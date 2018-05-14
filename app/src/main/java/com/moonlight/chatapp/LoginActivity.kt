package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.moonlight.chatapp.utils.CheckUtil
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun initViewData() {
        var editTextUsername: EditText = til_username.editText!!
        til_username.hint = "UserName(Email)"
        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && !CheckUtil.isEmail(s)) {
                    til_username.error = "Invalid UserName"
                    til_username.isErrorEnabled = true
                } else {
                    til_username.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        var editTextPwd: EditText = til_pwd.editText!!
        til_pwd.hint = "Password"
        editTextPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < 6) {
                    til_pwd.error = "Invalid password"
                    til_pwd.isErrorEnabled = true
                } else {
                    til_pwd.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        btn_login.setOnClickListener {
            val email = editTextUsername.text.toString()
            val pwd = CheckUtil.md5Encode(editTextPwd.text.toString())
            signIn(email, pwd)
        }
        btn_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun signIn(email: String, password: String) {

    }

    private fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}
