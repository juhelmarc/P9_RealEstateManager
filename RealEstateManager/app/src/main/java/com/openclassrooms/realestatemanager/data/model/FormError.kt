package com.openclassrooms.realestatemanager.data.model

data class FormError(
    val agentError: String?,
    val typeError: String?,
    val postalCodeError: String?,
    val districtError: String?,
    val addressError: String?,
    val townError: String?,
    val entryDateError: String?,
    val dateOfSaleError: String?
        )

