package cashflow.getmoney.amazeme;

/**
 * Created by Jesus on 11/15/17.
 */

public class Friend {
    private String name, username;

    public Friend() {

    }

    public Friend(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername() { this.username = username; }
}
