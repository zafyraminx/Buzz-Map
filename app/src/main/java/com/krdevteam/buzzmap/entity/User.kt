package com.krdevteam.buzzmap.entity

import android.location.Location

class User(
    var uid: String,
    var type: String,
    var title: String,
    var details: String,
    var imageURL: ArrayList<String>
) {
}