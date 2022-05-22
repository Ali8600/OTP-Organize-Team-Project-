package com.example.otporganize_team_project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.otporganize_team_project.R


import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.otporganize_team_project.firbase.FirestoreClass
import com.example.otporganize_team_project.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_up)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        // TODO (Step 11: Add a click event to the Sign-Up button and call the registerUser function.)
        // START
        // Click event for sign-up button.
        val btn_sign_up: Button = findViewById(R.id.btn_sign_up)
        btn_sign_up.setOnClickListener{
            registerUser()
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

//        setSupportActionBar(toolbar_sign_up_activity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
//        }
//
//        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
    }

    // TODO (Step 9: A function to register a new user to the app.)
    // START
    /**
     * A function to register a user to our app using the Firebase.
     * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
     */
    private fun registerUser(){
        val et_name: EditText = findViewById(R.id.et_name)
        val et_email: EditText = findViewById(R.id.et_email)
        val et_password: EditText = findViewById(R.id.et_password)

        val name: String = et_name.text.toString().trim { it <= ' ' }
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val password: String = et_password.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->



                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!


                            // TODO(Step 1: As you can see we are now authenticated by Firebase but for more inserting more details we need to use the DATABASE in Firebase.)
                            // START
                            // Before start with database we need to perform some steps in Firebase Console and add a dependency in Gradle file.
                            // Follow the Steps:
                            // Step 1: Go to the "Database" tab in the Firebase Console in your project details in the navigation bar under "Develop".
                            // Step 2: In the Database Page and Click on the Create Database in the Cloud Firestore in the test mode. Click on Next
                            // Step 3: Select the Cloud Firestore location and press the Done.
                            // Step 4: Now the database is created in the test mode and now add the cloud firestore dependency.
                            // Step 5: For more details visit the link: https://firebase.google.com/docs/firestore
                            // END

                            // TODO (Step 4: Now here we will make an entry in the Database of a new user registered.)
                            // START
                            val user = User(
                                firebaseUser.uid, name, registeredEmail
                            )

                            // call the registerUser function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerUser(this@SignUpActivity, user)
                            // END

                            // TODO (Step 10: Move the activity finish line code in the success function.)
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }
    // END

    // TODO (Step 10: A function to validate the entries of a new user.)
    // START
    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }
    fun userRegisteredSuccess() {

        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
    // END
}
