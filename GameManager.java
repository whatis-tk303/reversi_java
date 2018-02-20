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
	  private Players m_players;		/* �v���C���[�Q�l */
	  private Player m_current_player;	/* ���݂̃v���C���[ */

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public GameManager()
	  {
		 /* do nothing */
	  }

	  /********************************************************************************
	   * @brief	���݂̃v���C���[���擾����
	   */
	  public Player getCurrentPlayer()
	  {
		 return m_current_player;
	  }

	  /********************************************************************************
	   * @brief	�Q�[�����J�n����
	   * @note	�P�Q�[�����I���܂ŕԂ��ė��Ȃ�
	   */
	  public void start(ReversiBoard board, Players players)
	  {
		 m_board = board;
		 m_players = players;
		 m_current_player = m_players.first;
		 
		 /* �J�n���̋��u�� */
		 m_board.reset();
		 m_board.setPiece(3, 3, new ReversiPiece(ReversiPiece.Type.BLACK));
		 m_board.setPiece(4, 3, new ReversiPiece(ReversiPiece.Type.WHITE));
		 m_board.setPiece(3, 4, new ReversiPiece(ReversiPiece.Type.WHITE));
		 m_board.setPiece(4, 4, new ReversiPiece(ReversiPiece.Type.BLACK));
		 m_board.repaint();
		 
		 try
		 {
			int count = 0;
			int count_fail = 0;	/* ��u���Ȃ������ꍇ���A�����������m�F����J�E���^ */

			System.out.println("starting this game.");
			while(true)
			{
			   Thread.sleep(1000);  /* for debug: */

			   /* ���݂̃v���C���[�����u���̂�҂��āA�v���C���[����シ�� */
			   System.out.println("- - - - - - - - - - - - - - - - - "); /* for debug: */
			   System.out.printf("%s's turn.\n", m_current_player); /* for debug: */
			   boolean success = waitPlaying(m_current_player);
			   if (success)
			   {
				  count_fail = 0;
			   }
			   else
			   { /* ���u���Ȃ������I */
				  count_fail += 1;
				  if (count_fail == 2)
				  { /* 2��A���ŋ��u���Ȃ����� �� 2�l�Ƃ����u���Ȃ��̂ŃQ�[���I�� */
					 break;
				  }
			   }

			   /* �U���� */
			   m_current_player = changePlayer();
			}
		 }
		 catch(Exception e)
		 {
			System.out.println(e);
		 }

		 /* ���̃Q�[�����I������ */
		 /* TODO: 20180218  ���̃Q�[�����I���������Ƃ������\������H */
		 System.out.println("ending this game.");
	  }

	  /********************************************************************************
	   * @brief	�v���C���[����シ��
	   */
	  private Player changePlayer()
	  {
		 return (m_current_player == m_players.first)
			? m_players.second
			: m_players.first;
	  }

	  /********************************************************************************
	   * @brief	���݂̃v���C���[�����u���i���邢�͒u���Ȃ����Ƃ��m�肷��j�܂ő҂�
	   */
	  private boolean waitPlaying(Player player)
	  {
		 /* TODO: 20180220  �Ђ�����Ԃ���ʒu�ƁA�Ђ�����Ԃ��鑊���̔z��̃}�b�v��
		                    �����Ŏ擾���Ă��� think()�ɓn�� */
		 HashMap<Point, Vector<Point>> candidate_pos_map;
		 candidate_pos_map = m_board.getCandidatePos(player.getPieceType());

/* #if 1 : for debug: 20180220  �Ђ�����Ԃ���������Ă݂� */
		 System.out.println(candidate_pos_map);
/* #endif : for debug: 20180220  �Ђ�����Ԃ���������Ă݂� */

		 /* ���݂̃v���C���[�����u���ʒu���l���� */
		 /* TODO: 20180220  think()�̓X���b�h�Ŏ��s���A�I������܂ő҂� */
		 
		 Point pos = player.think(candidate_pos_map);
		 
		 if (pos != null)
		 { /* �w�肳�ꂽ�ʒu�Ɏ����̋��u�� */
			ReversiPiece piece = new ReversiPiece(player.getPieceType());
			m_board.setPiece(pos.x, pos.y, piece);
			m_board.repaint();

			/* ����̋���Ђ�����Ԃ� */
			Vector<Point> pos_turn = candidate_pos_map.get(pos);
			/* TODO: 20180219  �����ŋ���Ђ�����Ԃ��i�A�j���[�V���������s����H�j */
			return true;	/* ��u���� */
		 }
		 else
		 {
			return false;	/* ��u���Ȃ������I */
		 }
	  }
}
