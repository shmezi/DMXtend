package lol.ezra.dmexus.ui.display.gridded.cells

enum class ParamUnit(val unit: Int) {
   DMX(255),
   PERCENT(100),
   DMX10(10),
   DMX25(25)
   ;


   fun next(): ParamUnit {

      var ni = ordinal + 1
      if (ni > entries.size - 1)
         ni = 0
      return entries[ni]
   }

}