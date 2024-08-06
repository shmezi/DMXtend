package lol.ezra.dmexus.control.model

import lol.ezra.dmexus.ui.display.Display

data class ShowFile(
   val version: Int,
   val displays: MutableMap<String, Display>
)