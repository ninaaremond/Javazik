package javazik.modele;

import java.io.*;
import java.util.*;

public abstract class Utilisateur {
    protected int id;
    protected String mdp;
    protected String email;
    protected int droit; // 0=visiteur, 1=client, 2=admin

    public Utilisateur(int id, String mdp, String email, int droit) {
        this.id = id;
        this.mdp = mdp;
        this.email = email;
        this.droit = droit;
    }

    public boolean connexion(String emailSaisi, String mdpSaisi) {
        try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");
                if (p.length >= 7 && p[3].equals(emailSaisi) && p[2].equals(mdpSaisi)) {
                    this.id = Integer.parseInt(p[0]);
                    this.droit = Integer.parseInt(p[1]);
                    this.mdp = p[2];
                    this.email = p[3];
                    return true;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public ArrayList<String[]> rechercherDansCatalogue(Map<String, String> criteres) {
        ArrayList<String[]> resultats = new ArrayList<>();
        File f = new File("catalogueJavazik.txt");
        if (!f.exists()) return resultats;

        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine()) {
                String[] ligne = scanner.nextLine().split(";", -1);
                if (ligne.length < 5) continue;

                boolean match = false;
                if (criteres.containsKey("Artiste") && ligne[1].equalsIgnoreCase(criteres.get("Artiste"))) match = true;
                if (criteres.containsKey("Morceau") && ligne[2].equalsIgnoreCase(criteres.get("Morceau"))) match = true;
                
                if (match) resultats.add(ligne);
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        return resultats;
    }

    public int getDroit() { return droit; }
    public String getEmail() { return email; }
    public int getId() { return id; }
    public void setMdp(String mdp) { this.mdp = mdp; }
}
