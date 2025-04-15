import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.sql.Statement;
import java.sql.ResultSet;

public class AjouterContact extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private HashMap<String, Integer> villesMap = new HashMap<>();
    private HashMap<String, Integer> categoriesMap = new HashMap<>();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    private void loadDataIntoComboBox(JComboBox<String> comboBox, String tableName, String nameColumn, String idColumn, HashMap<String, Integer> map) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT " + idColumn + ", " + nameColumn + " FROM " + tableName)) {

            while (rs.next()) {
                int id = rs.getInt(idColumn);
                String name = rs.getString(nameColumn);
                comboBox.addItem(name);
                map.put(name, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données depuis " + tableName);
        }
    }


    public AjouterContact(DefaultTableModel model){
        setTitle("Ajouter un Contact");
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(10, 2,5,5));
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));


        String[] labels = {"nom", "prenom", "libelle", "telPerso", "telPro", "email", "sexe","Ville","Categorie"};
        JTextField[] fields = new JTextField[labels.length];

        JComboBox<String> VilleCombobox = new JComboBox<>();
        JComboBox<String> categorieComboBox = new JComboBox<>();

        JComboBox<String> sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        loadDataIntoComboBox(VilleCombobox,"Ville", "NomVille", "NumVille",villesMap);
        loadDataIntoComboBox(categorieComboBox,"Categorie", "NomCategorie", "NumCat",categoriesMap);

        for (int i = 0 ; i < labels.length -3 ; i++) {
            mainPanel.add(new JLabel(labels[i]));
            fields[i] = new JTextField();
            mainPanel.add(fields[i]);
        }
        mainPanel.add(new JLabel(labels[6]));
        mainPanel.add(sexeComboBox);
        mainPanel.add(new JLabel(labels[7]));
        mainPanel.add(VilleCombobox);
        mainPanel.add(new JLabel(labels[8]));
        mainPanel.add(categorieComboBox);


        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);



        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String querry = "INSERT INTO contact (nom, prenom, libelle, telPerso, telPro, email, sexe, NumVille, NumCat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = AjouterContact.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(querry)){
                    for (int i = 0; i < fields.length - 3; i++) {
                            stmt.setString(i + 1, fields[i].getText());
                    }
                    stmt.setString(7, sexeComboBox.getSelectedItem().toString());

                    String selectedVille = VilleCombobox.getSelectedItem().toString();
                    String selectedCategorie = categorieComboBox.getSelectedItem().toString();

                    int villeId = villesMap.get(selectedVille);
                    int categorieId = categoriesMap.get(selectedCategorie);

                    stmt.setInt(8, villeId);
                    stmt.setInt(9, categorieId);

                    stmt.executeUpdate();

                    Object[] newRow = new Object[labels.length+1];

                    int rowCount = 0;

                    String query = "SELECT COUNT(*) FROM contact";

                    try (Connection con = getConnection();
                         Statement stmts = con.createStatement();
                         ResultSet rs = stmts.executeQuery(query)) {

                        if (rs.next()) {
                            rowCount = rs.getInt(1); // getInt(1) gets the first column from the result
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    newRow[0] = rowCount;
                    newRow[1] = fields[0].getText(); // nom
                    newRow[2] = fields[1].getText(); // prenom
                    newRow[3] = fields[2].getText(); // libelle
                    newRow[4] = fields[3].getText(); // telPerso
                    newRow[5] = fields[4].getText(); // telPro
                    newRow[6] = fields[5].getText(); // email
                    newRow[7] = sexeComboBox.getSelectedItem().toString(); // sexe
                    newRow[8] = VilleCombobox.getSelectedItem().toString(); // ville name
                    newRow[9] = categorieComboBox.getSelectedItem().toString(); // categorie name


                    model.addRow(newRow);
                    dispose();
                    JOptionPane.showMessageDialog(null, "Contact Ajouté");
                     // close window
                }catch (SQLException ex){
                    ex.printStackTrace(); // 👈 shows full error in console
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du contact:\n" + ex.getMessage());
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}

