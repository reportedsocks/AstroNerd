package com.antsyferov.astronerd.ui.panes.visualization

import androidx.compose.runtime.Composable
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberNode

enum class Planet {
    SUN, // Yes, sun is here. No, I'm not renaming the enum to 'Body' or smth.
    MERCURY,
    VENUS,
    EARTH,
    MARS,
    JUPITER,
    SATURN,
    URANUS,
    NEPTUNE
}

fun String.toPlanet(): Planet {
    return Planet.valueOf(this)
}

@Composable
fun rememberSun(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/sun.glb"
            ),
            scaleToUnits = 0.5f,

            ).apply { name = Planet.SUN.name }
    }
}

@Composable
fun rememberMercury(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/mercury.glb"
            ),
            scaleToUnits = 0.2f,
            ).apply { name = Planet.MERCURY.name }
    }
}

@Composable
fun rememberVenus(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/venus.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = Planet.VENUS.name }
    }
}

@Composable
fun rememberEarth(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/earth.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = Planet.EARTH.name }
    }
}

@Composable
fun rememberMars(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/mars.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = Planet.MARS.name }
    }
}

@Composable
fun rememberJupiter(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/jupiter.glb"
            ),
            scaleToUnits = 0.4f,
        ).apply { name = Planet.JUPITER.name }
    }
}

@Composable
fun rememberSaturn(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/saturn.glb"
            ),
            scaleToUnits = 0.6f,
        ).apply { name = Planet.SATURN.name }
    }
}

@Composable
fun rememberUranus(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/uranus.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = Planet.URANUS.name }
    }
}

@Composable
fun rememberNeptune(modelLoader: ModelLoader) : ModelNode {
    return rememberNode {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/neptune.glb"
            ),
            scaleToUnits = 0.3f,
        ).apply { name = Planet.NEPTUNE.name }
    }
}