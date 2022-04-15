package com.example.recipesapp.ui.supermarket

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.recipesapp.data.api.GoogleMapApi
import com.example.recipesapp.data.api.RetrofitBuilder
import com.example.recipesapp.data.model.Results
import com.example.recipesapp.data.model.SupermarketModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupermarketViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * get location from Location LIve Data
     */
    private val locationData = LocationLiveData(application)
    var _resultSupermarket = MutableLiveData<ArrayList<Results>>()

    /**
     * action for GetLocation
     */
    fun getLocationData() = locationData

    fun getSupermarket(loc:LatLng) : LiveData<ArrayList<Results>> {

        var url = "https://maps.googleapis.com/maps/api/"
        var location = loc.latitude.toString() + "," + loc.longitude.toString()

        val service = RetrofitBuilder.makeRetrofitService(url).create(GoogleMapApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getNearBy(location,500,"supermarket",true, R.string.google_nearby_key)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful()!!) {
                    _resultSupermarket.value = response.body()!!.results
                    Log.d("Supermarket", response.body().toString())
                }else{
                    Log.d("Supermarket", response.message())
                }
            }
        }
        val resultsSupermarket : LiveData<ArrayList<Results>> = _resultSupermarket
        return resultsSupermarket
    }
}
