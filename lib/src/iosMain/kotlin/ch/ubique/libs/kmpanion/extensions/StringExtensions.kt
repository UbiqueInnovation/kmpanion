package ch.ubique.libs.kmpanion.extensions

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.*

@OptIn(BetaInteropApi::class)
fun String.toNSData(): NSData? = NSString.create(string = this).dataUsingEncoding(NSUTF8StringEncoding)

