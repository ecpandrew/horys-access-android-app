package com.example.klsdinfo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, RegisterActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    lateinit var mUserNameRegister    : EditText
    lateinit var mUserRegisterEmail   : EditText
    lateinit var mUserRegisterPass    : EditText
    lateinit var mUserRegisterBtn     : Button
    lateinit var mUserHasAccont       : TextView

    lateinit var progress             : AlertDialog.Builder
    lateinit var alertDialog          : AlertDialog
    lateinit var mAuth                : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        mUserNameRegister  = findViewById(R.id.input_name)
        mUserRegisterEmail = findViewById(R.id.input_email)
        mUserRegisterPass  = findViewById(R.id.input_password)
        mUserRegisterBtn   = findViewById(R.id.btn_signup)
        mUserHasAccont     = findViewById(R.id.link_login)

        // Setup progress bar
        progress = AlertDialog.Builder(this)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()


        mUserHasAccont.setOnClickListener {
            startActivity(LoginActivity.getLaunchIntent(this))
            finish()
        }

        mUserRegisterBtn.setOnClickListener {

            val username = mUserNameRegister.text.toString().trim()
            val password = mUserRegisterPass.text.toString().trim()
            val email = mUserRegisterEmail.text.toString().trim()

            if(TextUtils.isEmpty(username)){
                mUserNameRegister.error = "Name field is blank"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(email)){
                mUserRegisterEmail.error = "Email field is blank"
                return@setOnClickListener
            }

            if(!email.isValidEmail()){
                mUserRegisterEmail.error = "Not an Email"
                return@setOnClickListener
            }
            if(!email.isLsdiEmail()){
                mUserRegisterEmail.error = "Not an LSDi Email"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                mUserRegisterPass.error = "Enter Password"
                return@setOnClickListener
            }
            if(mUserRegisterPass.length() <=5){
                mUserRegisterPass.error = "Password must have at least 6 digits"
                return@setOnClickListener
            }


            registerUser(username, email, password)
        }

    }

    private fun registerUser(username: String, email: String, password: String) {
        alertDialog.show()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    alertDialog.dismiss()
//                    startActivity(MainActivity.getLaunchIntent(this))
//                    finish()

                    val mUser = mAuth.currentUser
                    mUser!!.sendEmailVerification()
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this,
                                    "Verification email sent to " + mUser.email,
                                    Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    startActivity(VerifyActivity.getLaunchIntent(this))
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                alertDialog.dismiss()
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
        Log.d("Lifecycle", "RegisterActivity: $msg")
    }

}
