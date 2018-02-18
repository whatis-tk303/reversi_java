/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;



/*********************************************************************************
 * @brief	$B%j%P!<%7$N6p(B
 */
class ReversiPiece
{
	  public enum Type {
		 BLACK,
		 WHITE,
	  };

	  private Type m_type;


	  public static ReversiPiece createPiece(Type type)
	  {
		 return new ReversiPiece(type);
	  }

	  /* constructor */
	  public ReversiPiece(Type type)
	  {
		 m_type = type;
	  }

	  /**/
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
	  public static final int WIDTH = 8;
	  public static final int HEIGHT = 8;

	  private ReversiPiece[][] m_piece_matrix;

	  /* constructor */
	  public ReversiBoard()
	  {
		 m_piece_matrix = new ReversiPiece[8][8]; /* { null | Type.Black | Type.WHITE } */

		 Dimension size = new Dimension(400, 400);
		 setSize(size);
		 setPreferredSize(size);
	  }

	  /**/
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

	  /**/
	  public ReversiPiece getPiece(int x, int y)
	  {
		 return m_piece_matrix[x][y];
	  }


	  /* $B;XDj$5$l$??'$N6p$,CV$1$k>l=j$N%j%9%H$r<hF@$9$k(B */
	  public Vector<Point> getAvailablePos(ReversiPiece.Type piece_type)
	  {
		 /* TODO: 20180218 $B;XDj$5$l$??'$N6p$,CV$1$k>l=j$r%j%9%H%"%C%W$7$FG[Ns$K$7$FJV$9(B */
		 return null;
	  }

	  /**/
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
			   System.out.printf("{%d(%d), %d(%d)}\n", x, xx, y, yy); /* for debug: */
			   piece.rendering(g, pos, size_piece);
			}
		 }

	  }

	  /**/
	  public boolean setPiece(int x, int y, ReversiPiece piece)
	  {
		 if ((x < 0) && (WIDTH <= x))
		 {
			return false;
		 }

		 if ((y < 0) && (HEIGHT <= y))
		 {
			return false;
		 }

		 m_piece_matrix[x][y] = piece;

		 return true;
	  }
}
