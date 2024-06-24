package com.antsyferov.astronerd.ui.panes.visualization

import android.view.Display.Mode
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
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.Quaternion
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.localRotation
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.ar.scene.destroy
import io.github.sceneview.collision.Vector3
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import java.time.LocalDateTime

private const val kModelFile = "models/damaged_helmet.glb"

@Composable
fun ArVisualization(
    date: LocalDateTime
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // The destroy calls are automatically made when their disposable effect leaves
        // the composition or its key changes.
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val materialLoader = rememberMaterialLoader(engine)
        val cameraNode = rememberARCameraNode(engine)
        val childNodes = rememberNodes()
        val view = rememberView(engine)
        val collisionSystem = rememberCollisionSystem(view)

        val orbitalData = rememberOrbitalPositions(date, rotateOrientation = true)

        val sun = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/sun.glb"
                ),
                scaleToUnits = 0.5f,
            )
        }
        val mercury = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/mercury.glb"
                ),
                scaleToUnits = 0.2f,
                centerOrigin = orbitalData.mercury
            )
        }
        val venus = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/venus.glb"
                ),
                scaleToUnits = 0.3f,
            )
        }

        val earth = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/earth.glb"
                ),
                scaleToUnits = 0.3f,
            )
        }

        val mars = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/mars.glb"
                ),
                scaleToUnits = 0.3f,
            )
        }

        val jupiter = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/jupiter.glb"
                ),
                scaleToUnits = 0.4f,
            )
        }

        val saturn = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/saturn.glb"
                ),
                scaleToUnits = 0.6f,
            )
        }

        val uranus = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/uranus.glb"
                ),
                scaleToUnits = 0.3f,
            )
        }

        val neptune = rememberNode {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = "models/neptune.glb"
                ),
                scaleToUnits = 0.3f,
            )
        }

        val nodes = listOf(sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune)

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
                                modelLoader = modelLoader,
                                materialLoader = materialLoader,
                                anchor = anchor,
                                nodes = nodes
                            )
                        }
                }
            },
            onSessionPaused = {
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { motionEvent, node ->
                    /*if (node == null) {
                        val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                        hitResults?.firstOrNull {
                            it.isValid(
                                depthPoint = false,
                                point = false
                            )
                        }?.createAnchorOrNull()
                            ?.let { anchor ->
                                planeRenderer = false
                                childNodes.map {
                                    it.destroy()
                                }
                                childNodes.clear()
                                childNodes + nodes
                                childNodes + createAnchorNode(
                                    engine = engine,
                                    modelLoader = modelLoader,
                                    materialLoader = materialLoader,
                                    anchor = anchor,
                                    nodes = nodes
                                )
                            }
                    }*/
                })
        )
        Text(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 32.dp, end = 32.dp),
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            color = Color.White,
            text = trackingFailureReason?.let {
                it.getDescription(LocalContext.current)
            } ?: if (childNodes.isEmpty()) {
                stringResource(R.string.point_your_phone_down)
            } else {
                stringResource(R.string.tap_anywhere_to_add_model)
            }
        )
    }
}

/*
fun createAnchorNode(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    anchor: Anchor
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val modelNode = ModelNode(
        modelInstance = modelLoader.createModelInstance(kModelFile),
        // Scale to fit in a 0.5 meters cube
        scaleToUnits = 0.5f
    ).apply {
        // Model Node needs to be editable for independent rotation from the anchor rotation
        isEditable = true
        editableScaleRange = 0.2f..0.75f
    }
    val boundingBoxNode = CubeNode(
        engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
        isVisible = false
    }
    modelNode.addChildNode(boundingBoxNode)
    anchorNode.addChildNode(modelNode)

    listOf(modelNode, anchorNode).forEach {
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }
    return anchorNode
}*/

fun createAnchorNode(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    anchor: Anchor,
    nodes: List<ModelNode>
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor).apply {
        //worldTransform(quaternion = Quaternion.fromEuler(0f, 0f, 90f))
        //this.transform(quaternion = Quaternion.fromEuler(90f, 0f, 0f))
        //this.quaternion = Quaternion.fromAxisAngle(Float3(1.0f, 1.0f, 1.0f), 45.0f)
        //localRotation = Quaternion.axisAngle()

    }
   /* val sun = ModelNode(
        modelInstance = modelLoader.createModelInstance(
            assetFileLocation = "models/sun.glb"
        ),
        scaleToUnits = 0.5f,
    )*//*.apply {
        // Model Node needs to be editable for independent rotation from the anchor rotation
        isEditable = true
        editableScaleRange = 0.2f..0.75f
    }*/

    /*val earth = ModelNode(
        modelInstance = modelLoader.createModelInstance(
            assetFileLocation = "models/earth.glb"
        ),
        scaleToUnits = 0.3f,
    ).apply {
        position = Position(0f,0f,0f)
        centerOrigin(orbitalData.earth)
    }*/

    /*val boundingBoxNode = CubeNode(
        engine,
        size = sun.extents,
        center = sun.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
        isVisible = false
    }*/

    //sun.addChildNode(boundingBoxNode)
    nodes.forEach {
        anchorNode.addChildNode(it)
    }
    //anchorNode.addChildNode(sun)
    //anchorNode.addChildNode(earth)


    /*listOf(anchorNode).forEach { //sun,
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }*/
    return anchorNode
}
