package com.antsyferov.impl.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AsteroidsResponse(
    @SerialName("links")
    val links: Links = Links(),
    @SerialName("element_count")
    val elementCount: Int = 0,
    @SerialName("near_earth_objects")
    val asteroids: Map<String, List<Asteroid>> = emptyMap(),
)

@Serializable
data class Links(
    @SerialName("next")
    val next: String? = null,
    @SerialName("previous")
    val previous: String? = null,
    @SerialName("self")
    val self: String? = null
)

@Serializable
data class Asteroid(
    @SerialName("links")
    val links: Links = Links(),
    @SerialName("id")
    val id: String = "",
    @SerialName("neo_reference_id")
    val neoReferenceId: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("nasa_jpl_url")
    val nasaJplUrl: String = "",
    @SerialName("absolute_magnitude_h")
    val absoluteMagnitudeH: Double = 0.0,
    @SerialName("estimated_diameter")
    val estimatedDiameter: EstimatedDiameter,
    @SerialName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardousAsteroid: Boolean = false,
    @SerialName("close_approach_data")
    val closeApproachData: List<CloseApproachData>,
    @SerialName("is_sentry_object")
    val isSentryObject: Boolean = false
)

@Serializable
data class EstimatedDiameter(
    @SerialName("kilometers")
    val kilometers: Diameter = Diameter(),
    @SerialName("meters")
    val meters: Diameter = Diameter(),
    @SerialName("miles")
    val miles: Diameter = Diameter(),
    @SerialName("feet")
    val feet: Diameter = Diameter()
)

@Serializable
data class Diameter(
    @SerialName("estimated_diameter_min")
    val min: Double = 0.0,
    @SerialName("estimated_diameter_max")
    val max: Double = 0.0
)

@Serializable
data class CloseApproachData(
    @SerialName("close_approach_date")
    val closeApproachDate: String = "",
    @SerialName("close_approach_date_full")
    val closeApproachDateFull: String = "",
    @SerialName("epoch_date_close_approach")
    val epochDateCloseApproach: Long = 0L,
    @SerialName("relative_velocity")
    val relativeVelocity: RelativeVelocity = RelativeVelocity(),
    @SerialName("miss_distance")
    val missDistance: MissDistance = MissDistance(),
    @SerialName("orbiting_body")
    val orbitingBody: String = ""
)

@Serializable
data class RelativeVelocity(
    @SerialName("kilometers_per_second")
    val kilometersPerSecond: String = "",
    @SerialName("kilometers_per_hour")
    val kilometersPerHour: String = "",
    @SerialName("miles_per_hour")
    val milesPerHour: String = ""
)

@Serializable
data class MissDistance(
    @SerialName("astronomical")
    val astronomical: String = "",
    @SerialName("lunar")
    val lunar: String = "",
    @SerialName("kilometers")
    val kilometers: String = "",
    @SerialName("miles")
    val miles: String = ""
)
