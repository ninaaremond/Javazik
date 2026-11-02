package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class VueHistoriqueEcoute extends JPanel {

    private JButton btnRetour;
    private JButton btnEffacer;
    private JPanel listeHistorique;
    private JScrollPane scrollPane;

    public VueHistoriqueEcoute() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Historique d'écoute");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour au Menu");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Contenu principal
        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(new Color(253, 235, 241));

        // Liste d'historique
        listeHistorique = new JPanel();
        listeHistorique.setLayout(new BoxLayout(listeHistorique, BoxLayout.Y_AXIS));
        listeHistorique.setBackground(new Color(253, 235, 241));
        listeHistorique.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        scrollPane = new JScrollPane(listeHistorique);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contenu.add(scrollPane, BorderLayout.CENTER);

        // Bouton Effacer
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bas.setOpaque(false);
        bas.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        btnEffacer = new JButton("Effacer l'historique");
        btnEffacer.setBackground(new Color(200, 100, 100));
        btnEffacer.setForeground(Color.WHITE);
        btnEffacer.setFocusPainted(false);
        btnEffacer.setBorderPainted(false);
        btnEffacer.setFont(new Font("Arial", Font.BOLD, 12));
        bas.add(btnEffacer);
        contenu.add(bas, BorderLayout.SOUTH);

        add(contenu, BorderLayout.CENTER);
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(253, 235, 241));
        btn.setForeground(new Color(204, 127, 151));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void afficherHistorique(ArrayList<String> entries) {
        listeHistorique.removeAll();
        
        if (entries == null || entries.isEmpty()) {
            JLabel vide = new JLabel("Aucune lecture enregistrée.");
            vide.setFont(new Font("Arial", Font.ITALIC, 14));
            vide.setForeground(new Color(150, 150, 150));
            listeHistorique.add(vide);
        } else {
            for (String entry : entries) {
                listeHistorique.add(creerLigneHistorique(entry));
                listeHistorique.add(Box.createVerticalStrut(8));
            }
        }
        
        listeHistorique.revalidate();
        listeHistorique.repaint();
    }

    private JPanel creerLigneHistorique(String entry) {
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setBackground(new Color(251, 212, 222));
        ligne.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblEntree = new JLabel(entry);
        lblEntree.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblEntree.setForeground(new Color(80, 40, 120));

        ligne.add(lblEntree, BorderLayout.CENTER);
        return ligne;
    }

    public void ajouterEcouteurRetour(ActionListener al) {
        btnRetour.addActionListener(al);
    }

    public void ajouterEcouteurEffacer(ActionListener al) {
        btnEffacer.addActionListener(al);
    }
}
