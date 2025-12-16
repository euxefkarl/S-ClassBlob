package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, abilityPressed, changeFormPressed;
    public boolean showDebug;

    GamePanel gp;
    
    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        //do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        //playstate
        else if(gp.gameState == gp.playState){
            playState(code);
        }

        //pause state
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }
        //dialogue state
        else if(gp.gameState == gp.dialogueState){
            dialogueState(code);
        }
        else if(gp.gameState == gp.statusScreenState){
            statusState(code);
        }
        else if(gp.gameState == gp.gameOverState){
            gameOverState(code);
        }
        
        
    }
    public void titleState(int code){
        if(code == KeyEvent.VK_W){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 2;
                }
            }
            if(code == KeyEvent.VK_S){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){
                    gp.gameState = gp.playState;
                }
                if(gp.ui.commandNum == 1){
                    //load game
                }
                if(gp.ui.commandNum == 2){
                    System.exit(0);
                }
            }
            
    }
    public void dialogueState(int code){
        if (code == KeyEvent.VK_F || code == KeyEvent.VK_SPACE){
                gp.ui.currentDialogue = "";
                gp.gameState = gp.playState;
            }
    }
    public void pauseState(int code){
          if (code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
            }
    }
    public void statusState(int code){
        if(code == KeyEvent.VK_E){
                gp.gameState = gp.playState;
            }
        if(code == KeyEvent.VK_W){
                if(gp.ui.slotRow !=0){gp.ui.slotRow--;}
            }
        if(code == KeyEvent.VK_A){
                if(gp.ui.slotCol !=0){gp.ui.slotCol--;}
            }
        if(code == KeyEvent.VK_S){
                if(gp.ui.slotRow != 3){gp.ui.slotRow++;}
            }
        if(code == KeyEvent.VK_D){
                if(gp.ui.slotCol !=4){gp.ui.slotCol++;}
            }
        if(code == KeyEvent.VK_SPACE){gp.player.selectItem();}
    }

    public void gameOverState(int code){
        if(code == KeyEvent.VK_W){
            gp.ui.commandNum --;
            if(gp.ui.commandNum < 0){
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum ++;
            if(gp.ui.commandNum > 1){
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.gameState = gp.playState;
                gp.retry();

            }
            if(gp.ui.commandNum == 1){
                System.exit(0);
            }
        }
        
    }
    public void showDebugTextint(int code){
        if(showDebug == true){gp.player.invincible = false;showDebug = false;}
        else if (showDebug == false){gp.player.invincible = true;showDebug = true;}
    }

    public void playState(int code){
        if (code == KeyEvent.VK_W){upPressed = true;}
            if (code == KeyEvent.VK_A){leftPressed = true;}
            if (code == KeyEvent.VK_S){downPressed = true;}
            if (code == KeyEvent.VK_D){rightPressed = true;}
            if (code == KeyEvent.VK_P){gp.gameState = gp.pauseState;}
            if (code == KeyEvent.VK_SPACE){interactPressed = true;}
            if (code == KeyEvent.VK_E){gp.gameState = gp.statusScreenState;}
            if(code == KeyEvent.VK_T){showDebugTextint(code);}
            if(code == KeyEvent.VK_F){abilityPressed = true;}
            if(code == KeyEvent.VK_Q){gp.player.cycleForm();}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W){
            upPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;            
        }
    }

}
