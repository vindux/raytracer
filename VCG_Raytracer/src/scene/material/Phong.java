package scene.material;

import scene.light.Light;
import scene.camera.Camera;
import utils.Intersection;
import utils.RgbColor;
import utils.algebra.Vec3;

public class Phong extends Material{

    public Phong(RgbColor _ambientLight, float _ambientCoefficient, RgbColor _diffuseLight, float _diffuseCoefficient, RgbColor _specularLight, float _specularCoefficient, float _SpecularExponent) {
        super(_ambientLight, _diffuseLight, _ambientCoefficient, _diffuseCoefficient, _specularLight, _specularCoefficient, _SpecularExponent);
    }

    public RgbColor getRGB(Light _light, Camera _camera, Intersection _intersection){
        Vec3 viewVector = _camera.getViewVector();
        Vec3 lightPosition = _light.getPosition();
        Vec3 lightVector = _intersection.getIntersectionPoint().sub(lightPosition).normalize();
        Vec3 normal = _intersection.getNormal().normalize();
        Vec3 reflectionVector = (normal.sub(lightVector)).multScalar(normal.scalar(lightVector)).multScalar(2);

        RgbColor specular = _light.getColor().multScalar(mSpecularCoefficient).multScalar((float) Math.pow((viewVector.scalar(reflectionVector)),mSpecularExponent));

        return specular;
    }
}
