package com.example.domain.model

sealed class Error: Exception() {
    abstract override val message: String
    abstract val originalException: Exception

    data class Server(
        override val message: String,
        override val originalException: Exception
    ) : Error()

    sealed class Network : Error() {
        data class NotConnected(
            override val message: String = "No internet connection",
            override val originalException: Exception
        ) : Network()

        data class Unknown(
            override val message: String,
            override val originalException: Exception
        ) : Network()
    }

    data class Unknown(
        override val message: String,
        override val originalException: Exception
    ) : Error()
}