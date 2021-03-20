package com.example.data.ext

import android.util.Log
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloNetworkException
import com.example.data.mapper.toDomain
import com.example.domain.model.Result
import com.example.domain.model.Error
import java.net.UnknownHostException

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> =
    try {
        val response = call.invoke()

        when {
            response.hasErrors() -> Result.Failure(response.errors!!.map { it.toDomain() })
            else -> Result.Success(response.data as T)
        }

    } catch (e: ApolloNetworkException) {
        when (e.cause) {
            is UnknownHostException -> Result.Failure(
                listOf(
                    Error.Network.NotConnected(originalException = e)
                )
            )
            else -> Result.Failure(listOf(Error.Network.Unknown(e.message.orEmpty(), e)))
        }
    } catch (e: Exception) {
        Log.d("Unknown request error", "${e.javaClass.simpleName} ${e.message}")
        Result.Failure(listOf(Error.Unknown(e.message.orEmpty(), e)))
    }