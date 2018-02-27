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

import java.awt.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	$B%"%W%j%1!<%7%g%sK\BN(B
 */
class Application extends JFrame implements Runnable
{
	  private StatusPanel m_status_panel;
	  private ReversiBoard m_board;
	  private GameManager m_game;
	  private Players m_players;


	  /********************************************************************************
	   * @brief		constructor
	   */
	  public Application(String title)
	  {
		 super(title);
	  }

	  /********************************************************************************
	   * @brief		$B=`Hw!'@h<j!"8e<j$r7h$a$k(B
	   * @param [out]	$B7hDj$7$?%W%l%$%d!<!J@h<j!&8e<j!K$r@_Dj$7$FJV$9(B
	   */
	  private void prepare(Players players)
	  {
		 /* $B@h<j!&8e<j$r7h$a$k%@%$%"%m%0(B */
		 Object[] options = {
			"Human(A) vs Human(B)",
			"Human vs Computer",
			"Computer vs Human",
			"Computer(A) vs Computer(B)",
		 };

		 int ret = JOptionPane.showOptionDialog(
			this,
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
			   players.first  = new HumanPlayer(m_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(m_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 1: /* "Human vs Computer" */
			   players.first  = new HumanPlayer(m_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(m_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
			case 2: /* "Computer vs Human" */
			   players.first  = new AutoPlayer(m_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(m_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 3: /* "Computer(A) vs Computer(B)" */
			   players.first  = new AutoPlayer(m_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(m_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
		 }

		 System.out.printf("ret = %d\n", ret);
	  }

	  /********************************************************************************
	   * @brief		$B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B
	   * @return	true:$B:FEY%2!<%`$r3+;O$9$k!"(Bfalse:$B%2!<%`$r=*N;$9$k(B
	   */
	  private boolean showResult(ReversiBoard board, Players players)
	  {
		 /* $B:FEY%2!<%`$r3+;O$9$k!"%"%W%j$r=*N;$9$k$N$rA*Br$9$k%@%$%"%m%0(B */
		 Object[] options = {"play next game", "exit game"};

		 int ret = JOptionPane.showOptionDialog(
			this,
			"are you continue game ?",
			"play result:",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);

		 return (ret == 0);  /* 0:$B:FEY%2!<%`$r3+;O$9$k(B */
	  }

	  /*********************************************************************************
	   * @brief		$B%2!<%`$r=i4|2=$9$k(B
	   */
	  public void init()
	  {
		 m_board = new ReversiBoard();
		 m_status_panel = new StatusPanel();
		 m_game = new GameManager(m_status_panel);
		 m_players = new Players();

		 add(m_status_panel, BorderLayout.NORTH);
		 add(m_board, BorderLayout.CENTER);
		 pack();
	  }

	  /*********************************************************************************
	   * @brief		$B%2!<%`%k!<%W(B
	   */
	  @Override  /* Runnable */
	  public void run()
	  {
		 while(true)
		 {
			/* $B=`Hw!'@h<j!"8e<j$r7h$a$k(B */
			prepare(m_players);
			System.out.println(m_players.first);
			System.out.println(m_players.second);

			/* $B%2!<%`3+;O(B */
			m_game.start(m_board, m_players);

			/* $B7k2LI=<(!'(B $B>!GT$rI=<($9$k(B */
			if (!showResult(m_board, m_players))
			{
			   break;  /* $B%2!<%`$r=*N;$9$k(B */
			}
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
		 Application app = new Application("Reversi");
		 app.setLocationByPlatform(true);
		 app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 app.init();
		 app.setVisible(true);

		 Thread thread = new Thread(app);
		 try
		 {
			thread.start();
			thread.join();
		 }
		 catch(Exception e)
		 {
			System.out.println(e);
		 }

		 System.exit(0);
	  }
}
