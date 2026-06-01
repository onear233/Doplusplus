package com.onear.doplusplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.onear.doplusplus.ui.screen.MainScreen
import com.onear.doplusplus.ui.theme.DoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoTheme {
//                DoApp()
                MainScreen()
            }
        }
    }
}

//@PreviewScreenSizes
//@Composable
//fun DoApp() {
//    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
//
//    NavigationSuiteScaffold(
//        navigationSuiteItems = {
//            AppDestinations.entries.forEach {
//                item(
//                    icon = {
//                        Icon(
//                            it.icon,
//                            contentDescription = it.label
//                        )
//                    },
//                    label = { Text(it.label) },
//                    selected = it == currentDestination,
//                    onClick = { currentDestination = it }
//                )
//            }
//        }
//    ) {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            Greeting(
//                content = when,
//                modifier = Modifier.padding(innerPadding)
//            )
//            previewGiveSomeCard()
//        }
//    }
//}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(content: String, modifier: Modifier = Modifier) {
    MaterialTheme() {
        Text(
            text = content,
            modifier = modifier
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoTheme {
        Greeting("Android")
    }
}

data class Message(val author : String,val content : String)


@Composable
fun GiveSomeCard(msg: Message){
    Column() {
        Text(msg.author)
        Text(msg.content)
    }

}


@Preview
@Composable
fun previewGiveSomeCard(){
    GiveSomeCard(
        msg = (Message("gunmu","woshigunmu"))
    )
}