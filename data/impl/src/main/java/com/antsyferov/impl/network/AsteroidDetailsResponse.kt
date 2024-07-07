package com.antsyferov.impl.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AsteroidDetailsResponse(
    @SerialName("orbital_data")
    val orbitalData: OrbitalData
)

@Serializable
data class OrbitalData(
    @SerialName("orbit_id")
    val orbitId: String,
    @SerialName("orbit_determination_date")
    val orbitDeterminationDate: String,
    @SerialName("first_observation_date")
    val firstObservationDate: String,
    @SerialName("last_observation_date")
    val lastObservationDate: String,
    @SerialName("data_arc_in_days")
    val dataArcInDays: Int,
    @SerialName("observations_used")
    val observationsUsed: Int,
    @SerialName("orbit_uncertainty")
    val orbitUncertainty: String,
    @SerialName("minimum_orbit_intersection")
    val minimumOrbitIntersection: Double,
    @SerialName("jupiter_tisserand_invariant")
    val jupiterTisserandInvariant: Double,
    @SerialName("epoch_osculation")
    val epochOsculation: Double,
    @SerialName("eccentricity")
    val eccentricity: Double,
    @SerialName("semi_major_axis")
    val semiMajorAxis: Double,
    @SerialName("inclination")
    val inclination: Double,
    @SerialName("ascending_node_longitude")
    val ascendingNodeLongitude: Double,
    @SerialName("orbital_period")
    val orbitalPeriod: Double,
    @SerialName("perihelion_distance")
    val perihelionDistance: Double,
    @SerialName("perihelion_argument")
    val perihelionArgument: Double,
    @SerialName("aphelion_distance")
    val aphelionDistance: Double,
    @SerialName("perihelion_time")
    val perihelionTime: Double,
    @SerialName("mean_anomaly")
    val meanAnomaly: Double,
    @SerialName("mean_motion")
    val meanMotion: Double,
    @SerialName("equinox")
    val equinox: String,
    @SerialName("orbit_class")
    val orbitClass: OrbitClass
)

@Serializable
data class OrbitClass(
    @SerialName("orbit_class_type")
    val orbitClassType: String,
    @SerialName("orbit_class_description")
    val orbitClassDescription: String,
    @SerialName("orbit_class_range")
    val orbitClassRange: String
)