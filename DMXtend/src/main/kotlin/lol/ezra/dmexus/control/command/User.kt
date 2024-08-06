package lol.ezra.dmexus.control.command

/**
 * Defines a user that can send out commands to the command framework
 */
class User(val id: String) {
   val wings = mutableMapOf<Short, Wing>()
}