package scene.material;

import utils.RgbColor;

public abstract class Material {

    protected RgbColor mAmbientLight;
    protected RgbColor mDiffuseLight;
    protected RgbColor mSpecularLight;
    protected float mDiffuseCoefficent;
    protected float mAmbientCoefficent;
    protected float mSpecularCoefficent;

    public Material(RgbColor mAmbientLight, RgbColor mDiffuseLight, RgbColor mSpecularLight, float mAmbientCoefficent, float mDiffuseCoefficent, float mSpecularCoefficent) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseLight = mDiffuseLight;
        this.mSpecularLight = mSpecularLight;
        this.mDiffuseCoefficent = mDiffuseCoefficent;
        this.mAmbientCoefficent = mAmbientCoefficent;
        this.mSpecularCoefficent = mSpecularCoefficent;
    }
}
