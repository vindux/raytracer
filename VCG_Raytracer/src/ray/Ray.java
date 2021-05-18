package ray;

import utils.algebra.Vec3;

public class Ray {

    private Vec3 direction;

    public Ray(Vec3 _direction) {
        this.direction = _direction;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 _direction) {
        this.direction = _direction;
    }

    public Vec3 calculateRayAt(float t) {
        return direction.multScalar(t).normalize();
    }

}
