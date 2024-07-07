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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.antsyferov.astronerd.ui.panes.visualization.rememberAsteroid
import com.antsyferov.astronerd.ui.panes.visualization.rememberEarth
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
    val materialLoader = rememberMaterialLoader(engine = engine)
    val orbitNode = rememberNode(engine = engine)

    var orbitPoints by remember { mutableStateOf(emptyList<Vector3>()) }

    LaunchedEffect(asteroidEntity.orbitData) {
        asteroidEntity.orbitData?.let {
            orbitPoints = generateOrbitPoints(it)

            val asteroidPosition = orbitPoints[0].scaled(5f).toFloat3()
            asteroid.position = Position(0f,0f,0f)
            asteroid.centerOrigin(asteroidPosition)

            drawOrbitNode(points = orbitPoints, engine = engine, materialLoader = materialLoader, orbitNode)
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

    LaunchedEffect(date) {
        if (orbitPoints.isNotEmpty()) {
            val index = date.dayOfYear % orbitPoints.size
            transitionState.targetState = orbitPoints[index].scaled(5f).toFloat3()
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
            }

        },
        childNodes = listOf(centerNode,earth, asteroid, orbitNode),
        isOpaque = false
    )

}