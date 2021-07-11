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
import scene.material.Material;
import scene.shape.Shape;
import scene.camera.Camera;
import scene.light.Light;
import ui.Window;
import utils.*;
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
	private ArrayList<Shape> shapeList;
	private ArrayList<Light> lightList;
	private int counter = 0;
	private float currentIndex = 0;

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
		shapeList = mScene.getObjects();
		lightList = mScene.getLights();
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
	public Ray setupRay(float x, float y) {
		Ray ray = new Ray(camera.getCameraPosition(),
				new Vec3(0, 0, 0),
				new Vec3(0, 0, 0),
				0.0f);
		ray.setDestinationPoint(camera.calculateDestination(x, y, camera.getScreenWidth(), camera.getScreenHeight()));
		ray.setDirection(ray.getDestinationPoint().sub(ray.getStartPoint()).normalize());

		return ray;
	}

	public RgbColor calculateColor(Intersection intersection) {
		// Depending on the material calculate the pixel color
		RgbColor calculatedColor = null;
		RgbColor pixelColor = null;

		if (intersection != null && intersection.isHit() && intersection.getShape() != null) {
			Material shapeMaterial = intersection.getShape().getMaterial();
			if(shapeMaterial.isReflective() && counter < mMaxRecursions) {
				counter++;
				Ray reflectionRay = shapeMaterial.calculateReflection(intersection);
				Intersection nextIntersection = getNearest(reflectionRay, intersection.getShape(), 9999f);
				pixelColor = calculateColor(nextIntersection);
			} else if (shapeMaterial.isRefractive() && counter < mMaxRecursions) {
				counter++;
				currentIndex = shapeMaterial.getRefractiveIndex();
				pixelColor = calculateRefraction(intersection);
			} else {
				for (Light light : lightList) {
					Ray lightRay = new Ray(intersection.getIntersectionPoint(), light.getPosition(), light.getPosition().sub(intersection.getIntersectionPoint()).normalize(), 0f);
					if (!inShadow(lightRay, intersection.getShape(), light)) {
						calculatedColor = (calculatedColor == null)
								? shapeMaterial.getColor(light, intersection)
								: calculatedColor.add(shapeMaterial.getColor(light, intersection));
					} else {
						return shapeMaterial.getAmbient();
					}
				}
				RgbColor pixelColorAmbient = shapeMaterial.getAmbient();
				pixelColor = (calculatedColor != null)
						? pixelColorAmbient.add(calculatedColor)
						: pixelColorAmbient;
			}
		}
		counter = 0;
		return pixelColor;
	}

	public Ray invertRay(Ray _ray, Shape _shape) {
		Ray invertedRay = new Ray(
				new Vec3(0,0,0),
				new Vec3(0,0,0),
				new Vec3(0,0,0),
				0f);
		invertedRay.setDirection(_ray.getDirection());
		Vec3 invertedStartPoint = _shape.getTransformMatrix().invert().multVec3(_ray.getStartPoint(), true);
		invertedRay.setStartPoint(invertedStartPoint);

		return invertedRay;
	}

	/** Get nearest intersection with shape **/
	public Intersection getNearest(float x, float y) {
		float nearest = 99999;
		Intersection nearestIntersection = null;

		Ray ray = setupRay(x,y);
		for (Shape shape : shapeList) {
			Ray invertedRay = invertRay(ray, shape);
			Intersection intersection = new Intersection();
			intersection.setIntersectionRay(invertedRay);
			intersection.setShape(shape);
			intersection.intersect();
			intersection.setIntersectionPoint();
			intersection.setNormal();

			if (intersection.isHit()) {
				if (intersection.getDistance() <= nearest) {
					nearestIntersection = intersection;
					nearest = intersection.getDistance();
				}
			}
		}
		return nearestIntersection;
	}

	public Intersection getNearest(Ray lightRay, Shape self, float distance) {
		Intersection nearestIntersection = null;
		float nearest = distance;
		Ray ray;

		for (Shape shape : shapeList) {
			if (shape != self) {
				ray = lightRay;
				Ray invertedRay = invertRay(ray, shape);
				Intersection intersection = new Intersection();
				intersection.setIntersectionRay(invertedRay);
				intersection.setShape(shape);
				intersection.intersect();
				intersection.setIntersectionPoint();
				intersection.setNormal();

				// If we hit the shape we compare the distance with the current nearest shape
				if (intersection.isHit()) {
					if (intersection.getDistance() <= nearest) {
						nearestIntersection = intersection;
						nearest = intersection.getDistance();
					}
				}
			}
		}
		return nearestIntersection;
	}

	public boolean inShadow(Ray lightRay, Shape self, Light light) {
		float distance = lightRay.getStartPoint().sub(light.getPosition()).length();
		Intersection intersection = getNearest(lightRay, self, distance);
		if (intersection != null && intersection.isHit()) {
			return true;
		}
		return false;
	}

	public RgbColor calculateRefraction(Intersection _intersection) {
		RgbColor pixelColor;
		Ray refractionRay;
		Material shapeMaterial = _intersection.getShape().getMaterial();
		float entryIndex = 0, exitIndex = 0;
		if (currentIndex == shapeMaterial.getRefractiveIndex()) { // we start inside the sphere
			entryIndex = currentIndex;
			exitIndex = 1f;
		}

		refractionRay = shapeMaterial.calculateRefraction(_intersection, entryIndex, exitIndex);
		Intersection nextIntersection = getNearest(refractionRay, _intersection.getShape(), 9999f);

		pixelColor = calculateColor(nextIntersection).multScalar(shapeMaterial.getRefractionCoefficient());

		return pixelColor;
	}

	/**  This is where our scene is actually ray-traced **/
	public void renderScene(){
		Log.print(this, "Prepare rendering at " + stopTime(tStart));
		// Get height and width properties
		float screenHeight = camera.getScreenHeight();
		float screenWidth = camera.getScreenWidth();

		// Iterate through every pixel
		for(int y = 0; y < screenHeight; y++) {
			for (int x = 0; x < screenWidth; x++) {
				Intersection intersection = getNearest(x,y);
				RgbColor pixelColor = calculateColor(intersection);
				mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), pixelColor, new Vec2(x, y));
			}
		}
		mRenderWindow.exportRendering("1",1,1,true);
		this.exportRendering();
		Log.print(this, "Finished rendering at " + stopTime(tStart));
	}
}


