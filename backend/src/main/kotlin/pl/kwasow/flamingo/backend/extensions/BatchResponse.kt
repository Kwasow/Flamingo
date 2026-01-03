package pl.kwasow.flamingo.backend.extensions

import com.google.firebase.messaging.BatchResponse
import com.sun.org.apache.xml.internal.serializer.utils.Utils.messages

fun BatchResponse.prettyPrint(): String {
    val res = StringBuilder()

    res.append("[")

    responses?.forEach { response ->
        res.append("\"")
        if (response.isSuccessful) {
            res.append("OK")
        } else {
            res.append(response.exception.localizedMessage)
        }
        res.append("\"")

        res.append(", ")
    }

    res.append("]")

    return res.toString()
}
