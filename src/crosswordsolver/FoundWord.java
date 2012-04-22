/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crosswordsolver;

/**
 *
 * @author John
 */

import java.awt.Point;

public class FoundWord
{
    public String word;         //the word that was searched for.
    public int numMatched;      //the number of letters matched.  For later implementation.
    public Point start;         //the point on the puzzle that the word started.
    public Point direction;     //the direction that the word goes in.  Each value has a range of -1, 1.
    public String response;     //formatted string that is displayed to the user that describes the direction and starting location of the word.
    private boolean error;      //tripped if the word was not found.
    
    public FoundWord(String word, int numMatched, int[] searchRes)
    {
        this.word = word;
        this.numMatched = numMatched;
        
        //check to see if the word was even found
        if(searchRes[0] == -1)
        {
            error = true;
        }
        else
        {
            error = false;
            this.start = new Point(searchRes[0], searchRes[1]);
            this.direction = new Point(searchRes[2], searchRes[3]);
        }
    }
    
    public void generateResponseString()
    {
        if(!error)
            this.response = word + " is found first at (" + Integer.toString(start.x) + ", " + Integer.toString(start.y) + ") and goes " + constructDirectionString(new int[] {start.x, start.y, direction.x, direction.y});
        else
            this.response = word + " was not found in the given puzzle.";
    }
       
    /*takes in the position and direction
     * of the found string and outputs it
     * as english words using directions.
     */
    String constructDirectionString(int[] foo)
    {
        int i = 2;
            String result = "";
        boolean isDiag = ((foo[i] != 0) && (foo[i+1] != 0));
        if(isDiag)
            result += "diagonally ";
        if(foo[i] == -1)
            result += "left ";
        if(foo[i] == 1)
            result += "right ";
        if(isDiag)
            result += "and ";
        i++;
        if(foo[i] == -1)
            result += "up.";
        if(foo[i] == 1)
            result += "down.";
        return result;
    }
    
    /*
     * if the word was not found, the Point[] output will only contain one element, that element will contain (-1, -1).
     */
    public Point[] getStartAndEnd()
    {
        if(error)
            return new Point[] {new Point(-1, -1)};
        else
            return new Point[] {new Point(this.start.x, this.start.y), new Point(this.start.x+this.direction.x*this.word.length(), this.start.y+this.direction.y*this.word.length())};
    }
}
