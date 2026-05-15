package ch.ubique.libs.kmpanion.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

// Font Color Modifiers
fun TextStyle.color(color: Color) = this.copy(color = color)
val TextStyle.white get() = this.copy(color = Color.White)
val TextStyle.black get() = this.copy(color = Color.Black)

// Font Weight Modifiers
val TextStyle.thin get() = this.copy(fontWeight = FontWeight.Thin)
val TextStyle.extraLight get() = this.copy(fontWeight = FontWeight.ExtraLight)
val TextStyle.light get() = this.copy(fontWeight = FontWeight.Light)
val TextStyle.medium get() = this.copy(fontWeight = FontWeight.Medium)
val TextStyle.semiBold get() = this.copy(fontWeight = FontWeight.SemiBold)
val TextStyle.bold get() = this.copy(fontWeight = FontWeight.Bold)
val TextStyle.extraBold get() = this.copy(fontWeight = FontWeight.ExtraBold)

// Font Style Modifiers
val TextStyle.italic get() = this.copy(fontStyle = FontStyle.Italic)

// Text Decoration Modifiers
val TextStyle.underline get() = this.copy(textDecoration = TextDecoration.Underline)
val TextStyle.strikeThrough get() = this.copy(textDecoration = TextDecoration.LineThrough)