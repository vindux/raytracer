package scene;

import ray.Ray;
import scene.material.Material;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

/**
 * Shape class
 * Shape defines the shape of the SceneObject
 */
public class Shape extends SceneObject {

    protected Vec3 center;
    protected Vec3 normal;
    protected float size;
    protected Material material;
    protected Matrix4x4 transformMatrix = new Matrix4x4();

    /**
     * Constructor used for basic shapes
     */
    public Shape(Vec3 _center, Material _material) {
        this.transformMatrix.translateXYZ(_center);
        this.center = transformMatrix.multVec3(new Vec3(0,0,0), true);
        this.material = _material;
    }

    /**
     * Constructor used for shapes with normals e.g. planes
     */
    public Shape(Vec3 _center, Vec3 _normal, Material _material) {
        this.transformMatrix.translateXYZ(_center);
        this.center = transformMatrix.multVec3(new Vec3(0,0,0), true);
        this.normal = _normal;
        this.material = _material;
    }
    public Shape(Vec3 _center, Vec3 _normal, float _size, Material _material) {
        this.transformMatrix.translateXYZ(_center);
        this.center = transformMatrix.multVec3(new Vec3(0,0,0), true);
        this.normal = _normal;
        this.size = _size;
        this.material = _material;
    }

    public Vec3 getCenter() {
        return center;
    }
    public void setCenter(Vec3 _center) { this.center = _center; }

    public Vec3 getNormal() { return normal; }
    public void setNormal(Vec3 normal) { this.normal = normal; }
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Matrix4x4 getTransformMatrix() {
        return transformMatrix;
    }
    public void setTransformMatrix(Matrix4x4 transformMatrix) {
        this.transformMatrix = transformMatrix;
    }

    public double intersect(Ray ray) { return Double.NaN; }
    public Vec3 calculateNormal(Vec3 point) {return null;}
}

