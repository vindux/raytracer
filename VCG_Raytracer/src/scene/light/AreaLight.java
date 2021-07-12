package scene.light;

import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.ArrayList;

/**
 * Adds a n*n area light to the scene
 * **/
public class AreaLight extends Light {

	protected float mSize;
	protected int mLightMultiples;
	private ArrayList<Light> lights = new ArrayList<>();

	/** Contructor **/
	public AreaLight(Vec3 _position, RgbColor _color, float _size, int _lightMultiples) {
		super(_position, _color);
		mColor = _color.divideRGB(_lightMultiples*_lightMultiples);
		System.out.println(_color);
		System.out.println(mColor);
		this.mSize = _size;
		this.mLightMultiples = _lightMultiples;
		createAreaLight();
	}

	private void createAreaLight() {
		float off = mSize / mLightMultiples;
		float offset = off / 2;
		float x = mPosition.x - mSize/2 - offset;
		for(int i = 0; i < mLightMultiples; i++) {
			x += off;
			float z = mPosition.z - mSize/2 - offset;
			for(int j = 0; j < mLightMultiples; j++) {
				z += off;
				lights.add(new PointLight(calculatePosition(mPosition, x, z), mColor));
			}
		}
	}

	private Vec3 calculatePosition(Vec3 _position, float x, float z) {
		return new Vec3(x, _position.y, z);
	}

	public ArrayList<Light> getAreaLight() {
		return lights;
	}
}
