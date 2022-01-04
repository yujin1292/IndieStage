package com.jin.android.indiestage.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

object EnterCode {
    fun verifyEnterCode(inputCode: String, enterCode:String):Boolean {
        val sha = try {
            val sh: MessageDigest = MessageDigest.getInstance("SHA-256")
            sh.update(inputCode.encodeToByteArray())
            val byteData: ByteArray = sh.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) sb.append(
                ((byteData[i] and 0xff.toByte()) + 0x100).toString(16).substring(1)
            )
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }

        return sha == enterCode
    }
}