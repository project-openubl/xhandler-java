package io.github.project.openubl.xsender.company;

public final class CompanyCredentialsBuilder {

    private String username;
    private String password;

    private CompanyCredentialsBuilder() {
    }

    public static CompanyCredentialsBuilder aCompanyCredentials() {
        return new CompanyCredentialsBuilder();
    }

    public CompanyCredentialsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public CompanyCredentialsBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CompanyCredentials build() {
        return new CompanyCredentials(username, password);
    }

}
