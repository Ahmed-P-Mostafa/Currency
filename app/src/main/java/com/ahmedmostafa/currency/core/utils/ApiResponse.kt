package com.ahmedmostafa.currency.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

interface ApiResponse {
    val success: Boolean
    val error: ApiError?
}

interface ApiError {
    val info: String?
}

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> T,
    transform: (T) -> R
): Resource<R> where T : ApiResponse = withContext(Dispatchers.IO) {
    try {
        val response = apiCall()
        if (response.success) {
            Resource.Success(transform(response))
        } else {
            Resource.Error(response.error?.info ?: "Operation failed")
        }
    } catch (e: UnknownHostException) {
        Resource.Error("No Internet connection")
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unexpected error occurred")
    }
}