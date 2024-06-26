package com.antsyferov.astronerd.ui.panes.visualization

import androidx.annotation.DrawableRes
import com.antsyferov.astronerd.R
import io.github.sceneview.math.Position

enum class CameraPosition(
    @DrawableRes
    val icon: Int,
    var position: Position,
    val isPlanet: Boolean
) {
    Top(R.drawable.ic_camera, Position(z=8f), false),
    Tilted(R.drawable.ic_sun, Position(z=5f, y=-5f), false),
    MERCURY(R.drawable.ic_mercury, Position(), true),
    VENUS(R.drawable.ic_venus, Position(), true),
    EARTH(R.drawable.ic_earth, Position(), true),
    MARS(R.drawable.ic_mars, Position(), true),
    JUPITER(R.drawable.ic_jupiter, Position(), true),
    SATURN(R.drawable.ic_saturn, Position(), true),
    URANUS(R.drawable.ic_uranus, Position(), true),
    NEPTUNE(R.drawable.ic_neptune, Position(), true);

    fun setOrbitalData(orbitalData: OrbitalData) {
        MERCURY.position = orbitalData.mercury.copy(y = 2f)
        VENUS.position = orbitalData.venus.copy(y = 2f)
        EARTH.position = orbitalData.earth.copy(y = 2f)
        MARS.position = orbitalData.mars.copy(y = 2f)
        JUPITER.position = orbitalData.jupiter.copy(y = 2f)
        SATURN.position = orbitalData.saturn.copy(y = 2f)
        URANUS.position = orbitalData.uran.copy(y = 2f)
        NEPTUNE.position = orbitalData.neptune.copy(y = 2f)
    }
}