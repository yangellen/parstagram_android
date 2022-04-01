package com.example.parstagram.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parstagram.R
import com.example.parstagram.activities.fragments.ComposeFragment
import com.example.parstagram.activities.fragments.FeedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnLogout).setOnClickListener{
            logout()
        }

        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->

            var fragmentToShow: Fragment? = null

            when(item.itemId){

                R.id.action_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    fragmentToShow = FeedFragment()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                }
                R.id.action_profile -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()

                }

            }

            if (fragmentToShow != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }
            true
        }
        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home

        queryPosts()
    }

    fun logout(){
        ParseUser.logOut()
        goToLoginActivity()
    }

    private fun goToLoginActivity(){
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Query for all posts
    fun queryPosts(){
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.include(Post.KEY_USER)
        query.findInBackground(object : FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null){
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if(posts != null){
                        for (post in posts){
                            Log.i(TAG, "Post " + post.getDescription() + " ,username: "
                            + post.getUser()?.username)
                        }
                    }
                }
            }
        })
    }
    companion object {
        const val TAG = "MainActivity"
    }
}