package ch.ubique.libs.kmpanion.compose.extensions

import android.content.ClipData
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.toClipEntry

suspend fun Clipboard.setText(text: String, label: String = text) {
	val clipData = ClipData.newPlainText(label, text)
	setClipEntry(clipData.toClipEntry())
}

suspend fun Clipboard.getText(): String? {
	val clipData = getClipEntry()
		?.clipData
		?.takeIf { it.itemCount > 0 }
		?: return null

	return clipData.getItemAt(0).text?.toString()
}