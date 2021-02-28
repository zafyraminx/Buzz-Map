package com.krdevteam.buzzmap.controller.article

import androidx.lifecycle.LiveData
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.controller.base.ViewState

class ArticleViewState(
    var newsLiveData: LiveData<List<News>>? = null,
    var errorMessage: String = "",
    var enterBtnEnabled: Boolean = true,
    var clearRoomTextfield: Boolean = false
): ViewState()