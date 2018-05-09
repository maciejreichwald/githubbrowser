package com.rudearts.githubbrowser.ui.location

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.rudearts.githubbrowser.R
import com.rudearts.githubbrowser.domain.model.User
import com.rudearts.githubbrowser.extentions.*
import com.rudearts.githubbrowser.model.LoadingState
import com.rudearts.githubbrowser.ui.ToolbarActivity
import javax.inject.Inject

class DetailsActivity : ToolbarActivity() {

    companion object {
        const val USER_NAME_PARAM = "UserName"
        private const val AVATAR_SIZE = 300
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel:DetailsViewModel

    internal val lblName: TextView by bind(R.id.name)
    internal val lblStars: TextView by bind(R.id.stars)
    internal val lblFollowers: TextView by bind(R.id.followers)
    internal val imgAvatar: ImageView by bind(R.id.avatar)

    private val inflater by lazy { LayoutInflater.from(this@DetailsActivity) }

    override fun provideSubContentViewId() = R.layout.activity_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    internal fun setup() {
        inject()
        setupTitle()
        setupViewModel()
        loadUserData()
    }

    private fun loadUserData() {
        val name = intent.getStringExtra(USER_NAME_PARAM)
        viewModel.loadUser(name)
    }

    private fun setupViewModel() {
        viewModel = getViewModel<DetailsViewModel>(viewModelFactory).apply {
            observe(user, ::onUserLoaded)
            observe(errorMessage, ::onErrorMessage)
            observe(loadingState, ::onLoadingStateChange)
        }
    }

    private fun onLoadingStateChange(loadingState: LoadingState?) {
        toolbarProgressBar.visible = loadingState == LoadingState.LOADING
    }

    private fun onErrorMessage(message: String?) {
        message?.let {
            showSnackMessage(it)
        }
    }

    private fun onUserLoaded(loadedUser: User?) {
        loadedUser?.let { user ->
            lblName.text = user.name
            lblStars.text = user.stars.toString()
            lblFollowers.text = user.followers.toString()
            loadAvatar(user)
        }
    }

    private fun loadAvatar(user: User) {
        imgAvatar.setImageResource(R.drawable.user)
        user.avatar?.let {
            imgAvatar.loadUrlThumb(AVATAR_SIZE, R.drawable.user, it)
        }
    }

    internal fun setupTitle() {
        title = getString(R.string.details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    internal fun inject() {
        getAppInjector().inject(this@DetailsActivity)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
