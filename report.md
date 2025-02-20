# Report for assignment 3

## Project

Name: geometry-api-java

URL: https://github.com/Esri/geometry-api-java

This API makes it easier to handle and analyze geospatial data in custom applications and big data platforms, improving their ability to work with location information.

## Onboarding experience

The project in itself is just an API so there is not any so called "running" the project. The project is maven-based so we will be using maven related commands such as

mvn clean compile
mvn test
The project was delivered with java 1.6 but when running mvn clean compile the compilation plugin from maven is not supporting java versions older then JAVA 8 (JDK 1.8) so we changed this in the xml configuration file.

Besides this quick fix the tests were running fine from a mvn clean test with no failing tests.

To confim the project choice we then went on running a test coverage tool on it, we choosed Open Clover from Atlassian. The documentation of the tools was missleading and with help of a TA we got it to work and observed a coverage result of 69.9% with the original repository by running the command mvn clean clover:setup test clover:aggregate clover:clover.

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

I chose the function "compareSegments" from geometry-api-java-coverage/src/main/java/com/esri/core/geometry/SweepComparator.java. Lizard calculates the CC to 16. For my manual calculation i used the
formula "M = π - s + 2" from which I get 17, which is one higher than the number produced by Lizard.

The function is quite long compared to the average NLOC (50 vs 15), although far from the longest functions of 250-500 NLOC. However, if you compare the longest of all functions with the most complex of all functions, there seems to be a correlation. In the top 10 lists for both of these properties, 7 of the functions appear in both lists. These functions involve complex operations. They perform geometrical calculations such as cutting a shape of any kind with a shape of any kind. This means they require many many conditional statements for the many possible cases which can arise.

Yes, lizard takes exceptions into account. It considers exceptions as possible branches, which increases the number of branches and thereby increases the CC. However, in my studied function there are no exceptions and so this makes no difference. 

Documentation is more than unclear, actually there is no documentation of these functions! Neither for the top 10 or compareSegments.

### Henrik - function: doOne

The helper function doOne in the file PointInPolygonHelper.java was studied. Both the manual calculation and lizard got the cyclomatic complexity of the function to 21. Being just 57 lines the function is not particularly long despite the cyclomatic complexity. The function checks if a point is inside of a polygon, on the border of a polygon or outside of a polygon. As no exceptions exist in fuction they are not accouted for in the complexity. Most parts of the function are well documented, but the method to check if the point is inside or outside a polygon using the "odd-even rule" could be better documented

### Edgar - function: overlaps_

I choosed the method overlaps_ in the file RelationalOperationsMatrix.java from package com.esri.core.geometry. The complexity reduction can be expressed with the following metrics
|Sample time|NLOC|CCN|token|PARAM|length|
|:---:|---:|---:|---:|---:|---:|
|Before|20|21|207|3|25|
|After|9|3|48|3|11|

Since at the start the function was only 25 lines long, I would not say it was a long function but according to lizard, the circular complexity of 21 is higher then the usual (lizard sets a threshold at CCN=15 to differentiate between commonly complex functions and unusually complex ones).
This function is used to identify if a String matches some pattern considering the mathematical dimension of two other parameters (of type Geometry). It is used as a helper method for a bigger function in the same file.
These measurements do not include exceptions nor exception handlings because of the nature of the code which is enough lowlevel and does not involve any IO operations.
The documentation is almost inexistant but the method corresponds to the single line comment at the line just before which is `Checks whether the scl string is the overlaps relation.`.

For a calculation of the cyclomatic complexity by hand, we can infer that lizard is considering each comparison with `scl.charAt()` as a decision point since they are each a point where the equality `==` can evaluate either to `true` or `false`.
Thus we have the initial `if (dim_a == dim_b)`, then the `if (dim_a != 1)` and twice the nine decision points with the `chartAt()` call. The formula for the CC can be written as `CC=E−N+2P` with E the number of edges in the control flow graph, N the number of nodes in the same graph and P the number of connected components. This formula can be rewritten as following `CC=(Number of decision points)+1`. Here we have:`CC = 1 + 1 + 2*9 + 1 = 21` before reduction.

## Refactoring

### Linus - function: appendDouble

Plan for refactoring complex code:

Clamp precision using Math.max and Math.min to replace two if-statements. Move checking of whether string contains a dot and no exponent to separate helper function.

Estimated impact of refactoring (lower CC, but other drawbacks?).

Lowers CC by 2 (from 9 --> 7). Using clamping reduces readibility.

Carried out refactoring (optional, P+):

### Kristin - function: compareSegments

To reduce complexity, I suggest making a helper function called makeEdge for when tryGetCachedEdge returns null. (see below) This would remove nestled if-statements and save many lines. 

