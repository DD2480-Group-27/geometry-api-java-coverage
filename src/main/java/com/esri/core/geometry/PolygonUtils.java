/*
 Copyright 1995-2015 Esri

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class PolygonUtils {

	public enum PiPResult {
		PiPOutside, PiPInside, PiPBoundary
	};

	// enum_class PiPResult { PiPOutside = 0, PiPInside = 1, PiPBoundary = 2};
	/**
	 * Tests if Point is inside the Polygon. Returns PiPOutside if not in
	 * polygon, PiPInside if in the polygon, PiPBoundary is if on the border. It
	 * tests border only if the tolerance is greater than 0, otherwise PiPBoundary cannot
	 * be returned. Note: If the tolerance is not 0, the test is more expensive
	 * because it calculates closest distance from a point to each segment.
	 * 
	 * O(n) complexity, where n is the number of polygon segments.
	 */
	public static PiPResult isPointInPolygon2D(Polygon polygon,
			Point inputPoint, double tolerance) {
		int res = PointInPolygonHelper.isPointInPolygon(polygon, inputPoint,
				tolerance);
		if (res == 0)
			return PiPResult.PiPOutside;
		if (res == 1)
			return PiPResult.PiPInside;

		return PiPResult.PiPBoundary;
	}

	public static PiPResult isPointInPolygon2D(Polygon polygon,
			Point2D inputPoint, double tolerance) {
		int res = PointInPolygonHelper.isPointInPolygon(polygon, inputPoint,
				tolerance);
		if (res == 0)
			return PiPResult.PiPOutside;
		if (res == 1)
			return PiPResult.PiPInside;

		return PiPResult.PiPBoundary;
	}

	static PiPResult isPointInPolygon2D(Polygon polygon, double inputPointXVal,
			double inputPointYVal, double tolerance) {
		int res = PointInPolygonHelper.isPointInPolygon(polygon,
				inputPointXVal, inputPointYVal, tolerance);
		if (res == 0)
			return PiPResult.PiPOutside;
		if (res == 1)
			return PiPResult.PiPInside;

		return PiPResult.PiPBoundary;
	}

	/**
	 * Tests if Point is inside the Polygon's ring. Returns PiPOutside if not in
	 * ring, PiPInside if in the ring, PiPBoundary is if on the border. It tests
	 * border only if the tolerance is greater than 0, otherwise PiPBoundary cannot be
	 * returned. Note: If the tolerance is not 0, the test is more expensive
	 * because it calculates closest distance from a point to each segment.
	 * 
	 * O(n) complexity, where n is the number of ring segments.
	 */
	public static PiPResult isPointInRing2D(Polygon polygon, int iRing,
			Point2D inputPoint, double tolerance) {
		MultiPathImpl polygonImpl = (MultiPathImpl) polygon._getImpl();
		int res = PointInPolygonHelper.isPointInRing(polygonImpl, iRing,
				inputPoint, tolerance, null);
		if (res == 0)
			return PiPResult.PiPOutside;
		if (res == 1)
			return PiPResult.PiPInside;

		// return PiPResult.PiPBoundary;
		return PiPResult.PiPInside; // we do not return PiPBoundary. Overwise,
									// we would have to do more complex
									// calculations to differentiat between
									// internal and external boundaries.
	}

	/**
	 * Tests if Point is inside of the any outer ring of a Polygon. Returns
	 * PiPOutside if not in any outer ring, PiPInside if in the any outer ring,
	 * or on the boundary. PiPBoundary is never returned. Note: If the tolerance
	 * is not 0, the test is more expensive because it calculates closest
	 * distance from a point to each segment.
	 * 
	 * O(n) complexity, where n is the number of polygon segments.
	 */
	public static PiPResult isPointInAnyOuterRing(Polygon polygon,
			Point2D inputPoint, double tolerance) {
		int res = PointInPolygonHelper.isPointInAnyOuterRing(polygon,
				inputPoint, tolerance);
		if (res == 0)
			return PiPResult.PiPOutside;
		if (res == 1)
			return PiPResult.PiPInside;

		// return PiPResult.PiPBoundary;
		return PiPResult.PiPInside; // we do not return PiPBoundary. Overwise,
									// we would have to do more complex
									// calculations to differentiat between
									// internal and external boundaries.
	}

	/**
	 * Tests point is inside the Polygon for an array of points. Returns
	 * PiPOutside if not in polygon, PiPInside if in the polygon, PiPBoundary is
	 * if on the border. It tests border only if the tolerance is greater than 0, otherwise
	 * PiPBoundary cannot be returned. Note: If the tolerance is not 0, the test
	 * is more expensive.
	 * 
	 * O(n*m) complexity, where n is the number of polygon segments, m is the
	 * number of input points.
	 */
	public static void testPointsInPolygon2D(Polygon polygon,
			Point2D[] inputPoints, int count, double tolerance,
			PiPResult[] testResults) {
		if (inputPoints.length < count || testResults.length < count)
			throw new IllegalArgumentException();// GEOMTHROW(invalid_argument);

		for (int i = 0; i < count; i++)
			testResults[i] = isPointInPolygon2D(polygon, inputPoints[i],
					tolerance);
	}

	static void testPointsInPolygon2D(Polygon polygon, double[] xyStreamBuffer,
			int pointCount, double tolerance, PiPResult[] testResults) {
		if (xyStreamBuffer.length / 2 < pointCount
				|| testResults.length < pointCount)
			throw new IllegalArgumentException();// GEOMTHROW(invalid_argument);

		for (int i = 0; i < pointCount; i++)
			testResults[i] = isPointInPolygon2D(polygon, xyStreamBuffer[i * 2],
					xyStreamBuffer[i * 2 + 1], tolerance);
	}

	/**
	 * Tests point is inside an Area Geometry (Envelope, Polygon) for an array
	 * of points. Returns PiPOutside if not in area, PiPInside if in the area,
	 * PiPBoundary is if on the border. It tests border only if the tolerance is
	 * greater than 0, otherwise PiPBoundary cannot be returned. Note: If the tolerance is
	 * not 0, the test is more expensive.
	 * 
	 * O(n*m) complexity, where n is the number of polygon segments, m is the
	 * number of input points.
	 */
	public static void testPointsInArea2D(Geometry polygon,
			Point2D[] inputPoints, int count, double tolerance,
			PiPResult[] testResults) {
		if (polygon.getType() == Geometry.Type.Polygon)
			testPointsInPolygon2D((Polygon) polygon, inputPoints, count,
					tolerance, testResults);
		else if (polygon.getType() == Geometry.Type.Envelope) {
			Envelope2D env2D = new Envelope2D();
			((Envelope) polygon).queryEnvelope2D(env2D);
			_testPointsInEnvelope2D(env2D, inputPoints, count, tolerance,
					testResults);
		} else
			throw new GeometryException("invalid_call");// GEOMTHROW(invalid_call);
	}

	public static void testPointsInArea2D(Geometry polygon,
			double[] xyStreamBuffer, int count, double tolerance,
			PiPResult[] testResults) {
		if (polygon.getType() == Geometry.Type.Polygon)
			testPointsInPolygon2D((Polygon) polygon, xyStreamBuffer, count,
					tolerance, testResults);
		else if (polygon.getType() == Geometry.Type.Envelope) {
			Envelope2D env2D = new Envelope2D();
			((Envelope) polygon).queryEnvelope2D(env2D);
			_testPointsInEnvelope2D(env2D, xyStreamBuffer, count, tolerance,
					testResults);
		} else
			throw new GeometryException("invalid_call");// GEOMTHROW(invalid_call);
	}

	static void _testPointsInEnvelope2D(Envelope2D env2D,
                                        Point2D[] inputPoints, int count, double tolerance,
                                        PiPResult[] testResults) {
		if (inputPoints.length < count || testResults.length < count)
			throw new IllegalArgumentException();

		if (env2D.isEmpty()) {
			for (int i = 0; i < count; i++)
				testResults[i] = PiPResult.PiPOutside;
			return;
		}

		Envelope2D envIn = env2D; // note for java port - assignement by value
		envIn.inflate(-tolerance * 0.5, -tolerance * 0.5);
		Envelope2D envOut = env2D;// note for java port - assignement by value
		envOut.inflate(tolerance * 0.5, tolerance * 0.5);
		for (int i = 0; i < count; i++) {
			if (envIn.contains(inputPoints[i]))
				testResults[i] = PiPResult.PiPInside;
			else if (!envOut.contains(inputPoints[i]))
				testResults[i] = PiPResult.PiPOutside;
			else
				testResults[i] = PiPResult.PiPBoundary;
		}
	}

	private static void _testPointsInEnvelope2D(Envelope2D env2D,
			double[] xyStreamBuffer, int pointCount, double tolerance,
			PiPResult[] testResults) {
		if (xyStreamBuffer.length / 2 < pointCount
				|| testResults.length < pointCount)
			throw new IllegalArgumentException();

		if (env2D.isEmpty()) {
			for (int i = 0; i < pointCount; i++)
				testResults[i] = PiPResult.PiPOutside;
			return;
		}

		Envelope2D envIn = env2D; // note for java port - assignement by value
		envIn.inflate(-tolerance * 0.5, -tolerance * 0.5);
		Envelope2D envOut = env2D;// note for java port - assignement by value
		envOut.inflate(tolerance * 0.5, tolerance * 0.5);
		for (int i = 0; i < pointCount; i++) {
			if (envIn
					.contains(xyStreamBuffer[i * 2], xyStreamBuffer[i * 2 + 1]))
				testResults[i] = PiPResult.PiPInside;
			else if (!envIn.contains(xyStreamBuffer[i * 2],
					xyStreamBuffer[i * 2 + 1]))
				testResults[i] = PiPResult.PiPOutside;
			else
				testResults[i] = PiPResult.PiPBoundary;
		}
	}

	static void testPointsOnSegment_(Segment seg, Point2D[] input_points,
			int count, double tolerance, PolygonUtils.PiPResult[] test_results) {
		for (int i = 0; i < count; i++) {
			if (seg.isIntersecting(input_points[i], tolerance))
				test_results[i] = PiPResult.PiPBoundary;
			else
				test_results[i] = PiPResult.PiPOutside;
		}
	}


	static void testPointsOnPolyline2D_(Polyline poly, Point2D[] input_points,
			int count, double tolerance, PolygonUtils.PiPResult[] test_results) {

		// Create another testResults for the coverage call
		PiPResult[] testResultsCopy = new PolygonUtils.PiPResult[count];

		// Call the version with coverage
		testPointsOnPolyline2DWithCoverage(poly, input_points, count, tolerance, testResultsCopy);

		MultiPathImpl mp_impl = (MultiPathImpl) poly._getImpl();
		GeometryAccelerators accel = mp_impl._getAccelerators();
		RasterizedGeometry2D rgeom = null;
		if (accel != null) {
			rgeom = accel.getRasterizedGeometry();
		}

		int pointsLeft = count;
		for (int i = 0; i < count; i++) {
			test_results[i] = PiPResult.PiPInside;// set to impossible value

			if (rgeom != null) {
				Point2D input_point = input_points[i];
				RasterizedGeometry2D.HitType hit = rgeom.queryPointInGeometry(
						input_point.x, input_point.y);
				if (hit == RasterizedGeometry2D.HitType.Outside) {
					test_results[i] = PiPResult.PiPOutside;
					pointsLeft--;
				}
			}
		}

		if (pointsLeft != 0) {
			SegmentIteratorImpl iter = mp_impl.querySegmentIterator();
			while (iter.nextPath() && pointsLeft != 0) {
				while (iter.hasNextSegment() && pointsLeft != 0) {
					Segment segment = iter.nextSegment();
					for (int i = 0; i < count && pointsLeft != 0; i++) {
						if (test_results[i] == PiPResult.PiPInside) {
							if (segment.isIntersecting(input_points[i],
									tolerance)) {
								test_results[i] = PiPResult.PiPBoundary;
								pointsLeft--;
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < count; i++) {
			if (test_results[i] == PiPResult.PiPInside)
				test_results[i] = PiPResult.PiPOutside;
		}
	}

	static void testPointsOnPolyline2DWithCoverage(Polyline poly, Point2D[] input_points,
										int count, double tolerance,
										PolygonUtils.PiPResult[] test_results) {
		int[][] branchCoverage = new int[10][2]; // 2D Coverage array

		MultiPathImpl mp_impl = (MultiPathImpl) poly._getImpl();
		GeometryAccelerators accel = mp_impl._getAccelerators();
		RasterizedGeometry2D rgeom = null;

		// Create the directory if it doesn't exist
		File coverageDir = new File("src/test/resources/coverage_report");
		if (!coverageDir.exists()) {
			coverageDir.mkdirs(); // Create the directory structure
		}
		// Initialize the coverage file path
//		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String coverageFilePath = "src/test/resources/coverage_report/report.txt";

		// Initialize the file writer
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(coverageFilePath, true))) {
			writer.write("---------------------------------------------------\n");
			// Branch 1
			if (accel == null) {
				branchCoverage[1][0]++; // B1 True case
				writer.write("Branch 1 reached: accel is null\n");
			} else {
				branchCoverage[1][1]++; // B1 False case
				writer.write("Branch 1 reached: accel is not null\n");
				rgeom = accel.getRasterizedGeometry();
			}

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


			// Output coverage summary to the file
			writer.write("Branch Coverage Summary:\n");
			for (int i = 1; i < branchCoverage.length; i++) {
				writer.write("Branch " + i + ": True case - " + branchCoverage[i][0] + ", False case - " + branchCoverage[i][1] + "\n");
			}
			writer.write("---------------------------------------------------\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Output coverage summary to console
//		System.out.println("Branch Coverage Summary:");
//		for (int i = 1; i < branchCoverage.length; i++) {
//			System.out.println("Branch " + i + ": True case - " + branchCoverage[i][0] + ", False case - " + branchCoverage[i][1]);
//		}
	}

	static void testPointsOnLine2D(Geometry line, Point2D[] input_points,
			int count, double tolerance, PolygonUtils.PiPResult[] test_results) {
		Geometry.Type gt = line.getType();
		if (gt == Geometry.Type.Polyline)
			testPointsOnPolyline2D_((Polyline) line, input_points, count,
					tolerance, test_results);
		else if (Geometry.isSegment(gt.value())) {
			testPointsOnSegment_((Segment) line, input_points, count,
					tolerance, test_results);
		} else
			throw new GeometryException("Invalid call.");
	}

}
