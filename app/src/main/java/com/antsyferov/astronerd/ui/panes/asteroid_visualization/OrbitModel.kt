package com.antsyferov.astronerd.ui.panes.asteroid_visualization

import android.graphics.Color
import com.antsyferov.impl.network.OrbitClass
import com.antsyferov.impl.network.OrbitalData
import com.google.android.filament.Engine
import com.google.android.filament.MaterialInstance
import io.github.sceneview.collision.Vector3
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.math.toFloat3
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.node.Node
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


fun drawOrbitNode(
    points: List<Vector3>,
    engine: Engine,
    materialLoader: MaterialLoader,
    orbitNode: Node,
    color: androidx.compose.ui.graphics.Color
) {
    val material =
        materialLoader.createColorInstance(color)

    //val points = generateOrbitPoints(orbitalData)

    for (i in 0 until points.size - 1 step 3) {
        val line = addLineBetweenPoints(points[i], points[i + 1], material, engine)
        orbitNode.addChildNode(line)
    }
}

fun generateMoonPoints(semiMajorAxis: Double): List<Vector3> {
    return generateOrbitPoints(
        OrbitalData(
            orbitId = "",
            orbitDeterminationDate = "",
            firstObservationDate = "",
            lastObservationDate = "",
            dataArcInDays = 0,
            observationsUsed = 0,
            orbitUncertainty = "",
            minimumOrbitIntersection = .0,
            jupiterTisserandInvariant = .0,
            epochOsculation = .0,
            eccentricity = 0.0549, // Eccentricity of the Moon's orbit
            semiMajorAxis = semiMajorAxis,//384400.0 / 149597870.7, // Semi-major axis in AU (Earth-Moon average distance in km / AU in km)
            inclination = 5.145, // Inclination in degrees
            ascendingNodeLongitude = 125.08, // Longitude of the ascending node in degrees
            orbitalPeriod = 27.322, // Orbital period in days
            perihelionDistance = 363300.0 / 149597870.7, // Perihelion distance in AU
            perihelionArgument = 318.15, // Argument of perihelion in degrees
            aphelionDistance = 405500.0 / 149597870.7, // Aphelion distance in AU
            perihelionTime = .0,
            meanAnomaly = 0.0, // Mean anomaly at epoch
            meanMotion = 0.0366, // Mean motion in degrees/day
            equinox = "",
            orbitClass = OrbitClass(
                orbitClassType = "",
                orbitClassDescription = "",
                orbitClassRange = ""
            )
        )
    )
}

fun generateOrbitPoints(orbitalData: OrbitalData): List<Vector3> {
    val points = mutableListOf<Vector3>()
    val segments = 365
    val semiMinorAxis =
        orbitalData.semiMajorAxis * sqrt(1 - orbitalData.eccentricity * orbitalData.eccentricity)

    for (i in 0 until segments + 1) {
        val angle = 2 * Math.PI * i / segments
        val x = orbitalData.semiMajorAxis * cos(angle)
        val z = semiMinorAxis * sin(angle)
        val point = Vector3(x.toFloat(), 0f, z.toFloat())
        points.add(applyOrbitalRotations(point, orbitalData))
    }

    return points
}

private fun applyOrbitalRotations(point: Vector3, orbitalData: OrbitalData): Vector3 {
    val iRad = Math.toRadians(orbitalData.inclination.toDouble())
    val longRad = Math.toRadians(orbitalData.ascendingNodeLongitude.toDouble())
    val argRad = Math.toRadians(orbitalData.perihelionArgument.toDouble())

    val cosI = cos(iRad).toFloat()
    val sinI = sin(iRad).toFloat()
    val cosLong = cos(longRad).toFloat()
    val sinLong = sin(longRad).toFloat()
    val cosArg = cos(argRad).toFloat()
    val sinArg = sin(argRad).toFloat()

    val x = point.x * cosLong - point.z * sinLong
    val z = point.x * sinLong + point.z * cosLong
    val y = point.y * cosI - z * sinI
    val finalZ = point.y * sinI + z * cosI

    return Vector3(x * cosArg - finalZ * sinArg, y, x * sinArg + finalZ * cosArg)
}

private fun addLineBetweenPoints(
    start: Vector3,
    end: Vector3,
    material: MaterialInstance,
    engine: Engine,
): Node {


    val direction = Vector3.subtract(end, start).normalized()
    val distance = Vector3.subtract(end, start).length()

    // Calculate midpoint between start and end points
    val midpoint = Vector3.add(start, end).scaled(0.5f)

    // Calculate rotation to align cylinder between start and end points
    val rotationAxis = Vector3.cross(Vector3.up(), direction).normalized()
    val rotationAngle = Vector3.angleBetweenVectors(Vector3.up(), direction)

    return CylinderNode(
        engine = engine,
        height = distance,
        radius = 0.005f,
        materialInstance = material,
    ).apply {
        position = midpoint.toFloat3()
        quaternion = dev.romainguy.kotlin.math.Quaternion.fromAxisAngle(
            rotationAxis.toFloat3(),
            rotationAngle
        )
    }

}

