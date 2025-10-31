# Projet de Gestion de Base de Données

## Introduction

Ce projet est un système de gestion de base de données qui permet de manipuler des entités stockées dans une base de données PostgreSQL à l'aide de classes Java. Il utilise un modèle d'accès aux données générique pour faciliter les opérations de CRUD (Create, Read, Update, Delete).

## Contenu du projet

- **src/database/core**: Contient les classes principales pour la gestion de la connexion et l'accès à la base de données.
- **src/database/provider**: Implémente le fournisseur de base de données, notamment PostgreSQL.
- **src/test**: Contient des exemples d'utilisation de ce système via des entités de test telles que `Emp` et `Plat`.

## Installation et Configuration

1. Assurez-vous d'avoir Java installé sur votre machine (Java 8 ou plus).
2. Clonez ce dépôt sur votre machine locale.
3. Configurez votre base de données PostgreSQL et mettez à jour les informations de connexion dans le code si nécessaire.
4. Compilez le projet en utilisant un IDE compatible Java ou en ligne de commande.

## Exécution

1. Exécutez la classe `Main` située dans le package `src/test` pour voir un exemple de fonctionnement avec les entités `Emp` et `Plat`.
2. Vous pouvez tester et ajouter plus d'entités selon vos besoins en suivant le modèle existant.

## Contributions

Les contributions à ce projet sont les bienvenues. Veuillez vous assurer de tester vos modifications avant de les soumettre.

## Aide et Support

Pour toute question ou aide, veuillez ouvrir une issue dans le repository GitHub.