package com.example.otporganize_team_project.activities


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.otporganize_team_project.R
import com.example.otporganize_team_project.firbase.FirestoreClass
import com.example.otporganize_team_project.model.User
import com.google.firebase.auth.FirebaseAuth


// TODO (Step 1: Extend the BaseActivity instead of AppCompatActivity.)
class SignInActivity : BaseActivity() {
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_in)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        // TODO(Step 4: Add click event for sign-in button and call the function to sign in.)
        // START

        val btn_sign_in: Button = findViewById(R.id.btn_sign_in)
        btn_sign_in.setOnClickListener {
            signInRegisteredUser()
        }
        // END
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {
//
//        setSupportActionBar(toolbar_sign_in_activity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
//        }
//
//        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }

    // TODO (Step 2: A function for Sign-In using the registered user using the email and password.)
    // START
    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space

        val et_email_sign_in: EditText = findViewById(R.id.et_email_sign_in)
        val et_password_sign_in: EditText = findViewById(R.id.et_password_sign_in)
        val email: String = et_email_sign_in.text.toString().trim { it <= ' ' }
        val password: String = et_password_sign_in.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
// TODO (Step 2: Remove the toast message and call the FirestoreClass signInUser function to get the data of user from database. And also move the code of hiding Progress Dialog and Launching MainActivity to Success function.)
                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        FirestoreClass().loadUserData(this@SignInActivity)
                        // END
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    // END

    // TODO (Step 3: A function to validate the entries of a user.)
    // START
    /**
     * A function to validate the entries of a user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }
    fun signInSuccess(user: User) {

        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }
    // END
}