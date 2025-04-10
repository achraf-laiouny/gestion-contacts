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

public class ModifierContact extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public ModifierContact(DefaultTableModel model, int Row, String tel){
        setTitle("Modifier un Contact");
        setSize(400,300);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel mainPanel = new JPanel(new GridLayout(8,2,5,5));
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));

        String[] labels = {"Nom", "Prénom", "Email", "Téléphone", "Ville", "Catégorie","sexe"};
        JTextField[] fields = new JTextField[labels.length];
        if (Row != -1){
            for (int j = 0; j<labels.length;j++){
                mainPanel.add(new JLabel(labels[j]));
                String value = model.getValueAt(Row, j).toString();
                if (value == null) {
                    value = "";
                }
                fields[j] = new JTextField(value);
                mainPanel.add(fields[j]);
            }
        }else {
            JOptionPane.showMessageDialog(this, "Aucune ligne sélectionnée.");
            dispose();
        }


        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = "UPDATE contact SET nom = ?, prenom = ? ,email = ?, telephone = ?, ville = ?, categorie = ?," +
                        " sexe = ? WHERE telephone = ?";
                try (Connection conn = AjouterContact.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)){
                    Object[] rowData = new Object[labels.length];
                    for (int i = 0; i < fields.length; i++) {
                        stmt.setString(i + 1, fields[i].getText());
                    }
                    stmt.setString(8,tel);
                    stmt.executeUpdate();
                    for (int i = 0; i < fields.length; i++) {
                        model.setValueAt(fields[i].getText(), Row, i);  // Update the model with the new values
                    }
                    dispose(); // close window

                    JOptionPane.showMessageDialog(null, "Contact Modifié");
                }catch (SQLException ex){
                    ex.printStackTrace(); // 👈 shows full error in console
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du contact:\n" + ex.getMessage());
                }

            }
        });
        cancelButton.addActionListener(e -> dispose());

    }
}
