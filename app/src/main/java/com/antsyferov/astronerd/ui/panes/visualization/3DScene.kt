package com.antsyferov.astronerd.ui.panes.visualization

import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
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
import com.google.android.filament.Viewport
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animatePosition
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
    camera: CameraPosition,
    onChangeCameraToPlane: (Planet) -> Unit,
    onShowDetails: (Planet) -> Unit,
    enableRealDistances: Boolean,
    isInnerBelt: Boolean
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var moveStart by remember { mutableIntStateOf(0) }
    val centerNode = rememberNode(engine)

    val cameraNode = rememberCameraNode(engine) {
        centerNode.addChildNode(this)
        position  = CameraPosition.Top.position
    }

    LaunchedEffect(camera) {
        cameraNode.focalLength = if (camera.isPlanet) 50.0 else 28.0
    }

    val orbitalData = rememberOrbitalPositions(date, rotateOrientation = camera.isPlanet, enableRealDistances, isInnerBelt)
    camera.setOrbitalData(orbitalData)
    val transitionState = remember {MutableTransitionState(CameraPosition.Tilted) }
    transitionState.targetState = camera

    val cameraTransition = rememberTransition(
        label = "CameraTransition",
        transitionState = transitionState
    )
    val cameraPosition by cameraTransition.animatePosition(
        { tween(3000) }
    ) { it.position }

    val sun = rememberSun(modelLoader = modelLoader, enableRealDistances)
    val mercury = rememberMercury(modelLoader = modelLoader, enableRealDistances)
    val venus = rememberVenus(modelLoader = modelLoader, enableRealDistances)
    val earth = rememberEarth(modelLoader = modelLoader, enableRealDistances)
    val mars = rememberMars(modelLoader = modelLoader, enableRealDistances)
    val jupiter = rememberJupiter(modelLoader = modelLoader, enableRealDistances)
    val saturn = rememberSaturn(modelLoader = modelLoader, enableRealDistances)
    val uranus = rememberUranus(modelLoader = modelLoader, enableRealDistances)
    val neptune = rememberNeptune(modelLoader = modelLoader, enableRealDistances)

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

    val childNodes = if (enableRealDistances) {
        if (isInnerBelt) {
            listOf(centerNode, sun, mercury, venus, earth, mars)
        } else {
            listOf(centerNode, sun, jupiter, saturn, uranus, neptune)
        }

    } else {
        listOf(centerNode, sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune)
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
            cameraNode.position = cameraPosition
            cameraNode.lookAt(centerNode)
        },
        childNodes = childNodes,
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { e, node ->
                node?.name?.let {
                    onShowDetails.invoke(it.toPlanet())
                }
            },
            onLongPress = { e, node ->
                node?.name?.let {
                    onChangeCameraToPlane.invoke(it.toPlanet())
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
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
