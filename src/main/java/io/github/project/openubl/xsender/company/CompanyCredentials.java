package io.github.project.openubl.xsender.company;

public class CompanyCredentials {

    private final String username;
    private final String password;

    public CompanyCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
