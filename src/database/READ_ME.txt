********** assuming you're working with Intellij IDEA community Edition ************
" also assuming you pulled the project using Git "

1) first you need to download and install XAMPP

2) secondly you need to add the MySQL Connector/J (JDBC driver) .jar

    Step 1: Download the JAR

        - Go to: https://dev.mysql.com/downloads/connector/j/
        - Download the platform-independent .zip
        - Extract it → You’ll find mysql-connector-j-8.x.x.jar inside the folder

    Step 2: Add JAR to IntelliJ Project

        - Go to File > Project Structure (or press Ctrl+Alt+Shift+S)
        - Select Modules > Dependencies
        - Click the + icon > JARs or directories
        - Select the downloaded .jar file
        - Click OK → Make sure it appears in the list with scope Compile

3) thirdly run xampp Apache & MySql

4) fourthly import database

    Step 1 : Go to localhost on browser

        - Go to: phpMyAdmin
        - Create database under the name "gestion_contacts"
        - Select it
        - Click on Import & select the .sql file in src/database/contact.sql
        - Execute

    Step 2  : Got to Contacts.java

        - Click RUN

*****************************************************************************************
# Classe RechercherContact

Cette classe implémente une fenêtre permettant aux utilisateurs de rechercher des contacts en spécifiant divers critères.

## Fonctionnalités

La fenêtre `RechercherContact` offre les fonctionnalités suivantes :

* **Recherche par Nom :** Permet de rechercher des contacts par leur nom.
* **Recherche par Prénom :** Permet de rechercher des contacts par leur prénom.
* **Recherche par Libellé :** Permet de rechercher des contacts par un libellé spécifique.
* **Recherche par Ville :** Permet de rechercher des contacts par leur ville.
* **Recherche par Sexe :** Permet de filtrer les contacts par sexe (Male ou Female).
* **Recherche par Catégorie :** Permet de filtrer les contacts par catégorie (Ami, Famille, Travail).
* **Combinaison de Critères :** L'utilisateur peut sélectionner et remplir plusieurs critères simultanément pour affiner la recherche.
* **Interaction avec la Fenêtre Principale :** Une fois la recherche lancée, les résultats sont affichés dans la fenêtre principale de gestion des contacts (`Contacts`).
* **Annulation de la Recherche :** Un bouton "Annuler" permet de fermer la fenêtre de recherche sans effectuer de recherche.

## Structure de la Classe
### Variables d'Instance
* `contentPane`: Panneau de contenu principal de la fenêtre.
* `rechercherButton`: Bouton pour déclencher la recherche.
* `annulerButton`: Bouton pour fermer la fenêtre de recherche.
* `nomCheckBox`, `nomTextField`: Case à cocher et champ de texte pour le critère de nom.
* `prenomCheckBox`, `prenomTextField`: Case à cocher et champ de texte pour le critère de prénom.
* `libelleCheckBox`, `libelleTextField`: Case à cocher et champ de texte pour le critère de libellé.
* `villeCheckBox`, `villeTextField`: Case à cocher et champ de texte pour le critère de ville.
* `sexeCheckBox`, `sexeComboBox`: Case à cocher et liste déroulante pour le critère de sexe.
* `categorieCheckBox`, `categorieComboBox`: Case à cocher et liste déroulante pour le critère de catégorie.
* `fenetreContacts`: Référence à l'instance de la fenêtre `Contacts`.

### Constructeur

* `RechercherContact(JFrame parent, Contacts fenetreContacts)`: Initialise la fenêtre de recherche, configure son apparence et stocke une référence à la fenêtre `Contacts` parente.

### Méthode `effectuerRecherche()`

* Collecte les critères de recherche sélectionnés par l'utilisateur.
* Crée une `HashMap` contenant les critères actifs (case cochée et champ/liste non vide).
* Appelle la méthode `rechercherContacts()` de l'instance `fenetreContacts` pour effectuer la recherche dans les données.
* Affiche un message si aucun critère de recherche n'est sélectionné.
* Ferme la fenêtre de recherche après la tentative de recherche.


## Dépendances

