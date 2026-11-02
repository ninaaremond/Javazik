package javazik.modele;

import java.util.*;

public class Playlist {
    private int id;
    private String nom;
    private ArrayList<Integer> musicsID;

    public Playlist(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.musicsID = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<Integer> getMusicsID() {
        return musicsID;
    }

    public void ajouterMusicID(int musicID) {
        musicsID.add(musicID);
    }

    public void supprimerMusicID(int musicID) {
        musicsID.remove(Integer.valueOf(musicID));
    }

    public int getNombreMorceaux() {
        return musicsID.size();
    }

    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(";").append(nom).append(";");
        for (int i = 0; i < musicsID.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(musicsID.get(i));
        }
        return sb.toString();
    }
}
