/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo2D;

//import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Vanessa
 */
public class FrmJogo extends javax.swing.JFrame implements Runnable, KeyListener{
    
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean emJogo = true;
    private boolean venceu = false;
    private boolean keyRestart;
    private boolean tiro;
    private long ultimoTiro;
    private long ultimoInimigo;
    private int contador = 25;
    private int nivel = 1;
    private int i = 0;
    private Nave player;
    private Image background;

    /**
     * Creates new form FrmJogo
     */
    public FrmJogo() {
        initComponents();
        
        createBufferStrategy(2);
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

       
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmJogo().setVisible(true);
            }
        });
    }

    @Override
    public void run() {
        
        Graphics g;
        addKeyListener(this);
                
        ArrayList<Base> lista = new ArrayList();
        ArrayList<Base> lixo = new ArrayList();
        
        
        ImageIcon ref = new ImageIcon(FrmJogo.class.getResource("/img/fundo.jpg"));
                         
        player = new Nave();
        player.setIncY(0);
        player.setIncX(0);
        lista.add(player); 
                        
                                    
        while(true){
            g = getBufferStrategy().getDrawGraphics();
            
            
            if(emJogo){
                //desenha a imagem de fundo
                background = ref.getImage();
                g.drawImage(background, 0, 0, null);

                //escreve informaçoes do jogo
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 15));
                g.drawString("Nível " + nivel, 10, 50);
                g.drawString("Inimigos a capturar: " + contador, 10, 70);

                //desenha a nave
                g.drawImage(player.getImg(), player.getX(), player.getY(), this);


                //colisao player com inimigo
                for(Base b : lista){
                    if(b.getX() == player.getX() + player.getLargura()){
                        emJogo = false;
                        nivel = 1;
                    }
                }
                               
                //colisao tiro com inimigo
                for(Base b1 : lista){
                    for(Base b2 : lista){
                        if(b1.getX() + b1.getLargura() == b2.getX()) {
                            lixo.add(b1);
                            lixo.add(b2);
                            contador--;
                        }
                    } 
                }                                                           
                                
                for (Base b : lista){
                    b.mover();
                }
                
                for (Base b : lista){
                    if(!b.visible){
                        lixo.add(b);
                    }
                }
                
                for (Base b : lista){
                    g.drawImage(b.getImg(), b.getX(), b.getY(), null);
                }
                
                lista.removeAll(lixo);
                lixo.clear();
                                                                                
                if(left){
                    player.setIncX(-1);
                } else if (right){
                    player.setIncX(1);
                } else if (up){
                    player.setIncY(-2);
                } else if (down){
                    player.setIncY(2);
                } else {
                    player.setIncX(0);
                    player.setIncY(0);
                }

                //cria tiros
                long tempo_tiro = System.currentTimeMillis();
                if(tiro && tempo_tiro > ultimoTiro + 200){
                    ultimoTiro = tempo_tiro;
                    Tiro t = new Tiro();
                    t.setX(player.getX()+ player.getLargura() + 1);
                    t.setY(player.getY() + ((player.getAltura()/2) - 7));
                    lista.add(t);
                }
                
                if (nivel == 1) {
                    long tempo_inimigo = System.currentTimeMillis();
                    Random r = new Random();
                    //cria todos os inimigos
                    if(i < 25 && tempo_inimigo > ultimoInimigo + 1500){
                        ultimoInimigo = tempo_inimigo;
                        Inimigo in = new Inimigo(nivel);
                        in.setY(r.nextInt(getHeight() - getHeight()/2));
                        lista.add(in);
                        i++;
                    } 
                    
                    if(contador == 0){
                        nivel = 2;
                        contador = 15;
                        i = 0;
                        lista.clear();
                        lista.add(player);
                    }
                } else {
                    long tempo_inimigo = System.currentTimeMillis();
                    Random r = new Random();
                    //cria todos os inimigos
                    if(i < 15 && tempo_inimigo > ultimoInimigo + 1000){
                        ultimoInimigo = tempo_inimigo;
                        Inimigo in = new Inimigo(nivel);
                        in.setY(r.nextInt(getHeight() - getHeight()/2));
                        lista.add(in);
                        i++;
                    } 
                    if(contador == 0){
                        emJogo = false;
                        venceu = true;
                        nivel = 1;
                        contador = 25;
                        i = 0;
                    }
                }
                
            } else if(venceu){
                ImageIcon ref3 = new ImageIcon(FrmJogo.class.getResource("/img/venceu.jpg"));
                background = ref3.getImage();
                g.drawImage(background, 0, 0, null);
                
            } else {
                ImageIcon ref2 = new ImageIcon(FrmJogo.class.getResource("/img/game_over.jpg"));
                background = ref2.getImage();
                g.drawImage(background, 0, 0, null);
                               
            }
           
            if(keyRestart){
                contador = 25;
                i = 0;
                emJogo = true;
                lista.clear();
                lista.add(player);
                player.setX(1);
            }
                    
            g.dispose();
            getBufferStrategy().show();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
                           
    }
        
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            keyRestart = true;
        } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            tiro = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            keyRestart = false;
        } else if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            tiro = false;
        }
    }
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
