package lol.ezra.dmexus

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Theme(content: @Composable (() -> Unit)) =
   MaterialTheme(
//      typography = Typography(h1 = TextStyle(fontSize = 13.sp, color = Color.Red)),
      content = content
   )