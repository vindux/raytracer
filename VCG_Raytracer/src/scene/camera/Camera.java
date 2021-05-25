package scene.camera;

import scene.SceneObject;
import utils.algebra.Vec3;

/**
 * Camera class
 * Specifies camera and properties of the portion we see at the end
 **/

public class Camera extends SceneObject {

    private Vec3 cameraPosition;
    private Vec3 lookAt;
    private Vec3 userUpVector;
    private Vec3 viewVector;
    private Vec3 sideVector;
    private Vec3 cameraUpVector;
    private Vec3 destinationVector;
    private Vec3 focalPoint;
    private float viewAngle;
    private float focalLength;
    private double width;
    private double height;
    private double aspect_ratio;
    private float screenHeight;
    private float screenWidth;

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

    public float getScreenHeight() {
        return screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    /** Constructor **/
    public Camera(Vec3 _cameraPosition, Vec3 _lookAt, Vec3 _userUpVector, float _viewAngle, float _focalLength, float _screenWidth, float _screenHeight) {
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
        System.out.println("ASPECT : " + aspect_ratio);
        this.height = 2*(Math.tan(Math.toRadians(viewAngle)/2))*focalLength;
        this.width = aspect_ratio*height;
        this.focalPoint = cameraPosition.add(viewVector.sub(cameraPosition).multScalar(focalLength).normalize());
    }

    /**
     * Method that calculates the direction of our viewpoint
     **/
    public Vec3 calculateDirection(float deltaX, float deltaY) {
        destinationVector = viewVector.add(sideVector.multScalar(deltaX));
        return destinationVector.add(cameraUpVector.multScalar(deltaY)).add(focalPoint);
    }

}
