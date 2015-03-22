% ---------------------------------------------------------------------
%  ----- Informatics 2D - 2012/13 - Second Assignment - Planning -----
% ---------------------------------------------------------------------
%
% Write here you matriculation number (only - your name is not needed)
% Matriculation Number: s1210313
%
%
% ------------------------- Problem Instance --------------------------
% This file is a template for a problem instance: the definition of an
% initial state and of a goal. 

% debug(on).	% need additional debug information at runtime?

% --- Load domain definitions from an external file -------------------

:- [domain-task33].		% Replace with the domain for this problem
%:- [new31].
%:- [new31].
% --- Definition of the initial state ---------------------------------

batteryLevel(8,nb,s0).
batteryLevel(8,w,s0).

% Nursebot is in location A3 holding the Tray
at(nb, a3, s0).
holdingTray(s0).

% Wheelchair is in location A4
at(w, a4, s0).
patientInBed(s0).
patientFed(s0).

isAdjacent(a1,a2).
isAdjacent(a2,a3).
isAdjacent(a2,a4).
isAdjacent(a4,a5).
isAdjacent(a5,a6).
isAdjacent(a2,a1).
isAdjacent(a3,a2).
isAdjacent(a4,a2).
isAdjacent(a5,a4).
isAdjacent(a6,a5).

% --- Goal condition that the planner will try to reach ---------------
% The goal is for the NB to grab the plate

goal(S) :- patientLoaded(S),agentOutside(w,S),not(holdingTray(S)).

% ---------------------------------------------------------------------
% ---------------------------------------------------------------------

