import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifierContact extends JFrame {
    public ModifierContact(DefaultTableModel model, int Row){
        setTitle("Modifier un Contact");
        setSize(400,300);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel mainPanel = new JPanel(new GridLayout(7,2,5,5));
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));

        String[] labels = {"Nom", "Prénom", "Email", "Téléphone", "Ville", "Catégorie"};
        JTextField[] fields = new JTextField[labels.length];

        for (int j = 0; j<labels.length;j++){
            mainPanel.add(new JLabel(labels[j]));
            fields[j] = new JTextField(model.getValueAt(Row,j).toString());
            mainPanel.add(fields[j]);
        }

        JButton saveButton = new JButton("Enregistrer");
        JButton cancelButton = new JButton("Annuler");
        mainPanel.add(saveButton);
        mainPanel.add(cancelButton);

        add(mainPanel);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < fields.length; i++) {
                    model.setValueAt(fields[i].getText(), Row, i);
                }
                dispose();

            }
        });
        cancelButton.addActionListener(e -> dispose());

    }
}
