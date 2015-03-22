-- Inf2d Assignment 1 2013-14 
-- Matriculation number: s1210313	

module Inf2d where

import Data.List
import Data.Maybe
import CSPframework
import Examples

{- NOTES FOR THE MARKER:

		-- All the algorithms run in less than a minute.
		
-}

-------------------------------------------------
-- (3) Sudoku problem
-------------------------------------------------

-- (3.i) Variables & values
{- --------------- queenVars :: Int -> [Var] ---------------	
	will return the  list of possible queens given an x by x board
	where x is the input. Recursively calls, enumerating the board 0 through n-1
-}
queenVars :: Int -> [Var]
queenVars x 
	| x > 0 = queenVars (x-1) ++ [show (x-1)]
    | x == 0 = [] 

{-  --------------- queenDomains :: Int -> Domains ---------------
	generates a queenCsp for a specified board size, then returns all the variables
	from the CSP
	
	Note: this calls two helper functions: getVars and makeDomain
-}	
queenDomains :: Int -> Domains
queenDomains x = (getVars (queenVars x) x)

{- --------------- getVars:: [Var] -> Int -> Domains ---------------
	Helper function for queenDomains which simply constructs a domain by iterating
	over all the possible vars and assigning them the same domain from makeDomain
-}
getVars:: [Var] -> Int -> Domains
getVars [] y = []
getVars (x:xs) y = (getVars xs y) ++ [(x,(makeDomain y))]

{- --------------- makeDomain :: Int -> [Int] ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}	 
makeDomain :: Int -> [Int]
makeDomain x 
	| x > 0 = (makeDomain (x-1))++ [x-1]
	| otherwise = []

-- (3.ii) Constraints

{- --------------- diagonalRelation :: Relation -- [Var] -> Assignment -> bool ---------------
	if two queens are on a diagonal, then the square of the difference in their row
	placement is equal to the distance in their column placement. For instance Q1 = (1,2), Q2 = (2-1)
	(x1-x2)^2 = (y1-y2)^2. 
	
	Thus, this function assumes there are only two vars being compared. First it checks
	to ensure that the values are assigned. If they are it checks the distance in columns away from each other is
	the same as the distance in rows. If so, then the queens violate the relation and false is returned, otherwise true.
-}
diagonalRelation :: Relation -- [Var] -> Assignment -> bool
diagonalRelation (x:y:ys) z 
	| ((isAssigned z x) && (isAssigned z y) &&(square $ (read x)-(read y)) == (square $ (fromJust (lookupVar z x)) - (fromJust(lookupVar z y)))) = False
	-- the x distance and the y distance between the two queens is the same
	| otherwise = True	
diagonalRealation [] = False

-- helper which returns the square of an input int
square :: Int -> Int
square x = x*x

-- Binary constraint stating that two variables differ by exactly n.
diagonalConstraint :: Var -> Var -> Constraint
diagonalConstraint a b = CT (a ++ " and " ++ b ++ " not in the same diagonal",[a,b],diagonalRelation)

-- (3.iii) N-Queens CSP
{- --------------- queenCsp :: Int -> CSP ---------------
	Generates a csp for a specified board size
	Note: this has two helper functions--cspGen and cspRec
-}
queenCsp :: Int -> CSP
queenCsp n = CSP ((show n) ++ "-Queens Problem",
             queenDomains n, [
               ] ++ (cspGen (queenVars n)))
			   
{- --------------- cspGen :: [Var] -> [Constraint] ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}
cspGen :: [Var] -> [Constraint]
cspGen [] = []
cspGen (x:xs) = (cspRec x xs) ++ (cspGen xs)

{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}
cspRec :: Var -> [Var] -> [Constraint] 
cspRec _ [] = []
cspRec x (y:ys) = [(varsDiffConstraint x y )] ++ [(diagonalConstraint x y)] ++ [(varsDiffConstraint y x )] ++ [(diagonalConstraint y x)] ++ (cspRec x ys)


-------------------------------------------------
-- (4.1) Forward Checking
-------------------------------------------------

-- (4.1.i) Forward check for infernce:

-- test case: forwardcheck ausCsp (assign ("Q", 1) []) "Q"

