package javazik.modele;

public class Visiteur extends Utilisateur {
    private int compteurEcoutes = 0;
    private final int LIMITE = 5;

    public Visiteur() {
        super(0, null, null, 0);
    }

    public boolean peutEcouter() {
        return compteurEcoutes < LIMITE;
    }

    public void incrementerEcoute() {
        compteurEcoutes++;
    }
    
    public int getCompteurEcoutes() {
        return compteurEcoutes;
    }
}
