import com.example.myapplication.Msg

/**
 * SampleData for Jetpack Compose Tutorial 
 */
object SampleData {
    // Sample conversation data
    fun getConversationSample(userName: String): List<Msg> {
        return listOf(
            Msg(
                userName,
                "Test...Test...Test..."
            ),
            Msg(
                userName,
                """List of Android versions:
                |Android KitKat (API 19)
                |Android Lollipop (API 21)
                |Android Marshmallow (API 23)
                |Android Nougat (API 24)
                |Android Oreo (API 26)
                |Android Pie (API 28)
                |Android 10 (API 29)
                |Android 11 (API 30)
                |Android 12 (API 31)""".trim()
            ),
            Msg(
                userName,
                """I think Kotlin is my favorite programming language.
                |It's so much fun!""".trim()
            ),
            Msg(
                userName,
                "Searching for alternatives to XML layouts..."
            ),
            Msg(
                userName,
                """Hey, take a look at Jetpack Compose, it's great!
                |It's the Android's modern toolkit for building native UI.
                |It simplifies and accelerates UI development on Android.
                |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim()
            ),
            Msg(
                userName,
                "It's available from API 21+ :)"
            ),
            Msg(
                userName,
                "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
            ),
            Msg(
                userName,
                "Android Studio next version's name is Arctic Fox"
            ),
            Msg(
                userName,
                "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
            ),
            Msg(
                userName,
                "I didn't know you can now run the emulator directly from Android Studio"
            ),
            Msg(
                userName,
                "Compose Previews are great to check quickly how a composable layout looks like"
            ),
            Msg(
                userName,
                "Previews are also interactive after enabling the experimental setting"
            ),
            Msg(
                userName,
                "Have you tried writing build.gradle with KTS?"
            ),
        )
    }
}
