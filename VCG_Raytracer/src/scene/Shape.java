package scene;

import ray.Ray;
import utils.Intersection;
import utils.algebra.Vec3;

/**
 * Shape class
 * Shape defines the shape of the SceneObject
 */
public class Shape extends SceneObject {

    private Vec3 center;

    public double intersect(Ray ray) { return Double.NaN; }
    public Vec3 getCenter() {
        return center;
    }
    public void setCenter(Vec3 _center) {
        this.center = _center;
    }
}

