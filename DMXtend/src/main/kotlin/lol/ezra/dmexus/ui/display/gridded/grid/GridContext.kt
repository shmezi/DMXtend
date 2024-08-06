package lol.ezra.dmexus.ui.display.gridded.grid

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints


class GridContext private constructor() {
   /**
    * Bugs occurs with recompisiton using the by keyword. If
    */
   var widthState = mutableStateOf(0)
   var heightState = mutableStateOf(0)

   private var width by widthState
   private var height by heightState

   private var columns by mutableStateOf(1)
   private var rows by mutableStateOf(1)

   private val cells = mutableStateMapOf<Cellable, MutableSet<Pair<Int, Int>>>()


   /**
    * Occupation of cells and movement
    * We use a "Precompiled" list set of all the locations used as a cache from [cells]
    */
   private val occupations = mutableSetOf<Pair<Int, Int>>()

   /**
    * Update the location of a [Cellable]
    * @param cellable The cell to be moved
    * @param occupyAt The cells that the [Cellable] will take up
    * @return Weather the operation could be done.
    */
   fun updateOccupation(cellable: Cellable, occupyAt: MutableSet<Pair<Int, Int>>): Boolean {
      /**
       * Filtering out the previous location.
       */
      val currentOccupation = cells.getOrPut(cellable) { mutableSetOf() }
      val occupied = occupations.filter { !currentOccupation.contains(it) }
      /**
       * Validating the new location is unoccupied
       */
      occupyAt.forEach {
         if (occupied.contains(it)) {
            return false
         }
      }
      /**
       * Updating cache of occupations and such
       */
      occupations.removeAll(currentOccupation)
      occupations.addAll(occupyAt)
      cells[cellable] = occupyAt
      return true
   }

   /**
    * Retrieve the graphical width of a cell or multiple
    * @param cellW The amount of columns the cell is wide to calculate.
    * @return The graphical width of the cell
    */
   fun cellWidth(cellW: Int = 1) = cellW * (width / columns)

   /**
    * Retrieve the graphical height of a cell or multiple
    * @param cellH The amount of rows the cell is tall to calculate.
    * @return The graphical height of the cell
    */
   fun cellHeight(cellH: Int = 1) = cellH * (height / rows)


   companion object {
      /**
       * Defines the content the layout will use.
       */
      @Composable
      private fun placeChildren(context: GridContext) {
         context.cells.forEach {

            it.key.build()
         }
      }

      /**
       * Define the constraints of the grid and place children inside.
       */
      private fun MeasureScope.defineLayout(
         measurables: List<Measurable>,
         constraints: Constraints,
      ): MeasureResult {

         val maxW = constraints.maxWidth
         val maxH = constraints.maxHeight


         val values = measurables.map {
            it.measure(constraints)
         }

         return layout(maxW, maxH) {
            values.forEach {
               it.place(0, 0)
            }
         }

      }

      /**
       * Build a new Grid that cellables can be placed inside.
       * @param modifier Modifier for entire Grid
       * @param contents The contents of the grid
       */
      @Composable
      fun Grid(
         modifier: Modifier = Modifier, columns: Int = 1, rows: Int = 1, contents: (GridContext.() -> Unit)
      ) {

         /**
          * Build and apply settings and contents
          */
         val context = remember {
            GridContext().apply { contents() }
         }
         context.columns = columns
         context.rows = rows



         Layout(modifier = modifier, content = {
            placeChildren(context)
         }, measurePolicy = { measurables, constraints ->

            val maxW = constraints.maxWidth
            val maxH = constraints.maxHeight

            context.width = maxW
            context.height = maxH


            val values = measurables.map {
               it.measure(constraints)
            }

            layout(maxW, maxH) {
               values.forEach {
                  it.place(0, 0)
               }
            }
         })

      }

   }
}
