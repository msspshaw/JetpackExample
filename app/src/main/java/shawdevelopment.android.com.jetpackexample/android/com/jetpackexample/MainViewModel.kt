package shawdevelopment.android.com.jetpackexample.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import shawdevelopment.android.com.jetpackexample.android.com.jetpackexample.GREETINGS_COLLECTION
import shawdevelopment.android.com.jetpackexample.android.com.jetpackexample.GREETINGS_MESSAGE_KEY

class MainViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    private var mUser = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = mUser
    private var mGreetings = MutableLiveData<MutableList<String>>()
    val greetings: LiveData<MutableList<String>>
        get() = mGreetings

    init {
        mUser.value = mAuth.currentUser
    }

    fun registerUser(email:String, password:String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        mUser.value = mAuth.currentUser
                    } else {
                        TODO("Figure out how to respond to registration failure")
                    }
                })
    }

    fun loginUser(email: String, password:String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        mUser.value = mAuth.currentUser
                    } else {
                        TODO("Figure out how to respond to login failure")
                    }
                })
    }

    fun fetchGreetings() {
        FirebaseFirestore.getInstance().collection(GREETINGS_COLLECTION)
                .get()
                .addOnCompleteListener ({task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            mGreetings.value?.add(document.data.get(GREETINGS_MESSAGE_KEY).toString())
                        }
                    }
                    else {
                        mGreetings.value?.add("Failed")
                    }
                })
    }
}
