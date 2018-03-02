/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class ReversiBoard : ���o�[�V�̔Ֆ�
 *	���Ӗ���
 *	  - �Ֆʂ��Ǘ�����
 *			(constructor)	�Ֆʂ𐶐�����
 *			reset			�Ֆʂ����Z�b�g����
 *			getPiece		�w�肳�ꂽ�ʒu�ɂ������Q�Ƃ���
 *			countPieces		�w�肳�ꂽ��ʂ̋�̐��𐔂���
 *
 *	  - ���z�u���`�悷��
 *			setPiece		�Ֆʂɋ��u��
 *			paintComponent	�Ֆʂ�`�悷��
 *
 *	  - ��u����ꏊ�ƁA�Ђ�����Ԃ����𒲂ׂ�
 *			getCandidatePos	�w�肳�ꂽ�F�̋�u������̈ʒu�̃��X�g���擾����
 *			getTurnPieces	�Ђ�����Ԃ����𒲂ׂ�
 *
 *	class ReversiPiece : ���o�[�V�̋�
 *	���Ӗ���
 *	  - ���o�[�V�̋�̑�����ێ����`�悷��
 *			(constructor)	�w�肳�ꂽ��ʂ̋�𐶐�����
 *			getType			��̎�ʂ��擾����
 *			rendering		�w�肳�ꂽ�ʒu�ɋ��`�悷��
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;




/*********************************************************************************
 * @brief	����A�j���[�V����������I/F
 */
interface AnimationRender
{
	  /* �A�j���[�V�����̐i������ݒ肷�� */
	  public void doAnimation(AnimationProp anim_prop);
}


/*********************************************************************************
 * @brief	����A�j���[�V���������鑮��
 */
class AnimationProp
{
	  public static final int RATE_MIN = 0;
	  public static final int RATE_MAX = 100;

	  private int m_rate;  /* �A�j���[�V�����i�����i0 .. 100%�j */
	  private Object m_from;
	  private Object m_to;
	  private AnimationRender m_render;

	  /* constructor */
	  public AnimationProp(AnimationRender render)
	  {
		 m_rate = 0;
		 m_from = null;
		 m_to = null;
		 m_render = render;
	  }

	  /* �i�������擾���� */
	  public int getRate()
	  {
		 return m_rate;
	  }

	  /* from���擾���� */
	  public Object getFrom()
	  {
		 return m_from;
	  }

	  /* to���擾���� */
	  public Object getTo()
	  {
		 return m_to;
	  }

	  /* ������ */
	  public void initProp(Object from, Object to)
	  {
		 m_rate = RATE_MIN;
		 m_from = from;
		 m_to = to;
	  }

	  /* �A�j���[�V�����̐i������ݒ肷�� */
	  public void setRate(int rate)
	  {
		 if (rate < RATE_MIN)
		 {
			m_rate = RATE_MIN;
		 }
		 else if (RATE_MAX < rate)
		 {
			m_rate = RATE_MAX;
		 }
		 else
		 {
			m_rate = rate;
		 }

		 m_render.doAnimation(this);
	  }

	  /* �A�j���[�V�����̐i����i�߂� */
	  public void increment(int rate)
	  {
		 m_rate += rate;
		 if (RATE_MAX < m_rate)
		 {
			m_rate = RATE_MAX;
		 }

		 m_render.doAnimation(this);
	  }

	  /********************************************************************************
	   * @brief		�A�j���[�V���������Z�b�g����
	   * @note		�A�j���[�V�������J�n����ۂɂ��̃��\�b�h���Ă�ł����A
	   *			���� progressAnimation()���ĂԂ��ƂŃA�j���[�V������i��������
	   */
	  public void resetAnimation()
	  {
		 setRate(RATE_MIN);
	  }

	  /********************************************************************************
	   * @brief		�A�j���[�V������i�߂�
	   * @return	true:�A�j���[�V���������������Afalse:�܂��A�j���[�V�����̓r��
	   */
	  public boolean progressAnimation(int rate)
	  {
		 increment(rate);
		 return (getRate() == RATE_MAX);
	  }
}


/*********************************************************************************
 * @brief	���o�[�V�̋�
 */
class ReversiPiece implements AnimationRender
{
	  /* @brief	��̎�� { �� | �� } */
	  public enum Type {
		 BLACK("piece_black.png"),
		 WHITE("piece_white.png"),
		 ;

		 private String image_file;
		 private Icon icon;

		 private Type(String fname)
		 {
			this.image_file = fname;
			this.icon = new ImageIcon(fname);
		 }
	  };

	  private Type m_type;
	  private AnimationProp m_anim_prop;
	  private int m_anim_x, m_anim_y;
	  private Icon m_anim_icon;

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public ReversiPiece(Type type)
	  {
		 m_type = type;
		 m_anim_prop = new AnimationProp(this);
		 m_anim_prop.setRate(AnimationProp.RATE_MAX);  /* �ŏI�I�ɂЂ�����Ԃ������ */
	  }

	  /* @brief	��̎�ʂ��擾���� */
	  public Type getType()
	  {
		 return m_type;
	  }

	  /********************************************************************************
	   * @brief	�w�肳�ꂽ�ʒu�ɋ��`�悷��
	   * @param [in]	g    - �`��Ώۂ̃O���t�B�b�N�X
	   * @param [in]	pos  - �`�悷��ʒu�i������j
	   * @param [in]	size - �`�悷���`�̃T�C�Y�i���A�����j
	   */
	  public void rendering(Graphics g, Point pos, Dimension size)
	  {
		 /* TODO: 20180301  size�ɍ��킹�ăC���[�W���g��E�k������ */
		 //m_type.icon.paintIcon(null, g, pos.x, pos.y);
		 int x = pos.x + m_anim_x;
		 int y = pos.y + m_anim_y;
		 Icon icon = m_anim_icon;
		 m_anim_icon.paintIcon(null, g, x, y);
	  }

	  /********************************************************************************
	   * @brief	�A�j���[�V���������s����
	   * @param [in]	anim_prop - �A�j���[�V�����̑���
	   */
	  @Override /* AnimationRender */
	  public void doAnimation(AnimationProp anim_prop)
	  {
		 int rate = anim_prop.getRate();

		 Icon icon_from = ((m_type == Type.BLACK) ? Type.WHITE : Type.BLACK).icon;
		 Icon icon_to = m_type.icon;

		 if (rate == AnimationProp.RATE_MIN)
		 { /* �Ђ�����Ԃ��O */
			m_anim_x = 0;
			m_anim_y = 0;
			m_anim_icon = icon_from;
		 }
		 else if (rate == AnimationProp.RATE_MAX)
		 { /* �Ђ�����Ԃ����� */
			m_anim_x = 0;
			m_anim_y = 0;
			m_anim_icon = icon_to;
		 }
		 else
		 {
			m_anim_x = 0;
			double omega = ((double)rate / AnimationProp.RATE_MAX) * Math.PI;
			m_anim_y = (int)(40.0 * -Math.sin(omega));

			if (rate < (AnimationProp.RATE_MAX / 2))
			{ /* �Ђ�����Ԃ��̑O�� */
			   m_anim_icon = icon_from;
			}
			else
			{ /* �Ђ�����Ԃ��̌㔼 */
			   m_anim_icon = icon_to;
			}
		 }
	  }

	  /********************************************************************************
	   * @brief		�A�j���[�V���������Z�b�g����
	   * @note		�A�j���[�V�������J�n����ۂɂ��̃��\�b�h���Ă�ł����A
	   *			���� progressAnimation()���ĂԂ��ƂŃA�j���[�V������i��������
	   */
	  public void resetAnimation()
	  {
		 m_anim_prop.resetAnimation();
	  }

	  /********************************************************************************
	   * @brief		�A�j���[�V������i�߂�
	   * @return	true:�A�j���[�V���������������Afalse:�܂��A�j���[�V�����̓r��
	   */
	  public boolean progressAnimation()
	  {
		 return m_anim_prop.progressAnimation(10);
	  }
}


/*********************************************************************************
 * @brief	���o�[�V�̔Ֆ�
 */
class ReversiBoard extends JPanel
{
	  public static final int BOARD_SIZE = 8;		/* �Ֆʂ̋�搔�i�W���j */
	  public static final int WIDTH  = BOARD_SIZE;	/* �Ֆʂ̉������̋�搔   */
	  public static final int HEIGHT = BOARD_SIZE;	/* �Ֆʂ̏c�����̋�搔   */

	  /* ��𒲂ׂ�����̔z��i�㉺���E�΂߂̌v�W�����j */
	  public static final Point[] AROUND_8DIR = {
		 new Point(  0, -1 ),
		 new Point(  1, -1 ),
		 new Point(  1,  0 ),
		 new Point(  1,  1 ),
		 new Point(  0,  1 ),
		 new Point( -1,  1 ),
		 new Point( -1,  0 ),
		 new Point( -1, -1 ),
	  };

	  private ReversiPiece[][] m_piece_matrix;	/* �Ֆʂ̋���Ǘ�����z�� */
	  private Icon m_icon_hand;					/* �w����̃C���[�W�A�C�R�� */
	  private boolean m_isVisibleHand;			/* �Ֆʏ�Ƀn���h�A�C�R����\������t���O */
	  private HumanPlayer m_player_to_notify;	/* �Ֆʏ�ł̑����`�������v���C���[�i���l�ԁj */

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public ReversiBoard()
	  {
		 m_piece_matrix = new ReversiPiece[8][8]; /* { null | Type.Black | Type.WHITE } */

		 m_icon_hand = new ImageIcon("icon_hand.png");
		 m_isVisibleHand = false;

		 /* �Ֆʂ��N���b�N�������̓����o�^���� */
		 addMouseListener(new MouseAdapter(){
				  @Override public void mouseClicked(MouseEvent e) 
				  {
					 doMouseClicked(e);
				  }
			});

		 /* �Ֆʂ��}�E�X�J�[�\�������������̓����o�^���� */
		 addMouseMotionListener(new MouseMotionAdapter(){
				  @Override public void mouseMoved(MouseEvent e) 
				  {
					 doMouseMoved(e);
				  }
			});

		 Dimension size = new Dimension(400, 400);	/* ��ʏ�̔Ֆʂ̃T�C�Y�i�s�N�Z���j */
		 setSize(size);
		 setPreferredSize(size);
	  }

	  /********************************************************************************
	   * @brief		�Ֆʂ��}�E�X�J�[�\�������������̓���
	   * @note		�l�ԃv���C���[�̏ꍇ�̂ݗL�� 
	   */
	  private void doMouseMoved(MouseEvent e)
	  {
		 if (m_player_to_notify == null)
		 {
			return;
		 }

		 /* �l�ԃv���C���[�Ȃ�n���h��`�悷�� */
		 repaint();
	  }

	  /********************************************************************************
	   * @brief		�Ֆʂ��N���b�N�������̓���
	   * @note		�l�ԃv���C���[�̏ꍇ�̂ݗL�� 
	   */
	  private void doMouseClicked(MouseEvent e)
	  {
		 if (m_player_to_notify == null)
		 {
			return;
		 }

		 Point pos_scrn = e.getPoint();
		 Point pos = convertComponentPosToBoardPos(pos_scrn);
		 if (m_player_to_notify.isAvailablePos(pos))
		 {
			System.out.printf("place here (%d,%d).\n", pos.x, pos.y);
			m_player_to_notify.setPos(pos);
		 }
		 else
		 {
			System.out.printf("can not place here (%d,%d) !\n", pos.x, pos.y);
		 }
	  }

	  /********************************************************************************
	   * @brief	�X�N���[�����W����{�[�h���W���Z�o����
	   */
	  private Point convertComponentPosToBoardPos(Point pt_scrn)
	  {
		 Dimension size = getSize();
		 int d = size.width / BOARD_SIZE;
		 int x = pt_scrn.x / d;
		 int y = pt_scrn.y / d;
		 Point pos = new Point(x, y);
		 return pos;
	  }

	  /********************************************************************************
	   * @brief	�n���h�A�C�R���̕\���^��\����؂�ւ���
	   */
	  public void enableVisibleHand(boolean en)
	  {
		 m_isVisibleHand = en;
		 repaint();
	  }

	  /********************************************************************************
	   * @brief	�Ֆʂ����Z�b�g����
	   */
	  public void reset()
	  {
		 int x, y;
		 for (y=0; y<HEIGHT; y++)
		 {
			for (x=0; x<WIDTH; x++)
			{
			   m_piece_matrix[x][y] = null;
			}
		 }
	  }

	  /********************************************************************************
	   * @brief	�w�肳�ꂽ�ʒu�ɂ������Q�Ƃ���
	   */
	  public ReversiPiece getPiece(int x, int y)
	  {
		 return m_piece_matrix[x][y];
	  }

	  /********************************************************************************
	   * @brief	�w�肳�ꂽ�ʒu�ɂ������Q�Ƃ���
	   * @param [in]	piece_type - ��̎��
	   * @return	�Ֆʏ�̋�̐�
	   */
	  public int countPieces(ReversiPiece.Type piece_type)
	  {
		 int num = 0;
		 int x, y;

		 for (y=0; y<HEIGHT; y++)
		 {
			for (x=0; x<WIDTH; x++)
			{
			   ReversiPiece piece = getPiece(x, y);
			   if (piece != null)
			   {
				  if (piece_type == piece.getType())
				  {
					 num += 1;
				  }
			   }
			}
		 }

		 return num;
	  }

	  /********************************************************************************
	   * @brief	�Ֆʂɋ��u��
	   * @param [in]	x, y  - ���u���ʒu
	   * @param [in]	piece - �u����
	   * @return	true:���u�����Afalse:���u���Ȃ�
	   */
	  public boolean setPiece(int x, int y, ReversiPiece piece)
	  {
		 if ((0 <= x) && (x < WIDTH) && (0 <= y) && (y < HEIGHT))
		 { /* �w�肳�ꂽ�F�̋��u�� */
			m_piece_matrix[x][y] = piece;
			return true;
		 }
		 else
		 {
			return false;
		 }
	  }

	  /********************************************************************************
	   * @brief	�w�肳�ꂽ�F�̋�u������̈ʒu�̃��X�g���擾����
	   * @param [in]	piece_type - �u����̎��
	   * @return	�Ђ�����Ԃ���ʒu�ƁA�Ђ�����Ԃ��鑊���̔z��̃}�b�v
	   */
	  public HashMap<Point, Vector<Point>> getCandidatePos(ReversiPiece.Type piece_type)
	  {
		 /* ����u����ꏊ��T���菇��
		  *   - �Ֆʏ�̑S��i�W���W�j�ɓn���Ĉȉ��𒲂ׂ�
		  *     - ���ׂ�ʒu�ɋ�u����Ă��Ȃ�
		  *       - ���ׂ�ʒu�̏㉺���E�΂߂̂W�����̂����ꂩ�ɑ���̋����
		  *         - ����̋�̕����Ɏ����̋����i�[�܂ŏ��ԂɌ��Ē��ׂ�j
		  */

		 /* �Ђ�����Ԃ���ʒu�ƁA�Ђ�����Ԃ��鑊���̔z��̃}�b�v */
		 HashMap<Point, Vector<Point>> candidate_pos_map = new HashMap<Point, Vector<Point>>();

		 int x, y;

		 for (y=0; y<HEIGHT; y++)
		 {
			for (x=0; x<WIDTH; x++)
			{
			   ReversiPiece piece_target = m_piece_matrix[x][y];
			   if (piece_target != null)
			   { /* ���ɋ�u���Ă�������u���Ȃ� �� �����͒��ׂȂ� */
				  continue;
			   }

			   Point pos_candidate = null;

			   for (Point dir : AROUND_8DIR)
			   {
				  /* �w�肵���ʒu�̗� */
				  int xx = x + dir.x;
				  int yy = y + dir.y;
				  /* �ׂ̈ʒu���Ֆʂ̊O�Ȃ璲�ׂȂ� */
				  if ((0 <= xx) && (xx < WIDTH) && (0 <= yy) && (yy < HEIGHT))
				  {
					 /* �ׂ̈ʒu�ɑ���̋�P�ł����݂�����A�Ђ�����Ԃ�����̈ʒu�Ƃ��� */
					 ReversiPiece piece_next = m_piece_matrix[xx][yy];
					 if ((piece_next != null) && (piece_next.getType() != piece_type))
					 {
						pos_candidate = new Point(x, y);
						break;
					 }
				  }
			   }

			   /* �Ђ�����Ԃ����₪���������I */
			   if (pos_candidate != null)
			   {
				  /* �{���ɂЂ�����Ԃ���Ƃ����T���Ċi�[���� */				  
				  Vector<Point> pos_turn_pieces = getTurnPieces(pos_candidate, piece_type);
				  if (0 < pos_turn_pieces.size())
				  {
					 candidate_pos_map.put(pos_candidate, pos_turn_pieces);
				  }
			   }
			}
		 }

		 return candidate_pos_map;
	  }

	  /********************************************************************************
	   * @brief	�Ђ�����Ԃ��鑊���̈ʒu���擾����
	   * @param [in]	pos   - �����̋�̈ʒu
	   * @param [in]	piece - �����̋�̎��
	   * @return	�Ђ�����Ԃ��鑊���̈ʒu�̔z��i�Ȃ���΋�̔z��j
	   * @note	�����̋�̈ʒu����W�������X�L�������āA�Ђ�����Ԃ��鑊��̋��
	   *        �S�����X�g�A�b�v���Ĕz��Ɋi�[����B
	   * @note	�z��̏��Ԃ́A�����̋�̈ʒu���������ɑ{�������ԂŊi�[����B
	   *        �i��F �ŏ��͏�����̑S���A���ɉE�΂ߏ�����̑S���A���̎��͉E�����̑S���E�E�E�j
	   *        �������邱�ƂŁA����Ђ�����Ԃ��A�j���[�V����������ۂ�
	   *        �����̋�ɋ߂��Ƃ��납�������ɂЂ�����Ԃ�悤�Ɍ����邱�Ƃ��ł���B
	   */
	  private Vector<Point> getTurnPieces(Point pos, ReversiPiece.Type piece_type)
	  {
		 Vector<Point> pos_turn_pieces = new Vector<Point>();

		 /* �W�������ׂĂɂ��ĂЂ�����Ԃ��鑊�����邩�T���Ĕz��Ɋi�[���� */
		 for (Point dir : AROUND_8DIR)
		 {
			Point pos_find = new Point(pos);
			Vector<Point> temp_pos_pieces = new Vector<Point>();

			for (int n=0; n<(8-1); n++)  /* ������Œ��ׂ���ő吔��(8-1) */
			{
			   /* ��������Ђ�����Ԃ��鑊�����邩�T�� */
			   pos_find.translate(dir.x, dir.y);
			   int xx = pos_find.x;
			   int yy = pos_find.y;

			   if ((0 <= xx) && (xx < WIDTH) && (0 <= yy) && (yy < HEIGHT))
			   {
				  ReversiPiece piece = m_piece_matrix[xx][yy];
				  if (piece == null)
				  {
					 break;	/* ���߂Ȃ��I �� ���̕����� */
				  }
				  else
				  {
					 /* �Ђ�����Ԃ�����̋����H */
					 if (piece.getType() == piece_type)
					 {/* �����Ɠ����F�̋�Ȃ狲�߂�
					   * �� �Ђ�����Ԃ��鑊���i�̔z��j���m�肷��i�� ��̏ꍇ������j */
						pos_turn_pieces.addAll(temp_pos_pieces);
						break;	/* ������𒲂׏I����� �� ���̕����� */
					 }
					 else
					 { /* �Ђ�����Ԃ�����̋��ǉ����Ă��� */
						temp_pos_pieces.add(new Point(xx, yy));
					 }
				  }
			   }
			   else
			   {
				  break;	/* ���߂Ȃ��I �� ���̕����� */
			   }
			}
		 }
		 
		 return pos_turn_pieces;
	  }

	  /********************************************************************************
	   * @brief	�Ֆʂ�`�悷��
	   */
	  @Override /* JPanel.paintComponent */
	  protected void paintComponent(Graphics g)
	  {
		 Dimension size = getSize();
		 Color color_board = new Color(32, 96, 0);
		 setForeground(color_board);
		 g.fillRect(0, 0, size.width, size.height);

		 int x, y;
		 int d = size.width / WIDTH;
		 Dimension size_piece = new Dimension(d, d);

		 /* �Ֆʂ�`�悷�� */
		 g.setColor(Color.BLACK);
		 for (y=0; y<HEIGHT; y++)
		 {
			int yy = y * d;
			g.drawLine(0, yy, size.width, yy);

			for (x=0; x<WIDTH; x++)
			{
			   int xx = x * d;
			   g.drawLine(xx, 0, xx, size.height);
			}
		 }

		 /* �u����Ă�����`�悷�� */
		 for (y=0; y<HEIGHT; y++)
		 {
			int yy = y * d;

			for (x=0; x<WIDTH; x++)
			{
			   ReversiPiece piece = m_piece_matrix[x][y];
			   
			   if (piece == null)
			   {
				  continue;
			   }

			   int xx = x * d;
			   Point pos = new Point(xx, yy);
			   piece.rendering(g, pos, size_piece);
			}
		 }

		 /* �l�Ԃ̎w����̎��ɔՖʏ�Ƀ}�E�X�J�[�\��������Ȃ�A�w�����`�悷�� */
		 if (m_isVisibleHand)
		 {
			Point pt_cursor = MouseInfo.getPointerInfo().getLocation();
			Point pt_component = getLocationOnScreen();
			pt_cursor.x -= pt_component.x;
			pt_cursor.y -= pt_component.y;

			Rectangle rect = new Rectangle(size);
			if (rect.contains(pt_cursor))
			{ /* �Ֆʂ̃}�X�ڂ̐^�񒆂�������n���h�̐l�����w���w���悤�ɍ��W�𒲐����� */
			   Point pos = convertComponentPosToBoardPos(pt_cursor);
			   int x_icon = (pos.x * d) + ((d - m_icon_hand.getIconWidth()) / 2) + 5;
			   int y_icon = (pos.y * d) + ((d - m_icon_hand.getIconHeight()) / 2) + 12;
			   m_icon_hand.paintIcon(this, g, x_icon, y_icon);
			}
		 }
	  }

	  /********************************************************************************
	   * @brief	���u�����Ƃ������Ƃ�ʒm�������v���C���[���Z�b�g����
	   * @note	AutoPlayer�͎����ŋ��u���̂ŃZ�b�g����K�v�͂Ȃ�
	   */
	  public void setNotifier(Player player)
	  {
		 m_player_to_notify = player.canAutoPlay() ? null : (HumanPlayer)player;
	  }
}
