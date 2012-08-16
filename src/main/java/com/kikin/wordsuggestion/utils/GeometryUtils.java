package com.kikin.wordsuggestion.utils;

import com.google.common.collect.Lists;
import com.kikin.wordsuggestion.vo.geometry.Vectors;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradhepraju
 * Date: 8/15/12
 * Time: 11:08 AM
 * Geometry utils. Used for calculating distances between bounding boxes and touch points
 */
public class GeometryUtils {


    public static double calculateDistanceBetweenLineAndPoint(Line2D line, Point2D c, boolean segment) {

        Vectors base = new Vectors(line.getP1(), line.getP2());
        Vectors side = new Vectors(line.getP2(), c);
        if (segment) {
            double dotProduct = VectorOperations.dotProduct(base, side);
            if (dotProduct > 0) {
                return side.getDimensions();
            } else {

                Vectors otherBase = new Vectors(line.getP2(), line.getP1());
                Vectors otherSide = new Vectors(line.getP1(), c);
                dotProduct = VectorOperations.dotProduct(otherBase, otherSide);
                if (dotProduct > 0) {
                    return otherSide.getDimensions();
                }
            }
        }

        final double crossProduct = VectorOperations.crossProduct(base, side);
        final double distance = crossProduct / base.getDimensions();
        return Math.abs(distance);
    }


    /**
     * Finds the closest point based on the proximity of the point to one of the four edges of the rectangle.
     * <p/>
     * There is an alternate mechanism, where we could use the center point of the rectangle and find the distance between
     * the point and the center, but this could cause problems for longer strings.
     *
     * @param rectangle
     * @param point
     * @return
     */
    public static double calculateDistanceBetweenRectAndPoint(Rectangle2D rectangle, Point2D point) {

        Point2D leftTop = new Point2D.Double(rectangle.getX(), rectangle.getY());
        Point2D rightTop = new Point2D.Double(rectangle.getX() + rectangle.getWidth(), rectangle.getY());
        Point2D leftBottom = new Point2D.Double(rectangle.getX(), rectangle.getY() + rectangle.getHeight());
        Point2D rightBottom = new Point2D.Double(rectangle.getX() + rectangle.getWidth(),
                rectangle.getY() + rectangle.getHeight());


        ArrayList<Line2D.Double> edges = Lists.newArrayList(new Line2D.Double(leftTop, leftBottom), new Line2D.Double(
                leftTop,
                rightTop), new Line2D.Double(leftBottom, rightBottom), new Line2D.Double(rightTop, rightBottom));

        /*   Line2D leftEdge = new Line2D.Double(leftTop, leftBottom);
   Line2D topEdge = new Line2D.Double(leftTop, rightTop);
   Line2D bottomEdge = new Line2D.Double(leftBottom, rightBottom);
   Line2D rightEdge = new Line2D.Double(rightTop, rightBottom);*/
        double minValue = Integer.MAX_VALUE;
        for (Line2D.Double edge : edges) {

            double v = calculateDistanceBetweenLineAndPoint(edge, point, true);
            if (v < minValue) {
                minValue = v;
            }

        }

        return Math.abs(minValue);

    }


}
