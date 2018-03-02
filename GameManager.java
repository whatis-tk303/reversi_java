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
	  public Players() {}

	  /**/
	  public void setFirst(Player p) { first = p; }

	  /**/
	  public void setSecond(Player p) { second = p; }
}


/*********************************************************************************
 * @brief	�Q�[���i�s�Ǘ�
 */
public class GameManager
{
	  private ReversiBoard m_board;				/** �Ֆ�                       */
	  private Players m_players;				/** �v���C���[�Q�l             */
	  private Player m_current_player;			/** ���݂̃v���C���[           */
	  private StatusNotifier m_status_notifier;	/** �Q�[���X�e�[�^�X��ʒm���� */

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
		 
		 /* �Q�[���̃��[�v */
		 doLoop();

		 /* ���̃Q�[�����I������ */
		 /* TODO: 20180218  ���̃Q�[�����I���������Ƃ������\������H */
		 /* �Q�[���I�����̃Q�[���X�e�[�^�X��ʒm���� */
		 m_status_notifier.notify(m_board);
		 System.out.println("ending this game.");
	  }

	  /********************************************************************************
	   * @brief		�Q�[���̃��[�v
	   */
	  public void doLoop()
	  {
		 int count_fail = 0;	/* ��u���Ȃ������ꍇ���A�����������m�F����J�E���^ */

		 /* �Q�[�����̃��[�v */
		 while(true)
		 {
			/* �l�Ԃ̎w����́i�����v���C�ł��Ȃ��j�ꍇ�̓n���h��\������ */
			boolean visible_hand = !m_current_player.canAutoPlay();
			m_board.enableVisibleHand(visible_hand);

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
	   * @brief		���݂̃v���C���[�����u���i���邢�͒u���Ȃ����Ƃ��m�肷��j
	   * @param [in]	player - ���݂̃v���C���[
	   * @return		true:���u�����Afalse:���u���Ȃ�����
	   */
	  private boolean waitPlaying(Player player)
	  {
		 /* �Ђ�����Ԃ���ʒu�ƁA�Ђ�����Ԃ��鑊���̔z��̃}�b�v��������
		  * �擾���Ă��� think()�ɓn�� */
		 HashMap<Point, Vector<Point>> candidate_pos_map;
		 candidate_pos_map = m_board.getCandidatePos(player.getPieceType());

		 System.out.println(candidate_pos_map); /* for debug: 20180220  �Ђ�����Ԃ����� */

		 /* ���݂̃v���C���[�����u���ʒu���l���� */
		 m_board.setNotifier(player);
		 Point pos = player.think(candidate_pos_map);
		 System.out.println("done.");  /* for debug: 20180221 */
		 
		 if (pos == null)
		 {
			return false;	/* ��u���Ȃ������I */
		 }

		 /* �w�肳�ꂽ�ʒu�Ɏ����̋��u�� */
		 ReversiPiece piece = new ReversiPiece(player.getPieceType());
		 m_board.setPiece(pos.x, pos.y, piece);
		 m_board.repaint();

		 /* ����̋���Ђ�����Ԃ� */
		 Vector<Point> pos_turn_pieces = candidate_pos_map.get(pos);
		 turnPirces(pos_turn_pieces, player.getPieceType());

		 return true;	/* ��u���� */
	  }

	  /********************************************************************************
	   * @brief		����̋���Ђ�����Ԃ�
	   * @param [in]	pos_turn_pieces - �Ђ�����Ԃ���̈ʒu�̔z��
	   * @param [in]	piece_type      - �w����̋�̎��
	   * @note		����Ђ�����Ԃ��A�j���[�V�����`����s��
	   */
	  private void turnPirces(Vector<Point> pos_turn_pieces, ReversiPiece.Type piece_type)
	  {
		 /* ��S���Ђ�����Ԃ�܂ŃA�j���[�V�������� */
		 /* �Ђ�����Ԃ���̃L���[ */
		 LinkedList<ReversiPiece> que_piece = new LinkedList<ReversiPiece>();
		 /* �A�j���[�V�������̋�̃L���[ */
		 LinkedList<ReversiPiece> que_remain = new LinkedList<ReversiPiece>();

		 int idx_posary = 0;
		 int count = 0;
		 boolean isAnimating = true;
		 while(isAnimating)
		 {
		 	try { Thread.sleep(20); }
		 	catch(Exception e) {}

			/* �����Ԋu�������āA�Ђ�����Ԃ�������Ԃɓo�^���Ă��� */
			if ((count % 5 == 0) && (idx_posary < pos_turn_pieces.size()))
			{
			   ReversiPiece piece = new ReversiPiece(piece_type);
			   piece.resetAnimation();
			   que_piece.add(piece);
			   Point pos_turn = pos_turn_pieces.get(idx_posary);
			   m_board.setPiece(pos_turn.x, pos_turn.y, piece);
			   idx_posary += 1;
			}
			/* �Ђ�����Ԃ��A�j���[�V������i�߂� */
			isAnimating = false;
			for (ReversiPiece piece : que_piece)
			{
			   if (!piece.progressAnimation())
			   {
				  que_remain.add(piece);
				  isAnimating = true;
			   }
			}
			/* �A�j���[�V������̃{�[�h��`�悷�� */
		 	m_board.repaint();
			/* �܂��A�j���[�V�������������Ă��Ȃ���ŌJ��Ԃ� */
			que_piece = que_remain;
			que_remain = new LinkedList<ReversiPiece>();
			count += 1;
		 }
	  }
}
