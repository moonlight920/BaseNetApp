package com.moonlight.chatapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.moonlight.chatapp.home.HomeFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
            // ...
        }

        btn_create.setOnClickListener {
            createAccount(et_email.text.toString(), et_password.text.toString())
        }
        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btn_signIn.setOnClickListener {
            signIn(et_email.text.toString(), et_password.text.toString())
        }
        btn_signOut.setOnClickListener {
            mAuth.signOut()
        }
        btn_database.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        btn_storage.setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java))
        }

        var adapter = HomeFragmentPagerAdapter(this,supportFragmentManager)
        viewpager.adapter = adapter

        tab_layout.setupWithViewPager(viewpager)
        for (i in 0 until tab_layout.tabCount) {
            val tab = tab_layout.getTabAt(i)
            tab?.customView = adapter.getTabView(i)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener);
        var currentUser: FirebaseUser? = mAuth.currentUser
        updateUI(currentUser)
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)

    }

    private fun createAccount(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = mAuth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@MainActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    }
                })
    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@MainActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            tv_username.text = it.email
        }

    }
}
