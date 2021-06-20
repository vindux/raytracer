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
import scene.Shape;
import scene.camera.Camera;
import scene.light.Light;
import scene.material.Lambert;
import ui.Window;
import utils.*;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.image.BufferedImage;
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
		float rayTValue = ray.getT();
		if (rayTValue == Float.NaN) {
			intersection.setHit(false);
			return intersection;
		}
		intersection.setShape(shape);
		intersection.setHit(hitValue >= 0);
		if (intersection.isHit()) {
			Vec3 rayDefinition = ray.getStartPoint().add(ray.getDirection().multScalar(ray.getT()));
			intersection.setIntersectionPoint(shape.getTransformMatrix().multVec3(rayDefinition, true));
			intersection.setNormal(intersection.getIntersectionPoint().sub(shape.getCenter()));
			intersection.setDistance(intersection.getIntersectionPoint().sub(ray.getStartPoint()).length());
		}
		return intersection;
	}

	/**  This is where our scene is actually ray-traced **/
	public void renderScene(){
		Log.print(this, "Prepare rendering at " + stopTime(tStart));
		// Get height and width properties
		double width = camera.getWidth();
		double height = camera.getHeight();
		float screenHeight = camera.getScreenHeight();
		float screenWidth = camera.getScreenWidth();

		// Set up variables for our raytracing
		float deltaX;
		float deltaY;
		Ray ray;
		Vec3 cameraDirection;
		Intersection intersection;
		Intersection tempIntersection = null;
		ArrayList<Shape> shapeList = mScene.getObjects();
		ArrayList<Light> lightList = mScene.getLights();

		// Prepare materials
		Lambert lambert = new Lambert(RgbColor.WHITE, 0.5f, 0.5f);

		// Iterate through every pixel
		for(int y = 0; y < screenHeight; y++) {
			for (int x = 0; x < screenWidth; x++) {
				float nearest = 99999;
				Shape nearestShape = null;
				RgbColor pixelColor = null;
				RgbColor pixelColorAmbient;
				RgbColor pixelColorDiffuse = null;

				// First transform pixel to world coordinates
				deltaX = (float) ((2*(x+0.5)/screenWidth-1)*(width/2));
				deltaY = (float) ((2*(y+0.5)/screenHeight-1)*-(height/2));

				// Create the ray
				cameraDirection = camera.calculateDirection(deltaX,-deltaY);
				ray = new Ray(camera.getCameraPosition(), cameraDirection, 1);

				// Find nearest shape in the scene
				for (Shape shape : shapeList ) {
					// Invert matrix and ray for transformed shape
					Matrix4x4 inverse = shape.getTransformMatrix().invert();
					ray.setDirection(inverse.multVec3(ray.getDirection(), false));
					ray.setStartPoint(inverse.multVec3(ray.getDirection(), true));

					double discriminant = shape.intersect(ray);
					intersection = intersect(discriminant, ray, shape);

					// If we hit the shape we compare the distance with the current nearest shape
					if (intersection.isHit()) {
						if (intersection.getDistance() <= nearest) {
							nearestShape = shape;
							nearest = intersection.getDistance();
							tempIntersection = intersection;
						}
					}
				}
				// Depending on the material we set the color on hit
				if (tempIntersection != null && tempIntersection.isHit() && nearestShape != null) {
					for (int i = 0; i < lightList.size(); i++) {
						switch (nearestShape.getMaterial()) {
							case "normal":
								pixelColor = RgbColor.WHITE;
								break;
							case "lambert":
								// For every light we sum up the pixel colors
								pixelColorAmbient = lambert.getAmbient();
								pixelColorDiffuse = (pixelColorDiffuse == null)
										? lambert.getDiffuse(lightList.get(i), tempIntersection)
										: pixelColorDiffuse.add(lambert.getDiffuse(lightList.get(i), tempIntersection));
								// Then we add the sum to the ambient
								if (i == lightList.size()-1)
									pixelColor = (pixelColorDiffuse != null)
											? pixelColorAmbient.add(pixelColorDiffuse)
											: pixelColorAmbient;
								break;
							default:
								// do nothing
						}
					}
					mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), pixelColor, new Vec2(x, y));
				}
			}
		}
		mRenderWindow.exportRendering("1",1,1,true);
		Log.print(this, "Finished rendering at " + stopTime(tStart));
	}
}
