package scene.material;

import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Phong extends Material{

    private RgbColor mSpecularCoefficient;
    private float mSpecularExponent;

    /**
     * Constructor
     */
    public Phong(RgbColor _ambientLight, RgbColor _ambientCoefficient, RgbColor _diffuseCoefficient, RgbColor _specularCoefficient, float _specularExponent) {
        super(_ambientLight, _ambientCoefficient, _diffuseCoefficient);
        this.mSpecularCoefficient = _specularCoefficient;
        this.mSpecularExponent = _specularExponent;
    }

    public RgbColor getDiffuseSpecular(Light _light, Intersection _intersection) {
        Vec3 normal = _intersection.getNormal();
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = lightPosition.sub(_intersection.getIntersectionPoint()).normalize();
        float diffuseScalar = lightVector.scalar(normal);
        Vec3 reflectionVector = normal.multScalar(diffuseScalar).multScalar(2f).sub(lightVector);
        float specularScalar = reflectionVector.scalar(_intersection.getIntersectionRay().getDirection().negate());

        if(diffuseScalar < 0){
            diffuseScalar = 0;
        }

        if(specularScalar < 0){
            specularScalar=0;
        }

        RgbColor diffuse = mDiffuseCoefficient.multScalar(diffuseScalar);
        RgbColor specular = mSpecularCoefficient.multScalar((float) Math.pow(specularScalar,mSpecularExponent));

        return _light.getColor().multRGB(diffuse.add(specular));
    }

    public RgbColor getColor(Light _light, Intersection _intersection) {
        return getDiffuseSpecular(_light, _intersection);
    }
}
