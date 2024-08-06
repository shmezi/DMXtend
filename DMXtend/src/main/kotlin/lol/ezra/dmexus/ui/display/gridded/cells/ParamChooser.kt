package lol.ezra.dmexus.ui.display.gridded.cells

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import lol.ezra.dmexus.ui.display.gridded.grid.Cellable
import lol.ezra.dmexus.ui.display.gridded.grid.GridContext
import kotlin.math.roundToInt


class ParamChooser(
   modifier: Modifier = Modifier,
   context: GridContext,
   width: Int = 1,
   height: Int = 1,
   color: Color = Color.Red,
   x: Int = 0,
   y: Int = 0,
   val onChange: (UByte) -> Unit,
) : Cellable(modifier, context, width, height, x, y, color) {
   /**
    * Space taken up by stuff like buttons from the area of the slider
    */
   val zeroSize = 10
   val buttonHeights = 10
   val text = 10

   /**
    * Defines the dynamically changing heights of everything
    */
   var maxHeight by mutableStateOf(100)
   var cycle by mutableStateOf(ParamUnit.DMX)

   /**
    * A dynamically changing percentage from the total size of the height.
    */
   fun Int.dh() = maxHeight / this

   /**
    * A single Unit's height. Units can very based on the [ParamUnit]
    */
   fun singleUnit(): Float = ((maxHeight - zeroSize.dh() - buttonHeights.dh() * 2) / (cycle.unit.toFloat()))


   var value by mutableStateOf(0)

   /**
    * Return an actual color value
    */
   fun finalizeValue() = value * 255 / cycle.unit

   fun updateValue(): Color {
      val r = color.red / 255 * finalizeValue()
      val g = color.green / 255 * finalizeValue()
      val b = color.blue / 255 * finalizeValue()
      return Color(r, g, b)
   }

   fun cycleUnit() {
      val former = cycle.unit
      cycle = cycle.next()
      value = (value.toFloat() * cycle.unit.toFloat() / former.toFloat()).roundToInt()
   }


   @Composable
   fun BasicButton(upDown: Boolean) {
      Box(
         modifier.fillMaxWidth().background(Color.Gray).height((maxHeight / 10).dp).clickable {
            val nv = value + if (upDown) 1 else -1
            if (nv <= cycle.unit && nv >= 0) {
               value = nv
               onChange(finalizeValue().toUByte())
            }
         }.zIndex(10f)
      ) {

         Text(if (upDown) "▲ $value" else "▼", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
      }
   }

   @Composable
   override fun BoxScope.contents() {
      BasicButton(true)
      Column(modifier = Modifier.background(color = Color.Gray).background(color = color.copy(0.4f)).fillMaxSize()
         .zIndex(1f)
         .onGloballyPositioned {
            maxHeight = it.size.height
         }.pointerInput(Unit) {
            detectTapGestures(onDoubleTap = {
               cycleUnit()
            })
         }, verticalArrangement = Arrangement.Bottom
      ) {
//         Text("",modifier=Modifier.height())
         Box(modifier = Modifier.fillMaxWidth().height((((value) * singleUnit()) + zeroSize.dh()).dp)
            .background(updateValue())
            .pointerInput(Unit) {
               detectTapGestures(onDoubleTap = {
                  cycleUnit()
               })

            }.draggable(rememberDraggableState {
               val nv = value - it.roundToInt()
               if (nv < 0 || nv > cycle.unit) return@rememberDraggableState
               value = nv
               onChange(finalizeValue().toUByte())
            }, Orientation.Vertical)
         ) {

         }
         BasicButton(false)
      }

   }


}