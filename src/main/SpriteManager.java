package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteManager {

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    

    public BufferedImage load(String imagePath, int width, int height) {
    UtilityTool uTool = new UtilityTool();
    BufferedImage image = null;
    try {
        image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        image = uTool.scaleImage(image, width, height);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return image;
}

}
