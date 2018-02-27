/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class Reversi : $B%j%P!<%7%2!<%`$N%"%W%j%1!<%7%g%s(B
 *	$B!c@UL3!d(B
 *	  - $B%j%P!<%7%2!<%`$NK\BN(B
 *			prepare			$B=`Hw!'@h<j!"8e<j$r7h$a$k(B
 *			showResult		$B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B
 *			main			$B%2!<%`$r?J9T$9$k(B
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	$B%"%W%j%1!<%7%g%sK\BN(B
 */
class Application implements Runnable
{
	  private static JFrame s_frame;
	  private static StatusPanel s_status_panel;
	  private static ReversiBoard s_board;
	  private static GameManager s_game;
	  private static Players s_players;


	  /********************************************************************************
	   * @brief		$B=`Hw!'@h<j!"8e<j$r7h$a$k(B
	   * @param [out]	$B7hDj$7$?%W%l%$%d!<!J@h<j!&8e<j!K$r@_Dj$7$FJV$9(B
	   */
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
			   players.first  = new HumanPlayer(s_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(s_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 1: /* "Human vs Computer" */
			   players.first  = new HumanPlayer(s_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(s_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
			case 2: /* "Computer vs Human" */
			   players.first  = new AutoPlayer(s_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(s_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 3: /* "Computer(A) vs Computer(B)" */
			   players.first  = new AutoPlayer(s_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(s_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
		 }

		 System.out.printf("ret = %d\n", ret);
	  }

	  /********************************************************************************
	   * @brief		$B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B
	   */
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

	  /*********************************************************************************
	   * @brief		$B!)!)!)(B
	   */
	  @Override  /* Runnable */
	  public void run()
	  {
		 s_frame = new JFrame("Reversi");
		 s_board = new ReversiBoard();
		 s_status_panel = new StatusPanel();
		 s_game = new GameManager(s_status_panel);
		 s_players = new Players();

		 s_frame.setLocationByPlatform(true);
		 s_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 s_frame.add(s_status_panel, BorderLayout.NORTH);
		 s_frame.add(s_board, BorderLayout.CENTER);
		 s_frame.pack();
		 s_frame.setVisible(true);

		 while(true)
		 {
			/* $B=`Hw!'@h<j!"8e<j$r7h$a$k(B */
			prepare(s_players);
			System.out.println(s_players.first);
			System.out.println(s_players.second);

			/* $B%2!<%`3+;O(B */
			s_game.start(s_board, s_players);

			/* $B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B */
			showResult(s_board, s_players);
		 }
	  }
}


/*********************************************************************************
 * @brief	$B%a%$%s%/%i%9(B
 */
public class Reversi
{

	  /********************************************************************************
	   * @brief		$B%"%W%j%1!<%7%g%s!&%a%$%s(B
	   */
	  public static void main(String[] args)
	  {
		 Thread thread = new Thread(new Application());
		 thread.start();
	  }
}
