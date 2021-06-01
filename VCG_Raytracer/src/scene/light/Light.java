package scene.light;

import scene.SceneObject;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Light extends SceneObject {

    protected Vec3 mPosition;
    protected RgbColor mColor;

    public Light(Vec3 _position, RgbColor _color) {
        this.mPosition = _position;
        this.mColor = _color;
    }

    public RgbColor getColor() {
        return mColor;
    }

    public Vec3 getPosition() {
        return mPosition;
    }

}
