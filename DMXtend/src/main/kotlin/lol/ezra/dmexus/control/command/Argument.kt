package lol.ezra.dmexus.control.command

class Argument<T : Any>(private val content: T) {
   fun asInt() = content as Int
   fun asString() = content as String
   fun asDouble() = content as Double
   fun asLong() = content as Long
   fun asFloat() = content as Float
   fun asShort() = content as Short
   fun asByte() = content as Byte
   fun asBoolean() = content as Boolean
   fun <V : Any> asType() = content as V
}