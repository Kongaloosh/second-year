-- (6.iii) AC-3 constraint propagation
{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}
ac3Check :: CSP -> [(Var,Var)] -> (CSP,Bool)
ac3Check csp vars = ac3Check' vars (csp, True)

ac3Check' :: [(Var,Var)] -> (CSP,Bool) -> (CSP,Bool)
ac3Check' [] result = result
ac3Check' ((x,y):rest) (csp,bool)
	| bool = ac3Check' rest $ revise csp x y -- continue checking
	| otherwise = (csp,False) -- there is no solution, since one of the arcs cannot be made consistent

-- (6.iv) AC-3 algorithm

-- purely for de-bug. This lets me see the output of allVarComb, as vars cannot be shown.
allComb :: [Int] -> [(Int,Int)] -> [(Int,Int)]
allComb [] x = x++[(b,a)|(a,b)<-x]
allComb (x:xs) y = allComb xs (y++[(x,y)| y <- xs]) 

{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen
-}
allVarComb :: [Var] -> [(Var,Var)] -> [(Var,Var)]
allVarComb [] x = x++[(b,a)|(a,b)<-x]
allVarComb (x:xs) y = allVarComb xs (y++[(x,y)| y <- xs])   

ac3Recursion :: CSP -> Assignment -> (Maybe Assignment, Int)
ac3Recursion csp assignment = 
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
                     where (result,nodes) = ac3Recursion cspNew $ assign (var,val) assignment
                           (ret,nodes') = findConsistentValue vs
                           (cspNew, bool) = ac3Check (setDomain (var,[val]) csp) (allVarComb (cspVars (setDomain(var,[val]) csp)) [])
						   

ac3 :: CSP -> (Maybe Assignment,Int)
ac3 csp = ac3Recursion csp []

-- (6.v) AC-3 algorithm + MRV heuristic + LCV heuristic
{- --------------- cspRec :: Var -> [Var] -> [Constraint]  ---------------
	Helper funciton for getVars and queenDomains. Given the size of the board,
	the function returns the list of all possible rows a queen could be in--
	the domain of a queen

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
                        (cspNew, bool) = ac3Check (setDomain (var,[val]) csp) (allVarComb (cspVars (addDomainVal (var,val) csp)) [])

--(cspNew, bool) =  ac3Check (setDomain (var,val) csp) (allVarComb (cspVars (addDomainVal (var,val) csp)) [])
	
ac3MRV_LCV :: CSP -> (Maybe Assignment,Int)
ac3MRV_LCV csp = ac3MRV_LCVRecursion csp []