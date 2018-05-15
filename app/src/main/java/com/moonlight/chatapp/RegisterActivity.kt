package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.moonlight.chatapp.utils.CheckUtil
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun initViewData() {
        var editTextUsername: EditText = til_username.editText!!
        til_username.hint = "邮箱"
        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && !CheckUtil.isEmail(s)) {
                    til_username.error = getString(R.string.warn_username_invalid)
                    til_username.isErrorEnabled = true
                } else {
                    til_username.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        var editTextPwd: EditText = til_pwd.editText!!
        til_pwd.hint = "密码"
        editTextPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < 6) {
                    til_pwd.error = getString(R.string.warn_pwd_invalid)
                    til_pwd.isErrorEnabled = true
                } else {
                    til_pwd.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        var editTextConfirmPwd: EditText = til_confirm_pwd.editText!!
        til_confirm_pwd.hint = "确认密码"
        editTextConfirmPwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val pwd = editTextPwd.text.toString()
                if (s.isNotEmpty() && s.length < 6) {
                    til_confirm_pwd.error = getString(R.string.warn_pwd_invalid)
                    til_confirm_pwd.isErrorEnabled = true
                } else if(pwd != s){
                    til_confirm_pwd.error = getString(R.string.warn_pwd_diff)
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
            } else {
                toast("检查用户名或密码")
            }
        }
        btn_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun createAccount(email: String, password: String) {
        val bu = BmobUser()
        bu.username = email
        bu.setPassword(password)
        bu.email = email
        //注意：不能用save方法进行注册
        bu.signUp<User>(object : SaveListener<User>() {
            override fun done(s: User?, e: BmobException?) {
                if (e == null) {
                    toast("注册成功:" + s.toString())
                    registerSuccess()
                } else {
                    toast("注册失败:" + e.toString())
                }
            }
        })
    }

    private fun registerSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
