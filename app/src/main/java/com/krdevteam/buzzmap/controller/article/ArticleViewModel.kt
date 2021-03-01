package com.krdevteam.buzzmap.controller.article

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.krdevteam.buzzmap.controller.base.BaseViewModel
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.injection.scope.AppScoped
import com.krdevteam.buzzmap.util.repository.FirebaseRepository
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AppScoped
class ArticleViewModel @Inject constructor(
        firebaseRepository: FirebaseRepository,
        viewState: ArticleViewState
) : BaseViewModel<ArticleViewState>(firebaseRepository, viewState) {

    private val mutableLocation = MutableLiveData<String>()
    val location: LiveData<String> = mutableLocation

    fun getUserType() {
        Timber.d("getNewsList")
        firebaseRepository.observeUser()
        viewState.userLiveData = firebaseRepository.userLiveData
        Timber.d("getNewsList ${viewState.userLiveData}")
        updateUi()
    }

    fun handleSendButton(news: News) {
        sendMessage(news)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(activity: Activity) {
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        try {
                            val geocoder = Geocoder(activity, Locale.getDefault())
                            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 10)
                            for (address: Address in addresses) {
                                if (address.adminArea != null) {
                                    mutableLocation.value = address.adminArea
                                }
                            }
                        } catch (exception: IOException) {
                            Timber.e(exception, "Error Fetching Location")
                        }
                    }
                }
            }
        }
        LocationServices
                .getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun getLocation(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var locationGps: Location?
        var locationNetwork: Location?
        if (hasGps || hasNetwork) {
            if (hasGps) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F) { p0 -> locationGps = p0 }
            val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (localGpsLocation != null) {
                locationGps = localGpsLocation
                Timber.d("localGpsLocation: $localGpsLocation")
            }
        }
        if (hasNetwork) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F) { p0 -> locationNetwork = p0 }

            val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (localNetworkLocation != null) {
                locationNetwork = localNetworkLocation
                Timber.d("localNetworkLocation: $localNetworkLocation")
            }

        } else {
//            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        updateUi()
    }

    private fun sendMessage(news: News) {
        firebaseRepository.sendArticle(news)
        viewState.showToast = true
        viewState.isClose = true
        updateUi()
    }

    fun close() {
        viewState.isClose = true
        updateUi()
    }

    fun clearState() {
        viewState.controller = null
        viewState.newActivity = null
        viewState.clearActivityOnIntent = false
        viewState.controller = null
        viewState.showMenu = false
        viewState.listItem = ArrayList()
        viewState.userType = "Editor"
        viewState.isClose = false
    }
}