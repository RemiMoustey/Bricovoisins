-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  mer. 19 fév. 2020 à 15:55
-- Version du serveur :  5.7.26
-- Version de PHP :  7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `brico-user_service`
--

-- --------------------------------------------------------

--
-- Structure de la table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(113);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `last_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `age` int(11) NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `town` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `points` int(11) NOT NULL,
  `level_carpentry` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level_diy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level_electricity` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level_gardening` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level_masonry` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level_painting` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level_plumbing` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `first_name`, `last_name`, `age`, `email`, `password`, `town`, `avatar`, `points`, `level_carpentry`, `level_diy`, `level_electricity`, `level_gardening`, `level_masonry`, `level_painting`, `level_plumbing`, `description`, `is_admin`) VALUES
(62, 'Rémi', 'Moustey', 23, 'remimoustey@gmail.com', '$2a$10$rcXuX0tobXZE60ZUzv3gfOvnxe9qCpWVKbkF4qvI2mavLkVXeZfau', 'Clermont-Ferrand', '/avatars/32202002120836.jpg', 116, NULL, 'little-works-diy', NULL, NULL, NULL, NULL, NULL, 'Étudiant en Informatique.\r\nJe n\'ai pas de grande spécialité en bricolage, mais je peux seconder efficacement quelqu\'un qui a besoin d\'une aide basique.', 1),
(63, 'Jean', 'J.', 71, 'Jeanjean63230@gmail.com', '$2a$10$DCWmcVsQf4Dfb2r/ry0wiu0DOeCHZFt/XUTyABZI52D/BB2sfgBai', 'Orcines', '/avatars/31202002120837.jpg', 30, 'connoisseur-carpentry', NULL, 'expert-electricity', 'little-works-gardening', 'expert-masonry', 'expert-painting', 'expert-plumbing', 'Bricoleur expert. J\'ai construit ma maison de A à Z.\r\nOutillage très complet.\r\nMais pas au niveau pour le jardinage.', 0),
(71, 'Gérard', 'B.', 30, 'GerardB@gmail.com', '$2a$10$QBhPN0pRR.furSEQXvXiu...8G0nacoe1aPsJn.9LEtZDzRtHZ2Ae', 'Pérignat-lès-Sarliève', '/avatars/01202002191456.jpg', 10, NULL, NULL, 'little-works-electricity', NULL, NULL, 'connoisseur-painting', NULL, 'Employé municipal.\r\nTouche à tout, mais surtout peinture et un peu électricité.', 0),
(72, 'Élodie', 'C.', 25, 'ElodieC@gmail.com', '$2a$10$8GtoHmnfr24v.UYqXZjXnOvZEcztqb8V3oVxARCZ8o6ucoeZhz2Tq', 'Orcines', '/avatars/02202002191457.jpg', 10, NULL, NULL, NULL, NULL, NULL, 'little-works-painting', NULL, 'Étudiante en médecine et j\'adore me détendre en effectuant des travaux de rénovation.', 0),
(73, 'Guy', 'H/', 32, 'GuyH@gmail.com', '$2a$10$sLHS8KWIkhQNY0cl3WcOzu94xaoJeOWqT9h6y/PbrHG4CxYcHG2Qa', 'Aubières', '/avatars/03202002191501.jpg', 10, NULL, 'little-works-diy', NULL, NULL, NULL, NULL, NULL, 'Ouvrier mécanicien. Je peux donner un coup de main pour n\'importe quel genre de travail.', 0),
(74, 'René', 'J.', 53, 'ReneJ@gmail.com', '$2a$10$vLXCQZVBYIDTNwoPJmQIZ.2mzhwaqNSDO63z9NWVIxE0Thrzn2FbC', 'Nohanent', '/avatars/04202002191502.jpg', 10, 'little-works-carpentry', NULL, 'little-works-electricity', NULL, 'little-works-masonry', 'little-works-painting', 'little-works-plumbing', 'À part le jardinage pour lequel je n\'ai aucun goût, je peux vous aider pour tout le reste, mais à un niveau raisonnable.', 0),
(75, 'Mouloud', 'K.', 35, 'MouloudK@gmail.com', '$2a$10$yu/hEnhXpBdq2mhPi/RfdOfuSIk8Ahf/9I38QEyPVdwWRLSw1Ev32', 'Lempdes', '/avatars/05202002191503.jpg', 10, NULL, 'connoisseur-diy', NULL, NULL, NULL, NULL, NULL, 'Formé dans les métiers du bâtiment en CFA, je peux intervenir pour tous bricolages de niveau connaisseur.', 0),
(76, 'Justine', 'F.', 24, 'JustineF@gmail.com', '$2a$10$rV38DVF7pqmnXo7/hvbHCONzn3.r93K0Hcljpkcsc.EnohX6ndXCq', 'Clermont-Ferrand', '/avatars/06202002191503.jpg', 10, NULL, NULL, 'connoisseur-electricity', NULL, NULL, NULL, NULL, 'Je suis en formation électricienne, j\'ai déjà obtenu mon CAP.', 0),
(77, 'André', 'R.', 49, 'AndreD@gmail.com', '$2a$10$781YjlNsUPEEBCA9DZjDHO5znz2Mw69Z7C5hvdWgoaF7zCYNxnfF.', 'Clermont-Ferrand', '/avatars/07202002191504.jpg', 10, NULL, NULL, NULL, NULL, 'little-works-masonry', 'expert-painting', NULL, 'Je suis ouvrier peintre et j\'ai des notions de maçonnerie.', 0),
(78, 'Adrien', 'D.', 22, 'AdrienD@gmail.com', '$2a$10$0zbYcQaFKEhomnjd.kJJ4uca5QSAfbx7A8OQN73k47iDj/bFt7haG', 'Gerzat', '/avatars/08202002191505.jpg', 10, NULL, 'little-works-diy', 'expert-electricity', NULL, NULL, NULL, NULL, 'Je travaille dans une entreprise en électricité. Je suis par ailleurs touche à tout en bricolage.', 0),
(79, 'Joël', 'A.', 32, 'JoelA@gmail.com', '$2a$10$0yD6WElk3pSdTR6T5jXVqOjfGBo1I1P7wwWYdLB9J4lNvFsYbAaQC', 'Gerzat', '/avatars/09202002191505.jpg', 10, NULL, NULL, NULL, NULL, NULL, NULL, 'expert-plumbing', 'Tous dépannages et installations plomberie.', 0),
(80, 'Arthur', 'D.', 55, 'ArthurD@gmail.com', '$2a$10$gvUPh0itEwe9zCwhMgvxJ.XDqE1OWxN6R2RynybiIto5.CWjACAsu', 'Aulnat', '/avatars/10202002191506.jpg', 10, NULL, NULL, NULL, 'expert-gardening', NULL, NULL, NULL, 'Ingénieur agronome.\r\nAccro au jardinage.', 0),
(81, 'Huguette', 'L.', 72, 'HuguetteL@gmail.com', '$2a$10$65.Ut0drjeSbou4iMTHywOXoQCna6WmUDrQjdg758OYOI5LwzFVBS', 'Châteaugay', '/avatars/11202002191506.jpg', 10, NULL, NULL, NULL, 'little-works-gardening', NULL, NULL, NULL, 'J\'aime me détendre en jardinant.', 0),
(82, 'Brahim', 'A.', 26, 'BrahimA@gmail.com', '$2a$10$MJSpSwFhKGQggyRpu1d.jeESIUiJbWB23LHQNA1mVmWxREY1LDa1.', 'Cébazat', '/avatars/12202002191507.jpg', 10, 'little-works-carpentry', NULL, NULL, NULL, NULL, NULL, NULL, 'Je suis apprenti menuisier. Je pense pouvoir vous aider au niveau connaisseur.', 0),
(83, 'Gaspard', 'Q.', 82, 'GaspardQ@gmail.com', '$2a$10$6sH06mKZv344G5xIXVMXYuYLWV3a123IGMOEUQ.HDOJgYRUqH/HBm', 'Saint-Genès-Champanelle', '/avatars/13202002191508.jpg', 10, NULL, NULL, NULL, NULL, NULL, NULL, 'expert-plumbing', 'Retraité.\r\n40 ans de métier.\r\nPatron d\'une entreprise de plomberie et chauffage.\r\nÂgé mais expert, je peux vous apporter connaissance du métier, outillage et participer à la mise en œuvre.', 0),
(84, 'Hector', 'V.', 71, 'HecotrV@gmail.com', '$2a$10$dztevfiON9JqHe4jiZ6DFuyUeHLadxtFrFUpxB5rR3tBK6S8yP3ZW', 'Pont-du-Château', '/avatars/14202002191508.jpg', 10, 'expert-carpentry', NULL, NULL, NULL, NULL, NULL, NULL, 'Retraité. J\'ai revendu mon entreprise de menuiserie, mais j\'ai toujours accès aux machines.', 0),
(85, 'Akim', 'P.', 21, 'AkimP@gmail.com', '$2a$10$lZwvHjBhCOV0abi.iXWEfuSSv3ISoRgfJQstAaA/JXklEFd3Odxmq', 'Blanzat', '/avatars/15202002191509.jpg', 10, NULL, NULL, NULL, NULL, 'connoisseur-masonry', 'connoisseur-painting', NULL, 'Apprenti du bâtiment.', 0),
(86, 'Marcel', 'T.', 78, 'MarcelT@gmail.com', '$2a$10$ptJ7AbHPsj8HrD4gXEtL3OhWayHe6pOHUDfvl9dt8s/N/xnd8gyBi', 'Romagnat', '/avatars/16202002191510.jpg', 10, NULL, 'expert-diy', NULL, NULL, NULL, NULL, NULL, 'Retraité. Bricoleur chevronné.\r\nJe sais tout faire, mais je n\'ai pas forcément tous les outils.', 0),
(87, 'Pierre', 'G.', 81, 'PierreG@gmail.com', '$2a$10$q8xB4.itd3XTLthDAH4BWugrv1u/pDRUwUfReD0UAl3g1XjkPOCtK', 'Royat', '/avatars/17202002191511.jpg', 10, NULL, NULL, NULL, NULL, NULL, NULL, 'connoisseur-plumbing', 'Ancien plombier, je suis encore capable de beaucoup de choses dans le métier.', 0),
(88, 'Rozalia', 'O.', 40, 'RozaliaO@gmail.com', '$2a$10$TFJkLCjkdWGpoq.eFxEG5.lp97ISIS4Rj5afZp8emwyoSrR64SKk2', 'Clermont-Ferrand', '/avatars/19202002191512.jpg', 10, 'connoisseur-carpentry', NULL, NULL, NULL, 'connoisseur-masonry', 'connoisseur-painting', 'connoisseur-plumbing', 'Secrétaire dans une entreprise de travaux public, je peux vous renseigner sur les normes et vous trouver les bonnes personnes.', 0),
(89, 'Jean-Claude', 'N.', 55, 'JeanclaudeN@gmail.com', '$2a$10$5GRfwn/l0H8MWBs72vDdV..cjgjLzPEfE/S318O9S9qkM7QHC1.Fe', 'Orcines', '/avatars/20202002191514.jpg', 10, NULL, NULL, NULL, 'expert-gardening', NULL, NULL, NULL, 'Agriculteur. Expert en potager.\r\nMatériel professionnel pour coupe d\'arbres et mise en stères.\r\nTronçonneuse.Tracteur. Remorque.', 0),
(90, 'Paul', 'C.', 61, 'PaulC@gmail.com', '$2a$10$VGAJma4obOdnSeDLqgk7EujZSV7ga2bsViUUVbMcbz/qm1ONll6pe', 'Cournon', '/avatars/21202002191514.jpg', 10, NULL, NULL, 'expert-electricity', NULL, 'expert-masonry', 'expert-painting', 'expert-plumbing', 'Jeune retraité chef de chantier du bâtiment multitâches.\r\nTrès bien outillé.', 0),
(91, 'Antonin', 'S.', 84, 'Antonin@gmail.com', '$2a$10$A2iSnsuKPnZjgW2j0SugRumF5P8cjXadl2a7h/KnzXYhFkbFc22Rm', 'Chamalières', '/avatars/18202002191515.jpg', 10, NULL, 'expert-diy', NULL, NULL, NULL, NULL, NULL, 'Architecte retraité. Je peux vous aider pour tous conseils mais je ne suis plus très actif pour ce qui est manuel.', 0),
(92, 'Léon', 'R.', 69, 'LeonR@gmail.com', '$2a$10$1W/DwR0hqwIwcLJKWH.DWeh2hX2F2OGFcNk0h1DQtFiXE8jAvdF0y', 'Lempdes', '/avatars/22202002191516.jpg', 10, NULL, 'little-works-diy', NULL, NULL, NULL, 'connoisseur-painting', 'connoisseur-plumbing', 'Je me débrouille pas mal en plomberie et en peinture et j\'aime bricoler un peu de tout.', 0),
(93, 'Ernest', 'W.', 56, 'ErnestW@gmail.com', '$2a$10$h2BH7g/pBY1Jt9Hga7U2ve1V9J2P1c8rPWPGRihIt/DmN1HvStAa.', 'Aubières', '/avatars/23202002191516.jpg', 10, NULL, 'connoisseur-diy', NULL, NULL, NULL, NULL, NULL, 'Bricoleur de bon niveau. Je suis bien outillé et je dispose d\'un grand atelier.', 0),
(94, 'Sophie', 'Y.', 39, 'SophieY@gmail.com', '$2a$10$ATkVsIDSUBTSrATrQ8VFwuyUTZHmh7.o0c242bpBUCL/AwE5bZ6YS', 'Gerzat', '/avatars/24202002191517.jpg', 10, 'connoisseur-carpentry', NULL, NULL, 'connoisseur-gardening', NULL, NULL, NULL, 'Je suis passionnée de jardinage et je restaure des meubles anciens.', 0),
(95, 'Emmanuel', 'M.', 44, 'EmmanuelM@gmail.com', '$2a$10$MHCSUcp0A5p/EMpKWy3KCODOqnVTiaLQcfhlU5ArjGDAIgBVOhwy6', 'Ceyrat', '/avatars/25202002191518.jpg', 10, NULL, NULL, NULL, 'little-works-gardening', 'little-works-masonry', 'connoisseur-painting', NULL, 'Informaticien.\r\nPassionné de travaux manuels.\r\n(mais pas tous).', 0),
(96, 'Marlène', 'D.', 41, 'MarleneD@gmail.com', '$2a$10$/rxy3aCUyYyujSmu9sCM0.0KYdEt0E2IQl73vavUcga9t6Kt.oPu6', 'Pont-du-Château', '/avatars/26202002191519.jpg', 10, NULL, NULL, NULL, 'connoisseur-gardening', NULL, 'little-works-painting', NULL, 'Vendeuse dans une jardinerie et passionnée de jardinage. J\'adore également tout ce qui est peinture.', 0),
(97, 'Jeanne', 'B.', 45, 'JeanneB@gmail.com', '$2a$10$gTNkE.rtbOcU9xBa0OoQgOYDYvoNSaJqTKbkF9I2xmKC0QmHlwcGq', 'Blanzat', '/avatars/27202002191519.jpg', 10, NULL, NULL, 'expert-electricity', NULL, NULL, NULL, NULL, 'Je travaille dans une entreprise de fournitures en électricité.', 0),
(98, 'Justine', 'V.', 49, 'JustineV@gmail.com', '$2a$10$ArtMqJq8eGeB2LfmSzggPucsKJh11pZdUJNf/MY2DX/SeU1yPx32S', 'Gerzat', '/avatars/28202002191520.jpg', 10, 'expert-carpentry', NULL, NULL, NULL, NULL, NULL, NULL, 'Je seconde mon mari dans son entreprise de menuiserie charpente et couverture.\r\nJe peux vous aider à trouver les bonnes personnes.', 0),
(99, 'Joëlle', 'Q.', 43, 'JoelleQ@gmail.com', '$2a$10$4jOoTLeQzAEB6ix6umqd/OwC3ctmC3mZwmJOAgbZ/2D1VAHfM3t0q', 'Cébazat', '/avatars/29202002191521.jpg', 10, NULL, 'connoisseur-diy', NULL, NULL, NULL, NULL, NULL, 'Ça va certainement vous surprendre, mais je suis une parfaite bricoleuse. Je suis très équipée en outillage et j\'étonne mon entourage par mon savoir-faire.', 0),
(100, 'Didier', 'G.', 56, 'DidierG@gmail.com', '$2a$10$q8jfXyK.07ITSpRF33Tqp.Cu0wlRzgn/kBnova0xy3A0kZkDTrY3G', 'Ceyrat', '/avatars/30202002191521.jpg', 10, NULL, 'little-works-diy', 'connoisseur-electricity', NULL, NULL, NULL, NULL, 'J\'ai un savoir-faire solide en électricité. Sinon, je fais tout moi-même.', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
