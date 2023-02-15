package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController

@Composable
fun SignUp(navController: NavController<Destinations>){
    var login by remember { mutableStateOf("") }
    var mailU by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = login,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { login = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("Login") },
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = mailU,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { mailU = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("e-Mail") },
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = passwd,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { passwd = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("Password") },
        )
        Button(onClick = { /*TODO*/ },modifier = Modifier.padding(top = 120.dp)) {
            Text(text = "Sign Up")
        }
    }
}

@Composable
fun SignIn(navController: NavController<Destinations>){
    var login by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = login,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { login = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("Login") },
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = passwd,
            textStyle= TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
            ),
            onValueChange = { passwd = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            label = { Text("Password") },
        )
        Button(onClick = { /*TODO*/ },modifier = Modifier.padding(top = 120.dp)) {
            Text(text = "Sign In")
        }
        Text(
            text = "Sign Up Now",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable { navController.navigate(Destinations.SignUp) }.padding(top = 10.dp))
    }
}