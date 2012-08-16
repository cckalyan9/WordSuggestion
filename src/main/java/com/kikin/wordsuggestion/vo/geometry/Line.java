package com.kikin.wordsuggestion.vo.geometry;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 6/1/12
 * Time: 11:56 AM
 * Representation of a line.
 */
public class Line {

    private final double a;
    private final double b;
    private double c;

    /**
     * Provided two points in line, we should be able to generate the equation for the line.
     *
     * @param first
     * @param second
     */
    public Line(Point2D first, Point2D second) {

        a = second.getY() - first.getY();
        b = first.getX() - second.getX();
        c = a * first.getX() + b * first.getY();

    }

    public Line(double a, double b) {
        this.a = a;
        this.b = b;

    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public Line findPerpendicularLine() {

        return new Line(-b, a);

    }


}
