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
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.entypo_typeface_library.Entypo
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.materialize.color.Material
import com.mikepenz.octicons_typeface_library.Octicons
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeHelper
import com.stephentuso.welcome.WelcomeHelper.DEFAULT_WELCOME_SCREEN_REQUEST
import com.zaen.testly.FirebaseAuthUser
import com.zaen.testly.Global
import com.zaen.testly.Global.Companion.userinfoRef
import com.zaen.testly.R
import com.zaen.testly.R.id.*
import com.zaen.testly.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(),
//        NavigationView.OnNavigationItemSelectedListener,
        FileBrowserFragment.RenameToolbar{
    companion object {
        const val TAG = "MainActivity"
    }

    var savedInstanceState: Bundle? = null
    private val transaction = supportFragmentManager
    private var mToolbar : Toolbar? = null

    // Material Drawer
    private var drawer: Drawer? = null
    private var hasProviderItem: Boolean? = null
    private var hasDeveloperItem: Boolean? = null
    /// Items
    val nav_home = PrimaryDrawerItem().withName(R.string.nav_menu_home).withIcon(GoogleMaterial.Icon.gmd_home)
            .withIdentifier(1).withSelectable(true)
    val nav_pinned = PrimaryDrawerItem().withName(R.string.nav_menu_pinned).withIcon(CommunityMaterial.Icon.cmd_pin)
            .withIdentifier(2).withSelectable(true)
    val nav_prep = PrimaryDrawerItem().withName(R.string.nav_menu_prep).withIcon(Octicons.Icon.oct_checklist)
            .withIdentifier(3).withSelectable(true)
    val nav_improve = PrimaryDrawerItem().withName(R.string.nav_menu_improve).withIcon(MaterialDesignIconic.Icon.gmi_labels)
            .withIdentifier(4).withSelectable(true)
    val nav_handouts = PrimaryDrawerItem().withName(R.string.nav_menu_handouts).withIcon(MaterialDesignIconic.Icon.gmi_file)
            .withIdentifier(5).withSelectable(true)
    val nav_pastexam = PrimaryDrawerItem().withName(R.string.nav_menu_pastexam).withIcon(CommunityMaterial.Icon.cmd_archive)
            .withIdentifier(6).withSelectable(true)
    val nav_section_provider = SectionDrawerItem().withName(R.string.nav_menu_section_provider)
            .withIdentifier(7)
    val nav_create = PrimaryDrawerItem().withName(R.string.nav_menu_create).withIcon(CommunityMaterial.Icon.cmd_file_plus)
            .withIdentifier(71).withSelectable(true)
    val nav_upload = PrimaryDrawerItem().withName(R.string.nav_menu_upload).withIcon(CommunityMaterial.Icon.cmd_upload)
            .withIdentifier(72).withSelectable(true)
    val nav_section_developer = SectionDrawerItem().withName(R.string.nav_menu_section_developer)
            .withIdentifier(8)
    val nav_dev_chat = PrimaryDrawerItem().withName(R.string.nav_menu_dev_chat).withIcon(GoogleMaterial.Icon.gmd_free_breakfast)
            .withIdentifier(81).withSelectable(true)
    val nav_settings = PrimaryDrawerItem().withName(R.string.nav_menu_settings).withIcon(GoogleMaterial.Icon.gmd_settings)
            .withIdentifier(100).withSelectable(true)
    val nav_help = PrimaryDrawerItem().withName(R.string.nav_menu_help).withIcon(Entypo.Icon.ent_help_with_circle)
            .withIdentifier(101).withSelectable(true)
    val nav_feedback = PrimaryDrawerItem().withName(R.string.nav_menu_feedback).withIcon(GoogleMaterial.Icon.gmd_feedback)
            .withIdentifier(102).withSelectable(true)
    val nav_about = PrimaryDrawerItem().withName(R.string.nav_menu_about).withIcon(Entypo.Icon.ent_info_with_circle)
            .withIdentifier(103).withSelectable(true)


    private var welcomeScreen:WelcomeHelper? = null

    private var mAuth: FirebaseAuth? = null
    private var mAuthUser: FirebaseAuthUser? = null
    private var userinfoSnapshot: DocumentSnapshot? = null

    override fun onCreate(bundle: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        mAuthUser = FirebaseAuthUser()
        savedInstanceState = bundle
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mToolbar = toolbar

        if (!mAuthUser!!.isSignedIn()){
            welcome()
        } else {

            // Firebase
            /// Userinfo set up listener & Hide (or show)elements
            val userinfoRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            userinfoRef.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.w(TAG, "Userinfo listen failed. Exception: $exception")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Userinfo listened. Current data: ${snapshot.data}")
                    onUserinfoUpdate(snapshot)
                } else {
                    Log.d(TAG, "Userinfo listened. Current data: null")
                    onUserinfoUpdate(null)
                }
            }

            // Navigation Drawer
            /// Two strings are for accessibility functions.
//        val toggle = ActionBarDrawerToggle(
//                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(toggle)
//        nav_view.setNavigationItemSelectedListener(this)
////        nav_view.setBackgroundColor(ContextCompat.getColor(this,R.color.accent_green))
//        toggle.syncState()

            /// Build
            drawer = DrawerBuilder().withActivity(this).withToolbar(toolbar)
                    .withTranslucentStatusBar(false)
                    .withHeader(R.layout.header_drawer_main)
                    .addDrawerItems(
                            nav_home,
                            nav_pinned,
                            DividerDrawerItem(),
                            nav_prep,
                            nav_improve,
                            nav_handouts,
                            nav_pastexam,
                            nav_section_provider,
                            nav_create,
                            nav_upload,
                            nav_section_developer,
                            nav_dev_chat,
                            DividerDrawerItem(),
                            nav_help,
                            nav_feedback,
                            nav_about
                    )
                    .addStickyDrawerItems(
                            nav_settings
                    )
                    .withOnDrawerItemClickListener { view, position, drawerItem ->
                        if (drawerItem != null) {
                            var intent: Intent? = null
                            when (drawerItem.identifier) {
                                1L -> onFragmentClicked(DashboardFragment(), getString(R.string.app_name))
                                2L -> onFragmentClicked(PinnedFragment(), getString(R.string.title_fragment_pinned))
                                3L -> onFragmentClicked(PrepFragment(), getString(R.string.title_fragment_prep))
                                4L -> onFragmentClicked(ImproveFragment(), getString(R.string.title_fragment_improve))
                                5L -> onFragmentClicked(HandoutsFragment(), getString(R.string.title_fragment_handouts))
                                6L -> onFragmentClicked(PastexamFragment(), getString(R.string.title_fragment_pastexam))
                                71L -> onFragmentClicked(CreateFragment(), getString(R.string.title_fragment_create))
                                72L -> onFragmentClicked(UploadFragment(), getString(R.string.title_fragment_upload))
                                81L -> onFragmentClicked(DeveloperChatFragment(), getString(R.string.title_fragment_dev_chat))
                                100L -> intent = Intent(this, SettingsActivity::class.java)
                                101L -> intent = Intent(this, HelpActivity::class.java)
                                102L -> intent = Intent(this, FeedbackActivity::class.java)
                                103L -> intent = Intent(this, AboutActivity::class.java)
                            }
                            if (intent != null) {
                                startActivity(intent)
                            }
                            drawer?.closeDrawer()
                        }
                        return@withOnDrawerItemClickListener true
                    }
                    .withSavedInstance(savedInstanceState)
                    .build()
            //// has provider & developer
            hasProviderItem = true
            hasDeveloperItem = true
            /// Config
            if (savedInstanceState == null) {
                drawer?.setSelection(1, false)
            }

            // Initialize with Dashboard
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
    }

    override fun onStart() {
        super.onStart()
        // Firebase check if already signed-in
        if (!mAuthUser!!.isSignedIn()){
            welcome()
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
        drawer?.saveInstanceState(outState)
        welcomeScreen?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onBackPressed() {
        if (drawer != null && drawer!!.isDrawerOpen) {
            drawer!!.closeDrawer()
        }else{
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

    fun welcome(){
        welcomeScreen = WelcomeHelper(this,IntroActivity::class.java)
        welcomeScreen?.show(savedInstanceState,DEFAULT_WELCOME_SCREEN_REQUEST)
    }

    private fun onUserinfoUpdate(snapshot: DocumentSnapshot?){
        userinfoSnapshot = snapshot
        toggleProviderDrawerItems()
    }
    private fun toggleProviderDrawerItems(){
        if (userinfoSnapshot != null){
            if (userinfoSnapshot!!.data!!["provider"] as Boolean) {
                if (hasProviderItem == false){
                    addDrawerItem(arrayOf(
                            nav_section_provider,
                            nav_create,
                            nav_upload
                    ))
                }
            } else {
                deleteDrawerItem(arrayOf(7,71,72))
                hasProviderItem = false
            }
            if (userinfoSnapshot!!.data!!["developer"] as Boolean){
                if (hasDeveloperItem == false){
                    addDrawerItem(arrayOf(
                            nav_section_developer,
                            nav_dev_chat
                    ))
                }
            } else {
                deleteDrawerItem(arrayOf(8,81))
                hasDeveloperItem = false
            }
        }else{
            deleteDrawerItem(arrayOf(7,71,72))
            deleteDrawerItem(arrayOf(8,81))
            hasProviderItem = false
            hasDeveloperItem = false
        }
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
    private fun addDrawerItem(items: Array<Any>){
        for (item in items){
            when (item){
                is PrimaryDrawerItem -> drawer?.addItem(item)
                is SecondaryDrawerItem -> drawer?.addItem(item)
                is DividerDrawerItem -> drawer?.addItem(item)
                is SectionDrawerItem -> drawer?.addItem(item)
            }
        }
    }
    private fun deleteDrawerItem(identifier: Array<Int>){
        for (i in identifier) {drawer?.removeItem(i.toLong())}
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
