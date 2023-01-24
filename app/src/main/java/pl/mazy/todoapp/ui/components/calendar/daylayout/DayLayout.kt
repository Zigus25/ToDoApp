package pl.mazy.todoapp.ui.components.calendar.daylayout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.logic.data.Event
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import pl.mazy.todoapp.ui.components.calendar.schedule.SingleEvent
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun CalendarLayout(modifier: Modifier,content: CalendarScope.() -> Unit){
    val overlappingList = mutableListOf<Int>()
    val startPointList = mutableListOf<Int>()
    val heightList = mutableListOf<Int>()
    val pxValue = with(LocalDensity.current) { 1.dp.toPx() }
    val elements = mutableListOf<@Composable () -> Unit>()
    with(object : CalendarScope {
        override fun item(time1: LocalTime, time2: LocalTime, content: @Composable () -> Unit) {
            startPointList.add((time1.hour) * 180 + time1.minute * 3)
            heightList.add((time1.until(time2, ChronoUnit.MINUTES)).toInt() * 3)
            elements.add(content)
        }
    }) {
        content()
    }
    for (s in startPointList){
        overlappingList.add(0)
    }
    for (start in startPointList){
        val end = start + heightList[startPointList.indexOf(start)]
        for (star in startPointList){
            if (star in (start + 1) until end) {
                overlappingList[startPointList.indexOf(star)] += 1
                if (overlappingList[startPointList.indexOf(star) - 1] != 0) {
                    overlappingList[startPointList.indexOf(star)] =
                        overlappingList[startPointList.indexOf(star) - 1] + 1
                }
            }
        }
    }
    Layout(
        modifier = modifier,
        content = {
            for (el in elements){
                el()
            }
        }
    ){
        measures, constraints ->
        val placeables = measures.mapIndexed { index,measurable ->
            if (overlappingList[index]==1002) {
                measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth / 2,
                        minWidth = constraints.minWidth / 2
                    )
                )
            }else{
                measurable.measure(
                    constraints
                )
            }
        }

        layout(constraints.maxWidth, constraints.maxHeight) {

            placeables.forEachIndexed { index, placeable ->
                if (overlappingList[index]!=1002&&overlappingList[index]!=1003) {
                    placeable.placeRelative(
                        x = overlappingList[index] * 200,
                        y = (startPointList[index] * pxValue).toInt()
                    )
                }else{
                    placeable.placeRelative(
                        x = 0,
                        y = (startPointList[index] * pxValue).toInt()
                    )
                }
            }
        }
    }
}

interface CalendarScope{
    fun item(time1:LocalTime,time2:LocalTime,content: @Composable () -> Unit)
    fun item(navController: NavController<Destinations>, event: Event){
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time1 = LocalTime.parse(event.TimeStart, formatter)
        val time2 = LocalTime.parse(event.TimeEnd, formatter)
        item(time1,time2) { SingleEvent(navController,event) }
    }
}