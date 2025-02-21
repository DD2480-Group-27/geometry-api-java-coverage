# Report for assignment 3

## Project

Name: geometry-api-java

URL: https://github.com/Esri/geometry-api-java

This API makes it easier to handle and analyze geospatial data in custom applications and big data platforms, improving their ability to work with location information.

## Onboarding experience

The project in itself is just an API so there is not any so-called "running" the project. The project is maven-based so we will be using maven related commands such as
```bash
mvn clean compile
mvn test
```
The project was delivered with java 1.6 but when running mvn clean compile the compilation plugin from maven is not supporting java versions older than JAVA 8 (JDK 1.8) so we changed this in the xml configuration file.

Besides this quick fix the tests were running fine from a mvn clean test with no failing tests.

To confirm the project choice we then went on running a test coverage tool on it, we chose Open Clover from Atlassian. The documentation of the tools was misleading and with help of a TA we got it to work and observed a coverage result of 69.9% with the original repository by running the command ```mvn clean clover:setup test clover:aggregate clover:clover```.

This led us to choose this project but also the fact that this project uses maven (with which our group already worked on earlier assignments) and JUnit4 as testing library (which we were also using in earlier assignment for this same course).

Note that since the addition of Open Clover to the maven configuration file, the test run with mvn clean test are failing because of what it appears to be a conflict between clover and the maven-javadoc-plugin used by the repo for documentation purposes.

A second noticeable point, since we are talking of an API some of its public java interfaces are documented it is however not the case of many classes. There is thus a further difficulty in understanding the code in order to improve its quality and coverage while staying mathematically correct.


## Complexity

1. What are your results for five complex functions?
   * Did all methods (tools vs. manual count) get the same result?
   * Are the results clear?
2. Are the functions just complex, or also long?
3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?
5. Is the documentation clear w.r.t. all the possible outcomes?

### Linus - function: AppendDouble
Results: 
(Entire codebase)
Total nloc: 78292
Automated AvgCCN: 3.4

(StringUtils)
Total nloc: 67
Automated AvgCCN: 6.0

(AppendDouble function)
Total nloc: 29
Automated CCN count: 9.0
Manual CCN count: 9.0

Whether the functions are just complex or also long depends.

The function AppendDouble appends a double value to a StringBuilder with a specified precision. There are no exceptions in the function but lizard would count them towards the CCN. The documentation is not clear, there is no documentation for this function.

### Kristin - function: compareSegments

I chose the function "compareSegments" from geometry-api-java-coverage/src/main/java/com/esri/core/geometry/SweepComparator.java. Lizard calculates the CC to 16. For my manual calculation I used the
formula "M = π - s + 2" from which I get 17, which is one higher than the number produced by Lizard.

The function is quite long compared to the average NLOC (50 vs 15), although far from the longest functions of 250-500 NLOC. However, if you compare the longest of all functions with the most complex of all functions, there seems to be a correlation. In the top 10 lists for both of these properties, 7 of the functions appear in both lists. These functions involve complex operations. They perform geometrical calculations such as cutting a shape of any kind with a shape of any kind. This means they require many many conditional statements for the many possible cases which can arise.

Yes, lizard takes exceptions into account. It considers exceptions as possible branches, which increases the number of branches and thereby increases the CC. However, in my studied function there are no exceptions and so this makes no difference. 

Documentation is more than unclear, actually there is no documentation of these functions! Neither for the top 10 nor compareSegments.

### Henrik - function: doOne

The helper function doOne in the file PointInPolygonHelper.java was studied. Both the manual calculation and lizard got the cyclomatic complexity of the function to 21. Being just 57 lines the function is not particularly long despite the cyclomatic complexity. The function checks if a point is inside a polygon, on the border of a polygon or outside a polygon. As no exceptions exist in function they are not accounted for in the complexity. Most parts of the function are well documented, but the method to check if the point is inside or outside a polygon using the "odd-even rule" could be better documented

### Edgar - function: overlaps_

I chose the method overlaps_ in the file RelationalOperationsMatrix.java from package com.esri.core.geometry. The complexity reduction can be expressed with the following metrics
|Sample time|NLOC|CCN|token|PARAM|length|
|:---:|---:|---:|---:|---:|---:|
|Before|20|21|207|3|25|
|After|9|3|48|3|11|

