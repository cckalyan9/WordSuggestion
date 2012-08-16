package com.kikin.wordsuggestion.vo.geometry;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 5/31/12
 * Time: 10:32 AM
 * General Vectors.
 */
public class Vectors {

    private final Point2D vector;
    private final double dimensions;


    public Vectors(Point2D vector) {
        this.vector = vector;
        dimensions = calculateDimension();
    }

    public Vectors(Point2D startPosition, Point2D endPosition) {
        this.vector = new Point2D.Double((endPosition.getX() - startPosition.getX()),
                (endPosition.getY() - startPosition.getY()));
        dimensions = calculateDimension();
    }


    public Point2D getVector() {
        return vector;
    }


    private double calculateDimension() {
        return Math.sqrt(Math.pow(this.getVector().getX(), 2) + Math.pow(this.getVector().getY(), 2));
    }

    public double getDimensions() {
        return dimensions;
    }

    @Override
    public String toString() {
        return "Vectors{" +
                "vector=" + vector +
                '}';
    }
}
