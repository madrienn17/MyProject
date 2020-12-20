package com.example.myproject.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.firstapplication.R
import com.example.myproject.MainActivity
import com.example.myproject.models.User
import com.example.myproject.models.UserPic
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {
    private val daoViewModel: DaoViewModel by activityViewModels()
    private lateinit var button: Button
    private lateinit var name:TextView
    private lateinit var address:TextView
    private lateinit var phone :TextView
    private lateinit var email :TextView
    private lateinit var floatButton: FloatingActionButton
    private lateinit var profile: ImageView
    lateinit var allUsers: LiveData<List<User>>
    lateinit var users:List<User>
    lateinit var allUserPic: LiveData<List<UserPic>>
    var userPics: List<UserPic> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)
        navBar!!.visibility = View.VISIBLE
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        allUsers = daoViewModel.readAllUsers

        // getting user pictures from the db
        allUserPic = daoViewModel.readAllUserPic
        allUserPic.observe(viewLifecycleOwner, { us ->
            userPics = us
            getProfilePicture()
            //Log.d("Profiledata", userPics.toString())
        })

        // if the user exists, set up it's profile data
        allUsers.observe(viewLifecycleOwner, { us ->
            users = us
            //Log.d("USERS", users.toString())
            setProfileData()
        })

        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            name = user_name_profile
            address = address_profile
            phone = phone_profile
            email = email_profile
            floatButton = add_user_image
            profile = profilePic
        }

        // by pressing the favorites button, redirecting to favorites fragment
        view.view_favourites.setOnClickListener {
        findNavController().navigate(R.id.navigation_favourites)
        }

        // adding picture with the floating button
        floatButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PermissionChecker.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, DetailsFragment.PERMISSION_CODE)
                } else {
                    //permission already granted
                    openGallery()
                }
            } else {
                //system OS is < Marshmallow
                openGallery()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * function to set the profile data for the user currently logged in
     */
    private fun setProfileData() {
        for (i in users) {
            if(i.name == Constants.USER_NAME && MainActivity.isLoggedIn) {
                name.text = i.name
                address.text = i.address
                phone.text = i.phone
                email.text = i.email
            }
        }
    }


    /**
     * function to get the profile picture by decoding b64 string into bytearray, then into bitmap
     * load the decoded bitmap into profilepicture imageview styled with circlecrop
     * */
    private fun getProfilePicture() {
        if(userPics.isNotEmpty()) {
            for (i in userPics) {
                if (i.userName == Constants.USER_NAME) {
                    val imageBytes = Base64.decode(i.userPic, Base64.DEFAULT)
                    Log.d("IMAGEBYTES", imageBytes.toString())
                    val decocdedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    Glide.with(requireContext())
                            .load(decocdedImage)
                            .circleCrop()
                            .into(profile)
                }
            }
        }
    }

    private fun openGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, DetailsFragment.IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            DetailsFragment.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup granted
                    openGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == DetailsFragment.IMAGE_PICK_CODE){
            Glide.with(requireContext())
                    .load(data?.data)
                    .circleCrop()
                    .into(profile)

            val baos = ByteArrayOutputStream()
            @SuppressWarnings("deprecation")
            val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, data?.data)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes:ByteArray = baos.toByteArray()
            val imgString:String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            daoViewModel.insertUserPic(UserPic(Constants.USER_NAME, imgString))
        }
    }

    // setting functionality for logging out via actionbar icon
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            logOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // logging out
    private fun logOut() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            MainActivity.isLoggedIn = false
            findNavController().navigate(R.id.navigation_login)
            Toast.makeText(
                    requireContext(),
                    "You are logged out!",
                    Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Do you want to logout?")
        builder.setMessage("Are you sure you want to log out?")
        builder.create().show()
    }
}