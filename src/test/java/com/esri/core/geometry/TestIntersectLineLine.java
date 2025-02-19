package com.esri.core.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestIntersectLineLine {
    
    @Test
    public void testIntersectionPointsNotNull()
    {
        Line line1 = new Line(0, 0, 3, 3);
        Line line2 = new Line(1, 0, 2, 3 );
        double tolerance = 10;
        Point2D[] intersectionPoints = new Point2D[100];
        Point2D params = Line._intersectHelper1(line1, line2, tolerance);
        intersectionPoints[0] = line1.getCoord2D(params.x);
        intersectionPoints[1] = line1.getCoord2D(3);
        intersectionPoints[2] = line1.getCoord2D(2);
        intersectionPoints[3] = line1.getCoord2D(1);

        int IPnotNull = Line._intersectLineLine(line1, line2, intersectionPoints, null, null, tolerance);
  
        Point2D[] intersectionPoints2 = new Point2D[100];

        int IPNull = Line._intersectLineLine(line1, line2, intersectionPoints2, null, null, tolerance);

        assertEquals(IPnotNull, IPNull);
    }

}
