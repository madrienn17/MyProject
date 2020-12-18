package com.example.myproject.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
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
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val daoViewModel: DaoViewModel by activityViewModels()
    private lateinit var button: Button
    private lateinit var name:TextView
    private lateinit var address:TextView
    private lateinit var phone :TextView
    private lateinit var email :TextView
    private lateinit var floatButton: FloatingActionButton
    lateinit var allUsers: LiveData<List<User>>
    lateinit var users:List<User>

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

        allUsers.observe(viewLifecycleOwner, { us ->
            users = us
            Log.d("USERS", users.toString())
            setProfileData()
        })
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.apply {
            name = findViewById(R.id.user_name_profile)
            address = findViewById(R.id.address_profile)
            phone = findViewById(R.id.phone_profile)
            email = findViewById(R.id.email_profile)
            floatButton = findViewById(R.id.add_user_image)
        }

        button = view.findViewById(R.id.view_favourites)

        button.setOnClickListener {
            findNavController().navigate(R.id.navigation_favourites)
        }

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

    private fun setProfileData() {
        for (i in users) {
            if(i.name == Constants.USER_NAME) {
                name.text = i.name
                address.text = i.address
                phone.text = i.phone
                email.text = i.email
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
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    openGallery()
                }
                else{
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
                    .into(profilePic)
        }
    }

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