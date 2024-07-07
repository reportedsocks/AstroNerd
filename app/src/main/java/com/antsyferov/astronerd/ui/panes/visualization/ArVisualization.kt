package com.antsyferov.astronerd.ui.panes.visualization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antsyferov.astronerd.R
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import java.time.LocalDateTime


@Composable
fun ArVisualization(
    date: LocalDateTime,
    onShowDetails: (Planet) -> Unit,
    enableRealDistances: Boolean,
    isInnerBelt: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val cameraNode = rememberARCameraNode(engine)
        val childNodes = rememberNodes()
        val view = rememberView(engine)
        val collisionSystem = rememberCollisionSystem(view)

        val orbitalData = rememberOrbitalPositions(date, rotateOrientation = true, enableRealDistances, isInnerBelt)

        val sun = rememberSun(modelLoader = modelLoader, enableRealDistances)
        val mercury = rememberMercury(modelLoader = modelLoader, enableRealDistances)
        val venus = rememberVenus(modelLoader = modelLoader, enableRealDistances)
        val earth = rememberEarth(modelLoader = modelLoader, enableRealDistances)
        val mars = rememberMars(modelLoader = modelLoader, enableRealDistances)
        val jupiter = rememberJupiter(modelLoader = modelLoader, enableRealDistances)
        val saturn = rememberSaturn(modelLoader = modelLoader, enableRealDistances)
        val uranus = rememberUranus(modelLoader = modelLoader, enableRealDistances)
        val neptune = rememberNeptune(modelLoader = modelLoader, enableRealDistances)

        LaunchedEffect(key1 = isInnerBelt) {
            childNodes.clear()
        }
        val nodes = if (enableRealDistances) {
            if (isInnerBelt) {
                listOf(sun, mercury, venus, earth, mars)
            } else {
                listOf(sun, jupiter, saturn, uranus, neptune)
            }

        } else {
            listOf(sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune)
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

        var planeRenderer by remember { mutableStateOf(true) }

        var trackingFailureReason by remember {
            mutableStateOf<TrackingFailureReason?>(null)
        }
        var frame by remember { mutableStateOf<Frame?>(null) }
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
                            childNodes += createAnchorNode(
                                engine = engine,
                                anchor = anchor,
                                nodes = nodes
                            )
                        }
                }
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { e, node ->
                    node?.name?.toPlanet()?.let(onShowDetails)
                }
            )
        )
        if (childNodes.isEmpty() ||trackingFailureReason != null)  {
            Text(
                modifier = Modifier
                    .systemBarsPadding()
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(start = 32.dp, end = 32.dp),
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                color = Color.White,
                text = trackingFailureReason?.getDescription(LocalContext.current) ?:
                    stringResource(R.string.point_your_phone_down)

            )
        }

    }
}


fun createAnchorNode(
    engine: Engine,
    anchor: Anchor,
    nodes: List<ModelNode>
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)

    nodes.forEach {
        anchorNode.addChildNode(it)
    }

    return anchorNode
}
