% ---------------------------------------------------------------------
%  ----- Informatics 2D - 2012/13 - Second Assignment - Planning -----
% ---------------------------------------------------------------------
% CHECKED
% Write here you matriculation number (only - your name is not needed)
% Matriculation Number: s_______
%
%
% ------------------------- Problem Instance --------------------------
% This file is a template for a problem instance: the definition of an
% initial state and of a goal. 

% debug(on).	% need additional debug information at runtime?



% --- Load domain definitions from an external file -------------------

:- [domain-task21].		% Replace with the domain for this problem

% --- Definition of the initial state ---------------------------------

% Nusebot is in location A4
at(nb,a4,s0).
% Wheelchair is in location A3
at(w,a1,s0).
% The patient is in bed, and has not been fed
patientInBed(s0).
% other details not specified: the tray is in A1
				
% --- Goal condition that the planner will try to reach ---------------
%  the goal is that the patient is fed and the plate is disposed of
goal(S) :- patientFed(S),not(holdingTray(S)).




% ---------------------------------------------------------------------
% ---------------------------------------------------------------------
