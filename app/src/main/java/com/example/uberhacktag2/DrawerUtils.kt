package com.example.uberhacktag2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.uberhacktag2.ui.maps.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

open class DrawerUtils : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    protected lateinit var frameLayout: FrameLayout
    private var context: Context? = null
    private var toolbar: Toolbar? = null
    lateinit var txt_menuTitle: TextView
    var txt_username: TextView? = null
    var txt_email: TextView? = null
    var txt_change_pass: TextView? = null
    var txt_card_value: TextView? = null
    lateinit var img_menuOption: ImageView
    var image_profile: ImageView? = null
    var img_menu_add_cart: ImageView? = null
    var img_menu_prac_test: FloatingActionButton? = null
    private lateinit var drawer: DrawerLayout
    private val INTENT_CAMERA_CODE = 100
    var toggle: ActionBarDrawerToggle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_home)
        context = this
        initView()
        frameLayout = findViewById<View>(R.id.container) as FrameLayout
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        txt_menuTitle = findViewById(R.id.txt_menuTitle)
        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerview = navigationView.getHeaderView(0)
        img_menuOption = findViewById(R.id.img_menuOption)

        img_menuOption.setBackgroundResource(R.drawable.ic_menu)
        img_menuOption.setOnClickListener(View.OnClickListener { drawer.openDrawer(GravityCompat.START) })

        toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle!!)
        toggle!!.isDrawerIndicatorEnabled = false
        toggle!!.syncState()
        navigationView.setNavigationItemSelectedListener(this)


    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            ExitApp()
        }
    }

    private fun ExitApp() {
        val builder =
            AlertDialog.Builder(context!!)
        builder.setTitle("Code Inside Coffee")
        builder.setMessage("Do You Want To Exit?")
        builder.setIcon(R.drawable.ic_icon)
        //final AlertDialog dialog = builder.create();
        builder.setPositiveButton(
            "YES"
        ) { dialogInterface, i -> finish() }
        builder.setNegativeButton(
            "NO"
        ) { dialogInterface, i -> }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.driver_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val intent: Intent
        if (id == R.id.nav_home) {
            Toast.makeText(
                context,
                "You Click On Home",
                Toast.LENGTH_SHORT
            ).show()
            intent = Intent(this, MapsActivity ::class.java)
            startActivity(intent)
            finish()
            // Handle the camera action
        } else if (id == R.id.nav_events) {
            Toast.makeText(
                context,
                "You Click On Galary",
                Toast.LENGTH_SHORT
            ).show()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(
                context,
                "You Click On SlideShow",
                Toast.LENGTH_SHORT
            ).show()
        } else if (id == R.id.nav_tools) {
            Toast.makeText(
                context,
                "You Click On Tools",
                Toast.LENGTH_SHORT
            ).show()
        } else if (id == R.id.nav_share) {
            Toast.makeText(
                context,
                "You Click On Share",
                Toast.LENGTH_SHORT
            ).show()
        } else if (id == R.id.nav_send) {
            Toast.makeText(
                context,
                "You Click On Send",
                Toast.LENGTH_SHORT
            ).show()
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        private const val INTENT_REQUEST_CODE = 200
    }
}