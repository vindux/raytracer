package scene;

import scene.light.Light;
import scene.light.PointLight;
import utils.RgbColor;
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

    // Store all lights in an array list
    private ArrayList<Light> lights = new ArrayList<>();

    /** Constructor **/
    public Scene() {
        Log.print(this, "Init");
    }

    public ArrayList<Shape> getObjects() { return objects; }
    public ArrayList<Light> getLights() { return lights; }

    public void createSphere(Vec3 _center, int _radius) {
        Sphere sphere = new Sphere(_center, _radius);
        objects.add(sphere);
    }

    public void createPointLight(Vec3 _position, RgbColor _intensity){
        PointLight pointlight = new PointLight(_position, _intensity);
        lights.add(pointlight);
    }

}
