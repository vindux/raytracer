package scene.material;

import ray.Ray;
import scene.light.Light;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Phong extends Material{

    private RgbColor mSpecularCoefficient;
    private float mSpecularExponent;
    private float mReflectionCoefficient;
    private float mRefractionCoefficient;
    private float mIor;

    /**
     * Constructor for basic/reflective phong
     */
    public Phong(RgbColor _ambientLight, RgbColor _ambientCoefficient, RgbColor _diffuseCoefficient, RgbColor _specularCoefficient, float _specularExponent, float _reflectionCoefficient) {
        super(_ambientLight, _ambientCoefficient, _diffuseCoefficient);
        this.mSpecularCoefficient = _specularCoefficient;
        this.mSpecularExponent = _specularExponent;
        this.mReflectionCoefficient = _reflectionCoefficient;
    }

    /**
     * Constructor for refractive phong
     */
    public Phong(RgbColor _ambientLight, RgbColor _ambientCoefficient, RgbColor _diffuseCoefficient, RgbColor _specularCoefficient, float _specularExponent, float _refractionCoefficient, float _ior) {
        super(_ambientLight, _ambientCoefficient, _diffuseCoefficient);
        this.mSpecularCoefficient = _specularCoefficient;
        this.mSpecularExponent = _specularExponent;
        this.mRefractionCoefficient = _refractionCoefficient;
        this.mIor = _ior;
    }

    public boolean isReflective() {
        return this.mReflectionCoefficient > 0;
    }

    public RgbColor getDiffuseSpecular(Light _light, Intersection _intersection) {
        Vec3 normal = _intersection.getNormal();
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = lightPosition.sub(_intersection.getIntersectionPoint()).normalize();
        float diffuseScalar = lightVector.scalar(normal);
        Vec3 reflectionVector = normal.multScalar(diffuseScalar).multScalar(2f).sub(lightVector).normalize();
        float specularScalar = reflectionVector.scalar(_intersection.getIntersectionRay().getDirection().negate());

        if(diffuseScalar < 0){
            diffuseScalar = 0;
        }

        if(specularScalar < 0){
            specularScalar = 0;
        }

        RgbColor diffuse = mDiffuseCoefficient.multScalar(diffuseScalar);
        RgbColor specular = mSpecularCoefficient.multScalar((float) Math.pow(specularScalar,mSpecularExponent));

        return _light.getColor().multRGB(diffuse.add(specular));
    }

    public RgbColor getColor(Light _light, Intersection _intersection) {
        return getDiffuseSpecular(_light, _intersection);
    }

    public Ray calculateReflection(Intersection _intersection) {
        Ray reflectionRay = new Ray(
                new Vec3(0,0,0),
                new Vec3(0,0,0),
                new Vec3(0,0,0),
                0f);

        Ray ray = _intersection.getIntersectionRay();
        Vec3 normal = _intersection.getNormal();
        // Calculate reflection
        float diffuseScalar = normal.scalar(ray.getDirection());
        Vec3 direction = ray.getDirection().sub(normal.multScalar(diffuseScalar).multScalar(2));

        // Set ray properties
        reflectionRay.setStartPoint(_intersection.getIntersectionPoint());
        reflectionRay.setDirection(direction);

        return reflectionRay;
    }

    public Ray calculateRefraction(Intersection _intersection, float _entryIndex, float _exitIndex){
        Ray refractionRay = new Ray(
                new Vec3(0,0,0),
                new Vec3(0,0,0),
                new Vec3(0,0,0),
                0f);

        float entryIndex = _entryIndex;
        float exitIndex = _exitIndex;

        Ray ray = _intersection.getIntersectionRay();
        Vec3 negatedDirection = ray.getDirection().negate();
        Vec3 normal = _intersection.getNormal();

        float snellius = entryIndex / exitIndex;
        float cosAlpha = normal.scalar(negatedDirection.normalize());
        float cosBeta = (float) Math.sqrt(1 - Math.pow(snellius, 2) * (1 - Math.pow(cosAlpha, 2)));

        Vec3 exitDirection = ((normal.multScalar(cosAlpha).sub(negatedDirection.normalize())).multScalar(snellius)).sub(normal.multScalar(cosBeta));
        refractionRay.setStartPoint(_intersection.getIntersectionPoint());
        refractionRay.setDirection(exitDirection);

        return refractionRay;
    }

    public boolean isRefractive() {
        return mRefractionCoefficient != 0;
    }
    public float getRefractionCoefficient() {
        return mRefractionCoefficient;
    }
    public float getRefractiveIndex() {
        return mIor;
    }
}
