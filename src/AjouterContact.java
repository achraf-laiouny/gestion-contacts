import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/**
 * La classe `AjouterContact` permet d'ajouter un nouveau contact à la base de données
 * et de mettre à jour le tableau de contacts affiché dans l'application.
 * Elle fournit une interface utilisateur avec des champs pour saisir les informations
 * du contact et des validations pour assurer l'intégrité des données.
 * Elle hérite de `JFrame`, ce qui en fait une fenêtre graphique.
 */
public class AjouterContact extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private HashMap<String, Integer> villesMap = new HashMap<>();
    private HashMap<Integer, String> villesInverseMap = new HashMap<>();
    private HashMap<String, Integer> categoriesMap = new HashMap<>();
    private JComboBox<String> villeComboBox;
    private JComboBox<String> categorieComboBox;
    private JTextField nouvelleVilleTextField;
    private final DefaultTableModel contactTableModel;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void loadDataIntoComboBox(JComboBox<String> comboBox, String tableName, String nameColumn, String idColumn, HashMap<String, Integer> map, boolean allowNew) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT " + idColumn + ", " + nameColumn + " FROM " + tableName)) {

            while (rs.next()) {
                int id = rs.getInt(idColumn);
                String name = rs.getString(nameColumn);
                comboBox.addItem(name);
                map.put(name, id);
                if ("Ville".equals(tableName)) {
                    villesInverseMap.put(id, name);
                }
            }
            if (allowNew) {
                comboBox.addItem("Ajouter une nouvelle ville...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données depuis " + tableName);
        }
    }

    public AjouterContact(final DefaultTableModel model) {
        this.contactTableModel = model;
        setTitle("Ajouter un Contact");
        setSize(400, 450);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        String[] labels = {"Nom:", "Prénom:", "Libellé:", "Tél. Perso:", "Tél. Pro:", "Email:", "Sexe:", "Ville:", "Catégorie:"};
        JTextField[] fields = new JTextField[labels.length - 3];
        villeComboBox = new JComboBox<>();
        categorieComboBox = new JComboBox<>();
        nouvelleVilleTextField = new JTextField();
        nouvelleVilleTextField.setVisible(false);
        JComboBox<String> sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        loadDataIntoComboBox(villeComboBox, "Ville", "NomVille", "NumVille", villesMap, true);
        loadDataIntoComboBox(categorieComboBox, "Categorie", "NomCategorie", "NumCat", categoriesMap, false);

        JPanel villePanelContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbcVille = new GridBagConstraints();
        gbcVille.insets = new Insets(0, 0, 0, 0);
        gbcVille.anchor = GridBagConstraints.WEST;
        gbcVille.fill = GridBagConstraints.HORIZONTAL;
        gbcVille.weightx = 1.0;

        gbcVille.gridx = 0;
        gbcVille.gridy = 0;
        villePanelContainer.add(villeComboBox, gbcVille);

        gbcVille.gridx = 0;
        gbcVille.gridy = 1;
        villePanelContainer.add(nouvelleVilleTextField, gbcVille);

        int row = 0;
        for (int i = 0; i < labels.length - 3; i++) {
            gbc.gridx = 0;
            gbc.gridy = row;
            mainPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField();
            mainPanel.add(fields[i], gbc);
            row++;
        }

        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel(labels[6]), gbc);
        gbc.gridx = 1;
        mainPanel.add(sexeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel(labels[7]), gbc);
        gbc.gridx = 1;
        mainPanel.add(villePanelContainer, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel(labels[8]), gbc);
        gbc.gridx = 1;
        mainPanel.add(categorieComboBox, gbc);

        villeComboBox.addActionListener(e -> {
            boolean isAddingNewCity = "Ajouter une nouvelle ville...".equals(villeComboBox.getSelectedItem());
            nouvelleVilleTextField.setVisible(isAddingNewCity);
            if (isAddingNewCity) {
                nouvelleVilleTextField.setText("");
                SwingUtilities.invokeLater(AjouterContact.this::pack);
            } else {
                SwingUtilities.invokeLater(AjouterContact.this::pack);
            }
        });

        nouvelleVilleTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {}

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = fields[0].getText().trim();
                String prenom = fields[1].getText().trim();
                String telPerso = fields[3].getText().trim();
                String telPro = fields[4].getText().trim();
                String email = fields[5].getText().trim();
                String libelle = fields[2].getText().trim();
                String sexe = sexeComboBox.getSelectedItem().toString();
                String selectedVilleNom = (String) villeComboBox.getSelectedItem();
                String nouvelleVilleSaisie = nouvelleVilleTextField.getText().trim();
                String selectedCategorieNom = (String) categorieComboBox.getSelectedItem();
                int villeId;
                int categorieId;
                String villeNomPourTableau;

                if (!nom.matches("[a-zA-Z\\s\\-_]+")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le nom ne doit contenir que des lettres, des espaces, des tirets (-) ou des underscores (_).", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!prenom.matches("[a-zA-Z\\s\\-_]+")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le prénom ne doit contenir que des lettres, des espaces, des tirets (-) ou des underscores (_).", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!telPerso.matches("0\\d{9}")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le téléphone personnel doit commencer par 0 et contenir 10 chiffres.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!telPro.isEmpty() && !telPro.matches("0\\d{9}")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le téléphone professionnel doit commencer par 0 et contenir 10 chiffres.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "L'adresse email n'est pas au format valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ("Ajouter une nouvelle ville...".equals(selectedVilleNom)) {
                    if (nouvelleVilleSaisie.isEmpty()) {
                        JOptionPane.showMessageDialog(AjouterContact.this, "Veuillez saisir le nom de la nouvelle ville.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    villeId = ajouterNouvelleVille(nouvelleVilleSaisie);
                    if (villeId == -1) {
                        return;
                    }
                    villeNomPourTableau = nouvelleVilleSaisie;
                } else {
                    villeId = villesMap.get(selectedVilleNom);
                    villeNomPourTableau = selectedVilleNom;
                }

                categorieId = categoriesMap.get(selectedCategorieNom);
                int newContactId = -1;

                String query = "INSERT INTO contact (nom, prenom, libelle, telPerso, telPro, email, sexe, NumVille, NumCat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = AjouterContact.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, nom);
                    stmt.setString(2, prenom);
                    stmt.setString(3, libelle);
                    stmt.setString(4, telPerso);
                    stmt.setString(5, telPro);
                    stmt.setString(6, email);
                    stmt.setString(7, sexe);
                    stmt.setInt(8, villeId);
                    stmt.setInt(9, categorieId);

                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        ResultSet generatedKeys = stmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            newContactId = generatedKeys.getInt(1);
                        }
                    }

                    Object[] newRow = new Object[10];
                    newRow[0] = newContactId;
                    newRow[1] = nom;
                    newRow[2] = prenom;
                    newRow[3] = libelle;
                    newRow[4] = telPerso;
                    newRow[5] = telPro;
                    newRow[6] = email;
                    newRow[7] = sexe;
                    newRow[8] = villeNomPourTableau;
                    newRow[9] = selectedCategorieNom;

                    contactTableModel.addRow(newRow);
                    dispose();
                    JOptionPane.showMessageDialog(null, "Contact Ajouté");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du contact:\n" + ex.getMessage());
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private int ajouterNouvelleVille(String nomVille) {
        String insertVilleQuery = "INSERT INTO Ville (NomVille) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertVilleQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nomVille);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newVilleId = generatedKeys.getInt(1);
                    villesMap.put(nomVille, newVilleId);
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) villeComboBox.getModel();
                    model.insertElementAt(nomVille, model.getSize() - 1);
                    villeComboBox.setSelectedItem(nomVille);
                    return newVilleId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la nouvelle ville.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }
}