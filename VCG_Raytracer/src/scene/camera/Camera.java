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
    private int screenHeight;
    private int screenWidth;

    public Vec3 getCameraPosition() {
        return cameraPosition;
    }

    public Vec3 getLookAt() {
        return lookAt;
    }

    public Vec3 getUserUpVector() {
        return userUpVector;
    }

    public Vec3 getViewVector() {
        return viewVector;
    }

    public Vec3 getSideVector() {
        return sideVector;
    }

    public Vec3 getCameraUpVector() {
        return cameraUpVector;
    }

    public Float getViewAngle() {
        return viewAngle;
    }

    public Float getFocalLength() {
        return focalLength;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public Camera(Vec3 _cameraPosition, Vec3 _lookAt, Vec3 _userUpVector, float _viewAngle, float _focalLength, int _screenWidth, int _screenHeight) {
        this.cameraPosition = _cameraPosition;
        this.lookAt = _lookAt;
        this.userUpVector = _userUpVector;
        this.viewAngle = _viewAngle;
        this.focalLength = _focalLength;
        this.viewVector = lookAt.sub(cameraPosition).normalize();
        this.sideVector = viewVector.cross(userUpVector).normalize();
        this.cameraUpVector = sideVector.cross(viewVector).normalize();
        this.screenHeight = _screenHeight;
        this.screenWidth = _screenWidth;
        this.aspect_ratio = screenWidth/screenHeight;
        this.height = 2*(Math.tan(Math.toRadians(viewAngle)/2))*focalLength;
        this.width = aspect_ratio*height;
    }

    public Vec3 calculateDirection(float deltaX, float deltaY) {
        destinationVector = viewVector.add(sideVector.multScalar(deltaX));
        return destinationVector.add(cameraUpVector.multScalar(deltaY));
    }

}
