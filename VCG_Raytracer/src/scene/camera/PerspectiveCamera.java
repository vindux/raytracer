package scene.camera;

import utils.algebra.Vec3;

public class PerspectiveCamera extends Camera {

    public PerspectiveCamera(Vec3 _cameraPosition, Vec3 _lookAt, Vec3 _upVector, float _viewAngle, float _focalLength) {
        super(_cameraPosition, _lookAt, _upVector, _viewAngle, _focalLength);
    }
}
