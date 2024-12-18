package lol.ezra.dmexus.ui.display.gridded.cells

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import lol.ezra.dmexus.ui.display.gridded.grid.Cellable
import lol.ezra.dmexus.ui.display.gridded.grid.GridContext
import lol.ezra.dmexus.ui.display.gridded.grid.GridContext.Companion.Grid

class ParameterPage(
   modifier: Modifier = Modifier,
   context: GridContext,
   width: Int = 900,
   height: Int = 900,
   x: Int,
   y: Int,
   color: Color = Color.Red

) : Cellable(modifier = Modifier.border(3.dp, Color.Green, RoundedCornerShape(10)), context, width, height, x, y, color = Color.Red) {


   @Composable
   override fun BoxScope.contents() {
      Grid(columns = 5, rows = 1) {
         ParamChooser(x = 0, y = 0, context = this, color = Color.Red) { }
         ParamChooser(x = 1, y = 0, context = this, color = Color.Green) { }
         ParamChooser(x = 2, y = 0, context = this, color = Color.Blue) { }
         ParamChooser(x = 3, y = 0, context = this, color = Color.White) { }
//         ParamChooser(x = 4, y = 0, context = this, color = Color.DarkGray) { }
      }
   }


}