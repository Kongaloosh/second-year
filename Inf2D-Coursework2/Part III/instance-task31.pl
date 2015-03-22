% ---------------------------------------------------------------------
%  ----- Informatics 2D - 2012/13 - Second Assignment - Planning -----
% ---------------------------------------------------------------------
% Checked
% Write here you matriculation number (only - your name is not needed)
% Matriculation Number: s1210313
%
%
% ------------------------- Problem Instance --------------------------
% This file is a template for a problem instance: the definition of an
% initial state and of a goal. 

% debug(on).	% need additional debug information at runtime?

% --- Load domain definitions from an external file -------------------

:- [domain-task31].		% Replace with the domain for this problem
%:- [new31].
% --- Definition of the initial state ---------------------------------

% Nursebot is in location A3 holding the Tray
at(nb, a3, s0).
holdingTray(s0).

% Wheelchair is in location A4
at(w, a4, s0).
patientInBed(s0).
patientFed(s0).

% --- Goal condition that the planner will try to reach ---------------

goal(S):- patientLoaded(S), not(holdingTray(S)).

% ---------------------------------------------------------------------
% ---------------------------------------------------------------------
