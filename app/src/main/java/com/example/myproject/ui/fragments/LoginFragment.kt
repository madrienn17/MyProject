package com.example.myproject.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.firstapplication.R
import com.example.myproject.MainActivity
import com.example.myproject.models.User
import com.example.myproject.ui.adapters.FavouritesAdapter
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.example.myproject.utils.Constants


class LoginFragment : Fragment() {
    private val daoViewModel:DaoViewModel by activityViewModels()
    lateinit var userEmail:TextView
    lateinit var userPassword:TextView
    lateinit var allUsers:LiveData<List<User>>
    lateinit var users:List<User>
    lateinit var bundle: Bundle

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        allUsers = daoViewModel.readAllUsers

        allUsers.observe(viewLifecycleOwner, { us ->
            users = us
            Log.d("USER",users.toString())
        })

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val registerButton = view.findViewById<Button>(R.id.goto_registration)
        val signinButton = view.findViewById<Button>(R.id.sign_in)
        userEmail = view.findViewById(R.id.user_email_login)
        userPassword = view.findViewById(R.id.password_login)

        registerButton.isEnabled = false

        registerButton.setOnClickListener {
            val bundle = bundleOf(
                    "email" to userEmail.text.toString(),
                    "password" to userPassword.text.toString()
            )
            findNavController().navigate(R.id.navigation_register, bundle)
        }

        signinButton.setOnClickListener {
            val isValid = validUser(userEmail.text.toString(), userPassword.text.toString())

                if(isValid!= null) {
                    MainActivity.isLoggedIn = true
                    findNavController().navigate(R.id.navigation_restaurants, bundle)
                }
                else {
                    if (isRegistered(userEmail.text.toString(), userPassword.text.toString())) {
                        Toast.makeText(context, "Wrong credentials!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        signinButton.isEnabled = false
                        registerButton.isEnabled = true
                        Toast.makeText(requireContext(), "You are not a registered user! Please register", Toast.LENGTH_LONG).show()
                    }
                }
        }

        if(MainActivity.isLoggedIn) {
            val favs = daoViewModel.getUserFavorites(Constants.USER_NAME)

            favs.observe(viewLifecycleOwner, { us ->
                Constants.favoritIds = us
                FavouritesAdapter.getRestListByid(Constants.favoritIds)
            })
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun isRegistered(email: String, password: String): Boolean {
        for (u in users) {
            if (u.email  == email || u.password == password) {
                return true
            }
        }
        return false
    }

    private fun validUser(email: String, password: String): User? {
        for (i in users) {
            if (i.email == email && i.password == password) {
                bundle = bundleOf(
                        "name" to i.name,
                        "address" to i.address,
                        "phone" to i.phone,
                        "email" to i.email
                )
                RegisterFragment.setUser(i.name)
                return i
            }
        }
        return null
    }
}