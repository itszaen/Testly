package com.zaen.testly.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zaen.testly.R
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.fragments.*
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener {
    val transaction = supportFragmentManager
    var tool_bar : Toolbar? = null
    lateinit private var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        checkFirstRun()

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        nav_view.setNavigationItemSelectedListener(this)
        toggle.syncState()

        tool_bar = toolbar

        if (fragment_container != null) {
            if (savedInstanceState != null) {
                return;
            }
            val dbFragment = DashboardFragment()
            dbFragment.setArguments(getIntent().getExtras())
            transaction.beginTransaction()
                    .add(R.id.fragment_container, dbFragment).commit()
        }
    }

    override fun onStart() {
        super.onStart()
        // Firebase check if already signed-in
        var currentUser: FirebaseUser? = mAuth.getCurrentUser()
        updateUI(currentUser)

    }

    override fun onResume() {
        super.onResume()
    }
//
//    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onPostCreate(savedInstanceState, persistentState)
//        toggle.syncState()
//    }

    fun forceCrash(view: View) {
        throw RuntimeException("This is a crash")
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
//            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> onFragmentClicked(DashboardFragment(),getString(R.string.app_name))
            R.id.nav_pinned -> onFragmentClicked(PinnedFragment(),getString(R.string.title_fragment_pinned))
            R.id.nav_prep -> onFragmentClicked(PrepFragment(),getString(R.string.title_fragment_prep))
            R.id.nav_improve -> onFragmentClicked(ImproveFragment(),getString(R.string.title_fragment_improve))
            R.id.nav_handouts -> onFragmentClicked(HandoutsFragment(),getString(R.string.title_fragment_handouts))
            R.id.nav_pastexam -> onFragmentClicked(PastexamFragment(),getString(R.string.title_fragment_pastexam))
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_help -> {
                val intent = Intent(this, HelpActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_feedback -> {
                val intent = Intent(this, FeedbackActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun onFragmentClicked(newFragment:Fragment,title:String){
        val args = Bundle()
        newFragment.setArguments(args)
        transaction.beginTransaction()
                .replace(R.id.fragment_container,newFragment)
                .addToBackStack(null)
                .commit()
        tool_bar?.setTitle(title)
    }

    // Firebase stuff
    private fun updateUI(user:FirebaseUser?) {
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    fun checkFirstRun(){
        val IF_FIRST_START = getString(R.string.pref_if_firststart)
        val prefs = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val firstStart = prefs.getBoolean(IF_FIRST_START, true)
        if (firstStart){
            onFirstRun()
            val edit = prefs.edit()
            edit.putBoolean(IF_FIRST_START,false)
            edit.commit()
        }
    }

    fun onFirstRun(){

    }
}
