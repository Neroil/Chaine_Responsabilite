# MCR - Chaine de Responsabilité

## Le Donjon d'Edaryaban :crossed_swords:

## Introduction

> Arthur Junod, Edwin Häffner, Yanis Ouadahi, Esteban Lopez

> Thu, May 16, 2024

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

---
# Rapport

```
Intro
Présentation jeu (diagramme, structure)
Choix d'implémentation
Chaînes
Conclusion
```


# Introduction

Le but de notre projet est de montrer l'utilisation du modèle de conception "Chaine de responsabilité" en le mettant en place dans un petit jeu (de type "Dungeon Crawler"). Pour effectuer cela, nous avons utilisé le framework LibGDX et donc l'entièreté de notre projet est conçu en Java. 

> le jeu d'abord, présentation, comment jouer/lancer

## Spécifications techniques/Implémentation

### LibGDX 

LibGDX est un framework de développement de jeux libre, sous licence Apache, et multiplateforme écrit en Java. Le choix de cette librairie a été fait car nous voulions rester sur une approche relativement proche du code, sans utiliser un game engine complet. Nous voulions également explorer un framework plus complet que JavaSwing.

Il y a aussi beaucoup de documentation et d'exemple pour utiliser LibGDX ce qui a fait pencher la balance pour le choix de ce game engine.

### Description détaillée du concept du jeu (en haut)

Le Donjon d'Edaryaban est un jeu d'exploration de type "Dungeon Crawler" où le joueur est piégé dans un labyrinthe souterrain généré aléatoirement et peuplé d'ennemis. L'objectif principal est de trouver la sortie de ce donjon.

Au début d'une partie, le joueur apparaît aléatoirement dans l'une des salles du donjon. Mais il n'est pas seul, des ennemis apparaissent aussi dans ce donjon.

Le joueur peut trouver de l'équipement dans ce Donjon, ce qui lui permettera de changer d'arme, afin d'avoir plus de dégats ou plus de portée, ou de récupérer des équipements qui lui feront faire plus de dégats ou réduiront les coûts d'utilisations de ces armes.

### Genre, gameplay, mécaniques principales (en haut)

Notre gameplay est essentiellement basé sur ce style de jeu, c'est-à-dire que le but est de se déplacer avec les flèches du clavier et de trouver l'escalier permettant d'accéder à la prochaine salle, jusqu'à réussir à sortir du donjon.

Nous avons cependant prévu une autre partie du gameplay qui, elle, est axée sur le combat avec des ennemis présents sur la carte.

Finalement, une autre fonctionnalité est l'interaction avec des objets ou armes.

```
Le jeu ressemble à un "Donjon crawler" ou le jeu "Rogue".

Le gameplay se base principalement sur l'exploration du donjon et le combat contre les ennemis.

Explorer le donjon est important afin de trouver l'échelle qui nous permets de sortir du donjon et de trouver de l'équipement pour simplifier les combats contre les ennemis qur nous rencontrerons.
```

> Dans systèmes de jeu ? et peut être comment jouer ici ?

Le combat reste assez basique avec une gestion des ressources (mana ou vigueur) suivant le type de l'arme que nous avons équipée et éventuellement, suivant l'arme équipée, un système de 
    
### Ecran de jeu (Jeu et Win/Lose)

L'écran principal du jeu est composé des éléments suivants et se comportant ainsi :

- La caméra est centrée sur le personnage que le joueur manipule, et donc suit chaque déplacement de celui-ci
- Le personnage peut se déplacer selon les 4 points cardinaux, NORD, SUD, EST et WEST
- Des ennemis (tous du même type) sont disseminés sur le niveau et se déplacent également
- Des armes ou objets que le joueur peut ramasser sont présents sur le sol

