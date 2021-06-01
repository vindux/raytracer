package scene.material;

import utils.algebra.Vec3;

public class Material {

    protected Vec3 mAmbientLight;
    protected Vec3 mDiffuseLight;
    protected Vec3 mSpecularLight;
    protected float mDiffuseCoefficent;
    protected float mAmbientCoefficent;
    protected float mSpecularCoefficent;

    public Material(Vec3 _ambientLight, Vec3 _diffuseLight, Vec3 _specularLight, float _diffuseCoefficent, float _ambientCoefficent, float _specularCoefficent) {
        this.mAmbientLight = _ambientLight;
        this.mDiffuseLight = _diffuseLight;
        this.mSpecularLight = _specularLight;
        this.mDiffuseCoefficent = _diffuseCoefficent;
        this.mAmbientCoefficent = _ambientCoefficent;
        this.mSpecularCoefficent = _specularCoefficent;
    }

}
