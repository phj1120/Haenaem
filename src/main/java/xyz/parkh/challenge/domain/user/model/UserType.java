package xyz.parkh.challenge.domain.user.model;

public enum UserType {
    ROLE_CHALLENGER("ROLE_CHALLENGER"), ROLE_ADMIN("ROLE_ADMIN");

    String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
