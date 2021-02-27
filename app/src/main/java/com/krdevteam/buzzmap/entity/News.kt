package com.krdevteam.buzzmap.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlin.collections.ArrayList

class News(
    var uid: String,
    var location: GeoPoint,
    var dateTime: Timestamp,
    var title: String,
    var details: String,
    var imageURL: ArrayList<String>
) {
}