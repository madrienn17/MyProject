package com.example.myproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.firstapplication.R
import com.example.myproject.models.User
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {
    private val daoViewModel: DaoViewModel by activityViewModels()
    private lateinit var button: Button
    private lateinit var name:TextView
    private lateinit var address:TextView
    private lateinit var phone :TextView
    private lateinit var email :TextView
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
            setProfileData()
        })

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.apply {
            name = findViewById(R.id.user_name_profile)
            address = findViewById(R.id.address_profile)
            phone = findViewById(R.id.phone_profile)
            email = findViewById(R.id.email_profile)
        }
        button = view.findViewById(R.id.view_favourites)


        button.setOnClickListener {
            findNavController().navigate(R.id.navigation_favourites)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun setProfileData() {
        for (i in users) {
            if(i.name == Constants.USER_NAME) {
                name.text = i.name
                address.text = i.address
                phone.text = i.phone
                email.text = i.email
            }
        }
    }
}