package com.kikin.wordsuggestion.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: pradhepraju
 * Date: 8/15/12
 * Time: 11:35 AM
 * ${END}$
 */
public class GeometryUtilsTest {


    private static Logger logger = LoggerFactory.getLogger(GeometryUtilsTest.class);

    @Test
    public void testCalculateDistance() throws Exception {

        final double distance = GeometryUtils.calculateDistanceBetweenLineAndPoint(new Line2D.Double(
                new Point2D.Double(
                        0, 0), new Point2D.Double(5, 0)),
                new Point2D.Double(6,
                        3), false);

        assertEquals(distance, 3.0);

        logger.info("Exiting testCalculateDistance {}", distance);

    }


    @Test
    public void testCalculateDistanceForLineSegment_PointClosestToB() throws Exception {

        final double distance = GeometryUtils.calculateDistanceBetweenLineAndPoint(new Line2D.Double(
                new Point2D.Double(
                        0, 0), new Point2D.Double(5, 0)),
                new Point2D.Double(6,
                        3), true);

        assertEquals(distance, Math.sqrt(9 + 1));

        logger.info("Exiting testCalculateDistance {}", distance);

    }


    @Test
    public void testCalculateDistanceForLineSegment_PointWithinLineSegment() throws Exception {

        final double distance = GeometryUtils.calculateDistanceBetweenLineAndPoint(new Line2D.Double(
                new Point2D.Double(
                        0, 0), new Point2D.Double(5, 0)),
                new Point2D.Double(3,
                        3), true);

        assertEquals(distance, 3.0);

        logger.info("Exiting testCalculateDistance {}", distance);

    }

    @Test
    public void testCalculateDistanceForLineSegment_PointClosestToA() throws Exception {

        final double distance = GeometryUtils.calculateDistanceBetweenLineAndPoint(new Line2D.Double(
                new Point2D.Double(
                        0, 0), new Point2D.Double(5, 0)),
                new Point2D.Double(-1,
                        3), true);

        assertEquals(distance, Math.sqrt(9 + 1));

        logger.info("Exiting testCalculateDistance {}", distance);

    }


    @Test
    public void testCalculateDistanceBetweenRectangleAndPoint() throws Exception {

        Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 2, 3, 2);
        logger.info(rectangle.toString());
        final double distance = GeometryUtils.calculateDistanceBetweenRectAndPoint(rectangle,
                new Point2D.Double(6,
                        3));

        assertEquals(distance, GeometryUtils.calculateDistanceBetweenLineAndPoint(new Line2D.Double(
                new Point2D.Double(
                        3, 2), new Point2D.Double(3, 4)),
                new Point2D.Double(6,
                        3), true));

        logger.info("Exiting testCalculateDistance {}", distance);

    }


    @Test
    public void testCalculateDistanceBetweenRectangleAndPoint_WithPointInsideRectangle() throws Exception {

        Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 2, 3, 2);
        logger.info(rectangle.toString());
        final double distance = GeometryUtils.calculateDistanceBetweenRectAndPoint(rectangle,
                new Point2D.Double(1,
                        1));

        assertEquals(distance, GeometryUtils.calculateDistanceBetweenLineAndPoint(new Line2D.Double(
                new Point2D.Double(
                        0, 0), new Point2D.Double(3, 0)),
                new Point2D.Double(1,
                        1), true));

        logger.info("Exiting testCalculateDistance {}", distance);

    }
}
