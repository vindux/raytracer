package scene.material;

import utils.RgbColor;

public abstract class Material {

    protected RgbColor mAmbientLight;
    protected RgbColor mDiffuseLight;
    protected RgbColor mSpecularLight;
    protected float mDiffuseCoefficent;
    protected float mAmbientCoefficent;
    protected float mSpecularCoefficent;

    public Material(RgbColor mAmbientLight, float mAmbientCoefficent, float mDiffuseCoefficent) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseCoefficent = mDiffuseCoefficent;
        this.mAmbientCoefficent = mAmbientCoefficent;
    }
}
