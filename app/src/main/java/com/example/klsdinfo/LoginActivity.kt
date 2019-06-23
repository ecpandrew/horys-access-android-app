package com.example.klsdinfo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


val String.domain: String?
    get() {
        val index = this.indexOf('@')
        return if (index == -1) null else this.substring(index + 1)
    }

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isLsdiEmail(): Boolean{

    if (this.domain == "lsdi.ufma.br"){
        return true
    }
    return false
}

class LoginActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    lateinit var mLoginBtn            : Button
    lateinit var mLoginGoogleBtn      : Button
    lateinit var mCreateUser          : TextView
    lateinit var mForgetPass          : TextView
    lateinit var mLoginEmail          : EditText
    lateinit var mLoginPass           : EditText

    val RC_SIGN_IN                    : Int = 1
    lateinit var mGoogleSignInClient  : GoogleSignInClient
    lateinit var mGoogleSignInOptions : GoogleSignInOptions

    lateinit var progress             : AlertDialog.Builder
    lateinit var alertDialog          : AlertDialog
    lateinit var mAuth                : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mLoginEmail     = findViewById(R.id.input_email)
        mLoginPass      = findViewById(R.id.input_password)
        mLoginBtn       = findViewById(R.id.btn_login)
        mLoginGoogleBtn = findViewById(R.id.btn_login_google)
        mCreateUser     = findViewById(R.id.link_signup)
        mForgetPass     = findViewById(R.id.link_forgot_pwd)


        progress = AlertDialog.Builder(this)
        progress.setCancelable(false)
        progress.setView(R.layout.loading_dialog_layout)
        alertDialog = progress.create()

        mAuth = FirebaseAuth.getInstance()

        mCreateUser.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


        mForgetPass.setOnClickListener {

            val email = mLoginEmail.text.toString().trim()

            if(TextUtils.isEmpty(email)){
                mLoginEmail.error = "Email field is blank"
                return@setOnClickListener
            }

            if(!email.isValidEmail()){
                mLoginEmail.error = "Not an Email"
                return@setOnClickListener
            }
            if(!email.isLsdiEmail()){
                mLoginEmail.error = "Not an LSDi Email"
                return@setOnClickListener
            }

            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success: check your email box", Toast.LENGTH_LONG).show()
                    }
                }


        }


        mLoginGoogleBtn.setOnClickListener {
            alertDialog.show()
            googleSignIn()
        }


        mLoginBtn.setOnClickListener {

            val email = mLoginEmail.text.toString().trim()
            val password = mLoginPass.text.toString().trim()

            if(TextUtils.isEmpty(email)){
                mLoginEmail.error = "Email field is blank"
                return@setOnClickListener
            }

            if(!email.isValidEmail()){
                mLoginEmail.error = "Not an Email"
                return@setOnClickListener
            }
            if(!email.isLsdiEmail()){
                mLoginEmail.error = "Not an LSDi Email"
                return@setOnClickListener
            }
            if(mLoginPass.length() <=5){
                mLoginPass.error = "Password must have at least 6 digits"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        configureGoogleSignIn()

    }

    private fun googleSignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        alertDialog.dismiss()
    }

    override fun onStart() {
        super.onStart()
        print("onStart")
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            if(!user.isEmailVerified) {
                startActivity(VerifyActivity.getLaunchIntent(this))
                finish()
            }
            startActivity(MainActivity.getLaunchIntent(this))
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {

        alertDialog.show()
        // Todo:
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    alertDialog.dismiss()
                    startActivity(MainActivity.getLaunchIntent(this))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                //Todo: verificar esse operador !!
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        alertDialog.show()
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                alertDialog.dismiss()
                startActivity(MainActivity.getLaunchIntent(this))
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
            alertDialog.dismiss()

        }
    }


    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }
    // Just for debug purposes

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
        Log.d("Lifecycle", "LoginActivity: $msg")
    }



}
