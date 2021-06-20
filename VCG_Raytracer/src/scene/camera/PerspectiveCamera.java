package scene.camera;

import utils.algebra.Vec3;

/**
 * Perspective camera class
 */
public class PerspectiveCamera extends Camera {

    /** Constructor **/
    public PerspectiveCamera(Vec3 _cameraPosition, Vec3 _lookAt, Vec3 _upVector, float _viewAngle, float _focalLength, int _screenWidth, int _screenHeight) {
        super(_cameraPosition, _lookAt, _upVector, _viewAngle, _focalLength, _screenWidth, _screenHeight);
    }
}
