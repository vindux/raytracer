package scene;

import ray.Ray;
import scene.material.Material;
import utils.algebra.Vec3;

/**
 * Plane class
 * Subclass of Shape, defines a square that can be placed in our scene
 */
public class Square extends Shape {

    private Vec3 center;
    private Vec3 normal;
    private float size;
    private Vec3 rayDirection;
    private Vec3 rayStartPoint;

    /**
     * Constructor
     **/
    public Square(Vec3 _center, Vec3 _normal, float _size, Material _material) {
        super(_center, _normal, _size, _material);
        this.center = _center;
        this.normal = _normal;
        this.size = _size;
        this.material = _material;
    }

    public Vec3 calculateNormal(Vec3 point) {
        return normal.multScalar(-1);
    }

    /**
     * Method that calculates intersection between a square and a ray
     */
    public double intersect(Ray ray) {
        // First, get ray parameters
        rayDirection = ray.getDirection();
        rayStartPoint = ray.getStartPoint();

        // Calculating the discriminant
        float discriminant = rayDirection.scalar(normal);
        float t;

        // If the discriminant is > 0, calculate t and commit it to the ray
        if (Math.abs(discriminant) >= 0) {
            t = center.sub(ray.getStartPoint()).scalar(normal)/discriminant;
            if ( t >= 0 ) {
                Vec3 intersectionPoint = rayStartPoint.add(rayDirection.multScalar(t));
                if (squareHit(intersectionPoint)) {
                    ray.setT(t);
                }
            } else {
                return Double.NaN;
            }
        }
        return discriminant;
    }

    /**
     * Method to check if the square is hit
     */
    private boolean squareHit(Vec3 intersectionPoint) {
        return center.add(new Vec3(size / 2, 0, 0)).x < intersectionPoint.x || center.sub(new Vec3(size / 2, 0, 0)).x > intersectionPoint.x
                || center.add(new Vec3(0, size / 2, 0)).y < intersectionPoint.y || center.sub(new Vec3(0, size / 2, 0)).y > intersectionPoint.y
                || center.add(new Vec3(0, 0, size / 2)).z < intersectionPoint.z || center.sub(new Vec3(0, 0, size / 2)).z > intersectionPoint.z;
    }
}