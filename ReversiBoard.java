/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class ReversiBoard : $B%j%P!<%7$NHWLL(B
 *	$B!c@UL3!d(B
 *	  - $BHWLL$r4IM}$9$k(B
 *			(constructor)	$BHWLL$r@8@.$9$k(B
 *			reset			$BHWLL$r%j%;%C%H$9$k(B
 *			getPiece		$B;XDj$5$l$?0LCV$K$"$k6p$r;2>H$9$k(B
 *			countPieces		$B;XDj$5$l$?<oJL$N6p$N?t$r?t$($k(B
 *
 *	  - $B6p$rG[CV$7IA2h$9$k(B
 *			setPiece		$BHWLL$K6p$rCV$/(B
 *			paintComponent	$BHWLL$rIA2h$9$k(B
 *
 *	  - $B6p$,CV$1$k>l=j$H!"$R$C$/$jJV$;$k6p$rD4$Y$k(B
 *			getCandidatePos	$B;XDj$5$l$??'$N6p$,CV$1$k8uJd$N0LCV$N%j%9%H$r<hF@$9$k(B
 *			getTurnPieces	$B$R$C$/$jJV$;$k6p$rD4$Y$k(B
 *
 *	class ReversiPiece : $B%j%P!<%7$N6p(B
 *	$B!c@UL3!d(B
 *	  - $B%j%P!<%7$N6p$NB0@-$rJ];}$7IA2h$9$k(B
 *			(constructor)	$B;XDj$5$l$?<oJL$N6p$r@8@.$9$k(B
 *			getType			$B6p$N<oJL$r<hF@$9$k(B
 *			rendering		$B;XDj$5$l$?0LCV$K6p$rIA2h$9$k(B
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	$B%j%P!<%7$N6p(B
 */
class ReversiPiece
{
	  /* @brief	$B6p$N<oJL(B { $B9u(B | $BGr(B } */
	  public enum Type {
		 BLACK,
		 WHITE,
	  };

	  private Type m_type;

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public ReversiPiece(Type type)
	  {
		 m_type = type;
	  }

	  /* @brief	$B6p$N<oJL$r<hF@$9$k(B */
	  public Type getType()
	  {
		 return m_type;
	  }

	  /********************************************************************************
	   * @brief	$B;XDj$5$l$?0LCV$K6p$rIA2h$9$k(B
	   * @param [in]	g    - $BIA2hBP>]$N%0%i%U%#%C%/%9(B
	   * @param [in]	pos  - $BIA2h$9$k0LCV!J:8>e6y!K(B
	   * @param [in]	size - $BIA2h$9$k6k7A$N%5%$%:!JI}!"9b$5!K(B
	   */
	  public void rendering(Graphics g, Point pos, Dimension size)
	  {
		 Color color;

		 if (m_type == Type.BLACK)
		 {
			color = Color.BLACK;
		 }
		 else if (m_type == Type.WHITE)
		 {
			color = Color.WHITE;
		 }
		 else
		 {
			return;
		 }

		 int w = (int)(size.width * 0.8);
		 int h = (int)(size.height * 0.8);
		 int dx = (size.width - w) / 2;
		 int dy = (size.height - h) / 2;
		 int x = pos.x + dx;
		 int y = pos.y + dy;
		 g.setColor(color);
		 g.fillOval(x, y, w, h); 
	  }
}


/*********************************************************************************
 * @brief	$B%j%P!<%7$NHWLL(B
 */
class ReversiBoard extends JPanel
{
	  public static final int BOARD_SIZE = 8;		/* $BHWLL$N6h2h?t!J#88DJ,!K(B */
	  public static final int WIDTH  = BOARD_SIZE;	/* $BHWLL$N2#J}8~$N6h2h?t(B   */
	  public static final int HEIGHT = BOARD_SIZE;	/* $BHWLL$N=DJ}8~$N6h2h?t(B   */

	  /* $B6p$rD4$Y$kJ}8~$NG[Ns!J>e2<:81&<P$a$N7W#8J}8~!K(B */
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

	  private ReversiPiece[][] m_piece_matrix;	/* $BHWLL$N6p$r4IM}$9$kG[Ns(B */
	  private Icon m_icon_hand;					/* $B;X$7<j$N%$%a!<%8%"%$%3%s(B */
	  private boolean m_isVisibleHand;			/* $BHWLL>e$K%O%s%I%"%$%3%s$rI=<($9$k%U%i%0(B */
	  private HumanPlayer m_player_to_notify;	/* $BHWLL>e$G$NA`:n$rEA$($?$$%W%l%$%d!<!J!a?M4V!K(B */

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public ReversiBoard()
	  {
		 m_piece_matrix = new ReversiPiece[8][8]; /* { null | Type.Black | Type.WHITE } */

		 m_icon_hand = new ImageIcon("icon_hand.png");
		 m_isVisibleHand = false;

		 /* $BHWLL$r%/%j%C%/$7$?;~$NF0:n$rEPO?$9$k(B */
		 addMouseListener(new MouseAdapter(){
				  @Override public void mouseClicked(MouseEvent e) 
				  {
					 doMouseClicked(e);
				  }
			});

		 /* $BHWLL$r%^%&%9%+!<%=%k$,F0$$$?;~$NF0:n$rEPO?$9$k(B */
		 addMouseMotionListener(new MouseMotionAdapter(){
				  @Override public void mouseMoved(MouseEvent e) 
				  {
					 doMouseMoved(e);
				  }
			});

		 Dimension size = new Dimension(400, 400);	/* $B2hLL>e$NHWLL$N%5%$%:!J%T%/%;%k!K(B */
		 setSize(size);
		 setPreferredSize(size);
	  }

	  /********************************************************************************
	   * @brief		$BHWLL$r%^%&%9%+!<%=%k$,F0$$$?;~$NF0:n(B
	   * @note		$B?M4V%W%l%$%d!<$N>l9g$N$_M-8z(B 
	   */
	  private void doMouseMoved(MouseEvent e)
	  {
		 if (m_player_to_notify == null)
		 {
			return;
		 }

		 /* $B?M4V%W%l%$%d!<$J$i%O%s%I$rIA2h$9$k(B */
		 repaint();
	  }

	  /********************************************************************************
	   * @brief		$BHWLL$r%/%j%C%/$7$?;~$NF0:n(B
	   * @note		$B?M4V%W%l%$%d!<$N>l9g$N$_M-8z(B 
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
	   * @brief	$B%9%/%j!<%s:BI8$+$i%\!<%I:BI8$r;;=P$9$k(B
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
	   * @brief	$B%O%s%I%"%$%3%s$NI=<(!?HsI=<($r@Z$jBX$($k(B
	   */
	  public void enableVisibleHand(boolean en)
	  {
		 m_isVisibleHand = en;
		 repaint();
	  }

	  /********************************************************************************
	   * @brief	$BHWLL$r%j%;%C%H$9$k(B
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
	   * @brief	$B;XDj$5$l$?0LCV$K$"$k6p$r;2>H$9$k(B
	   */
	  public ReversiPiece getPiece(int x, int y)
	  {
		 return m_piece_matrix[x][y];
	  }

	  /********************************************************************************
	   * @brief	$B;XDj$5$l$?0LCV$K$"$k6p$r;2>H$9$k(B
	   * @param [in]	piece_type - $B6p$N<oJL(B
	   * @return	$BHWLL>e$N6p$N?t(B
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
	   * @brief	$BHWLL$K6p$rCV$/(B
	   * @param [in]	x, y  - $B6p$rCV$/0LCV(B
	   * @param [in]	piece - $BCV$/6p(B
	   * @return	true:$B6p$rCV$$$?!"(Bfalse:$B6p$rCV$1$J$$(B
	   */
	  public boolean setPiece(int x, int y, ReversiPiece piece)
	  {
		 if ((0 <= x) && (x < WIDTH) && (0 <= y) && (y < HEIGHT))
		 { /* $B;XDj$5$l$??'$N6p$rCV$/(B */
			m_piece_matrix[x][y] = piece;
			return true;
		 }
		 else
		 {
			return false;
		 }
	  }

	  /********************************************************************************
	   * @brief	$B;XDj$5$l$??'$N6p$,CV$1$k8uJd$N0LCV$N%j%9%H$r<hF@$9$k(B
	   * @param [in]	piece_type - $BCV$/6p$N<oJL(B
	   * @return	$B$R$C$/$jJV$;$k0LCV$H!"$R$C$/$jJV$;$kAj<j6p$NG[Ns$N%^%C%W(B
	   */
	  public HashMap<Point, Vector<Point>> getCandidatePos(ReversiPiece.Type piece_type)
	  {
		 /* $B!c6p$,CV$1$k>l=j$rC5$9<j=g!d(B
		  *   - $BHWLL>e$NA40h!J#8#x#8!K$KEO$C$F0J2<$rD4$Y$k(B
		  *     - $BD4$Y$k0LCV$K6p$,CV$+$l$F$$$J$$(B
		  *       - $BD4$Y$k0LCV$N>e2<:81&<P$a$N#8J}8~$N$$$:$l$+$KAj<j$N6p$,$"$k(B
		  *         - $BAj<j$N6p$NJ}8~$K<+J,$N6p$,$"$k!JC<$^$G=gHV$K8+$FD4$Y$k!K(B
		  */

		 /* $B$R$C$/$jJV$;$k0LCV$H!"$R$C$/$jJV$;$kAj<j6p$NG[Ns$N%^%C%W(B */
		 HashMap<Point, Vector<Point>> candidate_pos_map = new HashMap<Point, Vector<Point>>();

		 int x, y;

		 for (y=0; y<HEIGHT; y++)
		 {
			for (x=0; x<WIDTH; x++)
			{
			   ReversiPiece piece_target = m_piece_matrix[x][y];
			   if (piece_target != null)
			   { /* $B4{$K6p$,CV$$$F$"$C$?$iCV$1$J$$(B $B"*(B $B$=$3$OD4$Y$J$$(B */
				  continue;
			   }

			   Point pos_candidate = null;

			   for (Point dir : AROUND_8DIR)
			   {
				  /* $B;XDj$7$?0LCV$NNY(B */
				  int xx = x + dir.x;
				  int yy = y + dir.y;
				  /* $BNY$N0LCV$,HWLL$N30$J$iD4$Y$J$$(B */
				  if ((0 <= xx) && (xx < WIDTH) && (0 <= yy) && (yy < HEIGHT))
				  {
					 /* $BNY$N0LCV$KAj<j$N6p$,#1$D$G$bB8:_$7$?$i!"$R$C$/$jJV$;$k8uJd$N0LCV$H$9$k(B */
					 ReversiPiece piece_next = m_piece_matrix[xx][yy];
					 if ((piece_next != null) && (piece_next.getType() != piece_type))
					 {
						pos_candidate = new Point(x, y);
						break;
					 }
				  }
			   }

			   /* $B$R$C$/$jJV$;$k8uJd$,8+$D$+$C$?!*(B */
			   if (pos_candidate != null)
			   {
				  /* $BK\Ev$K$R$C$/$jJV$;$k$H$3$m$rC5$7$F3JG<$9$k(B */				  
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
	   * @brief	$B$R$C$/$jJV$;$kAj<j6p$N0LCV$r<hF@$9$k(B
	   * @param [in]	pos   - $B<+J,$N6p$N0LCV(B
	   * @param [in]	piece - $B<+J,$N6p$N<oJL(B
	   * @return	$B$R$C$/$jJV$;$kAj<j6p$N0LCV$NG[Ns!J$J$1$l$P6u$NG[Ns!K(B
	   * @note	$B<+J,$N6p$N0LCV$+$i#8J}8~$r%9%-%c%s$7$F!"$R$C$/$jJV$;$kAj<j$N6p$r(B
	   *        $BA4It%j%9%H%"%C%W$7$FG[Ns$K3JG<$9$k!#(B
	   * @note	$BG[Ns$N=gHV$O!"<+J,$N6p$N0LCV$+$i0lJ}8~$KA\$7$?=gHV$G3JG<$9$k!#(B
	   *        $B!JNc!'(B $B:G=i$O>eJ}8~$NA4It!"<!$K1&<P$a>eJ}8~$NA4It!"$=$N<!$O1&J}8~$NA4It!&!&!&!K(B
	   *        $B$3$&$9$k$3$H$G!"6p$r$R$C$/$jJV$9%"%K%a!<%7%g%s$r$9$k:]$K(B
	   *        $B<+J,$N6p$K6a$$$H$3$m$+$i0lJ}8~$K$R$C$/$jJV$k$h$&$K8+$;$k$3$H$,$G$-$k!#(B
	   */
	  private Vector<Point> getTurnPieces(Point pos, ReversiPiece.Type piece_type)
	  {
		 Vector<Point> pos_turn_pieces = new Vector<Point>();

		 /* $B#8J}8~$9$Y$F$K$D$$$F$R$C$/$jJV$;$kAj<j6p$,$"$k$+C5$7$FG[Ns$K3JG<$9$k(B */
		 for (Point dir : AROUND_8DIR)
		 {
			Point pos_find = new Point(pos);
			Vector<Point> temp_pos_pieces = new Vector<Point>();

			for (int n=0; n<(8-1); n++)  /* $B0lJ}8~$GD4$Y$i$l$k:GBg?t!a(B(8-1) */
			{
			   /* $B0lJ}8~$:$D$R$C$/$jJV$;$kAj<j6p$,$"$k$+C5$9(B */
			   pos_find.translate(dir.x, dir.y);
			   int xx = pos_find.x;
			   int yy = pos_find.y;

			   if ((0 <= xx) && (xx < WIDTH) && (0 <= yy) && (yy < HEIGHT))
			   {
				  ReversiPiece piece = m_piece_matrix[xx][yy];
				  if (piece == null)
				  {
					 break;	/* $B64$a$J$$!*(B $B"*(B $B<!$NJ}8~$X(B */
				  }
				  else
				  {
					 /* $B$R$C$/$jJV$;$k8uJd$N6p$,$"$k!)(B */
					 if (piece.getType() == piece_type)
					 {/* $B<+J,$HF1$8?'$N6p$J$i64$a$k(B
					   * $B"*(B $B$R$C$/$jJV$;$kAj<j6p!J$NG[Ns!K$r3NDj$9$k!J"((B $B6u$N>l9g$b$"$k!K(B */
						pos_turn_pieces.addAll(temp_pos_pieces);
						break;	/* $B0lJ}8~$rD4$Y=*$o$C$?(B $B"*(B $B<!$NJ}8~$X(B */
					 }
					 else
					 { /* $B$R$C$/$jJV$;$k8uJd$N6p$rDI2C$7$F$$$/(B */
						temp_pos_pieces.add(new Point(xx, yy));
					 }
				  }
			   }
			   else
			   {
				  break;	/* $B64$a$J$$!*(B $B"*(B $B<!$NJ}8~$X(B */
			   }
			}
		 }
		 
		 return pos_turn_pieces;
	  }

	  /********************************************************************************
	   * @brief	$BHWLL$rIA2h$9$k(B
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

		 /* $BHWLL$rIA2h$9$k(B */
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

		 /* $BCV$+$l$F$$$k6p$rIA2h$9$k(B */
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

		 /* $B?M4V$N;X$7<j$N;~$KHWLL>e$K%^%&%9%+!<%=%k$,$"$k$J$i!";X$7<j$rIA2h$9$k(B */
		 if (m_isVisibleHand)
		 {
			Point pt_cursor = MouseInfo.getPointerInfo().getLocation();
			Point pt_component = getLocationOnScreen();
			pt_cursor.x -= pt_component.x;
			pt_cursor.y -= pt_component.y;

			Rectangle rect = new Rectangle(size);
			if (rect.contains(pt_cursor))
			{ /* $BHWLL$N%^%9L\$N??$sCf$"$?$j$r%O%s%I$N?M:9$7;X$,;X$9$h$&$K:BI8$rD4@0$9$k(B */
			   Point pos = convertComponentPosToBoardPos(pt_cursor);
			   int x_icon = (pos.x * d) + ((d - m_icon_hand.getIconWidth()) / 2) + 5;
			   int y_icon = (pos.y * d) + ((d - m_icon_hand.getIconHeight()) / 2) + 12;
			   m_icon_hand.paintIcon(this, g, x_icon, y_icon);
			}
		 }
	  }

	  /********************************************************************************
	   * @brief	$B6p$rCV$3$&$H$7$?$3$H$rDLCN$7$?$$%W%l%$%d!<$r%;%C%H$9$k(B
	   * @note	AutoPlayer$B$O<+J,$G6p$rCV$/$N$G%;%C%H$9$kI,MW$O$J$$(B
	   */
	  public void setNotifier(Player player)
	  {
		 m_player_to_notify = player.canAutoPlay() ? null : (HumanPlayer)player;
	  }
}
