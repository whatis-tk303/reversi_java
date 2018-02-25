/* @file	GameManager.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class GameManager : �Q�[���̐i�s���Ǘ�����
 *	���Ӗ���
 *	  - �Q�[���̐i�s���Ǘ�����
 *			(constructor)	
 *			getCurrentPlayer	���݂̃v���C���[���擾����
 *			start				�Q�[�����J�n����
 *			changePlayer		�v���C���[����シ��
 *			waitPlaying			���݂̃v���C���[�����u���i���邢�͒u���Ȃ����Ƃ��m�肷��j�܂ő҂�
 *
 *	class Players : �Q�[���v���C���[�i�Q�l�j��ێ�����
 *	���Ӗ���
 *	  - �Q�[���v���C���[�i�Q�l�j��ێ�����
 *			(constructor)	
 *			setFirst		���v���C���[���Z�b�g����
 *			setSecond		���v���C���[���Z�b�g����
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
	  private StatusNotifier m_status_notifier;		/* �Q�[���X�e�[�^�X��ʒm���� */

	  /********************************************************************************
	   * @brief	constructor
	   * @param [in]	obs - �Q�[���i�s���Ď�����I�u�W�F�N�g
	   */
	  public GameManager(Observer obs)
	  {
		 m_status_notifier = new StatusNotifier();
		 m_status_notifier.addObserver(obs);
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
			int count_fail = 0;	/* ��u���Ȃ������ꍇ���A�����������m�F����J�E���^ */

			System.out.println("starting this game.");
			while(true)
			{
			   Thread.sleep(500);  /* for debug: */

			   /* ���݂̃Q�[���X�e�[�^�X��ʒm���� */
			   m_status_notifier.notify(m_board);

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
		 /* �Q�[���I�����̃Q�[���X�e�[�^�X��ʒm���� */
		 m_status_notifier.notify(m_board);
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

	  private Point m_pos_decided;
	  private void setDecidedPos(Point pos) { m_pos_decided = pos; }
	  private Point getDecidedPos() { return m_pos_decided; }

	  /********************************************************************************
	   * @brief	���݂̃v���C���[�����u���i���邢�͒u���Ȃ����Ƃ��m�肷��j�܂ő҂�
	   */
	  private boolean waitPlaying(final Player player)
	  {
		 /* �Ђ�����Ԃ���ʒu�ƁA�Ђ�����Ԃ��鑊���̔z��̃}�b�v��������
		  * �擾���Ă��� think()�ɓn�� */
		 final HashMap<Point, Vector<Point>> candidate_pos_map;
		 candidate_pos_map = m_board.getCandidatePos(player.getPieceType());

		 System.out.println(candidate_pos_map); /* for debug: 20180220  �Ђ�����Ԃ����� */

		 /* ���݂̃v���C���[�����u���ʒu���l���� */
		 System.out.println("begining to think " + player + " ...");  /* for debug: 20180221 */
		 Thread thread = new Thread(new Runnable(){
				  @Override
				  public void run() {
					 Point pos = player.think(candidate_pos_map);
					 setDecidedPos(pos);
				  }
			});
		 try
		 {
			thread.start();
			thread.join();
		 } catch(Exception e) { System.out.println(e); }
		 System.out.println("done.");  /* for debug: 20180221 */
		 
		 Point pos = getDecidedPos();
		 if (pos != null)
		 { /* �w�肳�ꂽ�ʒu�Ɏ����̋��u�� */
			ReversiPiece piece = new ReversiPiece(player.getPieceType());
			m_board.setPiece(pos.x, pos.y, piece);
			m_board.repaint();

			/* ����̋���Ђ�����Ԃ� */
			Vector<Point> pos_turn_pieces = candidate_pos_map.get(pos);
			/* TODO: 20180219  �����ŋ���Ђ�����Ԃ��i�A�j���[�V���������s����H�j */
			for (Point pos_turn : pos_turn_pieces)
			{
			   m_board.setPiece(pos_turn.x, pos_turn.y, new ReversiPiece(player.getPieceType()));
			   m_board.repaint();
			   try
			   {
				  Thread.sleep(200);
			   } catch(Exception e) {}
			}

			return true;	/* ��u���� */
		 }
		 else
		 {
			return false;	/* ��u���Ȃ������I */
		 }
	  }
}