Il y a également 2 écrans supplémentaires, pour la victoire et la défaite, qui affiche simplement un message de victoire/défaite et comment relancer le jeu. On meurt si on se trouve sans vie et on gagne si on trouve la sortie. Pour le moment il suffit simplement de trouver la sortie une seule fois, mais il est facilement possible de mettre en place un système qui compte combien de fois on a trouver la sortie et ainsi donner la possibilité de gagner si on a survécu n étages du donjon.

### Systèmes de jeu (combat, objets, inventaire, etc.)


> Présentation du jeu ? ou présentation plus générale avant les spécification à voir avec plus haut


### Génération aléatoire des niveaux

Le système pour générer un niveau est assez simple. En premier lieu on créer une couche de murs pleins et dans cette couche on va enlever aléatoirement des rectangles. Ces rectangles deviennent les pièces de notre niveau. 

Il faut ensuite les relier entre eux et voilà, nous avons un niveau basique mais fonctionnel pour ce que nous voulons faire.

### Système d'attaque avec les différentes armes

Notre personnage démarre avec seulement ses poings comme arme, ce qui est également l'arme utilisée par les ennemis.

Sur la carte du jeu, différentes armes peuvent apparaitre : 

- Une épée
- Un gourdin
- Un Sceptre magique

Les deux premières armes sont des armes physiques qui utilisent de la vigueur pour pouvoir être utilisé. Alors que la dernière est une arme magique qui elle utilise de la mana.

### Gestion des ennemis (IA, spawn, etc.)

Tous les ennemis savent ou le joueur se trouve à tout moment. A chaque game step, l'ennemi peux faire deux choses, se déplacer ou attaquer.

- Déplacement
    - Si le joueur est proche de l'ennemi, il va se déplacer vers le joueur. C'est une simple distance donc il n'y a aucun système de pathing. Il va juste bêtement se déplacer en ligne droite, même si un mur peut le bloquer.
    - Si le joueur n'est pas assez proche, l'ennemi va simplement se déplacer aléatoirement.

- Attaque
    - Si l'ennemi est sur une même coordonnée x ou y que le joueur et qu'il est assez proche pour effectuer son attaque, il le fait. Sinon il essaie de se déplacer. L'ennemi utilise le même système que le joueur pour effectuer des attaques, c'est à dire qu'il peut manquer son attaque et c'est aussi possible de plus tard, on peut rajouter différents types d'ennemis qui n'ont pas tous la même arme, donc un comportement différent vis à vis de la géstion de l'attaque.


Pour le moment, tous les ennemis ont 10 points de vie et il en apparait 10 dans le niveau (à vérifier). Il n'y a pas de système de respawn donc si on a nettoyé le donjon, tant mieux pour nous ! 


### "Chaîne de responsabilité" dans le jeu

#### Petit rappel

Le modèle de conception "Chaîne de Responsabilité" est un modèle qui permet de transformer une requête complexe en une série de handlers, de requêtes simples. Chaque handler décide s'il peut traiter la requête lui-même ou la transmettre au handler suivant dans la chaîne. L'utilité de ce model est de pouvoir dynamiquement modifier le comportement d'une requête au cours de l'exécution du code. Il aide aussi à hiérarchiser les comportements et ainsi avoir un code plus évolutif.


Dans notre jeu "Le Donjon d'Edaryaban", nous avons implémenté ce modèle pour gérer différents aspects du gameplay : le système de combat et les interactions avec les objets. 

## Conception générale
- Architecture logicielle (diagrammes UML si pertinents)
- Conception du modèle "Chaîne de responsabilité"
    - Diagrammes/Schémas explicatifs
    - Handlers prévus (AttackHandler, RangeHandler, etc.)
- Autres patrons de conception utilisés le cas échéant


# Mise en œuvre
- Structure du projet (paquets, classes principales, etc.)
- Implémentation des différentes fonctionnalités
    - Système de mouvement (joueur, ennemis)
    - Gestion des collisions
    - Génération des niveaux
    - Système de combat
    - Interface utilisateur
    - Etc.
