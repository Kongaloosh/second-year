
********************************************************************************
*********************** README, to help you get started ************************
****************************** By Jaroslaw Hirniak *****************************
************************* E-mail: j.hirniak@sms.ed.ac.uk ***********************
********************************************************************************

    Content:

    0. Working on your code.
    1. Using ant.
      1.0. About ant.
      1.1. Executing ant commands.
      1.2. Available ant commands.
      1.3. Passing parameters to ant.
    2. Testing your solution.
    3. IDE.
      3.0. Importing existing project to Eclipse.
      3.1. Running ant file from Eclipse.
    4. Submitting your answer.
      4.0. Files.
      4.1. Double check all files.
      4.2. Submit your directory.
      4.3. Check if files were submitted.
    5. How your code is going to be tested and evaluated.
    6. How to get help.
    7. Writing hints.
      7.0. Self-documenting code.
      7.1. Comments.
      7.2. Little things that really matter.
    8. Inquisitive corner.
      8.0. Experminenting with different asymptotic bounds.
      8.1. Alternative solutions.

********************************************************************************
* 0. Working on your code. *****************************************************
********************************************************************************

The only class you need to modify is almost empty [StudentSolution] located at 
[src/closestPair/StudentSolution.java]. It implements [ISolution] interface,
specified in [closestPair.ISolution], meaning [StudentSolution] must provide all
3 methods specified in the interface:

 * int closestPair(PointSet problem) - should return square distance between the
   closest pair of points in problem PointSet;
 - String toString() - override method returning solution name (i.e. "Student");
 - String getEquation() - method returning asymptotic model of runtime from
   size of PointSet, as Shamos' algorithm is expected to have [c * n * lg(n)]
   asymptotic bound, so it is "n * lg(n)".

Moreover, in the assignment you are asked to specify the following methods as
part of implementing Shamos' algorithm:
 * int closestPairAux(PointSet X, PointSet Y)
 * void splitY(Point testPoint, PointSet Y, PointSet YL, PointSet YR)

 (*) - you are responsible for providing it, (-) - is already provided for

********************************************************************************
* 1. Using ant. ****************************************************************
********************************************************************************

- 1.0. About ant. --------------------------------------------------------------

There is ant [build.xml] file in the coursework folder to help you work with
the code efficiently and comfortably.

Apache Ant is an automating software building tool, similar to Make for C, but
more suitable for Java. It helps you to save all the hustle with building steps
which include configuring your classpath (including all jar files for JUnit
testing or JMathPlot for plotting), cleaning directories, compiling fresh,
testing, evaluating etc. to just a short set of commands.

- 1.1. Executing ant commands. -------------------------------------------------

You execute ant commands from project main directory (i.e., the one in which 
[build.xml] file is located) from command line by typing [ant command_name].

As you are evaluating runtime of algorithms, make sure you are not using
computer during evaluation as it can easily influence the running times (e.g.
if you are browsing Internet on the same computer during evaluation then kernel
will preempt runtime evaluating program few times making measured values to jump
up during that time).

- 1.2. Available ant commands. -------------------------------------------------

Core instructions (the one you will need):
 * ant generate_tests - generate test data and save it in [data] directory
 * ant test_student - run all unit tests specified in [tests/closestPair/
   TestStudentSolution.java]
 * ant student - compiles and runs main method of [closestPair.StudentSolution]
 * ant test - perform all unit tests specified in [tests] folder
 * ant docs - generates Java Docs for the classes specified in [src] folder
 * ant eval - perform evaluation, save report files and plot in [report] folder
 * ant eval_naive - perform evaluation only of [NaiveSolution]
 * ant eval_student - as above, but of [StudentSolution]
 * ant prepare_answer - create answer folder and copy all required files there,
   mind that if you have [ADSanswer] folder already it will be deleted first and
   then all required files will be copied there
 * ant flush - removes all created files, reverting code to its minimal version
   (as you downloaded it). Be aware as it removes [report] and [ADSanswer]
   folders as well
 * ant - default command, test, eval and prepare_answer all at once

- 1.3. Passing parameters to ant. ----------------------------------------------

When commands specified in [1.2] are executed the default parameters (called
properties in ant) are used. However, you may want to perform test on different
point sets or generate them differently.

You can modify the properties value by adding [-Dproperty_name=value], mind that
there is no space after [-D]. To modify multiple properties you should write
[-Dproperty_name=value] for every property you wish to modify.

Available properties are:
 * for [ant eval]:
   o first.eval - number of first problem to be evaluated from data folder
   o step.eval - how the problem number will incrase
   o last.eval - number of problem before which test will stop
   o solutions - specify which solutions runtime to evaluate, available options
     are all meaning all available, or list of solutions name which you want to
     be evaluated, e.g. [ant eval -Dsolutions=naive,student,other]
   o interpolate - as above, but which to interpolate using the average and
     maximum ratio obtained in the experiment, it has the same options available
 * for [ant generate_tests]:
   o tests.gen - number of tests to be created
   o first.gen - number of points in first PointSet to be created
   o step.gen - how much number of points increase from point set to point set
   o min.gen - minimum value of point coordinate
   o max.gen - maximum value of point coordinate

