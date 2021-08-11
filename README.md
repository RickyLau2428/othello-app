# An Implementation of Othello

## A Quick Description

Othello is a 2-player board game played on a 8x8 board. My application aims to simulate an Othello game that can be 
played on a computer, faithful to the original rules as provided by the World Othello Federation
(rules found here https://www.worldothello.org/about/about-othello/othello-rules/official-rules/english).

As a quick summary, my application will be able to: 
* Do whatever an Othello board can do and **more**.
* Store a current game to be finished at another time.
* Upon request, provide a list of all valid moves that can be taken.

## Who is this for?
My application can be used by absolutely anybody who has an interest in playing Othello, from those who have never 
heard of it to veteran players. As long as you have two players (or maybe just one if you enjoy playing
against yourself), this application will satisfy most of your Othello needs.

## Why Othello?
I was inspired to design my project around Othello by watching gameplay on YouTube. To be completely frank, I've never 
played the game before, but I felt that it translated into a program fairly well and would be fun to implement.


## User Stories
* As a user, I want to be able to place a piece on the board, thus capturing opponent pieces
* As a user, I want to be able to start an Othello game with the correct starting configuration
* As a user, I want to be able to end a game of Othello and determine who the superior Othello player is.
* As a user, I want to be able to request for and see a list of all currently valid moves.
* As a user, I want to be able to see the final score of an Othello match.
* As a user, I want to be able to save an ongoing match to file.
* As a user, I want to be able to load an ongoing match from file.
* As a user, I want to be able to save/load multiple games to/from file.

## Phase 4: Task 2
**Chosen option:** Test and design a class in your model package that is robust.
 * **Class with exception:** Some methods in Cursor throws IllegalCursorException
    * **Methods:** setPosition, moveCursorRight, moveCursorLeft, moveCursorDown, moveCursorUp, 
      moveCursorUpperRight, moveCursorLowerRight, moveCursorUpperLeft, moveCursorLowerLeft
    * **Tests for exceptions:** See unit tests in CursorTest (e.g. testMoveCursorRightNothingThrown, 
      testMoveCursorRightBorderException, testMoveCursorRightWrapAroundException)
      

## Phase 4: Task 3
Given more time to work on the project, there are a number of things I'd like to improve:
<ol>
<li>Implement the Observer design pattern to reduce the number of associations leading to GameBoard from classes
in the ui package. </li>
<ul>
<li>Observers would be the GUI components, and the Subject would be GameBoard.</li>
</ul>
<li>Increase cohesion of code by splitting up the GameBoard class.</li>
<ul>
<li>GameBoard currently violates the Single Responsibility Principle - it is tasked with placing the pieces, 
generating valid moves, and checking for the game's end.</li>
<li>I would refactor it by splitting GameBoard into 4 separate classes:</li>
<ul>
<li>PiecePlacer (for placing the pieces on the board and updating other classes)</li>
<li>ValidMoveGenerator (for generating all valid moves).</li>
<li>GameEnder (for checking game-ending conditions).</li>
<li>GameBoardManager (for managing the entirety of the GameBoard).</li>
</ul>
</ul>
</ol>