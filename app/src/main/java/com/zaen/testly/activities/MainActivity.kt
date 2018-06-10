package com.zaen.testly.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.entypo_typeface_library.Entypo
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem
import com.mikepenz.octicons_typeface_library.Octicons
import com.stephentuso.welcome.WelcomeActivity
import com.stephentuso.welcome.WelcomeHelper
import com.zaen.testly.R
import com.zaen.testly.TestlyUser
import com.zaen.testly.activities.base.BaseActivity
import com.zaen.testly.data.UserData
import com.zaen.testly.fragments.*
import com.zaen.testly.fragments.base.BaseFragment
import com.zaen.testly.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity(),
        FileBrowserFragment.RenameToolbar{
    companion object {
        const val REQUEST_WELCOME_SCREEN_RESULT = 13
    }

    var savedInstanceState: Bundle? = null
    private var mToolbar : Toolbar? = null

    // Fragments
    private var mContent: Fragment? = null

    // Material Drawer
    private var drawer: Drawer? = null
    private var hasProviderItem: Boolean = false
    private var hasDeveloperItem: Boolean = false
    private var hasAdminItem: Boolean = false
    /// Items
    private val homeNavId = 1L
    private val homeNav = PrimaryDrawerItem().withName(R.string.nav_menu_home).withIcon(GoogleMaterial.Icon.gmd_home)
            .withIdentifier(homeNavId).withSelectable(true)
    private val pinnedNavId = 2L
    private val pinnedNav = PrimaryDrawerItem().withName(R.string.nav_menu_pinned).withIcon(CommunityMaterial.Icon.cmd_pin)
            .withIdentifier(pinnedNavId).withSelectable(true)
    private val prepNavId = 3L
    private val prepNav = PrimaryDrawerItem().withName(R.string.nav_menu_prep).withIcon(Octicons.Icon.oct_checklist)
            .withIdentifier(prepNavId).withSelectable(true)
    private val improveNavId = 4L
    private val improveNav = PrimaryDrawerItem().withName(R.string.nav_menu_improve).withIcon(MaterialDesignIconic.Icon.gmi_labels)
            .withIdentifier(improveNavId).withSelectable(true)
    private val handoutsNavId = 5L
    private val handoutsNav = PrimaryDrawerItem().withName(R.string.nav_menu_handouts).withIcon(MaterialDesignIconic.Icon.gmi_file)
            .withIdentifier(handoutsNavId).withSelectable(true)
    private val pastexamNavId = 6L
    private val pastexamNav = PrimaryDrawerItem().withName(R.string.nav_menu_pastexam).withIcon(CommunityMaterial.Icon.cmd_archive)
            .withIdentifier(pastexamNavId).withSelectable(true)
    private val providerNavSectionId = 7L
    private val providerNavSection = SectionDrawerItem().withName(R.string.nav_menu_section_provider)
            .withIdentifier(providerNavSectionId)
    private val createNavId = 71L
    private val createNav = PrimaryDrawerItem().withName(R.string.nav_menu_create).withIcon(CommunityMaterial.Icon.cmd_file_plus)
            .withIdentifier(createNavId).withSelectable(true)
    private val uploadNavId = 72L
    private val uploadNav = PrimaryDrawerItem().withName(R.string.nav_menu_upload).withIcon(CommunityMaterial.Icon.cmd_upload)
            .withIdentifier(uploadNavId).withSelectable(true)
    private val developerNavSectionId = 8L
    private val developerNavSection = SectionDrawerItem().withName(R.string.nav_menu_section_developer)
            .withIdentifier(developerNavSectionId)
    private val devChatNavId = 81L
    private val devChatNav = PrimaryDrawerItem().withName(R.string.nav_menu_dev_chat).withIcon(GoogleMaterial.Icon.gmd_free_breakfast)
            .withIdentifier(devChatNavId).withSelectable(true)
    private val adminNavSectionId = 9L
    private val adminNavSection = SectionDrawerItem().withName(R.string.nav_menu_section_admin)
            .withIdentifier(adminNavSectionId)
    private val adminPanelNavId = 91L
    private val adminPanelNav = PrimaryDrawerItem().withName(R.string.nav_menu_admin_panel).withIcon(GoogleMaterial.Icon.gmd_security)
            .withIdentifier(adminPanelNavId)
    private val settingsNavId = 100L
    private val settingsNav = PrimaryDrawerItem().withName(R.string.nav_menu_settings).withIcon(GoogleMaterial.Icon.gmd_settings)
            .withIdentifier(settingsNavId).withSelectable(true)
    private val helpNavId = 101L
    private val helpNav = PrimaryDrawerItem().withName(R.string.nav_menu_help).withIcon(Entypo.Icon.ent_help_with_circle)
            .withIdentifier(helpNavId).withSelectable(true)
    private val feedbackNavId = 102L
    private val feedbackNav = PrimaryDrawerItem().withName(R.string.nav_menu_feedback).withIcon(GoogleMaterial.Icon.gmd_feedback)
            .withIdentifier(feedbackNavId).withSelectable(true)
    private val aboutNavId = 103L
    private val aboutNav = PrimaryDrawerItem().withName(R.string.nav_menu_about).withIcon(Entypo.Icon.ent_info_with_circle)
            .withIdentifier(aboutNavId).withSelectable(true)
    
    private var welcomeScreen :WelcomeHelper? = null

    private var mAuthUser = TestlyUser(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_main
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null){
            mContent = supportFragmentManager.getFragment(savedInstanceState,"lastFragment")
        }

        if (!mAuthUser.isSignedIn()){
            welcomeScreen = WelcomeHelper(this,IntroActivity::class.java)
            welcomeScreen?.show(savedInstanceState)
            return
        }

        // listen to sign in state
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser == null){
                val intent =  baseContext.packageManager.getLaunchIntentForPackage(
                        baseContext.packageName
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(intent)
            }
        }

        setSupportActionBar(toolbar)
        mToolbar = toolbar
        // Firebase
        /// Userinfo SET up listener & Hide (or show)elements
        mAuthUser.addUserinfoListener(object: TestlyUser.UserinfoListener{
            override fun onUserinfoUpdate(userinfo: UserData?) {
                toggleProviderDrawerItems()
            }
        })

        /// build
        drawer = DrawerBuilder().withActivity(this).withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withHeader(R.layout.header_drawer_main)
                .addDrawerItems(
                        homeNav,
                        pinnedNav,
                        DividerDrawerItem(),
                        prepNav,
                        improveNav,
                        handoutsNav,
                        pastexamNav,
                        providerNavSection,
                        createNav,
                        uploadNav,
                        this.developerNavSection,
                        devChatNav,
                        adminNavSection,
                        adminPanelNav,
                        DividerDrawerItem(),
                        this.helpNav,
                        feedbackNav,
                        aboutNav
                )
                .addStickyDrawerItems(
                        settingsNav
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    if (drawerItem != null) {
                        var intent: Intent? = null
                        when (drawerItem.identifier) {
                            homeNavId       -> onFragmentClicked(DashboardFragment(), "dashboard", getString(R.string.app_name))
                            pinnedNavId     -> onFragmentClicked(PinnedFragment(), "pinned", getString(R.string.title_fragment_pinned))
                            prepNavId       -> onFragmentClicked(PrepFragment(), "prep", getString(R.string.title_fragment_prep))
                            improveNavId    -> onFragmentClicked(ImproveFragment(), "improve", getString(R.string.title_fragment_improve))
                            handoutsNavId   -> onFragmentClicked(HandoutsFragment(), "handouts", getString(R.string.title_fragment_handouts))
                            pastexamNavId   -> onFragmentClicked(PastexamFragment(), "pastexam", getString(R.string.title_fragment_pastexam))
                            createNavId     -> onFragmentClicked(CreateCasFragment(), "create", getString(R.string.title_fragment_create))
                            uploadNavId     -> onFragmentClicked(UploadFragment(), "upload", getString(R.string.title_fragment_upload))
                            devChatNavId    -> onFragmentClicked(DeveloperChatFragment(), "devchat", getString(R.string.title_fragment_dev_chat))
                            adminPanelNavId -> onFragmentClicked(AdminPanelFragment(),"adminpanel",getString(R.string.title_fragment_admin_panel))
                            settingsNavId   -> intent = Intent(this, SettingsActivity::class.java)
                            helpNavId       -> intent = Intent(this, HelpActivity::class.java)
                            feedbackNavId   -> intent = Intent(this, FeedbackActivity::class.java)
                            aboutNavId      -> intent = Intent(this, AboutActivity::class.java)
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
        hasAdminItem = true
        /// Config
        if (savedInstanceState == null) {
            drawer?.setSelection(1, false)
        }

        // Initialize with Dashboard
        if (fragment_container_activity_main != null) {
            if (savedInstanceState != null) {
                return
            }
            loadRootFragment(R.id.fragment_container_activity_main, DashboardFragment(), true, true)

        }
    }

    override fun attachBaseContext(base: Context?) {
        // Iconics
        super.attachBaseContext(IconicsContextWrapper.wrap(base))
    }

    override fun onStart() {
        super.onStart()
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
        drawer?.saveInstanceState(outState)
        welcomeScreen?.onSaveInstanceState(outState)

        // Fragments
        supportFragmentManager.putFragment(outState,"lastFragment",mContent)
    }

//    override fun onBackPressed() {
//        if (drawer != null && drawer!!.isDrawerOpen) {
//            drawer!!.closeDrawer()
//        } else if (supportFragmentManager.backStackEntryCount != 0) {
//            supportFragmentManager.popBackStack()
//        } else {
//            super.onBackPressed()
//        }
//    }

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

    private fun toggleProviderDrawerItems(){
        val userinfo = mAuthUser.userinfo
        if (userinfo != null){
            if (userinfo.isProvider) {
                if (!hasProviderItem){
                    addDrawerItem(arrayOf(providerNavSection, createNav, uploadNav))
                }
            } else {
                deleteDrawerItem(arrayOf(providerNavSectionId,createNavId,uploadNavId))
                hasProviderItem = false
            }
            if (userinfo.isDeveloper){
                if (!hasDeveloperItem){
                    addDrawerItem(arrayOf(this.developerNavSection, devChatNav))
                }
            } else {
                deleteDrawerItem(arrayOf(developerNavSectionId,devChatNavId))
                hasDeveloperItem = false
            }
            if (userinfo.isAdmin){
                if (!hasAdminItem){
                    addDrawerItem((arrayOf(adminNavSection, adminPanelNav)))
                }
            } else {
                deleteDrawerItem(arrayOf(adminNavSectionId,adminPanelNavId))
                hasAdminItem = false
            }
        }else{
            deleteDrawerItem(arrayOf(providerNavSectionId,createNavId,uploadNavId))
            hasProviderItem = false
            deleteDrawerItem(arrayOf(developerNavSectionId,devChatNavId))
            hasDeveloperItem = false
            deleteDrawerItem(arrayOf(adminNavSectionId,adminPanelNavId))
            hasAdminItem = false
        }
    }

    private fun onFragmentClicked(newFragment: BaseFragment, tag:String, title:String){
        start(newFragment)
        mToolbar?.title = title
//        val args = Bundle()
//
//        var fragment = supportFragmentManager.findFragmentByTag(tag)
//        if (fragment == null){
//            fragment = newFragment
//        }
//        fragment.arguments = args
//
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container,fragment,tag)
//                .addToBackStack(null)
//                .commit()
//
//        mToolbar?.title = title
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
    private fun deleteDrawerItem(identifier: Array<Long>){
        for (i in identifier) {drawer?.removeItem(i)}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Intro
        if (requestCode == WelcomeHelper.DEFAULT_WELCOME_SCREEN_REQUEST) {
            // The key of the welcome screen is in the Intent
            val welcomeKey = data.getStringExtra(WelcomeActivity.WELCOME_SCREEN_KEY)
            if (resultCode == Activity.RESULT_OK) {
                LogUtils.success(this,3,"Welcome screen completed. Restarting...")
                val intent =  baseContext.packageManager.getLaunchIntentForPackage(
                        baseContext.packageName
                )
                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(intent)
            } else {
                finish() //Close app
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun renameToolbar(new: String) {
        mToolbar?.title = new
    }
}
