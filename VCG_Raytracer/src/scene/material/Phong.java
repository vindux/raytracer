package scene.material;

import scene.light.Light;
import scene.camera.Camera;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Phong extends Material{

    public Phong(RgbColor _ambientLight, float _ambientCoefficient, RgbColor _diffuseLight, float _diffuseCoefficient, RgbColor _specularLight, float _specularCoefficient, float _specularExponent) {
        super(_ambientLight, _ambientCoefficient, _diffuseLight, _diffuseCoefficient);
        this.mSpecularLight = _specularLight;
        this.mSpecularCoefficient = _specularCoefficient;
        this.mSpecularExponent = _specularExponent;
    }

    public RgbColor getRGB(Light _light, Camera _camera, Intersection _intersection){
        Vec3 viewVector = _camera.getViewVector();
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = _intersection.getIntersectionPoint().sub(lightPosition).normalize();
        Vec3 normal = _intersection.getNormal().normalize();
        Vec3 reflectionVector = (normal.sub(lightVector)).multScalar(normal.scalar(lightVector)).multScalar(2);
        RgbColor ambient = mAmbientLight.multScalar(mAmbientCoefficient);
        RgbColor diffuse = _light.getColor().multScalar(mDiffuseCoefficient).multScalar(normal.scalar(lightVector));
        RgbColor specular = _light.getColor().multScalar(mSpecularCoefficient).multScalar((float) Math.pow((viewVector.scalar(reflectionVector)),mSpecularExponent));

        return ambient.add(diffuse).add(specular);
    }
}
