package com.example.recipesapp.ui.supermarket

import android.Manifest
import android.location.Location.distanceBetween
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.health.TimerStat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentSupermarketBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class SupermarketFragment : Fragment(), OnMapReadyCallback{

    private lateinit var supermarketViewModel: SupermarketViewModel
    private var _binding: FragmentSupermarketBinding? = null
    private lateinit var mMap: GoogleMap
    private var latLng: LatLng? = LatLng(0.0,0.0)
    private var current_marker: Marker? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        supermarketViewModel =
            ViewModelProvider(this)[SupermarketViewModel::class.java]

        _binding = FragmentSupermarketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cardView.visibility = View.VISIBLE
        val delay = 4000L // 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if(this.isVisible) {
                binding.cardView.visibility = View.GONE
            }
        }, delay)



        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Check permission
        var requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // returns boolean represent whether the
            // permission is granted or not
            //If granted add the markers
            if (isGranted) {
                //When permission granted
                //Call method
                supermarketViewModel.getLocationData().observe(viewLifecycleOwner, Observer {
                    var local = LatLng(it.latitude, it.longitude)
                    val distance = FloatArray(1)
                    val radiusInMeters = 1.5

                    //Compute the distance only after the first execution
                    if(latLng != null){
                        distanceBetween(latLng!!.latitude, latLng!!.longitude, local.latitude, local.longitude, distance)
                    }

                    //Check if the distance is changed of 1 meter or I'm in the first execution then update the location
                    if(latLng == null || distance[0] > radiusInMeters) {
                        if(current_marker!=null){
                            current_marker!!.remove()
                        }
                        current_marker = mMap.addMarker(
                            MarkerOptions().
                            position(local).
                            title("Home").
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.home_google_map)))!!

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 15f))
                    }
                    //binding.cardView.visibility = View.GONE
                    getSupermarkets(local)
                })
            } else {
                // if permission denied then check whether never ask
                // again is selected or not by making use of
                // !ActivityCompat.shouldShowRequestPermissionRationale(
                // requireActivity(), Manifest.permission.CAMERA)
                Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        return root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun getSupermarkets(local: LatLng) {
        val distance = FloatArray(1)
        val radiusInMeters = 250.0

        //Compute the distance only after the first execution
        if(latLng != null){
            distanceBetween(latLng!!.latitude, latLng!!.longitude, local.latitude, local.longitude, distance)
        }
        /**
         * If the User change the location in a range bigger than 250m
         * I recompute the supermarket in the area, first I clean the map and then I recompute all the markers
         */
        if(latLng == null || distance[0] > radiusInMeters) {
            mMap.clear()
            latLng = local

            current_marker = mMap.addMarker(
                MarkerOptions().
                position(local).
                title("Home").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.home_google_map)))!!
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(local, 15f))

            supermarketViewModel.getSupermarket(latLng!!).observe(viewLifecycleOwner, Observer {
                for (elem in it) {
                    var lat = elem.geometry!!.location.lat
                    var lng = elem.geometry!!.location.lng
                    var name = elem.name

                    var supermarketLocation = LatLng(lat!!, lng!!)

                    mMap.addMarker(
                        MarkerOptions().
                        position(supermarketLocation).
                        title(name).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Manipulates the map once available.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

}

