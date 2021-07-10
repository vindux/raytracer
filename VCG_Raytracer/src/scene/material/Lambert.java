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

    /**
     * Constructor
     */
    public Lambert(RgbColor _ambientLight, RgbColor _ambientCoefficient, RgbColor _diffuseCoefficient) {
        super(_ambientLight, _ambientCoefficient, _diffuseCoefficient);
    }

    public RgbColor getDiffuse(Light _light, Intersection _intersection) {
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = lightPosition.sub(_intersection.getIntersectionPoint()).normalize();
        Vec3 normal = _intersection.getNormal();
        float diffuseScalar = lightVector.scalar(normal);

        if (diffuseScalar < 0) {
            diffuseScalar = 0;
        }
        RgbColor diffuse = mDiffuseCoefficient.multScalar(diffuseScalar);

        return _light.getColor().multRGB(diffuse);
    }

    @Override
    public RgbColor getColor(Light _light, Intersection _intersection) {
        return getDiffuse(_light, _intersection);
    }
}
