package com.esri.core.geometry;

import org.junit.Assert;
import org.junit.Test;

public class TestRelationalOperationsMatrix {

    @Test
    public void testRelateOverlapLines() {
        SpatialReference sr = SpatialReference.create(42);
        ProgressTracker pt = new MyProgressTracker();
        Geometry a = new Line(0, 0, 1, 1);
        Geometry b = new Line(0, 0, 1, 1);
        // String relation = "0********"; // crosses
        String relation = "1*T***T**";  // For line-line overlap

        boolean overlapResult = RelationalOperationsMatrix.relate(a, b, sr, relation, pt);
        Assert.assertFalse(overlapResult);
    }

    @Test
    public void testRelateOverlapPoints() {
        SpatialReference sr = SpatialReference.create(42);
        ProgressTracker pt = new MyProgressTracker();
        Geometry a = new Point(0, 0);
        Geometry b = new Point(0, 0);
        String relation = "T*T***T**";  // For area-area and point-point overlap


        boolean overlapResult = RelationalOperationsMatrix.relate(a, b, sr, relation, pt);
        Assert.assertFalse(overlapResult);
    }

    @Test
    public void testRelateOverlapPolygons() {
        SpatialReference sr = SpatialReference.create(42);
        ProgressTracker pt = new MyProgressTracker();
        Geometry a = new Polygon().createInstance();
        Geometry b = new Polygon();
        String relation = "T*T***T**";  // For area-area and point-point overlap

        boolean overlapResult = RelationalOperationsMatrix.relate(a, b, sr, relation, pt);
        Assert.assertFalse(overlapResult);
    }

    static class MyProgressTracker extends ProgressTracker {

        @Override
        public boolean progress(int step, int totalExpectedSteps) {
            return false;
        }
    }
}