* `javax.swing.*` (pour l'interface graphique)
* `javax.swing.border.EmptyBorder` (pour la bordure du panneau)
* `java.awt.*` (pour la gestion de la mise en page et les événements)
* `java.awt.event.*` (pour la gestion des actions des boutons)
* `java.util.HashMap` et `java.util.Map` (pour stocker les critères de recherche)
* `Contacts` (la classe de la fenêtre principale des contacts)
*************************************************************************************************************
# Classe SupprimerContact

## Description

La classe `SupprimerContact` est une fenêtre (JFrame) qui permet à l'utilisateur de supprimer un contact sélectionné dans un tableau de contacts. Elle affiche une boîte de dialogue de confirmation avant de procéder à la suppression du contact de la base de données.

## Structure de la Classe

### Héritage

* `javax.swing.JFrame`

### Variables d'Instance

* `contactIdToDelete`: `int` - L'ID du contact à supprimer, récupéré de la ligne sélectionnée du tableau. Initialisé à -1.
* `mainFrame`: `Contacts` - Une référence à l'instance de la fenêtre principale `Contacts`, utilisée pour recharger la liste des contacts après la suppression.
* `contactTable`: `javax.swing.JTable` - Une référence au tableau de contacts affiché dans la fenêtre principale, nécessaire pour déterminer la ligne sélectionnée.

### Constructeur

* `public SupprimerContact(JFrame parent, Contacts mainFrame, JTable contactTable)`
    * Initialise la fenêtre de suppression avec un titre et configure l'opération de fermeture par défaut.
    * Reçoit une référence à la fenêtre parente (`Contacts`) et au tableau de contacts.
    * Crée un `JPanel` avec un `FlowLayout` pour organiser les composants.
    * Ajoute un `JLabel` demandant la confirmation de la suppression.
    * Crée deux `JButton`: "Confirmer" et "Annuler".
    * Ajoute des `ActionListener` aux boutons pour gérer les événements de clic.
        * Le bouton "Confirmer" appelle la méthode `supprimerContact()`.
        * Le bouton "Annuler" ferme la fenêtre de suppression.
    * Ajoute les composants au panneau de contenu, ajuste la taille de la fenêtre (`pack()`), la centre par rapport à la fenêtre parente (`setLocationRelativeTo(parent)`) et la rend visible (`setVisible(true)`).

### Méthode `supprimerContact()`

1.  **Récupère la ligne sélectionnée :**
    * `int selectedRow = contactTable.getSelectedRow();` Obtient l'index de la ligne sélectionnée dans le tableau de contacts.

2.  **Vérifie si une ligne est sélectionnée :**
    * Si `selectedRow` est -1, affiche un message d'erreur demandant à l'utilisateur de sélectionner un contact.

3.  **Récupère l'ID du contact à supprimer :**
    * Tente de récupérer la valeur de la première colonne (index 0) de la ligne sélectionnée, qui est supposée contenir l'ID du contact.
    * Convertit cette valeur en un entier (`int`).
    * Gère les exceptions `NumberFormatException` ou `ArrayIndexOutOfBoundsException` si la valeur n'est pas un nombre valide ou si l'index est hors limites, affichant un message d'erreur en cas de problème.

4.  **Établit une connexion à la base de données :**
    * Définit l'URL de la base de données MySQL, le nom d'utilisateur et le mot de passe.
    * Définit la requête SQL `DELETE` pour supprimer un contact en fonction de son ID (`WHERE id = ?`).

5.  **Exécute la requête de suppression :**
    * Utilise un bloc `try-with-resources` pour assurer la fermeture automatique de la connexion et du `PreparedStatement`.
    * Prépare la requête SQL avec l'ID du contact à supprimer.
    * Exécute la requête `executeUpdate()` et récupère le nombre de lignes affectées.

6.  **Gère le résultat de la suppression :**
    * Si `affectedRows` est supérieur à 0, affiche un message de succès et appelle la méthode `loadContactsFromDatabase()` de l'instance `mainFrame` pour recharger la liste des contacts dans la fenêtre principale (ce qui mettra à jour l'affichage après la suppression).
    * Si `affectedRows` n'est pas supérieur à 0, affiche un message d'erreur indiquant que la suppression a échoué.

7.  **Gère les erreurs de base de données :**
    * Un bloc `catch (SQLException e)` capture toute exception SQL qui pourrait survenir pendant la connexion ou l'exécution de la requête, affiche la trace de la pile et un message d'erreur à l'utilisateur.

8.  **Ferme la fenêtre de suppression :**
    * Le bloc `finally` assure que la fenêtre de suppression est fermée (`dispose()`) après l'opération de suppression (réussie ou échouée).

