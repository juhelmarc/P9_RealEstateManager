package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.models.entities.PropertyPictureEntity

class PropertyPictureFixtures {

    object ListPictureUtils {
        fun create() : List<PropertyPictureEntity> {
            return listOf(
                PictureUtils1.create(),
                PictureUtils2.create()
            )
        }
    }
    object PictureUtils1 {
        fun create() : PropertyPictureEntity {
            return PropertyPictureEntity(
                id = 1L,
                propertyId = PictureForeignKeyUtils.create(),
                url = "",
                title = ""
            )
        }
    }

    object PictureUtils2 {
        fun create() : PropertyPictureEntity {
            return PropertyPictureEntity(
                id = 2L,
                propertyId = PictureForeignKeyUtils.create(),
                url = "",
                title = ""
            )
        }
    }

    object PictureForeignKeyUtils {
        fun create() : Long {
            return  1L
        }

    }
}