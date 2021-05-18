package scene.camera;

import utils.algebra.Vec3;

public class Camera {

    private Vec3 cameraPosition;
    private Vec3 lookAt;
    private Vec3 userUpVector;
    private Vec3 viewVector;
    private Vec3 sideVector;
    private Vec3 cameraUpVector;
    private Vec3 destinationVector;
    private Float viewAngle;
    private Float focalLength;
    private double width;
    private double height;
    private double aspect_ratio;

    public Camera(Vec3 _cameraPosition, Vec3 _lookAt, Vec3 _userUpVector, float _viewAngle, float _focalLength) {
        this.cameraPosition = _cameraPosition;
        this.lookAt = _lookAt;
        this.userUpVector = _userUpVector;
        this.viewAngle = _viewAngle;
        this.focalLength = _focalLength;
        this.viewVector = lookAt.sub(cameraPosition).normalize();
        this.sideVector = viewVector.cross(userUpVector).normalize();
        this.cameraUpVector = sideVector.cross(viewVector).normalize();
    }

    public Vec3 calculateDirection(float deltaX, float deltaY) {
        destinationVector = viewVector.add(sideVector.multScalar(deltaX));
        return destinationVector.add(cameraUpVector.multScalar(deltaY));
    }

}
