package lol.ezra.dmexus.ui.display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import lol.ezra.dmexus.Theme
import lol.ezra.dmexus.ui.display.gridded.grid.GridContext
import lol.ezra.dmexus.ui.display.gridded.grid.GridContext.Companion.Grid
import lol.ezra.dmexus.utils.pq

class Display(
   val id: String,
   grid: GridContext
) {
   var open by mutableStateOf(true)

   @Composable
   fun build(content: GridContext.() -> Unit) {


      Window(onCloseRequest = { open = false }, visible = open) {
         MenuBar {
            Menu("Testing") {
               Item("test") {
                  "Hey there".pq()
               }
            }
            Menu("Testing") {
               Item("test") {
                  "Hey there".pq()
               }
            }
         }
         Theme {
            Grid(columns = 15, rows = 12) {
               content()

            }
         }
      }
   }
}