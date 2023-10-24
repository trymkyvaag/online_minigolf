## Mutliplayer Top-Down Golf Game ##
### By: Antonio Craveiro, James Eastwood, and Trym Kyvaag ###
---

# Ever played GamePigeon Mini-Golf? #
## Now its available on computer!
In this simple java based game you can:

* Golf with freinds
* Host a server
* Join a server
* 9 Holes, 3 which are chosen at random
* Ensue chaos with real time collisions

### Installation/Startup Intructions ###
Install the jar file located in the repository, it is named XXXX.XXXX. Then run the File. A UI will appear with two panels. On side requests for an IP and a port number, and if you would like to join or host a server.
To host, simply enter IP and the desired port number to open a server on the current device. To join a server, simply enter the IP and Port you are trying to connect to and hit Join a Server. On the other panel of the screen,
there are customization options for you, such as ball color and nicknames. Fill out those fields as desired.

### Game Loop ###
Players first wait in a lobby, where the current score and nicknames are shown. Score is based off how may its it took a player to get to the hole. Players are sorted by score.

Players then move into a level. The level is one of 9 available. Players all wait for a countdown before shooting. Then all players attempt to get to the end of the level with the lowest number of puts.

After all players make it to the hole in a level, players are moved back to the lobby.

If there are more holes to play, the loop repeats. If not, then the game ends, returning players to thier join/host screens.

[UML Diagram](https://drive.google.com/file/d/14xAuk4I8hD6EX4EgCmdlVksbpTR3ntAb/view?usp=sharing)