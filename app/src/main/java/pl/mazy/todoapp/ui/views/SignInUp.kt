package pl.mazy.todoapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import pl.mazy.todoapp.logic.data.LoginData
import pl.mazy.todoapp.logic.data.repos.AccountRep
import pl.mazy.todoapp.logic.navigation.Destinations
import pl.mazy.todoapp.logic.navigation.NavController
import java.util.regex.Pattern

@Composable
fun SignUp(navController: NavController<Destinations>){
    val userRepository: AccountRep by localDI().instance()
    var login by remember { mutableStateOf("") }
    var mailU by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    var showErrorL by remember { mutableStateOf(false) }
    var showErrorM by remember { mutableStateOf(false) }
    var showErrorP by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun errorM(){
        if (showErrorL){
            errorMessage += " invalid login"
        }
        if (showErrorM){
            errorMessage += " invalid E-Mail"
        }
        if (showErrorP){
            errorMessage += " invalid Passwd"
        }
    }

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
            isError = showErrorL,
            onValueChange = {
                login = it
                showErrorL = !isValidLogin(it)
                errorMessage = ""
                errorM()
            },
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
            isError = showErrorM,
            onValueChange = {
                mailU = it
                showErrorM = !isValidEmail(it)
                errorMessage = ""
                errorM()
            },
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
            onValueChange = {
                passwd = it
                showErrorP = !isValidPasswd(it)
                errorMessage = ""
                errorM()
            },
            isError = showErrorP,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,keyboardType = KeyboardType.Password),
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, contentDescription = "")
                }
            }
        )
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
             if((!showErrorL&&!showErrorM&&!showErrorP)&&login!=""&&passwd!=""&&mailU!=""){
                 userRepository.signUpUser(login,passwd,mailU)
                 LoginData.logIn(login,userRepository.getActiveUser().first)
                 navController.navigate(Destinations.TaskList)
             }
        },modifier = Modifier.padding(top = 120.dp)) {
            Text(text = "Sign Up")
        }
    }
}

@Composable
fun SignIn(navController: NavController<Destinations>){
    val userRepository: AccountRep by localDI().instance()
    var passwordVisible by remember { mutableStateOf(false) }
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
            onValueChange = {
                passwd = it
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,keyboardType = KeyboardType.Password),
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, contentDescription = "")
                }
            }
        )
        Button(onClick = {
            if(login!=""&&passwd!="") {
                if (userRepository.signInUser(login, passwd)) {
                    navController.navigate(Destinations.TaskList)
                    LoginData.logIn(login,userRepository.getActiveUser().first)
                }
            }
         },modifier = Modifier.padding(top = 120.dp)) {
            Text(text = "Sign In")
        }
        Text(
            text = "Sign Up Now",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clickable { navController.navigate(Destinations.SignUp) }
                .padding(top = 10.dp))
    }
}

fun isValidLogin(loginStr:String) =
    Pattern
        .compile(
            "[A-Za-z0-9]{4,40}$",
            Pattern.CASE_INSENSITIVE
        ).matcher(loginStr).find()
fun isValidEmail(emailStr: String) =
    Pattern
        .compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
        ).matcher(emailStr).find()
fun isValidPasswd(passwdStr:String) =
    Pattern
        .compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$",
            Pattern.CASE_INSENSITIVE
        ).matcher(passwdStr).find()