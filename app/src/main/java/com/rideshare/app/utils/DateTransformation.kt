package com.rideshare.app.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}
fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 12) text.text.substring(0..11) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "."
        if (i == 7) out += " "
        if (i == 9) out += ":"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset +1
            if (offset <= 8) return offset +2
            if (offset <= 11) return offset +3
            if (offset <= 12) return offset +4
            return 15
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <=2) return offset
            if (offset <=5) return offset -1
            if (offset <=10) return offset -2
            if (offset <=12) return offset -3
            return 12
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}