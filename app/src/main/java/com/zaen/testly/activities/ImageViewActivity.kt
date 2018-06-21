package com.zaen.testly.activities

import android.app.TaskStackBuilder
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zaen.testly.GlideApp
import com.zaen.testly.R
import com.zaen.testly.base.activities.BaseActivity
import com.zaen.testly.utils.LogUtils.Companion.TAG
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_view_image.*

/**
 * Created by zaen on 4/2/18.
 */

class ImageViewActivity : BaseActivity() {
    var path: String? = null
    var ref: StorageReference? = null
    var toolbar : ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutRes = R.layout.activity_view_image
        super.onCreate(savedInstanceState)
        toolbar = this.supportActionBar
        toolbar?.setHomeAsUpIndicator(R.drawable.ic_action_close)
        toolbar?.setDisplayShowTitleEnabled(true)

        path = intent.getStringExtra("strPath")
        if (path != null) {
            ref = FirebaseStorage.getInstance().reference.child(path!!)
            GlideApp.with(this)
                    .load(ref)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.progress_bar))
                    .into(view_image_activity_view_image)
            ref!!.metadata
                    .addOnSuccessListener {
                        toolbar?.title = it.name
                    }
                    .addOnFailureListener {
                        Log.w(TAG(this),"Error getting the filename of $ref. Exception: $it")
                        Snacky.builder().setActivity(this)
                            .setText("Error getting the filename")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .error()
                            .show()
                    }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Up button
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this) // WOW!~
                if (NavUtils.shouldUpRecreateTask(this, upIntent!!)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities()
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent)
                }
                return true
            }

        // ...
        }
        return super.onOptionsItemSelected(item)
    }
}
