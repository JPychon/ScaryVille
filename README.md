# ScaryVille

ScaryVille is a 2D Puzzle Game-Demo built with Java & JavaFX. 
The application utilizes MVC design pattern for the business-logic.


## Documentations:

### Controllers:

##### Board Controller

The Board Controller handles all the 2D board logic through generating a 2D array containing a set of randomly-generated grid modules (walls/paths) based on a set of specific parameters.
> **Models Associated:** AsylumMap

#### GameController

The Game Controller manages the game-flow by controlling the current-game status, whether its lost, won or in progress, it also controls in-game pauses & starting new games. This controller also initalizes & houses the other static instances of the sub-controllers.
> **Models Associated:** N/A

#### GUIController

The GUI Controller handles all the 2D GUI logic through populating the grid with the correct nodes, celltypes, and starting/ending positions. It also handdles updating the GUI-status throughout the game to reflect the run-time changes based on user-input and AI movement. This controler also manages the hiding/showing the grid for pauses/winning/losing.
> **Models Associated:** MapPane & Cell

#### LunaticController

The Lunatic Controller manages the AI behavior throughout the game by initially creating them, and selecting random-valid values for their spawn positions, it also handles their movement timeline & stauts updates. Aswell as disabling/enabling them when needed.
> **Models Associated:** Lunatic, GridPathFinder, Node

#### PlayerController

The Player Controller manages the player-bevahir & status through out the game, it controls the player-behavior by updating the GUI & board upon player movement, it also keeps track of the player position relative to losing/winning.
> **Models Associated:** Player

#### InputController

The Input Controller listens to player-keyboard clicks to update the player-position within each click or pause/start the game.

#### MouseController

The Mouse Controller listens to button-clicks on the GUI for starting a new game & pausing mid-game.

#### SoundController

The Sound Controller manages the soundtrack of the game through starting/stopping the correct tracks based on the game status
> **Models Associated:** SoundTrack

### Pathfinding:

The Luantics AI utilizes a pathfinding algorithm found in 
###### GridPathFinder 
which is a variation of A* algorithm & dijkstra algorithm. The lunatic's line of sight is updated within every move to check for the player & chase if spotted. The lunatics have 3 roam status which controls how agressive they chase the player.
