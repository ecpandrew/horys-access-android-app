package com.example.KLSDinfo
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
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.GravityCompat.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.util.*
import com.example.KLSDinfo.Adapters.SimpleExpandableAdapter
import com.example.KLSDinfo.Fragments.HistoryFragment
import com.example.KLSDinfo.Fragments.HomeFragment
import com.example.KLSDinfo.Fragments.RealFragment
import kotlin.collections.LinkedHashMap


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val items: Array<String> = arrayOf("Home Page","Acontecendo Agora","Acessando Histórico")
    private lateinit var listTitle: List<String>
    private lateinit var listChild: Map<String, List<String>>
    private lateinit var adapter: SimpleExpandableAdapter
    private lateinit var drawerLayout: DrawerLayout
//    private lateinit var expListView: ExpandableListView
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    private val fragManager: FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAll()

        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.nav_home)
            // Todo: chamar o home fragment

            navigateToFragment(HomeFragment.newInstance())
        }
        Log.i("Lifecycle", "OnCreate: Main Activity")

    }






    private fun setupAll(){
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
//        expListView = findViewById(R.id.navList)
        navView = findViewById(R.id.nav_view)


//        genData()
//        addDrawerItem()

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
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

//    private fun addDrawerItem() {
//
//        adapter = SimpleExpandableAdapter(this, listTitle, listChild)
//        expListView.setAdapter(adapter)
//
//
//        expListView.setOnGroupClickListener { parent, v, _, id ->
//            return@setOnGroupClickListener false
//        }
//        expListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//            val selectedItem: String = listChild.getValue(listTitle[groupPosition])[childPosition]
//            title = selectedItem
//
//            if(items[0] == listTitle[groupPosition]){
//                when (childPosition) {
//                    0 -> {
//                        // Todo: Call fragment
//                        Log.i("debug", "Navigation Drawer: group=$groupPosition   child=$childPosition")
//                    }
//
//                    1 -> {
//                        // Todo: Call fragment
//                        Log.i("debug", "Navigation Drawer: group=$groupPosition   child=$childPosition")
//
//                    }
//                    2 -> {
//                        // Todo: Call fragment
//                        Log.i("debug", "Navigation Drawer: group=$groupPosition   child=$childPosition")
//                    }
//                    else -> {
//                        // Todo: Call fragment
//                        Log.i("debug", "Navigation Drawer: default")
//                    }
//
//                }
//            }
//
//
//
//            drawerLayout.closeDrawer(GravityCompat.START)
//            false
//        }
//    }

    private fun genData() {

        val titleList: List<String> = Arrays.asList("Home Page", "Acontecendo Agora", "Acessando Histórico")
        val childitem = Arrays.asList("Minhas Horas Trabalhadas", "Meus Locais Visitados")
        val childitem1 = Arrays.asList("Buscar Pessoas", "Buscar Locais")
        val childitem2 = Arrays.asList("Buscar Pessoas", "Buscar Locais")
        listChild = LinkedHashMap()
        (listChild as LinkedHashMap<String, List<String>>).put(titleList[0], childitem)
        (listChild as LinkedHashMap<String, List<String>>).put(titleList[1], childitem1)
        (listChild as LinkedHashMap<String, List<String>>).put(titleList[2], childitem2)

        listTitle = ArrayList(listChild.keys)

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
                navigateToFragment(HomeFragment.newInstance())
                title = "Home Page"
            }
            R.id.nav_gallery -> {
                Toast.makeText(baseContext, "Acontecendo Agora", Toast.LENGTH_LONG).show()
                title = "Acontecendo Agora"
                navigateToFragment(RealFragment.newInstance())

            }
            R.id.nav_slideshow -> {
                Toast.makeText(baseContext, "Acessando Histórico", Toast.LENGTH_LONG).show()
                navigateToFragment(HistoryFragment.newInstance())
                title = "Acessando Histórico"

            }

            R.id.nav_share -> {
                Toast.makeText(baseContext, "Config Not Implemented Yet!", Toast.LENGTH_LONG).show()

            }
            R.id.nav_send -> {
                Toast.makeText(baseContext, "Report Bugs Not Implemented Yet!", Toast.LENGTH_LONG).show()


            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    public fun navigateToFragment(fragToGo: Fragment, addToBackStack: Boolean = false){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
    }

    private fun removeFragment(){
        val fragment: Fragment = fragManager.findFragmentById(R.id.fragment_container)!!
        val ft: FragmentTransaction = fragManager.beginTransaction()
        ft.remove(fragment)
        ft.commit()
    }

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
