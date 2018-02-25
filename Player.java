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

	  /********************************************************************************
	   * @brief		constructor
	   */
	  public Player(ReversiBoard board, String name, Player.Type player_type, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_name = name;
		 m_player_type = player_type;
		 m_piece_type = piece_type;
	  }

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

	  /* @brief		���u���ꏊ���l����
	   * @return	���u���ꏊ
	   * @note		��u���Ȃ��ꍇ�ɂ́A���̃��\�b�h���Ă΂�邱�Ƃ͂Ȃ�
	   */
	  abstract protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map);

	  /********************************************************************************
	   * @brief		�����v���C���ǂ����₢���킹��
	   * @return	true: �����v���C�ł��܂�
	   */
	  abstract public boolean isAutoPlay();
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

		 m_thread_think = new Thread(new Runnable(){
				  public void run()
				  {
					 while(true)
					 {
						try
						{
						   /* ��u���ꂽ�烋�[�v�𔲂��� */
						   if (getPos() != null)
						   {
							  break;
						   }

						   Thread.sleep(100);  /* �V�X�e���̕��ׂ��グ�Ȃ��悤�ɁE�E�E */
						   /* �܂���u����ĂȂ������� wait()�𔲂��Ă���
							*    �� �Ֆʂ��ĕ`�悷��i�ړ�����n���h��`�悷�邽�߁j */
						   m_board.repaint();
						}
						catch(Exception e)
						{
						   System.out.println(e);
						}
					 }
				  }
			});


		 /* �l�Ԃ����u���܂ő҂� */
		 try
		 {
			m_thread_think.start();
			m_thread_think.join();
		 }
		 catch(Exception e)
		 {
			System.out.println(e);
		 }

		 return m_pos;
	  }

	  /********************************************************************************
	   * @brief		�����v���C���ǂ����₢���킹��
	   * @return	true: �����v���C�ł��܂�
	   */
	  public boolean isAutoPlay()
	  {
		 return false;
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
			   System.out.printf("can place (%d,%d) == (%d,%d) ?\n", pos_place.x, pos_place.y, pos.x, pos.y);

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
	   * @brief	���u���ꏊ���l����
	   * @note		��u���Ȃ��ꍇ�ɂ́A���̃��\�b�h���Ă΂�邱�Ƃ͂Ȃ�
	   */
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

	  /********************************************************************************
	   * @brief		�����v���C���ǂ����₢���킹��
	   * @return	true: �����v���C�ł��܂�
	   */
	  public boolean isAutoPlay()
	  {
		 return true;
	  }
}
