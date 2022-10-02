package com.billsystem;

abstract public class User {
    private final int id;
    private final String password;
    private final String role;
    private final String name;

    public User(int id, String password, String role, String name) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public String getPassword() {
        return decrypt(this.password);
    }

    public String getEncryptedPassword() {
        return encrypt(this.password);
    }

    public String getStoredPassword() {
        return this.password;
    }

    public String getRole() {
        return this.role;
    }

    abstract void setStorage(Data data);

    /**
     * To encrypt password
     *
     * @param password
     * @return encrypted password
     */
    public static String encrypt(String password) {
        StringBuilder encrypted_pass = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if ('0' <= c && c < '9' || 'A' <= c && c < 'Z' || 'a' <= c && c < 'z')
                encrypted_pass.append((char) (c + 1));
            else if (c == 'Z' || c == 'z')
                encrypted_pass.append((char) (c - 25));
            else if (c == '9')
                encrypted_pass.append('0');
        }
        return encrypted_pass.toString();
    }

    /**
     * To decrypt password
     *
     * @param password
     * @return decrypted password
     */
    private String decrypt(String password) {
        StringBuilder decrypted_pass = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if ('0' < c && c <= '9' || 'A' < c && c <= 'Z' || 'a' < c && c <= 'z')
                decrypted_pass.append((char) (c - 1));
            else if (c == 'A' || c == 'a')
                decrypted_pass.append((char) (c + 25));
            else if (c == '0')
                decrypted_pass.append('9');
        }
        return decrypted_pass.toString();
    }

    abstract public void start();

}
