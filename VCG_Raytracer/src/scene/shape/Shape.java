package scene.shape;

import ray.Ray;
import scene.SceneObject;
import scene.material.Material;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

/**
 * Shape class
 * Shape defines the shape of the SceneObject
 */
public abstract class Shape extends SceneObject {

    protected Vec3 center;
    protected Material material;

    /**
     * Constructor used for basic shapes
     */
   /* public Shape(Vec3 _center, Material _material) {
        this.transformMatrix.translateXYZ(_center);
        this.center = transformMatrix.multVec3(new Vec3(0,0,0), true);
        this.material = _material;
    }

    /**
     * Constructor used for shapes with normals e.g. planes
     *//*
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
    }**/
    public Shape(Vec3 _center, Material _material) {
        this.center = _center;
        this.material = _material;
    }

    public Material getMaterial(){return this.material;}
    public void setMaterial(Material _material){this.material = _material;}
    public abstract Matrix4x4 getTransformMatrix();
    public abstract float intersect(Vec3 startPoint, Vec3 direction);
    public abstract Vec3 calculateNormal(Vec3 point);

    public Vec3 getPosition() {
        return this.center;
    }

    public void setPosition(Vec3 center) {
        this.center = center;
    }
}

