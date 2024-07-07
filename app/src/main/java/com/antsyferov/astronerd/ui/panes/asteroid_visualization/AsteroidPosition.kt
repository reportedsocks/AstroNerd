package com.antsyferov.astronerd.ui.panes.asteroid_visualization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.antsyferov.impl.network.OrbitalData
import io.github.sceneview.math.Position
import io.github.sceneview.math.toFloat3
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun rememberAsteroidPosition(orbitalData: OrbitalData):Position {
    return remember(orbitalData) {
        calculateAsteroidPosition(orbitalData)
    }
}

/*
fun calculateAsteroidPosition(orbitalData: OrbitalData): Position {
    val a = orbitalData.semiMajorAxis // Semi-major axis
    val e = orbitalData.eccentricity // Eccentricity
    val i = Math.toRadians(orbitalData.inclination) // Inclination in radians
    val omega = Math.toRadians(orbitalData.perihelionArgument) // Argument of perihelion
    val Omega = Math.toRadians(orbitalData.ascendingNodeLongitude) // Longitude of ascending node
    val M = Math.toRadians(orbitalData.meanAnomaly) // Mean anomaly

    // Solve Kepler's Equation for Eccentric Anomaly (E)
    var E = M
    for (j in 0..100) {
        val deltaE = (M + e * sin(E) - E) / (1 - e * cos(E))
        E += deltaE
        if (abs(deltaE) < 1e-6) break
    }

    // True Anomaly (ν)
    val nu = 2 * atan2(sqrt(1 + e) * sin(E / 2), sqrt(1 - e) * cos(E / 2))

    // Distance (r) from the central body
    val r = a * (1 - e * cos(E))

    // Heliocentric coordinates in the orbital plane
    val xOrbital = r * cos(nu)
    val yOrbital = 0.0 // Keep y-coordinate as 0 to stay in the orbital plane
    val zOrbital = r * sin(nu)

    // Rotate to the ecliptic plane
    val xEcliptic = xOrbital * (cos(Omega) * cos(omega) - sin(Omega) * sin(omega) * cos(i)) -
            zOrbital * (cos(Omega) * sin(omega) + sin(Omega) * cos(omega) * cos(i))
    val yEcliptic = xOrbital * (sin(Omega) * cos(omega) + cos(Omega) * sin(omega) * cos(i)) +
            zOrbital * (cos(Omega) * cos(omega) - sin(Omega) * sin(omega) * cos(i))
    val zEcliptic = xOrbital * sin(omega) * sin(i) + zOrbital * cos(omega) * sin(i)

    // No scaling needed, directly use ecliptic coordinates
    val x = xEcliptic.toFloat()
    val y = yEcliptic.toFloat()
    val z = zEcliptic.toFloat()

    return Position(x = x.toFloat(), y = y.toFloat(), z = z.toFloat())
}*/


fun calculateAsteroidPosition(orbitalData: OrbitalData): Position {
    return generateOrbitPoints(orbitalData)[0].scaled(5f).toFloat3()
    //return Position(x = x.toFloat(), y = y.toFloat(), z = z.toFloat())
}