It may help you to think of [first.eval], [step.eval], and [last.eval] as
parameters of for..loop, which they actually are.

Example usage:
[ant generate_tests -Dtests.gen=20 -Dfirst.gen=1000 -Dstep.gen=100]
[ant eval -Dfirst.eval=1 -Dstep.eval=2 -Dlast.eval=20 -Dsolutions=all 
-Dinterpolate=Student]

********************************************************************************
* 2. Testing your solution. ****************************************************
********************************************************************************

There are JUnit tests (available via [ant test]) and assertions in the code,
e.g. [InconsistentCalculationsException.checkConsistency(results)] in both
[TimerSuite] and [Timer] classes.

Firstly, it means that your code is being tested when you evaluate it, watch for
[InconsistentCalculationsException] in your output as it indicates that values
obtained from your solution in subsequent tests or from your solution and naive
one were different. Note that all output is stored collectively in
[report/log.txt].

Secondly, there is JUnit test comparing results from [NaiveSolution] with
[StudentSolution] implemented in [tests/closestPair/TestStudentSolution.java].
Hence, you can run [ant test] to check for it as well as you can add your own
tests in [tests] folder or directly in [tests/closestPair/TestStudentSolution-
.java], they will be automatically included by ant and output visible when
running [ant test].

Last, but not least, if you would like just compile and run your class
[closestPair.StudentSolution] then you can run [ant student] and code will be
compiled and [StudentSolution] executed. The executable code should be put in
main method of [StudentSolution], i.e. in

public static void main(String[] args)
{
  /* PUT YOUR CODE HERE */
}

********************************************************************************
* 3. IDE. **********************************************************************
********************************************************************************

You may find using IDE (Integrated Development Environment) helpful, since it
makes coding easier because of features like code completion, underlying errors
even before compilatin, and help in managing your project efficiently.

However, please note, since you are asked to edit only one file it is absolutely
possible to accomplish the same thing using a simple text editor.

Eclipse, to which you can import your project in a described below way is
available on Dice machines.

- 3.0. Importing existing project to Eclipse. ----------------------------------

Open Eclipse either from menu or from console using [eclipse &]. When asked for
workspace path selected a folder where your coursework is located, i.e. folder
in which [inf2bcw1] is located and then press OK. If you do not see that option
when running Eclipse, simply once it is open click in top menu on [File ->
Switch Workspace -> Other ...] and selected explained before folder.

Most probably project is still not visible in [package manager] (by default
visible on the left side of screen). Click on it with right button and select
[Import...]. Now, select [General -> Existing Projects into Workspace]. By
defult [Select root directory] will be checked, now click on [Browse...] next to
it and select [inf2bcw1] directory by clicking OK. Make sure [inf2bcw1] is
ticked in [Projects:] text area and click [Finish] at the bottom.

Now, you should see your project in [package manager] on the left side. The
class which you are going to work on is placed under [inf2bcw1 -> src ->
closestPair -> StudentSolution.java].

- 3.1. Running ant file from Eclipse. ------------------------------------------

You can run ant build from Eclipse, e.g. by clicking right on [build.xml] and
selecting [run as -> ant build]. You can also specify parameter such as which
command you would like to run, e.g. [ant eval].

********************************************************************************
* 4. Submitting your answer. ***************************************************
********************************************************************************
                                                      
- 4.0. Files. ------------------------------------------------------------------

You should submit the final version of the below files, where all except
[StudentSolution.java] should come from running [ant eval] or [ant] command.

 * StudentSolution.java
 * plot.png
 * performance.txt
 * ratios.txt
 * summary.txt

- 4.1. Double check all files. -------------------------------------------------

Remember as you are the one responsible for the files you are submitting, please
double check that you are submitting actually what you want and nothing else.

You can preview all reports at once by [cd ADSanswer] and then [more *txt]
command. You can view your plot using [eog plot.png &]. Preview
[StudentSolution], e.g. using [less StudentSolution.java].

If all seem to be ok then you can go ahead and submit the answer directory.

- 4.2. Submit your directory. --------------------------------------------------

Make sure you are in the course main folder using [pwd] command. It should show
[/afs/inf.ed.ac.uk/user/s??/s???????/../inf2bcw1] and it should contain
[ADSanswer] folder (you can check that with [ls]).

Then submit using the following command:
submit inf2b 1 ADSanswer

Follow the instructions on the screen. Please, not that it is not a requirement
that the folder would be named [ADSanswer], but it is required that all asked
files are there and you submit just them.

- 4.3. Check if files were submitted. ------------------------------------------

Make sure the command was successful by checking the terminal output on the
screen and mail sent to your mailbox that all files were submitted correctly.

