package com.example.calorietracker

import CalorieModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Database
import coil.compose.rememberAsyncImagePainter
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.example.kotlin_compose_database.CalorieData
import com.example.kotlin_compose_database.CaloriesDao
import com.example.kotlin_compose_database.DatabaseProvider
import java.time.LocalDate

class DailyIntake : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalorieTrackerTheme {
                val db = DatabaseProvider.getDatabase(this)
                val calModel:CalorieModel=viewModel()
                mainScreen(calModel = calModel, caloriesDao = db.caloriesDao())
            }
        }
    }
}


@Composable
fun mainScreen(calModel: CalorieModel, caloriesDao: CaloriesDao) {
    val context = LocalContext.current
    val calorieData = remember { mutableStateListOf<CalorieData>() }

    LaunchedEffect(Unit) {
        calorieData.clear()
        calorieData.addAll(caloriesDao.getDailyCals(date = LocalDate.now().toString()))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(14.dp))

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ){
            Text(
                text = "My Daily Intake",
                fontSize = 50.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.weight(1f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
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
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
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
            calColumn(calView = calModel, calData =calorieData )
            predifinedCal()
        }
    }
}


fun totalCalories(calView:CalorieModel,calData:List<CalorieData>):Double{
    var totalCals = 0.0
    for(cals in calData){
        totalCals+=cals.calories
    }
    return totalCals


}


@Composable
fun calColumn(calView:CalorieModel,calData:List<CalorieData>){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        val cals = totalCalories(calView = calView, calData = calData).dp.value.toString()

        Text(
            text = "Total Calories: $cals",
            fontSize = 30.sp
        )
    }

}


@Composable
fun predifinedCal(){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        Text(
            text = "Daily Calorie Goal: 2000.00",
            fontSize = 30.sp
        )
    }
}







