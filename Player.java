/* @file	Player.java
 * @date	2018-02-18
 * @author	TK303
 */


import java.util.*;
import java.awt.*;


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i���ۃN���X�j
 */
abstract class Player
{
	  public enum Type {
		 HUMAN,
		 COMPUTER
	  }

	  protected ReversiBoard m_board;			/* �Ֆ� */
	  protected String m_name;					/* �v���C���[�̖��O */
	  protected Type m_player_type;				/* �v���C���[ { HUMAN | COMPUTER} */
	  protected ReversiPiece.Type m_piece_type;	/* �����̋�̐F { BLACK | WHITE } �� ������� */

	  /* @brief	constructor */
	  public Player(ReversiBoard board, String name, Player.Type player_type, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_name = name;
		 m_player_type = player_type;
		 m_piece_type = piece_type;
	  }

	  /* @brief	���u���ꏊ���l����
	   * @return	���u���ꏊ�A�u���Ȃ��ꍇ�� null */
	  public Point think()
	  {
		 /* TODO: 20180219  ���u����ꏊ�����X�g�A�b�v���� avail_pos �֊i�[���� */
		 Vector<Point> avail_pos = m_board.getAvailablePos(m_piece_type);

		 if (avail_pos.size() != 0)
		 {
			return doThink(avail_pos);
		 }
		 else
		 {
			return null;
		 }
	  }

	  /* @brief	��̎�ʂ��擾���� */
	  public ReversiPiece.Type getPieceType()
	  {
		 return m_piece_type;
	  }

	  /* @brief	���u���ꏊ���l����
	   * @return	���u���ꏊ�A�u���Ȃ��ꍇ�� null */
	  abstract protected Point doThink(Vector<Point> avail_pos);
	  
}


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i�l�ԁj
 * @note	�l�Ԃ��l���ċ��u���ꏊ��������
 */
class HumanPlayer extends Player
{
	  /* @brief	constructor */
	  public HumanPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, Player.Type.HUMAN, piece_type);
	  }

	  /* @brief	���u���ꏊ���l���� */
	  @Override
	  protected Point doThink(Vector<Point> avail_pos)
	  {
		 Point pos = new Point(0, 0);
		 /* TODO: 20180218  �l�Ԃ����u����ꏊ��������܂ŏ�����Ԃ��Ȃ��i�H�j */
		 return pos;
	  }

	  /* @brief	�i�l�Ԃ��j���u���ꏊ�������� */
	  public void setPiece()
	  {
		 /* ���u����ꏊ���擾���� */
		 Vector<Point> pt = m_board.getAvailablePos(m_piece_type);
	  }
}


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i�R���s���[�^�[�j
 * @note	�����ōl���ċ��u��
 */
class AutoPlayer extends Player
{
	  /* @brief	constructor */
	  public AutoPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, Player.Type.COMPUTER, piece_type);
	  }

	  /* @brief	���u���ꏊ���l���� */
	  @Override
	  protected Point doThink(Vector<Point> avail_pos)
	  {
		 Point pos = new Point(0, 0);
		 /* TODO: 20180218  �R���s���[�^�[�͋��u����ꏊ�����͂ŒT�� */
		 return pos;
	  }
}
