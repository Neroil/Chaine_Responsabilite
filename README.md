# MCR - Chaine de Responsabilité

## Le Donjon d'Edaryaban :crossed_swords:

## Introduction

> [name=Arthur Junod, Edwin Häffner, Yanis Ouadahi, Esteban Lopez ]
> [time=Thu, May 16, 2024 1:28 PM]

### Outils utilisés

Moteur graphique : 

libGDX

TODO : Expliquer pourquoi


### Qu'est-ce qu'on fait 

Développement d'un jeu 2D dans le style de [Rogue](https://fr.wikipedia.org/wiki/Rogue_(jeu_vid%C3%A9o)) / Donjon Crawler mettant en lumière le modèle de conception "Chaîne de responsabilité". 

### Contenu

#### Ecrans:

##### Menu

- Titre 
- Information sur comment jouer (flèches du clavier, but -> trouver l'escalier et sortir du donjon !)
- Bouton *"quitter"*
- Bouton *"jouer"*

##### Ecran de jeu

- Un personne se déplaçant sur les 4 points cardinaux
- Caméra qui suit le personnage sur une carte plus grande
- 1 type d'ennemi
- Armes à trouver
- Génération de niveau "aléatoire"

#### Armes

**/!\ FAIRE EN SORTE DE RENDRE LES CHAINES DYNAMIQUES -> Bague qui rend le mana utilisé par 2, etc.** 

Vigueur -> Récupération rapide mais peu de vigueur max (100)

Mana -> Récupération lente mais plus de mana max (250)

Chance de rater l'attaque -> **15%** de chance de rater l'attaque

- Epee
    - Attaque en **arc**, **3 tiles** devant le character
    - Utilise **10** de **VIGUEUR**
    - Dégâts : **2hp**
    - **200ms** cooldown
- Sceptre Magique
    - Attaque à **distance** (jusqu'à toucher mur ou ennemi)
    - Utilise **10** de **MANA**
    - Dégâts : **1hp**
    - **400ms** cooldown
- Gourdin
    - Attaque **courte**, **1 tile**
    - Utilise **20** de **VIGUEUR** 
    - Dégâts : **4hp**
    - **350ms** cooldown

#### Ennemi
 
 Chance de rater -> 25% de chance de rater
 
 - Déplacement aléatoire en général
 - Si voit (range de x tiles) le personnage se déplace vers lui
 - Attaque dès qu'on est à portée (1 tile de distance)
 - Spawn aléatoirement sur la carte entière et 5 à 8 dans le jeu

#### Carte

- Salles générées aléatoirement reliées entre elles par des chemin de 1 case de large
    - Taille des salles: x cases min et x case max

#### Implémentations requises

- Génération de niveau dans ce style : <br/> ![image](https://hackmd.io/_uploads/SyYCydXXA.png) <br/>
Le jeu se joue dans un système de quadrillage ou le joueur et les ennemis peuvent se déplacer sur la carte. 
- Gestion du "spawn" des objets sur la map
- "Chaine de responsabilité" pour les attaques des différentes armes
- "Chaine de responsabilité" pour les intéraction avec les objets
- Gestion d'inventaire (?)
- Création des handlers nécessaires :
    - ManaHandler
    - RangeHandler
    - PositionHandler
    - AttackHandler
        - MagicalAttackHandler
        - PhysicalAttackHandler
    - (ItemHandler?)
    - (TrapHandler?)
- "IA" des ennemis
- "Spawn des ennemis"
- UI

##### Implémentation secondaire

- Différents ennemis
- Lutins
- Décors
- Musique


#### Librairies utile : 

https://libgdx.com/wiki/graphics/2d/tile-maps

https://libgdx.com/wiki/graphics/2d/spritebatch-textureregions-and-sprites

https://libgdx.com/wiki/graphics/viewports

https://libgdx.com/wiki/graphics/2d/orthographic-camera



### Répartition

- [ ] Mouvements (Esteban)
- [ ] Attaquer
- [ ] Ramasser un item
- [ ] Génération de la carte aléatoire (Edwin)
- [ ] Gestion collision (Edwin)
- [ ] Menu principal
- [ ] Armes + chaine de responsabilité spécifique à chaque type (Yanis)
- [ ] Ennemi + chaines de responsabilités (Arthur)
- [ ] Personnage avec sa mana, vigueur, vie
- [ ] UI
- [ ] Escalier pour finir partie
- [ ] Gestion de la caméra (Edwin)
