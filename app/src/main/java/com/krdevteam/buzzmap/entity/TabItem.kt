package com.krdevteam.buzzmap.entity

import com.bluelinelabs.conductor.Controller

class TabItem {
    var controller: Controller
    var title:String

    constructor(title: String, controller: Controller) {
        this.controller = controller
        this.title = title
    }
}