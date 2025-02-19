package com.esri.core.geometry

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PolygonUtilsWithCoverage {

    static void testPointsOnPolyline2D_(Polyline poly, Point2D[] input_points,
                                        int count, double tolerance,
                                        PolygonUtils.PiPResult[] test_results) {
        int[][] branchCoverage = new int[10][2]; // 2D Coverage array

        MultiPathImpl mp_impl = (MultiPathImpl) poly._getImpl();
        GeometryAccelerators accel = mp_impl._getAccelerators();
        RasterizedGeometry2D rgeom = null;

        // Output file path
        String coverageFilePath = "branch_coverage.txt";

        // Initialize the file writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(coverageFilePath))) {
            // Branch 1
            if (accel == null) {
                branchCoverage[1][0]++; // B1 True case
                writer.write("Branch 1 reached: accel is null\n");
            } else {
                branchCoverage[1][1]++; // B1 False case
                writer.write("Branch 1 reached: accel is not null\n");
                rgeom = accel.getRasterizedGeometry();

                int pointsLeft = count;
                for (int i = 0; i < count; i++) {
                    test_results[i] = PolygonUtils.PiPResult.PiPInside; // Set to impossible value

                    if (rgeom != null) {
                        // Branch 2
                        branchCoverage[2][0]++; // B2 True case
                        writer.write("Branch 2 reached: rgeom is not null\n");
                        Point2D input_point = input_points[i];
                        RasterizedGeometry2D.HitType hit = rgeom.queryPointInGeometry(
                                input_point.x, input_point.y);

                        // Branch 3
                        if (hit == RasterizedGeometry2D.HitType.Outside) {
                            branchCoverage[3][0]++; // B3 True case
                            writer.write("Branch 3 reached: point is outside\n");
                            test_results[i] = PolygonUtils.PiPResult.PiPOutside;
                            pointsLeft--;
                        } else {
                            branchCoverage[3][1]++; // B3 False case
                            writer.write("Branch 3 reached: point is inside\n");
                        }
                    } else {
                        // Branch 2 False case
                        branchCoverage[2][1]++; // B2 False case
                        writer.write("Branch 2 reached: rgeom is null\n");
                    }
                }

                // Branch 4
                if (pointsLeft != 0) {
                    branchCoverage[4][0]++; // B4 True case
                    writer.write("Branch 4 reached: points left not zero\n");
                    SegmentIteratorImpl iter = mp_impl.querySegmentIterator();
                    while (iter.nextPath() && pointsLeft != 0) {
                        // Branch 5
                        while (iter.hasNextSegment() && pointsLeft != 0) {
                            branchCoverage[5][0]++; // B5 True case
                            writer.write("Branch 5 reached: next segment available\n");
                            Segment segment = iter.nextSegment();
                            for (int i = 0; i < count && pointsLeft != 0; i++) {
                                // Branch 6
                                if (test_results[i] == PolygonUtils.PiPResult.PiPInside) {
                                    branchCoverage[6][0]++; // B6 True case
                                    writer.write("Branch 6 reached: test result is PiPInside\n");
                                    // Branch 7
                                    if (segment.isIntersecting(input_points[i], tolerance)) {
                                        branchCoverage[7][0]++; // B7 True case
                                        writer.write("Branch 7 reached: segment intersects\n");
                                        test_results[i] = PolygonUtils.PiPResult.PiPBoundary;
                                        pointsLeft--;
                                    } else {
                                        branchCoverage[7][1]++;
                                        writer.write("Branch 7 reached: segment don't intersects\n");// B7 False case
                                    }
                                }
                                else {
                                    branchCoverage[6][1]++; // B6 False case
                                    writer.write("Branch 6 reached: test result is not PiPInside (Outside)\n");
                                }
                            }
                        }
                        branchCoverage[5][1]++; // B5 False case
                        writer.write("Branch 5 reached: next segment not available\n");
                    }
                } else {
                    branchCoverage[4][1]++; // B4 False case
                    writer.write("Branch 4 reached: points left is zero\n");
                }

                // Branch 8
                for (int i = 0; i < count; i++) {
                    if (test_results[i] == PolygonUtils.PiPResult.PiPInside) {
                        branchCoverage[8][0]++; // B8 True case
                        writer.write("Branch 8 reached: result remains PiPInside\n");
                        test_results[i] = PolygonUtils.PiPResult.PiPOutside;
                    } else {
                        branchCoverage[8][1]++; // B8 False case
                        writer.write("Branch 8 reached: result is not PiPInside anymore\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Output coverage summary to console
        System.out.println("Branch Coverage Summary:");
        for (int i = 1; i < branchCoverage.length; i++) {
            System.out.println("Branch " + i + ": True case - " + branchCoverage[i][0] + ", False case - " + branchCoverage[i][1]);
        }
    }
}