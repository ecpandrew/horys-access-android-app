package com.example.KLSDinfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.KLSDinfo.Historic.MainFragments.HistoryFragment
import com.example.KLSDinfo.Home.HomeFragment
import com.example.KLSDinfo.RealTime.MainFragments.RealFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView      : NavigationView
    private lateinit var toolbar      : Toolbar
    private lateinit var toggle       : ActionBarDrawerToggle
    lateinit var mGoogleSignInClient  : GoogleSignInClient
    lateinit var mGoogleSignInOptions : GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)

        super.onCreate(savedInstanceState)

        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if(user == null){
            FirebaseAuth.getInstance().signOut()
            startActivity(LoginActivity.getLaunchIntent(this))
            finish()
        }else{

            setupAll(user)
            configureGoogleSignIn()
        }







        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.nav_home)
            navigateToFragment(HomeFragment.newInstance())
            title = "Home Page"
        }
        Log.i("Lifecycle", "OnCreate: Main Activity")
    }
//

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupAll(user: FirebaseUser){

        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)


        navView = findViewById(R.id.nav_view)



        val header: View = navView.getHeaderView(0)
        (header.findViewById(R.id.user_name) as TextView).text = user.displayName
        (header.findViewById(R.id.user_email) as TextView).text = user.email

        if(user.photoUrl.toString().isNullOrBlank()){
            (header.findViewById(R.id.user_image) as ImageView).setImageDrawable(getDrawable(R.mipmap.ic_aluno))
        }else{
            Picasso.get().load(user.photoUrl.toString()).into((header.findViewById(R.id.user_image) as ImageView))
        }

        toggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }
        }
        drawerLayout.addDrawerListener(toggle)

//        toggle.isDrawerIndicatorEnabled = true
//        toggle.setHomeAsUpIndicator(R.mipmap.logo_round_round)

//        toggle.syncState()
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.lsdi_transparent)

        navView.setNavigationItemSelectedListener(this)



    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(START)) {
            drawerLayout.closeDrawer(START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                clearBackStack()
                navigateToFragment(HomeFragment.newInstance())
                title = "Home Page"
            }
            R.id.nav_gallery -> {
                Toast.makeText(baseContext,"Happening Now", Toast.LENGTH_LONG).show()
                title = "Happening Now"
                clearBackStack()
                navigateToFragment(RealFragment.newInstance())
            }
            R.id.nav_slideshow -> {
                Toast.makeText(baseContext, "Acessando Histórico", Toast.LENGTH_LONG).show()
                clearBackStack()
                navigateToFragment(HistoryFragment.newInstance())
                title = "Acessando Histórico"
            }

            R.id.nav_report_bug -> {

                val recipient = "andreluizalmeidacardoso@gmail.com"
                val subject = "Report bugs/ Suggestions"
                val message = "Write your message here!"
                //method call for email intent with these inputs as parameters
                sendEmail(recipient, subject, message)

            }
            R.id.nav_logout -> {

                signOut()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(START)
        return true
    }

    private fun signOut() {
        startActivity(LoginActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut()

    }

    fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
    }

    private fun clearBackStack(){
//        val fragment: Fragment = fragManager.findFragmentById(R.id.fragment_container)!!
//        val ft: FragmentTransaction = fragManager.beginTransaction()
//        ft.remove(fragment)
//        ft.commit()
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
        Log.d("Lifecycle", "MainActivity: $msg")
    }


    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

}
