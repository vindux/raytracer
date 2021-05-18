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
import scene.camera.Camera;
import ui.Window;
import utils.*;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.image.BufferedImage;

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
        //Log.print(this, "Init");
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

    /**  This is where our scene is actually ray-traced **/
    public void renderScene(){
        //Log.print(this, "Prepare rendering at " + String.valueOf(stopTime(tStart)));
        double width = camera.getWidth();
        double height = camera.getHeight();
        int screenHeight = camera.getScreenHeight();
        int screenWidth = camera.getScreenWidth();

        Vec3 direction;
        float deltaX;
        float deltaY;
        RgbColor color;
        Ray ray;
        Vec3 cameraDirection;

        for(int y = screenHeight-1;y>=0;y--) {
            for (int x =0; x <screenWidth; x++) {
                deltaX = (float) ((2*(x+0.5)/screenWidth-1)*(width/2));
                deltaY = (float) ((2*(y+0.5)/screenHeight-1)*(height/2));
                cameraDirection = camera.calculateDirection(deltaX,-deltaY);
                ray = new Ray(cameraDirection);
                direction = ray.calculateRayAt(1);

                direction.x = (direction.x+1)/2;
                direction.y = (direction.y+1)/2;
                direction.z = (direction.z+1)/2;
                color = new RgbColor(direction);

                mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), color, new Vec2(x,y));
            }
        }

        // Here trace the rays ...

        // Primary rays ...

        // Later secondary rays ...

        // Yeeaahhh, raytracing is so much fun ...
    }
}
