/* @file	Player.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class Player : �Q�[���̃v���C���[�̒��ۃN���X
 *	���Ӗ���
 *	  - �v���C���[���ł��鑀���S��
 *			(constructor)	�v���C���[�̖��O�Ɛ��E�����w�肵�ăv���C���[�𐶐�����
 *			think			���u���ꏊ���l����
 *			getPieceType	��̎�ʂ��擾����
 *
 *	class HumanPlayer : �蓮���������v���C���[
 *	���Ӗ���
 *	  - �蓮����v���C���[�i�l�ԁj���ł��鑀���S��
 *			(constructor)	
 *			doThink			���u���ꏊ���l����i�蓮�ŏꏊ��I�����鑀����܂ށj
 *			setPiece		�H�H�H�i�O��������u���ꏊ���w������j
 *
 *	class AutoPlayer : �������������v���C���[
 *	���Ӗ���
 *	  - ��������v���C���[�i�R���s���[�^�[�j���ł��鑀���S��
 *			(constructor)	
 *			doThink			���u���ꏊ���l����i�Ֆʏ󋵂��玩���Ŕ��f����j
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
	  private   boolean m_canAutoPlay;			/* true:�����v���C���[�i�R���s���[�^�[�j */

	  /********************************************************************************
	   * @brief		constructor
	   */
	  public Player(ReversiBoard board, String name, Player.Type player_type, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_name = name;
		 m_player_type = player_type;
		 m_piece_type = piece_type;
		 m_canAutoPlay = (player_type == Type.COMPUTER);
	  }

	  /********************************************************************************
	   * @brief		���u���ꏊ���l����
	   * @return	���u���ꏊ
	   * @note		��u���Ȃ��ꍇ�ɂ́A���̃��\�b�h���Ă΂�邱�Ƃ͂Ȃ�
	   */
	  abstract protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map);

	  /********************************************************************************
	   * @brief		���u���ꏊ���l����
	   * @return	���u���ꏊ�A�u���Ȃ��ꍇ�� null
	   */
	  public Point think(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 if (candidate_pos_map.size() != 0)
		 {
			return doThink(candidate_pos_map);
		 }
		 else
		 {
			return null;
		 }
	  }

	  /* @brief		��̎�ʂ��擾���� */
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

	  /********************************************************************************
	   * @brief		�����v���C���ǂ����₢���킹��
	   * @return	true: �����v���C�ł��܂�
	   */
	  public boolean canAutoPlay()
	  {
		 return m_canAutoPlay;
	  }
}


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i�l�ԁj
 * @note	�l�Ԃ��l���ċ��u���ꏊ��������
 */
class HumanPlayer extends Player
{
	  private Point m_pos;
	  private HashMap<Point, Vector<Point>> m_candidate_pos_map;
	  private Thread m_thread_think;

	  /* @brief	constructor */
	  public HumanPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, Player.Type.HUMAN, piece_type);
	  }

	  /********************************************************************************
	   * @brief	���u���ꏊ���l����
	   * @note		��u���Ȃ��ꍇ�ɂ́A���̃��\�b�h���Ă΂�邱�Ƃ͂Ȃ�
	   */
	  @Override
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 m_pos = null;
		 m_candidate_pos_map = candidate_pos_map;

		 /* ��u���ꂽ���ǂ������`�F�b�N���郋�[�v */
		 while(true)
		 {
			if (getPos() != null)
			{
			   break;
			}
			else
			{ /* �������Ȃ����͏����Q��iCPU���ׂ��グ�Ȃ����߁j */
			   try { Thread.sleep(1); }
			   catch(Exception e) { System.out.println(e); }
			}
		 }

		 return m_pos;
	  }

	  /********************************************************************************
	   * @brief		�w�肳�ꂽ�ꏊ�ɋ��u���邩��₢���킹��
	   * @param [in]	x, y - ���u�������ꏊ
	   * @return	true: ���u����Afalse: �u���Ȃ�
	   */
	  public boolean isAvailablePos(Point pos_place)
	  {
		 ReversiPiece piece = m_board.getPiece(pos_place.x, pos_place.y);
		 /* �Ֆʏ�ŋ�u����ĂȂ��ꏊ�Ȃ�A���u���邩�ǂ������m�F���� */
		 if (piece == null)
		 {
			for (Point pos : m_candidate_pos_map.keySet())
			{
			   System.out.printf("can place (%d,%d) == (%d,%d) ?\n",
								 pos_place.x, pos_place.y, pos.x, pos.y);

			   if (pos.equals(pos_place))
			   {
				  /* �����ɂ͋��u���� */
				  return true;
			   }
			}
		 }

		 /* ���u���Ȃ����� */
		 return false;
	  }

	  /********************************************************************************
	   * @brief		�w�肳�ꂽ�ꏊ�ɋ��u��
	   * @param [in]	x, y - ���u�������ꏊ
	   */
	  synchronized public void setPos(Point pos)
	  {
		 m_pos = pos;
	  }


	  /********************************************************************************
	   * @brief		�w�肳�ꂽ�ꏊ�ɋ��u��
	   * @param [in]	x, y - ���u�������ꏊ
	   */
	  synchronized private Point getPos()
	  {
		 return m_pos;
	  }
}


