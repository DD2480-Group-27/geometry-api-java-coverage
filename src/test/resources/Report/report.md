# Report

## Complexity

1. What are your results for five complex functions?
    - Did all methods (tools vs. manual count) get the same result?
    - Are the results clear?
2. Are the functions just complex, or also long?
3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?
5. Is the documentation clear w.r.t. all the possible outcomes?

I choosed the method
`testPointsOnPolyline2D_` in the file `PolygonUtils.java` from package com.esri.core.geometry. The complexity reduction can be expressed with the following metrics

| Sample time | NLOC | CCN | token | PARAM | length |
| --- | --- | --- | --- | --- | --- |
| Before | 43     | 16 | 306 | 5 | 47 |
| After |  |  |  |  |  |

By lizard, the CCN is 16. However, when I manually count it, it is only 12. This ddiscrepancy comes from the logic of the code implementation. You can see from the original code that ig `accel` is null, rgeom is 100% null, thus the nested if statement `if (hit == RasterizedGeometry2D.HitType.Outside)`inside the `if rgeom != null` is never going to be executed. Thus, 4 branches is omitted.

The function tests if points lie on a polyline by:

- First using `RasterizedGeometry2D` to determine the `HitType`
- Then checking segment intersections
- Finally defaulting remaining points to "outside"
- The array`test_results`store the `PiResult` (`PiPResult.PiPBoundary` means on the polyline) for each test point in `input_points`

The function logic is not very complex, it is just the original code tried to put all steps to be in one function that the function have many nested loops and if statements. However, it is worth noting that the part for testing the segment intersections is indeed neccessary to have such nested loops.

There are no exceptions in the function so no exceptions are taken into measurement of the complexity calculations.

There is apparently no documentation for this function, but it does have a short description of the [`PolygonUtils.java`](http://PolygonUtils.java) class. So at the first glance, the logic is not easy to understand, you need to go through the related function calls/ class structures inside this function to understand what it is doing.

## Refactoring

As mentioned in [Complexity](https://www.notion.so/Report-19fe7c813e1c80108cfcd5f6ad73e4fe?pvs=21), the code is having high CNN because it didn’t handle the `rgeom` case well and it puts everything inside the single function.

### Plan

1. Handle the case for empty test points (this is important becuase later when we do the branch for `accel==null` , direct  testing of segment interesection can be performed without having the if statement for `if pointsLeft !=0`
2. Combine the steps into 2 cases: one for `accel != null` , one for `accel == null` (we know that when accel == null, rgeom==null, then pointsLeft will always be == count because we don’t go into the `if rgeom !=null` case.
3. Split the segment intersection processing into a new smaller function named `processSegmentIntersections` so that in the main method, it can be called for both `accel== null` and `not null` cases, reducing a branch.

### Results (for method`testPointsOnPolyline2D_`)

|  | NLOC | CCN | token | PARAM | length |
| --- | --- | --- | --- | --- | --- |
| Before | 43 | 16 | 306 | 5 | 47 |
| After | 27     | 6 | 194 | 5 | 30 |
| `processSegmentIntersections` | 20    | 9 | 145 | 5 | 22 |

As seen, both NLOC and CCN has reduced, in particular the CCN is reduced by 62.5%.

## Coverage