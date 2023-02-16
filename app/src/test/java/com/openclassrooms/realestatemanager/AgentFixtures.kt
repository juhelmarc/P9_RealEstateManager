package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.data.models.entities.AgentEntity

class AgentFixtures {

    object AgentListUtils {
        fun create() : List<AgentEntity> {
            return listOf(
                AgentEntity(11L, "Agent1"),
                AgentEntity(22L, "Agent2"),
                AgentEntity(33L, "Agent3")
            )
        }
    }

}