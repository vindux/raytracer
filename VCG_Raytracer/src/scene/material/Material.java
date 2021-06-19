package scene.material;

import utils.RgbColor;

public abstract class Material {

    protected RgbColor mAmbientLight;
    protected RgbColor mDiffuseLight;
    protected RgbColor mSpecularLight;
    protected float mDiffuseCoefficient;
    protected float mAmbientCoefficient;
    protected float mSpecularCoefficient;
    protected float mSpecularExponent;

    public Material(RgbColor mAmbientLight, float mAmbientCoefficient, RgbColor mDiffuseLight, float mDiffuseCoefficient) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseLight = mDiffuseLight;
        this.mDiffuseCoefficient = mDiffuseCoefficient;
        this.mAmbientCoefficient = mAmbientCoefficient;
    }

}