- Défis rencontrés et solutions apportées
- Tests et validation

## Défis rencontrés 

Vu que nous avons jamais fait de projet de cette sorte, le premier défis était de se répartir les taches. Sachant que personne ne savais comment utiliser LibGDX, nous avons dû en premier lieu tous essayer le framework de notre coté.

# Conclusion
- Bilan du projet
- Perspectives d'évolution

N'hésitez pas à ajouter des sections supplémentaires si nécessaire (par exemple une section dédiée aux références). N'oubliez pas d'inclure toute information pertinente liée à la répartition des tâches au sein de l'équipe.

# Rapport - MCR - Chaîne de responsabilités
## Le Donjon d'Edaryaban :crossed_swords:
> Auteurs : Arthur Junod, Edwin Häffner, Yanis Ouadahi, Esteban Lopez
## Introduction
Le but de notre projet est de montrer l'utilisation du modèle de conception "Chaine de responsabilité" en le mettant en place dans un petit jeu (de type "Dungeon Crawler"). Pour effectuer cela, nous avons utilisé le framework LibGDX et donc l'entièreté de notre projet est conçu en Java.
## LibGDX

LibGDX est un framework de développement de jeux libre, sous licence Apache, et multiplateforme écrit en Java. Le choix de cette librairie a été fait car nous voulions rester sur une approche relativement proche du code, sans utiliser un game engine complet. Nous voulions également explorer un framework plus complet que JavaSwing.

Il y a aussi beaucoup de documentation et d'exemple pour utiliser LibGDX ce qui a fait pencher la balance pour le choix de ce framework.

## Présentation du jeu
Le Donjon d'Edaryaban est un jeu d'exploration de type "Dungeon Crawler" où le joueur est piégé dans un labyrinthe souterrain généré aléatoirement et peuplé d'ennemis. L'objectif principal est de trouver la sortie du donjon.

Le temps en jeu avance à chaque déplacement ou attaque que fait le joueur (step), tous les ennemis n'effectuent des actions qu'à chaque step.

Au début d'une partie, le joueur apparaît aléatoirement dans l'une des salles du donjon. Il peut ensuite trouver de l'équipement éparpillé sur le sol, ce qui lui permettera de changer d'arme, afin d'avoir plus de dégats ou plus de portée, ou de récupérer des équipements qui augmenteront les dégats de ses armes ou réduiront les coûts d'utilisations en mana ou en vigueur.

