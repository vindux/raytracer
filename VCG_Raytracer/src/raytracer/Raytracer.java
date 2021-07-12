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
import java.util.HashMap;

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
	private int mLights;

	private int mAntiAliasingSamples;
	private int mThreads;

	private boolean mDebug;
	private long tStart;
	private ArrayList<Shape> shapeList;
	private ArrayList<Light> lightList;
	private int currentRecursion = 0;
	private float currentIndex = 1;

	/**  Constructor **/
	public Raytracer(Scene _scene, Window _renderWindow, int _recursions, RgbColor _backColor, RgbColor _ambientLight, int _antiAliasingSamples, boolean _debugOn, Camera _camera, int _lights, int _threads){
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

		mThreads = _threads;
		mLights = _lights;

		shapeList = mScene.getObjects();
		lightList = mScene.getLights();
	}

	/**  Send the created window to the frame delivered by JAVA to display our result **/
	public void exportRendering(){
		mRenderWindow.exportRendering(String.valueOf(stopTime(tStart)), mMaxRecursions, mAntiAliasingSamples, mLights, mThreads, mDebug);
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
		RgbColor pixelColor = null;

		if (intersection != null && intersection.isHit() && intersection.getShape() != null) {
			Material shapeMaterial = intersection.getShape().getMaterial();
			if(shapeMaterial.isReflective() && currentRecursion < mMaxRecursions) {
				currentRecursion++;
				Ray reflectionRay = shapeMaterial.calculateReflection(intersection);
				Intersection nextIntersection = getNearest(reflectionRay, 9999f);
				pixelColor = calculateColor(nextIntersection);
			} else if (shapeMaterial.isRefractive() && currentRecursion < mMaxRecursions) {
				currentRecursion++;
				pixelColor = calculateRefraction(intersection);
			} else {
				pixelColor = calculateColorAndShadow(intersection);
			}
		}
		currentRecursion = 0;
		return pixelColor;
	}

	public RgbColor calculateColorAndShadow(Intersection _intersection) {
		RgbColor pixelColor;
		RgbColor calculatedColor = null;
		Material shapeMaterial = _intersection.getShape().getMaterial();

		for (Light light : lightList) {
			Ray lightRay = new Ray(_intersection.getIntersectionPoint(), light.getPosition(), light.getPosition().sub(_intersection.getIntersectionPoint()).normalize(), 0f);
			if (!inShadow(lightRay, light)) {
				calculatedColor = (calculatedColor == null)
						? shapeMaterial.getColor(light, _intersection)
						: calculatedColor.add(shapeMaterial.getColor(light, _intersection));
			}
		}

		RgbColor pixelColorAmbient = shapeMaterial.getAmbient();
		pixelColor = (calculatedColor != null)
				? pixelColorAmbient.add(calculatedColor)
				: pixelColorAmbient;

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

	public Intersection getNearest(Ray lightRay, float distance) {
		Intersection nearestIntersection = null;
		float nearest = distance;
		Ray ray;

		for (Shape shape : shapeList) {
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
		return nearestIntersection;
	}

	public boolean inShadow(Ray lightRay, Light light) {
		float distance = lightRay.getStartPoint().sub(light.getPosition()).length();
		Intersection intersection = getNearest(lightRay,distance);
		return intersection != null && intersection.isHit();
	}

	public RgbColor calculateRefraction(Intersection _intersection) {
		RgbColor pixelColor;
		Ray refractionRay;
		Material shapeMaterial = _intersection.getShape().getMaterial();
		float entryIndex = 1;
		float exitIndex = shapeMaterial.getRefractiveIndex();
		boolean inside = false;
		if (currentIndex == shapeMaterial.getRefractiveIndex()) { // we start inside the sphere
			entryIndex = currentIndex;
			exitIndex = 1f;
			inside = true;
		}

		refractionRay = shapeMaterial.calculateRefraction(_intersection, entryIndex, exitIndex, inside);
		Intersection nextIntersection = getNearest(refractionRay, 9999f);

		pixelColor = calculateColor(nextIntersection).multScalar(shapeMaterial.getRefractionCoefficient());

		return pixelColor;
	}

	/**  This is where our scene is actually ray-traced **/
	public void renderScene() {
		Log.print(this, "Prepare rendering at " + stopTime(tStart));
		ArrayList<Thread> threads = createThreads(mThreads);

		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			try {
				thread.join(60000);
			} catch (Exception e) {
			}
		}
		this.exportRendering();
		Log.print(this, "Finished rendering at " + stopTime(tStart));
	}

	public ArrayList<Thread> createThreads(int threadCount) {
		ArrayList<Thread> threads = new ArrayList<>();
		ArrayList<Runnable> runnableList = new ArrayList<>();
		float portion = camera.getScreenHeight() / threadCount;

		for (int i = 0; i < threadCount; i++) {
			int finalI = i;
			runnableList.add(() -> render(portion*finalI,portion*(finalI +1)));
		}

		for(int i = 0; i < runnableList.size(); i++) {
			threads.add(new Thread(runnableList.get(i)));
		}
		return threads;
	}


	public void render(float _minHeight, float _maxHeight) {
		for(float y = _minHeight; y < _maxHeight; y++) {
			for (float x = 0; x < camera.getScreenWidth(); x++) {
				Intersection intersection = getNearest(x,y);
				RgbColor pixelColor = calculateColor(intersection);
				mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), pixelColor, new Vec2(x, y));
			}
		}
	}
}


