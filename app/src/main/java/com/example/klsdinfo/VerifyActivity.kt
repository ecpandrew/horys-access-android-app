package com.example.klsdinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class VerifyActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, VerifyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)


        val btn =  findViewById<Button>(R.id.btn_signin)
        btn.setOnClickListener {


            val mAuth = FirebaseAuth.getInstance()
            val userTask = mAuth.currentUser?.reload()

            userTask?.addOnSuccessListener {
                var user: FirebaseUser? = mAuth.currentUser
                if(user != null){
                    if (user.isEmailVerified) {
                        startActivity(MainActivity.getLaunchIntent(this))
                        finish()
                    } else {
                        Toast.makeText(this, "Email not verified.", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    }
    // Just for debug purposes
    override fun onStart() {
        super.onStart()
        print("onStart")
    }
    override fun onRestart() {
        super.onRestart()
        print("onRestart")
    }
    override fun onResume() {
        super.onResume()
        print("onResume")

    }
    override fun onPause() {
        super.onPause()
        print("onPause")

    }
    override fun onStop() {
        super.onStop()
        print("onStop")

    }
    override fun onDestroy() {
        super.onDestroy()
        print("onDestroy")
    }
    fun print(msg: String){
        Log.d("Lifecycle", "VerifyActivity: $msg")
    }

}
