// ************************************************************ //
//                      Hochschule Duesseldorf                  //
//                                                              //
//                     Vertiefung Computergrafik                //
// ************************************************************ //


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    1. Documentation:    Did you comment your code shortly but clearly?
    2. Structure:        Did you clean up your code and put everything into the right bucket?
    3. Performance:      Are all loops and everything inside really necessary?
    4. Theory:           Are you going the right way?

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 <<< Rebooters >>>

     Master of Documentation: Timur Linden
     Master of Structure: Philipp Reichel
     Master of Performance: Matthias Wolpert
     Master of Theory: Alexander Funke

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

import raytracer.Raytracer;
import scene.camera.Camera;
import scene.material.Lambert;
import scene.material.Phong;
import ui.Window;
import scene.Scene;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Main {

    /** RESOLUTION **/
    static final int IMAGE_WIDTH = 800;
    static final int IMAGE_HEIGHT = 600;

    /** RAYTRACER **/
    static final short AREA_LIGHT_SIZE = 4;
    static final int ANTI_ALIASING = 2;
    static final int RECURSIONS = 4;
    static final int THREADS = Runtime.getRuntime().availableProcessors();

    static final RgbColor BACKGROUND_COLOR = RgbColor.BLACK;
    static final RgbColor AMBIENT_LIGHT = RgbColor.WHITE;


    /** GI **/
    static final boolean USE_GI = false;
    static final int GI_LEVEL = 0;
    static final int GI_SAMPLES = 0;
    static final RgbColor LIGHT_COLOR = null;
    static final boolean USE_AO = false;
    static final int NUMBER_OF_AO_SAMPLES = 0;
    static final float AO_MAX_DISTANCE = 0f;


    /** CAMERA **/
    static final Vec3 CAM_POS = new Vec3(0, 0, 5);
    static final Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static final Vec3 USER_UP_VECTOR = new Vec3(0, 1, 0);

    static final float VIEW_ANGLE = 70f;
    static final float FOCAL_LENGTH = 1f;

    static public Camera mCamera;

    /** MATERIALS **/

    /** LAMBERT **/
    static Lambert lambertRed = new Lambert(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0.5f, 0, 0));
    static Lambert lambertBlue = new Lambert(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0, 0, 0.5f));
    static Lambert lambertGreen = new Lambert(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0, 0.5f, 0.0f));
    static Lambert lambertWhite = new Lambert(AMBIENT_LIGHT,
            new RgbColor(0.15f, 0.15f, 0.15f),
            new RgbColor(0.8f,0.8f, 0.8f));
    static Lambert lambertGray = new Lambert(AMBIENT_LIGHT,
            new RgbColor(0.05f, 0.05f, 0.05f),
            new RgbColor(0.05f,0.05f, 0.05f));
    static Lambert lambertSquareWhite = new Lambert(RgbColor.WHITE,
            new RgbColor(1, 1, 1),
            new RgbColor(0,0, 0));
    static Lambert lambertSquareBlack = new Lambert(RgbColor.BLACK,
            new RgbColor(1, 1, 1),
            new RgbColor(0,0, 0));
    static Lambert lambertSquareBrown = new Lambert(new RgbColor(0.6f, 0.3f, 0),
            new RgbColor(0.5f, 0.5f, 0.5f),
            new RgbColor(0,0, 0));

    /** PHONG **/
    static Phong phongGrayReflective = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0.5f,0.5f,0.5f),
            new RgbColor(1f,1f,1f),
            50,
            1f);
    static Phong phongGreen = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0.0f,0.8f,0.0f),
            new RgbColor(0.5f,0.5f,0.5f),
            50,
            0f);
    static Phong phongBlue = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0.0f,0.8f,1.0f),
            new RgbColor(0.5f,0.5f,0.5f),
            50,
            0f);
    static Phong phongPurple = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(0.8f,0.0f,0.8f),
            new RgbColor(0.5f,0.5f,0.5f),
            50,
            0f);
    static Phong phongYellow = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f,0.1f,0.1f),
            new RgbColor(1f,0.9f,0.1f),
            new RgbColor(0.5f,0.5f,0.5f),
            50,
            0f);

    /** REFRACTIVE MATERIALS **/
    static Phong water = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f, 0.1f, 0.1f),
            new RgbColor(0.5f, 0.5f, 0.5f),
            new RgbColor(1f, 1f, 1f),
            50,
            1f,
            1.3f);
    static Phong glass = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f, 0.1f, 0.1f),
            new RgbColor(0.5f, 0.5f, 0.5f),
            new RgbColor(1f, 1f, 1f),
            50,
            1f,
            1.5f);
    static Phong diamond = new Phong(AMBIENT_LIGHT,
            new RgbColor(0.1f, 0.1f, 0.1f),
            new RgbColor(0.5f, 0.5f, 0.5f),
            new RgbColor(1f, 1f, 1f),
            1f,
            1f,
            2.33f);


    /** DEBUG **/
    static final boolean SHOW_PARAM_LABEL = true;

    /** Initial method. This is where the show begins. **/
    public static void main(String[] args){
        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);
        draw(renderWindow);
    }

    /**  Draw the scene using our Raytracer **/
    private static void draw(Window renderWindow){
        Scene renderScene = new Scene();

        setupScene(renderScene);

        raytraceScene(renderWindow, renderScene);
    }

    /** Setup all components that we want to see in our scene **/
    private static void setupScene(Scene renderScene){
        setupCameras(renderScene);

        setupCornellBox(renderScene);

        setupObjects(renderScene);

        setupLights(renderScene);
    }

    private static void setupLights(Scene renderScene) {
        renderScene.createAreaLight(new Vec3 (0, 4.0f, -6), new RgbColor(0.9f, 0.9f, 0.9f), 2f, AREA_LIGHT_SIZE);
    }

    private static void setupCameras(Scene renderScene) {
        mCamera = new Camera(CAM_POS, LOOK_AT, USER_UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private static void setupObjects(Scene renderScene) {
        renderScene.createSphere(new Vec3(-1.5f, -3f, -7f), 1.5f, phongBlue);
        renderScene.createSphere(new Vec3(-1f, -0.5f, -6.5f), 0.55f, phongPurple);
        renderScene.createSphere(new Vec3(0.55f, -0.1f, -7.5f), 0.55f, phongYellow);
        renderScene.createSphere(new Vec3(2, -3.5f, -6f), 1f, glass);
    }

    private static void setupCornellBox(Scene renderScene) {
        renderScene.createSquare(new Vec3(0,4.45f,-6), new Vec3(0,-1,0), 2f, lambertSquareWhite);
        // back
        renderScene.createPlane(new Vec3(0,0,-10), new Vec3(0,0,1), lambertWhite);
        // ceiling
        renderScene.createPlane(new Vec3(0, 4.5f,0), new Vec3(0,-1,0),lambertWhite);
        // ground
        renderScene.createPlane(new Vec3(0,-4.5f,0), new Vec3(0,1,0),lambertWhite);
        // left
        renderScene.createPlane(new Vec3(-6f,0,0), new Vec3(1,0,0),lambertRed);
        // right
        renderScene.createPlane(new Vec3(6f,0,0), new Vec3(-1,0,0),lambertGreen);
        // renderScene.createSquare(new Vec3(5.99f,-2f,-4f), new Vec3(-1,0,0), 4.2f, lambertSquareBrown);
        // renderScene.createSquare(new Vec3(5.98f,-2f,-4f), new Vec3(-1,0,0), 4f, phongGrayReflective);
        // behind camera
        renderScene.createPlane(new Vec3(0,0,6), new Vec3(0,0,-1), lambertGray);
    }

    /** Create our personal renderer and give it all of our items and prefs to calculate our scene **/
    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(
                renderScene,
                renderWindow,
                RECURSIONS,
                BACKGROUND_COLOR,
                AMBIENT_LIGHT,
                ANTI_ALIASING,
                SHOW_PARAM_LABEL,
                mCamera,
                AREA_LIGHT_SIZE,
                THREADS);

        raytracer.renderScene();
    }
}