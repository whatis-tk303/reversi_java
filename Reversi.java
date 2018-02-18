/* @file	reversi.java
 * @date	2018-02-18
 * @author	TK303
 */

import java.awt.*;
import javax.swing.*;



/**********************************************************************************/
class ReversiBoard extends JFrame
{
	  public ReversiBoard()
	  {
		 Dimension size = new Dimension(400, 400);
		 setSize(size);
	  }
}


/**********************************************************************************/
class Reversi
{
	  public static void main(String[] args)
	  {
		 System.out.println("Reversi");
		 ReversiBoard board = new ReversiBoard();
		 board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 board.setVisible(true);
	  }
}
