import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjouterContact extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
//    public static void addContact(String nom,String prenom, String email, String tel,String ville,String cat) throws SQLException {
//        String querry = "INSERT INTO CONTACT (nom,prenom,email,telephone,ville,categorie) VALUES (?,?,?,?,?,?)";
//
//        try (Connection conn = AjouterContact.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(querry)){
//            stmt.setString(1, nom);
//            stmt.setString(2, prenom);
//            stmt.setString(3, email);
//            stmt.setString(4, tel);
//            stmt.setString(5, ville);
//            stmt.setString(6, cat);
//            stmt.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Contact Ajouté");
//        }catch (SQLException e){
//            JOptionPane.showMessageDialog(null,"problem" );
//
//        }
//    }
    public AjouterContact(DefaultTableModel model){
        setTitle("Ajouter un Contact");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(7, 2,5,5));
        mainPanel.setBorder(new EmptyBorder(10,10,10,10));


        String[] labels = {"Nom", "Prénom", "Email", "Téléphone", "Ville", "Catégorie"};
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length;i++) {
            mainPanel.add(new JLabel(labels[i]));
            fields[i] = new JTextField();
            mainPanel.add(fields[i]);
        }

        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);



        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String querry = "INSERT INTO CONTACT (nom,prenom,email,telephone,ville,categorie) VALUES (?,?,?,?,?,?)";

                try (Connection conn = AjouterContact.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(querry)){
                    Object[] rowData = new Object[labels.length];
                    for (int i = 0; i < fields.length; i++) {
                        stmt.setString(i + 1, fields[i].getText());
                        rowData[i] = fields[i].getText();
                    }
                    model.addRow(rowData);
                    dispose(); // close window
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Contact Ajouté");
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

