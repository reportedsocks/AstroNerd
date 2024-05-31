package com.antsyferov.impl.mappers

import com.antsyferov.impl.local.AsteroidEntity
import com.antsyferov.impl.network.Asteroid
import com.antsyferov.impl.network.AsteroidsResponse
import java.text.SimpleDateFormat
import java.util.Date

private val formatter = SimpleDateFormat("yyyy-MM-dd")

fun AsteroidsResponse.toEntities(): List<AsteroidEntity> {
    val entities = mutableListOf<AsteroidEntity>()
    asteroids.forEach { key, values ->
        val timestamp = formatter.parse(key)?.time ?: Date().time
        entities.addAll(values.map { it.toEntity(timestamp) })
    }
    return entities
}

fun Asteroid.toEntity(date: Long): AsteroidEntity {
    val diameter = AsteroidEntity.EstimatedDiameterEntity(
        kilometers = AsteroidEntity.EstimatedDiameterEntity.DiameterEntity(
            estimatedDiameter.kilometers.min, estimatedDiameter.kilometers.max
        ),
        miles = AsteroidEntity.EstimatedDiameterEntity.DiameterEntity(
            estimatedDiameter.miles.min, estimatedDiameter.miles.max
        ),
        meters = AsteroidEntity.EstimatedDiameterEntity.DiameterEntity(
            estimatedDiameter.meters.min, estimatedDiameter.meters.max
        ),
        feet = AsteroidEntity.EstimatedDiameterEntity.DiameterEntity(
            estimatedDiameter.feet.min, estimatedDiameter.feet.max
        )
    )
    val closeApproachData = closeApproachData.firstOrNull()?.let {
        AsteroidEntity.CloseApproachEntity(
            closeApproachDate = it.closeApproachDate,
            closeApproachDateFull = it.closeApproachDateFull,
            epochDateCloseApproach = it.epochDateCloseApproach,
            orbitingBody = it.orbitingBody,
            relativeVelocity = AsteroidEntity.CloseApproachEntity.RelativeVelocityEntity(
                kilometersPerSecond = it.relativeVelocity.kilometersPerSecond,
                kilometersPerHour = it.relativeVelocity.kilometersPerHour,
                milesPerHour = it.relativeVelocity.milesPerHour
            ),
            missDistance = AsteroidEntity.CloseApproachEntity.MissDistanceEntity(
                astronomical = it.missDistance.astronomical,
                lunar = it.missDistance.lunar,
                kilometers = it.missDistance.kilometers,
                miles = it.missDistance.miles
            )
        )
    }
    return AsteroidEntity(
        id = id,
        date = date,
        linkSelf = links.self,
        neoReferenceId = neoReferenceId,
        name = name,
        nasaJplUrl = nasaJplUrl,
        absoluteMagnitudeH = absoluteMagnitudeH,
        estimatedDiameter = diameter,
        isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid,
        isSentryObject = isSentryObject,
        closeApproachData = closeApproachData
    )
}