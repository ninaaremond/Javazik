package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class VueRecherche extends JPanel {

    private JButton btnRetour;
    private JTextField champArtisteGroupe;
    private JTextField champAlbum;
    private JTextField champMorceau;
    private JButton btnRechercherArtisteGroupe;
    private JButton btnRechercherAlbum;
    private JButton btnRechercherMorceau;
    private JPanel panneauResultats;
    private Map<String, JButton> boutonsPlayParMorceau;
    private Map<String, String[]> donneesMorceau;
    private Map<String, JButton> boutonsDetailMorceau;

    public VueRecherche() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        boutonsPlayParMorceau = new HashMap<>();
        donneesMorceau = new HashMap<>();
        boutonsDetailMorceau = new HashMap<>();
        construireUI();
    }

    private void construireUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Recherche");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour au Menu");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Formulaire de recherche
        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setOpaque(false);
        formulaire.setBorder(BorderFactory.createEmptyBorder(15, 30, 10, 30));

        champArtisteGroupe = new JTextField();
        btnRechercherArtisteGroupe = creerBoutonRecherche("Rechercher");
        formulaire.add(creerLigneRecherche("Artiste / Groupe", champArtisteGroupe, btnRechercherArtisteGroupe));
        formulaire.add(Box.createVerticalStrut(10));

        champAlbum = new JTextField();
        btnRechercherAlbum = creerBoutonRecherche("Rechercher");
        formulaire.add(creerLigneRecherche("Album", champAlbum, btnRechercherAlbum));
        formulaire.add(Box.createVerticalStrut(10));

        champMorceau = new JTextField();
        btnRechercherMorceau = creerBoutonRecherche("Rechercher");
        formulaire.add(creerLigneRecherche("Morceau", champMorceau, btnRechercherMorceau));

        // Résultats
        panneauResultats = new JPanel();
        panneauResultats.setLayout(new BoxLayout(panneauResultats, BoxLayout.Y_AXIS));
        panneauResultats.setBackground(new Color(253, 235, 241));
        panneauResultats.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JScrollPane scroll = new JScrollPane(panneauResultats);
        scroll.setBorder(null);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formulaire, scroll);
        split.setDividerLocation(180);
        split.setBorder(null);
        split.setOpaque(false);
        add(split, BorderLayout.CENTER);
    }

    private JPanel creerLigneRecherche(String label, JTextField champ, JButton btn) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setOpaque(false);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        JLabel lbl = new JLabel(label + " :");
        lbl.setPreferredSize(new Dimension(80, 38));
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        ligne.add(lbl, BorderLayout.WEST);
        ligne.add(champ, BorderLayout.CENTER);
        ligne.add(btn, BorderLayout.EAST);
        return ligne;
    }

    private JButton creerBoutonRecherche(String texte) {
        JButton btn = new JButton(texte);
        btn.setBackground(new Color(204, 127, 151));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void afficherResultats(List<String[]> resultats) {
        panneauResultats.removeAll();
        boutonsPlayParMorceau.clear();
        donneesMorceau.clear();
        boutonsDetailMorceau.clear();
        if (resultats == null || resultats.isEmpty()) {
            panneauResultats.add(new JLabel("Aucun résultat trouvé."));
        } else {
            for (String[] r : resultats) {
                JPanel ligne = new JPanel(new BorderLayout(10, 0));
                ligne.setBackground(new Color(230, 210, 255));
                ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                ligne.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                // Format: ID;Artiste;Titre;Durée;Album;Année;Ecoutes
                String id = r.length > 0 ? r[0] : "";
                String titre = r.length > 2 ? r[2] : "";
                String artiste = r.length > 1 ? r[1] : "";
                
                String cle = id + "_" + titre;
                
                // Bouton titre (cliquable)
                JButton btnTitre = new JButton(titre);
                btnTitre.setFont(new Font("Arial", Font.BOLD, 12));
                btnTitre.setForeground(new Color(80, 40, 120));
                btnTitre.setBackground(new Color(230, 210, 255));
                btnTitre.setBorderPainted(false);
                btnTitre.setFocusPainted(false);
                btnTitre.setContentAreaFilled(false);
                btnTitre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnTitre.setHorizontalAlignment(SwingConstants.LEFT);
                btnTitre.setActionCommand(cle);
                
                boutonsDetailMorceau.put(cle, btnTitre);
                
                String texte = "Artiste: " + artiste;
                if (r.length > 3 && !r[3].isEmpty()) texte += "  |  Durée: " + r[3];
                if (r.length > 4 && !r[4].isEmpty()) texte += "  |  Album: " + r[4];
                
                JLabel lblinfos = new JLabel(texte);
                lblinfos.setFont(new Font("Arial", Font.PLAIN, 11));
                
                JPanel textes = new JPanel(new BorderLayout(0, 3));
                textes.setOpaque(false);
                textes.add(btnTitre, BorderLayout.NORTH);
                textes.add(lblinfos, BorderLayout.CENTER);
                
                JButton btnPlay = new JButton("▶ Jouer");
                btnPlay.setBackground(new Color(100, 200, 100));
                btnPlay.setForeground(Color.WHITE);
                btnPlay.setFocusPainted(false);
                btnPlay.setBorderPainted(false);
                btnPlay.setFont(new Font("Arial", Font.BOLD, 11));
                btnPlay.setPreferredSize(new Dimension(80, 35));
                btnPlay.setActionCommand(cle);
                
                JButton btnPause = new JButton("⏸ Pause");
                btnPause.setBackground(new Color(200, 180, 100));
                btnPause.setForeground(Color.WHITE);
                btnPause.setFocusPainted(false);
                btnPause.setBorderPainted(false);
                btnPause.setFont(new Font("Arial", Font.BOLD, 11));
                btnPause.setPreferredSize(new Dimension(80, 35));
                btnPause.setActionCommand(cle);
                
                boutonsPlayParMorceau.put(cle + "_play", btnPlay);
                boutonsPlayParMorceau.put(cle + "_pause", btnPause);
                donneesMorceau.put(cle, r);
                
                JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                boutons.setOpaque(false);
                boutons.add(btnPlay);
                boutons.add(btnPause);
                
                ligne.add(textes, BorderLayout.CENTER);
                ligne.add(boutons, BorderLayout.EAST);
                panneauResultats.add(ligne);
                panneauResultats.add(Box.createVerticalStrut(5));
            }
        }
        panneauResultats.revalidate();
        panneauResultats.repaint();
    }

    public String getChampArtisteGroupe() { return champArtisteGroupe.getText().trim(); }
    public String getChampAlbum() { return champAlbum.getText().trim(); }
    public String getChampMorceau() { return champMorceau.getText().trim(); }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurRechercherArtisteGroupe(ActionListener al) { btnRechercherArtisteGroupe.addActionListener(al); }
    public void ajouterEcouteurRechercherAlbum(ActionListener al) { btnRechercherAlbum.addActionListener(al); }
    public void ajouterEcouteurRechercherMorceau(ActionListener al) { btnRechercherMorceau.addActionListener(al); }
    
    public Map<String, JButton> getBoutonsPlayParMorceaux() {
        return boutonsPlayParMorceau;
    }
    
    public Map<String, String[]> getDonneesMorceaux() {
        return donneesMorceau;
    }
    
    public String[] getDonneesMorceau(String cle) {
        return donneesMorceau.get(cle);
    }
    
    public Map<String, JButton> getBoutonsDetailMorceau() {
        return boutonsDetailMorceau;
    }
}