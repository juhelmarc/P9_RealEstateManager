package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.dao.PropertyPriceSurface
import com.openclassrooms.realestatemanager.data.models.CurrentFilterValue
import com.openclassrooms.realestatemanager.ui.filter.FilterFeatureViewState

class FilterFixtures {

    object FilterFeatureUtils {
        fun create() : FilterFeatureViewState {
           return FilterFeatureViewState(
                minPrice = MinMaxPriceSurfaceUtils.create().minPrice,
                maxPrice = MinMaxPriceSurfaceUtils.create().maxPrice,
                minSurface = MinMaxPriceSurfaceUtils.create().minSurface,
                maxSurface = MinMaxPriceSurfaceUtils.create().maxSurface,
                listAgent = AgentFixtures.AgentListUtils.create(),
                listOfType = ListTypeUtils.create(),
                listOfTown = ListTownUtils.create(),
                minPriceSelected = CurrentFilterValueUtils.create().minPriceSelected,
                maxPriceSelected = CurrentFilterValueUtils.create().maxPriceSelected,
                minSurfaceSelected = CurrentFilterValueUtils.create().minSurfaceSelected,
                maxSurfaceSelected = CurrentFilterValueUtils.create().minSurfaceSelected,
                agentNameSelected = CurrentFilterValueUtils.create().agentNameSelected,
                listOfTypeSelected = CurrentFilterValueUtils.create().listOfTypeSelected,
                listOfTownSelected = CurrentFilterValueUtils.create().listOfTownSelected
            )
        }
    }
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

    object MinMaxPriceSurfaceUtils {
        fun create() : PropertyPriceSurface {
            return PropertyPriceSurface(
                    minPrice = 1,
                    maxPrice = 1,
                    minSurface = 1,
                    maxSurface = 1
            )
        }
    }

    object ListTypeUtils {
        fun create() : List<String> {
            return listOf("flat", "house")
        }
    }

    object ListTownUtils {
        fun create() : List<String> {
            return listOf("Grenoble", "Paris")
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