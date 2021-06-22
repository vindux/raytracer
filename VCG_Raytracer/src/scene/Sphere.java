package scene;

import ray.Ray;
import scene.material.Material;
import utils.algebra.Vec3;

/**
 * Sphere class
 * Subclass of Shape, defines a sphere that can be placed in our scene
 * A Sphere is described as (x^2+y^2+z^2) = r^2
 */
public class Sphere extends Shape {

    private Vec3 center;
    private float radius;
    private Vec3 rayDirection;
    private Vec3 rayStartPoint;
    private Vec3 normal;

    /** Constructor **/
    public Sphere(Vec3 _center, Float _radius, Material _material) {
        super(_center, _material);
        this.radius = _radius;
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

    /**
     * Method that calculates intersection between a sphere and a ray
     */
    public double intersect(Ray ray) {
        // First, get ray parameters
        rayDirection = ray.getDirection();
        rayStartPoint = ray.getStartPoint();

        // Set equations describing the discriminant
        double raySphereEquation1 = 2*(rayStartPoint.x * rayDirection.x + rayStartPoint.y * rayDirection.y + rayStartPoint.z * rayDirection.z);
        double raySphereEquation2 = Math.pow(rayStartPoint.x, 2) + Math.pow(rayStartPoint.y, 2) + Math.pow(rayStartPoint.z, 2) - Math.pow(radius,2);
        double discriminant = Math.pow(raySphereEquation1, 2)-4*raySphereEquation2;

        /*
         * Do the intersection test
         * The discriminant can already lead to an answer without calculating everything
         */
        if (discriminant < 0) {
            return Double.NaN;
        } else {
            float t0 = (float) ((-(raySphereEquation1)-Math.sqrt(Math.pow(raySphereEquation1,2)-4*raySphereEquation2))/2);
            float t1 = (float) ((-(raySphereEquation1)+Math.sqrt(Math.pow(raySphereEquation1,2)-4*raySphereEquation2))/2);

            if (discriminant == 0) {
                // It does not matter which t we take since both are equal
                ray.setT(t0);
            } else if (discriminant > 0) {
                if (t0 > 0 && t1 > 0) {
                    ray.setT(Math.max(t0, t1));
                } else if (t0 < 0 && t1 < 0) {
                    ray.setT(Float.NaN);
                } else if (t0 < 0 && t1 > 0) {
                    ray.setT(t1);
                } else if (t0 > 0 && t1 < 0) {
                    ray.setT(t0);
                }
            }
        }
        return discriminant;
    }
}