********************************************************************************
* 5. How your code is going to be tested and evaluated. ************************
********************************************************************************

Remember that your assessment is going to be based merely on the files you are
providing. The report files will be evaluated (i.e. plot.png, performance.txt, 
ratios.txt, and summary.txt) to see how well you performed your experiment.

Next, automated testing will be performed on [StudentSolution] class to see if
the class implements Shamos' algorithm correctly.

Lastly, the code will be reviewed and evaluated in terms of quality, e.g. how
well is it formatted, does it has clear names, is it reasonably easy to grasp
and perhaps to work with.

For your code to get highest mark it should pass the following tests
  * correctness - your code returns always valid answer
  * boundary - your code throws appropriate exception, e.g. when there is only
    one point in the set.

On top of that, obviously, it need to implement Shamos' algorithm described in
the handout and be formatted appropriately.

********************************************************************************
* 6. How to get help. **********************************************************
********************************************************************************

There has been NB board set-up for this assignment. You should recevie
invitation via your university e-mail. If for some reason, you were not invited
to NB, please mail me as soon as possible to [j.hirniak@sms.ed.ac.uk].

On NB you can select part of the assignment print-out or code and post question
regarding that fragment. You are able to do it anonymously or under your student
name. There was arranged time slot, where I am going to be answering question
three times a week for 1 hour. Check the coursework page for details:
[http://www.inf.ed.ac.uk/teaching/courses/inf2b/coursework/cwk1.html].
If that would turn to not be enough there can be arranged more. Also if you 
think you cannot ask your question via NB then you can always email me at
[j.hirniak@sms.ed.ac.uk]. However, asking question in public is usually better
as others may have the same problem as you, and this approach may help others as
well as other doing the same can help you.

********************************************************************************
* 7. Writing hints. ************************************************************
********************************************************************************
                                                      
Please, note the below are just hints for you how to write good quality code.
Remember that the best guideline is to write a code which is easy to understand,
easy to read, and potentially easy to modify or use.

- 7.0. Self-documenting code. --------------------------------------------------

Write self-documenting code, meaning all variables, classes, methods has names
clearly indicating their purpose, e.g. [int a = Math.min(b, c);] is much less
informative than [int minDistance = Math.min(leftDistance, rightDistance);].

- 7.1. Comments. ---------------------------------------------------------------

Write comments, if something is unclear or gives someone useful information
about your code which is not obvious from the code itself, e.g.
/** Get server uptime in the given time period.
    @param Timespan timespan - timespan from - to in server local time
    @return double uptime - ratio uptime to total time (ranging from 0.0 to 1.0)
*/
public double uptime(Timespan timespan)
{
  ...
}

Note that /** */ is Javadoc comment allowing for creating documentation from
your code automatically and supports certain tags as @return, @param, @author.

/* */ is standard multiline comment and // is single line comment from here to
the end of line.

- 7.2. One little thing that matter. -------------------------------------------

Different operating systems, text editors, IDE, people have different settings
when it comes to displaying tab characters. Hence, if you want that your code
be as much readable for other people as for you make sure you use space in place
of tab and use reasonable indentation (usually 2 or 4 spaces sufficies). Most 
text editors provide option for using spaces instead of tabs and how many spaces
mean a tab. Then you can use tab, but spaces will be inserted instead.

To read how it can be done in Eclipse, you can visit [http://bit.ly/1eSjbD9].

Please know that you will not be penalised for not using spaces instead of tabs.

********************************************************************************
* 8. Inquisitive corner. *******************************************************
********************************************************************************

There are quite few things you can experiment with, but please remember that
they should not influence the assessed part, if you would run into compilation
problems or would make changes elsewhere on which your code relies. It is good
idea to make any experiments once you are happy with your answer by making a
copy of the project folder that you would save the version with your answer
untouched.

- 8.0. Experminenting with different asymptotic bounds. ------------------------

In [StudentSolution] as well as in [NaiveSolution] you can change asymptotic
bound model, e.g. [StudentSolution] to "n^2" and [NaiveSolution] to "n^3".

Do the approximation curves look plausible or are they much different from
experimental ones, now?

What happens if you specify e.g. "n^10", is the plot still visible? If not click
on table icon (last in top menu) and then on solution's interpolation names.
NaN means "Not a Number", this problem comes from the fact that the value you
tried to compute was too big (not fitting in Long). Is there any way to solve
this? (Hint: read about BigInteger in Java). Does other languages like Haskell
or Python has the same issue? Why yes or not? What are benefits of one and
another?

- 8.1. Alternative solutions. --------------------------------------------------

Shamos' algorithm and naive (brute-force) solutions are not the only one
possible. You can specify another solution implementing [ISolution] interface
and add it in [evaluation.Evaluate] to [timerSuite] then it would be included
in the report and plot you receive after evaluation.


  Happy Coding,  
  Jaroslaw
