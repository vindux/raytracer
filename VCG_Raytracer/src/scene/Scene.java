package scene;

import utils.algebra.Vec3;
import utils.io.Log;

import java.util.ArrayList;

/**
 * Scene class
 * Contains SceneObjects
 **/
public class Scene {

    // Store all objects in an array list
    private ArrayList<Shape> objects = new ArrayList<>();

    /** Constructor **/
    public Scene() {
        Log.print(this, "Init");
    }

    public ArrayList<Shape> getObjects() {
        return objects;
    }

    public void createSphere(Vec3 _center, int _radius) {
        Sphere sphere = new Sphere(_center, _radius);
        objects.add(sphere);
    }
}