Since at the start the function was only 25 lines long, I would not say it was a long function but according to lizard, the circular complexity of 21 is higher than the usual (lizard sets a threshold at CCN=15 to differentiate between commonly complex functions and unusually complex ones).
This function is used to identify if a String matches some pattern considering the mathematical dimension of two other parameters (of type Geometry). It is used as a helper method for a bigger function in the same file.
These measurements do not include exceptions nor exception handling because of the nature of the code which is enough low level and does not involve any IO operations.
The documentation is almost none existing but the method corresponds to the single line comment at the line just before which is `Checks whether the scl string is the overlaps relation.`.

For a calculation of the cyclomatic complexity by hand, we can infer that lizard is considering each comparison with `scl.charAt()` as a decision point since they are each a point where the equality `==` can evaluate either to `true` or `false`.
Thus, we have the initial `if (dim_a == dim_b)`, then the `if (dim_a != 1)` and twice the nine decision points with the `chartAt()` call. The formula for the CC can be written as `CC=E−N+2P` with E the number of edges in the control flow graph, N the number of nodes in the same graph and P the number of connected components. This formula can be rewritten as following `CC=(Number of decision points)+1`. Here we have:`CC = 1 + 1 + 2*9 + 1 = 21` before reduction.

### Yoyo - function: testPointsOnPolyline2D_
I chose the method
`testPointsOnPolyline2D_` in the file `PolygonUtils.java` from package com.esri.core.geometry. The complexity  can be expressed with the following metrics

|  | NLOC | CCN | token | PARAM | length |
| --- | --- | --- | --- | --- | --- |
| Before reduction | 43     | 16 | 306 | 5 | 47 |

By lizard, the CCN is 16. However, when I manually count it, it is only 12.

Nodes (N):

1. Start
2. Get mp_impl
3. Get accel
4. Check accel null
5. Get rgeom (if accel not null)
6. Initialize pointsLeft
7. Initialize first loop i
8. Set test_results[i]
9. Check rgeom null
10. Get input_point
11. Query point in geometry
12. Check hit type
13. Update test_results and pointsLeft
14. Check pointsLeft
15. Get iterator
16. Check iter.nextPath
17. Check iter.hasNextSegment
18. Get next segment
19. Inner loop check
20. Check test_results[i]
21. Check intersection
22. Set to boundary
23. Final loop initialization
24. Check test_results[i] final
25. Set to outside
26. End

Total Nodes (N) = 26

Edges (E):

- Main sequence connections: 25
- Additional paths from:
	- accel null check: +1
	- rgeom null check: +1
	- hit type check: +1
	- pointsLeft check: +1
	- iter.nextPath loop: +2 (the and operator)
	- iter.hasNextSegment loop: +2 (the and operator)
	- test_results check in loop: +2 (the and operator)
	- intersection check: +1
	- final check for PiPInside: +1

Total Edges (E) = 37

V(G) = E - N + 2P

= 37 - 26 + 2(1)

= 37 - 26 + 2

= 13

This discrepancy comes from how lizard counts for the while loops and for loops (and the nested while loops) and the and operators inside the while loops. They consider the while and for loops as an extra decision point.

The function tests if points lie on a polyline by:

- First using `RasterizedGeometry2D` to determine the `HitType`
- Then checking segment intersections
- Finally defaulting remaining points to "outside"
- The array`test_results`store the `PiResult` (`PiPResult.PiPBoundary` means on the polyline) for each test point in `input_points`

The function logic is not very complex, it is just the original code tried to put all steps to be in one function that the function have many nested loops and if statements. However, it is worth noting that the part for testing the segment intersections is indeed necessary to have such nested loops.

There are no exceptions in the function so no exceptions are taken into measurement of the complexity calculations.

