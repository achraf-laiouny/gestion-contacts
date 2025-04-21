import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.regex.Pattern;
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
    // Constantes pour l'URL, l'utilisateur et le mot de passe de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    // Map pour stocker les noms de villes et leurs IDs (chargées depuis la base)
    private HashMap<String, Integer> villesMap = new HashMap<>();
    // Map inverse pour récupérer le nom de la ville à partir de l'ID
    private HashMap<Integer, String> villesInverseMap = new HashMap<>();
    // Map pour stocker les noms de catégories et leurs IDs (chargées depuis la base)
    private HashMap<String, Integer> categoriesMap = new HashMap<>();
    // La JComboBox pour les villes est déclarée ici pour être accessible dans toute la classe
    private JComboBox<String> villeComboBox;
    // La JComboBox pour les catégories
    private JComboBox<String> categorieComboBox;
    // Le JTextField pour la nouvelle ville est déclaré ici pour être accessible dans toute la classe
    private JTextField nouvelleVilleTextField;

    /**
     * Méthode statique pour établir une connexion à la base de données.
     * @return Une instance de `Connection` à la base de données.
     * @throws SQLException Si une erreur de base de données se produit lors de la connexion.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Méthode pour charger les données (villes ou catégories) dans une JComboBox depuis la base de données.
     * @param comboBox La JComboBox à remplir.
     * @param tableName Le nom de la table dans la base de données.
     * @param nameColumn Le nom de la colonne contenant le nom à afficher dans la ComboBox.
     * @param idColumn Le nom de la colonne contenant l'ID correspondant.
     * @param map La HashMap pour stocker les noms et les IDs.
     * @param allowNew Indique si une option pour ajouter un nouveau champ doit être ajoutée.
     */
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
                    villesInverseMap.put(id, name); // Remplir la map inverse pour les villes
                }
            }
            if (allowNew) {
                comboBox.addItem("Ajouter une nouvelle ville..."); // Ajouter une option pour permettre l'ajout d'une nouvelle ville
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données depuis " + tableName);
        }
    }

    /**
     * Constructeur de la classe `AjouterContact`.
     * @param model Le modèle de table contenant les contacts (pour ajouter la nouvelle ligne).
     */
    public AjouterContact(final DefaultTableModel model) { // Model final car utilisé dans l'ActionListener
        setTitle("Ajouter un Contact");
        setSize(400, 450);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour une meilleure structure
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche des labels
        gbc.fill = GridBagConstraints.HORIZONTAL; // Extension horizontale des champs de saisie
        gbc.weightx = 1.0; // Distribution de l'espace horizontal supplémentaire aux champs

        String[] labels = {"Nom:", "Prénom:", "Libellé:", "Tél. Perso:", "Tél. Pro:", "Email:", "Sexe:", "Ville:", "Catégorie:"};
        JTextField[] fields = new JTextField[labels.length - 3];
        villeComboBox = new JComboBox<>(); // Initialisation ici pour la portée de la classe
        categorieComboBox = new JComboBox<>(); // Initialisation ici
        nouvelleVilleTextField = new JTextField(); // Initialisation ici pour la portée de la classe
        nouvelleVilleTextField.setVisible(false); // Initialement caché
        JComboBox<String> sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        loadDataIntoComboBox(villeComboBox, "Ville", "NomVille", "NumVille", villesMap, true); // Charger les villes depuis la base de données avec l'option "Ajouter..."
        loadDataIntoComboBox(categorieComboBox, "Categorie", "NomCategorie", "NumCat", categoriesMap, false); // Charger les catégories depuis la base de données sans l'option "Ajouter..."

        JPanel villePanelContainer = new JPanel(new GridBagLayout()); // Panneau pour contenir la ComboBox et le TextField de la ville
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

        // Sexe
        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel(labels[6]), gbc);
        gbc.gridx = 1;
        mainPanel.add(sexeComboBox, gbc);

        // Ville (utilisation du panneau conteneur)
        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel(labels[7]), gbc);
        gbc.gridx = 1;
        mainPanel.add(villePanelContainer, gbc);

        // Catégorie
        gbc.gridx = 0;
        gbc.gridy = row++;
        mainPanel.add(new JLabel(labels[8]), gbc);
        gbc.gridx = 1;
        mainPanel.add(categorieComboBox, gbc);

        // ActionListener pour afficher/cacher le champ de texte de la nouvelle ville
        villeComboBox.addActionListener(e -> {
            boolean isAddingNewCity = "Ajouter une nouvelle ville...".equals(villeComboBox.getSelectedItem());
            nouvelleVilleTextField.setVisible(isAddingNewCity);
            // Si "Ajouter une nouvelle ville..." est sélectionné, effacer le champ de texte
            if (isAddingNewCity) {
                nouvelleVilleTextField.setText("");
                SwingUtilities.invokeLater(AjouterContact.this::pack); // Réajuster la taille après que le champ soit visible
            } else {
                SwingUtilities.invokeLater(AjouterContact.this::pack); // Réajuster la taille quand le champ est caché aussi pour un affichage propre
            }
        });

        // DocumentListener pour le champ de texte de la nouvelle ville (pas de mise à jour de la ComboBox ici)
        nouvelleVilleTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Rien à faire ici, le champ de texte est pour la saisie
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Rien à faire ici
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Rien à faire ici pour un champ de texte simple
            }
        });

        // Boutons Enregistrer et Annuler
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

        // ActionListener pour le bouton Enregistrer (avec validations)
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = fields[0].getText().trim();
                String prenom = fields[1].getText().trim();
                String telPerso = fields[3].getText().trim();
                String telPro = fields[4].getText().trim();
                String email = fields[5].getText().trim();

                // Validation du nom (accepte les lettres, espaces, tirets et underscores)
                if (!nom.matches("[a-zA-Z\\s\\-_]+")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le nom ne doit contenir que des lettres, des espaces, des tirets (-) ou des underscores (_).", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validation du prénom (accepte les lettres, espaces, tirets et underscores)
                if (!prenom.matches("[a-zA-Z\\s\\-_]+")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le prénom ne doit contenir que des lettres, des espaces, des tirets (-) ou des underscores (_).", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validation du téléphone personnel (10 chiffres commençant par 0)
                if (!telPerso.matches("0\\d{9}")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le téléphone personnel doit commencer par 0 et contenir 10 chiffres.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validation du téléphone professionnel (si non vide, doit avoir 10 chiffres commençant par 0)
                if (!telPro.isEmpty() && !telPro.matches("0\\d{9}")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "Le téléphone professionnel doit commencer par 0 et contenir 10 chiffres.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validation de l'email (format basique d'adresse email)
                if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(AjouterContact.this, "L'adresse email n'est pas au format valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String libelle = fields[2].getText().trim();
                String sexe = sexeComboBox.getSelectedItem().toString();
                String selectedVilleNom = (String) villeComboBox.getSelectedItem();
                String nouvelleVilleSaisie = nouvelleVilleTextField.getText().trim();
                String selectedCategorieNom = (String) categorieComboBox.getSelectedItem();
                int villeId;
                int categorieId;

                // Gestion de la ville
                if ("Ajouter une nouvelle ville...".equals(selectedVilleNom)) {
                    if (nouvelleVilleSaisie.isEmpty()) {
                        JOptionPane.showMessageDialog(AjouterContact.this, "Veuillez saisir le nom de la nouvelle ville.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    villeId = ajouterNouvelleVille(nouvelleVilleSaisie);
                    if (villeId == -1) {
                        return;
                    }
                } else {
                    villeId = villesMap.get(selectedVilleNom);
                }

                // Récupérer l'ID de la catégorie sélectionnée
                categorieId = categoriesMap.get(selectedCategorieNom);

                String query = "INSERT INTO contact (nom, prenom, libelle, telPerso, telPro, email, sexe, NumVille, NumCat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = AjouterContact.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, nom);
                    stmt.setString(2, prenom);
                    stmt.setString(3, libelle);
                    stmt.setString(4, telPerso);
                    stmt.setString(5, telPro);
                    stmt.setString(6, email);
                    stmt.setString(7, sexe);
                    stmt.setInt(8, villeId);
                    stmt.setInt(9, categorieId); // Utilisation de l'ID de la catégorie

                    stmt.executeUpdate();

                    Object[] newRow = new Object[labels.length + 1];
                    int rowCount = 0;
                    String countQuery = "SELECT COUNT(*) FROM contact";
                    try (Connection con = getConnection();
                         Statement stmts = con.createStatement();
                         ResultSet rs = stmts.executeQuery(countQuery)) {
                        if (rs.next()) {
                            rowCount = rs.getInt(1);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    newRow[0] = rowCount;
                    newRow[1] = nom;
                    newRow[2] = prenom;
                    newRow[3] = libelle;
                    newRow[4] = telPerso;
                    newRow[5] = telPro;
                    newRow[6] = email;
                    newRow[7] = sexe;
                    newRow[8] = villesInverseMap.get(villeId);
                    newRow[9] = selectedCategorieNom;

                    model.addRow(newRow);
                    dispose();
                    JOptionPane.showMessageDialog(null, "Contact Ajouté");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du contact:\n" + ex.getMessage());
                }
            }
        });

        // ActionListener pour le bouton Annuler
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    /**
     * Méthode pour ajouter une nouvelle ville à la base de données.
     * @param nomVille Le nom de la nouvelle ville à ajouter.
     * @return L'ID de la nouvelle ville ajoutée, ou -1 en cas d'erreur.
     */
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