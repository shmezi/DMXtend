package lol.ezra.dmexus.ui.display.gridded.grid

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import co.touchlab.kermit.Logger
import lol.ezra.dmexus.ui.KeyboardInput.shift
import kotlin.math.roundToInt

/**
 * Defines a cell in a Grid
 * @param x The X position on the grid
 * @param y The Y position on the grid
 * @param width The amount of width the cell takes up
 * @param height The amount of height the cell takes up
 */
abstract class Cellable(
   val modifier: Modifier,
   val context: GridContext,
   val width: Int,
   val height: Int,
   x: Int,
   y: Int,
   val color: Color = Color.Red,
) {
   init {
      Logger.i("Could be an issue later here?")
      if (!context.updateOccupation(this, occupyAt(x, y))) throw OverlappingCellsException(x, y)
   }


   /**
    * Gridded position
    */
   private var x by mutableStateOf(x)
   private var y by mutableStateOf(y)

   fun xEndPose() = x + width - 1

   fun yEndPose() = y + height - 1


   /**
    * Sizing of the grid
    */
   private val gridWidth by context.widthState
   private val gridHeight by context.heightState


   /**
    * The width of the cell in compose
    * @return Returns the width in dp of the cell
    */
   private fun graphicalWidth() = context.cellWidth(width)

   /**
    * The height of the cell in compose
    * @return Returns the height in dp of the cell
    */
   private fun graphicalHeight() = context.cellHeight(height)

   /**
    * Dragging positioning
    * These are temporary values until snapping occurs
    */
   private var dragX by mutableStateOf(0f)
   private var dragY by mutableStateOf(0f)

   /**
    * The position offset in compose
    * @return Returns the offset of the X axis in compose
    */
   private fun graphicalXPose() = x * context.cellWidth() + dragX

   /**
    * The position of the end of the cell in compose
    * @return Returns the offset of the X axis of the end of the cell.
    */
   private fun graphicalXEndPose() = graphicalXPose() + graphicalWidth()

   /**
    * The position of the end of the cell in compose
    * @return Returns the offset of the Y axis of the end of the cell.
    */
   private fun graphicalYEndPose() = graphicalYPose() + graphicalHeight()

   /**
    * The position offset in compose
    * @return Returns the offset of the Y axis in compose
    */
   private fun graphicalYPose() = y * context.cellHeight() + dragY

   /**
    * Get the location this cell would take up at a given position
    * @param x position
    * @param y position
    * @return The positions that this cell will occupy in it's parent grid
    */

   private fun occupyAt(x: Int, y: Int): MutableSet<Pair<Int, Int>> {
      val coords = mutableSetOf<Pair<Int, Int>>()
      for (xV in x ..< width + x) {
         for (yV in y ..< height + y) {
            coords.add(Pair(xV, yV))

         }
      }
      return coords
   }


   /**
    * The origin point before a dragging period starts
    * Used to reset the position if the cell was dragged to an illegal position.
    */
   private var originX by mutableStateOf(0)
   private var originY by mutableStateOf(0)

   /**
    * zIndex affects that layer that is on top.
    * This value should be used as 0 and 1, indicating on top or not.
    */
   private var zIndex by mutableStateOf(0f)


   /**
    * Called upon at the start of the cell being dragged
    */
   private fun dragStart(origin: Offset) {
      originX = x
      originY = y
      zIndex = 1f
   }

   /**
    * Called upon during the period that the cell is being dragged
    */
   private fun drag(change: PointerInputChange, dragAmount: Offset) {
      change.consume()
      /**
       * New drag position additions
       */
      val dX = dragAmount.x
      val dY = dragAmount.y

      val xOutOfBounds = dX + graphicalXEndPose() > gridWidth || dX + graphicalXPose() < 0
      val yOutOfBounds = dY + graphicalYEndPose() > gridHeight || dY + graphicalYPose() < 0
      if (xOutOfBounds || yOutOfBounds) return

      dragX += dX
      dragY += dY
   }

   /**
    * Called upon at the end of the cell being dragged
    */
   private fun dragEnd() {
      zIndex = 0f
      val cw = context.cellWidth()
      val ch = context.cellHeight()
      val xPose = (graphicalXPose())
      val yPose = (graphicalYPose())


      /**
       * Snapping of the cell to the grid
       */
      val xEndStop = gridWidth - context.cellWidth(width - 1)
      val yEndStop = gridHeight - context.cellHeight(height - 1)
      val newX = (((if (xPose > xEndStop) xPose + xEndStop else xPose)) / cw).roundToInt()
      val newY = (((if (yPose > yEndStop) yPose + yEndStop else yPose)) / ch).roundToInt()

      if (context.updateOccupation(this, occupyAt(newX, newY))) {
         x = newX
         y = newY
      }

      dragX = 0f
      dragY = 0f
   }

   @Composable
   abstract fun BoxScope.contents()

   /**
    * Build the composable cell
    */
   @Composable
   fun build() {
      Box(modifier = Modifier.offset(graphicalXPose().dp, graphicalYPose().dp).zIndex(zIndex)
         .clip(RoundedCornerShape(10)).size(graphicalWidth().dp, graphicalHeight().dp)
         .background(color, RoundedCornerShape(10)).border(3.dp, Color.Black, RoundedCornerShape(10)).run {
            if (shift) pointerInput(Unit) {

               detectDragGestures(
                  onDragStart = ::dragStart, onDrag = ::drag, onDragEnd = ::dragEnd
               )
            }
            else this
         }.then(modifier), content = { contents() }

      )

   }
}