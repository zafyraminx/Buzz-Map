package com.krdevteam.buzzmap.controller.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.controller.base.ViewState
import com.krdevteam.buzzmap.entity.User

class ArticleViewState(
//    val mutableLocation = MutableLiveData<String>(),
    var newsLiveData: LiveData<List<News>>? = null,
    var userLiveData: LiveData<User>? = null,
    var errorMessage: String = "",
    var enterBtnEnabled: Boolean = true,
    var clearRoomTextfield: Boolean = false,
    var isClose: Boolean = false,
    var showToast: Boolean = false
): ViewState()