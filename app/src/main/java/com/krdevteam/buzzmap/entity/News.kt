package com.krdevteam.buzzmap.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlin.collections.ArrayList

class News {
    var uid: String = ""
    var location: GeoPoint
    var dateTime: Timestamp
    var title: String
    var details: String
    var imageURL: ArrayList<String>
    var status: Boolean
    var editor: String

    constructor(
        uid: String,
        location: GeoPoint,
        dateTime: Timestamp,
        title: String,
        details: String,
        imageURL: ArrayList<String>,
        status: Boolean,
        editor: String
    ) {
        this.uid = uid
        this.location = location
        this.dateTime = dateTime
        this.title = title
        this.details = details
        this.imageURL = imageURL
        this.status = status
        this.editor = editor
    }

    constructor(
        location: GeoPoint,
        dateTime: Timestamp,
        title: String,
        details: String,
        imageURL: ArrayList<String>,
        status: Boolean,
        editor: String
    ) {
        this.location = location
        this.dateTime = dateTime
        this.title = title
        this.details = details
        this.imageURL = imageURL
        this.status = status
        this.editor = editor
    }
}
