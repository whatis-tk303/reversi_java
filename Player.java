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
	  public Point think(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 try /* for debug: 20180221 ���Ԃ̂����鏈���i���[�U�[���͂Ȃǁj���V�~�����[�g */
		 {
			Thread.sleep(1000);
		 } catch(Exception e) { System.out.println(e); }

		 if (candidate_pos_map.size() != 0)
		 {
			return doThink(candidate_pos_map);
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

	  /* @brief	��̎�ʂ��擾���� */
	  @Override /*  */
	  public String toString()
	  {
		 return m_name;
	  }

	  /* @brief	���u���ꏊ���l����
	   * @return	���u���ꏊ�A�u���Ȃ��ꍇ�� null */
	  abstract protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map);
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
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos = null;
		 /* TODO: 20180218  �l�Ԃ����u����ꏊ��������܂ŏ�����Ԃ��Ȃ��i�H�j */
		 return pos;
	  }

	  /* @brief	�i�l�Ԃ��j���u���ꏊ�������� */
	  public void setPiece(int x, int y)
	  {
		 /* TODO: 20180220  ���u����ꏊ���擾���� */
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
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos = null;
		 /* TODO: 20180218  �R���s���[�^�[�͋��u����ꏊ�����͂ŒT��
		  * �i���͂Ƃ肠���������_���Œu����Ƃ����I�����Ēu���j */
		 int num = candidate_pos_map.size();
		 int idx = (new Random()).nextInt(num);
		 pos = (Point)(candidate_pos_map.keySet().toArray()[idx]);

		 return pos;
	  }
}
