package scene.light;

import utils.RgbColor;
import utils.algebra.Vec3;

public class PointLight extends Light {
    private Vec3 position;
    private RgbColor intensity;

    /** Constructor **/
    public PointLight (Vec3 _position, RgbColor _intensity) {
        super(_position, _intensity);
        this.position = _position;
        this.intensity = _intensity;
    }

    public RgbColor getColor() {
        return intensity;
    }

    public Vec3 getPosition() {
        return position;
    }

}
