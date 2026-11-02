package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VueGestionUtilisateur extends JPanel {

    private JButton btnRetour;
    private JButton btnAjouter;
    private JButton btnModifierRole;
    private JButton btnSupprimer;
    private JTable tableUtilisateurs;
    private DefaultTableModel modelUtilisateurs;
    private JTextField txtEmail;
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JComboBox<String> cmbRole;
    private JTextField txtEmailModifier;
    private JComboBox<String> cmbRoleModifier;
    private JTextField txtEmailSupprimer;

    public VueGestionUtilisateur() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Gestion des Utilisateurs");
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

        // Panneau gauche : Affichage des utilisateurs
        JPanel panneauGauche = new JPanel(new BorderLayout());
        panneauGauche.setOpaque(false);
        panneauGauche.setBorder(BorderFactory.createTitledBorder("Utilisateurs"));
        
        // Créer le modèle de table
        String[] colonnes = {"ID", "Email", "Nom", "Prénom", "Rôle"};
        modelUtilisateurs = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableUtilisateurs = new JTable(modelUtilisateurs);
        tableUtilisateurs.setFont(new Font("Arial", Font.PLAIN, 11));
        tableUtilisateurs.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableUtilisateurs.getTableHeader().setBackground(new Color(204, 127, 151));
        tableUtilisateurs.getTableHeader().setForeground(Color.WHITE);
        tableUtilisateurs.setRowHeight(25);
        tableUtilisateurs.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableUtilisateurs.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableUtilisateurs.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableUtilisateurs.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableUtilisateurs.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
        panneauGauche.add(scrollPane, BorderLayout.CENTER);

        // Panneau droit : Ajouter/Modifier/Supprimer
        JPanel panneauDroit = new JPanel(new GridLayout(3, 1, 10, 10));
        panneauDroit.setOpaque(false);
        panneauDroit.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panneauDroit.setPreferredSize(new Dimension(280, 600));

        panneauDroit.add(creerPanelAjouter());
        panneauDroit.add(creerPanelModifier());
        panneauDroit.add(creerPanelSupprimer());

        centre.add(panneauGauche, BorderLayout.CENTER);
        centre.add(panneauDroit, BorderLayout.EAST);
        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerPanelAjouter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(251, 212, 222));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Ajouter un utilisateur"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(new JLabel("Email :"));
        txtEmail = new JTextField(15);
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Nom :"));
        txtNom = new JTextField(15);
        panel.add(txtNom);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Prénom :"));
        txtPrenom = new JTextField(15);
        panel.add(txtPrenom);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Rôle :"));
        cmbRole = new JComboBox<>(new String[]{"Client", "Admin"});
        panel.add(cmbRole);
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

    private JPanel creerPanelModifier() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 185, 207));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Modifier le rôle"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(new JLabel("Email :"));
        txtEmailModifier = new JTextField(15);
        panel.add(txtEmailModifier);
        panel.add(Box.createVerticalStrut(8));

        panel.add(new JLabel("Nouveau rôle :"));
        cmbRoleModifier = new JComboBox<>(new String[]{"Client", "Admin"});
        panel.add(cmbRoleModifier);
        panel.add(Box.createVerticalStrut(15));

        btnModifierRole = new JButton("Modifier");
        btnModifierRole.setBackground(new Color(200, 180, 100));
        btnModifierRole.setForeground(Color.WHITE);
        btnModifierRole.setFocusPainted(false);
        btnModifierRole.setBorderPainted(false);
        btnModifierRole.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(btnModifierRole);

        return panel;
    }

    private JPanel creerPanelSupprimer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(247, 173, 195));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Supprimer un utilisateur"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(new JLabel("Email à supprimer :"));
        txtEmailSupprimer = new JTextField(15);
        panel.add(txtEmailSupprimer);
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

    public void afficherUtilisateurs(String contenu) {
        modelUtilisateurs.setRowCount(0);
        
        String[] lignes = contenu.split("\n");
        for (String ligne : lignes) {
            if (!ligne.trim().isEmpty()) {
                String[] parts = ligne.split(";");
                if (parts.length >= 7) {
                    String roleText = "1".equals(parts[1]) ? "Client" : "2".equals(parts[1]) ? "Admin" : "Visiteur";
                    Object[] row = {
                        parts[0],           // ID
                        parts[3],           // Email
                        parts[4],           // Nom
                        parts[5],           // Prénom
                        roleText            // Rôle
                    };
                    modelUtilisateurs.addRow(row);
                }
            }
        }
    }

    public String getEmail() { return txtEmail.getText(); }
    public String getNom() { return txtNom.getText(); }
    public String getPrenom() { return txtPrenom.getText(); }
    public int getRole() { return cmbRole.getSelectedIndex() == 1 ? 2 : 1; }
    
    public String getEmailModifier() { return txtEmailModifier.getText(); }
    public int getRoleModifier() { return cmbRoleModifier.getSelectedIndex() == 1 ? 2 : 1; }
    
    public String getEmailSupprimer() { return txtEmailSupprimer.getText(); }

    public void reinitialiserFormulaires() {
        txtEmail.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        cmbRole.setSelectedIndex(0);
        txtEmailModifier.setText("");
        cmbRoleModifier.setSelectedIndex(0);
        txtEmailSupprimer.setText("");
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurAjouter(ActionListener al) { btnAjouter.addActionListener(al); }
    public void ajouterEcouteurModifier(ActionListener al) { btnModifierRole.addActionListener(al); }
    public void ajouterEcouteurSupprimer(ActionListener al) { btnSupprimer.addActionListener(al); }
}
