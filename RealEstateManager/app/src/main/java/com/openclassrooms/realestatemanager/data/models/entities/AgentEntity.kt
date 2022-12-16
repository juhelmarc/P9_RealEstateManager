package com.openclassrooms.realestatemanager.data.models.entities
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;


@Entity
class AgentEntity (
    @PrimaryKey(autoGenerate = true) val agentId: Long = 0L,
    val name: String,
) {
    override fun toString(): String = name
}
