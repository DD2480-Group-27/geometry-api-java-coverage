/*
 Copyright 1995-2017 Esri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 For additional information, contact:
 Environmental Systems Research Institute, Inc.
 Attn: Contracts Dept
 380 New York Street
 Redlands, California, USA 92373

 email: contracts@esri.com
 */

package com.esri.core.geometry;

import junit.framework.TestCase;

import java.util.Arrays;

import org.junit.Test;

public class TestPolygonUtils extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public static void testPointInAnyOuterRing() {
        Polygon polygon = new Polygon();
        // outer ring1
        polygon.startPath(-200, -100);
        polygon.lineTo(200, -100);
        polygon.lineTo(200, 100);
        polygon.lineTo(-190, 100);
        polygon.lineTo(-190, 90);
        polygon.lineTo(-200, 90);

        // hole
        polygon.startPath(-100, 50);
        polygon.lineTo(100, 50);
        polygon.lineTo(100, -40);
        polygon.lineTo(90, -40);
        polygon.lineTo(90, -50);
        polygon.lineTo(-100, -50);

        // island
        polygon.startPath(-10, -10);
        polygon.lineTo(10, -10);
        polygon.lineTo(10, 10);
        polygon.lineTo(-10, 10);

        // outer ring2
        polygon.startPath(300, 300);
        polygon.lineTo(310, 300);
        polygon.lineTo(310, 310);
        polygon.lineTo(300, 310);

        polygon.reverseAllPaths();

        Point2D testPointIn1 = new Point2D(1, 2); // inside the island
        Point2D testPointIn2 = new Point2D(190, 90); // inside, betwen outer
        // ring1 and the hole
        Point2D testPointIn3 = new Point2D(305, 305); // inside the outer ring2
        Point2D testPointOut1 = new Point2D(300, 2); // outside any
        Point2D testPointOut2 = new Point2D(-195, 95); // outside any (in the
        // concave area of outer
        // ring 2)
        Point2D testPointOut3 = new Point2D(99, 49); // outside (in the hole)

