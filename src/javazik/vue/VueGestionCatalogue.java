package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VueGestionCatalogue extends JPanel {

    private JButton btnRetour;
    private JButton btnAjouter;
    private JButton btnSupprimer;
    private JTable tableCatalogue;
    private DefaultTableModel modelCatalogue;
    private JTextField txtArtiste;
    private JTextField txtTitre;
    private JTextField txtDuree;
    private JTextField txtAlbum;
    private JTextField txtMorceauASupprimer;

    public VueGestionCatalogue() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Gestion du Catalogue");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout());
        centre.setOpaque(false);
        centre.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panneau gauche : Affichage du catalogue
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setOpaque(false);
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Catalogue actuel"));
        
        // Créer le modèle de table
        String[] colonnes = {"ID", "Artiste / Groupe", "Titre", "Durée", "Album"};
        modelCatalogue = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCatalogue = new JTable(modelCatalogue);
        tableCatalogue.setFont(new Font("Arial", Font.PLAIN, 11));
        tableCatalogue.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableCatalogue.getTableHeader().setBackground(new Color(204, 127, 151));
        tableCatalogue.getTableHeader().setForeground(Color.WHITE);
        tableCatalogue.setRowHeight(25);
        tableCatalogue.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableCatalogue.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableCatalogue.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableCatalogue.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableCatalogue.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tableCatalogue);
        panneauGauche.add(scrollPane, BorderLayout.CENTER);

        // Panneau droit : Ajouter/Supprimer
        JPanel panneauDroit = new JPanel(new GridLayout(2, 1, 10, 10));
        panneauDroit.setOpaque(false);
        panneauDroit.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        // Sous-panneau : Ajouter
        JPanel panelAjouter = creerPanelAjouter();
        panneauDroit.add(panelAjouter);

        // Sous-panneau : Supprimer
        JPanel panelSupprimer = creerPanelSupprimer();
        panneauDroit.add(panelSupprimer);

        centre.add(panneauGauche, BorderLayout.CENTER);
        centre.add(panneauDroit, BorderLayout.EAST);
        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerPanelAjouter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(251, 212, 222));
        panel.setBorder(BorderFactory.createTitledBorder("Ajouter un morceau"));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Ajouter un morceau"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(new JLabel("Artiste / Groupe :"));
        txtArtiste = new JTextField(15);
        panel.add(txtArtiste);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Titre :"));
        txtTitre = new JTextField(15);
        panel.add(txtTitre);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Durée (mm,ss) :"));
        txtDuree = new JTextField(15);
        panel.add(txtDuree);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Album :"));
        txtAlbum = new JTextField(15);
        panel.add(txtAlbum);
        panel.add(Box.createVerticalStrut(15));

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(100, 200, 100));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBorderPainted(false);
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(btnAjouter);

        return panel;
    }

    private JPanel creerPanelSupprimer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(247, 173, 195));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Supprimer un morceau"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(new JLabel("ID du morceau à supprimer :"));
        txtMorceauASupprimer = new JTextField(15);
        panel.add(txtMorceauASupprimer);
        panel.add(Box.createVerticalStrut(15));

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(200, 100, 100));
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBorderPainted(false);
        btnSupprimer.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(btnSupprimer);

        return panel;
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    public void afficherCatalogue(String contenu) {
        // Vider la table
        modelCatalogue.setRowCount(0);
        
        // Parcourir les lignes du catalogue
        String[] lignes = contenu.split("\n");
        for (String ligne : lignes) {
            if (!ligne.trim().isEmpty()) {
                String[] parts = ligne.split(";");
                if (parts.length >= 5) {
                    Object[] row = {
                        parts[0],                    // ID
                        parts[1],                    // Artiste
                        parts[2],                    // Titre
                        parts[3],                    // Durée
                        parts[4]                     // Album
                    };
                    modelCatalogue.addRow(row);
                }
            }
        }
    }

    public String getArtiste() { return txtArtiste.getText(); }
    public String getTitre() { return txtTitre.getText(); }
    public String getDuree() { return txtDuree.getText(); }
    public String getAlbum() { return txtAlbum.getText(); }
    public String getMorceauASupprimer() { return txtMorceauASupprimer.getText(); }

    public void reinitialiserFormulaires() {
        txtArtiste.setText("");
        txtTitre.setText("");
        txtDuree.setText("");
        txtAlbum.setText("");
        txtMorceauASupprimer.setText("");
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurAjouter(ActionListener al) { btnAjouter.addActionListener(al); }
    public void ajouterEcouteurSupprimer(ActionListener al) { btnSupprimer.addActionListener(al); }
}
