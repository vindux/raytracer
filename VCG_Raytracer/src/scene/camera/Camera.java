package scene.camera;

import utils.algebra.Vec3;

public class Camera {

    private Vec3 cameraPosition;
    private Vec3 lookAt;
    private Vec3 upVector;
    private Float viewAngle;
    private Float focalLength;
    private Vec3 viewVector;

    public Camera(Vec3 _cameraPosition, Vec3 _lookAt, Vec3 _upVector, float _viewAngle, float _focalLength) {
        this.cameraPosition = _cameraPosition;
        this.lookAt = _lookAt;
        this.upVector = _upVector;
        this.viewAngle = _viewAngle;
        this.focalLength = _focalLength;
    }

    public Vec3 calculateDestinationPoint() {
        viewVector = lookAt.sub(cameraPosition);
        return cameraPosition.add(viewVector);
    }

    public Vec3 calculateDestination(float deltaX, float deltaY) {
        Vec3 destinationPoint = calculateDestinationPoint();
        Vec3 sideVector = viewVector.cross(upVector);
        Vec3 destinationVector = viewVector.add(sideVector.multScalar(deltaX));
        return destinationVector.add(upVector.multScalar(deltaY)).normalize();
    }

}
