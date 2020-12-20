package com.example.myproject.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.firstapplication.R
import com.example.myproject.MapsActivity
import com.example.myproject.models.RestaurantPic
import com.example.myproject.ui.viewmodels.DaoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_details.view.*
import java.io.ByteArrayOutputStream

class DetailsFragment: Fragment() {
    private lateinit var myView: View
    private lateinit var imageView:ImageView
    private val daoViewModel: DaoViewModel by activityViewModels()
    private lateinit var name: String
    private val TAG = "DetailsFragment"

    @SuppressLint("CutPasteId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)
        navBar!!.visibility = View.VISIBLE
        myView = inflater.inflate(R.layout.fragment_details, container, false)

        name = requireArguments().get("name").toString()
        val address = requireArguments().get("address").toString()
        val city = requireArguments().get("city").toString()
        val state = requireArguments().get("state").toString()
        val area = requireArguments().get("area").toString()
        val postal_code = requireArguments().get("postal_code").toString()
        val country  = requireArguments().get("country").toString()
        val price = requireArguments().get("price").toString()
        val lat = requireArguments().get("lat").toString()
        val lng = requireArguments().get("lng").toString()
        val phone = requireArguments().get("phone").toString()
        val reserve_url = requireArguments().get("reserve_url").toString()
        val mobile_reserve_url = requireArguments().get("mobile_reserve_url").toString()
        val image = requireArguments().get("image").toString()

        myView.apply {
            findViewById<TextView>(R.id.details_name).text = name
            findViewById<TextView>(R.id.details_address).text = address
            findViewById<TextView>(R.id.details_city).text = city
            findViewById<TextView>(R.id.details_state).text = state
            findViewById<TextView>(R.id.details_area).text = area
            findViewById<TextView>(R.id.details_postalcode).text = postal_code
            findViewById<TextView>(R.id.details_country).text = country
            findViewById<TextView>(R.id.details_price).text = price
            findViewById<TextView>(R.id.details_reserve_url).text = reserve_url
            findViewById<TextView>(R.id.details_mobile_reserve_url).text = mobile_reserve_url
            imageView = findViewById(R.id.details_image)

            Glide.with(context)
                .load(image)
                .apply(RequestOptions().centerCrop())
                .into(imageView).view

            for (i in RestaurantsListFragment.restPics) {
                if (name == i.restName) {
                    val bmp: Bitmap = BitmapFactory.decodeByteArray(i.restPic, 0, i.restPic.size)
                    Glide.with(requireContext())
                            .load(bmp)
                            .apply(RequestOptions().centerCrop())
                            .into(imageView).view
                }
            }
        }

        myView.view_location_map.setOnClickListener {
            val mapIntent = Intent(
                context, MapsActivity::class.java).apply {
                    putExtra("lat", lat.toDouble())
                    putExtra("lng", lng.toDouble())
                    putExtra("name", name)
            }
            startActivity(mapIntent)
        }

        val callButton = myView.findViewById<ImageButton>(R.id.call_the_place)
        callButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            intent.data = Uri.parse("tel:$phone")
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
            else {
                try {
                    startActivity(intent)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }

        val reserveUrl = myView.findViewById<TextView>(R.id.details_reserve_url)
        reserveUrl.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(reserve_url))
            startActivity(browserIntent)
        }

        val mobileReservation = myView.findViewById<TextView>(R.id.details_mobile_reserve_url)
        mobileReservation.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mobile_reserve_url))
            startActivity(browserIntent)
        }
        val addImageButton = myView.findViewById<FloatingActionButton>(R.id.addNewImage)
        addImageButton.setOnClickListener {
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PermissionChecker.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                openGallery()
            }
        }

       return myView
    }

    private fun openGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
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
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
           imageView.setImageURI(data?.data)

            val baos = ByteArrayOutputStream()
            @SuppressWarnings("deprecation")
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, data?.data)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes:ByteArray = baos.toByteArray()

            daoViewModel.insertRestPic(RestaurantPic(name,imageBytes))
        }
    }

    companion object {
        //image pick code
        const val IMAGE_PICK_CODE = 1000
        //Permission code
        const val PERMISSION_CODE = 1001
    }
}
