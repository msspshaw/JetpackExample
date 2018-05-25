package shawdevelopment.android.com.jetpackexample.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.main_fragment.*
import shawdevelopment.android.com.jetpackexample.R

class MainFragment : Fragment(), View.OnClickListener {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLoading()
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mViewModel.user.observe(this, Observer { user : FirebaseUser? ->
            when (user) {
                null -> {
                    hideLoading()
                    showUserInfo()
                }
                else -> {
                    hideLoading()
                    showUser()
                }
            }

        })
        mRegisterButton.setOnClickListener(this)
        mLoginButton.setOnClickListener(this)
        mFetchGreetingsButton.setOnClickListener(this)

        mViewModel.greetings.observe(this, Observer { data ->
            hideLoading()
            if (data!!.size > 0) {
                val iterate = data?.iterator()
                var display = ""
                while (iterate!!.hasNext()) {
                    display = display + "\n" + iterate.next()
                }
                mGreetingsDisplay.visibility = VISIBLE
                mGreetingsDisplay.setText(display)
            }
        })
    }

    fun showUserInfo() {
        mEmailEntry.visibility = VISIBLE
        mPasswordEntry.visibility = VISIBLE
        mLoginButton.visibility = VISIBLE
        mRegisterButton.visibility = VISIBLE
    }

    fun showLoading() {
        /*
        mEmailEntry.visibility = GONE
        mPasswordEntry.visibility = GONE
        mLoginButton.visibility = GONE
        mRegisterButton.visibility = GONE
        mStateView.visibility = GONE
        */
        mLoadingBar.visibility = VISIBLE
    }

    fun hideLoading() {
        mLoadingBar.visibility = View.GONE
    }

    fun showUser() {
        mStateView.text = "Logged In"
        mEmailEntry.setText(mViewModel.user.value?.email)
        mPasswordEntry.visibility = GONE
        mFetchGreetingsButton.visibility = VISIBLE
    }

    override fun onClick(clickedView: View?) {
        when (clickedView) {
            mLoginButton -> {
                mViewModel.loginUser(mEmailEntry.text.toString(), mPasswordEntry.text.toString())
                showLoading()
            }
            mRegisterButton -> {
                mViewModel.registerUser(mEmailEntry.text.toString(), mPasswordEntry.text.toString())
                showLoading()
            }
            mFetchGreetingsButton -> {
                mViewModel.fetchGreetings()
                showLoading()
            }
        }
    }
}
