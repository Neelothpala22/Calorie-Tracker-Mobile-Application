//package com.example.calorieapp
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat.startActivityForResult
//import java.io.ByteArrayOutputStream
//
//class MainActivity2 : AppCompatActivity() {
//    private var mealPhoto: ImageView? = null
//    private var capturePhoto: Button? = null
//    private var chooseFromGallery: Button? = null
//    private var addMeal: Button? = null
//
//    private var photoUri: Uri? = null
//
//    protected fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        mealPhoto = findViewById(R.id.imageViewMealPhoto)
//        capturePhoto = findViewById(R.id.buttonCapturePhoto)
//        chooseFromGallery = findViewById(R.id.buttonChooseFromGallery)
//        addMeal = findViewById(R.id.buttonAddMeal)
//
//        capturePhoto!!.setOnClickListener { view: View? -> openCamera() }
//        chooseFromGallery!!.setOnClickListener { view: View? -> openGallery() }
//        addMeal!!.setOnClickListener { view: View? -> uploadMealData() }
//
//        // Restore photo URI if available
//        if (savedInstanceState != null) {
//            photoUri = savedInstanceState.getParcelable("photoUri")
//            if (photoUri != null) {
//                mealPhoto!!.setImageURI(photoUri)
//            }
//        }
//
//        // Start listening for meal updates
//        retrieveAllMeals()
//    }
//
//    private fun openCamera() {
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, REQUEST_CAMERA)
//    }
//
//    private fun openGallery() {
//        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(galleryIntent, REQUEST_GALLERY)
//    }
//
//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == REQUEST_CAMERA) {
//                val photo = data.extras!!["data"] as Bitmap?
//                mealPhoto!!.setImageBitmap(photo)
//
//                // Convert Bitmap to Uri
//                val bytes = ByteArrayOutputStream()
//                photo!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                val path = MediaStore.Images.Media.insertImage(
//                    getContentResolver(),
//                    photo,
//                    "MealPhoto",
//                    null
//                )
//                photoUri = Uri.parse(path)
//            } else if (requestCode == REQUEST_GALLERY) {
//                photoUri = data.data
//                mealPhoto!!.setImageURI(photoUri)
//            }
//        }
//    }
//
//    private fun uploadMealData() {
//        val name = mealName!!.text.toString()
//        val type = mealType!!.text.toString()
//        val cal = calories!!.text.toString()
//
//        if (name.isEmpty() || type.isEmpty() || cal.isEmpty() || photoUri == null) {
//            Toast.makeText(this, "Please fill all fields and add a photo", Toast.LENGTH_SHORT)
//                .show()
//            return
//        }
//
//        // Create a new meal object
//        val mealId = System.currentTimeMillis().toString() // Unique ID for the meal
//        val meal: Meal = Meal(name, type, cal, photoUri.toString())
//        // Save the meal using FirestoreHelper
//        FirestoreHelper.saveMeal(mealId, meal)
//
//        // Display a confirmation message
//        Toast.makeText(this, "Meal added successfully!", Toast.LENGTH_SHORT).show()
//
//        // Redirect to MealListActivity after adding the meal
//        val intent: Intent = Intent(
//            this@MainActivity2,
//            MealListActivity::class.java
//        )
//        startActivity(intent)
//
//        // Optionally, finish the current activity to remove it from the back stack
//        finish()
//    }
//
//    // Retrieve all meals and listen for updates
//    private fun retrieveAllMeals() {
//        FirestoreHelper.getAllMeals(object : MealsCallback() {
//            fun onMealsReceived(meals: List<Meal?>) {
//                // Update your UI here with the list of meals
//                Log.d(TAG, "Received meals: $meals")
//                // TODO: Update your RecyclerView or ListView to show the meals
//            }
//        })
//    }
//
//    protected fun onDestroy() {
//        super.onDestroy()
//        // Stop listening for updates
//        FirestoreHelper.removeMealListener()
//    }
//
//    protected fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        // Save the photo URI to the instance state
//        outState.putParcelable("photoUri", photoUri)
//    }
//
//    companion object {
//        private const val REQUEST_CAMERA = 100
//        private const val REQUEST_GALLERY = 101
//        private const val TAG = "MainActivity"
//    }
//}