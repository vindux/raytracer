package scene.material;

import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Lambert extends Material {

    public Lambert(RgbColor _ambientLight, float _ambientCoefficent, RgbColor _diffuseLight, float _diffuseCoefficent) {
        super(_ambientLight, _diffuseLight, _ambientCoefficent, _diffuseCoefficent);
    }

    public RgbColor getRGB(Light _light, Intersection _intersection) {
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = _intersection.getIntersectionPoint().sub(lightPosition).normalize();
        Vec3 normal = _intersection.getNormal().normalize();

        RgbColor ambient = mAmbientLight.multScalar(mAmbientCoefficent);
        RgbColor diffuse = _light.getColor().multScalar(mDiffuseCoefficent).multScalar(normal.scalar(lightVector));

        return ambient.add(diffuse);
    }

}
