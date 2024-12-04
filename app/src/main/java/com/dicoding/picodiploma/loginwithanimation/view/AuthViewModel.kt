package com.dicoding.picodiploma.loginwithanimation.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.remote.response.LoginResult
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        // Launch coroutine in the ViewModel scope
        viewModelScope.launch {
            try {
                // Call the repository's register function
                val message = userRepository.register(name, email, password)

                // On success, pass true and the message
                onResult(true, message)
            } catch (e: Exception) {
                // On error, pass false and the error message
                onResult(false, e.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResult>> {
        return userRepository.login(email, password).asLiveData()
    }
}
