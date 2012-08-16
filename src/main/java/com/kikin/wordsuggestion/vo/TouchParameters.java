package com.kikin.wordsuggestion.vo;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 8/9/12
 * Time: 9:52 AM
 * POJO representing the touch parameters of the Touch based device.
 */
public class TouchParameters {

    private final Point2D touchLocation;
    private final double appliedPressure;
    private final double duration;

    private final transient double intent;

    private static final double MIN_PRESSURE = 1.0;
    private static final double MIN_DURATION = 1.0;


    public TouchParameters(double touchLocationX, double touchLocationY, double appliedPressure, double duration) {
        this.touchLocation = new Point2D.Double(touchLocationX, touchLocationY);
        if (appliedPressure <= 0) {
            appliedPressure = MIN_PRESSURE;
        }

        if (duration <= 0) {
            duration = MIN_DURATION;
        }

        this.appliedPressure = appliedPressure;
        this.duration = duration;
        this.intent = appliedPressure * duration;
    }


    public double getAppliedPressure() {
        return appliedPressure;
    }

    public double getDuration() {
        return duration;
    }

    public double getIntent() {
        return intent;
    }

    public Point2D getTouchLocation() {
        return touchLocation;
    }

    @Override
    public String toString() {
        return "TouchParameters{" +
                "appliedPressure=" + appliedPressure +
                ", duration=" + duration +
                ", intent=" + intent +
                '}';
    }
}
