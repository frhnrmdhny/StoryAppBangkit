package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.remote.AuthApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.remote.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.remote.response.LoginResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val authApiService: AuthApiService,
) {

    suspend fun register(name: String, email: String, password: String): String {
        return try {
            val response = authApiService.register(name, email, password)
            response.message
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            throw Exception(errorResponse?.message ?: "Gagal untuk mendaftar, coba lagi nanti.")
        }
    }

    fun login(email: String, password: String): Flow<Result<LoginResult>> = flow {
        emit(Result.Loading)
        try {
            val response = authApiService.login(email, password)
            val result = response.loginResult
            saveToken(result.token)
            saveSession(UserModel(email, result.token, password, true))
            emit(Result.Success(result))
        } catch (e: HttpException) {
            emit(Result.Error(e.message()))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveToken(token: String) {
        userPreference.saveToken(token)
    }

    fun getUserSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            authApiService: AuthApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, authApiService)
            }.also { instance = it }
    }
}