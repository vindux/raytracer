package utils;


import scene.Shape;
import utils.algebra.Vec3;

/**
 * Intersection class
 * Stores properties for intersection tests
 **/
public class Intersection {

    private Vec3 mIntersectionPoint;
    private Vec3 mNormal;
    private Vec3 mInRay;
    private Vec3 mOutRay;
    private Shape mShape;
    private float mDistance;
    private boolean mHit;

    /** Constructor **/
    public Intersection() {}

    public Vec3 getIntersectionPoint() {
        return mIntersectionPoint;
    }

    public void setIntersectionPoint(Vec3 intersectionPoint) {
        this.mIntersectionPoint = intersectionPoint;
    }

    public Vec3 getNormal() {
        return mNormal;
    }

    public void setNormal(Vec3 normal) {
        this.mNormal = normal.normalize();
    }

    public Vec3 getInRay() {
        return mInRay;
    }

    public void setInRay(Vec3 inRay) {
        this.mInRay = inRay;
    }

    public Vec3 getOutRay() {
        return mOutRay;
    }

    public void setOutRay(Vec3 outRay) {
        this.mOutRay = outRay;
    }

    public Shape getShape() {
        return mShape;
    }

    public void setShape(Shape shape) {
        this.mShape = shape;
    }

    public void setDistance(float distance) {
        this.mDistance = distance;
    }

    public float getDistance() {
        return mDistance;
    }

    public boolean isHit() {
        return mHit;
    }

    public void setHit(boolean hit) {
        this.mHit = hit;
    }
}
