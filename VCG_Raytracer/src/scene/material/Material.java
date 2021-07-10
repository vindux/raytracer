package scene.material;

import ray.Ray;
import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

/**
 * Base class for materials
 */
public abstract class Material {

    protected RgbColor mAmbientLight;
    protected RgbColor mDiffuseCoefficient;
    protected RgbColor mAmbientCoefficient;
    protected RgbColor mAmbient;

    /**
     * Constructor
     */
    public Material(RgbColor _mAmbientLight, RgbColor _mAmbientCoefficient, RgbColor _mDiffuseCoefficient) {
        this.mAmbientLight = _mAmbientLight;
        this.mAmbientCoefficient = _mAmbientCoefficient;
        this.mDiffuseCoefficient = _mDiffuseCoefficient;
        this.mAmbient = _mAmbientLight.multRGB(mAmbientCoefficient);
    }

    // Returning the name of the material
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public RgbColor getAmbient() { return this.mAmbient; }
    public RgbColor getColor(Light light, Intersection intersection) {return  RgbColor.WHITE;}
    public Ray calculateReflection(Intersection _intersection) {
        return new Ray(
            new Vec3(0,0,0),
            new Vec3(0,0,0),
            new Vec3(0,0,0),
            0f);
    }
    public abstract boolean isReflective();
}
