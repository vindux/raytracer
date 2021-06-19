package scene.material;

import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Lambert extends Material {

    public Lambert(RgbColor _ambientLight, float _ambientCoefficient, RgbColor _diffuseLight, float _diffuseCoefficient, RgbColor _specularLight, float _specularCoefficient, float _SpecularExponent) {
        super(_ambientLight, _diffuseLight, _ambientCoefficient, _diffuseCoefficient, _specularLight, _specularCoefficient, _SpecularExponent);
    }

    public RgbColor getRGB(Light _light, Intersection _intersection) {
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = _intersection.getIntersectionPoint().sub(lightPosition).normalize();
        Vec3 normal = _intersection.getNormal().normalize();

        RgbColor ambient = mAmbientLight.multScalar(mAmbientCoefficient);
        RgbColor diffuse = _light.getColor().multScalar(mDiffuseCoefficient).multScalar(normal.scalar(lightVector));

        return ambient.add(diffuse);
    }

}
