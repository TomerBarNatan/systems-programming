package bgu.spl.net.Users;

public abstract class User {
    private final String username;
    private final String password;
    private boolean is_logged_in;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.is_logged_in = false;
    }

    public String get_username() {
        return username;
    }

    public String get_password() {
        return password;
    }

    public boolean is_logged_in() {
        return is_logged_in;
    }

    public void login() {
        is_logged_in = true;
    }

    public void logout(){
        is_logged_in = false;
    }
}