        PolygonUtils.PiPResult res;
        // is_point_in_polygon_2D
        res = PolygonUtils.isPointInPolygon2D(polygon, testPointIn1, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        res = PolygonUtils.isPointInPolygon2D(polygon, testPointIn2, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        res = PolygonUtils.isPointInPolygon2D(polygon, testPointIn3, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);

        res = PolygonUtils.isPointInPolygon2D(polygon, testPointOut1, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
        res = PolygonUtils.isPointInPolygon2D(polygon, testPointOut2, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
        res = PolygonUtils.isPointInPolygon2D(polygon, testPointOut3, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPOutside);

        // Ispoint_in_any_outer_ring
        res = PolygonUtils.isPointInAnyOuterRing(polygon, testPointIn1, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        res = PolygonUtils.isPointInAnyOuterRing(polygon, testPointIn2, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        res = PolygonUtils.isPointInAnyOuterRing(polygon, testPointIn3, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);

        res = PolygonUtils.isPointInAnyOuterRing(polygon, testPointOut1, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
        res = PolygonUtils.isPointInAnyOuterRing(polygon, testPointOut2, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
        res = PolygonUtils.isPointInAnyOuterRing(polygon, testPointOut3, 0);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);// inside of outer
        // ring
    }

    @Test
    public static void testPointInPolygonBugCR181840() {
        PolygonUtils.PiPResult res;
        {// pointInPolygonBugCR181840 - point in polygon bug
            Polygon polygon = new Polygon();
            // outer ring1
            polygon.startPath(0, 0);
            polygon.lineTo(10, 10);
            polygon.lineTo(20, 0);

            res = PolygonUtils.isPointInPolygon2D(polygon,
                    Point2D.construct(15, 10), 0);
            assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
            res = PolygonUtils.isPointInPolygon2D(polygon,
                    Point2D.construct(2, 10), 0);
            assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
            res = PolygonUtils.isPointInPolygon2D(polygon,
                    Point2D.construct(5, 5), 0);
            assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        }

        {// CR181840 - point in polygon bug
            Polygon polygon = new Polygon();
            // outer ring1
            polygon.startPath(10, 10);
            polygon.lineTo(20, 0);
            polygon.lineTo(0, 0);

            res = PolygonUtils.isPointInPolygon2D(polygon,
                    Point2D.construct(15, 10), 0);
            assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
            res = PolygonUtils.isPointInPolygon2D(polygon,
                    Point2D.construct(2, 10), 0);
            assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
            res = PolygonUtils.isPointInPolygon2D(polygon,
                    Point2D.construct(5, 5), 0);
            assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        }
    }

    @Test
    public static void testPointInRing() {
        Polygon polygon = new Polygon();
        polygon.startPath(-200, -100);
        polygon.lineTo(200, -100);
        polygon.lineTo(200, 100);
        polygon.lineTo(-190, 100);
        polygon.lineTo(-190, 90);
        polygon.lineTo(-200, 90);

        // hole
        polygon.startPath(-100, 50);
        polygon.lineTo(100, 50);
        polygon.lineTo(100, -40);
        polygon.lineTo(90, -40);
        polygon.lineTo(90, -50);
        polygon.lineTo(-100, -50);

        Point2D testPointIn1 = new Point2D(1, 2);
        Point2D testPointIn2 = new Point2D(100, 200);
        Point2D testPointIn3 = new Point2D(0, 50);
        PolygonUtils.PiPResult res;

        Arrays.fill(PointInPolygonHelper.coverage_arr_isPointInRing, false);


        res = PolygonUtils.isPointInRing2D(polygon, 1, testPointIn1, 1);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        res = PolygonUtils.isPointInRing2D(polygon, 1, testPointIn2, 1);
        assertTrue(res == PolygonUtils.PiPResult.PiPOutside);
        res = PolygonUtils.isPointInRing2D(polygon, 1, testPointIn3, 1);
        assertTrue(res == PolygonUtils.PiPResult.PiPInside);
        System.out.println("Coverage for isPointInRing: " + Arrays.toString(PointInPolygonHelper.coverage_arr_isPointInRing));
        // No tests for quadTree != null
    }

    /*
    Test cases for testPointsOnPolyline2D_
     */
    //Test case 1: when accel is null
    @Test
    public void testPointsOnPolyline2D_whenAccelIsNull() {
        // Create an empty Polyline
        Polyline polyline = new Polyline(); // No segments added
        Point2D[] inputPoints = {new Point2D(5, 5), new Point2D(6, 6)}; // Example points
        PolygonUtils.PiPResult[] testResults = new PolygonUtils.PiPResult[inputPoints.length];
        double tolerance = 0.1;

        // Call the method
        PolygonUtils.testPointsOnPolyline2D_(polyline, inputPoints, inputPoints.length, tolerance, testResults);

        // Assert that all results are set to PiPInside, as no segments were checked
        for (PolygonUtils.PiPResult result : testResults) {
            assertEquals(PolygonUtils.PiPResult.PiPOutside, result);
        }
    }

    // Test case 2: When pointsLeft is 0
    @Test
    public void testPointsOnPolyline2D_whenPointsLeftIsZero() {
        // Create a Polyline with segments
        Polyline polyline = createMockPolylineWithSegments(); // Create a polyline with segments
        Point2D[] inputPoints = {new Point2D(5, 5), new Point2D(6, 6)}; // Example points
        PolygonUtils.PiPResult[] testResults = new PolygonUtils.PiPResult[inputPoints.length];
        double tolerance = 0.1;

        // Initialize results such that they simulate already being processed

        // Simulate the condition where pointsLeft becomes 0
        // Call the method
        PolygonUtils.testPointsOnPolyline2D_(polyline, inputPoints, inputPoints.length, tolerance, testResults);

        // Assert that all results remain as PiPOutside
        for (PolygonUtils.PiPResult result : testResults) {
            assertEquals(PolygonUtils.PiPResult.PiPBoundary, result);
        }
    }

    // Mock method to create a Polyline with segments
    // meaning accel !=null, rgeom is not null
    private Polyline createMockPolylineWithSegments() {
        Polyline polyline = new Polyline();
        // Add segments to the polyline
        polyline.addSegment(new Line(0, 0, 10, 10), true); // Add a segment
        return polyline;
    }

    /*
	Test cases for _testPointsInEnvelope2D
	 */
    //Test case 1: empty envelope
    @Test
    public void testWithEmptyEnvelope() {
        Envelope2D env = new Envelope2D();
        PolygonUtils.PiPResult[] results = new PolygonUtils.PiPResult[1];
        Point2D[] points = {new Point2D(5, 5)}; // Example point

        PolygonUtils._testPointsInEnvelope2D(env, points, 1, 0, results);

        assertEquals(PolygonUtils.PiPResult.PiPOutside, results[0]);
    }

    // Test case 2: Test with a point inside the envelope
    @Test
    public void testWithPointInsideEnvelope() {
        Envelope2D env = new Envelope2D();
        env.setCoords(0, 0, 10, 10); // Define a square envelope
        PolygonUtils.PiPResult[] results = new PolygonUtils.PiPResult[1];
        Point2D[] points = {new Point2D(5, 5)}; // Inside the envelope

        PolygonUtils._testPointsInEnvelope2D(env, points, 1, 0, results);

        assertEquals(PolygonUtils.PiPResult.PiPInside, results[0]);
    }

    // Test case 3: Test with a point outside the envelope
    @Test
    public void testWithPointOutsideEnvelope() {
        Envelope2D env = new Envelope2D();
        env.setCoords(0, 0, 10, 10);
        PolygonUtils.PiPResult[] results = new PolygonUtils.PiPResult[1];
        Point2D[] points = {new Point2D(15, 5)}; // Outside the envelope

        PolygonUtils._testPointsInEnvelope2D(env, points, 1, 0, results);

        assertEquals(PolygonUtils.PiPResult.PiPOutside, results[0]);
    }
}

