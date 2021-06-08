package scene.material;

import utils.RgbColor;
import utils.algebra.Vec3;

public abstract class Material {

    protected RgbColor mAmbientLight;
    protected RgbColor mDiffuseLight;
    protected RgbColor mSpecularLight;
    protected Vec3 normal;
    protected Vec3 intersectionPoint;
    protected float mDiffuseCoefficent;
    protected float mAmbientCoefficent;
    protected float mSpecularCoefficent;

    public Material(RgbColor mAmbientLight, RgbColor mDiffuseLight, float mAmbientCoefficent, float mDiffuseCoefficent, Vec3 normal, Vec3 intersectionPoint) {
        this.mAmbientLight = mAmbientLight;
        this.mDiffuseLight = mDiffuseLight;
        this.normal = normal;
        this.intersectionPoint = intersectionPoint;
        this.mDiffuseCoefficent = mDiffuseCoefficent;
        this.mAmbientCoefficent = mAmbientCoefficent;
    }
}
