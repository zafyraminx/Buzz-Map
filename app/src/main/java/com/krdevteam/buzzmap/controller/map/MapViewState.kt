package com.krdevteam.buzzmap.controller.map

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.GoogleMap
import com.krdevteam.buzzmap.controller.base.ViewState
import com.krdevteam.buzzmap.entity.News

class MapViewState(
    var newsLiveData: LiveData<List<News>>? = null,
    var errorMessage: String = "",
    var map: GoogleMap? = null
): ViewState()