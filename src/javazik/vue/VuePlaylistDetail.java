package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class VuePlaylistDetail extends JPanel {

    // Classe interne pour stocker les infos d'un morceau
    public static class InfoMorceau {
        public String id;
        public String titre;
        public String artiste;
        public String duree;
        
        public InfoMorceau(String id, String titre, String artiste, String duree) {
            this.id = id;
            this.titre = titre;
            this.artiste = artiste;
            this.duree = duree;
        }
    }

    private JButton btnRetour;
    private JLabel lblNomPlaylist;
    private JPanel listeMorceaux;
    private JTextField champAjout;
    private JButton btnAjouterMorceau;
    private Map<String, JButton> boutonsPlayParMorceau;
    private Map<String, JButton> boutonsPauseParMorceau;
    private Map<String, JButton> boutonsSupprimerParMorceau;
    private Map<String, InfoMorceau> infoMorceauxAffichee;
    private Map<String, String[]> donneesMorceau;

    public VuePlaylistDetail() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        boutonsPlayParMorceau = new HashMap<>();
        boutonsPauseParMorceau = new HashMap<>();
        boutonsSupprimerParMorceau = new HashMap<>();
        infoMorceauxAffichee = new HashMap<>();
        donneesMorceau = new HashMap<>();
        construireUI();
    }

    private void construireUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        lblNomPlaylist = new JLabel("Playlist");
        lblNomPlaylist.setFont(new Font("Arial", Font.BOLD, 20));
        lblNomPlaylist.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(lblNomPlaylist, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Centre : Liste des morceaux
        listeMorceaux = new JPanel();
        listeMorceaux.setLayout(new BoxLayout(listeMorceaux, BoxLayout.Y_AXIS));
        listeMorceaux.setBackground(new Color(253, 235, 241));
        listeMorceaux.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JScrollPane scroll = new JScrollPane(listeMorceaux);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Bas : ajouter morceau
        JPanel bas = new JPanel(new BorderLayout(10, 0));
        bas.setBackground(new Color(251, 212, 222));
        bas.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        champAjout = new JTextField();
        champAjout.setFont(new Font("Arial", Font.PLAIN, 13));
        btnAjouterMorceau = new JButton("Ajouter un morceau");
        btnAjouterMorceau.setBackground(new Color(204, 127, 151));
        btnAjouterMorceau.setForeground(Color.WHITE);
        btnAjouterMorceau.setFocusPainted(false);
        btnAjouterMorceau.setBorderPainted(false);
        btnAjouterMorceau.setFont(new Font("Arial", Font.BOLD, 12));
        bas.add(new JLabel("Titre : "), BorderLayout.WEST);
        bas.add(champAjout, BorderLayout.CENTER);
        bas.add(btnAjouterMorceau, BorderLayout.EAST);
        add(bas, BorderLayout.SOUTH);
    }

    public void setNomPlaylist(String nom) {
        lblNomPlaylist.setText("JAVAZIK - " + nom);
    }

    public void afficherMorceaux(List<String[]> morceaux) {
        listeMorceaux.removeAll();
        boutonsPlayParMorceau.clear();
        boutonsPauseParMorceau.clear();
        boutonsSupprimerParMorceau.clear();
        infoMorceauxAffichee.clear();
        donneesMorceau.clear();
        
        if (morceaux == null || morceaux.isEmpty()) {
            listeMorceaux.add(new JLabel("Playlist vide. Ajoutez des morceaux !"));
        } else {
            for (String[] m : morceaux) {
                listeMorceaux.add(creerLigneMorceau(m));
                listeMorceaux.add(Box.createVerticalStrut(6));
            }
        }
        listeMorceaux.revalidate();
        listeMorceaux.repaint();
    }

    private JPanel creerLigneMorceau(String[] data) {
        // Format: ID;Artiste;Titre;Durée;...
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setBackground(new Color(251, 212, 222));
        ligne.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        String id = data.length > 0 ? data[0] : "";
        String titre = data.length > 2 ? data[2] : "Inconnu";
        String artiste = data.length > 1 ? data[1] : "";
        String duree = data.length > 3 ? data[3] : "";

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel lblArtiste = new JLabel(artiste.isEmpty() ? "Artiste inconnu" : artiste);
        lblArtiste.setFont(new Font("Arial", Font.PLAIN, 11));
        lblArtiste.setForeground(new Color(204, 127, 151));

        JPanel texte = new JPanel();
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        texte.setOpaque(false);
        texte.add(lblTitre);
        texte.add(lblArtiste);

        JLabel lblDuree = new JLabel(duree.isEmpty() ? "" : "(" + duree + " min)");
        lblDuree.setFont(new Font("Arial", Font.PLAIN, 10));
        
        String cle = id + "_" + titre;
        donneesMorceau.put(cle, data);  // Stocker les données complètes du morceau
        
        JButton btnPlayMorceau = new JButton("▶ Jouer");
        btnPlayMorceau.setBackground(new Color(100, 200, 100));
        btnPlayMorceau.setForeground(Color.WHITE);
        btnPlayMorceau.setFocusPainted(false);
        btnPlayMorceau.setBorderPainted(false);
        btnPlayMorceau.setFont(new Font("Arial", Font.BOLD, 11));
        btnPlayMorceau.setPreferredSize(new Dimension(80, 35));
        btnPlayMorceau.setActionCommand(id + "_" + titre);
        
        JButton btnPauseMorceau = new JButton("⏸ Pause");
        btnPauseMorceau.setBackground(new Color(200, 180, 100));
        btnPauseMorceau.setForeground(Color.WHITE);
        btnPauseMorceau.setFocusPainted(false);
        btnPauseMorceau.setBorderPainted(false);
        btnPauseMorceau.setFont(new Font("Arial", Font.BOLD, 11));
        btnPauseMorceau.setPreferredSize(new Dimension(80, 35));
        btnPauseMorceau.setActionCommand(id + "_" + titre);
        
        JButton btnSupprimerMorceau = new JButton("✕");
        btnSupprimerMorceau.setBackground(new Color(200, 100, 100));
        btnSupprimerMorceau.setForeground(Color.WHITE);
        btnSupprimerMorceau.setFocusPainted(false);
        btnSupprimerMorceau.setBorderPainted(false);
        btnSupprimerMorceau.setFont(new Font("Arial", Font.BOLD, 11));
        btnSupprimerMorceau.setPreferredSize(new Dimension(40, 35));
        btnSupprimerMorceau.setActionCommand(id);
        
        boutonsPlayParMorceau.put(cle, btnPlayMorceau);
        boutonsPauseParMorceau.put(cle, btnPauseMorceau);
        boutonsSupprimerParMorceau.put(cle, btnSupprimerMorceau);
        infoMorceauxAffichee.put(cle, new InfoMorceau(id, titre, artiste, duree));

        ligne.add(texte, BorderLayout.CENTER);
        
        JPanel droite = new JPanel(new BorderLayout());
        droite.setOpaque(false);
        droite.add(lblDuree, BorderLayout.CENTER);
        
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        boutons.setOpaque(false);
        boutons.add(btnPlayMorceau);
        boutons.add(btnPauseMorceau);
        boutons.add(btnSupprimerMorceau);
        droite.add(boutons, BorderLayout.EAST);
        
        ligne.add(droite, BorderLayout.EAST);
        
        return ligne;
    }

    public String getTitreAjout() { return champAjout.getText().trim(); }
    public void viderChampAjout() { champAjout.setText(""); }
    
    public Map<String, InfoMorceau> getInfoMorceaux() {
        return infoMorceauxAffichee;
    }
    
    public Map<String, String[]> getDonneesMorceaux() {
        return donneesMorceau;
    }
    
    public String[] getDonneesMorceau(String cle) {
        return donneesMorceau.get(cle);
    }
    
    public Map<String, JButton> getBoutonsPlayParMorceaux() {
        return boutonsPlayParMorceau;
    }
    
    public Map<String, JButton> getBoutonsPauseParMorceaux() {
        return boutonsPauseParMorceau;
    }
    
    public JButton getBoutonPlayMorceau(String morceauId, String titre) {
        String cle = morceauId + "_" + titre;
        return boutonsPlayParMorceau.get(cle);
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurAjouterMorceau(ActionListener al) { btnAjouterMorceau.addActionListener(al); }
    public void ajouterEcouteurAjouter(ActionListener al) { btnAjouterMorceau.addActionListener(al); }
    
    public void ajouterEcouteurPlayMorceau(ActionListener al) {
        for (JButton btn : boutonsPlayParMorceau.values()) {
            btn.addActionListener(al);
        }
    }
    
    public void ajouterEcouteurPauseMorceau(ActionListener al) {
        for (JButton btn : boutonsPauseParMorceau.values()) {
            btn.addActionListener(al);
        }
    }
    
    public Map<String, JButton> getBoutonsSupprimerParMorceaux() {
        return boutonsSupprimerParMorceau;
    }
}