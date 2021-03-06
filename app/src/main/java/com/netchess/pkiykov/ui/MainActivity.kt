package com.netchess.pkiykov.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.netchess.pkiykov.R
import com.netchess.pkiykov.core.App
import com.netchess.pkiykov.core.interactors.FacebookInteractor
import com.netchess.pkiykov.core.interactors.FirebaseInteractor
import com.netchess.pkiykov.core.interactors.GoogleInteractor
import com.netchess.pkiykov.ui.NavigationItem.*
import com.netchess.pkiykov.ui.screens.login.view.LoginView
import com.netchess.pkiykov.ui.screens.onBoarding.view.OnBoardingView
import com.netchess.pkiykov.ui.screens.profile.view.ProfileView
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var firebaseInteractor: FirebaseInteractor
    @Inject
    lateinit var googleInteractor: GoogleInteractor
    @Inject
    lateinit var facebookInteractor: FacebookInteractor

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var isLoggedIn = false
    override val contentViewId = R.layout.activity_main
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        App.applicationComponent.inject(this)
        initViews()

        signOut()

        isLoggedIn = firebaseInteractor.isLoggedIn()

        if (getMainFragment() == null) {
            openFirstScreen()
        }
    }

    private fun openLoginScreen() {
        fragment = LoginView.getInstance()
        setFragment(R.id.container, fragment!!, true)
    }

    /* override fun onStart() {
         super.onStart()
         // Check if user is still signed in
         checkIfUserIsSignedIn()
         val account = GoogleSignIn.getLastSignedInAccount(this)
         // updateUI(account)
     }*/


    private fun initViews() {
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back)
        }
        drawerLayout = findViewById(R.id.drawerLayout)
        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()
            }
        }

        drawerLayout.addDrawerListener(drawerToggle)

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            val id = menuItem.itemId
            when (NavigationItem.getItemById(id)) {
                CURRENT_GAME,
                NEW_GAME,
                PROFILE -> {
                    selectFragment(Screen.PROFILE)
                }
                RANK_LIST -> {
                }
                EXIT -> {
                    finish()
                }
            }
            true
        }
        drawerLayout.post { enableNavigationItem(CURRENT_GAME, false) }
    }

    fun signInWithGoogle() {
        val signInIntent = googleInteractor.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun openFirstScreen() {
        fragment = if (isLoggedIn) {
            ProfileView.getInstance()
        } else {
            LoginView.getInstance()
        }
        setFragment(R.id.container, fragment!!, true)
    }


    override fun selectFragment(fragmentName: IScreen, data: Bundle?, addToBackStack: Boolean, animation: FragmentAnimation?) {
        var shouldAddToBackStack = addToBackStack
        if (fragmentName is Screen) {
            when (fragmentName) {
                Screen.PROFILE -> {
                    shouldAddToBackStack = true
                    fragment = ProfileView.getInstance(data)
                }
                Screen.ON_BOARDING -> {
                    shouldAddToBackStack = true
                    fragment = OnBoardingView.getInstance()
                }
                Screen.LOGIN -> {
                    shouldAddToBackStack = true
                    fragment = LoginView.getInstance()
                }
            }
            setFragment(R.id.container, fragment!!, shouldAddToBackStack, animation)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    private val firebaseAuthStateListener
        get() = firebaseInteractor
                .getListener { enableNavigationItem(NavigationItem.PROFILE, firebaseInteractor.currentUser != null) }


    private fun enableNavigationItem(item: NavigationItem, enable: Boolean) {
        navigationView.menu.getItem(item.order).isEnabled = enable
    }

    override fun onResume() {
        super.onResume()
        firebaseInteractor.addListener(firebaseAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        firebaseInteractor.removeListener(firebaseAuthStateListener)
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 3476
    }


    private var cropImageUri: Uri? = null

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {

            val disposable = googleInteractor.proceedWithAuthData(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ onSuccessLogin() }, { onFailLogin() })
            compositeDisposable.add(disposable)
        } else
        // handle result of pick image chooser
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                val imageUri = CropImage.getPickImageResultUri(this, data)
                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    cropImageUri = imageUri
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
                } else {
                    getProfileView()?.presenter?.startCropImageActivity(imageUri)
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    getProfileView()?.presenter?.uploadAvatar(result.uri)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //TODO: Add logging
                    // val error = result.error
                    Snackbar.make(drawerLayout, R.string.image_has_not_been_selected, Snackbar.LENGTH_SHORT).show()
                }
            } else {
                val isUserLoggedIn = firebaseInteractor.isLoggedIn()
                if (isLoggedIn != isUserLoggedIn) {
                    isLoggedIn = isUserLoggedIn
                    openFirstScreen()
                }
            }
    }

    fun onFailLogin() {
        isLoggedIn = false
        fragment?.showSimpleSnackbarMessage("something went wrong :(")
    }

    fun onSuccessLogin() {
        // Sign in success, update UI with the signed-in user's information
        val user = firebaseInteractor.currentUser
        // updateUI(user)
        user?.let {
            isLoggedIn = true
            fragment?.showSimpleSnackbarMessage("weeeelcome, ${user.displayName} !!!")
            openFirstScreen()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getProfileView()?.presenter?.startPickImageActivity()
                } else {
                    Snackbar.make(drawerLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show()
                }
            }
            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE -> {
                if (cropImageUri != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // required permissions granted, start crop image setupActivity
                    getProfileView()?.presenter?.startCropImageActivity(cropImageUri!!)
                } else {
                    Snackbar.make(drawerLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getProfileView(): ProfileView? = supportFragmentManager.findFragmentById(R.id.container) as? ProfileView

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) {
            finish()
            return
        }
        super.onBackPressed()
    }

    fun signOut() {
        firebaseInteractor.signOut()
        facebookInteractor.signOut()
        isLoggedIn = false
        fragment?.showSimpleSnackbarMessage("u have been signed out")
        openLoginScreen()
    }
}
