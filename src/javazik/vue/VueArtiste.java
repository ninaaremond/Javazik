package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class VueArtiste extends JPanel {

    private JButton btnRetour;
    private JPanel contenuInfo;
    private JPanel contenMorceaux;
    private Map<String, String[]> donneesMorceau;
    private Map<String, JButton> boutonsPlayParMorceau;
    private Map<String, JButton> boutonsPauseParMorceau;

    public VueArtiste() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        donneesMorceau = new java.util.HashMap<>();
        boutonsPlayParMorceau = new java.util.HashMap<>();
        boutonsPauseParMorceau = new java.util.HashMap<>();
        construireUI();
    }

    private void construireUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Artiste");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour au Menu");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Wrapper pour contenu scrollable
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(253, 235, 241));

        // Panel infos à gauche
        contenuInfo = new JPanel();
        contenuInfo.setLayout(new BoxLayout(contenuInfo, BoxLayout.Y_AXIS));
        contenuInfo.setBackground(new Color(253, 235, 241));
        contenuInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JScrollPane scrollInfo = new JScrollPane(contenuInfo);
        scrollInfo.setBorder(null);
        scrollInfo.getVerticalScrollBar().setUnitIncrement(16);

        // Panel morceaux à gauche aussi
        contenMorceaux = new JPanel();
        contenMorceaux.setLayout(new BoxLayout(contenMorceaux, BoxLayout.Y_AXIS));
        contenMorceaux.setBackground(new Color(253, 235, 241));
        contenMorceaux.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        JScrollPane scrollMorceaux = new JScrollPane(contenMorceaux);
        scrollMorceaux.setBorder(null);
        scrollMorceaux.getVerticalScrollBar().setUnitIncrement(16);

        // Ajouter les deux panels au wrapper
        wrapper.add(scrollInfo, BorderLayout.NORTH);
        wrapper.add(scrollMorceaux, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Affiche tous les infos en une seule fois
    public void afficherComplet(String nom, String annee, String resume, 
                                java.util.Set<String> albums, List<String[]> morceaux) {
        contenuInfo.removeAll();
        contenMorceaux.removeAll();
        donneesMorceau.clear();
        boutonsPlayParMorceau.clear();
        boutonsPauseParMorceau.clear();
        
        // === Infos artiste dans contenuInfo ===
        JLabel lblNom = new JLabel(nom);
        lblNom.setFont(new Font("Arial", Font.BOLD, 24));
        lblNom.setForeground(new Color(80, 40, 120));
        lblNom.setMaximumSize(new Dimension(700, 35));
        contenuInfo.add(lblNom);
        contenuInfo.add(Box.createVerticalStrut(8));
        
        JLabel lblAnnee = new JLabel("Année : " + annee);
        lblAnnee.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAnnee.setForeground(new Color(100, 100, 100));
        lblAnnee.setMaximumSize(new Dimension(700, 20));
        contenuInfo.add(lblAnnee);
        contenuInfo.add(Box.createVerticalStrut(12));
        
        JLabel lblResume = new JLabel("<html>" + resume + "</html>");
        lblResume.setFont(new Font("Arial", Font.PLAIN, 12));
        lblResume.setForeground(new Color(80, 80, 80));
        lblResume.setMaximumSize(new Dimension(700, Short.MAX_VALUE));
        contenuInfo.add(lblResume);
        contenuInfo.add(Box.createVerticalStrut(20));
        
        // === Albums dans contenuInfo ===
        if (albums != null && !albums.isEmpty()) {
            JLabel lblAlbumsTitle = new JLabel("Albums");
            lblAlbumsTitle.setFont(new Font("Arial", Font.BOLD, 14));
            lblAlbumsTitle.setForeground(new Color(80, 40, 120));
            lblAlbumsTitle.setMaximumSize(new Dimension(700, 25));
            contenuInfo.add(lblAlbumsTitle);
            
            for (String album : albums) {
                JLabel lbl = new JLabel("• " + album);
                lbl.setFont(new Font("Arial", Font.PLAIN, 11));
                lbl.setForeground(new Color(100, 100, 100));
                lbl.setMaximumSize(new Dimension(700, 20));
                contenuInfo.add(lbl);
            }
            contenuInfo.add(Box.createVerticalStrut(20));
        }
        
        contenuInfo.add(Box.createVerticalGlue());
        contenuInfo.revalidate();
        contenuInfo.repaint();
        
        // === Morceaux dans contenMorceaux ===
        if (morceaux != null && !morceaux.isEmpty()) {
            JLabel lblMorceauxTitle = new JLabel("Morceaux");
            lblMorceauxTitle.setFont(new Font("Arial", Font.BOLD, 14));
            lblMorceauxTitle.setForeground(new Color(80, 40, 120));
            lblMorceauxTitle.setMaximumSize(new Dimension(700, 25));
            contenMorceaux.add(lblMorceauxTitle);
            contenMorceaux.add(Box.createVerticalStrut(8));
            
            for (String[] m : morceaux) {
                contenMorceaux.add(creerLigneMorceau(m));
                contenMorceaux.add(Box.createVerticalStrut(8));
            }
        }
        
        contenMorceaux.add(Box.createVerticalGlue());
        contenMorceaux.revalidate();
        contenMorceaux.repaint();
    }

    private JPanel creerLigneMorceau(String[] data) {
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setBackground(new Color(251, 212, 222));
        ligne.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        ligne.setMaximumSize(new Dimension(700, 75));
        
        String id = data.length > 0 ? data[0] : "";
        String titre = data.length > 2 ? data[2] : "Inconnu";
        String duree = data.length > 3 ? data[3] : "";
        String album = data.length > 4 ? data[4] : "";
        
        String cle = id + "_" + titre;
        donneesMorceau.put(cle, data);
        
        // Texte à gauche
        JPanel texte = new JPanel();
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        texte.setOpaque(false);
        
        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitre.setForeground(new Color(80, 40, 120));
        texte.add(lblTitre);
        
        String infos = (album.isEmpty() ? "" : "Album: " + album);
        if (!duree.isEmpty()) {
            infos += (infos.isEmpty() ? "" : " | ") + "Durée: " + duree;
        }
        JLabel lblInfos = new JLabel(infos);
        lblInfos.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInfos.setForeground(new Color(100, 100, 100));
        texte.add(lblInfos);
        
        // Boutons Jouer et Pause
        JButton btnPlay = new JButton(" Jouer");
        btnPlay.setBackground(new Color(100, 200, 100));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);
        btnPlay.setBorderPainted(false);
        btnPlay.setFont(new Font("Arial", Font.BOLD, 11));
        btnPlay.setPreferredSize(new Dimension(80, 35));
        
        JButton btnPause = new JButton(" Pause");
        btnPause.setBackground(new Color(200, 180, 100));
        btnPause.setForeground(Color.WHITE);
        btnPause.setFocusPainted(false);
        btnPause.setBorderPainted(false);
        btnPause.setFont(new Font("Arial", Font.BOLD, 11));
        btnPause.setPreferredSize(new Dimension(80, 35));
        
        boutonsPlayParMorceau.put(cle, btnPlay);
        boutonsPauseParMorceau.put(cle, btnPause);
        
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        boutons.setOpaque(false);
        boutons.add(btnPlay);
        boutons.add(btnPause);
        
        ligne.add(texte, BorderLayout.CENTER);
        ligne.add(boutons, BorderLayout.EAST);
        
        return ligne;
    }

    public void ajouterEcouteurRetour(ActionListener al) {
        btnRetour.addActionListener(al);
    }

    public Map<String, JButton> getBoutonsPlayParMorceaux() {
        return boutonsPlayParMorceau;
    }

    public Map<String, JButton> getBoutonsPauseParMorceaux() {
        return boutonsPauseParMorceau;
    }

    public Map<String, String[]> getDonneesMorceau(String cle) {
        return donneesMorceau;
    }
}
