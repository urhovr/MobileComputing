package com.example.myapplication

import SampleData
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.rememberAsyncImagePainter
import com.example.myapplication.UserDatabase.Companion.getDatabase
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MessageScreen(navController: NavController) {
    val context = LocalContext.current
    val database = remember { getDatabase(context) }
    var name by remember { mutableStateOf("Guest") }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userData = database.UserDao().getUser()
            if (userData != null) {
                name = userData.userName
            }
        }
    }

    Column {
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = { navController.navigate("DefaultScreen") {
            popUpTo("DefaultScreen") {
                inclusive = true
            }
        }}){
            Text(text = "SETTINGS")
        }
        Conversation(SampleData.getConversationSample(name))
    }
}

data class Msg(val author: String, val body: String)

@Composable
fun MsgCard(msg: Msg) {
    val context = LocalContext.current
    val imageFile = File(context.filesDir, "pfp.jpg")
    val painter = rememberAsyncImagePainter(imageFile)

    Row {
        Image(
            painter = painter,
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Msg>) {
    LazyColumn {
        items(messages) { message ->
            MsgCard(message)
        }
    }
}

/*@Preview
@Composable
fun PreviewConversation() {
    MyApplicationTheme {
        Conversation(SampleData.conversationSample)
    }
}*/

//@Preview(name = "Light Mode")

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)

@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Surface {
            MsgCard(
                msg = Msg("Urho", "Hienoa tekstiä")
            )
        }
    }
}