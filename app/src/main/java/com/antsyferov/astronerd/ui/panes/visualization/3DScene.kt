package com.antsyferov.astronerd.ui.panes.visualization

import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import com.google.android.filament.IndirectLight
import com.google.android.filament.Renderer
import com.google.android.filament.Skybox
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.environment.Environment
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


@Composable
fun Scene3D(
    date: LocalDateTime,
    onDateChanged: (Int) -> Unit,
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var moveStart by remember { mutableIntStateOf(0) }
    val centerNode = rememberNode(engine)

    val cameraNode = rememberCameraNode(engine) {
        position = Position(z=5f, y=-5f)
        lookAt(centerNode)
        centerNode.addChildNode(this)
    }

    val orbitalData = rememberOrbitalPositions(date)

    val sun = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/sun.glb"
            ),
            scaleToUnits = 0.5f,

        ).apply { name = "Sun" }
    }
    val mercury = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/mercury.glb"
            ),
            scaleToUnits = 0.2f,
            centerOrigin = orbitalData.mercury
        ).apply { name = "Mercury" }
    }
    val venus = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/venus.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = "Venus" }
    }

    val earth = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/earth.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = "Earth" }
    }

    val mars = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/mars.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = "Mars" }
    }

    val jupiter = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/jupiter.glb"
            ),
            scaleToUnits = 0.4f,
        ).apply { name = "Jupiter" }
    }

    val saturn = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/saturn.glb"
            ),
            scaleToUnits = 0.6f,
        ).apply { name = "Saturn" }
    }

    val uranus = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/uranus.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = "Uranus" }
    }

    val neptune = rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/neptune.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = "Neptune" }
    }

    LaunchedEffect(orbitalData) {
        mercury.position = Position(0f,0f,0f)
        mercury.centerOrigin(orbitalData.mercury)
        venus.position = Position(0f,0f,0f)
        venus.centerOrigin(orbitalData.venus)
        earth.position = Position(0f,0f,0f)
        earth.centerOrigin(orbitalData.earth)
        mars.position = Position(0f,0f,0f)
        mars.centerOrigin(orbitalData.mars)
        jupiter.position = Position(0f,0f,0f)
        jupiter.centerOrigin(orbitalData.jupiter)
        saturn.position = Position(0f,0f,0f)
        saturn.centerOrigin(orbitalData.saturn)
        uranus.position = Position(0f,0f,0f)
        uranus.centerOrigin(orbitalData.uran)
        neptune.position = Position(0f,0f,0f)
        neptune.centerOrigin(orbitalData.neptune)
    }


    val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
    val cameraRotation by cameraTransition.animateRotation(
        initialValue = Rotation(y = 0.0f),
        targetValue = Rotation(y = 360.0f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7.seconds.toInt(DurationUnit.MILLISECONDS))
        )
    )


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
        childNodes = listOf(centerNode, sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune),
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { e, node ->

                Toast.makeText(context, "${node?.name} Node clicked", Toast.LENGTH_SHORT).show()

            },
            onLongPress = { e, node ->
                Toast.makeText(context, "${node?.name} Node long clicked", Toast.LENGTH_SHORT).show()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        ),
        onTouchEvent = {e, hit ->
            var isHandled = false
            when(e.actionMasked) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_POINTER_DOWN,
                -> {
                    moveStart = e.getX(0).toInt()
                }
                MotionEvent.ACTION_MOVE -> {

                    val dest = e.getX(0).toInt()
                    val diff = moveStart - dest
                    moveStart =  e.getX(0).toInt()

                    onDateChanged.invoke(diff)

                    isHandled = true
                }
            }

            isHandled
        },
        isOpaque = false
    )
}
