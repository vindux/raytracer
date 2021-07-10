package scene;

import scene.light.Light;
import scene.light.PointLight;
import scene.material.Material;
import scene.shape.Shape;
import scene.shape.Sphere;
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

    public void createSphere(Vec3 _center, Float _radius, Material _material) {
        Sphere sphere = new Sphere(_center, _radius, _material);
        objects.add(sphere);
    }
//    public void createPlane(Vec3 _center, Vec3 _normal, Material _material) {
//        Plane plane = new Plane(_center, _normal, _material);
//        objects.add(plane);
//    }
//    public void createSquare(Vec3 _center, Vec3 _normal, float _size, Material _material) {
//        Square square = new Square(_center, _normal, _size, _material);
//        objects.add(square);
//    }

    public void createPointLight(Vec3 _position, RgbColor _intensity){
        PointLight pointlight = new PointLight(_position, _intensity);
        lights.add(pointlight);
    }

}
