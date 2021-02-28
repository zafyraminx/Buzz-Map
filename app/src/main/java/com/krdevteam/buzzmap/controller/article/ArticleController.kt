package com.krdevteam.buzzmap.controller.article

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import kotlinx.android.synthetic.main.controller_article.view.*
import javax.inject.Inject


@ActivityScoped
class ArticleController : BaseController<ArticleViewModel, ArticleViewState>(R.layout.controller_article)
{
    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: ArticleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        initLifeCycleRegistry()
        if (!::viewModel.isInitialized) {
            (activity!!.application as Application).appComponent.inject(this)
        }
        return super.onCreateView(inflater, container, savedViewState)
    }

    override fun onViewCreated(view: View) {
        view.controller_add_news_button_date.setOnClickListener {  }
        view.controller_add_news_button_upload.setOnClickListener {  }
        view.controller_add_news_button_save.setOnClickListener {
            val images = ArrayList<String>()
            images.add(view.controller_add_news_input_title.text.toString())
            viewModel.handleSendButton(
                News(
                    GeoPoint(1.0, 1.0),
                    Timestamp.now(),
                    view.controller_add_news_input_title.text.toString(),
                    view.controller_add_news_input_title.text.toString(),
                    images
                )
            )
        }
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    private fun initLifeCycleRegistry()
    {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun updateUi(state: ArticleViewState) {
    }
}