package ray;

import utils.algebra.Vec3;

/**
 * Ray class
 * Defines the ray that is sent from the camera
 **/
public class Ray {

    private Vec3 direction;

    /** Constructor **/
    public Ray(Vec3 _direction) {
        this.direction = _direction;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 _direction) {
        this.direction = _direction;
    }

    /**
     * Method that returns the normalized ray vector
     * Parameter t influences the length of the vector
    **/
    public Vec3 calculateRayAt(float t) {
        return direction.multScalar(t).normalize();
    }

}
