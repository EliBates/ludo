package game;

public class GameController extends Thread {


    private GameGrid grid;

    public GameController(GameGrid g) {
        this.grid = g;
        this.start();
    }

    @Override
    public void run() {
        while (Main.isRunning) {
            if (!Main.gameStarted) {
                setupGame();
            } else if (Main.gameOver) {
                break;
            } else {
                try {
                    if (!Main.animating)
                        conductTurn(PlayerManager.getActivePlayer());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setupGame() {
        //setup players
        PlayerManager.addPlayer(new Player(Config.RED, "Eli"));
        PlayerManager.addPlayer(new Player(Config.GREEN, "Dom"));
        PlayerManager.addPlayer(new Player(Config.BLUE, "Bob"));
        PlayerManager.addPlayer(new Player(Config.YELLOW, "Jeff"));

        //set who goes first
        Player redPlayer = PlayerManager.getPlayer(Config.RED);
        if (redPlayer != null)
            PlayerManager.setActivePlayer(redPlayer);

        //start
        startGame();
    }

    public void startGame() {
        Main.gameStarted = true;
    }

    public void conductTurn(Player player) throws InterruptedException {
        if (player == null)
            return;

        Dice dice = new Dice();
        int roll = dice.roll();

        //System.out.println(player.getName() + " rolled a " + roll);

        sleep(500);

        player.movePiece(roll, grid);
        if (roll == 6) {
            //System.out.println(player.getName() +" just got a piece out of the starting area!");
        } else {
            PlayerManager.nextTurn();
        }


        //display text / graphics to let player know its their turn
        //Roll Dice
            //Determine Moveable Pieces
            //Highlight Moveables
            //Wait for click
                //-or-
            //Move auto if only one available
        //Respond to click
            //Move piece
            //Check for collision (hitting another piece)
            //Check for home run
            //Check for completed game
        //Commplete turn
    }

    public void endGame() {

    }





}
