package scene.material;

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

    public RgbColor getDiffuse(Light _light, Intersection _intersection) {
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = _intersection.getIntersectionPoint().sub(lightPosition).normalize();
        Vec3 normal = _intersection.getNormal().normalize();

        return _light.getColor().multScalar(mDiffuseCoefficient).multScalar(normal.scalar(lightVector));
    }
}
