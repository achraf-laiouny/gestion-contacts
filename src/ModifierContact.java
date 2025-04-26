import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

public class ModifierContact extends JFrame {
    private JTextField nomField, prenomField, libelleField, telPersoField, telProField, emailField;
    private JComboBox<String> sexeComboBox, villeComboBox, categorieComboBox;
    private DefaultTableModel model;
    private int selectedRow, contactId;
    private HashMap<String, Integer> villesMap = new HashMap<>(), categoriesMap = new HashMap<>();

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC", "root", "");
    }

    private void loadVilles() {
        villeComboBox.removeAllItems();
        villesMap.clear();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT NumVille, NomVille FROM Ville")) {
            while (rs.next()) {
                String nomVille = rs.getString("NomVille");
                villeComboBox.addItem(nomVille);
                villesMap.put(nomVille, rs.getInt("NumVille"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des villes.");
        }
    }

    private void loadCategories() {
        categorieComboBox.removeAllItems();
        categoriesMap.clear();
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT NumCat, NomCategorie FROM Categorie")) {
            while (rs.next()) {
                String nomCategorie = rs.getString("NomCategorie");
                categorieComboBox.addItem(nomCategorie);
                categoriesMap.put(nomCategorie, rs.getInt("NumCat"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des catégories.");
        }
    }

    public ModifierContact(DefaultTableModel model, int selectedRow, int contactId) {
        this.model = model;
        this.selectedRow = selectedRow;
        this.contactId = contactId;

        setTitle("Modifier le Contact");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        nomField = createLabeledTextField(formPanel, "Nom:", (String) model.getValueAt(selectedRow, 1));
        prenomField = createLabeledTextField(formPanel, "Prénom:", (String) model.getValueAt(selectedRow, 2));
        libelleField = createLabeledTextField(formPanel, "Libellé:", (String) model.getValueAt(selectedRow, 3));
        telPersoField = createLabeledTextField(formPanel, "Téléphone Perso:", (String) model.getValueAt(selectedRow, 4));
        telProField = createLabeledTextField(formPanel, "Téléphone Pro:", (String) model.getValueAt(selectedRow, 5));
        emailField = createLabeledTextField(formPanel, "Email:", (String) model.getValueAt(selectedRow, 6));

        sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        sexeComboBox.setSelectedItem(model.getValueAt(selectedRow, 7));
        addLabeledComponent(formPanel, "Sexe:", sexeComboBox);

        villeComboBox = new JComboBox<>();
        loadVilles();
        villeComboBox.setSelectedItem(model.getValueAt(selectedRow, 8));
        addLabeledComponent(formPanel, "Ville:", villeComboBox);

        categorieComboBox = new JComboBox<>();
        loadCategories();
        categorieComboBox.setSelectedItem(model.getValueAt(selectedRow, 9));
        addLabeledComponent(formPanel, "Catégorie:", categorieComboBox);

        contentPane.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.addActionListener(e -> modifierContactDansBaseDeDonnees());
        buttonPanel.add(enregistrerButton);

        JButton annulerButton = new JButton("Annuler");
        annulerButton.addActionListener(e -> dispose());
        buttonPanel.add(annulerButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JTextField createLabeledTextField(JPanel panel, String label, String initialValue) {
        JLabel jLabel = new JLabel(label);
        JTextField field = new JTextField(initialValue);
        panel.add(jLabel);
        panel.add(field);
        return field;
    }

    private void addLabeledComponent(JPanel panel, String label, JComponent component) {
        panel.add(new JLabel(label));
        panel.add(component);
    }

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

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
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

            if (pstmt.executeUpdate() > 0) {
                model.setValueAt(nom, selectedRow, 1);
                model.setValueAt(prenom, selectedRow, 2);
                model.setValueAt(libelle, selectedRow, 3);
                model.setValueAt(telPerso, selectedRow, 4);
                model.setValueAt(telPro, selectedRow, 5);
                model.setValueAt(email, selectedRow, 6);
                model.setValueAt(sexe, selectedRow, 7);
                model.setValueAt(villeNom, selectedRow, 8);
                model.setValueAt(categorieNom, selectedRow, 9);
                JOptionPane.showMessageDialog(this, "Contact modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du contact.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de base de données lors de la modification du contact.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
