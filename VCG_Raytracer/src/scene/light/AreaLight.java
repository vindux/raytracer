package scene.light;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.ArrayList;

/**
 * Adds a n*n area light to the scene
 **/
public class AreaLight extends Light {

	protected float mSize;
	protected int mLightMultiples;
	private final ArrayList<Light> lights = new ArrayList<>();

	/**
	 * Contructor
	 */
	public AreaLight(Vec3 _position, RgbColor _color, float _size, int _lightMultiples) {
		super(_position, _color);
		mColor = _color.divideRGB(_lightMultiples*_lightMultiples);
		this.mSize = _size;
		this.mLightMultiples = _lightMultiples;
		createAreaLight();
	}

	/**
	 * Distribute the point lights across our area light
	 */
	private void createAreaLight() {
		float off = mSize / mLightMultiples;
		float offset = off / 2;
		float x = mPosition.x - mSize/2 - offset;

		for(int i = 0; i < mLightMultiples; i++) {
			x += off;
			float z = mPosition.z - mSize/2 - offset;

			for(int j = 0; j < mLightMultiples; j++) {
				z += off;
				lights.add(new PointLight(new Vec3(x, mPosition.y, z), mColor));
			}
		}
	}

	public ArrayList<Light> getAreaLight() {
		return lights;
	}
}
