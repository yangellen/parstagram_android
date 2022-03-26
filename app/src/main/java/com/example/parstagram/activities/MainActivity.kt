package com.example.parstagram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            val description = findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            submitPost(description, user)
        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener{

        }

        queryPosts()
    }

    fun submitPost(description: String, user: ParseUser){
        val post = Post()
        post.setDescrption(description)
        post.setUser(user)
        post.saveInBackground { exception ->
            if(exception != null){
                Log.e(TAG,"Error while saving post")
                exception.printStackTrace()
                Toast.makeText(this, "Failed to submit", Toast.LENGTH_SHORT).show()
            }else{
                Log.e(TAG,"Successfully saved post")
                Toast.makeText(this, "Successfully saved post", Toast.LENGTH_SHORT).show()
            }
        }

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