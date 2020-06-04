package com.example.klsdinfo
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.klsdinfo.data.database.AppDatabase
import com.example.klsdinfo.data.database.LocalUserQuery
import com.example.klsdinfo.endlessservice.*
import com.example.klsdinfo.main.MainFragments.HistoryFragment
import com.example.klsdinfo.main.MainFragments.HomeFragment
import com.example.klsdinfo.main.MainFragments.RealFragment
import com.example.klsdinfo.main.MainFragments.SecurityFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

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
    lateinit var MY_EMAIL : String


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if(user == null){

            FirebaseAuth.getInstance().signOut()

            startActivity(LoginActivity.getLaunchIntent(this))
            finish()

        }else{ // Todo : Tratar esse !! operator
            configureGoogleSignIn()
            if(user.email != null) {

                AsyncTask.execute {
                    AppDatabase.getInstance(applicationContext!!)?.localUserDao()?.insert(LocalUserQuery(0, user.email!!))
                    AppDatabase.destroyInstance()
                }


                if(true){//user.email!!.isLsdiEmail()){//user.email!!.isLsdiEmail()){ //descomente essa linha para permitir apenas email LSDI

                    MY_EMAIL = user.email!!

                    setupAll(user)

                    if (savedInstanceState == null) {
                        navView.setCheckedItem(R.id.nav_home)
                        navigateToFragment(HomeFragment.newInstance())
                        title = "Home Page"
                    }
                }else{
                    FirebaseAuth.getInstance().signOut()
                    mGoogleSignInClient.signOut()
                    startActivity(LoginActivity.getLaunchIntent(this))
                    finish()
                }
            }else{
                FirebaseAuth.getInstance().signOut()
                mGoogleSignInClient.signOut()
                startActivity(VerifyActivity.getLaunchIntent(this))
                finish()
            }
        }


//        setupAll(user)

//        if (savedInstanceState == null) {
//            navView.setCheckedItem(R.id.nav_home)
//            navigateToFragment(HomeFragment.newInstance())
//            title = "Home Page"
//        }
//        Log.i("Lifecycle", "OnCreate: Main Activity")
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

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        setPermissions()


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
            R.id.action_start_service ->  {
                log("START THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.START)
                true
            }

            R.id.action_stop_service -> {
                log("STOP THE FOREGROUND SERVICE ON DEMAND")
                actionOnService(Actions.STOP)
                true
            }
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
                Toast.makeText(baseContext, "Accessing History", Toast.LENGTH_LONG).show()
                clearBackStack()
                navigateToFragment(HistoryFragment.newInstance())
                title = "Accessing History"
            }
            R.id.nav_security -> {
                Toast.makeText(baseContext, "Security Service", Toast.LENGTH_LONG).show()
                clearBackStack()
                navigateToFragment(SecurityFragment.newInstance(applicationContext))
                title = "Security Service"
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

    fun String.isLsdiEmail(): Boolean{

        if (this.domain == "lsdi.ufma.br"){
            return true
        }
        return false
    }



    private fun setPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE), 1
            )
        }
    }



    private fun actionOnService(action: Actions) {

        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return

        val bundle : Bundle = Bundle().also{
            it.putString("email", MY_EMAIL)
            it.putString("action", action.name)
        }

        Intent(this, EndlessService::class.java).also {
            it.putExtras(bundle)
//            it.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }



}
