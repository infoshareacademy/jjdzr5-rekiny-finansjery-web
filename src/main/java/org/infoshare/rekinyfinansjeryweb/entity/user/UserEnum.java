package org.infoshare.rekinyfinansjeryweb.entity.user;

public enum UserEnum {
    ROLE_ADMIN("user.role.admin"),
    ROLE_USER("user.role.user"),
    ROLE_GUEST("user.role.guest");

    final String roleName;

    UserEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
