package javazik.modele;

import java.io.File;
import javax.sound.sampled.*;

public class LecteurAudio {
    private Clip clip;
    private boolean estJouant = false;
    private String fichierActuel = null;

    public LecteurAudio() {
        // Initialiser le lecteur
    }

    /**
     * Lance la lecture d'un fichier audio
     * @param cheminFichier Chemin complet du fichier audio
     * @return true si la lecture a démarré, false sinon
     */
    public boolean lancer(String cheminFichier) {
        try {
            // Si une musique est déjà en cours, l'arrêter
            if (clip != null && estJouant) {
                arreter();
            }

            File fichier = new File(cheminFichier);
            if (!fichier.exists()) {
                System.err.println("Fichier non trouvé: " + cheminFichier);
                return false;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fichier);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setFramePosition(0);
            clip.start();
            estJouant = true;
            fichierActuel = cheminFichier;
            return true;

        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met en pause la lecture actuelle
     */
    public void pause() {
        if (clip != null && estJouant) {
            clip.stop();
            estJouant = false;
        }
    }

    /**
     * Reprend la lecture après une pause
     */
    public void reprendre() {
        if (clip != null && !estJouant) {
            clip.start();
            estJouant = true;
        }
    }

    /**
     * Arrête complètement la lecture
     */
    public void arreter() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            estJouant = false;
            fichierActuel = null;
        }
    }

    /**
     * Vérifie si une musique est en cours de lecture
     */
    public boolean estJouant() {
        return estJouant;
    }

    /**
     * Retourne le fichier actuellement joué
     */
    public String getFichierActuel() {
        return fichierActuel;
    }

    /**
     * Retourne la durée totale en secondes
     */
    public int getDureeTotal() {
        if (clip != null) {
            return (int) (clip.getMicrosecondLength() / 1_000_000);
        }
        return 0;
    }

    /**
     * Retourne la position actuelle en secondes
     */
    public int getPositionActuelle() {
        if (clip != null) {
            return (int) (clip.getMicrosecondPosition() / 1_000_000);
        }
        return 0;
    }
}
