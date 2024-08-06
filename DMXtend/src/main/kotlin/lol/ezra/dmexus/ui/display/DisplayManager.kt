package lol.ezra.dmexus.ui.display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import lol.ezra.dmexus.ui.display.gridded.grid.GridContext

object DisplayManager {
   private val displays = mutableStateMapOf<String, Display>()

   fun open(id: String) {
      displays[id]?.open = true
   }

   fun close(id: String) {
      displays[id]?.open = true
   }

   @Composable
   fun create(content: GridContext.() -> Unit) {
//      displays[display.id] = display
//      display.build()
   }
}