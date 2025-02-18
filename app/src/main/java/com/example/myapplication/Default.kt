package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Picture
import android.os.Build
import android.widget.EditText
import android.widget.ImageButton
import android.Manifest
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.rememberAsyncImagePainter
import com.example.myapplication.UserDatabase.Companion.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun Default(navController: NavController, stringLux: String) {
    val context = LocalContext.current
    val database = remember { getDatabase(context) }
    var name by remember { mutableStateOf("") }
    var imageFile = File(context.filesDir, "pfp.jpg")
    /*var imageFile by remember { mutableStateOf(File(context.filesDir, "pfp.jpg")) }*/
    var updateImage by remember { mutableStateOf(false) }
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {imageUri ->
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val outputFile = File(context.filesDir, "pfp.jpg")
            FileOutputStream(outputFile).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            imageFile = outputFile
            updateImage = !updateImage
        }
    }
    createNotificationChannel(context)
    var notificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            notificationPermission = isGranted
            showNotification(context, "Hi!", "Thanks for enabling notifications!")
        }
    )

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userData = database.UserDao().getUser()
            if (userData != null) {
                name = userData.userName
            }
        }
    }
    val painter = if (updateImage) {
        rememberAsyncImagePainter(imageFile)
    } else {
        rememberAsyncImagePainter(imageFile)
    }

    Column {
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = { navController.navigate("MessageScreen") }) {
            Text(text = "MESSAGES")
        }
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "pfp",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
        )
        TextField(
            value = name,
            onValueChange = {name = it},
            label = { Text("Username...")},
            singleLine = true
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                database.UserDao().insertUser(User(userName = name))
            }
        }) {
            Text("SET USERNAME")
        }
        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }) {
            Text(text = "Enable Notifications")
        }
        Text(text = stringLux)
    }
}