On peut se déplacer dans quatre directions différentes HAUT, BAS, GAUCHE et DROITE. Notre personnage n'attaque que dans la direction dans laquelle son dernier déplacement s'est fait, puis une fois au corps à corps d'un ennemi nous pouvons nous déplacer dans sa direction afin de se tourner face à lui et l'attaquer (il n'y a pas de repère visuel pour savoir dans quelle direction notre personnage regarde il faut attaquer pour savoir).

Durant toute la partie il faut gérer notre mana, vigueur et vie. La vigeur et la mana se regénère avec le temps mais il n'y a aucun moyen de faire remonter sa vie. Nous avons une plus grande quantité de mana que de vigueur mais elle se regénère moins vite que cette dernière.

### Listes des éléments du jeu

| Arme            | Dégats | Portée | Cooldown (nb steps d'attente) |    Coût    |                        Sprite                        |
| --------------- |:------:|:------:|:-----------------------------:|:----------:|:----------------------------------------------------:|
| Poings          |   1    |   1    |               0               | 5 vigueur  | ![griffe](https://hackmd.io/_uploads/r150ztnBC.png)  |
| Épée            |   3    |   3    |               0               | 10 vigueur |  ![epee](https://hackmd.io/_uploads/HkBrEtnB0.png)   |
| Gourdin         |   5    |   1    |               1               | 20 vigueur | ![gourdin](https://hackmd.io/_uploads/B1FBVthSA.png) |
| Sceptre magique |   3    |   10   |               1               |  30 mana   | ![sceptre](https://hackmd.io/_uploads/BJd84KhHC.png) |

| Item              | Description                                            |                         Sprite                         |
| ----------------- | ------------------------------------------------------ |:------------------------------------------------------:|
| Anneau de dégats  | Double les dégâts de toutes les attaques               | ![damagerin](https://hackmd.io/_uploads/Sykv4t3SR.png) |
| Anneau de vigueur | Divise par 2 le coût en vigueur des attaques physiques | ![virgorrin](https://hackmd.io/_uploads/r1fDEt2H0.png) |
| Anneau de mana    | Divise par 2 le coût en mana des attaques magiques     |  ![manarin](https://hackmd.io/_uploads/S1vDVt3SC.png)  |
| Echelle           | Nous fait gagner la partie                             |   ![echelle](https://hackmd.io/_uploads/HJEmBF2H0.png) |

| Entité | Description                                            |                       Sprite                        |
| ------ | ------------------------------------------------------ |:---------------------------------------------------:|
| Joueur | Le personnage que nous contrôlons                      | ![player](https://hackmd.io/_uploads/SyA7BF2BA.png) |
| Ennemi | L'ennemi de base du jeu, il attaque avec l'arme poings | ![enemy](https://hackmd.io/_uploads/r1Z4rF2SR.png)  |

Tous les sprites utilisés ont été pris sur Itch.io : 

- Murs, echelle, joueur et ennemis : https://pixel-poem.itch.io/dungeon-assetpuck
- Anneaux : https://mr-pixelz.itch.io/16x16-pixel-magic-items
- Armes : https://powered-by-decaf.itch.io/simple-pixel-art-weapons-16x16

### HUD du jeu

![infohud](https://hackmd.io/_uploads/ry5necnSC.png)

Dans notre jeu nous avons trois informations essentielle : 
1. Les informations des ressources du joueur, dans l'ordre : 
    - La mana (Bleu)
    - La vigeur (Jaune)
    - La vie (Rouge)
2. L'arme équipée actuellement
3. Les objets présent dans l'inventaire du joueur


## Comment jouer

Téléchargez le .jar du jeu sur [GitHub](https://github.com/Neroil/Chaine_Responsabilite/releases/tag/1.0). 

Vous pouvez le lancer en cliquant sur le fichier .jar ou bien en utilisant ceci dans le fichier racine du téléchargement du jeu : 
```bash!
java -jar ./desktop-1.0.jar
```

Il est nécessaire d'avoir java d'installé sur votre machine.

### Touches utilisée par le jeu : 
- **Flèche du clavier** - Déplacement du joueur
- **Shift Gauche** - Accelérer les déplacements du joueur
- **R** - Reset du jeu
- **Y** - Attaquer

## Choix d'implémentation

Le diagramme UML du projet est situé en annexe.

### Ecran de jeu (Jeu et Win/Lose)

L'écran principal du jeu est composé des éléments suivants et se comporte ainsi :

- La caméra est centrée sur le personnage que le joueur manipule, et suit donc chaque déplacement de celui-ci
- Le personnage peut se déplacer selon les 4 points cardinaux, NORD, SUD, EST et OUEST
- Des ennemis (tous du même type) sont disseminés sur le niveau et se déplacent également
- Des armes ou objets que le joueur peut ramasser sont présents sur le sol

Il y a également 2 écrans supplémentaires, pour la victoire et la défaite, qui affiche simplement un message de victoire/défaite et comment relancer le jeu. On meurt si on se trouve sans vie et on gagne si on trouve la sortie. Pour le moment, il suffit simplement de trouver la sortie une seule fois, mais il est facilement possible de mettre en place un système qui compte combien de fois on a trouvé la sortie et ainsi donner la possibilité de gagner si on a survécu à *n* étages du donjon.

### Génération aléatoire des niveaux

Nous avons implémenté un système de génération procédurale de niveaux simple pour ce jeu, un élément clé du genre dungeon crawler pour rendre chaque niveau aléatoire et imprévisible.

Le processus commence par la création d'une grille de cases remplie de murs. Des pièces rectangulaires sont ensuite générées aléatoirement dans cette grille, en respectant des contraintes de taille minimale et maximale. Ces pièces sont ensuite creusées en supprimant les cases de mur correspondantes dans la grille des murs.

Pour rendre le donjon navigable, des couloirs sont créés pour relier les pièces entre elles dans une façon semi aléatoire pour rendre le donjon plus intéressant. 

Les salles et les couloirs sont ensuite sauvegardés dans une liste, ce qui permet de placer les ennemis et les objets uniquement dans les zones accessibles du niveau. Enfin, les murs restants qui sont adjacent aux parties jouable sont décorés pour donner un aspect visuel cohérent au donjon.

### Gestion des ennemis (IA, spawn, etc.)

Tous les ennemis savent où le joueur se trouve à tout moment. A chaque *game step*, l'ennemi peut choisir d'effectuer une action, se déplacer ou attaquer.

**Déplacement**
- Si l'ennemi est proche du joueur, il va se déplacer vers lui. C'est une simple distance définie donc il n'y a aucun système de pathing. Il va simplement se déplacer en ligne droite, même si un mur peut le bloquer.
- Si le joueur n'est pas assez proche, l'ennemi va simplement se déplacer aléatoirement sur la map.

**Attaque**
- Si l'ennemi est sur une même coordonnée x ou y que le joueur et qu'il est assez proche pour effectuer son attaque, il le fait. Sinon il essaie de se déplacer. Il utilise le même système que le joueur pour effectuer des attaques, c'est-à-dire qu'il a également un risque (défini à 1 chance sur 5) de manquer son attaque.
- (**DANS CONCLUSION**) Une possibilité d'amélioration est d'ajouter différents types d'ennemis qui n'ont pas tous la même arme, et donc un comportement différent vis-à-vis de la gestion de l'attaque (distance, mana restante, ...).
- Tous les ennemis ont 10 points de vie et il en apparait un certain nombre (défini à 5) dans le niveau. Il n'y a pas de système de *respawn*, donc si on a nettoyé le donjon, tant mieux pour nous !

### Gestion des objets par le joueur

Comme dit précédemment, le joueur peut ramasser des objets disseminés sur la carte. Leurs différents effets sont décrits précédemment dans le rapport.

A chaque step du jeu, on vérifie si le joueur se trouve sur une case comportant un objet, si c'est le cas, il le ramasse automatiquement. L'objet ramassé va ensuite faire une action qui va dépendre de son type : 

- Si c'est un objet de type "Arme", il va remplacer l'arme que le joueur a actuellement en ça possession, l'arme remplacé est ensuite supprimée du jeu.
- Si c'est un objet de type "Objet", il va se ranger, selon son type spécialisé (attaque ou défense) dans l'inventaire du joueur. Il n'y a pas encore d'objet défensifs mais l'implémentation est déjà disponible. Il n'y a pas non plus de limite d'objets que peux avoir le joueur.
- Si c'est un objet de type "Echelle", il va appeler la fonction de fin de niveau du joueur. Dans l'état actuel de notre jeu, terminer le niveau va aussi appeler la fonction de fin de jeu qui met fin à la partie.


## Chaînes de responsabilités
Les chaînes de responsabilités sont simple à mettre en place quand nous devons faire un système qui gère une requête que l'on doit traiter ou non suivant la nature de celle-ci. Elles sont également intéressantes car elle nous permettent de les modifier dynamiquement.

Dans notre code nous avons mis en place une classe abstraite *Handler* et l'interface *Request* qui nous permettent de mettre en place la logique générale de nos chaîne de responsabilité.

Nous avons deux chaînes distinctes dans notre jeu, la chaîne d'attaque et la chaîne de requête de dégats, qui sont appelées l'une après l'autre.

Les handler disposent d'une fonction `setSuccessor()` qui leur permet de chaîne la construction de notre chaîne et que nous avons surtout utilisée pour la création de notre chaîne d'attaque.

Nos chaîne retournent un *boolean* qui l'on peut utiliser suivant la chaîne pour avoir une information sur l'état de celle-ci. Nous avons défini que si nous arrivons à la fin de nos chaînes, celles-ci retournent *true*.

A noter que l'interface *Request* ne sert qu'a manipuler tous les type de requête different avec la classe abstraite *Handler* et qu'il existe deux spécialisation de *Handler* (une par chaîne) qui servent à cast l'interface *Request* en un type de requête associé à la chaîne actuelle afin que l'on puisse accéder aux attributs de cette requête.

### Chaîne d'attaque
Cette chaîne est appelée à chaque fois que nous voulons faire une attaque avec notre arme et sert à savoir si notre attaque va réussir ou échouer. Elle est également recréée dynamiquement à chaque fois que nous ramassons une arme ou un item, car sa composition va changer suivant l'équipement de notre personnage.

Elle utilise l'objet *AttackRequest* afin de passer les informations de l'attaque tout son long.

Cette *AttackRequest* a comme informations
- Les dégats de l'attaque
- Le coût de l'attaque
- Le type d'attaque (physique ou magique)
- Le temps de récupération de l'arme utilisée
- La dernière attaque lancée avec l'arme actuelle
- Le temps en jeu du lancement de l'attaque
- Le joueur qui a lancé l'attaque

L'arme équipée de notre personnage change donc les informations de base de cette *AttackRequest* car nous construisons cette requête par rapport à celle-ci. Par la suite les items viennent modifier certaines partie de la requête (ici, le coût et les dégats) avant que l'on fasse des vérification sur les informations de la requête.

Tous les handlers la composant héritent de *AttackHandler* qui est une spécialisation de *Handler*.

| Handler               | Description                                                                                        |
| --------------------- | -------------------------------------------------------------------------------------------------- |
| CooldownHandler       | Vérifie si notre arme est en temps de récupération ou non                                          |
| HitChanceHandler      | Vérifie si nous passons le "jet de dés" à chaque attaque qui peut la faire échouer aléatoirement   |
| ManaHandler           | Vérifie si nous avons assez de mana pour lancer l'attaque et si oui baisse la mana du joueur       |
| VigorHandler          | Vérifie si nous avons assez de vigueur pour lancer l'attaque et si oui baisse la vigueur du joueur |
| CostModifierHandler   | Modifie le coût de l'attaque contenu dans *AttackRequest*                                          |
| DamageModifierHandler | Modifie les dégâts de l'attaque contenus dans *AttackRequest*                                      |

À savoir que *ManaHandler* et *VigorHandler* sont tous les deux des enfant de la classe abstraite *RessourceHandler* qui met en place la logique générale de la vérification des ressources du joueur. 

Notre chaîne est donc construite ce la façon suivante :
1. On commence par ajouter le *CooldownHandler* pour faire échouer, au plus tôt, notre attaque si l'arme est en temps de récupération.
2. Puis on ajoute le *HitChanceHandler* qui nous permet de faire le "jet de dés" (encore une fois, on veut faire échouer l'attaque au plus tôt pour éviter les passages dans des handlers inutiles).
3. On ajoute, s'il y en a, les handlers des items ce qui nous permet de changer les informations de notre *AttackRequest* avant d'aller plus loin.
4. On fini par mettre le *RessourceHandler* correspondant à l'arme.

Comme chaîne sert à savoir si l'attaque à réussi ou échouer, nous utilisons le *boolean* de retour pour avoir cette information. Si celui-ci est égal à *false* on considère l'attaque ratée et on s'arrête la pour l'action d'attaque, cependant s'il est égal à *true*, nous allons créer une chaine de requête pour la chaîne de requête de dégat qui utilisera les dégats de notre *AttackRequest*.

### Chaîne de requête de dégats

Cette chaîne est appelée après la chaîne précédente quand l'attaque est considérée comme réussie et elle nous permet de gérer l'application des dégats sur les potentielles cibles que l'on peut trouver dans la portée de notre attaque. Elle se crée dans chaque entités (joueur ou ennemis) et ne change pas dynamiquement.

Elle utilise l'objet *DamageRequest* pour garder ses informations tout son long.

*DamageRequest* contient :
- Les dégats à appliquer
- La liste des positions que l'on attaque avec notre arme (calculées en dehors de la chaîne)
- La liste de toutes les entités du jeu
- La liste des cibles de notre attaque (vide au début remplie dans la chaîne)

Tous les handlers la composant héritent de *DamageHandler* qui est une spécialisation de *Handler*.

| Handler       | Description                                                                                                               |
| ------------- | ------------------------------------------------------------------------------------------------------------------------- |
| TargetHandler | Parcours la liste des positions attackées puis la liste des entités afin de trouver les cibles touchées par notre attaque |
| HitHandler    | Applique les dégats a nos cibles, s'il y en a, en appelant leur fonction de dégats                                        |

Il faut donc que les deux handler soient appelés dans l'ordre données suivant : *TargetHandler* puis *HitHandler*.

### Contexte d'appels de nos chaînes

Comme mentionné plus haut ces chaînes sont appelés pour gérer des attaque, mais différents type d'entités peuvent attaquer :

Quand le joueur attaque toutes les fonctionnalités décrites au-dessus sont utilisées, mais si un ennemis attaque il va utiliser les même handlers que notre joueur.

La différence est que l'ennemi ne va que utiliser le *HitChanceHandler* dans sa chaîne d'attaque, il ne va donc construire sa requête qu'avec les informations de son arme car il veut juste en récupérer les dégats.

Par contre il fera appel à l'entiéreté de la chaîne de requête de dégats afin d'appliquer les dégats de ses poings au joueur.

## Défis rencontrés 

Étant donné que nous n'avions jamais réalisé de projet de ce type auparavant, le premier défi a été de répartir les tâches. Comme personne ne maîtrisait LibGDX, nous avons commencé par nous familiariser individuellement avec ce framework. 

Donc beaucoup de temps a été "perdu" à cause de cela ce qui nous a limité dans ce que nous pouvions faire dans notre jeu. 

Comprendre comment le rendering fonctionne sur LibGDX nottament en utilisant des sprites batch et des couches pour le visuel était un peu complexe. De même avec la géstion de la camera. Nous avons encore le problèmes de lignes verticale et horizontale lorsqu'on redimensionne la fenêtre du jeu par exemple. 

## Conclusion

### Améliorations

- Utiliser un handler que l'on pourrait rajouter dynamiquement, suivant l'arme, dans la chaîne de requête de dégats, de cette manière si une arme n'attaque pas en ligne droite elle pourrait plus facilement l'indiquer.
- Créer un écran titre, des menus d'options, et intégrer de la musique et des effets sonores pour enrichir l'expérience de jeu. Concevoir un tileset personnalisé pour éviter de devoir utiliser les "placeholders" actuels. 
- Diversifier les types de salles générées et ajouter des éléments interactifs tels que des pièges pour augmenter la complexité et l'intérêt des niveaux.
- Optimiser le déplacement des ennemis en utilisant des algorithmes de recherche de chemin plus performants que simplement se diriger en ligne droite vers le joueur, comme l'algorithme A*.
- Mettre en place un menu d'inventaire permettant au joueur de visualiser et de sélectionner ses armes et objets, sans avoir à les supprimer pour en changer.



