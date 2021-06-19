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

    public Material(RgbColor mAmbientLight, RgbColor mDiffuseLight, RgbColor mSpecularLight, float mAmbientCoefficient, float mDiffuseCoefficient, float mSpecularCoefficient, float mSpecularExponent) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseLight = mDiffuseLight;
        this.mSpecularLight = mSpecularLight;
        this.mDiffuseCoefficient = mDiffuseCoefficient;
        this.mAmbientCoefficient = mAmbientCoefficient;
        this.mSpecularCoefficient = mSpecularCoefficient;
        this.mSpecularExponent = mSpecularExponent;
    }

}