/*********************************************************************************
 * @brief	���o�[�V�̃v���C���[�i�R���s���[�^�[�j
 * @note	�����ōl���ċ��u��
 */
class AutoPlayer extends Player
{
	  /********************************************************************************
	  * @brief		constructor
	  */
	  public AutoPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, Player.Type.COMPUTER, piece_type);
	  }

	  /********************************************************************************
	   * @brief		���u���ꏊ���l����
	   *			�R���s���[�^�[�͋��u����ꏊ�����͂ŒT��
	   * @note		��u���Ȃ��ꍇ�ɂ́A���̃��\�b�h���Ă΂�邱�Ƃ͂Ȃ�
	   */
	  @Override
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos = null;

		 /* �u������L���ɂȂ�Ƃ���𒲂ׂāA�\�Ȃ�u�� */
		 /* TODO: 20180228  �������璷�Ȃ̂ł������������I�ȏ������ɂ���H */
		 for (Point pos_trgt : candidate_pos_map.keySet())
		 {
			if (checkAvailablePos_00(pos_trgt)) { return pos_trgt; }
		 }

		 for (Point pos_trgt : candidate_pos_map.keySet())
		 {
			if (checkAvailablePos_01(pos_trgt)) { return pos_trgt; }
		 }

		 for (Point pos_trgt : candidate_pos_map.keySet())
		 {
			if (checkAvailablePos_02(pos_trgt)) { return pos_trgt; }
		 }

		 /* �ǂ��Ƃ���ɒu���Ȃ� �� �����_���Œu����Ƃ����I�����Ēu�� */
		 int num = candidate_pos_map.size();
		 int idx = (new Random()).nextInt(num);
		 pos = (Point)(candidate_pos_map.keySet().toArray()[idx]);

		 return pos;
	  }

	  /********************************************************************************
	   * @brief		�ΏƎ��̋�̈ʒu�����i4�ӏ��j
	   * @return	�ΏƎ��̋�̈ʒu�i�v4�ӏ��j
	   */
	  private Vector<Point> makeContrastPos(Point pos_org)
	  {
		 Vector<Point> pos_ary = new Vector<Point>();
		 int x = pos_org.x;
		 int y = pos_org.y;
		 int inv_x = (m_board.WIDTH - 1) - x;
		 int inv_y = (m_board.HEIGHT - 1) - y;

		 pos_ary.add(new Point(x, y));
		 pos_ary.add(new Point(inv_x, y));
		 pos_ary.add(new Point(x, inv_y));
		 pos_ary.add(new Point(inv_x, inv_y));

		 return pos_ary;
	  }
	  
	  /********************************************************************************
	   * @brief		���u���Ƃ���𒲂ׂ�F00�F�S��
	   * @return	true: �u����
	   */
	  private boolean checkAvailablePos_00(Point pos_trgt)
	  {
		 Vector<Point> pos_ary = makeContrastPos(new Point(0, 0));
		 for (Point pos : pos_ary)
		 {
			if (pos_trgt.equals(pos))
			{
			   return true;   /* ���u���� */
			}
		 }

		 return false;  /* ���u����Ƃ���͂Ȃ����� */
	  }


	  /********************************************************************************
	   * @brief		���u���Ƃ���𒲂ׂ�F01�F�S���̂Q��
	   * @return	true: �u����
	   */
	  private boolean checkAvailablePos_01(Point pos_trgt)
	  {
		 Vector<Point> pos_ary = new Vector<Point>();
		 pos_ary.addAll(makeContrastPos(new Point(2, 0)));
		 pos_ary.addAll(makeContrastPos(new Point(0, 2)));
		 pos_ary.addAll(makeContrastPos(new Point(2, 2)));

		 for (Point pos : pos_ary)
		 {
			if (pos_trgt.equals(pos))
			{
			   return true;   /* ���u���� */
			}
		 }

		 return false;  /* ���u����Ƃ���͂Ȃ����� */
	  }

	  /********************************************************************************
	   * @brief		���u���Ƃ���𒲂ׂ�F02�F�H�H�H
	   * @return	true: �u����
	   */
	  private boolean checkAvailablePos_02(Point pos_trgt)
	  {
		 Vector<Point> pos_ary = new Vector<Point>();
		 pos_ary.addAll(makeContrastPos(new Point(3, 1)));
		 pos_ary.addAll(makeContrastPos(new Point(1, 3)));

		 for (Point pos : pos_ary)
		 {
			if (pos_trgt.equals(pos))
			{
			   return true;   /* ���u���� */
			}
		 }

		 return false;  /* ���u����Ƃ���͂Ȃ����� */
	  }
}
