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

:- [domain-task21].		% Replace with the domain for this problem

% --- Definition of the initial state ---------------------------------

% Nursebot is in location A4
at(nb, a1, s0).
% Wheelchair is in location A3
at(w, a3, s0).
% The nurseBot is not holdin the tray

% --- Goal condition that the planner will try to reach ---------------
% The goal is for the NB to grab the plate

goal(S) :- holdingTray(S).

% ---------------------------------------------------------------------
% ---------------------------------------------------------------------
