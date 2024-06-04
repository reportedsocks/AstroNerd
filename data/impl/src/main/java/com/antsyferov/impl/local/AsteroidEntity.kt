package com.antsyferov.impl.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.antsyferov.impl.network.CloseApproachData
import com.antsyferov.impl.network.EstimatedDiameter
import com.antsyferov.impl.network.MissDistance
import com.antsyferov.impl.network.RelativeVelocity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "asteroids")
data class AsteroidEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo("date")
    val date: Long,
    @ColumnInfo("link_self")
    val linkSelf: String?,
    @ColumnInfo("neo_reference_id")
    val neoReferenceId: String = "",
    @ColumnInfo("name")
    val name: String = "",
    @ColumnInfo("nasa_jpl_url")
    val nasaJplUrl: String = "",
    @ColumnInfo("absolute_magnitude_h")
    val absoluteMagnitudeH: Double = 0.0,
    @Embedded
    val estimatedDiameter: EstimatedDiameterEntity,
    @ColumnInfo("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardousAsteroid: Boolean = false,
    @Embedded
    val closeApproachData: CloseApproachEntity?,
    @ColumnInfo("is_sentry_object")
    val isSentryObject: Boolean = false,
    @ColumnInfo("is_favourite")
    val isFavourite: Boolean = false
) {
    data class CloseApproachEntity(
        @ColumnInfo("close_approach_date")
        val closeApproachDate: String = "",
        @ColumnInfo("close_approach_date_full")
        val closeApproachDateFull: String = "",
        @ColumnInfo("epoch_date_close_approach")
        val epochDateCloseApproach: Long = 0L,
        @Embedded
        val relativeVelocity: RelativeVelocityEntity,
        @Embedded
        val missDistance: MissDistanceEntity,
        @ColumnInfo("orbiting_body")
        val orbitingBody: String = ""
    ) {

        data class RelativeVelocityEntity(
            @ColumnInfo("kilometers_per_second")
            val kilometersPerSecond: String = "",
            @ColumnInfo("kilometers_per_hour")
            val kilometersPerHour: String = "",
            @ColumnInfo("miles_per_hour")
            val milesPerHour: String = ""
        )

        data class MissDistanceEntity(
            @ColumnInfo("astronomical")
            val astronomical: String = "",
            @ColumnInfo("lunar")
            val lunar: String = "",
            @ColumnInfo("kilometers")
            val kilometers: String = "",
            @ColumnInfo("miles")
            val miles: String = ""
        )
    }

    data class EstimatedDiameterEntity(
        @Embedded("km_")
        val kilometers: DiameterEntity,
        @Embedded("mt_")
        val meters: DiameterEntity,
        @Embedded("ml_")
        val miles: DiameterEntity,
        @Embedded("ft_")
        val feet: DiameterEntity
    ) {

        data class DiameterEntity(
            @ColumnInfo("estimated_diameter_min")
            val min: Double = 0.0,
            @ColumnInfo("estimated_diameter_max")
            val max: Double = 0.0
        )
    }


}
