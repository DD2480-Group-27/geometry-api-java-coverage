# Report for assignment 3

## Project

Name: geometry-api-java

URL: [https://github.com/Esri/geometry-api-java](https://github.com/Esri/geometry-api-java)

This API makes it easier to handle and analyze geospatial data in custom applications and big data platforms, improving their ability to work with location information.

## Onboarding experience

The project in itself is just an API so there is not any so called "running" the project.
The project is maven-based so we will be using maven related commands such as
```bash
mvn clean compile
mvn test
```
The project was delivered with java 1.6 but when running `mvn clean compile` the compilation plugin from maven is not supporting java versions older then JAVA 8 (JDK 1.8) so we changed this in the xml configuration file.

Besides this quick fix the tests were running fine from a `mvn clean test` with no failing tests.

To confim the project choice we then went on running a test coverage tool on it, we choosed [Open Clover](https://openclover.org/) from Atlassian. The documentation of the tools was missleading and with help of a TA we got it to work and observed a coverage result of 69.9% with the original repository by running the command `mvn clean clover:setup test clover:aggregate clover:clover`.

This led us to choose this project but also the fact that this project uses maven (with which our group already worked on earlier assignments) and JUnit4 as testing library (which we were also using in earlier assignment for this same course).

Note that since the addition of Open Clover to the maven configuration file, the test run with `mvn clean test` are failing because of what it appears to be a conflict between clover and the maven-javadoc-plugin used by the repo for documentation purposes.

A second noticeable point, since we are talking of an API some of its public java interfaces are documented it is however not the case of many classes. There is thus a further difficulty in understanding the code in order to improve its quality and coverage while staying mathematically correct.

## Complexity

1. What are your results for five complex functions?
   * Did all methods (tools vs. manual count) get the same result?
   * Are the results clear?
2. Are the functions just complex, or also long?
3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?
5. Is the documentation clear w.r.t. all the possible outcomes?

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
Thus we have the initial `if (dim_a == dim_b)`, then the `if (dim_a != 1)` and twice the nine decision points with the `chartAt()` call. The formula for the CC can be written as `CC=E−N+2P` with E the number of edges in the control flow graph, N the number of nodes in the same graph and P the number of connected components. This formula can be rewritten as following `CC=(Number of decision points)+1`. Here we have:`CC = 1 + 1 + 2*9 + 1 = 21` before reduction.


## Refactoring

The code that highly increases the complexity here is the char by char comparison that can be replaced with null-safe String comparison but also with a simple call to `String::startsWith()` since the `overlaps_()` method is private and only called from a context that ensures non null String input.

## Coverage

### Tools

To start this second part we first generated an general coverage report to know on what parts of the code to focus. For this we used Open Clover as mentionned before. It generates a browsable report with key metrics on a dashboard and an intuitive interface to see class and method related coverage.
As explained above, by the use of one single command the report was generated using all the tests in their last version and giving per-line coverage adding the number of times the given line is indeed executed (0 if never).

### Your own coverage tool

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

### Evaluation

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
