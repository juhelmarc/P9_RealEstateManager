package com.openclassrooms.realestatemanager.ui.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.data.repositories.CurrentPropertyRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrentPropertyRepositoryTests {

    private val currentPropertyRepository = CurrentPropertyRepository()

    @get:Rule
    var rule : TestRule = InstantTaskExecutorRule()

    @Test
    fun getCurrentIdLiveData() {
        val expectedId: Long = 1

        currentPropertyRepository.setCurrentId(expectedId)
        val currentIdValue: LiveData<Long> = currentPropertyRepository.currentIdLiveData

        Assert.assertSame(expectedId, currentIdValue.value)
    }
}