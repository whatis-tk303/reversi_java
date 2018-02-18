/* @file	GameManager.java
 * @date	2018-02-18
 * @author	TK303
 */


import java.util.*;
import java.awt.*;


/*********************************************************************************
 * @brief	�v���C���[�Q�l
 */
class Players
{
	  public Player first;
	  public Player second;

	  /* constructor */
	  public Players()
	  {
	  }

	  /**/
	  public void setFirst(Player p)
	  {
		 first = p;
	  }

	  /**/
	  public void setSecond(Player p)
	  {
		 second = p;
	  }
}


/*********************************************************************************
 * @brief	�Q�[���i�s�Ǘ�
 */
public class GameManager
{
	  private ReversiBoard m_board;
	  private Players m_players;	/* �v���C���[�Q�l */

	  /* constructor */
	  public GameManager()
	  {
	  }


	  /**/
	  public void start(ReversiBoard board, Players players)
	  {
		 m_board = board;
		 m_players = players;
		 
		 /* �J�n���̋��u�� */
		 m_board.reset();
		 m_board.setPiece(3, 3, new ReversiPiece(ReversiPiece.Type.BLACK));
		 m_board.setPiece(4, 3, new ReversiPiece(ReversiPiece.Type.WHITE));
		 m_board.setPiece(3, 4, new ReversiPiece(ReversiPiece.Type.WHITE));
		 m_board.setPiece(4, 4, new ReversiPiece(ReversiPiece.Type.BLACK));

		 try
		 {
			int count = 0;
			while(true)
			{
			   Thread.sleep(1000);
			   count += 1;
			   System.out.printf("play count = %d\n", count);
			   
			   if (20 <= count)
			   {
				  break;
			   }
			}
		 }
		 catch(Exception e)
		 {
		 }
	  }
}
