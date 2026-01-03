package pl.kwasow.flamingo.backend.extensions

import com.google.firebase.messaging.BatchResponse

fun BatchResponse.showFailed(): String {
    val res = StringBuilder()

    res.append("[")

    responses?.forEach { response ->
        if (!response.isSuccessful) {
            res.append("\"")
            res.append(response.exception.localizedMessage)
            res.append("\"")
            res.append(",")
        }
    }

    res.append("]")

    return res.toString()
}
