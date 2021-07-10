package scene.shape;

import scene.material.Material;
import utils.algebra.Vec3;
import utils.algebra.Matrix4x4;


/**
 * Plane class
 * Subclass of Shape, defines a square that can be placed in our scene
 */
public class Square extends Plane {

    private float size;

    /**
     * Constructor
     **/
    public Square(Vec3 _center, Material _material, Vec3 _normal, float _size) {
        super(_center, _material, _normal);
        this.size = _size;
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
        float discriminant = rayDirection.scalar(normal);

        if (discriminant < 0) {
            float t = -1 * (normal.scalar(rayStartPoint)) / discriminant;
            Vec3 intersectionPoint = rayStartPoint.add(rayDirection.multScalar(t));
            intersectionPoint = transformationMatrix.multVec3(intersectionPoint, true);
            if (!squareHit(intersectionPoint)) {
                return t;
            }
        }
        return Float.NaN;
    }

    /**
     * Method to check if the square is hit
     */
    private boolean squareHit(Vec3 intersectionPoint) {
        Vec3 position = getPosition();
        return position.add(new Vec3(size / 2, 0, 0)).x < intersectionPoint.x
                || position.sub(new Vec3(size / 2, 0, 0)).x > intersectionPoint.x
                || position.add(new Vec3(0, size / 2, 0)).y < intersectionPoint.y
                || position.sub(new Vec3(0, size / 2, 0)).y > intersectionPoint.y
                || position.add(new Vec3(0, 0, size / 2)).z < intersectionPoint.z
                || position.sub(new Vec3(0, 0, size / 2)).z > intersectionPoint.z;
    }
}