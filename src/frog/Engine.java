package frog;

import java.io.File;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.IOException;
import java.awt.BorderLayout;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Engine extends JPanel implements Runnable, KeyListener {
    
    private Sapo player;
    private Carro [][]carros;
    private Carro []caminhoes;
    private BufferedImage fundo;
    
    private final int quantCarros = 6, quantLin = 4;
    private final int tamL = 30, tamA = 20;
    
    public Engine(String Titulo, int largura, int altura) throws IOException {
        Random rand = new Random();
        ArrayList<Carro> carSpawn = new ArrayList<>();
        this.fundo = ImageIO.read(new File("Imagens/fundo.png"));
        JFrame janela = new JFrame(Titulo);
        player = new Sapo("Imagens/frog.png", tamL, tamA, largura / 2, 365);
        carros = new Carro[quantLin][quantCarros];
        caminhoes = new Carro[quantCarros];
        final int dirCam = rand.nextInt(2) - 1;
        
        player.addPasso(200);
        for (int i = 0; i < caminhoes.length; i++)
            caminhoes[i] = new Carro(i * 150, 230, tamL * 2, tamA, dirCam, true);
        player.addPasso(230);
        for (int i = 0; i < carros.length; i++) {
            int dir = rand.nextInt(2) - 1;
            player.addPasso(26 * (i + 10));
            for (int j = 0; j < carros[i].length; j++) {
                boolean sair;    
                do {
                    sair = true;
                    int distX = rand.nextInt(20 * quantCarros);
                    carros[i][j] = new Carro(j * (tamL + distX), 26 * (i + 10), tamL, tamA, dir, false);
                    for (Carro it : carSpawn) {
                        if (it.intersects(carros[i][j]))
                            sair = false;
                    }
                } while (!carSpawn.isEmpty() && !sair);
                carSpawn.add(carros[i][j]);
            }
            carSpawn.clear();
        }
        player.addPasso(player.getLocation().y);
        
        janela.setSize(new Dimension(largura, altura));
        janela.setResizable(false);
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new BorderLayout());
        janela.add(this);
        janela.addKeyListener(this);
        janela.setVisible(true);
        new Thread(this).run();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(fundo, 0, 0, this.getBounds().width, this.getBounds().height, this);
        player.draw(g, this);
        for (int i = 0; i < carros.length; i++) {
            for (int j = 0; j < carros[i].length; j++)
                carros[i][j].draw(g, this);
        }
        for (int i = 0; i < caminhoes.length; i++)
            caminhoes[i].draw(g, this);
    }
    
    private void update() {
        for (int i = 0; i < carros.length; i++) {
            for (int j = 0; j < carros[i].length; j++) {
                carros[i][j].mover();
                if (player.intersects(carros[i][j])) 
                    player.perdeVida();
            }
        }
        for (int i = 0; i < caminhoes.length; i++) {
            caminhoes[i].mover();
            caminhoes[i].mover();
            if (player.intersects(caminhoes[i])) 
                player.perdeVida();
        }
    }
    
    public void run() {
        while (player.taVivo()) {
            try {
                Thread.sleep(20);
                update();
                repaint();
            } catch (Exception ex) {
                System.err.println("Ocorreu algum erro durante a execução do Jogo");
            }
        }
    }
    
    public void keyPressed(KeyEvent ke) {
        int tecla = ke.getKeyCode();
        if (tecla >= KeyEvent.VK_LEFT && tecla <= KeyEvent.VK_DOWN)
            player.mover(tecla);
    }
    
    public void keyReleased(KeyEvent ke) {}
    
    public void keyTyped(KeyEvent ke) {}
    
    public static void main(String[] args) {
        try {
            new Engine("Frogger", 640, 480).run();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro: " + e.toString(), "ERRO", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}