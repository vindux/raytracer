/*package scene.material;

import ray.Ray;
import scene.light.Light;
import scene.camera.Camera;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Phong extends Material{

    private float mSpecularCoefficient;
    private float mSpecularExponent;

    /**
     * Constructor
     */
   /* public Phong(RgbColor _ambientLight, float _ambientCoefficient, float _diffuseCoefficient, float _specularCoefficient, float _specularExponent) {
        super(_ambientLight, _ambientCoefficient, _diffuseCoefficient);
        this.mSpecularCoefficient = _specularCoefficient;
        this.mSpecularExponent = _specularExponent;
    }

    public RgbColor getAmbient() {
        return mAmbientLight.multScalar(mAmbientCoefficient);
    }

    public RgbColor getDiffuseSpecular(Light _light, Camera _camera, Intersection _intersection, Ray lightRay) {
        Vec3 viewVector = _camera.getViewVector();
        Vec3 normal = _intersection.getNormal().normalize();
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = lightPosition.sub(_intersection.getIntersectionPoint()).normalize();
        float diffuseScalar = normal.scalar(lightVector);
        Vec3 reflectionVector = normal.multScalar(diffuseScalar).multScalar(2).sub(lightVector);
        float specularScalar = reflectionVector.scalar(_intersection.getInRay().negate());

        if(diffuseScalar <0){
            diffuseScalar = 0;
        }

        if(specularScalar<0){
            specularScalar=0;
        }
        float diffuse = mDiffuseCoefficient*(diffuseScalar);
        float specular = mSpecularCoefficient*((float) Math.pow(specularScalar,mSpecularExponent));

        return _light.getColor().multScalar(diffuse+specular);
    }
} */
