package lol.ezra.dmexus.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.*

object KeyboardInput {
   private val active = mutableSetOf<Key>()
   var shift by mutableStateOf(true)
   var alt by mutableStateOf(false)
   var cntrl by mutableStateOf(false)

   fun onInput(e: KeyEvent): Boolean {
      shift = e.isShiftPressed
      alt = e.isAltPressed
      cntrl = e.isCtrlPressed
      if (e.type == KeyEventType.KeyDown)
         active.add(e.key)
      else
         active.remove(e.key)

      return true
   }
}