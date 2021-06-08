/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    1. Send primary ray
    2. intersection test with all shapes
    3. if hit:
    3a: send secondary ray to the light source
    3b: 2
        3b.i: if hit:
            - Shape is in the shade
            - Pixel color = ambient value
        3b.ii: in NO hit:
            - calculate local illumination
    4. if NO hit:
        - set background color

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package raytracer;

import ray.Ray;
import scene.Scene;
import scene.SceneObject;
import scene.Shape;
import scene.camera.Camera;
import scene.light.Light;
import scene.material.Lambert;
import ui.Window;
import utils.*;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Raytracer class
 * This is where the rays are sent and the picture renders
 **/
public class Raytracer {

    private BufferedImage mBufferedImage;

    private Scene mScene;
    private Window mRenderWindow;
    private Camera camera;

    private int mMaxRecursions;

    private RgbColor mBackgroundColor;
    private RgbColor mAmbientLight;

    private int mAntiAliasingSamples;

    private boolean mDebug;
    private long tStart;

    /**  Constructor **/
    public Raytracer(Scene _scene, Window _renderWindow, int _recursions, RgbColor _backColor, RgbColor _ambientLight, int _antiAliasingSamples, boolean _debugOn, Camera _camera){
        Log.print(this, "Init");
        mMaxRecursions = _recursions;

        mBufferedImage = _renderWindow.getBufferedImage();

        mAntiAliasingSamples = _antiAliasingSamples;

        mBackgroundColor = _backColor;
        mAmbientLight = _ambientLight;
        mScene = _scene;
        mRenderWindow = _renderWindow;
        mDebug = _debugOn;
        tStart = System.currentTimeMillis();
        camera = _camera;

        this.exportRendering();
    }

    /**  Send the created window to the frame delivered by JAVA to display our result **/
    public void exportRendering(){
        mRenderWindow.exportRendering(String.valueOf(stopTime(tStart)), mMaxRecursions, mAntiAliasingSamples, mDebug);
    }

    /**  Stop time of rendering **/
    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

    /** Create intersection instance **/
    public Intersection intersect(double hitValue, Ray ray, Shape shape) {
        Intersection intersection = new Intersection();
        if (hitValue >= 0)
            intersection.setHit(true);
        else
            intersection.setHit(false);
        intersection.setShape(shape);
        if (intersection.isHit()) {
            intersection.setIntersectionPoint(ray.getDirection().multScalar(ray.getT()));
            intersection.setNormal(intersection.getIntersectionPoint().sub(shape.getCenter()));
        }
        return intersection;
    }

    /**  This is where our scene is actually ray-traced **/
    public void renderScene(){
        Log.print(this, "Prepare rendering at " + String.valueOf(stopTime(tStart)));
        // Get height and width properties
        double width = camera.getWidth();
        double height = camera.getHeight();
        float screenHeight = camera.getScreenHeight();
        float screenWidth = camera.getScreenWidth();
        ArrayList<Light> lights = mScene.getLights();

        // Set up variables for our raytracing
        float deltaX;
        float deltaY;
        RgbColor color;
        Ray ray;
        Vec3 direction;
        Vec3 cameraDirection;
        Vec3 lightVector;

        // Iterate through every pixel
        for(int y = 0; y < screenHeight; y++) {
            for (int x = 0; x < screenWidth; x++) {

                // First transform pixel to world coordinates
                deltaX = (float) ((2*(x+0.5)/screenWidth-1)*(width/2));
                deltaY = (float) ((2*(y+0.5)/screenHeight-1)*-(height/2));

                // Create the ray
                cameraDirection = camera.calculateDirection(deltaX,-deltaY);
                ray = new Ray(camera.getCameraPosition(), cameraDirection, 1);

                // Iterate through every shape in the scene
                for (Shape object : mScene.getObjects() ) {
                    double discriminant = object.intersect(ray);
                    Intersection intersection = intersect(discriminant, ray, object);

                    /*
                     * If we do not hit the object, we paint the background color
                     * If we hit the object, we paint another color
                     */
                    if (intersection.isHit()) {
                        for (Light light : lights) {
                            switch (object.getMaterial()) {
                                case "normal":
                                    mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), RgbColor.YELLOW, new Vec2(x,y));
                                    break;
                                case "lambert":
                                    Lambert lambert = new Lambert(RgbColor.BLACK,0.25f, RgbColor.CYAN, 1f, intersection.getNormal(), intersection.getIntersectionPoint() , light);
                                    mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), lambert.getRGB(light), new Vec2(x,y));
                                    break;
                                default:
                                    //do nothing
                            }
                        }
                    } else {
                        mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), mBackgroundColor, new Vec2(x,y));
                    }
                }
            }
        }
        mRenderWindow.exportRendering("1",1,1,true);
    }
}
