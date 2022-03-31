package scene.shape;

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
    public Shape(Vec3 _center, Material _material) {
        this.center = _center;
        this.material = _material;
    }

    public Material getMaterial(){return this.material;}
    public abstract Matrix4x4 getTransformMatrix();
    public abstract float intersect(Vec3 startPoint, Vec3 direction);
    public abstract Vec3 calculateNormal(Vec3 point);

    public Vec3 getPosition() {
        return this.center;
    }
}

