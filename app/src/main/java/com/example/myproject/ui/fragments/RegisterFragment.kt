package com.example.myproject.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
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
    lateinit var allUsers: LiveData<List<User>>
    lateinit var users:List<User>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        allUsers = daoViewModel.readAllUsers

        allUsers.observe(viewLifecycleOwner, { us ->
            users = us
        })
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val userName = view.findViewById<EditText>(R.id.user_name)
        val userAddress = view.findViewById<EditText>(R.id.user_adress)
        val userPhone = view.findViewById<EditText>(R.id.user_phone)
        val userPassword = view.findViewById<EditText>(R.id.password)
        val userEmail = view.findViewById<EditText>(R.id.user_email)

        val bundledEmail = requireArguments().get("email").toString()
        val bundledPassword = requireArguments().get("password").toString()

        userEmail.setText(bundledEmail)
        userPassword.setText(bundledPassword)

        val registerButton = view.findViewById<Button>(R.id.finish_registration)

        userEmail.validate("Valid email address required!") { s -> s.isValidEmail() }
        userPassword.validate("Password must be more than 8 characters!") { s -> s.isValidPassword()}
        userAddress.validate("Address must contain alphanumeric characters!") { s -> s.isValidAddress()}
        userPhone.validate("Phone number not correct!") { s-> s.isValidPhone()}
        userName.validate("Username is required!") { s-> s.isValidName()}


        registerButton.setOnClickListener {
            user = User(
                    userName.text.toString(),
                    userAddress.text.toString(),
                    userPhone.text.toString(),
                    userEmail.text.toString(),
                    userPassword.text.toString()
            )
            if(isRegistered(user)) {
                Toast.makeText(context,"You are already resistered!",Toast.LENGTH_LONG).show()
            }
            else {
                sharedViewModel.isLoggedIn = true
                Log.d("REGISTERED", user.name)
                daoViewModel.addUserDB(user)
                setUser(user.name)
                findNavController().navigate(R.id.navigation_restaurants)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        fun setUser(name:String) {
            Constants.USER_NAME = name
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    fun EditText.validate(message: String, validator: (String) -> Boolean) {
        this.afterTextChanged {
            this.error = if (validator(it)) null else message
        }
        this.error = if (validator(this.text.toString())) null else message
    }

    fun String.isValidEmail(): Boolean
        = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isValidPassword():Boolean
        = this.isNotEmpty() && (this.length >= 8 )

    fun String.isValidName() : Boolean
        = this.isNotEmpty()

    fun String.isValidPhone() : Boolean
        = this.isNotEmpty() && Patterns.PHONE.matcher(this).matches()

    fun String.isValidAddress():Boolean
        = this.isNotEmpty() && isLettersOrDigits(this)

    fun isLettersOrDigits(chars: String): Boolean {
        return chars.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }
                .length == chars.length || chars.contains(' ') || chars.contains(',')
    }

    private fun isRegistered(user:User):Boolean {
        for(u in users) {
            if((u.email == user.email) && (u.password == user.password)) {
                return true
            }
        }
        return false
    }
}