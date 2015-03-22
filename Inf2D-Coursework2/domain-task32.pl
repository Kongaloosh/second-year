% ---------------------------------------------------------------------
%  ----- Informatics 2D - 2012/13 - Second Assignment - Planning -----
% ---------------------------------------------------------------------
%
% Write here you matriculation number (only - your name is not needed)
% Matriculation Number: s1210313
%
%
% ------------------------- Domain Definition -------------------------
% This file describes a planning domain: a set of predicates and
% fluents that describe the state of the system, a set of actions and
% the axioms related to them. More than one problem can use the same
% domain definition, and therefore include this file


% --- Cross-file definitions ------------------------------------------
% marks the predicates whose definition is spread across two or more
% files
%
% :- multifile name/#, name/#, name/#, ...

:- multifile patientInBed/1,
 at/3,
 holdingTray/1,
 patientFed/1,
 notHoldingTray/1,
 patientLoaded/1,
 doorOpen/1,
 agentOutside/2,
 nurseBotOnWheelchair/1.

% --- Primitive control actions ---------------------------------------
% this section defines the name and the number of parameters of the
% actions available to the planner
%
% primitive_action( dosomething(_,_) ).	% underscore means `anything'

primitive_action( move(_,_,_)).
primitive_action( grabTray).
primitive_action( disposeTray).
primitive_action( feedPatient).
primitive_action( loadPatient).
primitive_action( openDoor).
primitive_action( closeDoor).
primitive_action( goOutside(_)).
primitive_action( mountWheelchair(_,_)).
primitive_action( dismountWheelchair(_,_)).

% --- Precondition for primitive actions ------------------------------
% describe when an action can be carried out, in a generic situation S
% poss( doSomething(...), S ) :- preconditions(..., S).
% Need preconditions for:

% -------------------- 3.2 -----------------------------

% nb may mount the w if it is adjacent to the w and
% the patient is not in the chair
% and it's not currently on the chair

poss( mountWheelchair(LocationWheelchair, LocationNurseBot),State) :- 
	not(nurseBotOnWheelchair(State)),
	isAdjacent(LocationNurseBot,LocationWheelchair),
	at(nb,LocationNurseBot,State),
	at(w,LocationWheelchair, State).

poss(dismountWheelchair(Location, DismountLoc),State) :-
	nurseBotOnWheelchair(State),
	at(w,Location,State),
	isAdjacent(LocationWheelchair, DismountLoc).
	
% -------------------- 3.1 -----------------------------

poss( loadPatient,State) :-
 at(nb,a6,State),
 at(w,a5,State),
 patientInBed(State),
 not(patientLoaded(State)).

poss( openDoor,State) :-
 at(nb,a5,State),
 not(doorOpen(State)).
 
poss( closeDoor,State) :-
 at(nb,a5,State),
 doorOpen(State).
 
poss( goOutside(Agent),State) :-
 at(Agent,a5,State),
 doorOpen(State),
 not(agentOutside(Agent,State)). 
%========================================================

poss( move(Agent, Location1, Location2), State) :-
 % not((nOnW(State), Agent = nb)), % 3.2 modification
 % if the move request is for the NB and it's on the wheelchair it can't move.
 (Agent = w; not(nurseBotOnWheelchair(State))),
 at(Agent, Location1, State),
 isAdjacent(Location1, Location2),
 isClear(Location2, State).
 
poss( grabTray,State):-
 at(nb,a1,State),
 not(holdingTray(State)).

poss( disposeTray, State):-
 holdingTray(State),
 at(nb,a4,State).

poss( feedPatient, State):-
 holdingTray(State),
 patientInBed(State),
 at(nb,a3,State),
 not(patientFed(State)).

% --- Successor state axioms ------------------------------------------

% ------------------ 3.2 ---------------------------

nurseBotOnWheelchair(result(Action,State)) :-
 Action = mountWheelchair(_,_);
 nurseBotOnWheelchair(State),
 not(Action = dismountWheelchair(_,_)).

% ------------------ 3.1 ----------------------------

patientLoaded(result(Action, State)) :-
 Action = loadPatient;
 patientLoaded(State).

doorOpen(result(Action,State)) :-
 Action = openDoor;
 doorOpen(State),
 not(Action = closeDoor).

agentOutside(Agent, result(Action,State)) :-
 Action = goOutside(Agent);
 agentOutside(Agent,State). 
 
% ===================================================

at(Agent, Location, result(Action, State)):-
	((nurseBotOnWheelchair(State), Agent = nb),
		(Action = move(w,Location,State));
		(at(nb,Location,State),not(Action = move(_,_,_)));
		(Action = dismountWheelchair(_,Location)));
	((Agent = w; not(nurseBotOnWheelchair(State))),
		((Action = move(Agent,_,Location);
		at(Agent,Location,State), not(Action = move(Agent,_,_))))). % all other instances

% The agent is holding the tray if it just grabbed the tray, or if it was previously holding the tray and did not dispose of it.
holdingTray(result(Action, State)) :- 
  Action = grabTray;
  holdingTray(State),
  not(Action = disposeTray).
 
%I've added this to optimize the code
%notHoldingTray(result(Action,State)):-
% Action = disposeTray;
% notHoldingTray(State),
% not(Action = grabTray).

% The patient is fed if the patient was just fed, or if the patient was previously fed. We make the assumption that the patient will never get hungry again.
patientFed(result(Action, State)) :-
 Action = feedPatient;
 patientFed(State).
  
% There is no means explicitly described to move the agent from their bed to any other part of the world, or vica versa.
patientInBed(result(Action, State)) :-
 patientInBed(State),
 not(Action = loadPatient).
 
% ---------------------------------------------------------------------

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

isClear(Location, State):- 
 not(at(nb,Location,State)),
 not(at(w,Location,State)).

 % ---------------------------------------------------------------------