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
 * La classe AjouterContact permet d'ajouter un nouveau contact à la base de données
 * et de mettre à jour le tableau de contacts affiché dans l'application.
 * Elle fournit une interface utilisateur avec des champs pour saisir les informations
 * du contact et des validations pour assurer l'intégrité des données.
 * Elle hérite de JFrame, ce qui en fait une fenêtre graphique.
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
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.decode("#F8F9FA"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
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
        villePanelContainer.setOpaque(false);
        GridBagConstraints gbcVille = new GridBagConstraints();
        gbcVille.insets = new Insets(0, 0, 0, 0);
        gbcVille.anchor = GridBagConstraints.WEST;
        gbcVille.fill = GridBagConstraints.HORIZONTAL;
        gbcVille.weightx = 1.0;

        gbcVille.gridx = 0;
        gbcVille.gridy = 0;
        villePanelContainer.add(villeComboBox, gbcVille);

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
            if (isAddingNewCity) nouvelleVilleTextField.setText("");
            SwingUtilities.invokeLater(AjouterContact.this::pack);
        });

        nouvelleVilleTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {}
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        saveButton.setBackground(new Color(25, 135, 84));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        saveButton.addActionListener(e -> enregistrerContact(fields, sexeComboBox, villeComboBox, nouvelleVilleTextField, categorieComboBox));
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void enregistrerContact(JTextField[] fields, JComboBox<String> sexeComboBox, JComboBox<String> villeComboBox, JTextField nouvelleVilleTextField, JComboBox<String> categorieComboBox) {
        // Existing logic for saving contact (same as your current implementation)
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
