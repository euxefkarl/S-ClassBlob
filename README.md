
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

Devlog 2.0 12/16/25
- added Final Boss
- added game over screen
- cleaned up code
- debug hitbox bugs

Devlog 2.1 12/19/2025
- Major overhaul
- Refactored majority of codebase to ensure SOLID adherence
- Collision and movement working again
- Combat and items in next update

Devlog 2.2 12/20/25
- Modularized most of the codebase
- Lessened dependencies as much as possible 
- * for example Gamepanel is a God Class, however implementing abstraction layers\ would have been time consuming and overengineering for a small project like this
- Implemented multi layer abstractions for behavior and combat 
- Implemented tests for combat system (Combat Component and Health Component) using JUnit 4
- Refactored to fit SOLID Design principles wherever possible and practical


Major Painpoints

- could not revamp titlescreen to instead display help menu instead of load game
- could not reimplement consumbale item, life gem
- refactored something i spent a month working on in 3 days leaves a lot to be desired