{- --------------- forwardcheck :: CSP -> Assignment -> Var -> (CSP, Bool) ---------------
		keeps track of remaining legal variables and deletes values that are inconsistent.
-}
forwardcheck :: CSP -> Assignment -> Var -> (CSP, Bool)
forwardcheck csp assignment var = forwardCheck' var (allNeighboursOf var csp) (csp,True) assignment

forwardCheck' :: Var -> [Var] -> (CSP,Bool) -> Assignment -> (CSP, Bool)
forwardCheck' x [] (csp,bool) assignment = (csp, bool)
forwardCheck' x (y:ys) (csp,bool) assignment = forwardCheck' x ys ((setDomain (y,newdomain) csp), (bool &&(not(null(newdomain))))) assignment
    where comconstraints = commonConstraints csp x y
          newdomain = filter (\z -> checkConstraints comconstraints (assign (y,z) assignment)) (getDomain y csp)

-- (4.1.ii) Algorithm: 
{- --------------- fcRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)  ---------------
	BT recursion modified with a forward check call
-}
fcRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcRecursion csp assignment =
    if (isComplete csp assignment) then (Just assignment,0)
    else findConsistentValue $ getDomain var csp
      where var = firstUnassignedVar assignment csp
            findConsistentValue vals = 
              case vals of  -- recursion over the possible values 
                            -- instead of for-each loop
                []     -> (Nothing,0)
                val:vs -> 
                  if (isConsistentValue csp assignment (var,val)) 
					then if bool
						then if (isNothing result) 
							then (ret,nodes+nodes'+1)
							else (result,nodes+1)
						else (ret, nodes'+1)
					else (ret,nodes'+1)
                    where 
                        (result,nodes) = fcRecursion cspNew (assign (var,val) assignment)
                        (ret,nodes') = findConsistentValue vs
                        (cspNew, bool) = forwardcheck csp (assign (var,val) assignment) var

fc :: CSP -> (Maybe Assignment, Int)
fc csp = fcRecursion csp []
-------------------------------------------------
-- (4.2) Minimum Remaining Values (MRV)
-------------------------------------------------

-- (4.2.i) Sorting function for variables based on the MRV heuristic:
{- --------------- mrvCompare :: CSP -> Var -> Var -> Ordering ---------------
	given the lengths of two variables' domains, orders.
-}
mrvCompare :: CSP -> Var -> Var -> Ordering
mrvCompare csp varA varB
	| lengthA < lengthB = LT
	| lengthA > lengthB = GT
	| otherwise = EQ
	where 
		lengthA = length (getDomain varA csp)
		lengthB = length (getDomain varB csp)
		
-- (4.2.ii) Get next variable according to MRV for the FC algorithm:
{- --------------- getMRVVariable :: Assignment -> CSP -> Var  ---------------
	Utilizing previously defined ordering, filters the unassigned variables picking the one which has the least number
	of possible assignments.
-}
getMRVVariable :: Assignment -> CSP -> Var
getMRVVariable assignment csp = head $ sortBy (\x y -> mrvCompare csp x y) (filter (\x -> not(isAssigned assignment x)) (cspVars csp))


-- (4.2.iii) FC + MRV algorithm
{- --------------- fcMRVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)  ---------------
	Forward Check where 'first unassigned variable' is replaced with a variable chosen based on MRV heuristic
	using getMRVVariable.
-}
fcMRVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcMRVRecursion csp assignment =
    if (isComplete csp assignment) then (Just assignment,0)
    else findConsistentValue $ getDomain var csp
      where var = getMRVVariable assignment csp
            findConsistentValue vals = 
              case vals of  -- recursion over the possible values 
                            -- instead of for-each loop
                []     -> (Nothing,0)
                val:vs -> 
                  if (isConsistentValue csp assignment (var,val)) 
					then if (bool)
						then if (isNothing result) 
							then (ret,nodes+nodes'+1)
							else (result,nodes+1)
					else (ret, nodes'+1)
				  else (ret,nodes'+1)
                     where (result,nodes) = fcMRVRecursion cspNew $ assign (var,val) assignment
                           (ret,nodes') = findConsistentValue vs
                           (cspNew, bool) = forwardcheck csp (assign (var,val) assignment) var

fcMRV :: CSP -> (Maybe Assignment,Int)
fcMRV csp = fcMRVRecursion csp []

-------------------------------------------------
-- (4.3) Least Constraining Value (LCV)
-------------------------------------------------

-- (4.3.i) Function that returns the number of choices available for all neighbours
--         of a variable:

-- debug:
{- 
	ausCsp (assign ("T",2) $ assign ("V",1) $ assign ("NSW",2) $ assign ("Q",1) $ assign ("NT",2) $ assign ("SA",1) []) "NSW"
	filter (not (isAssigned (assign ("T",2) $ assign ("V",1) $ assign ("NSW",2) $ assign ("Q",1) $ assign ("NT",2) $ assign ("SA",1) []))) (nub(allNeighboursOf "SA" ausCsp))
	filter (\x -> not(isAssigned (assign ("T",2) $ assign ("V",1) $ assign ("NSW",2) $ assign ("Q",1) $ assign ("NT",2) $ assign ("SA",1) []) x)) (allNeighboursOf "SA" ausCsp)
	getDomain (head $ filter (\x -> not(isAssigned (assign ("T",2) $ assign ("V",1) $ assign ("NSW",2) $ assign ("Q",1) $ assign ("NT",2) $ assign ("SA",1) []) x)) (allNeighboursOf "SA" ausCsp)) ausCsp

			numChoices ausCsp (assign ("T",2) $ assign ("V",1) $ assign ("NSW",2) $ assign ("Q",1) $ assign ("NT",2) $ assign ("SA",1) []) "NSW"
-}

{- --------------- numChoices :: CSP -> Assignment -> Var -> Int ---------------
	Given the current assignment, determines the number of possible choices reaming for all unassigned neighbours.
-}
numChoices :: CSP -> Assignment -> Var -> Int
numChoices csp assignment var = length( addDomains (filter (\x -> not(isAssigned assignment x)) (nub (allNeighboursOf var csp))) csp assignment)

{- --------------- addDomains :: [Var] -> CSP -> Assignment -> Domain  ---------------
	Helper function for numChoices which adds possible choices for all domains
-}
addDomains :: [Var] -> CSP -> Assignment -> Domain
addDomains [] _ _= []
addDomains (x:xs) csp assignment = (filter (\y -> isConsistent csp (assign (x,y) assignment)) (getDomain x csp)) ++ (addDomains xs csp assignment)

-- (4.3.ii) Function that sorts the domain of a variable based on the LCV heuristic.

{- --------------- lcvSort :: CSP -> Assignment -> Var -> [Int] ---------------

-}
lcvSort :: CSP -> Assignment -> Var -> [Int]
lcvSort csp assignment var
	| length(allNeighboursOf var csp) == 0 = getDomain var csp
	| otherwise = sortBy (\a b -> orderLCV csp assignment var a b) (getDomain var csp)
-- 

-- sortBy (orderLCV csp assignment var) (getDomain var csp)
-- sortBy (\x y -> mrvCompare csp x y) (filter (\x -> not(isAssigned assignment x)) (cspVars csp))

{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}
orderLCV :: CSP -> Assignment -> Var -> Int -> Int -> Ordering
orderLCV csp assignment var a b	
	| lengthA > lengthB = LT
	| lengthA < lengthB = GT
	| otherwise = EQ
	where 
		lengthA = numChoices csp (assign (var,a) assignment) var
		lengthB = numChoices csp (assign (var,b) assignment) var

-- (4.3.iii) FC + LCV algorithm
fcLCVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcLCVRecursion csp assignment =
	if (isComplete csp assignment) then (Just assignment,0)
    else findConsistentValue $ lcvSort csp assignment var 
      where var = firstUnassignedVar assignment csp
            findConsistentValue vals = 
              case vals of  -- recursion over the possible values 
                            -- instead of for-each loop
                []     -> (Nothing,0)
                val:vs -> 
                  if (isConsistentValue csp assignment (var,val)) 
					then if bool
						then if (isNothing result) 
							then (ret,nodes+nodes'+1)
							else (result,nodes+1)
						else (ret, nodes'+1)
					else (ret,nodes'+1)
                    where 
                        (result,nodes) = fcLCVRecursion cspNew (assign (var,val) assignment)
                        (ret,nodes') = findConsistentValue vs
                        (cspNew, bool) = forwardcheck csp (assign (var,val) assignment) var

fcLCV :: CSP -> (Maybe Assignment,Int)
fcLCV csp = fcLCVRecursion csp []



-- (4.3.iv) FC + MRV + LCV algorithm
fcMRV_LCVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcMRV_LCVRecursion csp assignment =
	if (isComplete csp assignment) then (Just assignment,0)
    else findConsistentValue $ lcvSort csp assignment var 
      where var = getMRVVariable  assignment csp
            findConsistentValue vals = 
              case vals of  -- recursion over the possible values 
                            -- instead of for-each loop
                []     -> (Nothing,0)
                val:vs -> 
                  if (isConsistentValue csp assignment (var,val)) 
					then if bool
						then if (isNothing result) 
							then (ret,nodes+nodes'+1)
							else (result,nodes+1)
						else (ret, nodes'+1)
					else (ret,nodes'+1)
                    where 
                        (result,nodes) = fcMRV_LCVRecursion cspNew (assign (var,val) assignment)
                        (ret,nodes') = findConsistentValue vs
                        (cspNew, bool) = forwardcheck csp (assign (var,val) assignment) var

fcMRV_LCV :: CSP -> (Maybe Assignment,Int)
fcMRV_LCV csp = fcMRV_LCVRecursion csp []

-------------------------------------------------
-- (5) Evaluation
-------------------------------------------------
{-
  (5.ii) Table:

----------+--------+--------+--------+---------+----------+---------+-----------+
          |   BT   |   FC   | FC+MRV | FC+LCV  |FC+MRV+LCV|   AC3   |AC3+MRV+LCV|
----------+--------+--------+--------+---------+----------+---------+-----------+
ausCsp    | 37     |  11    |   6    |    11   |    11    |   9     |     6     |
mapCsp    | 1176   |  23    |   15   |    23   |    15    |   16    |     15    |
ms3x3Csp  | 3654   |  195   |   110  |    188  |    106   |xxxxxxxxx|xxxxxxxxxxx|
8-Queens  | 876    |  88    |   75   |    47   |    39    |   35    |     18    |
12-Queens | 3066   |  193   |   153  |    209  |    32    |   65    |     21    |
----------+--------+--------+--------+---------+----------+---------+-----------+

  (5.iii - 5.iv) Report:

- Your report must be no longer than one A4 page for the first four algorithms
  and another maximum half page for AC3 and AC3+MRV+LCV.
- Write your report starting here. -
-------------------------------------------------
	iii. You must also write a brief report on your implementation and evaluation. Your report must include the
	following:
			(a) A brief explanation of the values in the table and the differences observed between the algorithms.
			(b) A discussion about whether these were expected or not, and why.				
		In particular, compare and explain your results of the following pairs of algorithms:

			1. BT vs. FC
				Forward checking is an improvement of backtracking. The main difference being the addition of a 
				sub-procedure which checks to see what the effects of choosing as specific branch is. Given this,
				It's logical that forward-checking is a considerable improvement over backtracking by detecting 
				_some_ errors early on. Through this detection branches which lead to incomplete assignments are
				prevented and the nodes visited are significantly diminished. Most notably, this is visible in the
				12-queens problem. All values in the table were expected.
				
			2. FC vs. FC+MRV
				The MRV is the application of a heuristic. By assigning values to the most constrained variables
				early on, you avoid having to explore them only to find they have already been constrained by previous selections.
				In the two map related csps it's noticable how by assigning values to variable that are most constrained, the
				number of nodes visited is greatly improved. The boundaries of the variables, such as "NSW" and "T" in 
				the ausCsp, are varied, and by prioritizing their assignments. All values in the table are expected.

			3. FC vs. FC+LCV
				LCV is another heuristic application which acts in a similar way to MRV. While MRV attempts to assign
				values to variables with the most constraints, LCV assigns values such that the fewest values are ruled
				out by any assignment. The number of nodes visited for the 12-queens problem was unexpected, likely a
				choice in the queenCsp may leave many choices down the line, but may critically prevent an idea formation
				that would be complete for the problem.

			4. FC+MRV vs. FC+LCV
				As expected the two heuristics are beneficial in differing situations. FC+MRV out performs FC+LCV on
				ausCsp, mapCsp, ms3x3Csp, and 12-Queens, but FC+LCV is advantageous in 8-Queens. These heuristics are not
				approaching the same issue with different ways of re-ordering exploration, but are tackling differing
				areas, so they can be combined. Looking more at what makes the different heuristics better in certain
				situations, all queens are equally constrained. All queens have the same required conditions, namely that
				they must not have any other queens on their diagonals, where all other queens are potential neighbours.
				Thus, MRV will not be able to meaningfully order the queens. However, LCV can makes informed decisions to
				choose those which will least constrain choices further in the decision making process. Given this, I 
				expected FC+LCV to outperform FC+MRV on 12 queens.
								
			5. FC+MRV vs. FC+MRV+LCV
				This is a modest increase in performance, as is expected by combining the methods mentioned above. You'll likely
				perform better than FC+MRV or FC+LCV in situations that suit both, such as problems where you have many variations in
				constraints for the values, and there is variation in how variable assignment choices constrain other choices later on.
				This can be fully observed in both queen problems and the ms3x3Csp problem.
			
			6. AC3 & AC3 MRV LCV compared to FC FC MRV LCV
				AC3 provides early detection of failures through propagation, which repeatedly enforces constraints locally.
				Due to improved early detection of assignments which are inconsistent, this provides moderate improvements, the
				unexpected exception being the 12-queens problem, where the algorithm explores over double the points of FC MRV LCV,
				although when combined with MRV and LCV the benefit is experienced.
				
		iv. Is there a simple yet effective optimisation that you can apply to your implemented FC algorithm to improve its
		performance without introducing additional heuristics (esp. when considering much bigger and harder C)
		
		performing lookahead on future values may require consistency checks which are not needed to detect dead ends.
		FC may also do unnecessary lookahead, by performing checks which are never used when detecting an empty domain.
		By limiting the number of inconsistency checks early on. We could avoid pruning domains early on and do a limited amount
		of lookahead on future domains early on. This would prevent some of the unnecessary IC computation. 
		This has been implemented previously as 'minimal forward checking', or 'lazy forward checking'
		
-------------------------------------------------
- End of report-
-}


-------------------------------------------------
-- (6) Arc Consistency
-------------------------------------------------


-- (6.i) Checks if there exists at least one value in a list of values that if 
{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
	
-}
existsConsistentValue :: CSP -> Var -> Int -> Var -> [Int] -> Bool
existsConsistentValue _ _ _ _ [] = False -- since this was not evaluated, this should not be included in the or across y's domain.
existsConsistentValue csp varX intX varY (intY:ys) = isConsistent csp (assign (varX,intX) $ assign (varY,intY) []) || existsConsistentValue csp varX intX varY ys 
-- checks consistency across all possible assignments for y given the assignment for x and checks to see if one is possible.

-- (6.ii) AC-3 constraint propagation
{-
 Debug: should trigger false:
	revise (delDomainVal("V",1)$ delDomainVal ("V",2) $ delDomainVal ("NSW",2) $ delDomainVal ("NSW",1) ausCsp) "NSW" "V"revise (delDomainVal("V",1)$ delDomainVal ("V",2) $ delDomainVal ("NSW",2) $ delDomainVal ("NSW",1) ausCsp) "NSW" "V"
-}
revise :: CSP -> Var -> Var -> (CSP,Bool)
revise csp x y = revise' csp x (getDomain x csp) y False

revise' :: CSP -> Var -> [Int] -> Var -> Bool -> (CSP,Bool)
revise' csp _ [] _ bool = (csp,bool)
revise' csp x (iX:iXS) y bool
	|existsConsistentValue csp x iX y (getDomain y csp) = revise' csp x iXS y False
	|otherwise = revise' (delDomainVal (x,iX) csp) x iXS y True -- this assignment is not consistent, so remove it from the domain. 

-- (6.iii) AC-3 constraint propagation
{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
-}
ac3Check :: CSP -> [(Var,Var)] -> (CSP,Bool)
ac3Check csp vars = ac3Check' vars (csp, True)

ac3Check' :: [(Var,Var)] -> (CSP,Bool) -> (CSP,Bool)
ac3Check' [] result = result
ac3Check' ((x,y):rest) (csp,bool)
	| newStateConsistent && boolNew = ac3Check' (rest ++ newQueue) (cspNew, True)
	| newStateConsistent = ac3Check' rest (cspNew, True)
	| otherwise = (csp,False) -- there is no solution, since one of the arcs cannot be made consistent
	where 
        (cspNew, boolNew) = revise csp x y
        newStateConsistent = not $ null $ getDomain x cspNew
        newQueue = [(z,x) | z <- nub(allNeighboursOf x cspNew), not(z == y)] -- must nub since neighbours are repeated. Removes z==y, as specified in R&N


		  
-- (6.iv) AC-3 algorithm
-- purely for de-bug. This lets me see the output of allVarComb, as vars cannot be shown.
allComb :: [Int] -> [(Int,Int)] -> [(Int,Int)]
allComb [] x = x++[(b,a)|(a,b)<-x]
allComb (x:xs) y = allComb xs (y++[(x,y)| y <- xs]) 

{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
-}
allVarComb :: CSP -> [(Var,Var)]
allVarComb csp = [(x,y)| x <- vars, y <- nub(allNeighboursOf x csp)]
	where 
		vars = cspVars csp
		
ac3Recursion :: CSP -> Assignment -> (Maybe Assignment, Int)
ac3Recursion csp assignment = 
    if (isComplete csp assignment) then (Just assignment,0)
    else findConsistentValue $ getDomain var csp
      where var =  firstUnassignedVar assignment csp
            findConsistentValue vals = 
              case vals of  -- recursion over the possible values 
                            -- instead of for-each loop
                []     -> (Nothing,0)
                val:vs -> 
                  if (isConsistentValue csp assignment (var,val)) 
					then if (bool)
						then if (isNothing result) 
							then (ret,nodes+nodes'+1)
							else (result,nodes+1)
					else (ret, nodes'+1)
				  else (ret,nodes'+1)
                     where (result,nodes) = ac3Recursion cspNew $ assign (var,val) assignment
                           (ret,nodes') = findConsistentValue vs
                           (cspNew, bool) = ac3Check (setDomain (var,[val]) csp) (allVarComb csp)
						   

ac3 :: CSP -> (Maybe Assignment,Int)
ac3 csp = ac3Recursion csp []

-- (6.v) AC-3 algorithm + MRV heuristic + LCV heuristic
{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
-}
ac3MRV_LCVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
ac3MRV_LCVRecursion csp assignment =
	if (isComplete csp assignment) then (Just assignment,0)
    else findConsistentValue $ lcvSort csp assignment var 
      where var = getMRVVariable  assignment csp
            findConsistentValue vals = 
              case vals of  -- recursion over the possible values 
                            -- instead of for-each loop
                []     -> (Nothing,0)
                val:vs -> 
                  if (isConsistentValue csp assignment (var,val)) 
					then if bool
						then if (isNothing result) 
							then (ret,nodes+nodes'+1)
							else (result,nodes+1)
						else (ret, nodes'+1)
					else (ret,nodes'+1)
                    where 
                        (result,nodes) = ac3MRV_LCVRecursion cspNew (assign (var,val) assignment)
                        (ret,nodes') = findConsistentValue vs
                        (cspNew, bool) = ac3Check (setDomain (var,[val]) csp) (allVarComb csp)

--(cspNew, bool) =  ac3Check (setDomain (var,val) csp) (allVarComb (cspVars (addDomainVal (var,val) csp)) [])
ac3MRV_LCV :: CSP -> (Maybe Assignment,Int)
ac3MRV_LCV csp = ac3MRV_LCVRecursion csp []

-------------------------------------------------
