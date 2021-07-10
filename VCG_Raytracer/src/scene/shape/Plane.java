/*package scene.shape;

import ray.Ray;
import scene.material.Material;
import utils.algebra.Vec3;

/**
 * Plane class
 * Subclass of Shape, defines a plane that can be placed in our scene
 * A plane is described as A*x+B*y+C*z+Q=0
 */
/*public class Plane extends Shape {

    private Vec3 center;
    private Vec3 normal;
    private Vec3 rayDirection;
    private Vec3 rayStartPoint;

    /**
     * Constructor
     **/
  /*  public Plane(Vec3 _center, Vec3 _normal, Material _material) {
        super(_center, _normal, _material);
        this.center = _center;
        this.normal = _normal.multScalar(-1);
        this.material = _material;
    }

    public Vec3 calculateNormal(Vec3 point) {
        return normal.multScalar(-1);
    }

    /**
     * Method that calculates intersection between a plane and a ray
     */
 /*   public double intersect(Ray ray) {
        // First, get ray parameters
        rayDirection = ray.getDirection();
        rayStartPoint = ray.getStartPoint();

        // Breakdown of the plane-ray equation
        float rayPlaneEquation = normal.scalar(rayDirection);
        float t;

        if (Math.abs(rayPlaneEquation) >= 0) {
            t = center.sub(ray.getStartPoint()).scalar(normal)/rayPlaneEquation;
            if ( t >= 0 ) {
                ray.setT(t);
            } else {
                return Double.NaN;
            }
        }
        return rayPlaneEquation;
    }
}*/