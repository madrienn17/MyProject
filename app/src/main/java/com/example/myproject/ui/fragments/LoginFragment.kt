package com.example.myproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_login.view.*


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

        // getting all users in a list
        allUsers = daoViewModel.readAllUsers

        allUsers.observe(viewLifecycleOwner, { us ->
            users = us
        })

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val registerButton = view.goto_registration
        val signInButton = view.sign_in
        userEmail = view.user_email_login
        userPassword = view.password_login

        // bundling already typed data to contiue to registration
        registerButton.setOnClickListener {
            val bundle = bundleOf(
                    "email" to userEmail.text.toString(),
                    "password" to userPassword.text.toString()
            )
            findNavController().navigate(R.id.navigation_register, bundle)
        }

        signInButton.setOnClickListener {
            // checking if the user is already registered and introduced correct data
            val isValid = validUser(userEmail.text.toString(), userPassword.text.toString())

            // if it passes, it will be navigated to restaurants
            if(isValid!= null) {
                MainActivity.isLoggedIn = true

                // get the favorite id's  for the favs to be starred already when navigating at the restaurants page
                    val favs = daoViewModel.getUserFavorites(Constants.USER_NAME)

                    favs.observe(viewLifecycleOwner, { us ->
                        Constants.favoritIds = us
                        FavouritesAdapter.getRestListByid(Constants.favoritIds)
                    })

                findNavController().navigate(R.id.navigation_restaurants, bundle)
            }
            else {
                // if the data doesn't match we check for misstyped credentials
                if (isRegistered(userEmail.text.toString(), userPassword.text.toString())) {
                    Toast.makeText(context, "Wrong credentials!", Toast.LENGTH_SHORT).show()
                }
                else {
                    signInButton.isEnabled = false
                    registerButton.isEnabled = true
                    Toast.makeText(requireContext(), "You are not a registered user! Please register", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * @param email user's email string
     * @param password user's password string
     * @return boolean, true if user is registered, false if it's not
     * */
    private fun isRegistered(email: String, password: String): Boolean {
        for (u in users) {
            if (u.email  == email || u.password == password) {
                return true
            }
        }
        return false
    }

    /**
     * same params
     * @return the user who's credentials were entered, or null
     * */
    private fun validUser(email: String, password: String): User? {
        for (i in users) {
            if (i.email == email && i.password == password) {
                bundle = bundleOf(
                        "name" to i.name,
                        "address" to i.address,
                        "phone" to i.phone,
                        "email" to i.email
                )
                Constants.USER_NAME = i.name
                return i
            }
        }
        return null
    }
}