package ch.ubique.libs.kmpanion.extensions

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.*
import platform.posix.memcpy

@OptIn(BetaInteropApi::class)
val NSData.stringValue: String?
	get() = NSString.create(this, NSUTF8StringEncoding) as String?


@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
	return ByteArray(this.length.toInt()).apply {
		usePinned {
			memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
		}
	}
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData {
	return this.usePinned {
		NSData.dataWithBytes(it.addressOf(0), this.size.toULong())
	}
}