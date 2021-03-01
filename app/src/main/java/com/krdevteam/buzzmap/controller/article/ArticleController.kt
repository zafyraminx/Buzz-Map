package com.krdevteam.buzzmap.controller.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.krdevteam.buzzmap.Application
import com.krdevteam.buzzmap.R
import com.krdevteam.buzzmap.controller.base.BaseController
import com.krdevteam.buzzmap.controller.main.MainController
import com.krdevteam.buzzmap.entity.News
import com.krdevteam.buzzmap.entity.User
import com.krdevteam.buzzmap.injection.scope.ActivityScoped
import com.krdevteam.buzzmap.util.AppConstants
import kotlinx.android.synthetic.main.controller_article.view.*
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class ArticleController : BaseController<ArticleViewModel, ArticleViewState>(R.layout.controller_article)
{
    private var mLifecycleRegistry = LifecycleRegistry(this)
    @Inject
    override lateinit var viewModel: ArticleViewModel

    private val observer: Observer<User> = Observer<User> { user -> updateUserUI(user) }

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
        viewModel.getUserType()
        view.controller_add_news_button_close.setOnClickListener { viewModel.close() }
        view.controller_add_news_button_save.setOnClickListener {
            val images = ArrayList<String>()
            images.add("")
            viewModel.handleSendButton(
                News(
                    GeoPoint(1.0, 1.0),
                    Timestamp.now(),
                    view.controller_add_news_input_title.text.toString(),
                    view.controller_add_news_input_title.text.toString(),
                    images,
                    false,
                    ""
                )
            )
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        viewModel.viewState.userLiveData?.observeForever(observer)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        viewModel.viewState.userLiveData?.removeObserver(observer)
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
        if (state.isClose) viewController()
    }

    private fun updateUserUI(user:User)
    {
        view?.controller_add_news_text_user_type?.text  = user.type
        view?.controller_add_news_text_title?.text = title(user.type)
        view?.controller_add_news_button_approve?.visibility = showApproveButton(user.type)
    }

    private fun title(type:String) : String
    {
        return if (type == "Editor")
            "Edit & Approve News Article"
        else
            "Add News Article"
    }

    private fun showApproveButton(type:String) : Int
    {
        return if (type == "Editor")
            View.VISIBLE
        else
            View.GONE
    }

    private fun viewController() {
        Timber.d("controller:")
        viewModel.clearState()
        router.popCurrentController()
    }
}