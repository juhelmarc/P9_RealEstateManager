package com.openclassrooms.realestatemanager.data

import com.openclassrooms.realestatemanager.R

enum class PoiList(val poiId: Int) {
    SCHOOL(R.string.poi_school),
    TRANSPORTATION(R.string.poi_transportation_hubs),
    SHOPPING(R.string.poi_shopping_centers),
    GROCERY_STORES(R.string.poi_grocery_stores),
    RESTAURANTS(R.string.poi_restaurants),
    CAFES(R.string.poi_cafes),
    GREEN_SPACES(R.string.poi_green_spaces),
    ENTERTAINMENT_VENUES(R.string.poi_entertainment_venues),
    CULTURAL_ATTRACTIONS(R.string.poi_cultural_attractions),
    SPORTS_FACILITIES(R.string.poi_sports_facilities),
    MEDICAL_FACILITIES(R.string.poi_medical_facilities)
}