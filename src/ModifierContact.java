import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

/**
 * La classe `ModifierContact` permet de modifier les informations d'un contact existant.
 * Elle affiche un formulaire pré-rempli avec les données du contact sélectionné
 * et interagit avec la base de données pour enregistrer les modifications.
 */
public class ModifierContact extends JFrame {

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField libelleField;
    private JTextField telPersoField;
    private JTextField telProField;
    private JTextField emailField;
    private JComboBox<String> sexeComboBox;
    private JComboBox<String> villeComboBox;
    private JComboBox<String> categorieComboBox;
    private DefaultTableModel model;
    private int selectedRow;
    private int contactId;
    private HashMap<String, Integer> villesMap = new HashMap<>();
    private HashMap<String, Integer> categoriesMap = new HashMap<>();

    /**
     * Méthode pour établir une connexion à la base de données.
     * @return Une instance de `Connection`.
     * @throws SQLException en cas d'erreur de connexion.
     */
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Méthode pour charger les villes depuis la base de données dans la JComboBox.
     */
    private void loadVilles() {
        villeComboBox.removeAllItems();
        villesMap.clear();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT NumVille, NomVille FROM Ville")) {
            while (rs.next()) {
                int id = rs.getInt("NumVille");
                String nomVille = rs.getString("NomVille");
                villeComboBox.addItem(nomVille);
                villesMap.put(nomVille, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des villes.");
        }
    }

    /**
     * Méthode pour charger les catégories depuis la base de données dans la JComboBox.
     */
    private void loadCategories() {
        categorieComboBox.removeAllItems();
        categoriesMap.clear();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT NumCat, NomCategorie FROM Categorie")) {
            while (rs.next()) {
                int id = rs.getInt("NumCat");
                String nomCategorie = rs.getString("NomCategorie");
                categorieComboBox.addItem(nomCategorie);
                categoriesMap.put(nomCategorie, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des catégories.");
        }
    }

    /**
     * Constructeur de la classe `ModifierContact`.
     * @param model Le modèle de table contenant les contacts.
     * @param selectedRow L'index de la ligne sélectionnée dans le tableau.
     * @param contactId L'ID du contact à modifier.
     */
    public ModifierContact(DefaultTableModel model, int selectedRow, int contactId) {
        this.model = model;
        this.selectedRow = selectedRow;
        this.contactId = contactId;

        setTitle("Modifier le Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 350); // Augmentation de la hauteur pour les ComboBox
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 2, 5, 5));

        // Récupération des données du contact à modifier à partir du modèle de table.
        String nom = (String) model.getValueAt(selectedRow, 1);
        String prenom = (String) model.getValueAt(selectedRow, 2);
        String libelle = (String) model.getValueAt(selectedRow, 3);
        String telPerso = (String) model.getValueAt(selectedRow, 4);
        String telPro = (String) model.getValueAt(selectedRow, 5);
        String email = (String) model.getValueAt(selectedRow, 6);
        String sexe = (String) model.getValueAt(selectedRow, 7);
        String ville = (String) model.getValueAt(selectedRow, 8);
        String categorie = (String) model.getValueAt(selectedRow, 9);

        // Création et ajout des labels et des champs de saisie pour chaque champ du contact.
        JLabel nomLabel = new JLabel("Nom:");
        contentPane.add(nomLabel);
        nomField = new JTextField(nom);
        contentPane.add(nomField);
        nomField.setColumns(10);

        JLabel prenomLabel = new JLabel("Prénom:");
        contentPane.add(prenomLabel);
        prenomField = new JTextField(prenom);
        contentPane.add(prenomField);
        prenomField.setColumns(10);

        JLabel libelleLabel = new JLabel("Libellé:");
        contentPane.add(libelleLabel);
        libelleField = new JTextField(libelle);
        contentPane.add(libelleField);
        libelleField.setColumns(10);

        JLabel telPersoLabel = new JLabel("Téléphone Perso:");
        contentPane.add(telPersoLabel);
        telPersoField = new JTextField(telPerso);
        contentPane.add(telPersoField);
        telPersoField.setColumns(10);

        JLabel telProLabel = new JLabel("Téléphone Pro:");
        contentPane.add(telProLabel);
        telProField = new JTextField(telPro);
        contentPane.add(telProField);
        telProField.setColumns(10);

        JLabel emailLabel = new JLabel("Email:");
        contentPane.add(emailLabel);
        emailField = new JTextField(email);
        contentPane.add(emailField);
        emailField.setColumns(10);

        JLabel sexeLabel = new JLabel("Sexe:");
        contentPane.add(sexeLabel);
        sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        sexeComboBox.setSelectedItem(sexe);
        contentPane.add(sexeComboBox);

        JLabel villeLabel = new JLabel("Ville:");
        contentPane.add(villeLabel);
        villeComboBox = new JComboBox<>();
        loadVilles();
        villeComboBox.setSelectedItem(ville);
        contentPane.add(villeComboBox);

        JLabel categorieLabel = new JLabel("Catégorie:");
        contentPane.add(categorieLabel);
        categorieComboBox = new JComboBox<>();
        loadCategories();
        categorieComboBox.setSelectedItem(categorie);
        contentPane.add(categorieComboBox);

        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierContactDansBaseDeDonnees();
            }
        });
        contentPane.add(enregistrerButton);

        JButton annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre de modification.
            }
        });
        contentPane.add(annulerButton);

        pack(); // Ajuste la taille de la fenêtre à ses composants.
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran.
        setVisible(true); // Rend la fenêtre visible.
    }

    /**
     * Méthode pour modifier le contact dans la base de données.
     */
    private void modifierContactDansBaseDeDonnees() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String libelle = libelleField.getText();
        String telPerso = telPersoField.getText();
        String telPro = telProField.getText();
        String email = emailField.getText();
        String sexe = (String) sexeComboBox.getSelectedItem();
        String villeNom = (String) villeComboBox.getSelectedItem();
        String categorieNom = (String) categorieComboBox.getSelectedItem();

        if (villeNom == null || categorieNom == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ville et une catégorie.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer villeId = villesMap.get(villeNom);
        Integer categorieId = categoriesMap.get(categorieNom);

        if (villeId == null || categorieId == null) {
            JOptionPane.showMessageDialog(this, "Erreur: Ville ou catégorie non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updateQuery = "UPDATE contact SET nom = ?, prenom = ?, libelle = ?, telPerso = ?, telPro = ?, email = ?, sexe = ?, NumVille = ?, NumCat = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, libelle);
            pstmt.setString(4, telPerso);
            pstmt.setString(5, telPro);
            pstmt.setString(6, email);
            pstmt.setString(7, sexe);
            pstmt.setInt(8, villeId);
            pstmt.setInt(9, categorieId);
            pstmt.setInt(10, contactId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Contact modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                // Mise à jour de la ligne dans le modèle de table
                model.setValueAt(nom, selectedRow, 1);
                model.setValueAt(prenom, selectedRow, 2);
                model.setValueAt(libelle, selectedRow, 3);
                model.setValueAt(telPerso, selectedRow, 4);
                model.setValueAt(telPro, selectedRow, 5);
                model.setValueAt(email, selectedRow, 6);
                model.setValueAt(sexe, selectedRow, 7);
                model.setValueAt(villeNom, selectedRow, 8);
                model.setValueAt(categorieNom, selectedRow, 9);
                dispose(); // Ferme la fenêtre après la modification réussie.
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du contact.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de base de données lors de la modification du contact.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}