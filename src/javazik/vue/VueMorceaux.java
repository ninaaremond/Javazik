package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class VueMorceaux extends JPanel {

    private JButton btnRetour;
    private JPanel listeMorceaux;
    private JScrollPane scrollPane;
    private Map<String, JButton> boutonsPlayParMorceau;
    private Map<String, String[]> donneesMorceau;
    private Map<String, JButton> boutonsDetailMorceau;

    public VueMorceaux() {
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
        JLabel titre = new JLabel("JAVAZIK - Les morceaux");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour au Menu");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Liste
        listeMorceaux = new JPanel();
        listeMorceaux.setLayout(new BoxLayout(listeMorceaux, BoxLayout.Y_AXIS));
        listeMorceaux.setBackground(new Color(253, 235, 241));
        listeMorceaux.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scrollPane = new JScrollPane(listeMorceaux);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void afficherMorceaux(List<String[]> morceaux) {
        listeMorceaux.removeAll();
        boutonsPlayParMorceau.clear();
        donneesMorceau.clear();
        boutonsDetailMorceau.clear();
        if (morceaux == null || morceaux.isEmpty()) {
            JLabel vide = new JLabel("Aucun morceau disponible.");
            vide.setFont(new Font("Arial", Font.ITALIC, 14));
            listeMorceaux.add(vide);
        } else {
            for (String[] m : morceaux) {
                listeMorceaux.add(creerLigneMorceau(m));
                listeMorceaux.add(Box.createVerticalStrut(8));
            }
        }
        listeMorceaux.revalidate();
        listeMorceaux.repaint();
    }

    private JPanel creerLigneMorceau(String[] data) {
        // data: [id, artiste, titre, duree, album, annee, ecoutes, groupe]
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setBackground(new Color(251, 212, 222));
        ligne.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        String id = data.length > 0 ? data[0] : "";
        String titre = data.length > 2 ? data[2] : "Inconnu";
        String artiste = data.length > 1 ? data[1] : "";

        // Bouton titre (cliquable)
        JButton btnTitre = new JButton(titre);
        btnTitre.setFont(new Font("Arial", Font.BOLD, 14));
        btnTitre.setForeground(new Color(80, 40, 120));
        btnTitre.setBackground(new Color(251, 212, 222));
        btnTitre.setBorderPainted(false);
        btnTitre.setFocusPainted(false);
        btnTitre.setContentAreaFilled(false);
        btnTitre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTitre.setHorizontalAlignment(SwingConstants.LEFT);
        btnTitre.setActionCommand(id + "_" + titre);
        
        JLabel lblArtiste = new JLabel(artiste.isEmpty() ? "Artiste inconnu" : artiste);
        lblArtiste.setFont(new Font("Arial", Font.PLAIN, 12));
        lblArtiste.setForeground(new Color(204, 127, 151));

        JPanel texte = new JPanel();
        texte.setLayout(new BoxLayout(texte, BoxLayout.Y_AXIS));
        texte.setOpaque(false);
        texte.add(btnTitre);
        texte.add(lblArtiste);

        // Stocker le bouton détail
        String cle = id + "_" + titre;
        boutonsDetailMorceau.put(cle, btnTitre);

        // Boutons Play et Pause
        JButton btnPlay = new JButton(" Jouer");
        btnPlay.setBackground(new Color(100, 200, 100));
        btnPlay.setForeground(Color.WHITE);
        btnPlay.setFocusPainted(false);
        btnPlay.setBorderPainted(false);
        btnPlay.setFont(new Font("Arial", Font.BOLD, 11));
        btnPlay.setPreferredSize(new Dimension(80, 35));
        btnPlay.setActionCommand(id + "_" + titre);
        
        JButton btnPause = new JButton(" Pause");
        btnPause.setBackground(new Color(200, 180, 100));
        btnPause.setForeground(Color.WHITE);
        btnPause.setFocusPainted(false);
        btnPause.setBorderPainted(false);
        btnPause.setFont(new Font("Arial", Font.BOLD, 11));
        btnPause.setPreferredSize(new Dimension(80, 35));
        btnPause.setActionCommand(id + "_" + titre);
        
        boutonsPlayParMorceau.put(cle + "_play", btnPlay);
        donneesMorceau.put(cle, data);  // Stocker les données complètes du morceau
        boutonsPlayParMorceau.put(cle + "_pause", btnPause);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        boutons.setOpaque(false);
        boutons.add(btnPlay);
        boutons.add(btnPause);

        ligne.add(texte, BorderLayout.CENTER);
        ligne.add(boutons, BorderLayout.EAST);
        return ligne;
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public Map<String, JButton> getBoutonsDetailMorceau() {
        return boutonsDetailMorceau;
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    
    public Map<String, JButton> getBoutonsPlayParMorceaux() {
        return boutonsPlayParMorceau;
    }
    
    public Map<String, String[]> getDonneesMorceaux() {
        return donneesMorceau;
    }
    
    public String[] getDonneesMorceau(String cle) {
        return donneesMorceau.get(cle);
    }
    
    public void ajouterEcouteurPlayMorceau(ActionListener al) {
        for (JButton btn : boutonsPlayParMorceau.values()) {
            if (btn.getText().contains("Jouer")) {
                btn.addActionListener(al);
            }
        }
    }
    
    public void ajouterEcouteurPauseMorceau(ActionListener al) {
        for (JButton btn : boutonsPlayParMorceau.values()) {
            if (btn.getText().contains("Pause")) {
                btn.addActionListener(al);
            }
        }
    }
}