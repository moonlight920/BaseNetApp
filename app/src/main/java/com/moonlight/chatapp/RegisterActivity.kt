package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.moonlight.chatapp.utils.CheckUtil
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun initViewData() {
        var editTextUsername: EditText = til_username.editText!!
        til_username.hint = "UserName"
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

        var editTextConfirmPwd: EditText = til_confirm_pwd.editText!!
        til_confirm_pwd.hint = "Confirm Password"
        editTextConfirmPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val pwd = editTextPwd.text.toString()
                if (s.isNotEmpty() && s.length < 6 && pwd != s) {
                    til_confirm_pwd.error = "Invalid password"
                    til_confirm_pwd.isErrorEnabled = true
                } else {
                    til_confirm_pwd.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        btn_register.setOnClickListener {
            val email = editTextUsername.text.toString()
            val pwd = CheckUtil.md5Encode(editTextPwd.text.toString())
            if (email.isNotEmpty() && pwd.isNotEmpty() &&
                    !til_username.isErrorEnabled && !til_pwd.isErrorEnabled && !til_confirm_pwd.isErrorEnabled) {
                createAccount(email, pwd)
            }else{
                toast("check text")
            }
        }
        btn_to_login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    private fun createAccount(email: String, password: String) {

    }

    private fun registerSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
