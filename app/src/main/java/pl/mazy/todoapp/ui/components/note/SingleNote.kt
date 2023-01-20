@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package pl.mazy.todoapp.ui.components.note

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.Notes
import pl.mazy.todoapp.logic.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleNote(
    note: Notes,
    navController: NavController<Destinations>
){
    Card(
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                navController.navigate(Destinations.NoteDetails(note.name,note.description))
        },
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(all = 3.dp),

        ) {
            Text(text = note.name,
                fontSize = 22.sp,
                modifier = Modifier.padding(7.dp).fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Text(text = note.description,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 7.dp, end = 7.dp, bottom = 8.dp).fillMaxWidth(),
                maxLines = 20,
                overflow = TextOverflow.Ellipsis)
        }
    }

}