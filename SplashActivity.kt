package com.example.otporganize_team_project.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.otporganize_team_project.R
import com.example.otporganize_team_project.firbase.FirestoreClass

// TODO (Step 1: Add new activity named as splash activity.)
// START
class SplashActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_splash)

        // TODO (Step 6: Add the full screen flags here.)
        // This is used to hide the status bar and make the splash screen as a full screen activity.
        // START
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // END

        // TODO (Step 7: Add the file in the custom font file to the assets folder. And add the below line of code to apply it to the title TextView.)
        // Steps for adding the assets folder are :
        // Right click on the "app" package and GO TO ==> New ==> Folder ==> Assets Folder ==> Finish.
        // START
        // This is used to get the file from the assets folder and set it to the title textView.

        // END
        Handler().postDelayed({

            // TODO (Step 2: Check if the current user id is not blank then send the user to MainActivity as he have logged in
            //  before or else send him to Intro Screen as earlier.)
            // START
            // Here if the user is signed in once and not signed out again from the app. So next time while coming into the app
            // we will redirect him to MainScreen or else to the Intro Screen as it was before.

            // Get the current user id
            val currentUserID = FirestoreClass().getCurrentUserID()
            // Start the Intro Activity

            if (currentUserID.isNotEmpty()) {
                // Start the Main Activity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // Start the Intro Activity
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish() // Call this when your activity is done and should be closed.
            // END
        }, 2500) // Here we pass the delay time in milliSeconds after which the splash activity will disappear.

    }
}
// END