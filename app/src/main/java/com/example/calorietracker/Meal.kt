package com.example.calorietracker

class Meal {
    var id: String? = null
    var name: String? = null
    var type: String? = null
    var calories: String? = null
    var photoUrl: String? = null

    constructor()

    constructor(name: String?, type: String?, calories: String?, photoUrl: String?) {
        this.id = id // Ensure the id is passed when creating a Meal object
        this.name = name
        this.type = type
        this.calories = calories
        this.photoUrl = photoUrl
    }
}