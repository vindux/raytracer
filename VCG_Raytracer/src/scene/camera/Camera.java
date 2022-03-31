package scene.camera;

import scene.SceneObject;
import utils.algebra.Vec3;

/**
 * Camera class
 * Specifies camera and properties of the portion we see at the end
 **/
public class Camera extends SceneObject {

    private final Vec3 cameraPosition;
    private final Vec3 lookAt;
    private final Vec3 userUpVector;
    private final Vec3 viewVector;
    private final Vec3 sideVector;
    private final Vec3 cameraUpVector;
    private final Vec3 focalPoint;
    private final float viewAngle;
    private final float focalLength;
    private final double width;
    private final double height;
    private final double aspect_ratio;
    private final float screenHeight;
    private final float screenWidth;

    public Vec3 getCameraPosition() {
        return cameraPosition;
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
        this.height = 2 * Math.tan(Math.toRadians(viewAngle)/2) * focalLength;
        this.width = aspect_ratio*height;
        this.focalPoint = cameraPosition.add((viewVector.sub(cameraPosition)).normalize().multScalar(focalLength));
    }

    /**
     * Method that calculates the direction of our viewpoint
     **/
    public Vec3 calculateDestination(float x, float y, float _width, float _height) {
        Vec3 destinationVector;
        Vec3 globalCoordinates = new Vec3(0, 0, 0);

        globalCoordinates.x = (float) ((2 * (x + 0.5) / _width - 1) * (this.width/2));
        globalCoordinates.y = (float) ((2 * (y + 0.5) / _height - 1) * (-1) * this.height/2);
        globalCoordinates.z = focalPoint.z;

        destinationVector = focalPoint.add(sideVector.multScalar(globalCoordinates.x).add(cameraUpVector.multScalar(globalCoordinates.y)));
        return destinationVector;
    }

}
