package entity;
import main.GamePanel;



//handles npc interaction, for now implemented for villager only
//future updates may include other implementation from this abstraction layer

public class DialogueComponent {

    private String[] dialogues;
    private int dialogueIndex = 0;
    private boolean exhausted = false; //no more dialogue

    //state machine when exhausted
    private Runnable onDialogueExhausted;

    public DialogueComponent(String[] dialogues) {
        this.dialogues = dialogues;
    }

    
    public void setOnDialogueExhausted(Runnable action) {
        this.onDialogueExhausted = action;
    }

    public void speak(GamePanel gp) {
        
        if (dialogues == null || dialogues.length == 0)
            return;

        //send ui the text
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        gp.gameState = gp.dialogueState;
        //if not exhausted then keep going
        if (dialogueIndex < dialogues.length - 1) {
            dialogueIndex++;
        } else {
            // if exhausted then specific action
            if (!exhausted && onDialogueExhausted != null) {
                onDialogueExhausted.run();
                exhausted = true; // ensure it only triggers once (optional)
            }
        }

        //set game state
        gp.gameState = gp.dialogueState;
    }
}