package scene;

import ray.Ray;
import scene.material.Material;
import utils.algebra.Vec3;

/**
 * Plane class
 * Subclass of Shape, defines a plane that can be placed in our scene
 * A plane is described as A*x+B*y+C*z+Q=0
 */
public class Plane extends Shape {

    private Vec3 center;
    private Vec3 normal;
    private Vec3 rayDirection;
    private Vec3 rayStartPoint;

    /**
     * Constructor
     **/
    public Plane(Vec3 _center, Vec3 _normal, Material _material) {
        super(_center, _normal, _material);
        this.center = _center;
        this.normal = _normal;
        this.material = _material;
    }

    /**
     * Method that calculates intersection between a plane and a ray
     *
     * @return
     */
    public double intersect(Ray ray) {
        // First, get ray parameters
        rayDirection = ray.getDirection();
        rayStartPoint = ray.getStartPoint();

        // Break down of the plane-ray equation
        float rayPlaneEquation1 = normal.scalar(rayDirection);
        float t;

       /* if(rayPlaneEquation1 == 0) {
            return Double.NaN;
        }else if >0{
            t = (rayPlaneEquation2.scalar(normal))/rayPlaneEquation1;
            ray.setT(t);
        }
        return rayPlaneEquation1; */

        if (Math.abs(rayPlaneEquation1) > 0) {
            t = center.sub(ray.getStartPoint()).scalar(normal)/rayPlaneEquation1;
            if ( t >= 0 ) {
                ray.setT(t);
            } else {
                return Double.NaN;
            }
        }
        return rayPlaneEquation1;
    }
}