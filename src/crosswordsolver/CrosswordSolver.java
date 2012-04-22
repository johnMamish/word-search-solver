/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crosswordsolver;

import javax.swing.JFrame;

/**
 *
 * @author John
 */
public class CrosswordSolver
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        CrosswordWindow window = new CrosswordWindow();
        window.setTitle("Mamish crossword puzzle solvah");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }
}
