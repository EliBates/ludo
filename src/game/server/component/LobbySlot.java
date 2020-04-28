package game.server.component;

import game.server.net.Connection;

public class LobbySlot {

    private Connection connection;

    private int index;

    private boolean isEnabled;

    public LobbySlot(Connection connection, int index, boolean isEnabled) {
        this.connection = connection;
        this.index = index;
        this.isEnabled = isEnabled;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getIndex() {
        return index;
    }
}