``SimpleEdge edgeLeft = tryGetCachedEdge_(leftElm);
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
			m_vertex_2 = right_vertex; ´´


Rewriting the ternary operator lines:
``` 
int kind = edgeLeft.m_b_horizontal ? 1 : 0;
kind |= edgeRight.m_b_horizontal ? 2 : 0; 
```
would also make the code more readable

### Henrik - function: doOne

The function was refactored by putting part of it into a helper function for easier readibility.
The reduction can be expressed with the following metrics
|Sample time|NLOC|CCN|token|PARAM|length|
|:---:|---:|---:|---:|---:|---:|
|Before|43|21|348|1|57|
|After|33|16|262|1|43| 

### Edgar - function: overlaps_

The code that highly increases the complexity here is the char by char comparison that can be replaced with null-safe String comparison but also with a simple call to `String::startsWith()` since the `overlaps_()` method is private and only called from a context that ensures non null String input.

## Coverage

To start this second part we first generated a general coverage report to know on what parts of the code to focus. For this we used Open Clover as mentionned before. It generates a browsable report with key metrics on a dashboard and an intuitive interface to see class and method related coverage.
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

Somewhat. My custom coverage tool uses branch coverage while Clover uses a combination of several, branch coverage being one of them. Therefore Clover has a higher coverage percentage than my too

#### Kristin

My DIY tool is not very high quality, but it produces results in line with clover and works for the specific function which I have been working on. It is hardcoded into the function I am testing coverage of (intersectLineLine). At the beginning of the file I created "static boolean[] covered = new boolean[52]", and at each branch I set covered[*branch-number*] to true if the branch is taken. On return I print a result to the file "coverage.txt" of all branches *not* covered.

##### Evaluation

1. How detailed is your coverage measurement?

It is clear for the specific funtion, but it *only* covers this function. It does not say in which test the coverage happens or any further details. So it is basically a bare minimum tool.

2. What are the limitations of your own tool?

As mentioned, it it completely limited to this situation and not very useful outside of it.

3. Are the results of your tool consistent with existing coverage tools?

Yes! (with clover at least)


#### Henrik
The DIY coverage tool for isPointInRing() was implemented as a static array of boolean values that turn true if the branch has been visited. The summarized results from all tests in the testPointInRing() to see what branches were reached and which weren't, this array is then printed in the test results. 

##### Evaluation

1. How detailed is your coverage measurement?

The measurement consists of which branches has been taken. The coverage is entierly up to the user as you have to manually add coverage points which is also limited to what branches has been reached and nothing more. In this case, the method contains 15 key points where the coverage reports if the branch has been reached.

2. What are the limitations of your own tool?

The biggest limitation is that it is completly manual and for now only covers one function, but the coverage also only reveals which checkpoints has been reached and not how many "if" checks were between each checkpoint and how much code or functionality each checkpoint covers.

3. Are the results of your tool consistent with existing coverage tools?

The result in OpenClover is not consistent with my DIY coverage, OpenClover does not detect it at all. But VScode and the DIY coveerage tools says that half the branches are cover compared to none before additional tests were added

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

It would not consider ternary operator if there were any and has no handling of java exceptions meaning that if the execution were to stop in the middle of the method, there would a uncoherent value on some branches.
The big drawback of my tool is that it is absolutly not generic. It is constrained to cover the overlaps_() method.

3. Are the results of your tool consistent with existing coverage tools?

The results of my coverage tool are consitent with the ones from Open Clover meaning both of them detect 99 calls of the overlaps_() method and trace them the same way.

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

### Edgar - TestRelationalOperationsmatrix
Comments showing required input can be seen added in [commit a9c41b7](https://github.com/DD2480-Group-27/geometry-api-java-coverage/commit/a9c41b787c8f34c52e7c8bfe04a04c445b79b925)

Report of old coverage: [link](https://github.com/DD2480-Group-27/geometry-api-java-coverage/blob/edgar/feat/coverage-overlaps_/coverage-before.txt)

Report of new coverage: [link](https://github.com/DD2480-Group-27/geometry-api-java-coverage/blob/edgar/feat/coverage-overlaps_/coverage-after.txt)

The following [test cases](https://github.com/DD2480-Group-27/geometry-api-java-coverage/blob/edgar/feat/coverage-overlaps_/src/test/java/com/esri/core/geometry/TestRelationalOperationsMatrix.java) have been added:
- TestRelationalOperationsMatrix::testRelateOverlapLines
- TestRelationalOperationsMatrix::testRelateOverlapPoints
- TestRelationalOperationsMatrix::testRelateOverlapPolygons

## Self-assessment: Way of working

Current state according to the Essence standard: ...

Was the self-assessment unanimous? Any doubts about certain items?

How have you improved so far?

Where is potential for improvement?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?
