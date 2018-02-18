/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	$B$3$N%"%W%j%1!<%7%g%s$N%&%#%s%I%&(B
 */
class GameFrame extends JFrame
{
	  public GameFrame(String title)
	  {  
		 super(title);
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
}


/*********************************************************************************
 * @brief	$B%"%W%j%1!<%7%g%sK\BN(B
 */
class Reversi
{
	  private static JFrame s_frame;
	  private static ReversiBoard s_board;
	  private static GameManager s_game;
	  private static Players s_players;

	  /* $B=`Hw!'@h<j!"8e<j$r7h$a$k(B */
	  private static void prepare(Players players)
	  {
		 Object[] options = {
			"Human(A) vs Human(B)",
			"Human vs Computer",
			"Computer vs Human",
			"Computer(A) vs Computer(B)",
		 };

		 int ret = JOptionPane.showOptionDialog(
			s_frame,
			"???",
			"select play type:",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);

		 switch(ret)
		 {
			case 0: /* "Human(A) vs Human(B)" */
			   players.first  = new HumanPlayer(s_board, "Human(A)", ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(s_board, "Human(B)", ReversiPiece.Type.WHITE);
			   break;
			case 1: /* "Human vs Computer" */
			   players.first  = new HumanPlayer(s_board, "Human",    ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(s_board,  "Computer", ReversiPiece.Type.WHITE);
			   break;
			case 2: /* "Computer vs Human" */
			   players.first  = new AutoPlayer(s_board,  "Computer", ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(s_board, "Human",    ReversiPiece.Type.WHITE);
			   break;
			case 3: /* "Computer(A) vs Computer(B)" */
			   players.first  = new AutoPlayer(s_board,  "Computer", ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(s_board,  "Computer", ReversiPiece.Type.WHITE);
			   break;
		 }

		 System.out.printf("ret = %d\n", ret);
	  }

	  /* $B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B */
	  private static void showResult(ReversiBoard board, Players players)
	  {
		 Object[] options = {"play next game", "exit game"};
		 int ret = JOptionPane.showOptionDialog(
			s_frame,
			"are you continue game ?",
			"play result:",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);

		 if (ret == 1)
		 { /* "exit game" $B$,A*Br$5$l$?(B $B"*(B $B$3$N%"%W%j%1!<%7%g%s$r=*N;$9$k(B */
			System.exit(0);
		 }
	  }

	  /* $B%"%W%j%1!<%7%g%s!&%a%$%s(B*/
	  public static void main(String[] args)
	  {
		 s_frame = new GameFrame("Reversi");
		 s_board = new ReversiBoard();
		 s_game = new GameManager();
		 s_players = new Players();

		 s_frame.add(s_board);
		 s_frame.pack();
		 s_frame.setLocationByPlatform(true);
		 s_frame.setVisible(true);

		 while(true)
		 {
			/* $B=`Hw!'@h<j!"8e<j$r7h$a$k(B */
			prepare(s_players);
			System.out.println(s_players.first);
			System.out.println(s_players.second);
			//System.exit(1);

			/* $B%2!<%`3+;O(B */
			s_game.start(s_board, s_players);

			/* $B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B */
			showResult(s_board, s_players);
		 }
	  }
}
