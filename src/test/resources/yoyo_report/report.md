## Coverage measurement & improvement

### 3.5.1 Task 1: DIY

Implement branch coverage by manual instrumentation of the source code for five functions

with high cyclomatic complexity. Use a separate development branch or repo for this, as this code is

not permanent. The simplest method for this is as follows:

### Task 2: Coverage improvement

Record of branch coverage before adding tests

| _testPointsInEnvelope2D | 202 | 16 | 0% | 8 | 28 | 0% |
| --- | --- | --- | --- | --- | --- | --- |
| testPointsOnPolyline2D_ | 267 | 27 | 0% |  | 11 | 78.4% |

For the function used in the refactoring section (`testPointsOnPolyline2D_`), the branch coverage is already high, therefore I only added two extra test cases to cover the remianing branches (one for the `accel==null` , another for `pointsLeft ==0` & `PiPResult.PipBpundary` case.

I also chose another function in the same class (`_testPointsInEnvelope2D`), which has 0 tests on it, and do 3 extra test cases for it (one for empty envelope, one for inside envelope case, another for outside envelope case)

Both sets of tests need to setup a polyline/ envelope for testing, but no extra interfaces are needed since the test suite for polygons (`testPolygons` ) exists already.