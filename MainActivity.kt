package com.example.otporganize_team_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar

import com.bumptech.glide.Glide
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otporganize_team_project.R
import com.example.otporganize_team_project.adapters.BoardItemsAdapter
import com.example.otporganize_team_project.firbase.FirestoreClass
import com.example.otporganize_team_project.model.Board
import com.example.otporganize_team_project.model.User
import com.example.otporganize_team_project.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mUserName: String
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)

        // This is used to align the xml view to this class
        setContentView(R.layout.activity_main)

        setupActionBar()

        // Assign the NavigationView.OnNavigationItemSelectedListener to navigation view.
        val nav_view: NavigationView = findViewById(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)

        // TODO (Step 3: Call a function to get the current logged in user details.)
        // START
        // Get the current logged in user details.
        FirestoreClass().loadUserData(this@MainActivity)
        // TODO (Step 9: Launch the Create Board screen on a fab button click.)
        // START
        val fab_create_board: FloatingActionButton = findViewById(R.id.fab_create_board)
        fab_create_board.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivity(intent)
        }
        // END
    }

    override fun onBackPressed() {
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            // A double back press function is added in Base Activity.
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_my_profile -> {
                startActivity(Intent(this@MainActivity, MyProfileActivity::class.java))
            }

            R.id.nav_sign_out -> {
                // Here sign outs the user from firebase in this device.
                FirebaseAuth.getInstance().signOut()

                // Send the user to the intro screen of the application.
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {
//        val toolbar_main_activity: Toolbar = findViewById(R.id.toolbar_main_activity)
//        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
//
//        toolbar_main_activity.setNavigationOnClickListener {
//            toggleDrawer()
//        }
    }

    /**
     * A function for opening and closing the Navigation Drawer.
     */
    private fun toggleDrawer() {
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    // TODO (Step 5: Create a function to update the user details in the navigation view.)
    // START
    /**
     * A function to get the current user details from firebase.
     */
    fun updateNavigationUserDetails(user: User) {
        // The instance of the header view of the navigation view.
        val nav_view: NavigationView = findViewById(R.id.nav_view)
        val headerView = nav_view.getHeaderView(0)
        mUserName = user.name
        // The instance of the user image of the navigation view.
        val navUserImage = headerView.findViewById<ImageView>(R.id.iv_user_image)

        // Load the user image in the ImageView.
        Glide
            .with(this@MainActivity)
            .load(user.image) // URL of the image
            .centerCrop() // Scale type of the image.
            .placeholder(R.drawable.ic_user_place_holder) // A default place holder
            .into(navUserImage) // the view in which the image will be loaded.

        // The instance of the user name TextView of the navigation view.
        val navUsername = headerView.findViewById<TextView>(R.id.tv_username)
        // Set the user name
        navUsername.text = user.name
    }
    fun populateBoardsListToUI(boardsList: ArrayList<Board>) {

        hideProgressDialog()
        val rv_boards_list: RecyclerView = findViewById(R.id.rv_boards_list)
        val tv_no_boards_available: TextView = findViewById(R.id.tv_no_boards_available)
        if (boardsList.size > 0) {

            rv_boards_list.visibility = View.VISIBLE
            tv_no_boards_available.visibility = View.GONE

            rv_boards_list.layoutManager = LinearLayoutManager(this@MainActivity)
            rv_boards_list.setHasFixedSize(true)

            // Create an instance of BoardItemsAdapter and pass the boardList to it.
            val adapter = BoardItemsAdapter(this@MainActivity, boardsList)
            rv_boards_list.adapter = adapter // Attach the adapter to the recyclerView.
        } else {
            rv_boards_list.visibility = View.GONE
            tv_no_boards_available.visibility = View.VISIBLE
        }
    }
    // END

    // END
}
