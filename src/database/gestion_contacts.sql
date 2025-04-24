-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 24 avr. 2025 à 22:44
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_contacts`
--

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `NumCat` int(11) NOT NULL,
  `NomCategorie` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `categorie`
--

INSERT INTO `categorie` (`NumCat`, `NomCategorie`) VALUES
(1, 'Famille'),
(2, 'Amis'),
(3, 'Travail');

-- --------------------------------------------------------

--
-- Structure de la table `contact`
--

CREATE TABLE `contact` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `libelle` varchar(100) DEFAULT NULL,
  `telPerso` varchar(20) DEFAULT NULL,
  `telPro` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `sexe` varchar(10) DEFAULT NULL,
  `NumVille` int(11) DEFAULT NULL,
  `NumCat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `contact`
--

INSERT INTO `contact` (`id`, `nom`, `prenom`, `libelle`, `telPerso`, `telPro`, `email`, `sexe`, `NumVille`, `NumCat`) VALUES
(1, 'Martin', 'Claire', 'Amie de lycée', '0601020304', '0145896325', 'claire.martin@example.com', 'Femme', 1, 2),
(2, 'Dupont', 'Jean', 'Collègue de travail', '0612345678', '0156478390', 'jean.dupont@example.com', 'Homme', 2, 3),
(3, 'Bernard', 'Sophie', 'Cousine', '0623456789', '0156789345', 'sophie.bernard@example.com', 'Female', 1, 2),
(23, 'laiouny', 'achraf', 'fonctionnaire', '0653563815', '0666201229', 'achraf.laiouny1999@gmail.com', 'Male', 4, 1),
(24, 'Dupont', 'Alice', 'Amie de lycée', '0601010101', '0102030405', 'alice.dupont@example.com', 'Female', 1, 1),
(25, 'Durand', 'Marc', 'Collègue', '0602020202', '0102030406', 'marc.durand@example.com', 'Male', 2, 2),
(26, 'Martin', 'Julie', 'Voisine', '0603030303', '0102030407', 'julie.martin@example.com', 'Female', 3, 1),
(27, 'Leroy', 'Nicolas', 'Ami d’enfance', '0604040404', '0102030408', 'nicolas.leroy@example.com', 'Male', 1, 3),
(28, 'Moreau', 'Sophie', 'Camarade de promo', '0605050505', '0102030409', 'sophie.moreau@example.com', 'Female', 2, 2),
(29, 'Robert', 'Paul', 'Collègue', '0606060606', '0102030410', 'paul.robert@example.com', 'Male', 3, 1),
(30, 'Petit', 'Emma', 'Amie de fac', '0607070707', '0102030411', 'emma.petit@example.com', 'Female', 1, 2),
(31, 'Garcia', 'Lucas', 'Contact pro', '0608080808', '0102030412', 'lucas.garcia@example.com', 'Male', 2, 3),
(32, 'Roux', 'Chloe', 'Voisine', '0609090909', '0102030413', 'chloe.roux@example.com', 'Female', 3, 1),
(33, 'Fournier', 'Thomas', 'Camarade de classe', '0610101010', '0102030414', 'thomas.fournier@example.com', 'Male', 1, 1),
(34, 'Andre', 'Laura', 'Amie de lycée', '0611111111', '0102030415', 'laura.andre@example.com', 'Female', 2, 2),
(35, 'Mercier', 'Bastien', 'Connaissance', '0612121212', '0102030416', 'bastien.mercier@example.com', 'Male', 3, 3),
(36, 'Blanc', 'Léa', 'Voisine', '0613131313', '0102030417', 'lea.blanc@example.com', 'Female', 1, 1),
(37, 'Guerin', 'Maxime', 'Ancien collègue', '0614141414', '0102030418', 'maxime.guerin@example.com', 'Male', 2, 2),
(38, 'Faure', 'Camille', 'Ami de fac', '0615151515', '0102030419', 'camille.faure@example.com', 'Female', 3, 1),
(39, 'Chevalier', 'Julien', 'Connaissance pro', '0616161616', '0102030420', 'julien.chevalier@example.com', 'Male', 1, 3),
(40, 'Barbier', 'Manon', 'Camarade de promo', '0617171717', '0102030421', 'manon.barbier@example.com', 'Female', 2, 2),
(41, 'Renaud', 'Florian', 'Ami du sport', '0618181818', '0102030422', 'florian.renaud@example.com', 'Male', 3, 1),
(42, 'Perrin', 'Lucie', 'Amie de lycée', '0619191919', '0102030423', 'lucie.perrin@example.com', 'Female', 1, 2),
(43, 'Gomez', 'Hugo', 'Collègue', '0620202020', '0102030424', 'hugo.gomez@example.com', 'Male', 2, 1),
(44, 'Colin', 'Anna', 'Amie d’enfance', '0621212121', '0102030425', 'anna.colin@example.com', 'Female', 3, 3),
(45, 'Benoit', 'Antoine', 'Voisin', '0622222222', '0102030426', 'antoine.benoit@example.com', 'Male', 1, 1),
(46, 'Charles', 'Elise', 'Camarade de classe', '0623232323', '0102030427', 'elise.charles@example.com', 'Female', 2, 2),
(47, 'Meyer', 'Alexandre', 'Contact pro', '0624242424', '0102030428', 'alex.meyer@example.com', 'Male', 3, 2),
(48, 'Robin', 'Sarah', 'Amie de fac', '0625252525', '0102030429', 'sarah.robin@example.com', 'Female', 1, 3),
(49, 'Henry', 'Benoit', 'Connaissance', '0626262626', '0102030430', 'benoit.henry@example.com', 'Male', 2, 1),
(50, 'Schmitt', 'Mélanie', 'Voisine', '0627272727', '0102030431', 'melanie.schmitt@example.com', 'Female', 3, 2),
(51, 'Hubert', 'Quentin', 'Ancien collègue', '0628282828', '0102030432', 'quentin.hubert@example.com', 'Male', 1, 1),
(52, 'Bonnet', 'Clara', 'Ami du sport', '0629292929', '0102030433', 'clara.bonnet@example.com', 'Female', 2, 3),
(53, 'Adam', 'Noah', 'Camarade de promo', '0630303030', '0102030434', 'noah.adam@example.com', 'Male', 3, 2),
(54, 'Barre', 'Eva', 'Amie de fac', '0631313131', '0102030435', 'eva.barre@example.com', 'Female', 1, 1),
(55, 'Noel', 'Nathan', 'Collègue', '0632323232', '0102030436', 'nathan.noel@example.com', 'Male', 2, 2),
(56, 'Giraud', 'Margaux', 'Amie de lycée', '0633333333', '0102030437', 'margaux.giraud@example.com', 'Female', 3, 1),
(57, 'Cousin', 'Léo', 'Ami d’enfance', '0634343434', '0102030438', 'leo.cousin@example.com', 'Male', 1, 3),
(58, 'Marchand', 'Jade', 'Voisine', '0635353535', '0102030439', 'jade.marchand@example.com', 'Female', 2, 2),
(59, 'Jacquet', 'Enzo', 'Connaissance', '0636363636', '0102030440', 'enzo.jacquet@example.com', 'Male', 3, 1),
(60, 'Lemoine', 'Inès', 'Camarade de classe', '0637373737', '0102030441', 'ines.lemoine@example.com', 'Female', 1, 2),
(61, 'Philippe', 'Matteo', 'Collègue', '0638383838', '0102030442', 'matteo.philippe@example.com', 'Male', 2, 3),
(62, 'Vidal', 'Lina', 'Amie d’enfance', '0639393939', '0102030443', 'lina.vidal@example.com', 'Female', 3, 2),
(63, 'Renard', 'Victor', 'Contact pro', '0640404040', '0102030444', 'victor.renard@example.com', 'Male', 1, 1),
(64, 'Leclerc', 'Maëlle', 'Amie de fac', '0641414141', '0102030445', 'maelle.leclerc@example.com', 'Female', 2, 1),
(65, 'Lopez', 'Hugo', 'Ami du sport', '0642424242', '0102030446', 'hugo.lopez@example.com', 'Male', 3, 2),
(66, 'Delattre', 'Lucie', 'Connaissance', '0643434343', '0102030447', 'lucie.delattre@example.com', 'Female', 1, 3),
(67, 'Masson', 'Romain', 'Camarade de promo', '0644444444', '0102030448', 'romain.masson@example.com', 'Male', 2, 2),
(68, 'Roy', 'Ambre', 'Amie de lycée', '0645454545', '0102030449', 'ambre.roy@example.com', 'Female', 3, 1),
(69, 'Berger', 'Clément', 'Collègue', '0646464646', '0102030450', 'clement.berger@example.com', 'Male', 1, 1),
(70, 'Rolland', 'Alicia', 'Voisine', '0647474747', '0102030451', 'alicia.rolland@example.com', 'Female', 2, 2),
(71, 'Charles', 'Tom', 'Contact pro', '0648484848', '0102030452', 'tom.charles@example.com', 'Male', 3, 3),
(72, 'Guichard', 'Laura', 'Amie de fac', '0649494949', '0102030453', 'laura.guichard@example.com', 'Female', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `ville`
--

CREATE TABLE `ville` (
  `NumVille` int(11) NOT NULL,
  `NomVille` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `ville`
--

INSERT INTO `ville` (`NumVille`, `NomVille`) VALUES
(1, 'Paris'),
(2, 'Lyon'),
(3, 'Marseille'),
(4, 'fes');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`NumCat`);

--
-- Index pour la table `contact`
--
ALTER TABLE `contact`
  ADD PRIMARY KEY (`id`),
  ADD KEY `NumVille` (`NumVille`),
  ADD KEY `NumCat` (`NumCat`);

--
-- Index pour la table `ville`
--
ALTER TABLE `ville`
  ADD PRIMARY KEY (`NumVille`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `NumCat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `contact`
--
ALTER TABLE `contact`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT pour la table `ville`
--
ALTER TABLE `ville`
  MODIFY `NumVille` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `contact`
--
ALTER TABLE `contact`
  ADD CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`NumVille`) REFERENCES `ville` (`NumVille`),
  ADD CONSTRAINT `contact_ibfk_2` FOREIGN KEY (`NumCat`) REFERENCES `categorie` (`NumCat`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
