package lol.ezra.dmexus.control.command.testing

import lol.ezra.dmexus.control.command.Argument
import lol.ezra.dmexus.control.command.Command
import lol.ezra.dmexus.control.command.Wing

class SelectCommand : Command {
   override fun execute(sender: Wing, args: Map<String, Argument<*>>) {
      args["value"]?.asByte()
   }
}