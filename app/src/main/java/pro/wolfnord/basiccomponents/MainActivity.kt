package pro.wolfnord.basiccomponents

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.wolfnord.basiccomponents.ui.theme.BasicComponentsTheme
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicComponentsTheme {
                Scaffold { innerPadding ->
                    MainDisplay(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainDisplay(modifier: Modifier) {
    var url by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current // Get context here

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            value = url,
            onValueChange = { url = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.White)
                .padding(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (coolURL(url, context)) {
                downloadImage(url, context) { bitmap ->
                    imageBitmap = bitmap
                    url = "" // Clear the TextField after download
                }
            }
        }) {
            Text("Загрузить фото", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(200.dp))
        }
    }
}

fun coolURL(url: String, context: android.content.Context): Boolean {
    val containsSubString = url.endsWith(".jpg")
    if (!containsSubString) {
        Log.i("Фотка", "Неправильное раcширение")
        Toast.makeText(context, "Неправильное раcширение", Toast.LENGTH_SHORT).show()
        return false
    }
    Log.i("Фотка", "URL полученно")
    return true
}

fun downloadImage(url: String, context: android.content.Context, onSuccess: (Bitmap) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val imageBitmap = BitmapFactory.decodeStream(URL(url).openStream())
            Log.i("Фотка", "Фото получено")
            withContext(Dispatchers.Main) {
                onSuccess(imageBitmap)
                Log.i("Фотка", "Фото отображено")
                saveImageToGallery(imageBitmap, context) // Pass context to the save function
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Log.i("Фотка", "Ошибка при загрузке изображения")
                Toast.makeText(context, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun saveImageToGallery(bitmap: Bitmap, context: android.content.Context) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppName")
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        val outputStream = resolver.openOutputStream(it)
        outputStream?.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            Log.i("Сохранение", "Фото сохранено")
        }
    }
}
