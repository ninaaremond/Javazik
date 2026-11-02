package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueDetailMorceau extends JPanel {

    private JButton btnRetour;
    private JLabel lblTitre;
    private JButton btnArtiste;
    private JLabel lblAnnee;
    private JLabel lblDuree;
    private JLabel lblEcoutes;
    private JButton btnJouer;
    private JButton btnArreter;
    private String morceauIdActuel;
    private String[] donneesActuelles;
    private String artisteActuel;

    public VueDetailMorceau() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titre = new JLabel("JAVAZIK - Détails du morceau");
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Contenu principal
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setOpaque(false);
        contenu.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));

        // Titre
        lblTitre = new JLabel("Titre du morceau");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitre.setForeground(new Color(80, 40, 120));
        lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenu.add(lblTitre);
        contenu.add(Box.createVerticalStrut(30));

        // Artiste (cliquable)
        btnArtiste = new JButton("Artiste");
        btnArtiste.setFont(new Font("Arial", Font.PLAIN, 22));
        btnArtiste.setForeground(new Color(204, 127, 151));
        btnArtiste.setBackground(new Color(253, 235, 241));
        btnArtiste.setBorderPainted(false);
        btnArtiste.setFocusPainted(false);
        btnArtiste.setContentAreaFilled(false);
        btnArtiste.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnArtiste.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenu.add(btnArtiste);
        contenu.add(Box.createVerticalStrut(25));

        // Année de sortie
        lblAnnee = new JLabel("Année: ");
        lblAnnee.setFont(new Font("Arial", Font.PLAIN, 16));
        lblAnnee.setForeground(new Color(100, 100, 100));
        lblAnnee.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenu.add(lblAnnee);
        contenu.add(Box.createVerticalStrut(15));

        // Durée
        lblDuree = new JLabel("Durée: ");
        lblDuree.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDuree.setForeground(new Color(100, 100, 100));
        lblDuree.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenu.add(lblDuree);
        contenu.add(Box.createVerticalStrut(15));

        // Nombre d'écoutes
        lblEcoutes = new JLabel("Nombre d'écoutes: ");
        lblEcoutes.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEcoutes.setForeground(new Color(100, 100, 100));
        lblEcoutes.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenu.add(lblEcoutes);
        contenu.add(Box.createVerticalStrut(50));

        // Bouton Jouer
        btnJouer = new JButton("▶ Jouer ce morceau");
        btnJouer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJouer.setMaximumSize(new Dimension(300, 50));
        btnJouer.setBackground(new Color(100, 200, 100));
        btnJouer.setForeground(Color.WHITE);
        btnJouer.setFont(new Font("Arial", Font.BOLD, 16));
        btnJouer.setFocusPainted(false);
        btnJouer.setBorderPainted(false);
        btnJouer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contenu.add(btnJouer);
        contenu.add(Box.createVerticalStrut(15));
        
        // Bouton Arrêter
        btnArreter = new JButton("⏹ Arrêter la musique");
        btnArreter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnArreter.setMaximumSize(new Dimension(300, 50));
        btnArreter.setBackground(new Color(200, 100, 100));
        btnArreter.setForeground(Color.WHITE);
        btnArreter.setFont(new Font("Arial", Font.BOLD, 16));
        btnArreter.setFocusPainted(false);
        btnArreter.setBorderPainted(false);
        btnArreter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contenu.add(btnArreter);

        JScrollPane scroll = new JScrollPane(contenu);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(253, 235, 241));
        btn.setForeground(new Color(204, 127, 151));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void afficherDetailMorceau(String titre, String artiste, String annee, String duree, String ecoutes) {
        lblTitre.setText(titre);
        btnArtiste.setText(artiste);
        this.artisteActuel = artiste;
        lblAnnee.setText("Année: " + annee);
        lblDuree.setText("Durée: " + duree + " min");
        
        // Formater le nombre d'écoutes avec séparateurs
        try {
            long nombreEcoutes = Long.parseLong(ecoutes.replace(",", ""));
            String ecoutesFormatees;
            if (nombreEcoutes >= 1_000_000_000) {
                ecoutesFormatees = String.format("%.1f", nombreEcoutes / 1_000_000_000.0) + " milliards";
            } else if (nombreEcoutes >= 1_000_000) {
                ecoutesFormatees = String.format("%.1f", nombreEcoutes / 1_000_000.0) + " millions";
            } else if (nombreEcoutes >= 1_000) {
                ecoutesFormatees = String.format("%.1f", nombreEcoutes / 1_000.0) + " milliers";
            } else {
                ecoutesFormatees = String.valueOf(nombreEcoutes);
            }
            lblEcoutes.setText("Nombre d'écoutes: " + ecoutesFormatees);
        } catch (Exception e) {
            lblEcoutes.setText("Nombre d'écoutes: " + ecoutes);
        }
        
        revalidate();
        repaint();
    }
    
    public void afficherDetailMorceau(String[] donneesMorceau) {
        // Format: ID;Artiste;Titre;Durée;Album;Année;Ecoutes
        this.donneesActuelles = donneesMorceau;
        if (donneesMorceau != null && donneesMorceau.length >= 6) {
            String titre = donneesMorceau[2];
            String artiste = donneesMorceau[1];
            String annee = donneesMorceau[5];
            String duree = donneesMorceau[3];
            String ecoutes = donneesMorceau.length > 6 ? donneesMorceau[6] : "0";
            
            afficherDetailMorceau(titre, artiste, annee, duree, ecoutes);
        }
    }
    
    public String[] getDonneesActuelles() {
        return donneesActuelles;
    }
    
    public void ajouterEcouteurArtiste(ActionListener al) {
        btnArtiste.addActionListener(al);
    }
    
    public String getArtisteActuel() {
        return artisteActuel;
    }

    public void ajouterEcouteurRetour(ActionListener al) {
        btnRetour.addActionListener(al);
    }

    public void ajouterEcouteurJouer(ActionListener al) {
        btnJouer.addActionListener(al);
    }
    
    public void ajouterEcouteurArreter(ActionListener al) {
        btnArreter.addActionListener(al);
    }
    
    public void definirBoutonJouer(String morceauId) {
        this.morceauIdActuel = morceauId;
        btnJouer.setActionCommand(morceauId);
    }
    
    public String getMorceauIdActuel() {
        return morceauIdActuel;
    }
}
