package scene.shape;

import scene.material.Material;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

/**
 * Sphere class
 * Subclass of Shape, defines a sphere that can be placed in our scene
 * A Sphere is described as (x^2+y^2+z^2) = r^2
 */
public class Sphere extends Shape {

    private float radius;
    private Vec3 normal;
    private Matrix4x4 transformationMatrix = new Matrix4x4();

    /** Constructor **/
    public Sphere(Vec3 _center, Float _radius, Material _material) {
        super(_center, _material);
        this.radius = _radius;
        this.center = _center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(Float _radius) {
        this.radius = _radius;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public void setNormal(Vec3 normal) {
        this.normal = normal;
    }

    public Vec3 calculateNormal(Vec3 point) {
        return point.sub(center).normalize();
    }

    public Matrix4x4 getTransformMatrix() {
        return this.transformationMatrix;
    }

    public Matrix4x4 transform() {
        return new Matrix4x4().translateXYZ(getPosition());
    }

    /**
     * Method that calculates intersection between a sphere and a ray
     */
    public float intersect(Vec3 rayStartPoint, Vec3 rayDirection) {
        // Set equations describing the discriminant
        double raySphereEquation1 = 2*(rayStartPoint.scalar(rayDirection));
        double raySphereEquation2 = rayStartPoint.scalar(rayStartPoint) - Math.pow(radius,2);
        double discriminant = Math.pow(raySphereEquation1, 2) - 4 * raySphereEquation2;

        /*
         * Do the intersection test
         * The discriminant can already lead to an answer without calculating everything
         */
        if (discriminant < 0) {
            return Float.NaN;
        } else {
            float t0 = (float) ((-(raySphereEquation1)-Math.sqrt(discriminant))/2);
            float t1 = (float) ((-(raySphereEquation1)+Math.sqrt(discriminant))/2);

            if (discriminant == 0) {
                // It does not matter which t we take since both are equal
                return t0;
            } else if (discriminant > 0) {
                if(t0 < 0.01f && t1 >= 0.01f || t1 < 0.01f && t0 >= 0.01f) {
                    // inside a sphere
                    return Math.max(t0, t1);
                } else if (t0 >= 0.01f && t1 >= 0.01f) {
                    // hit
                    return Math.min(t0, t1);
                } else {
                    // no hit
                    return Float.NaN;
                }
            }
        }
        return Float.NaN;
    }
}
