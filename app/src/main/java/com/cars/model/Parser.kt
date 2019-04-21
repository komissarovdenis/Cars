package com.cars.model

import org.json.JSONObject

// Manual parsing because in task was requested not to use a lot of third-party libs
// FYI: GSON lib can be used for auto-parsing fields
object Parser {

    fun parseManufacturer(json: JSONObject): List<Manufacturer> {
        val result = ArrayList<Manufacturer>()
        json.keys().forEach { key ->
            result.add(Manufacturer(key, json.getString(key)))
        }
        return result
    }

    fun parseModel(json: JSONObject): List<Model> {
        val result = ArrayList<Model>()
        json.keys().forEach { key ->
            result.add(Model(json.getString(key)))
        }
        return result
    }

    fun parseYear(json: JSONObject): List<Year> {
        val result = ArrayList<Year>()
        json.keys().forEach { key ->
            result.add(Year(json.getString(key)))
        }
        return result
    }
}