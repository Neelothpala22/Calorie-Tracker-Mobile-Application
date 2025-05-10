package com.example.calorietracker

import CalorieModel
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_compose_database.CalorieData
import com.example.kotlin_compose_database.CaloriesDao
import com.example.kotlin_compose_database.DatabaseProvider
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import coil.compose.rememberAsyncImagePainter
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import java.time.LocalDate
import androidx.compose.material3.CircularProgressIndicator


class AddFoodItem : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalorieTrackerTheme {
                val calorieModel: CalorieModel = viewModel()
                val db = DatabaseProvider.getDatabase(this)
                addPage(calorieView = calorieModel, calorieDao = db.caloriesDao())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun addPage(calorieView: CalorieModel, calorieDao: CaloriesDao) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val storageManager = FirebaseStorageManager()
    var loading by remember { mutableStateOf(false)}

    // Launchers for camera and gallery
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            // Convert Bitmap to Uri using a helper function
            val uri = bitmapToUri(context, it)
            calorieView.photoUri = uri
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            calorieView.photoUri = it
        }
    }

    // State to control the image picker dialog
    var showImagePicker by remember { mutableStateOf(false) }

    if (showImagePicker) {
        AlertDialog(
            onDismissRequest = { showImagePicker = false },
            title = { Text("Select Image") },
            text = { Text("Choose an option to select a photo") },
            confirmButton = {
                TextButton(onClick = {
                    showImagePicker = false
                    cameraLauncher.launch(null)
                }) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImagePicker = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Gallery")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Clickable Image Placeholder
        if (calorieView.photoUri != null) {
            // Display the selected image
            Image(
                painter = rememberAsyncImagePainter(calorieView.photoUri),
                contentDescription = "Selected Meal Photo",
                modifier = Modifier
                    .size(150.dp)
                    .clickable { showImagePicker = true }
            )
        } else {
            // Display a default image or placeholder
            Image(
                painter = painterResource(id = R.drawable.img), // Replace with your default image
                contentDescription = "Default Meal Photo",
                modifier = Modifier
                    .size(150.dp)
                    .clickable { showImagePicker = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = calorieView.foodName,
            onValueChange = { calorieView.foodName = it },
            label = { Text(text = "Enter Food Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Meal Type
        ExposedDropdownMenuBox(
            expanded = calorieView.expanded,
            onExpandedChange = { calorieView.expanded = !calorieView.expanded }
        ) {
            TextField(
                value = calorieView.selected,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Meal Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = calorieView.expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
            ExposedDropdownMenu(
                expanded = calorieView.expanded,
                onDismissRequest = { calorieView.expanded = false }
            ) {
                calorieView.mealChoices.forEach { choice ->
                    DropdownMenuItem(
                        text = { Text(text = choice) },
                        onClick = {
                            calorieView.selected = choice
                            calorieView.expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        if(loading){
            CircularProgressIndicator()
        }
        else {

            Button(
                onClick = {
                    if(!loading) {
                        loading=true

                        if (calorieView.foodName.isNotEmpty()) {
                            calorieView.fetchCalories(calorieView.foodName)
                            coroutineScope.launch {

                                val downloadUrl =
                                    storageManager.uploadImage(context, calorieView.photoUri!!)
                                if (downloadUrl != null && calorieView.calories.value != null) {

                                    createFoodItem(
                                        calView = calorieView,
                                        calorieDao = calorieDao,
                                        photoUrl = downloadUrl
                                    )
                                    Toast.makeText(
                                        context,
                                        "Meal added successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(context, FoodList::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Failed ", Toast.LENGTH_SHORT).show()
                                }
                                loading=false

                            }

                        }

                    }


                },


                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Post")
            }
        }
    }
}

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "SelectedMeal", null)
    return Uri.parse(path)
}

private fun createFoodItem(calView: CalorieModel, calorieDao: CaloriesDao, photoUrl: String) {
    val foodName = calView.foodName
    val createdDate = LocalDate.now().toString()
    val foodCategory = calView.selected
    val calories = calView.calories.value

    if(calories!=null){
        val data = CalorieData(
            foodName = foodName,
            dateCreated = createdDate,
            foodCategory = foodCategory,
            calories = calories,
            photoUri = photoUrl
        )
        calorieDao.insertData(data)
    }

}
