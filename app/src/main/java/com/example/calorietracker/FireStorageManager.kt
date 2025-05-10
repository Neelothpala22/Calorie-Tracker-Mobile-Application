package com.example.calorietracker

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FirebaseStorageManager {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference

    /**
     * Uploads an image to Firebase Storage and returns the download URL.
     *
     * @param context The application context.
     * @param imageUri The URI of the image to upload.
     * @return The download URL as a String if successful, null otherwise.
     */
    suspend fun uploadImage(context: Context, imageUri: Uri): String? {
        return try {
            // Generate a unique filename using UUID
            val fileName = "images/${UUID.randomUUID()}.jpg"
            val fileRef = storageRef.child(fileName)

            // Upload the file
            fileRef.putFile(imageUri).await()

            // Get the download URL
            val downloadUrl = fileRef.downloadUrl.await().toString()
            downloadUrl
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Error uploading image", e)

            // Manual logging to a local file if API logging fails
            logToFile(context, "Error uploading image: ${e.localizedMessage}")

            null
        }
    }

    /**
     * Logs errors manually to a file stored locally on the device.
     *
     * @param context The application context.
     * @param errorMessage The error message to log.
     */
    private fun logToFile(context: Context, errorMessage: String) {
        val logFileName = "firebase_upload_errors.txt"
        val logFile = File(context.filesDir, logFileName)

        try {
            // Append the error message with a timestamp
            val timeStamp = Calendar.getInstance().time.toString()
            val logEntry = "$timeStamp: $errorMessage\n"

            // Write to the log file
            FileOutputStream(logFile, true).use { outputStream ->
                outputStream.write(logEntry.toByteArray())
            }

            Log.d("FirebaseStorage", "Error logged to file: ${logFile.absolutePath}")
        } catch (fileException: Exception) {
            Log.e("FirebaseStorage", "Failed to log error to file", fileException)
        }
    }
}