There is apparently no documentation for this function, but it does have a short description of the [`PolygonUtils.java`](http://PolygonUtils.java) class. So at the first glance, the logic is not easy to understand, you need to go through the related function calls/ class structures inside this function to understand what it is doing.

## Refactoring

### Linus - function: appendDouble

Plan for refactoring complex code:

Clamp precision using Math.max and Math.min to replace two if-statements. Move checking of whether string contains a dot and no exponent to separate helper function.

Estimated impact of refactoring (lower CC, but other drawbacks?).

Lowers CC by 2 (from 9 --> 7). Using clamping reduces readability.

Carried out refactoring (optional, P+):

### Kristin - function: compareSegments

To reduce complexity, I suggest making a helper function called makeEdge for when tryGetCachedEdge returns null. (see below) This would remove nestled if-statements and save many lines. 

```java
	SimpleEdge edgeLeft = tryGetCachedEdge_(leftElm);
		if (edgeLeft == null) { //call makeEdge here instead of following if-statements
			if (m_vertex_1 == left_vertex)
				edgeLeft = m_temp_simple_edge_1;
			else {
				m_vertex_1 = left_vertex;
				edgeLeft = tryCreateCachedEdge_(leftElm);
				if (edgeLeft == null) {
					edgeLeft = m_temp_simple_edge_1;
					m_temp_simple_edge_1.m_value = leftElm;
				}
				initSimpleEdge_(edgeLeft, left_vertex);
			}
		} else
			m_vertex_1 = left_vertex;

		SimpleEdge edgeRight = tryGetCachedEdge_(right_elm);
		if (edgeRight == null) { //call makeEdge here instead of following if-statements
			if (m_vertex_2 == right_vertex)
				edgeRight = m_temp_simple_edge_2;
			else {
				m_vertex_2 = right_vertex;
				edgeRight = tryCreateCachedEdge_(right_elm);
				if (edgeRight == null) {
					edgeRight = m_temp_simple_edge_2;
					m_temp_simple_edge_2.m_value = right_elm;
				}
				initSimpleEdge_(edgeRight, right_vertex);
			}
		} else
			m_vertex_2 = right_vertex; 
```


Rewriting the ternary operator lines:
```java
int kind = edgeLeft.m_b_horizontal ? 1 : 0;
kind |= edgeRight.m_b_horizontal ? 2 : 0; 
```
would also make the code more readable

### Henrik - function: doOne

The function was refactored by putting part of it into a helper function for easier readability.
The reduction can be expressed with the following metrics
|Sample time|NLOC|CCN|token|PARAM|length|
|:---:|---:|---:|---:|---:|---:|
|Before|43|21|348|1|57|
|After|33|16|262|1|43| 

### Edgar - function: overlaps_

The code that highly increases the complexity here is the char by char comparison that can be replaced with null-safe String comparison but also with a simple call to `String::startsWith()` since the `overlaps_()` method is private and only called from a context that ensures non-null String input.

### Yoyo - function: testPointsOnPolyline2D_

As mentioned in Complexity part, the code is having high CNN because it didn’t handle the `rgeom` case well, and it puts everything inside the single function.

Plan

1. Handle the case for empty test points (this is important because later when we do the branch for `accel==null` , direct  testing of segment intersection can be performed without having the if statement for `if pointsLeft !=0`
2. Combine the steps into 2 cases: one for `accel != null` , one for `accel == null` (we know that when accel == null, rgeom==null, then pointsLeft will always be == count because we don’t go into the `if rgeom !=null` case).
3. Split the segment intersection processing into a new smaller function named `processSegmentIntersections` so that in the main method, it can be called for both `accel== null` and `not null` cases, reducing a branch.

Results (for method`testPointsOnPolyline2D_`)

|  | NLOC | CCN | token | PARAM | length |
| --- | --- | --- | --- | --- | --- |
| Before | 43 | 16 | 306 | 5 | 47 |
| After | 27     | 6 | 194 | 5 | 30 |
| `processSegmentIntersections` | 20    | 9 | 145 | 5 | 22 |

As seen, both NLOC and CCN has reduced, in particular the CCN is reduced by 62.5%.

## Coverage

To start this second part we first generated a general coverage report to know on what parts of the code to focus. For this we used Open Clover as mentioned before. It generates a browsable report with key metrics on a dashboard and an intuitive interface to see class and method related coverage.
As explained above, by the use of one single command the report was generated using all the tests in their last version and giving per-line coverage adding the number of times the given line is indeed executed (0 if never).

### DIY tools

#### Linus

https://github.com/DD2480-Group-27/geometry-api-java-coverage/tree/linus/coverage/appendDouble

##### Evaluation

1. How detailed is your coverage measurement?

The custom coverage measurement tracks the number of times each branch of the function was taken.

2. What are the limitations of your own tool?

It is hard coded for this specific function.

3. Are the results of your tool consistent with existing coverage tools?

Somewhat. My custom coverage tool uses branch coverage while Clover uses a combination of several, branch coverage being one of them. Therefore, Clover has a higher coverage percentage than my too

#### Kristin

My DIY tool is not very high quality, but it produces results in line with clover and works for the specific function which I have been working on. It is hardcoded into the function I am testing coverage of (intersectLineLine). At the beginning of the file I created "static boolean[] covered = new boolean[52]", and at each branch I set covered[*branch-number*] to true if the branch is taken. On return, I print a result to the file "coverage.txt" of all branches *not* covered.

##### Evaluation

1. How detailed is your coverage measurement?

It is clear for the specific function, but it *only* covers this function. It does not say in which test the coverage happens or any further details. So it is basically a bare minimum tool.

2. What are the limitations of your own tool?

As mentioned, it is completely limited to this situation and not very useful outside of it.

3. Are the results of your tool consistent with existing coverage tools?

Yes! (with clover at least)


#### Henrik
The DIY coverage tool for isPointInRing() was implemented as a static array of boolean values that turn true if the branch has been visited. The summarized results from all tests in the testPointInRing() to see what branches were reached and which weren't, this array is then printed in the test results. 

##### Evaluation

1. How detailed is your coverage measurement?

The measurement consists of which branches has been taken. The coverage is entirely up to the user as you have to manually add coverage points which is also limited to what branches has been reached and nothing more. In this case, the method contains 15 key points where the coverage reports if the branch has been reached.

2. What are the limitations of your own tool?

The biggest limitation is that it is completely manual and for now only covers one function, but the coverage also only reveals which checkpoints has been reached and not how many "if" checks were between each checkpoint and how much code or functionality each checkpoint covers.

3. Are the results of your tool consistent with existing coverage tools?

The result in OpenClover is not consistent with my DIY coverage, OpenClover does not detect it at all. But VSCode and the DIY coverage tools says that half the branches are cover compared to none before additional tests were added

#### Edgar

I implemented a DIY coverage tool as shown in the patch that can be created with the command below.
```bash
git format-patch -1 350dea23460a53c1cecf86c4eaff275bd8cf949e
```
It works by logging into a file and then map the log file into a report with a python script.
The report can be generated with the following commands:
```bash
mvn clean compile
python3 coverage-report.py
```
It will be printed out the report in the terminal

##### Evaluation

1. How detailed is your coverage measurement?

The shown measurement consists of how many times a branch has been taken.
In order to provide a report as readable as possible, it is the java code of method with annotation at the start and end of each branch (including right before return statement).
In this case, the method contains 9 key points where the coverage reports the umber of times the execution went there.

2. What are the limitations of your own tool?

It would not consider ternary operator if there were any and has no handling of java exceptions meaning that if the execution were to stop in the middle of the method, there would be an incoherent value on some branches.
The big drawback of my tool is that it is absolutely not generic. It is constrained to cover the overlaps_() method.

3. Are the results of your tool consistent with existing coverage tools?

The results of my coverage tool are consistent with the ones from Open Clover meaning both of them detect 99 calls of the overlaps_() method and trace them the same way.

#### Yoyo 
I went for the approach for cloning the function but hardcoded the branch record in between the lines of code. The data structure for the coverage is a 2D matrix, with shape (#branches, 2). The first column \[i for 0≤i<#branches\]\[0\] is for true cases, where the if statement is true; and \[i for 0≤i<#branches\]\[1\] are for false cases.

**Output:**

I also used BufferedWriter to output a txt file, which will display the workflow for that function call (e.g. `"Branch 1 reached: accel is null"`) and also the summary of the number of times each branch in true and false cases. There will be only one txt file for all test cases. Filepath is to be `src/test/resources/coverage_report/report.txt`.

##### Evaluation

1. Quality of Coverage Measurement

The quality of my coverage measurement is limited as it does not take into account ternary operators (condition ? yes : no) because there are none in the original code. Additionally, the current implementation focuses solely on `if-else` statements.

2. Limitations of the Tool

The tool is specifically designed for the `testPointsOnPolyline2D_` function, making it hardcoded and inflexible. If the code is modified or refactored, the instrumentation would need to be rewritten to accommodate the changes, limiting its reusability.

3. Consistency with Automated Tools

I used Clover to measure branch coverage, and the results from my DIY coverage tool are consistent with those produced by Clover. Clover provides execution counts for each line of code, aligning with the coverage insights generated by my implementation.


## Coverage improvement

### Linus - added test cases to appendDouble

Report of old coverage: 

Function has 71% total coverage according to Clover.

Branch 0 was taken 0 times
Branch 1 was taken 0 times
Branch 2 was taken 66368 times
Branch 3 was taken 3663 times
Branch 4 was taken 0 times
Branch 5 was taken 0 times
Branch 6 was taken 3663 times
Branch 7 was taken 3663 times

Report of new coverage:

Branch 0 was taken 1 times
Branch 1 was taken 1 times
Branch 2 was taken 66388 times
Branch 3 was taken 3664 times
Branch 4 was taken 1 times
Branch 5 was taken 0 times
Branch 6 was taken 3664 times
Branch 7 was taken 3664 times

Test cases added:

StringUtils.appendDouble(123.456, -1, stringBuilder);
Assert.assertEquals("1e+02", sb.toString());

and

StringUtils.appendDouble(123.456, 18, stringBuilder);
Assert.assertEquals("123.456", sb.toString());

### Kristin - added test cases for intersectLineLine

The coverage for intersectLineLine was 88.7 %, there were 6 branches not covered. I added two test cases in an added file called TestIntersectLineLine.java: testIntersectionPointsNotNull() and testParam2Null(). I chose these because some not covered branches were lines like "if(!IntersectionPoints == null)" and "if(param2 == null)". With my added test cases, there are two more branches covered, so now only 4 are not covered. This can be seen using my diy tool and confirmed with clover.

### Henrik - added test cases for isPointInRing()

No prior branch coverage for isPointInRing
Added tests for isPointInRing improving branch coverage in 7/15 test spots
- TestPolygonUtils::testPointInRing

### Edgar - TestRelationalOperationsMatrix
Comments showing the required inputs can be seen added in [commit a9c41b7](https://github.com/DD2480-Group-27/geometry-api-java-coverage/commit/a9c41b787c8f34c52e7c8bfe04a04c445b79b925)

Report of old coverage: [link](https://github.com/DD2480-Group-27/geometry-api-java-coverage/blob/edgar/feat/coverage-overlaps_/coverage-before.txt)

Report of new coverage: [link](https://github.com/DD2480-Group-27/geometry-api-java-coverage/blob/edgar/feat/coverage-overlaps_/coverage-after.txt)

The following [test cases](https://github.com/DD2480-Group-27/geometry-api-java-coverage/blob/edgar/feat/coverage-overlaps_/src/test/java/com/esri/core/geometry/TestRelationalOperationsMatrix.java) have been added:
- TestRelationalOperationsMatrix::testRelateOverlapLines
- TestRelationalOperationsMatrix::testRelateOverlapPoints
- TestRelationalOperationsMatrix::testRelateOverlapPolygons

### Yoyo - added test cases for _testPointsInEnvelope2D

Record of branch coverage before and after adding tests

| Function Name | Total Coverage before | Total Coverage After |
| --- | --- | --- |
| _testPointsInEnvelope2D | 0% | 87.5% |

For the function used in the refactoring section (`testPointsOnPolyline2D_`), the branch coverage is already high (78.4%), and the remaining branches cannot be tested unless complex setup is done which requires full understanding of the Accelerators and RasterizedGeometry class. Therefore, I chose another function in the same class `PolygonUtils.java`

I also chose another function in the same class (`_testPointsInEnvelope2D`), which has 0 tests on it, and do 3 extra test cases for it (one for empty envelope, one for inside envelope case, another for outside envelope case)

The tests need to set up an envelope for testing, but no extra interfaces are needed since the test suite for polygons (`testPolygons` ) exists already.


## Self-assessment: Way of working

Current state according to the Essence standard: In-place

Our team has established clear practices and tools that everyone uses to complete their tasks. At the start of this week's lab, we agreed on the practices and tasks during our first meeting. Each member then worked independently on their assigned tasks. Two days before the presentation, we reconvened to review each other's work, reassigned tasks as needed, and merged everything into a single cohesive report.

So far, we have improved in several areas. The work allocation and workflow for one lab are now clearly defined, which has enhanced our productivity and collaboration. Team members feel more confident in their roles, and the initial meeting has set a solid foundation for task management. Additionally, the practice of reconvening before presentations has helped us identify gaps and strengthen our final reports.

We still have room for improvement regarding consistency in task execution. To address this, we could implement more regular check-ins (probably through zoom since now we only have in-person meetings) throughout the lab process, allowing team members to share progress and challenges more frequently. Additionally, creating shared templates or guidelines for specific tasks could help ensure a more uniform approach and enhance the overall quality of our outputs.

## Overall experience

1. Use of Clover Library: Several team members gained valuable experience using the Clover library for code coverage analysis, helping us measure the effectiveness of our tests and identify areas that needed more attention.
2. Refactoring Techniques: We learned various techniques in refactoring, improving our code quality and maintainability. Understanding how to restructure existing code without changing its functionality was a significant skill development.
3. Writing Specific Test Cases: We also learned how to write specific test cases aimed at achieving branch coverage. This skill enhanced our testing strategy, ensuring that we thoroughly tested different execution paths in our code.