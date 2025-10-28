# Projet de Base de Données

## Introduction

Ce projet est une implémentation Java visant à interagir avec différents systèmes de gestion de base de données (SGBD) comme Oracle et PostgreSQL. Le projet met en œuvre des concepts de génériques DAO (Data Access Object), permettant une abstraction des interactions avec la base de données.

## Structure du Projet

Le projet est divisé en plusieurs composants principaux :

- **database.core** : Contient les classes principales pour la configuration et la gestion des connexions à la base de données, ainsi que les opérations génériques sur les données.
- **database.provider** : Implémente des classes spécifiques pour chaque type de base de données, comme Oracle et PostgreSQL.
- **src.test** : Contient les classes de test pour s'assurer que les implémentations fonctionnent correctement.

## Configuration

Les configurations pour se connecter aux bases de données sont définies dans la classe `Config` au sein de `database.core`. Vous pouvez configurer les détails de connexion pour Oracle et PostgreSQL.

## Exécution des Tests

Pour exécuter les tests, assurez-vous d'avoir configuré correctement les bases de données et utilisez une suite de tests Java ou JUnit.

## Contribution

Les contributions sont les bienvenues. Veuillez soumettre des pull requests pour toute modification ou amélioration.

## Auteurs

Le projet a été développé par l'équipe de développement de base de données.

## License

Ce projet est sous licence MIT. Veuillez consulter le fichier LICENSE pour plus de détails.