package utils;


import scene.Shape;
import utils.algebra.Vec3;

/**
 * Intersection class
 * Stores properties for intersection tests
 **/
public class Intersection {

    private Vec3 intersectionPoint;
    private Vec3 normal;
    private Vec3 inRay;
    private Vec3 outRay;
    private Shape shape;
    private float distance;
    private boolean hit;

    /** Constructor **/
    public Intersection() {}

    public Vec3 getIntersectionPoint() {
        return intersectionPoint;
    }

    public void setIntersectionPoint(Vec3 intersectionPoint) {
        this.intersectionPoint = intersectionPoint;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public void setNormal(Vec3 normal) {
        this.normal = normal;
    }

    public Vec3 getInRay() {
        return inRay;
    }

    public void setInRay(Vec3 inRay) {
        this.inRay = inRay;
    }

    public Vec3 getOutRay() {
        return outRay;
    }

    public void setOutRay(Vec3 outRay) {
        this.outRay = outRay;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
