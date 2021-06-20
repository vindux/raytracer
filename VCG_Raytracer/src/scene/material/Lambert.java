package scene.material;

import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Lambert extends Material {

    protected RgbColor ambient;

    public Lambert(RgbColor _ambientLight, float _ambientCoefficent, float _diffuseCoefficent) {
        super(_ambientLight, _ambientCoefficent, _diffuseCoefficent);
        ambient = _ambientLight.multScalar(_ambientCoefficent);
    }

    public RgbColor getAmbient() {
        return ambient;
    }

    public RgbColor getDiffuse(Light _light, Intersection _intersection) {
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = _intersection.getIntersectionPoint().sub(lightPosition).normalize();
        Vec3 normal = _intersection.getNormal().normalize();

        return _light.getColor().multScalar(mDiffuseCoefficent).multScalar(normal.scalar(lightVector));
        //      Ip              *           kd                   *   (       N       .       L       )

    }
}
