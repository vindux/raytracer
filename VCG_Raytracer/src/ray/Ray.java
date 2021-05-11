package ray;

import utils.algebra.Vec3;

public class Ray {

    private Vec3 startPoint;
    private Vec3 direction;

    public Ray(Vec3 _startPoint, Vec3 _direction) {
        this.startPoint = _startPoint;
        this.direction = _direction;
    }

    public Vec3 getStartPoint() {
        return startPoint;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setStartPoint(Vec3 _startPoint) {
        this.startPoint = _startPoint;
    }

    public void setDirection(Vec3 _direction) {
        this.direction = _direction;
    }

    public Vec3 calculateRayAt() {
        return startPoint.add(direction).normalize();
    }

}
