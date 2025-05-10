package com.example.calorietracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.example.kotlin_compose_database.CalorieData
import com.example.kotlin_compose_database.CaloriesDao
import com.example.kotlin_compose_database.DatabaseProvider

class FoodList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalorieTrackerTheme {
                val db = DatabaseProvider.getDatabase(this)
                foodList(caloriesDao = db.caloriesDao())
            }
        }
    }
}

@Composable
private fun foodList(caloriesDao: CaloriesDao) {
    val context = LocalContext.current
    val calorieData = remember { mutableStateListOf<CalorieData>() }

    LaunchedEffect(Unit) {
        calorieData.clear()
        calorieData.addAll(caloriesDao.getAll())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.weight(1f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(calorieData) { cals ->
                    Log.e("CalData", cals.toString())
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Display the image using the download URL
                            if (cals.photoUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(cals.photoUri),
                                    contentDescription = "Food image",
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(80.dp)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.img),
                                    contentDescription = "Placeholder image",
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(80.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = cals.foodName)
                                Text(text = cals.foodCategory)
                                Text(text = "${cals.calories.dp.value}")
                                Text(text = cals.dateCreated)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Button to Add New Food Item
        Button(
            onClick = {
                val intentToAddItem = Intent(context, AddFoodItem::class.java)
                context.startActivity(intentToAddItem)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Add New Food Item")
        }

        // Button to Add Nutritious Food
        Button(
            onClick = {
                val intentToAddNutritiousFood = Intent(context, AddNutritiousFoodActivity::class.java)
                context.startActivity(intentToAddNutritiousFood)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)  // Adjusting the padding to place it above the "Add New Food Item" button
        ) {
            Text("Add Nutritious Food")
        }
    }
}
