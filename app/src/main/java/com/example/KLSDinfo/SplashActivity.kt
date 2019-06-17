package com.example.KLSDinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class SplashActivity: AppCompatActivity() {

    val MY_REQUEST_CODE = 1
    lateinit var providers: List<AuthUI.IdpConfig>

//        val intent: Intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )


        showSignInOptions()
    }


    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(),
            MY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                gotoMain()
                finish()
            }else{
                Toast.makeText(this,"Error on Authentication", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun gotoMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}