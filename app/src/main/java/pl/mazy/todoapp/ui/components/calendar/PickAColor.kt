package pl.mazy.todoapp.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PickAColor(closeAdder: () -> Unit = {},returnString: (String) -> Unit){
    val listOfColors = listOf("#5b2c6f","#884ea0","#2471a3","#17a589","#229954","#d4ac0d","#f39c12","#ba4a00","#cb4335","#839192")
    fun closeAdderAndReturn(color:Int): String {
        closeAdder()
        return listOfColors[color]
    }
    LazyVerticalGrid(modifier = Modifier
        .fillMaxWidth(),
        columns = GridCells.Adaptive(80.dp),
    ){
        for (i in listOfColors) {
            item {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .padding(10.dp)
                        .background(
                            Color(
                                android.graphics.Color.parseColor(
                                    listOfColors[listOfColors.indexOf(i)]
                                )
                            )
                        )
                        .clickable {
                            returnString(closeAdderAndReturn(listOfColors.indexOf(i)))
                        }
                )
            }
        }
    }
}