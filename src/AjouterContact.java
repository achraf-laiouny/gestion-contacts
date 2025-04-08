import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AjouterContact extends JFrame {
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
                Object[] rowData = new Object[labels.length];
                for (int i = 0; i < fields.length; i++) {
                    rowData[i] = fields[i].getText();
                }
                model.addRow(rowData);
                dispose(); // close window
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }
}

