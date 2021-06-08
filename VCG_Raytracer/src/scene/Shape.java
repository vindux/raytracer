package scene;

import ray.Ray;
import scene.material.Material;
import utils.Intersection;
import utils.algebra.Vec3;

/**
 * Shape class
 * Shape defines the shape of the SceneObject
 */
public class Shape extends SceneObject {

    protected Vec3 center;
    protected String material;

    public Shape(Vec3 _center, String _material) {
        this.center = _center;
        this.material = _material;
    }

    public Vec3 getCenter() {
        return center;
    }
    public void setCenter(Vec3 _center) {
        this.center = _center;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double intersect(Ray ray) { return Double.NaN; }
}

