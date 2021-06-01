package scene;

import ray.Ray;
import utils.Intersection;

/**
 * Shape class
 * Shape defines the shape of the SceneObject
 */
public class Shape extends SceneObject {

    public double intersect(Ray ray) { return Double.NaN; }
    public boolean isHit() { return true; }
}

