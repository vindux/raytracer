package scene.material;

import utils.RgbColor;

/**
 * Base class for materials
 */
public abstract class Material {

    protected RgbColor mAmbientLight;
    protected float mDiffuseCoefficient;
    protected float mAmbientCoefficient;

    public Material(RgbColor mAmbientLight, float mAmbientCoefficient, float mDiffuseCoefficient) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseCoefficient = mDiffuseCoefficient;
        this.mAmbientCoefficient = mAmbientCoefficient;
    }

    // Returning the name of the material
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
