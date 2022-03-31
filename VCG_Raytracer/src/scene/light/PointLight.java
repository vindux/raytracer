package scene.light;

import utils.RgbColor;
import utils.algebra.Vec3;

/**
 * Point light class
 */
public class PointLight extends Light {
    private final Vec3 mPosition;
    private final RgbColor mIntensity;

    /** Constructor **/
    public PointLight (Vec3 _position, RgbColor _intensity) {
        super(_position, _intensity);
        this.mPosition = _position;
        this.mIntensity = _intensity;
    }

    public RgbColor getColor() {
        return mIntensity;
    }

    public Vec3 getPosition() {
        return mPosition;
    }

}
