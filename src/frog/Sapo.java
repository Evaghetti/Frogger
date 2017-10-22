package frog;

import java.io.File;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Sapo extends Rectangle {
   
    public enum Direcao{CIMA, DIREITA, BAIXO, ESQUERDA};

    private Direcao direc;
    private BufferedImage spritesheet;
    private ArrayList<Integer> passosDados, ondeCaminhar;
    private final Rectangle posIni;
    
    private int posYat, posXini;
    private static int vidas, score;
    
    public Sapo(String camIm, int largura, int altura, int x, int y) throws IOException{
        super(x, y, largura, altura);
        posIni = new Rectangle(this);
        passosDados = new ArrayList<>();
        passosDados.add(this.y);
        ondeCaminhar = new ArrayList<>();
        spritesheet = ImageIO.read(new File(camIm));
        vidas = 5;
        score = 0;
        posXini = x;
        direc = Direcao.CIMA;
    }
    
    public void draw(Graphics g, Engine desenhar) {
        BufferedImage temp = spritesheet.getSubimage(21 * direc.ordinal(), 0, 21, 13);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        g.drawImage(temp, this.x, this.y, this.width, this.height, desenhar);
        for (int i = 1; i <= vidas; i++) 
            g.drawImage(spritesheet.getSubimage(0, 0, 21, 13), 40 * i, 3, desenhar);
        g.drawString(Integer.toString(score), 500, 17);
    }
    
    public void mover(int tecla) {    
        switch (tecla) {
            case KeyEvent.VK_RIGHT:
                direc = Direcao.DIREITA;
                this.x += this.width;
                break;
            case KeyEvent.VK_LEFT:
                direc = Direcao.ESQUERDA;
                this.x -= this.width;                
                break;
            case KeyEvent.VK_UP:
                if (posYat > 0) {
                    direc = Direcao.CIMA;
                    this.posYat--;
                    this.y = ondeCaminhar.get(posYat);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (posYat < ondeCaminhar.size() - 1) {
                    direc = Direcao.BAIXO;
                    this.posYat++;
                    this.y = ondeCaminhar.get(posYat);
                }
                break;
        }
        if (!passosDados.contains(this.y) && this.y < posIni.y) {
            score += 5;
            passosDados.add(this.y);
        }
        if (this.y <= 200) {
            reset();
            score += 100;
            passosDados.clear();
        }
    }
    
    public void perdeVida() {
        vidas--;
        reset();
    }
    
    public boolean taVivo() {
        return vidas >= 0;
    }
    
    private void reset() {
        posYat = ondeCaminhar.size() - 1;
        this.x = posXini;
        this.y = ondeCaminhar.get(posYat);
        this.direc = Direcao.CIMA;
    }
    
    public void addPasso(final int pos) {
        ondeCaminhar.add(pos);
        posYat = ondeCaminhar.size() - 1;
    }
    
}