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
						   