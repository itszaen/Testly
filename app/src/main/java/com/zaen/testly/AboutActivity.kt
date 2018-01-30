package com.zaen.testly

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aboutPage = AboutPage(this)
                .setDescription("a")
                .isRTL(false)
                .setImage(R.drawable.kaisei)
                .addItem(Element().setTitle("Version 1.0"))
                .addGroup("Contact")
                .addEmail("chouzaen2002@gmail.com")
                .addTwitter("zaen200233")
                //.addPlayStore("com.")
                .addGitHub("zaen323")
                .create()
        setContentView(aboutPage)
//        setContentView(R.layout.activity_about)
    }
}
