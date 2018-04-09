package com.zaen.testly.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeHelper
import com.stephentuso.welcome.WelcomeHelper.DEFAULT_WELCOME_SCREEN_REQUEST
import com.zaen.testly.R
import com.zaen.testly.R.id.toolbar
import com.zaen.testly.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        FileBrowserFragment.RenameToolbar{

    private val transaction = supportFragmentManager
    var mToolbar : Toolbar? = null
    private var welcomeScreen:WelcomeHelper? = null
    private var mAuth: FirebaseAuth? = null
    var savedInstanceState: Bundle? = null

    override fun onCreate(bundle: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        savedInstanceState = bundle
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Two strings are for accessibility functions.
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        nav_view.setNavigationItemSelectedListener(this)
//        nav_view.setBackgroundColor(ContextCompat.getColor(this,R.color.accent_green))
        toggle.syncState()

        mToolbar = toolbar

        if (fragment_container != null) {
            if (savedInstanceState != null) {
                return
            }
            val dbFragment = DashboardFragment()
            dbFragment.arguments = intent.extras
            transaction.beginTransaction()
                    .add(R.id.fragment_container, dbFragment)
                    .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        // Firebase check if already signed-in
        val currentUser: FirebaseUser? = mAuth?.currentUser
        updateUI(currentUser)
        if (currentUser == null) {
            welcomeScreen = WelcomeHelper(this,IntroActivity::class.java)
            welcomeScreen?.show(savedInstanceState,DEFAULT_WELCOME_SCREEN_REQUEST)
        }
    }

    //
//    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onPostCreate(savedInstanceState, persistentState)
//        toggle.syncState()
//    }

    override fun onDestroy() {
        super.onDestroy()
        savedInstanceState = null
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        welcomeScreen?.onSaveInstanceState(outState)
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
            //R.id.action_settings -> return true
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

    private fun onFragmentClicked(newFragment:Fragment, title:String){
        val args = Bundle()
        newFragment.arguments = args
        transaction.beginTransaction()
                .replace(R.id.fragment_container,newFragment)
                .addToBackStack(null)
                .commit()
        mToolbar?.title = title
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Intro
        if (requestCode == WelcomeHelper.DEFAULT_WELCOME_SCREEN_REQUEST) {
            // The key of the welcome screen is in the Intent
            val welcomeKey = data.getStringExtra(WelcomeActivity.WELCOME_SCREEN_KEY)

            if (resultCode == Activity.RESULT_OK) {
                // do something
            } else {
                finish() //Close app
            }

        }

    }

    override fun renameToolbar(new: String) {
        mToolbar?.title = new
    }


}
