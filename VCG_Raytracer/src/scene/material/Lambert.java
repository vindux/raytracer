package scene.material;

import scene.light.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Lambert extends Material {

    private Vec3 lightPosition;
    private Vec3 lightVector;

    public Lambert(RgbColor _ambientLight, float _ambientCoefficent, RgbColor _diffuseLight, float _diffuseCoefficent, Vec3 _normal, Vec3 _intersectionPoint, Light _light) {
        super(_ambientLight, _diffuseLight, _ambientCoefficent, _diffuseCoefficent, _normal, _intersectionPoint);
        this.lightPosition = _light.getPosition();
        this.lightVector = intersectionPoint.sub(lightPosition);
    }

    public RgbColor getRGB(Light light) {
        RgbColor ambient = mAmbientLight.multScalar(mAmbientCoefficent);
        RgbColor diffuse = (light.getColor().multScalar(mDiffuseCoefficent)).multScalar(normal.scalar(lightVector.normalize()));

        return ambient.add(diffuse);
    }

}
