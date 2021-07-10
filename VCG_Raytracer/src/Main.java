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

 <<< YOUR TEAM NAME >>>

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
/*
    - THE RAYTRACER -

    TEAM:

    1. Alexander Funke
    2. Matthias Wolpert
    3. Philipp Reichel
    4. Timur Linden
 */

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    /** RESOLUTION **/
    static final int IMAGE_HEIGHT = 600;
    static final int IMAGE_WIDTH = 800;


    /** CORNELL_BOX_DIMENSION **/
    static final float BOX_DIMENSION = 4f;

    /** RAYTRACER **/
    static final int RECURSIONS = 0;
    static final int ANTI_ALIASING = 1;
    static final boolean USE_SOFT_SHADOWS = false;

    /** LIGHT **/
    static final short LIGHT_DENSITY = 20;
    static final short LIGHT_SAMPLES = 40;

    static final RgbColor BACKGROUND_COLOR = RgbColor.BLACK;

    static final Vec3 LIGHT_POSITION = null;
    static final short AREA_LIGHT_SIZE = 2;

    /** GI **/
    static final boolean USE_GI = false;
    static final int GI_LEVEL = 0;
    static final int GI_SAMPLES = 0;

    static final RgbColor LIGHT_COLOR = null;
    static final RgbColor AMBIENT_LIGHT = RgbColor.BLACK;

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
    static Lambert lambertLightWhite = new Lambert(RgbColor.WHITE, 0.2f, 0);
    static Lambert lambertWhite = new Lambert(RgbColor.WHITE, 0.2f, 0.4f);
    static Lambert lambertBlue = new Lambert(RgbColor.BLUE, 0.9f, 0.3f);
    static Lambert lambertRed = new Lambert(RgbColor.RED, 0.9f, 0.3f);
    static Phong phongBlue = new Phong(RgbColor.BLUE, 0.5f, 0.3f, 0.5f, 50);
    static Phong phongRed = new Phong(RgbColor.RED, 0.5f, 0.3f, 0.5f, 50);

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
        renderScene.createPointLight(new Vec3 (0,1.3f, 0), RgbColor.WHITE);
    }

    private static void setupCameras(Scene renderScene) {
        mCamera = new Camera(CAM_POS, LOOK_AT, USER_UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private static void setupObjects(Scene renderScene) {
        renderScene.createSphere(new Vec3(0,0,0), 1f, phongBlue);
        //renderScene.createSphere(new Vec3(0.5f,-2,2), 1f, phongRed);
    }

    private static void setupCornellBox(Scene renderScene) {
        //renderScene.createSquare(new Vec3(0,1.45f,0), new Vec3(0,-1,0), 1, lambertRed);
        //renderScene.createPlane(new Vec3(0,0,-2), new Vec3(0,0,1), lambertWhite);
        //renderScene.createPlane(new Vec3(0, 1.5f,0), new Vec3(0,-1,0),lambertWhite);
        //renderScene.createPlane(new Vec3(0,-1.5f,0), new Vec3(0,1,0),lambertWhite);
        //renderScene.createPlane(new Vec3(-2f,0,0), new Vec3(1,0,0),lambertRed);
        //renderScene.createPlane(new Vec3(2f,0,0), new Vec3(-1,0,0),lambertBlue);

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
                mCamera);

        raytracer.renderScene();
    }
}