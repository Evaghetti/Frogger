package frog;

import java.io.File;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Carro extends Rectangle {
    
    private BufferedImage spritesheet;
    private int curSprite, vel;
    
    public Carro(int x, int y, int largura, int altura, int dir, boolean caminhao) throws IOException {
        if (!caminhao) {
            curSprite = new Random().nextInt(4);
            spritesheet = ImageIO.read(new File("Imagens/carros.png"));
        }
        else {
            curSprite = 0;
            spritesheet = ImageIO.read(new File("Imagens/caminhão.png"));
        }
        this.setSize(new Dimension(largura, altura));
        this.setLocation(x, y);
        vel = 1 * ((dir == 0) ? 1 : -1);
    }
    
    public void draw(Graphics g, Engine desenhar) {
        BufferedImage temp = spritesheet.getSubimage(21 * curSprite, 0, 21, 13);
        g.drawImage(temp, this.x, this.y, this.width, this.height, desenhar);
    }
    
    public void mover() {
        this.x += vel;
        if (this.x > 640 + this.width)
            this.x = 0;
        else if (this.x < 0 - this.width)
            this.x = 640 + this.width;
    } 
    
}