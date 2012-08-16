package com.kikin.wordsuggestion.vo;


import com.kikin.wordsuggestion.utils.GeometryUtils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradhepraju
 * Date: 8/15/12
 * Time: 10:31 AM
 * Rects containing word element and its co-ordinates from the touch enabled device.
 */
public class Rects {


    private final String word;

    // Since object could be created (for eg: from json) from the private no-arg constructor, we have
    // to handle possibility of lazy initialization of rectangle. To avoid synchronized block, have made
    // it volatile to avoid memory consistency error. (Which would be rare since object is request scoped).
    private transient volatile Rectangle2D rectangle = null;

    private final double top;

    private final double bottom;

    private final double left;

    private final double right;

    private double distanceFromTouchPoint;

    // No-Org constructor for Json serialization.
    // Warning: Should not be used.
    private Rects() {

        word = null;
        rectangle = null;
        top = 0;
        bottom = 0;
        right = 0;
        left = 0;
    }

    private Rects(String word, double top, double bottom, double left, double right) {
        this.word = word;
        // Assuming origin is at top left corner.
        this.rectangle = new Rectangle2D.Double(left, top, (right - left), (bottom - top));

        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;

    }


    public static Rects createWordFromCoordinates(String text, double top, double bottom, double left, double right) {


        return new Rects(text, top, bottom, left, right);
    }


    public double getDistanceFromTouchPoint() {
        return distanceFromTouchPoint;
    }


    public void calculateDistanceFromTouchPoint(Point2D touchLocation) {

        this.distanceFromTouchPoint = GeometryUtils.calculateDistanceBetweenRectAndPoint(getRectangle(),
                touchLocation);

    }

    public String getWord() {
        return word;
    }

    public Rectangle2D getRectangle() {
        if (rectangle == null) {
            rectangle = new Rectangle2D.Double(left, top, (right - left), (bottom - top));
        }
        return rectangle;
    }

    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }
}
