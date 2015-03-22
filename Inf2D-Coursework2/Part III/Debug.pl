% ---------------------------------------------------------------------
%  ----- Informatics 2D - 2012/13 - Second Assignment - Planning -----
% ---------------------------------------------------------------------
%
% Write here you matriculation number (only - your name is not needed)
% Matriculation Number: s_______
%
%
% ------------------------- Problem Instance --------------------------
% This file is a template for a problem instance: the definition of an
% initial state and of a goal. 

% debug(on).	% need additional debug information at runtime?

% --- Load domain definitions from an external file -------------------

:- [domain-task31].		% Replace with the domain for this problem

% --- Definition of the initial state ---------------------------------

at(nb,a3,s0).
at(w,a4,s0).
patientInBed(s0).
patientFed(s0).
				
% --- Goal condition that the planner will try to reach ---------------
%  the goal is that the patient is fed and the plate is disposed of
goal(S) :- not(holdingTray(S)),patientLoaded(S),outside(w,S).

% ---------------------------------------------------------------------
% ---------------------------------------------------------------------
