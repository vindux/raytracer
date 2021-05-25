package ray;

import utils.algebra.Vec3;

/**
 * Ray class
 * Defines the ray that is sent from the camera
 **/
public class Ray {

    private Vec3 direction;
    private Vec3 startPoint;
    private float t;

    /** Constructor **/
    public Ray(Vec3 _startPoint, Vec3 _direction, float _t) {
        this.startPoint = _startPoint;
        this.direction = _direction.sub(startPoint).normalize();
        this.t = _t;
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

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    /**
     * Method that returns the normalized ray vector
     * Parameter t influences the length of the vector
     * Ray is described as : R = startPoint + t * rayDirection
    **/
    public Vec3 calculateRayAt() {
        // Formulate ray
        Vec3 rayVector = startPoint.add(direction.multScalar(t)).normalize();

        // Tweak direction to adapt it to the viewplane
        rayVector.x = (rayVector.x+1)/2;
        rayVector.y = (rayVector.y+1)/2;
        rayVector.z = (rayVector.z+1)/2;

        return rayVector;
    }

}
