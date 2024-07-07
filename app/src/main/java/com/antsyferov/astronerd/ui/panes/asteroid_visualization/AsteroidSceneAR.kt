package com.antsyferov.astronerd.ui.panes.asteroid_visualization

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.antsyferov.astronerd.ui.panes.visualization.rememberAsteroid
import com.antsyferov.astronerd.ui.panes.visualization.rememberEarth
import com.antsyferov.impl.local.AsteroidEntity
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.animation.Transition.animatePosition
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Position
import io.github.sceneview.math.toFloat3
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView
import java.time.LocalDateTime


@Composable
fun AsteroidSceneAR(
    date: LocalDateTime,
    asteroidEntity: AsteroidEntity
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val childNodes = rememberNodes()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view)


    val cameraNode = rememberARCameraNode(engine)

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
            asteroid.animatePositions(asteroid.position, asteroidPosition)

            drawOrbitNode(points = orbitPoints, engine = engine, materialLoader = materialLoader, orbitNode)
        }

    }

    LaunchedEffect(date) {
        if (orbitPoints.isNotEmpty()) {
            val index = date.dayOfYear % orbitPoints.size
            asteroid.position = Position(0f,0f,0f)
            asteroid.centerOrigin(orbitPoints[index].scaled(5f).toFloat3())
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        var planeRenderer by remember { mutableStateOf(true) }

        var trackingFailureReason by remember {
            mutableStateOf<TrackingFailureReason?>(null)
        }
        var frame by remember { mutableStateOf<Frame?>(null) }

        val nodes = listOf(earth,asteroid, orbitNode)

        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            view = view,
            modelLoader = modelLoader,
            collisionSystem = collisionSystem,
            mainLightNode = rememberMainLightNode(engine) {
                position = Position(x = 0.0f, y = 0.0f, z = 0.0f)
            },
            sessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        else -> Config.DepthMode.DISABLED
                    }
                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            cameraNode = cameraNode,
            planeRenderer = planeRenderer,
            onTrackingFailureChanged = {
                trackingFailureReason = it
            },
            onSessionUpdated = { session, updatedFrame ->
                frame = updatedFrame

                if (childNodes.isEmpty()) {
                    updatedFrame.getUpdatedPlanes()
                        .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                        ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                            childNodes += nodes
                            childNodes += createAsteroidAnchorNode(
                                engine = engine,
                                anchor = anchor,
                                nodes = nodes
                            )
                        }
                }
            }
        )
    }


}

fun createAsteroidAnchorNode(
    engine: Engine,
    anchor: Anchor,
    nodes: List<Node>
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)

    nodes.forEach {
        anchorNode.addChildNode(it)
    }

    return anchorNode
}
