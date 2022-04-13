package com.example.recipesapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.data.Users
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.data.api.UserApiService
import com.example.recipesapp.ui.FirebaseService
import com.example.recipesapp.ui.main.SearchActivity
import com.example.recipesapp.ui.view.SemiCircleView
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    var mGoogleSignInClient: GoogleSignInClient? = null
    var signInButtonGoogle: Button? = null

    private lateinit var loginButton: LoginButton
    private lateinit var fb: Button
    private lateinit var callbackManager: CallbackManager

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //This allows the window to extend outside of the screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        var pref = applicationContextKey()

        //If the user have done already the login
        if (pref!!.getBoolean(LOGIN_KEY, false) || isLoggedIn() ) {

            //Check for existing Google sign in account
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                val personId = account.id

                val BASE_URL = "https://danymrt.pythonanywhere.com"
                val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(UserApiService::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    val response = service.getUserById(personId!!.toString())
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            //Retrieve the information about the user
                            val personUsername = response.body()!![0].username
                            val personEmail = response.body()!![0].email
                            val personGivenName = response.body()!![0].name
                            val personFamilyName = response.body()!![0].familyName
                            val personPhoto = response.body()!![0].image

                            //Create a shared variable with them
                            val sharedPref = getSharedPreferences("LoginUser", MODE_PRIVATE)
                            var editor = sharedPref.edit()
                            editor.putString("id",personId)
                            editor.putString("username",personUsername)
                            editor.putString("email",personEmail)
                            editor.putString("name",personGivenName)
                            editor.putString("familyname",personFamilyName)
                            editor.putString("url",personPhoto.toString())
                            editor.apply()
                        }else{
                            Log.d("Login", response.message())
                        }
                    }
                }

            }

            if(FirebaseService.NOTIFICATION_ID==true){
                //has login
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                //must finish this activity (the main activity will not be shown when click back in main activity)
                finish()
            }else{
                //has login
                startActivity(Intent(this, SearchActivity::class.java))
                //must finish this activity (the main activity will not be shown when click back in main activity)
                finish()
            }
        } else {

            //Set the Login Layout
            setContentView(R.layout.login)
            val backgroundImage: SemiCircleView = findViewById(R.id.SplashScreenImage)

            val image:ImageView = findViewById(R.id.logo)

            //Animation and show the login button
            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                //Vertical
                val logo_scaleX = ObjectAnimator.ofFloat(image, "scaleX", 1f, 3f)
                val logo_scaleY = ObjectAnimator.ofFloat(image, "scaleY", 1f, 3f)
                val logo_traslate = ObjectAnimator.ofFloat(image, "translationY", 0f,-600f)
                val back_pivotY = ObjectAnimator.ofFloat(backgroundImage, "pivotY", 25f)
                val back_pivotX = ObjectAnimator.ofFloat(backgroundImage, "pivotX", 25f)
                val back_scale = ObjectAnimator.ofFloat(backgroundImage, "scaleY", 2f,1f)
                val transparency = ObjectAnimator.ofFloat(findViewById(R.id.title_app),"alpha", 1f,0f)

                val animationSet = AnimatorSet()
                animationSet.playTogether(
                    logo_scaleX,
                    logo_scaleY,
                    transparency,
                    logo_traslate,
                    back_pivotX,
                    back_pivotY,
                    back_scale)
                animationSet.interpolator = DecelerateInterpolator()
                animationSet.duration = 2000
                animationSet.addListener(
                    onStart = {
                        //When animation is started
                    },
                    onEnd = {
                        var layout: LinearLayout = findViewById(R.id.layout_button_login)
                        layout.visibility = View.VISIBLE
                    }
                )
                animationSet.start()
            }else{
                //horizontal
                val logo_scaleX = ObjectAnimator.ofFloat(image, "scaleX", 1f, 3f)
                val logo_scaleY = ObjectAnimator.ofFloat(image, "scaleY", 1f, 3f)
                val logo_traslate = ObjectAnimator.ofFloat(image, "translationY", 0f,-380f)
                val back_pivotY = ObjectAnimator.ofFloat(backgroundImage, "pivotY", 25f)
                val back_scale = ObjectAnimator.ofFloat(backgroundImage, "scaleY", 2f,1f)
                val transparency = ObjectAnimator.ofFloat(findViewById(R.id.title_app),"alpha", 1f,0f)

                val animationSet = AnimatorSet()
                animationSet.playTogether(
                    logo_scaleX,
                    logo_scaleY,
                    transparency,
                    logo_traslate,
                    back_pivotY,
                    back_scale)
                animationSet.interpolator = DecelerateInterpolator()
                animationSet.duration = 2000
                animationSet.addListener(
                    onStart = {
                        //When animation is started
                    },
                    onEnd = {
                        var layout: LinearLayout = findViewById(R.id.layout_button_login)
                        layout.visibility = View.VISIBLE
                    }
                )
                animationSet.start()
            }

            /**
             * GOOGLE ACCESS
             */
            //GOOGLE
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

            //Build a GoogleSignInClient with options specified by gso
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            if (mGoogleSignInClient != null){
                mGoogleSignInClient!!.signOut()
                    .addOnCompleteListener(this, OnCompleteListener{
                        signoutfun()
                    })
            }

            signInButtonGoogle = findViewById(R.id.button_login_google)
            signInButtonGoogle!!.setOnClickListener {
                val intentfb = mGoogleSignInClient!!.signInIntent
                startActivityForResult(
                    intentfb,
                    RC_SIGN_IN_FB
                )
            }

            /**
             * FACEBOOK ACCESS
             */
            FacebookSdk.sdkInitialize(this.applicationContext)
            //init
            fb = findViewById(R.id.fb)
            loginButton = findViewById(R.id.login_button)
            callbackManager = CallbackManager.Factory.create()
            loginButton.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
            }

            // Callback registration
            loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("TAG", "Success Login")
                    getUserProfile(loginResult?.accessToken, loginResult?.accessToken?.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
                }
            })

        }

    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture.type(large), email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                var facebookId=""
                // Facebook Id
                if (jsonObject!!.has("id")) {
                    facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId)
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }

                var facebookFirstName=""
                // Facebook First Name
                if (jsonObject.has("first_name")) {
                    facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                }


                var facebookMiddleName=""
                // Facebook Middle Name
                if (jsonObject.has("middle_name")) {
                    facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                }

                var facebookLastName=""
                // Facebook Last Name
                if (jsonObject.has("last_name")) {
                    facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                }

                var facebookName=""
                // Facebook Name
                if (jsonObject.has("name")) {
                    facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }

                var facebookProfilePicURL=""
                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                }
                var facebookEmail=""
                // Facebook Email
                if (jsonObject.has("email")) {
                    facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }
                var username = facebookEmail.split("@")

                addUserDB(facebookEmail,facebookId,facebookFirstName,facebookLastName,facebookProfilePicURL, username[0])
                var pref = applicationContextKey()
                pref?.edit()?.putBoolean(LOGIN_KEY, true)?.apply()

                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }).executeAsync()
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }

    fun onClick(v: View) {
        if (v === fb) {
            loginButton.performClick()
        }
    }

    private fun signoutfun() {
        //Toast.makeText(this@MainActivity, "Succefully signed out", Toast.LENGTH_LONG).show()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //result returned from launching the Intent from GoogleSigsInClient.getSignInIntent
        if (requestCode == RC_SIGN_IN_FB) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handlingSignInResult(task)
        }
    }

    private fun handlingSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val personName = account.displayName
                val personGivenName = account.givenName
                val personFamilyName = account.familyName
                val personEmail = account.email
                val personId = account.id
                val personPhoto = account.photoUrl.toString()
                var username = personEmail!!.split("@")

                addUserDB(personEmail, personId!!,personGivenName!!,personFamilyName!!,personPhoto,username[0])

            }
            var pref = applicationContextKey()
            pref?.edit()?.putBoolean(LOGIN_KEY, true)?.apply()
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        } catch (e: ApiException) {
            Log.d("message", e.toString())
        }
    }

    private fun addUserDB(email: String, id: String,name: String, family_name: String, image: String, username: String ){
        //Add the new User in the Database if it is not present
        var user = Users(email,family_name,id,image,name,username)
        val BASE_URL = "https://danymrt.pythonanywhere.com"
        val service = RetrofitBuilder.makeRetrofitService(BASE_URL).create(UserApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.addUser(user)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("DBUser1", response.message())
                    Log.d("DBUser1", response.body().toString())
                    //Shared variable for info in other fragment
                    val sharedPref = getSharedPreferences("LoginUser", MODE_PRIVATE)
                    var editor = sharedPref.edit()
                    editor.putString("id", response.body()!![0].id)
                    editor.putString("username",response.body()!![0].username)
                    editor.putString("email",response.body()!![0].email)
                    editor.putString("name",response.body()!![0].name)
                    editor.putString("familyname",response.body()!![0].familyName)
                    editor.putString("url",response.body()!![0].image)
                    editor.apply()
                }else{
                    Log.d("DBUser2", response.message())
                }
            }
        }
    }

    //Stop the backward navigation when we are in the login page
    override fun onBackPressed() {
        return
    }

    companion object {
        private const val RC_SIGN_IN_FB = 100
        val LOGIN_KEY = "LOGIN_KEY"
        private var instance: MainActivity? = null

        fun applicationContextKey() : SharedPreferences? {
            var pref = instance!!.getSharedPreferences(LOGIN_KEY,Context.MODE_PRIVATE)
            return pref
        }

        fun applicationContextLogin() : SharedPreferences? {
            var pref = instance!!.getSharedPreferences("LoginUser",Context.MODE_PRIVATE)
            return pref
        }

    }

    //Use this function for generating the HashKey for facebook
    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(ContentValues.TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(ContentValues.TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "printHashKey()", e)
        }
    }
}