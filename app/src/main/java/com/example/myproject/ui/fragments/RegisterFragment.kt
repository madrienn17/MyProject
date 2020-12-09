package com.example.myproject.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.firstapplication.R
import com.example.myproject.models.User
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.ui.viewmodels.SharedViewModel
import com.example.myproject.utils.Constants

class RegisterFragment : Fragment() {
    private val daoViewModel: DaoViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val userName = view.findViewById<EditText>(R.id.user_name)
        val userAddress = view.findViewById<EditText>(R.id.user_adress)
        val userPhone = view.findViewById<EditText>(R.id.user_phone)
        val userPassword = view.findViewById<EditText>(R.id.password)
        val userEmail = view.findViewById<EditText>(R.id.user_email)



        val registerButton = view.findViewById<Button>(R.id.finish_registration)

        registerButton.setOnClickListener {
            sharedViewModel.isLoggedIn = true
            user = User(
                userName.text.toString(),
                userAddress.text.toString(),
                userPhone.text.toString(),
                userEmail.text.toString(),
                userPassword.text.toString()
            )
            Log.d("REGISTERED", user.name)
            daoViewModel.addUserDB(user)
            setUser(user.name)
            findNavController().navigate(R.id.navigation_restaurants)
        }

        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        fun setUser(name:String) {
            Constants.USER_NAME = name
        }
    }
}