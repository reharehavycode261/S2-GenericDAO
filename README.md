# Projet de Gestion de Base de Données

Ce projet est une application de gestion de base de données qui utilise Java pour la couche métier, et PostgreSQL ou Oracle comme fournisseurs de base de données.

## Objectif

L'objectif principal de ce projet est de fournir une structure solide pour la gestion des données dans diverses applications se connectant à une base de données relationnelle.

## Configuration

### Prérequis

- Java 8 ou supérieur
- PostgreSQL ou Oracle Database
- Maven (si vous utilisez Maven pour la gestion des dépendances)

### Structure du projet

- `src/database/core`: Contient les classes principales pour la configuration et l'interaction avec la base de données.
- `src/database/provider`: Fournit les classes spécifiques aux fournisseurs de bases de données comme PostgreSQL et Oracle.
- `src/test`: Contient des classes pour le test et la démonstration des fonctionnalités du projet.

## Exécution

1. Clonez le repository sur votre machine locale.
2. Configurez votre base de données avec les bonnes configurations de connexion dans la classe `Config.java`.
3. Compilez et exécutez le projet à l'aide de votre IDE préféré ou utilisez Maven pour compiler et exécuter le projet.

## Contribution

Les contributions sont les bienvenues. Veuillez vous assurer de bien tester vos modifications avant de soumettre une pull request.