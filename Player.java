/* @file	Player.java
 * @date	2018-02-18
 * @author	TK303
 */


import java.util.*;
import java.awt.*;


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i���ۃN���X�j
 */
class Player
{
	  protected ReversiBoard m_board;				/* �Ֆ� */
	  protected ReversiPiece.Type m_piece_type;	/* �����̋�̐F { BLACK | WHITE }*/

	  /* constructor */
	  public Player(ReversiBoard board, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_piece_type = piece_type;
	  }


}


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i�l�ԁj
 * @note	�l�Ԃ��l���ċ��u���ꏊ��������
 */
class HumanPlayer extends Player
{
	  /* constructor */
	  public HumanPlayer(ReversiBoard board, ReversiPiece.Type piece_type)
	  {
		 super(board, piece_type);
	  }

	  /* ���u���ꏊ�������� */
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
	  /* constructor */
	  public AutoPlayer(ReversiBoard board, ReversiPiece.Type piece_type)
	  {
		 super(board, piece_type);
	  }

	  /* �����ŋ��u���ꏊ���l���� */
	  public void think()
	  {
	  }
}
