package com.example.myproject.ui.fragments

import android.Manifest
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
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.fragment_details.view.*
import java.io.ByteArrayOutputStream

class DetailsFragment: Fragment() {
    private lateinit var myView: View
    private lateinit var imageView:ImageView
    private val daoViewModel: DaoViewModel by activityViewModels()
    private lateinit var name: String // has to be reached outside of oncreateview

    private val TAG = "DetailsFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val navBar: BottomNavigationView? = this.activity?.findViewById(R.id.nav_view)
        navBar!!.visibility = View.VISIBLE
        myView = inflater.inflate(R.layout.fragment_details, container, false)

        // getting bundled elements from favorites fragment or list fragment
        name = requireArguments().get("name").toString()
        val address = requireArguments().get("address").toString()
        val city = requireArguments().get("city").toString()
        val state = requireArguments().get("state").toString()
        val area = requireArguments().get("area").toString()
        val postalCode = requireArguments().get("postal_code").toString()
        val country  = requireArguments().get("country").toString()
        val price = requireArguments().get("price").toString()
        val lat = requireArguments().get("lat").toString()
        val lng = requireArguments().get("lng").toString()
        val phone = requireArguments().get("phone").toString()
        val reserveUrl = requireArguments().get("reserve_url").toString()
        val mobileReserveUrl = requireArguments().get("mobile_reserve_url").toString()
        val image = requireArguments().get("image").toString()

        // assigning bundled values to view elements
        myView.apply {
            details_name.text = name
            details_address.text = address
            details_city.text = city
            details_state.text = state
            details_area.text = area
            details_postalcode.text = postalCode
            details_country.text = country
            details_price.text = price
            details_reserve_url.text = reserveUrl
            details_mobile_reserve_url.text = mobileReserveUrl
            imageView = details_image
        }

        // loading bundled image
        Glide.with(requireContext())
            .load(image)
            .apply(RequestOptions().centerCrop())
            .into(imageView).view

        // if the restaurant has a picture in the db, load that
        for (i in RestaurantsListFragment.restPics) {
            if (name == i.restName) {
                val bmp: Bitmap = BitmapFactory.decodeByteArray(i.restPic, 0, i.restPic.size)
                Glide.with(requireContext())
                        .load(bmp)
                        .apply(RequestOptions().centerCrop())
                        .into(imageView).view
            }
        }

        // bundling lat and lng coordinates for the MapActivity, and starting it on map button clicked
        myView.view_location_map.setOnClickListener {
            val mapIntent = Intent(
                context, MapsActivity::class.java).apply {
                    putExtra("lat", lat.toDouble())
                    putExtra("lng", lng.toDouble())
                    putExtra("name", name)
            }
            startActivity(mapIntent)
        }

        // starting dial action with the restaurant's phone number, on call button pressed
        myView.call_the_place.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            intent.data = Uri.parse("tel:$phone")

            // checking permission
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

        // opening link in browser by tapping it
        myView.details_reserve_url.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(reserveUrl))
            startActivity(browserIntent)
        }

        myView.details_mobile_reserve_url.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mobileReserveUrl))
            startActivity(browserIntent)
        }

        /**
         * on floating button clicked, we can select a picture from gallery and insert it into db
         * together with the restaurant's id, for future recurrence
         * */

        myView.addNewImage.setOnClickListener {
            // checking SDK version
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
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
        //Intent to pick image from phone's gallery
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
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // set the picked image into the imageView
            imageView.setImageURI(data?.data)
            // transform image into bitmap, then into a bytearray, and store it in the db
            val baos = ByteArrayOutputStream()

            @SuppressWarnings("deprecation")
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(context?.contentResolver, data?.data)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes: ByteArray = baos.toByteArray()

            daoViewModel.insertRestPic(RestaurantPic(name, imageBytes))
        }
    }

    companion object {
        //image pick code
        const val IMAGE_PICK_CODE = 1000
        //Permission code
        const val PERMISSION_CODE = 1001
    }
}
