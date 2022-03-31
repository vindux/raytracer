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
import scene.light.Light;
import scene.material.Material;
import scene.shape.Shape;
import ui.Window;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Raytracer class
 * This is where the rays are sent and the picture renders
 **/
public class Raytracer {

	private final BufferedImage mBufferedImage;

	private final Scene mScene;
	private final Window mRenderWindow;
	private final Camera camera;

	private final int mMaxRecursions;

	private final RgbColor mBackgroundColor;
	private final RgbColor mAmbientLight;
	private final int mLights;
	private final int mThreads;
	private final boolean mDebug;
	private final ArrayList<Shape> shapeList;
	private final ArrayList<Light> lightList;
	private final float currentIndex = 1;
	public int mAntiAliasingSamples;
	private long tStart;
	private double tStop;
	private int currentRecursion = 0;

	/**
	 * Constructor
	 **/
	public Raytracer(Scene _scene, Window _renderWindow, int _recursions, RgbColor _backColor, RgbColor _ambientLight, int _antiAliasingSamples, boolean _debugOn, Camera _camera, int _lights, int _threads) {
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

	/**
	 * Stop time of rendering
	 **/
	private static double stopTime(long tStart) {
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		return tDelta / 1000.0;
	}

	public static <T> Stream<List<T>> batches(List<T> source, int length) {
		if (length <= 0)
			throw new IllegalArgumentException("length = " + length);
		int size = source.size();
		if (size <= 0)
			return Stream.empty();
		int fullChunks = (size - 1) / length;
		return IntStream.range(0, fullChunks + 1).mapToObj(
				n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
	}

	/**
	 * Send the created window to the frame delivered by JAVA to display our result
	 **/
	public void exportRendering() {
		mRenderWindow.exportRendering(String.valueOf(tStop), mMaxRecursions, mAntiAliasingSamples, mLights, mThreads, mDebug);
	}

	/**
	 * Create intersection instance
	 **/
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
			}
			else if (shapeMaterial.isRefractive() && currentRecursion < mMaxRecursions) {
				currentRecursion++;
				pixelColor = calculateRefraction(intersection);
			}
			else {
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

		// Iterate through all lights and calculate colors
		for (Light light : lightList) {
			Ray lightRay = new Ray(_intersection.getIntersectionPoint(), light.getPosition(), light.getPosition().sub(_intersection.getIntersectionPoint()).normalize(), 0f);

			if (!inShadow(lightRay, light)) {
				calculatedColor = (calculatedColor == null)
						? shapeMaterial.getColor(light, _intersection)
						: calculatedColor.add(shapeMaterial.getColor(light, _intersection));
			}
		}

		// Add the calculated color on our ambient if we have a calculated color
		RgbColor pixelColorAmbient = shapeMaterial.getAmbient();
		pixelColor = (calculatedColor != null)
				? pixelColorAmbient.add(calculatedColor)
				: pixelColorAmbient;

		return pixelColor;
	}

	public Ray invertRay(Ray _ray, Shape _shape) {
		Ray invertedRay = new Ray(
				new Vec3(0, 0, 0),
				new Vec3(0, 0, 0),
				new Vec3(0, 0, 0),
				0f);
		invertedRay.setDirection(_ray.getDirection());
		Vec3 invertedStartPoint = _shape.getTransformMatrix().invert().multVec3(_ray.getStartPoint(), true);
		invertedRay.setStartPoint(invertedStartPoint);

		return invertedRay;
	}

	/**
	 * Get nearest intersection with shape for primary ray
	 **/
	public Intersection getNearest(float x, float y) {
		float nearest = 99999;
		Intersection nearestIntersection = null;

		Ray ray = setupRay(x, y);
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

	/**
	 * Get nearest intersection for secondary ray
	 **/
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

	/**
	 * Returns if an object is in the shadow for another object
	 **/
	public boolean inShadow(Ray lightRay, Light light) {
		float distance = lightRay.getStartPoint().sub(light.getPosition()).length();
		Intersection intersection = getNearest(lightRay, distance);
		return intersection != null && intersection.isHit();
	}

	/**
	 *
	 **/
	public RgbColor calculateRefraction(Intersection _intersection) {
		RgbColor pixelColor;
		Ray refractionRay;
		Material shapeMaterial = _intersection.getShape().getMaterial();
		float entryIndex;
		float exitIndex = shapeMaterial.getRefractiveIndex();
		boolean inside = currentIndex == exitIndex;
		entryIndex = currentIndex;

		refractionRay = shapeMaterial.calculateRefraction(_intersection, entryIndex, exitIndex, inside);
		Intersection nextIntersection = getNearest(refractionRay, 9999f);

		pixelColor = calculateColor(nextIntersection).multScalar(shapeMaterial.getRefractionCoefficient());

		return pixelColor;
	}

	/**
	 * Creates threads and puts them into a list
	 * Each thread renders a portion of the screen divided by the count
	 **/
	public ArrayList<Thread> createThreads(int threadCount, boolean random) {
		return (random) ? createThreadsRandom(threadCount) : createThreads(threadCount);
	}

	public ArrayList<Thread> createThreads(int threadCount) {
		ArrayList<Thread> threads = new ArrayList<>();
		ArrayList<Runnable> runnableList = new ArrayList<>();
		float portion = camera.getScreenHeight() / threadCount;

		for (int i = 0; i < threadCount; i++) {
			int finalI = i;
			runnableList.add(() -> render(portion * finalI, portion * (finalI + 1)));
		}

		for (Runnable runnable : runnableList) {
			threads.add(new Thread(runnable));
		}
		return threads;
	}

	public void render(float _minHeight, float _maxHeight) {
		for (float y = _minHeight; y < _maxHeight; y++) {
			for (float x = 0; x < camera.getScreenWidth(); x++) {
				Intersection intersection = getNearest(x, y);
				RgbColor pixelColor = calculateColor(intersection);
				mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), pixelColor, new Vec2(x, y));
			}
		}
	}

