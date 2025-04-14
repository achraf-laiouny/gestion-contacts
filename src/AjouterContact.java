import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class AjouterContact extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
//    public static List<String> getCategories() {
//        List<String> categories = new ArrayList<>();
//        String query = "SELECT NomCategorie FROM Categorie";
//
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                categories.add(rs.getString("NomCategorie"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return categories;
//    }
    public AjouterContact(DefaultTableModel model){
        setTitle("Ajouter un Contact");
        setSize(400, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(10, 2,5,5));
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));


        String[] labels = {"nom", "prenom", "libelle", "telPerso", "telPro", "email", "sexe","Ville","Categorie"};
        JTextField[] fields = new JTextField[labels.length];

//        JComboBox<String> categorieComboBox = new JComboBox<>(new String[]{qCat});
        JComboBox<String> sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});

        for (int i = 0; i < labels.length -2;i++) {
            mainPanel.add(new JLabel(labels[i]));
            fields[i] = new JTextField();
            mainPanel.add(fields[i]);
        }
        mainPanel.add(new JLabel(labels[5]));
        mainPanel.add(categorieComboBox);
        mainPanel.add(new JLabel(labels[6]));
        mainPanel.add(sexeComboBox);

        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);



        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String querry = "INSERT INTO CONTACT (nom,prenom,email,telephone,ville,categorie,sexe) VALUES (?,?,?,?,?,?,?)";

                try (Connection conn = AjouterContact.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(querry)){
                    for (int i = 0; i < fields.length - 2; i++) {
                            stmt.setString(i + 1, fields[i].getText());
                    }
                    stmt.setString(6,(categorieComboBox.getSelectedItem()).toString());
                    stmt.setString(7,(sexeComboBox.getSelectedItem()).toString());

                    stmt.executeUpdate();

                    Object[] newRow = new Object[labels.length];
                    for (int i = 0; i < fields.length -2 ; i++) {
                        newRow[i] = fields[i].getText();
                    }
                    newRow[5] = categorieComboBox.getSelectedItem().toString();
                    newRow[6] = sexeComboBox.getSelectedItem().toString();
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

