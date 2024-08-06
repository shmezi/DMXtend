package lol.ezra.dmexus.control.command

interface Command {
   fun execute(sender: Wing, args: Map<String, Argument<*>>)
}