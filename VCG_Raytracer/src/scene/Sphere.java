package scene;

import ray.Ray;
import utils.Intersection;
import utils.algebra.Vec3;

/**
 * Sphere class
 * Subclass of Shape, defines a sphere that can be placed in our scene
 * A Sphere is described as (x^2+y^2+z^2) = r^2
 */
public class Sphere extends Shape {

    private Vec3 center;
    private int radius;
    private Intersection intersection;
    private Vec3 rayDirection;
    private Vec3 rayStartPoint;

    /** Constructor **/
    public Sphere(Vec3 _center, int _radius) {
        super();
        this.center = _center;
        this.radius = _radius;
    }

    public Vec3 getCenter() {
        return center;
    }

    public void setCenter(Vec3 _center) {
        this.center = _center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int _radius) {
        this.radius = _radius;
    }

    public boolean isHit() {
        return intersection.isHit();
    }

    /**
     * Method that calculates intersection between a sphere and a ray
     */
    public void intersect(Ray ray) {
        // First, get ray parameters
        rayDirection = ray.getDirection();
        rayStartPoint = ray.getStartPoint();

        // Set equations describing the discriminant
        double raySphereEquation1 = 2*(rayStartPoint.x * rayDirection.x + rayStartPoint.y * rayDirection.y + rayStartPoint.z * rayDirection.z);
        double raySphereEquation2 = Math.pow(rayStartPoint.x, 2) + Math.pow(rayStartPoint.y, 2) + Math.pow(rayStartPoint.z, 2) - Math.pow(radius,2);
        double discriminant = Math.pow(raySphereEquation1, 2)-4*raySphereEquation2;

        /*
         * Do the intersection test
         * The discriminant can already lead to an answer without calculating much
         * TODO: Later on we will set different cases, for now we only need to know if we hit the sphere
         */
        if (discriminant < 0) {
            // no intersection
            intersection = new Intersection(false);
        } else {
            intersection = new Intersection(true);
        }
    }
}
