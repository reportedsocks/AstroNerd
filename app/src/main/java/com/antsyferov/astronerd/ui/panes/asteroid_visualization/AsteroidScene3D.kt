package com.antsyferov.astronerd.ui.panes.asteroid_visualization

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.antsyferov.astronerd.ui.panes.visualization.rememberAsteroid
import com.antsyferov.astronerd.ui.panes.visualization.rememberEarth
import com.antsyferov.astronerd.ui.panes.visualization.rememberISS
import com.antsyferov.astronerd.ui.panes.visualization.rememberMercury
import com.antsyferov.astronerd.ui.panes.visualization.rememberMoon
import com.antsyferov.impl.local.AsteroidEntity
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animatePosition
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Position
import io.github.sceneview.math.toFloat3
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import java.time.LocalDateTime

@Composable
fun AsteroidScene3D(
    date: LocalDateTime,
    onDateChanged: (Int) -> Unit,
    asteroidEntity: AsteroidEntity
) {

    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var moveStart by remember { mutableIntStateOf(0) }
    val centerNode = rememberNode(engine)

    val cameraNode = rememberCameraNode(engine) {
        centerNode.addChildNode(this)
        position = Position(z=5f)
    }

    val earth = rememberEarth(modelLoader = modelLoader, false).apply {
        position = Position(0f,0f,0f)
    }
    val asteroid = rememberAsteroid(modelLoader = modelLoader)
    val moon = rememberMoon(modelLoader = modelLoader, areRealDistances = false)
    val iss = rememberISS(modelLoader = modelLoader, areRealDistances = false)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val orbitNode = rememberNode(engine = engine)
    val moonOrbitNode = rememberNode(engine)

    var orbitPoints by remember { mutableStateOf(emptyList<Vector3>()) }
    var moonPoints by remember { mutableStateOf(emptyList<Vector3>()) }
    var issPoints by remember { mutableStateOf(emptyList<Vector3>()) }

    LaunchedEffect(asteroidEntity.orbitData) {
        asteroidEntity.orbitData?.let {
            orbitPoints = generateOrbitPoints(it)
            moonPoints = generateMoonPoints(it.semiMajorAxis * 0.6)
            issPoints = generateMoonPoints(it.semiMajorAxis * 0.3)

            val asteroidPosition = orbitPoints[0].scaled(5f).toFloat3()
            asteroid.position = Position(0f,0f,0f)
            asteroid.centerOrigin(asteroidPosition)

            val moonPosition = moonPoints[0].scaled(5f).toFloat3()
            moon.position = Position(0f,0f,0f)
            moon.centerOrigin(moonPosition)

            val issPosition = issPoints[0].scaled(5f).toFloat3()
            iss.position = Position(0f,0f,0f)
            iss.centerOrigin(issPosition)

            drawOrbitNode(points = orbitPoints, engine = engine, materialLoader = materialLoader, orbitNode, Color.Red.copy(alpha = 0.8f))
            drawOrbitNode(moonPoints, engine, materialLoader, moonOrbitNode, Color.Blue.copy(alpha = 0.8f))
        }

    }

    val transitionState = remember { MutableTransitionState(earth.position) }
    val asteroidTransition = rememberTransition(
        label = "CameraTransition",
        transitionState = transitionState
    )
    val asteroidPosition by asteroidTransition.animatePosition(
        { tween(50) }
    ) { it }


    val moonTransitionState = remember { MutableTransitionState(earth.position) }
    val moonTransition = rememberTransition(
        label = "MoonTransition",
        transitionState = moonTransitionState
    )
    val moonPosition by moonTransition.animatePosition(
        { tween(50) }
    ) { it }

    val issTransitionState = remember { MutableTransitionState(earth.position) }
    val issTransition = rememberTransition(
        label = "ISSTransition",
        transitionState = issTransitionState
    )
    val issPosition by issTransition.animatePosition(
        { tween(50) }
    ) { it }

    LaunchedEffect(date) {
        if (orbitPoints.isNotEmpty()) {
            val index = date.dayOfYear % orbitPoints.size
            transitionState.targetState = orbitPoints[index].scaled(5f).toFloat3()
        }
        if (moonPoints.isNotEmpty()) {
            val index = date.dayOfYear % moonPoints.size
            moonTransitionState.targetState = moonPoints[index].scaled(5f).toFloat3()
        }
        if (issPoints.isNotEmpty()) {
            val index = date.dayOfYear % issPoints.size
            issTransitionState.targetState = issPoints[index].scaled(5f).toFloat3()
        }
    }



    Scene(
        modifier = Modifier
            .fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        cameraNode = cameraNode,
        cameraManipulator = rememberCameraManipulator(
            orbitHomePosition = cameraNode.worldPosition,
            targetPosition = centerNode.worldPosition
        ),
        mainLightNode = rememberMainLightNode(engine) {
            position = Position(x = 0.0f, y = 0.0f, z = 0.0f)
        },
        onFrame = {
            cameraNode.lookAt(centerNode)
            if (asteroidEntity.orbitData != null) {
                asteroid.position = Position(0f,0f,0f)
                asteroid.centerOrigin(asteroidPosition)
                moon.position = Position(0f,0f,0f)
                moon.centerOrigin(moonPosition)
                iss.position = Position(0f,0f,0f)
                iss.centerOrigin(issPosition)
            }
        },
        childNodes = listOf(centerNode,earth, asteroid, orbitNode, moon, iss, moonOrbitNode),
        isOpaque = false
    )

}