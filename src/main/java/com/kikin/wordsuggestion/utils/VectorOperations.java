package com.kikin.wordsuggestion.utils;


import com.kikin.wordsuggestion.vo.geometry.Vectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradheepraju
 * Date: 5/31/12
 * Time: 10:38 AM
 * General Vector Operations.
 */
public class VectorOperations {

    public static double crossProduct(Vectors a, Vectors b) {

        return ((a.getVector().getX() * b.getVector().getY()) + (a.getVector().getY() * b.getVector().getX()));

    }


    public static double dotProduct(Vectors a, Vectors b) {

        return ((a.getVector().getX() * b.getVector().getX()) + (a.getVector().getY() * b.getVector().getY()));

    }


    public static double findAngle(Vectors a, Vectors b) {

        final double dotProduct = dotProduct(a, b);
        final double aDimensions = a.getDimensions();
        final double bDimensions = b.getDimensions();
        final double cosAngle = dotProduct / (aDimensions * bDimensions);
        return Math.acos(cosAngle);
    }


    public static double findAngleUsingCrossProduct(Vectors a, Vectors b) {

        final double dotProduct = crossProduct(a, b);
        final double aDimensions = a.getDimensions();
        final double bDimensions = b.getDimensions();
        final double sinAngle = dotProduct / (aDimensions * bDimensions);
        return Math.asin(sinAngle);
    }


}
