package game;

import java.util.ArrayList;

public class PlayerManager {

    private static int activeIndex = -1;

    public static ArrayList<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        if (players.size() <= 4)
            players.add(player);
    }

    public static Player[] getPlayers() {
        return (Player[]) players.toArray();
    }

    public static Player getPlayer(int id) {
        int index = findIndexById(id);
        if (index != -1)
            return players.get(index);
        else
            return null;
    }

    public static Player getActivePlayer() {
        return players.get(activeIndex);
    }

    public static void setActivePlayer(Player player) {
        int newIndex = findIndexById(player.getId());
        activeIndex = newIndex;
    }

    private static int findIndexById(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (id == players.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    public static void nextTurn() {
        if (activeIndex >= players.size() -1) {
            activeIndex = 0;
        } else {
            activeIndex++;
        }
    }

}
