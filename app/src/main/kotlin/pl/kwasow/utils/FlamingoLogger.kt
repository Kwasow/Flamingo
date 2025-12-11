package pl.kwasow.utils

import android.util.Log

object FlamingoLogger {
    // ====== Fields
    private const val TAG = "Flamingo"

    // ====== Public methods

    // Debug
    fun d(message: String) = Log.d(TAG, message)

    fun d(
        message: String,
        throwable: Throwable,
    ) = Log.d(TAG, message, throwable)

    // Error
    fun e(message: String) = Log.e(TAG, message)

    fun e(
        message: String,
        throwable: Throwable,
    ) = Log.e(TAG, message, throwable)

    // Info
    fun i(message: String) = Log.i(TAG, message)

    fun i(
        message: String,
        throwable: Throwable,
    ) = Log.i(TAG, message, throwable)

    // Warn
    fun w(message: String) = Log.w(TAG, message)

    fun w(throwable: Throwable) = Log.w(TAG, throwable)

    fun w(
        message: String,
        throwable: Throwable,
    ) = Log.w(TAG, message, throwable)

    // Verbose
    fun v(message: String) = Log.v(TAG, message)

    fun v(
        message: String,
        throwable: Throwable,
    ) = Log.v(TAG, message, throwable)
}
