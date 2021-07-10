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

import org.w3c.dom.css.RGBColor;
import ray.Ray;
import scene.Scene;
import scene.shape.Shape;
import scene.camera.Camera;
import scene.light.Light;
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
	private ArrayList<Shape> shapeList;
	private ArrayList<Light> lightList;

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
/*	public Intersection intersect(double hitValue, Ray ray, Shape shape) {
		Intersection intersection = new Intersection();
		intersection.setInRay(ray.getDirection());
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
			intersection.setNormal(shape.calculateNormal(intersection.getIntersectionPoint()));
			intersection.setDistance(intersection.getIntersectionPoint().sub(ray.getStartPoint()).length());
		}
		return intersection;
	}

	public Ray setupRay(float x, float y) {
		Ray ray = new Ray(camera.getCameraPosition(),
				new Vec3(0, 0, 0),
				new Vec3(0, 0, 0),
				0.0f);
		ray.setDestinationPoint(camera.calculateDestination(x, y, camera.getScreenWidth(), camera.getScreenHeight()));
		ray.setDirection(ray.getDestinationPoint().sub(ray.getStartPoint()).normalize());

		return ray;
	}

	public Intersection getNearest(float x, float y) {
		Intersection intersection;
		Intersection nearestIntersection = null;
		float nearest = 99999;
		Ray ray;

		for (Shape shape : shapeList ) {
			ray = setupRay(x,y);
			// Invert matrix and ray for transformed shape
			Matrix4x4 inverse = shape.getTransformMatrix().invert();
			//ray.setDirection(inverse.multVec3(ray.getDirection(), false));
			ray.setStartPoint(inverse.multVec3(ray.getStartPoint(), true));

			double discriminant = shape.intersect(ray);
			intersection = intersect(discriminant, ray, shape);

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


	       public Intersection getNearest(Ray lightRay, Shape self, float distance) {
			   Intersection intersection;
			   Intersection nearestIntersection = null;
			   float nearest = distance;
			   Ray ray;

			   for (Shape shape : shapeList) {
				   if (shape != self) {
					   ray = lightRay;
					   // Invert matrix and ray for transformed shape
					   Matrix4x4 inverse = shape.getTransformMatrix().invert();
					   //ray.setDirection(inverse.multVec3(ray.getDirection(), false));
					   ray.setStartPoint(inverse.multVec3(ray.getStartPoint(), true));

					   double discriminant = shape.intersect(ray);
					   intersection = intersect(discriminant, ray, shape);
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

	public RgbColor calculateColor(Intersection intersection) {
		// Depending on the material calculate the pixel color
		RgbColor pixelColorDiffuseSpecular = null;
		RgbColor pixelColor = mAmbientLight;

		if (intersection != null && intersection.isHit() && intersection.getShape() != null) {
			for (Light light : lightList) {
				Ray lightRay = new Ray(intersection.getIntersectionPoint(), light.getPosition(), 1);
				if(!inShadow(lightRay, intersection.getShape(), light)) {
					switch (intersection.getShape().getMaterial().toString().toLowerCase()) {
						case "lambert":
							pixelColorDiffuseSpecular = (pixelColorDiffuseSpecular == null)
									? intersection.getShape().getMaterial().getDiffuseSpecular(light, intersection, lightRay)
									: pixelColorDiffuseSpecular.add(intersection.getShape().getMaterial().getDiffuseSpecular(light, intersection, lightRay));
							break;
						case "phong":
							// For every light we sum up the pixel colors
							pixelColorDiffuseSpecular = (pixelColorDiffuseSpecular == null)
									? intersection.getShape().getMaterial().getDiffuseSpecular(light, camera, intersection, lightRay)
									: pixelColorDiffuseSpecular.add(intersection.getShape().getMaterial().getDiffuseSpecular(light, camera, intersection, lightRay));
						default:
							// do nothing
					}
				}
			}
			RgbColor pixelColorAmbient = intersection.getShape().getMaterial().getAmbient();
			pixelColor = (pixelColorDiffuseSpecular != null)
					? pixelColorAmbient.add(pixelColorDiffuseSpecular)
					: pixelColorAmbient;

		}
		return pixelColor;
	}

   public boolean inShadow(Ray lightRay, Shape self, Light light) {
	   boolean hit = false;
	   float distance = lightRay.getStartPoint().sub(light.getPosition()).length();
	   Intersection intersection = getNearest(lightRay, self, distance);
	   if (intersection != null && intersection.isHit()) {
	   		hit = true;
	   }

	   return hit;
	} */

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
		RgbColor pixelColor = mAmbientLight;

		if (intersection != null && intersection.isHit() && intersection.getShape() != null) {
			for (Light light : lightList) {
				switch (intersection.getShape().getMaterial().toString().toLowerCase()) {
					case "lambert":
						calculatedColor = (calculatedColor == null)
								? intersection.getShape().getMaterial().getColor(light, intersection)
								: calculatedColor.add(intersection.getShape().getMaterial().getColor(light, intersection));
						break;
					default:
						// do nothing
				}
			}
			RgbColor pixelColorAmbient = intersection.getShape().getMaterial().getAmbient();
			pixelColor = (calculatedColor != null)
					? pixelColorAmbient.add(calculatedColor)
					: pixelColorAmbient;

		}
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

	/**  This is where our scene is actually ray-traced **/
	public void renderScene(){
		Log.print(this, "Prepare rendering at " + stopTime(tStart));
		// Get height and width properties
		float screenHeight = camera.getScreenHeight();
		float screenWidth = camera.getScreenWidth();

		// Iterate through every pixel
		for(int y = 0; y < screenHeight; y++) {
			for (int x = 0; x < screenWidth; x++) {
				//Intersection intersection = getNearest(x,y);
				//mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), calculateColor(intersection), new Vec2(x, y));
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
						RgbColor pixelColor = calculateColor(intersection);
						mRenderWindow.setPixel(mRenderWindow.getBufferedImage(), pixelColor, new Vec2(x, y));
					}
				}
			}
		}
		mRenderWindow.exportRendering("1",1,1,true);
		Log.print(this, "Finished rendering at " + stopTime(tStart));
	}
}


