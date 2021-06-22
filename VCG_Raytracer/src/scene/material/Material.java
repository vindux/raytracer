package scene.material;

import scene.camera.Camera;
import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;

/**
 * Base class for materials
 */
public class Material {

    protected RgbColor mAmbientLight;
    protected float mDiffuseCoefficient;
    protected float mAmbientCoefficient;

    /**
     * Constructor
     */
    public Material(RgbColor mAmbientLight, float mAmbientCoefficient, float mDiffuseCoefficient) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseCoefficient = mDiffuseCoefficient;
        this.mAmbientCoefficient = mAmbientCoefficient;
    }

    public  RgbColor getAmbient() {return  RgbColor.WHITE;}

    // Returning the name of the material
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public RgbColor getDiffuse(Light light, Intersection tempIntersection) {return  RgbColor.WHITE;}

    public RgbColor getDiffuseSpecular(Light light, Camera camera, Intersection tempIntersection) {return  RgbColor.WHITE;}
}
