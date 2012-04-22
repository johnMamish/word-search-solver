/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crosswordsolver;

/**
 *
 * @author John
 */

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*features to add
 * -whitespace in puzzle for readability
 * -graphic overlay
 * 
 */

/*bugs
 * 
 */

/*user stupidblock
 * -whitespace management
 *    -remove ALL whitespace
 * 
 * -
 */

public class CrosswordWindow extends JFrame
{
    private ArrayList<String> puzzle;       //crossword puzzle
    private ArrayList<String> words;        //words that the user wants to find
    private ArrayList<FoundWord> foundWords;    //information about the location of each word the user wants to search for
    
    //constants for formatting purposes
    private final Insets stdInsets = new Insets(10, 10, 10, 10);
    private final Insets smallInsets = new Insets(0, 0, 0, 0);
    private final Font stdFont = new Font("Lucida Console", 0, 12);
    
    //self-explanitory
    private JButton solveButton;
    private JTextArea userCrossword;
    private JTextArea userWords;
    private JTextArea outputWords;
    private GridBagLayout layout;
    
    public CrosswordWindow()
    {
        puzzle = new ArrayList<String>();
        words = new ArrayList<String>();
        foundWords = new ArrayList<FoundWord>();
        
        layout = new GridBagLayout();
        this.setLayout(layout);

        //next few lines initialize and place 2 labels on the JFrame
        JLabel puzzleInLabel = new JLabel("puzzle: ");
        GridBagConstraints puzzleInLabelConstraints = new GridBagConstraints();
        puzzleInLabelConstraints.anchor = GridBagConstraints.WEST;
        puzzleInLabelConstraints.gridx = 0;
        puzzleInLabelConstraints.gridy = 0;
        puzzleInLabelConstraints.insets = smallInsets;
        layout.setConstraints(puzzleInLabel, puzzleInLabelConstraints);
        this.add(puzzleInLabel);
        
        JLabel wordsInLabel = new JLabel("search terms: ");
        GridBagConstraints wordsInLabelConstraints = new GridBagConstraints();
        wordsInLabelConstraints.anchor = GridBagConstraints.WEST;
        wordsInLabelConstraints.gridx = 1;
        wordsInLabelConstraints.gridy = 0;
        wordsInLabelConstraints.insets = smallInsets;
        layout.setConstraints(wordsInLabel, wordsInLabelConstraints);
        this.add(wordsInLabel);

        //creates, formats, and places crossword input
        userCrossword = new JTextArea(20, 50);
        userCrossword.setFont(stdFont);
        GridBagConstraints userCrosswordConstraints = new GridBagConstraints();
        userCrosswordConstraints.insets = stdInsets;
        userCrosswordConstraints.fill = GridBagConstraints.BOTH;
        userCrosswordConstraints.gridx = 0;
        userCrosswordConstraints.gridy = 1;
        userCrosswordConstraints.weightx = 1;
        userCrosswordConstraints.weighty = 1;
        layout.setConstraints(userCrossword, userCrosswordConstraints);
        this.add(userCrossword);
        
        class CrosswordCorrector implements FocusListener
        {

            public void focusGained(FocusEvent e)
            {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void focusLost(FocusEvent e)
            {
                update();
            }
            
            private void update()
            {
                //format the crossword puzzle
                //remove all the gross things
                String temp = userCrossword.getText();
                temp = temp.replace('\t', ' ');
                temp = temp.replaceAll(" ", "");
                
                //place even spaces
                for(int i = 1;i < temp.length();i += 2)
                {
                    if(temp.charAt(i) != '\n')
                        temp = temp.substring(0, i) + " " + temp.substring(i);
                }
                
                userCrossword.setText(temp);
            }           
        }
                
        userCrossword.addFocusListener(new CrosswordCorrector());
        
        //adds scrollbars to the crossword input
        JScrollPane userCrosswordScroll = new JScrollPane(userCrossword);
        layout.setConstraints(userCrosswordScroll, userCrosswordConstraints);
        add(userCrosswordScroll, userCrosswordConstraints);
        
        //do I even have to explain this??  No?  good.
        userWords = new JTextArea(20, 20);
        userWords.setFont(stdFont);
        GridBagConstraints userWordsConstraints = new GridBagConstraints();
        userWordsConstraints.fill = GridBagConstraints.VERTICAL;
        userWordsConstraints.weightx = 0;
        userWordsConstraints.weighty = 1;
        userWordsConstraints.gridx = 1;
        userWordsConstraints.gridy = 1;
        userWordsConstraints.insets = stdInsets;
        layout.setConstraints(userWords, userWordsConstraints);
        this.add(userWords);
        
        userWordsConstraints.weightx = 1;
        userWordsConstraints.fill = GridBagConstraints.BOTH;
        JScrollPane userWordsScroll = new JScrollPane(userWords);
        layout.setConstraints(userWordsScroll, userWordsConstraints);
        add(userWordsScroll, userWordsConstraints);
        
        solveButton = new JButton("solve");
        class solveButtonListener implements ActionListener
        {            
            /*
             * takes the inputs from the crossword puzzle input and the 
             * user word list input and puts them in their respective variables
             * after trimming the whitespace and tabs off.
             */
            private void readInputs()
            {
                puzzle.clear();
                
                //set up all the variables
                String readBuffer = "";
                String text = userCrossword.getText();
                
                //we use a dummy variable for the search and replace so we dont disturb the actual text in the textboxes
                text = text.replace('\t', ' ');
                text = text.replaceAll(" ", "");
                for(int i = 0;i < text.length();i++)
                {
                    //at each newline, add the readBuffer string to the puzzle arraylist and reset readBuffer
                    if(text.charAt(i) == '\n')
                    {
                        if(readBuffer.length() > 0)
                            puzzle.add(readBuffer);
                        readBuffer = "";
                    }
                    //there wasn't a newline, so just add the next char to readBuffer
                    else
                        readBuffer += text.charAt(i);
                }
                //add the last thing held in readBuffer onto the puzzle
                puzzle.add(readBuffer);
                
                //same process, but for the search word list
                words.clear();
                readBuffer = "";
                text = userWords.getText();
                text = text.replace('\t', ' ');
                text = text.replaceAll(" ", "");
                for(int i = 0;i < text.length();i++)
                {
                    if(text.charAt(i) == '\n')
                    {
                        if(readBuffer.length() > 0)
                            words.add(readBuffer);
                        readBuffer = "";
                    }
                    else
                        readBuffer += text.charAt(i);
                }
                if(readBuffer.length() > 0)
                    words.add(readBuffer);
            }
            
            /*
             * populates the foundWords arraylist with all the words the user specifies
             * and the information about where they can be found.
             */
            private void generateSolutions()
            {
                //for each word in words (the arraylist of words to search for specified by the user), search the arraylist and add the corresponding info to the foundWords arraylist
                int[] resultHolder;
                foundWords.clear();
                for(int i = 0;i < words.size();i++)
                {
                    foundWords.addAll(searchPuzzle(puzzle, words.get(i)));
                    foundWords.get(i).generateResponseString();
                }
            }
            
            /*
             * just dumps all of the response strings in foundWords into the output textbox.
             */
            private void displaySolutions()
            {
                outputWords.setText("");
                for(FoundWord s:foundWords)
                {
                    outputWords.setText(outputWords.getText() + s.response + "\n");
                }
            }
            
            public void actionPerformed(ActionEvent e)
            {                
                //do all de other stuff
                readInputs();
                generateSolutions();
                displaySolutions();
            }
        }
        
        //blah blah blah blah blah.
        //gridbag gridbag gridbag gridbag.
        solveButton.addActionListener(new solveButtonListener());
        GridBagConstraints solveButtonConstraints = new GridBagConstraints();
        solveButtonConstraints.anchor = GridBagConstraints.LAST_LINE_START;
        solveButtonConstraints.insets = stdInsets;
        solveButtonConstraints.gridx = 2;
        solveButtonConstraints.gridy = 1;
        solveButtonConstraints.weightx = 0;
        layout.setConstraints(solveButton, solveButtonConstraints);
        this.add(solveButton);
        
        outputWords = new JTextArea(20, 90);
        outputWords.setEditable(false);
        outputWords.setFont(stdFont);
        GridBagConstraints outputWordsConstraints = new GridBagConstraints();
        outputWordsConstraints.fill = GridBagConstraints.BOTH;
        outputWordsConstraints.weightx = 1;
        outputWordsConstraints.weighty = 0;
        outputWordsConstraints.gridy = 2;
        outputWordsConstraints.gridx = 0;
        outputWordsConstraints.gridwidth = 2;
        outputWordsConstraints.insets = stdInsets;
        layout.setConstraints(outputWords, outputWordsConstraints);
        this.add(outputWords);
        
        outputWordsConstraints.weighty = 1;
        JScrollPane outputWordsScroll = new JScrollPane(outputWords);
        layout.setConstraints(outputWordsScroll, outputWordsConstraints);
        add(outputWordsScroll, outputWordsConstraints);
        
    }
    
    /*
     * different way of comparing strings.  Returns the number of letters that
     * are not the same.
     */
    public int differenceCompare(String a, String b)
    {
        int count = 0;
        for(int i = 0;(i < a.length()) && (i < b.length());i++)
        {
            if(a.charAt(i) == b.charAt(i))
                count++;
        }
        return count;
    }
    /*
     * returns a word from an arbitrary location going in an arbritrary direction
     * of the puzzle arraylist.  Think of puzzle as a 2-d char array.
     * 
     * If the word would go out of bounds, returns an empty string
     */
    public String pullOutWord(ArrayList<String> puzzle, int xStart, int yStart, int xDir, int yDir, int length)
    {
        int puzzHeight = puzzle.size();
        int puzzWidth = puzzle.get(0).length();
        for(String s:puzzle)
        {
            if(puzzWidth < s.length())
                puzzWidth = s.length();
        }
        
        int xBound = xStart+(xDir*(length-1));
    int yBound = yStart+(yDir*(length-1));

    if((xDir == 0) && (yDir == 0))
            return "";

    if(((xBound < 0) || (xBound >= puzzWidth)) || ((yBound < 0) || (yBound >= puzzHeight)))
            return "";
        
        String result = "";
        for(int i = 0;i < length;i++)
        {
            result += puzzle.get(yStart).charAt(xStart);
            xStart += xDir;
            yStart += yDir;
        }
        return result;
    }
    
    /*
     * finds the location and direction of search in puzzle, treating puzzle like
     * a 2-d char array.  It returns the starting point in the form
     *      {start.x, start.y, xDir, yDir};
     * 
     * if the word cannot be found, an array with -1 as the first value is returned.
     */
    public ArrayList<FoundWord> searchPuzzle(ArrayList<String> puzzle, String search)
    {
        ArrayList<FoundWord> possibleWords = new ArrayList<FoundWord>();
        int maxMatch = search.length()/2;
        String testWord;
        for(int i = 0;i < puzzle.size();i++)
        {
            for(int iInner = 0;iInner < puzzle.get(i).length();iInner++)
            {
                for(int x = -1;x <= 1;x++)
                {
                    for(int y = -1;y <= 1;y++)
                    {
                        testWord = pullOutWord(puzzle, iInner, i, x, y, search.length());
                        if(differenceCompare(search, testWord) > maxMatch)
                        {
                            possibleWords.clear();
                            possibleWords.add(new FoundWord(search, 0, new int[] {iInner, i, x, y}));
                        }
                    }
                }
            }
        }
    if(possibleWords.size() == 0)
        possibleWords.add(new FoundWord(search, 0, new int[] {-1, -1, -1, -1}));
    return possibleWords;
    }
}