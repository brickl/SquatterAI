# SquatterAI
AI Game Project 

Squatter is a game in where players need to form loops around empty or the opponents cells in order to score points.
We implemented an Agent that uses minimax search, with alpha beta pruning, that returns moves in a strategic order to further prune more nodes.
We had a memory constraint of 1032k & time constraint of 7.5s per game for 6 tiles & 10s per game for 7 tiles.
Our agent was able to look at a depth of 4 moves ahead & 6 tiles with ease, under the time constraint.
7 tiles was more of a challenge & on average ran about 9-11s, slightly too high.

Overall we implemented a very intelligent game playing agent. 

Input arguments:
1: size of board - recommended 6 to 7
2: Player
3: Player

Availble Players: 
Lbrick - AI game playing agent we have written. 
Random - Places random moves
User   - Do you have what it takes to play against our AI agent?


