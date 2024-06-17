package com.antsyferov.astronerd.ui.panes.visualization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.sceneview.math.Position
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.*



data class OrbitalData(

    val mercury: Position,
    val venus: Position,
    val earth: Position,
    val mars: Position,
    val jupiter: Position,
    val saturn: Position,
    val uran: Position,
    val neptune: Position,
) {
    companion object {
        fun fromTriple(orbit: Triple<Double, Double, Double>) : Position {
            return Position(x = orbit.first.toFloat() * 10f, y = orbit.second.toFloat()* 10f, z = orbit.third.toFloat()* 10f)
        }
    }
}

const val CUBE_SIZE = 1.0  // Size of the cube in meters

val j2000Epoch = LocalDateTime.of(2000, 1, 1,0,0)
fun hoursSinceJ2000(date: LocalDateTime): Double {
    return ChronoUnit.HOURS.between(j2000Epoch, date).toDouble()
}

data class PlanetData(
    val index: Int,
    val orbitalPeriod: Double,
    val initialOffset: Double,
    val distance: Double
)

fun initializePlanetData(index: Int, orbitalPeriod: Double, initialOffset: Double): PlanetData {
    // Calculate the distance from the Sun for visualization purposes
    val distance = (index + 2) * (CUBE_SIZE / 9)
    return PlanetData(index, orbitalPeriod, initialOffset, distance)
}

fun calculatePlanetPosition(date: LocalDateTime, planetData: PlanetData): Triple<Double, Double, Double> {
    val hoursSinceEpoch = hoursSinceJ2000(date)

    // Calculate angular position (in radians) for the planet
    var angle = 2 * PI * (hoursSinceEpoch % planetData.orbitalPeriod) / planetData.orbitalPeriod
    angle += planetData.initialOffset

    // Calculate x and y positions assuming circular orbit in xy-plane
    val x = planetData.distance * cos(angle)
    val y = planetData.distance * sin(angle)

    // For simplicity, assume z = 0 (planets in xy-plane)
    return Triple(x, y, 0.0)
}

@Composable
fun rememberOrbitalPositions(date: LocalDateTime): OrbitalData {

    val planetData = remember {
        val planets = listOf(
            88.0 * 24 to 40.0,
            224.0 * 24 to -20.0,
            365.2 * 24 to 60.0 ,
            687.0 * 24 to 0.0,
            4331.0 * 24 to -40.0,
            10747.0 * 24 to 60.0,
            30589.0 * 24 to 0.0,
            59800.0 * 24 to 0.0,
        )
        planets.mapIndexed { index, pair -> initializePlanetData(index, pair.first, pair.second) }
    }

    return remember(date) {
        val positions = planetData.map { calculatePlanetPosition(date, it) }
        OrbitalData(
            mercury = positions[0].let { OrbitalData.fromTriple(it) },
            venus = positions[1].let { OrbitalData.fromTriple(it) },
            earth = positions[2].let { OrbitalData.fromTriple(it) },
            mars = positions[3].let { OrbitalData.fromTriple(it) },
            jupiter = positions[4].let { OrbitalData.fromTriple(it) },
            saturn = positions[5].let { OrbitalData.fromTriple(it) },
            uran = positions[6].let { OrbitalData.fromTriple(it) },
            neptune = positions[7].let { OrbitalData.fromTriple(it) }
        )
    }

}
