package com.example.calorietracker

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.calorietracker.ui.theme.CalorieTrackerTheme

class AddNutritiousFoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                AddNutritiousFoodScreen()
            }
        }
    }
}

@Composable
fun AddNutritiousFoodScreen() {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("Breakfast") }
    var selectedCategory by remember { mutableStateOf("Vegetable") }
    var showImagePicker by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    val categories = listOf("Vegetable", "Fruit", "Meat", "Dairy")

    // Camera and Gallery launchers
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        capturedImageBitmap = bitmap // Handle the captured bitmap
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        capturedImageBitmap = null // Reset bitmap if using gallery
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Add Nutritious Food",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = calories,
                onValueChange = { calories = it },
                label = { Text("Calories (kcal)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = protein,
                onValueChange = { protein = it },
                label = { Text("Protein (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = carbs,
                onValueChange = { carbs = it },
                label = { Text("Carbohydrates (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fat,
                onValueChange = { fat = it },
                label = { Text("Fat (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                var expanded by remember { mutableStateOf(false) }

                OutlinedButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(selectedMealType)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mealTypes.forEach { meal ->
                        DropdownMenuItem(
                            text = { Text(text = meal) },
                            onClick = {
                                selectedMealType = meal
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                var expandedCategory by remember { mutableStateOf(false) }

                OutlinedButton(
                    onClick = { expandedCategory = true },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(selectedCategory)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(text = category) },
                            onClick = {
                                selectedCategory = category
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { showImagePicker = true }) {
                Text("Add Image")
            }

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

            Spacer(modifier = Modifier.height(16.dp))

            // Display selected image or placeholder
            capturedImageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: selectedImageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(data = uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = rememberImagePainter(data = R.drawable.img), // Placeholder image
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Add button to save food details
            Button(
                onClick = {
                    // Handle the save action (e.g., save the form data to a database)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp), // Adjust button height
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EE))
            ) {
                Text("Add Food", color = Color.White)
            }
        }
    }
}

