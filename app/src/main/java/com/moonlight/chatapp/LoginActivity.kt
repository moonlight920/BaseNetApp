package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
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
            if (email.isNotEmpty() && pwd.isNotEmpty()
                    && !til_pwd.isErrorEnabled && !til_username.isErrorEnabled)
                signIn(email, pwd)
        }
        btn_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        BmobUser.loginByAccount<User>(email, password, object : LogInListener<User>() {

            override fun done(user: User?, e: BmobException?) {
                if (user != null) {
                    toast("登录成功:" + user.toString())
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    loginSuccess()
                } else {
                    toast("登录失败:" + e.toString())
                }
            }
        })
    }

    private fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}
