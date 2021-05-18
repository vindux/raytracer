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

     Master of Documentation:
     Master of Structure:
     Master of Performance:
     Master of Theory:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

import ray.Ray;
import raytracer.Raytracer;
import scene.camera.Camera;
import ui.Window;
import scene.Scene;
import utils.RgbColor;
import utils.algebra.Vec2;
import utils.algebra.Vec3;

/*
    - THE RAYTRACER -

    TEAM:

    1.
    2.
    3.
    4.
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
    static final RgbColor AMBIENT_LIGHT = null;

    static final boolean USE_AO = false;
    static final int NUMBER_OF_AO_SAMPLES = 0;
    static final float AO_MAX_DISTANCE = 0f;

    /** CAMERA **/

    static final Vec3 CAM_POS = new Vec3(0, 0, 17);
    static final Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static final Vec3 USER_UP_VECTOR = new Vec3(0, 1, 0);

    static final float VIEW_ANGLE = 170f;
    static final float FOCAL_LENGTH = 1f;

    /** DEBUG **/

    static final boolean SHOW_PARAM_LABEL = true;


    /** Initial method. This is where the show begins. **/
    public static void main(String[] args){
        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);
        Camera firstCamera = new Camera(CAM_POS, LOOK_AT, USER_UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH);
        Ray ray = new Ray(CAM_POS, LOOK_AT);
        double aspect_ratio = IMAGE_WIDTH/IMAGE_HEIGHT;
        double img_height = 2*Math.tan(Math.toRadians(170)/2)*FOCAL_LENGTH;
        double img_width = aspect_ratio*img_height;

        Vec3 direction;
        Vec3 rayDirection;
        float deltaX;
        float deltaY;
        RgbColor color;

        //Wofür Aspect ratio???
        //Wofür Angle?

        for(int y = IMAGE_HEIGHT-1;y>=0;--y) {
            for (int x =0; x <IMAGE_WIDTH-1; ++x) {
                deltaX = (float) ((2*(x+0.5)/IMAGE_WIDTH-1)*img_width/2);
                deltaY = (float) ((2*(y+0.5)/IMAGE_HEIGHT-1)*img_height/2);

                direction = firstCamera.calculateDirection(deltaX,-deltaY);
                ray.setDirection(direction);
                rayDirection = ray.calculateRayAt(); //+2 und /2
                rayDirection.x = (rayDirection.x + 2)/2;
                rayDirection.y = (rayDirection.y + 2)/2;
                rayDirection.z = (rayDirection.z + 2)/2;
                color = new RgbColor(rayDirection);

                System.out.println(rayDirection);
                renderWindow.setPixel(renderWindow.getBufferedImage(), color, new Vec2(x,y));
            }
        }
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
    }

    private static void setupCameras(Scene renderScene) {
    }

    private static void setupObjects(Scene renderScene) {
    }

    private static void setupCornellBox(Scene renderScene) {
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
                SHOW_PARAM_LABEL);

        raytracer.renderScene();
    }
}