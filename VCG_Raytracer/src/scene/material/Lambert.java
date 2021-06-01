package scene.material;

import scene.light.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Lambert extends Material {

    public Lambert(Vec3 _ambientLight, Vec3 _diffuseLight, Vec3 _specularLight, float _diffuseCoefficent, float _ambientCoefficent, float _specularCoefficent) {
        super(_ambientLight, _diffuseLight, _specularLight, _diffuseCoefficent, _ambientCoefficent, _specularCoefficent);
    }

    public RgbColor getRGB(Light light) {
        Vec3 ambient = mAmbientLight.multScalar(mAmbientCoefficent).normalize();
        Vec3 diffuse = (light.getColor().multScalar(mDiffuseCoefficent)).multRGB(normal.scalar(lightvector.normalize()));
    }

}
