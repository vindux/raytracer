package scene.shape;

import scene.material.Material;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

/**
 * Plane class
 * Subclass of Shape, defines a plane that can be placed in our scene
 * A plane is described as A*x+B*y+C*z+Q=0
 */
public class Plane extends Shape {

    protected Vec3 center;
    protected Vec3 normal;
    protected Matrix4x4 transformationMatrix;

    /**
     * Constructor
     **/
    public Plane(Vec3 _center, Material _material, Vec3 _normal) {
        super(_center, _material);
        this.center = _center;
        this.normal = _normal;
        this.material = _material;
        this.transformationMatrix = transform();
    }

    public Matrix4x4 transform() {
        return new Matrix4x4().translateXYZ(getPosition());
    }
    public Vec3 calculateNormal(Vec3 point) {
        return normal;
    }
    public Matrix4x4 getTransformMatrix() {
        return this.transformationMatrix;
    }

    /**
     * Method that calculates intersection between a plane and a ray
     */
    public float intersect(Vec3 rayStartPoint, Vec3 rayDirection) {
        // Breakdown of the plane-ray equation
        float discriminant = normal.scalar(rayDirection);
        float t;

        if (discriminant < 0) {
            t = -1 * (normal.scalar(rayStartPoint)) / discriminant;
            return(t);
        }
        return Float.NaN;
    }
}