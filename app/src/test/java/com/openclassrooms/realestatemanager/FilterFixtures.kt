package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.models.CurrentFilterValue

class FilterFixtures {

    object CurrentFilterValueUtils {
        fun create() : CurrentFilterValue {
            return CurrentFilterValue(
                minPriceSelected = 1,
                maxPriceSelected = 1,
                minSurfaceSelected = 1,
                maxSurfaceSelected = 1,
                agentNameSelected = "Test",
                listOfTypeSelected = listOf("test"),
                listOfTownSelected = listOf("test")
            )
        }
    }

    object DefaultFilterValueUtils {
        fun create() : CurrentFilterValue {
            return CurrentFilterValue(
                minPriceSelected = null,
                maxPriceSelected = null,
                minSurfaceSelected = null,
                maxSurfaceSelected = null,
                agentNameSelected = "",
                listOfTypeSelected = listOf(),
                listOfTownSelected = listOf()
            )
        }
    }

    object CurrentFilterQueryUtils {
        fun create() : String {
            return ""
        }
    }

    object DefaultFilterQueryUtils {
        fun create() : String {
            return "SELECT * FROM PropertyEntity ORDER BY id ASC"
        }
    }
}