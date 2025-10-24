# Projet Example

Ce projet est une application de gestion de base de données utilisant Java. Il inclut plusieurs classes et fonctionnalités pour manipuler les données à travers différentes interfaces et implémentations.

## Description du Projet

Le projet utilise une structure modulaire pour interagir avec différentes bases de données. Des classes existent pour gérer :
- Les connexions à la base de données Oracle
- La gestion des séquences dans les bases de données
- Les opérations CRUD génériques via un DAO

## Structure

- `src/database/provider`: Contient les classes spécifiques aux fournisseurs de base de données.
- `src/database/core`: Inclut les implémentations de base telles que Sequence, GenericDAO.
- `src/test`: Comprend les classes de test pour valider le bon fonctionnement des fonctionnalités.

## Prérequis

- Java SDK (version 1.8 ou ultérieure)
- Maven pour gérer les dépendances du projet

## Installation

1. Cloner le dépôt sur votre machine locale.