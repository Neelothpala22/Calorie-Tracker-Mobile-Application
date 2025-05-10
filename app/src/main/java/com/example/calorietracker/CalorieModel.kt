import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.RetrofitInstance

import com.example.kotlin_compose_database.CalorieData
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CalorieModel: ViewModel(){
    var foodName by  mutableStateOf("")
    var createDate by  mutableStateOf("")
    var expanded by mutableStateOf(false)
    val mealChoices = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    var selected by mutableStateOf(mealChoices[0])
    var photoUri by mutableStateOf<Uri?>(null)



    private val _calories = mutableStateOf<Double?>(null)
    var calories: State<Double?> = _calories

    fun fetchCalories(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getNutritionInfo(query)
                _calories.value = response.items.firstOrNull()?.calories?.toDouble()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun setCalories(value: Double?) {
        _calories.value = value
    }

}