package com.example.KLSDinfo
import android.app.Activity
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    val MY_REQUEST_CODE = 1
    lateinit var providers: List<AuthUI.IdpConfig>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )




        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            setupAll(user)
        } else {
            // User is signed out
            showSignInOptions()
        }







//        if (savedInstanceState == null) {
//            navView.setCheckedItem(R.id.nav_home)
//            navigateToFragment(HomeFragment.newInstance())
//        }
        Log.i("Lifecycle", "OnCreate: Main Activity")
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
                setupAll(user)
            }else{
            }
        }
    }




    private fun setupAll(user: FirebaseUser?){

        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)


        navView = findViewById(R.id.nav_view)



        val header: View = navView.getHeaderView(0)
        (header.findViewById(R.id.user_name) as TextView).text = user?.displayName
        (header.findViewById(R.id.user_email) as TextView).text = user?.email
        Picasso.get().load(user?.photoUrl.toString()).into((header.findViewById(R.id.user_image) as ImageView))

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
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.logo_round_round)

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
                Toast.makeText(baseContext, "Report Bugs Not Implemented Yet!", Toast.LENGTH_LONG).show()
            }
            R.id.nav_logout -> {

                AuthUI.getInstance().signOut(this@MainActivity)
                    .addOnCompleteListener {
                        showSignInOptions()
                    }
                    .addOnFailureListener {
                        e -> Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    }
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(START)
        return true
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
}
