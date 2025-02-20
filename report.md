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

The helper function doOne in the file PointInPolygonHelper.java was studied. Both the manual calculation and lizard got the cyclomatic complexity of the function to 21. Being just 57 lines the function is not particularly long despite the cyclomatic complexity. The function checks if a point is inside of a polygon, on the border of a polygon or outside of a polygon. As no exceptions exist in fuction they are not accouted for in the complexity. Most parts of the function are well documented, but the method to check if the point is inside or outside a polygon using the "odd-even rule" could be better documented



## Refactoring
The function was refactored by putting part of it into a helper function for easier readibility.
The reduction can be expressed with the following metrics
|Sample time|NLOC|CCN|token|PARAM|length|
|:---:|---:|---:|---:|---:|---:|
|Before|43|21|348|1|57|
|After|33|16|262|1|43| 



## Coverage


### Tools

To start this second part we first generated a general coverage report to know on what parts of the code to focus. For this we used Open Clover as mentionned before. It generates a browsable report with key metrics on a dashboard and an intuitive interface to see class and method related coverage.
As explained above, by the use of one single command the report was generated using all the tests in their last version and giving per-line coverage adding the number of times the given line is indeed executed (0 if never).

### Your own coverage tool

The DIY coverage tool for isPointInRing() was implemented as a static array of boolean values that turn true if the branch has been visited. The summarized results from all tests in the testPointInRing() to see what branches were reached and which weren't, this array is then printed in the test results.  


### Evaluation

1. How detailed is your coverage measurement?

The measurement consists of which branches has been taken. The coverage is entierly up to the user as you have to manually add coverage points which is also limited to what branches has been reached and nothing more. In this case, the method contains 15 key points where the coverage reports if the branch has been reached.

2. What are the limitations of your own tool?

The biggest limitation is that it is completly manual and for now only covers one function, but the coverage also only reveals which checkpoints has been reached and not how many "if" checks were between each checkpoint and how much code or functionality each checkpoint covers.

3. Are the results of your tool consistent with existing coverage tools?

The result in OpenClover is not consistent with my DIY coverage, OpenClover does not detect it at all. But VScode and the DIY coveerage tools says that half the branches are cover compared to none before additional tests were added

## Coverage improvement

No prior branch coverage for isPointInRing
Added tests for isPointInRing improving branch coverage in 8/15 test spots
- TestPolygonUtils::testPointInRing



## Self-assessment: Way of working

Current state according to the Essence standard: ...

Was the self-assessment unanimous? Any doubts about certain items?

How have you improved so far?

Where is potential for improvement?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?