package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class VuePlaylists extends JPanel {

    private JButton btnRetour;
    private JButton btnAjouter;
    private JPanel listePanel;
    private ActionListener ecouteurOuvrirPlaylist;
    private ActionListener ecouteurSupprimerPlaylist;

    public VuePlaylists() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Mes playlists");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        listePanel = new JPanel();
        listePanel.setLayout(new BoxLayout(listePanel, BoxLayout.Y_AXIS));
        listePanel.setBackground(new Color(253, 235, 241));
        listePanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JScrollPane scroll = new JScrollPane(listePanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bas.setOpaque(false);
        btnAjouter = new JButton("+ Ajouter une playlist");
        btnAjouter.setBackground(new Color(204, 127, 151));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBorderPainted(false);
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 14));
        btnAjouter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bas.add(btnAjouter);
        add(bas, BorderLayout.SOUTH);
    }

    public void afficherPlaylists(List<String> noms) {
        listePanel.removeAll();
        if (noms == null || noms.isEmpty()) {
            listePanel.add(new JLabel("Aucune playlist. Créez-en une !"));
        } else {
            for (String nom : noms) {
                listePanel.add(creerLignePlaylist(nom));
                listePanel.add(Box.createVerticalStrut(8));
            }
        }
        listePanel.revalidate();
        listePanel.repaint();
    }

    private JPanel creerLignePlaylist(String nom) {
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        ligne.setBackground(new Color(251, 212, 222));
        ligne.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JButton btnOuvrir = new JButton("📋  " + nom);
        btnOuvrir.setBackground(new Color(251, 212, 222));
        btnOuvrir.setForeground(new Color(80, 40, 120));
        btnOuvrir.setFont(new Font("Arial", Font.BOLD, 14));
        btnOuvrir.setFocusPainted(false);
        btnOuvrir.setBorderPainted(false);
        btnOuvrir.setHorizontalAlignment(SwingConstants.LEFT);
        btnOuvrir.setActionCommand(nom);
        btnOuvrir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (ecouteurOuvrirPlaylist != null) {
            btnOuvrir.addActionListener(ecouteurOuvrirPlaylist);
        }

        JButton btnSupprimer = new JButton("🗑 Supprimer");
        btnSupprimer.setBackground(new Color(200, 100, 100));
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBorderPainted(false);
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 11));
        btnSupprimer.setActionCommand(nom);
        btnSupprimer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (ecouteurSupprimerPlaylist != null) {
            btnSupprimer.addActionListener(ecouteurSupprimerPlaylist);
        }

        ligne.add(btnOuvrir, BorderLayout.CENTER);
        ligne.add(btnSupprimer, BorderLayout.EAST);

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

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurAjouter(ActionListener al) { btnAjouter.addActionListener(al); }
    public void ajouterEcouteurOuvrirPlaylist(ActionListener al) { this.ecouteurOuvrirPlaylist = al; }
    
    public void ajouterEcouteurSupprimerPlaylist(ActionListener al) {
        this.ecouteurSupprimerPlaylist = al;
        // Ajouter l'écouteur à tous les boutons de suppression existants
        for (Component comp : listePanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel ligne = (JPanel) comp;
                for (Component c : ligne.getComponents()) {
                    if (c instanceof JButton) {
                        JButton btn = (JButton) c;
                        if (btn.getText().contains("Supprimer")) {
                            btn.addActionListener(al);
                        }
                    }
                }
            }
        }
    }
}