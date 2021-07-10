package ray;

import utils.algebra.Vec3;

/**
 * Ray class
 * Defines the ray that is sent from the camera
 **/
public class Ray {

    private Vec3 direction;
    private Vec3 startPoint;
    private Vec3 destinationPoint;
    private float distance;

    /** Constructor **/
    public Ray(Vec3 _startPoint, Vec3 _destinationPoint, Vec3 _direction, float _distance) {
        this.startPoint = _startPoint;
        this.destinationPoint = _destinationPoint;
        this.direction = _direction;
        this.distance = _distance;
    }

    public Vec3 getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Vec3 startPoint) {
        this.startPoint = startPoint;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 _direction) {
        this.direction = _direction;
    }

    public Vec3 getDestinationPoint() { return destinationPoint; }

    public void setDestinationPoint(Vec3 destinationPoint) { this.destinationPoint = destinationPoint; }

    public float getDistance() { return distance; }

    public void setDistance(float distance) { this.distance = distance; }
}