	public ArrayList<Thread> createThreadsRandom(int threadCount) {
		ArrayList<ArrayList<Point>> pixels = new ArrayList<>();

		float width = camera.getScreenWidth();
		float height = camera.getScreenHeight();

		int chunkSize = 10;

		for (int y = 0; y < height; y += chunkSize) {
			for (int x = 0; x < width; x += chunkSize) {
				ArrayList<Point> pointChunk = new ArrayList<>();
				for (int yy = y; yy < y + chunkSize; yy++) {
					for (int xx = x; xx < x + chunkSize; xx++) {
						pointChunk.add(new Point(xx, yy));
					}
				}
				pixels.add(pointChunk);
			}
		}

		// comments this out to give each thread one long line
		Collections.shuffle(pixels);

		ArrayList<Thread> threads = new ArrayList<>();
		ArrayList<Runnable> runnableList = new ArrayList<>();
		ArrayList<List<ArrayList<Point>>> chunks = new ArrayList<>();
		batches(pixels, pixels.size() / threadCount).forEach(chunks::add);

		// Create runnables, each with a different portion of the screen
		for (List<ArrayList<Point>> chunk : chunks) {
			runnableList.add(() -> renderRandom(chunk));
		}

		for (Runnable runnable : runnableList) {
			threads.add(new Thread(runnable));
		}
		return threads;
	}

	public void renderRandom(List<ArrayList<Point>> chunk) {
		for (ArrayList<Point> chunkPoint : chunk) {
			for (Point pixel : chunkPoint) {
				RgbColor pixelColor = RgbColor.BLACK;
				float x = pixel.x;
				float y = pixel.y;
				float off = 1f / mAntiAliasingSamples;
				float offset = off / 2;
				float xx = x + offset;

				for (int i = 0; i < mAntiAliasingSamples; i++) {
					float yy = y + offset;
					for (int j = 0; j < mAntiAliasingSamples; j++) {
						Intersection intersection = getNearest(xx, yy);
						pixelColor = pixelColor.add(calculateColor(intersection).divideRGB((int) Math.pow(mAntiAliasingSamples, 2)));
						yy += off;
					}
					xx += off;
				}
				mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), pixelColor, new Vec2(x, y));
			}
		}
	}

	/**
	 * This is where our scene is actually ray-traced
	 **/
	public void renderScene() {
//		Log.print(this, "Prepare rendering at " + stopTime(tStart));
		Log.warn(this, "Recursions: " + mMaxRecursions + ", AA Samples: " + mAntiAliasingSamples + "x" + mAntiAliasingSamples + ", Lights: " + mLights + "x" + mLights + ", Threads: " + mThreads);

		tStart = System.currentTimeMillis();

		if (mThreads < 1) {
			Log.error(this, "You can't render with less than 1 thread.");
			System.exit(1);
		}

		ArrayList<Thread> threads = createThreads(mThreads, true);

		// Start each thread from our thread list
		for (Thread thread : threads) {
			thread.start();
		}

		// Wait for the threads to finish
		for (Thread thread : threads) {
			try {
				thread.join();
			}
			catch (Exception e) {
				Log.print(this, Arrays.toString(e.getStackTrace()));
			}
		}
		tStop = stopTime(tStart);
		this.exportRendering();
		Log.print(this, "Finished rendering at " + tStop);
	}
}


