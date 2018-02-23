package es.urjc.videotranscoding.entities;

public enum UserRoles
{
    ADMIN,
    USER;

    public String toString()
    {
        switch (this) {
            case ADMIN:
                return "ROLE_ADMIN";
            case USER:
                return "ROLE_USER";
            default:
                throw new IllegalStateException("No Rol");
        }
    }
}
