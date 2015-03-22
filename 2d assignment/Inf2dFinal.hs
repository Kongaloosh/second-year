-- Inf2d Assignment 1 2013-14 
-- Matriculation number: S1210313

module Inf2d where

import Data.List
import Data.Maybe
import CSPframework
import Examples

{- NOTES FOR THE MARKER:

	All required algorithms will run in under 1 minute

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
	| x > 0 = queenVars (x-1) ++ [show (x-1)] -- Recursively call for the next value and add the current int into the array as a string
    | x == 0 = [] -- if x=0 the board is 0 by 0--there are no queens

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
	| ((isAssigned z x) && (isAssigned z y) && (square $ (read x)-(read y)) == (square $ (fromJust (lookupVar z x)) - (fromJust(lookupVar z y)))) = False
	| otherwise = True	

-- helper which returns the square of an input int
square :: Int -> Int
square x = x*x	

diagonalConstraint :: Var -> Var -> Constraint
diagonalConstraint a b = CT (a ++ " and " ++ b ++ " not in the same diagonal",[a,b],diagonalRelation)


-- (3.iii) N-Queens CSP
{- --------------- queenCsp :: Int -> CSP ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}
queenCsp :: Int -> CSP
queenCsp n = CSP ((show n) ++ "-Queens Problem",
             queenDomains n, [
        ] ++ (cspGen (queenVars n)))

{- --------------- makeDomain :: Int -> [Int] ---------------

-}		
cspGen :: [Var] -> [Constraint]
cspGen [] = []
cspGen (x:xs) = (cspRec x xs) ++ (cspGen xs)

{- --------------- makeDomain :: Int -> [Int] ---------------
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

forwardcheck :: CSP -> Assignment -> Var -> (CSP, Bool)
forwardcheck = undefined

        
-- (4.1.ii) Algorithm: 

fcRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcRecursion = undefined



fc :: CSP -> (Maybe Assignment,Int)
fc csp = fcRecursion csp []


-------------------------------------------------
-- (4.2) Minimum Remaining Values (MRV)
-------------------------------------------------

-- (4.2.i) Sorting function for variables based on the MRV heuristic:

mrvCompare :: CSP -> Var -> Var -> Ordering
mrvCompare = undefined


-- (4.2.ii) Get next variable according to MRV for the FC algorithm:

getMRVVariable :: Assignment -> CSP -> Var
getMRVVariable = undefined


-- (4.2.iii) FC + MRV algorithm

fcMRVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcMRVRecursion = undefined



fcMRV :: CSP -> (Maybe Assignment,Int)
fcMRV csp = fcMRVRecursion csp []


-------------------------------------------------
-- (4.3) Least Constraining Value (LCV)
-------------------------------------------------

-- (4.3.i) Function that returns the number of choices available for all neighbours
--         of a variable:

numChoices :: CSP -> Assignment -> Var -> Int
numChoices = undefined


-- (4.3.ii) Function that sorts the domain of a variable based on the LCV heuristic.

lcvSort :: CSP -> Assignment -> Var -> [Int]
lcvSort = undefined


-- (4.3.iii) FC + LCV algorithm

fcLCVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcLCVRecursion = undefined



fcLCV :: CSP -> (Maybe Assignment,Int)
fcLCV = undefined



-- (4.3.iv) FC + MRV + LCV algorithm

fcMRV_LCVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
fcMRV_LCVRecursion = undefined



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
ausCsp    |        |        |        |         |          |         |           |
mapCsp    |        |        |        |         |          |         |           |
ms3x3Csp  |        |        |        |         |          |xxxxxxxxx|xxxxxxxxxxx|
8-Queens  |        |        |        |         |          |         |           |
12-Queens |        |        |        |         |          |         |           |
----------+--------+--------+--------+---------+----------+---------+-----------+

  (5.iii - 5.iv) Report:

- Your report must be no longer than one A4 page for the first four algorithms
  and another maximum half page for AC3 and AC3+MRV+LCV.
- Write your report starting here. -
-------------------------------------------------




-------------------------------------------------
- End of report-
-}


-------------------------------------------------
-- (6) Arc Consistency
-------------------------------------------------


-- (6.i) Checks if there exists at least one value in a list of values that if 
-- assigned to the given variable the assignment will be consistent.

existsConsistentValue :: CSP -> Var -> Int -> Var -> [Int] -> Bool
existsConsistentValue = undefined


-- (6.ii) AC-3 constraint propagation

revise :: CSP -> Var -> Var -> (CSP,Bool)
revise = undefined


-- (6.iii) AC-3 constraint propagation

ac3Check :: CSP -> [(Var,Var)] -> (CSP,Bool)
ac3Check = undefined


-- (6.iv) AC-3 algorithm

ac3Recursion :: CSP -> Assignment -> (Maybe Assignment, Int)
ac3Recursion = undefined


ac3 :: CSP -> (Maybe Assignment,Int)
ac3 csp = ac3Recursion csp []



-- (6.v) AC-3 algorithm + MRV heuristic + LCV heuristic

ac3MRV_LCVRecursion :: CSP -> Assignment -> (Maybe Assignment, Int)
ac3MRV_LCVRecursion = undefined


ac3MRV_LCV :: CSP -> (Maybe Assignment,Int)
ac3MRV_LCV csp = ac3MRV_LCVRecursion csp []

-------------------------------------------------
