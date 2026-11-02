package javazik.modele;

public class SessionUtilisateur {
    private static int roleActuel;
    
    public static void setRole(int role) {
        roleActuel = role;
    }
    
    public static int getRole() {
        return roleActuel;
    }
}
