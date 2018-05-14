package com.moonlight.chatapp.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern

/**
 * Created by songyifeng on 2018/4/26.
 */
class CheckUtil {
    companion object {
        fun isEmail(str: CharSequence): Boolean {
            val regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}"
            return match(regex, str)
        }

        private fun match(regex: String, str: CharSequence): Boolean {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(str)
            return matcher.matches()
        }

        fun md5Encode(text: String): String {
            try {
                //获取md5加密对象
                val instance: MessageDigest = MessageDigest.getInstance("MD5")
                // 对字符串加密，返回字节数组
                val digest: ByteArray = instance.digest(text.toByteArray())
                var sb: StringBuffer = StringBuffer()
                for (b in digest) {
                    //获取低八位有效值
                    var i: Int = b.toInt() and 0xff
                    //将整数转化为16进制
                    var hexString = Integer.toHexString(i)
                    if (hexString.length < 2) {
                        //如果是一位的话，补0
                        hexString = "0" + hexString
                    }
                    sb.append(hexString)
                }
                return sb.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}