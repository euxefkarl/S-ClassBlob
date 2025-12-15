
# S-Class Blob

Hello, this is S-Class Blob a game made with vanilla Java, meant as a introduction to OOP and Java for me. 

*Credits to RyiSnow's BBA tutorial for being the backbone of this project*

Hopefully this game turns out well enough.

# Controls
- WASD - Directional Movement/Window Navigation
- Enter - Title Screen confirmation
- E - Status Screen
- Space - Interact/Attack
- P - Pause
- F - Skill
- Q - Swap Form

# Devlogs
Devlog 1.0 6/11/25:
- initialized game window
- initialized game loop/clock
- drawed basic rectangle to screen
- initialized basic movement input


Devlog 1.1 6/11/25
- implemented player sprite
- refactorized movement controls to encapsulate Player class

Devlog 1.2 6/13/25

- implemented map tiles and map read and draw
- added environment tiles
- implemented player cam
- will be redesigning world map (consider this world map as placeholder)

Devlog 1.3 8/24/25

- implemented collision detection
- fixed infinite movement bug

Devlog 1.4.1 8/26/25

- implemented player interactable objects
- - no functionality as of now, only spawning and placement

Devlog 1.4.2 8/27/25
- implemented basic object interaction (object disappear once player touches them)
- implemented ui elements, such as powerup status and messages 

Devlog 1.4.3 11/20/25
- cleaned up codebase due to following treasure hunt format of tutorial
- implemented rudimentary pause/play state
- revamped map with tile editor 

Devlog 1.5 11/21/25
- implemented npc dialogue
- implemented title screen
- cleaned up game states

Devlog 1.6 12/10/25
- added 1st monster class: goblins
- added life mechanic (no death yet)
- clean up collision checker code readbility

Devlog 1.7 12/12/25
- added player status
- added monster death and death animation
- added monster health bar 

Devlog 1.8 12/14/25
- added inventory
- added form change (item equips)
- added projectiles (item ability)
- bug fixes

Devlog 1.9 12/16/25
- added pathfinding ai to npc (will add to monsters next)
- added item drops
- finalized form changes and form abilities (knockback, heal, dmg multiplier)
