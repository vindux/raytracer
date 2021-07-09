package scene.material;

import ray.Ray;
import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

/**
 * Lambert material class
 */
public class Lambert extends Material {

    private RgbColor mAmbient;

    /**
     * Constructor
     */
    public Lambert(RgbColor _ambientLight, float _ambientCoefficient, float _diffuseCoefficient) {
        super(_ambientLight, _ambientCoefficient, _diffuseCoefficient);
        mAmbient = _ambientLight.multScalar(_ambientCoefficient);
    }

    public RgbColor getAmbient() {
        return mAmbient;
    }

    public RgbColor getDiffuseSpecular(Light _light, Intersection _intersection, Ray lightRay) {
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = lightPosition.sub(_intersection.getIntersectionPoint()).normalize();
        Vec3 normal = _intersection.getNormal().normalize();

        return _light.getColor().multScalar(mDiffuseCoefficient).multScalar(normal.scalar(lightVector));
    }
}
