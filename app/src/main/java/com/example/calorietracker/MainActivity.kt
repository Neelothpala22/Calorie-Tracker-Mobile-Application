package com.example.calorietracker

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.example.kotlin_compose_database.CalorieData
import com.example.kotlin_compose_database.CaloriesDao
import com.example.kotlin_compose_database.DatabaseProvider
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalorieTrackerTheme {
                val db = DatabaseProvider.getDatabase(this)
                homePage()
                FirebaseApp.initializeApp(this)
            }
        }

    }
}




@Composable
fun homePage(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        imageBox()
        viewLists()
        viewCalories()
    }

}

@Composable
private fun imageBox() {
    Image(
        painter = painterResource(id = R.drawable.cal),
        contentDescription = null
    )
}

@Composable
private fun viewLists(){
    val context = LocalContext.current
    Button(
        onClick = {
            val listToView = Intent(context,FoodList::class.java)
            context.startActivity(listToView)
        }
    ) {
        Text(text = "View Food Lists")
    }
}

@Composable
private fun viewCalories(){
    val context = LocalContext.current
    Button(
        onClick = {
            val listToView = Intent(context,DailyIntake::class.java)
            context.startActivity(listToView)
        }
    ) {
        Text(text = "View Daily Calorie Intake")
    }
}

