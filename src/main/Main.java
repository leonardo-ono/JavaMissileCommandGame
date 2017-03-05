package main;

import br.ol.mc.MissileCommandGame;
import br.ol.mc.infra.Display;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * Main class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MissileCommandGame game = new MissileCommandGame();
                Display view = new Display(game);
                JFrame frame = new JFrame();
                frame.setTitle("Java Missile Command");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(view);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
                view.requestFocus();
                view.start();
            }

        });
    }
    
}
