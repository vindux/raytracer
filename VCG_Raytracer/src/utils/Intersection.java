package utils;


import scene.shape.Shape;
import utils.algebra.Vec3;
import ray.Ray;

/**
 * Intersection class
 * Stores properties for intersection tests
 **/
public class Intersection {

    private Vec3 mIntersectionPoint;
    private Vec3 mNormal;
    private Shape mShape;
    private Ray intersectionRay;
    private float mDistance;
    private boolean mHit;
    private float t;

    /** Constructor **/
    public Intersection() {}

    public Ray getIntersectionRay() {
        return intersectionRay;
    }

    public void setIntersectionRay(Ray intersectionRay) {
        this.intersectionRay = intersectionRay;
    }

    public Vec3 getIntersectionPoint() {
        return mIntersectionPoint;
    }

    public void setIntersectionPoint() {
        Vec3 intersectionPoint = intersectionRay.getStartPoint().add(intersectionRay.getDirection().multScalar(t));
        this.mIntersectionPoint = mShape.getTransformMatrix().multVec3(intersectionPoint, true);
    }

    public Vec3 getNormal() {
        return mNormal;
    }

    public void setNormal() {
        this.mNormal = mShape.calculateNormal(mIntersectionPoint);
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

    public void intersect() {
        t = mShape.intersect(intersectionRay.getStartPoint(), intersectionRay.getDirection());
        setHit(!Float.isNaN(t));
        if (mHit) {
            setDistance(intersectionRay.getDirection().multScalar(t).length());
        }
    }
}
