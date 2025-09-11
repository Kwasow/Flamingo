package pl.kwasow.extensions

import android.util.Patterns
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink

@Composable
fun String.linkify() =
    buildAnnotatedString {
        val linkStyle = SpanStyle(color = MaterialTheme.colorScheme.primary)

        val matcher = Patterns.WEB_URL.matcher(this@linkify)
        var lastIndex = 0
        while (matcher.find()) {
            val url = matcher.group()

            append(this@linkify.substring(lastIndex, matcher.start()))

            pushStringAnnotation(tag = "URL", annotation = url)
            withLink(
                LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(linkStyle),
                ),
            ) {
                append(url)
            }

            lastIndex = matcher.end()
        }

        append(this@linkify.substring(lastIndex))
    }
