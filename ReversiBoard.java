/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class ReversiBoard : リバーシの盤面
 *	＜責務＞
 *	  - 盤面を管理する
 *			(constructor)	盤面を生成する
 *			reset			盤面をリセットする
 *			getPiece		指定された位置にある駒を参照する
 *
 *	  - 駒を配置し描画する
 *			setPiece		盤面に駒を置く
 *			paintComponent	盤面を描画する
 *
 *	  - 駒が置ける場所と、ひっくり返せる駒を調べる
 *			getCandidatePos	指定された色の駒が置ける候補の位置のリストを取得する
 *			getTurnPieces	ひっくり返せる駒を調べる
 *
 *	class ReversiPiece : リバーシの駒
 *	＜責務＞
 *	  - リバーシの駒の属性を保持し描画する
 *			(constructor)	指定された種別の駒を生成する
 *			getType			駒の種別を取得する
 *			rendering		指定された位置に駒を描画する
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;



/*********************************************************************************
 * @brief	リバーシの駒
 */
class ReversiPiece
{
	  /* @brief	駒の種別 { 黒 | 白 } */
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

	  /* @brief	駒の種別を取得する */
	  public Type getType()
	  {
		 return m_type;
	  }

	  /********************************************************************************
	   * @brief	指定された位置に駒を描画する
	   * @param [in]	g    - 描画対象のグラフィックス
	   * @param [in]	pos  - 描画する位置（左上隅）
	   * @param [in]	size - 描画する矩形のサイズ（幅、高さ）
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
 * @brief	リバーシの盤面
 */
class ReversiBoard extends JPanel
{
	  public static final int WIDTH = 8;	/* 盤面の横方向の区画数（８個分） */
	  public static final int HEIGHT = 8;	/* 盤面の縦方向の区画数（８個分） */

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

	  private ReversiPiece[][] m_piece_matrix;	/* 盤面の駒を管理する配列 */

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public ReversiBoard()
	  {
		 m_piece_matrix = new ReversiPiece[8][8]; /* { null | Type.Black | Type.WHITE } */

		 Dimension size = new Dimension(400, 400);	/* 画面上の盤面のサイズ（ピクセル） */
		 setSize(size);
		 setPreferredSize(size);
	  }

	  /********************************************************************************
	   * @brief	盤面をリセットする
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
	   * @brief	指定された位置にある駒を参照する
	   */
	  public ReversiPiece getPiece(int x, int y)
	  {
		 return m_piece_matrix[x][y];
	  }

	  /********************************************************************************
	   * @brief	盤面に駒を置く
	   * @param [in]	x, y  - 駒を置く位置
	   * @param [in]	piece - 置く駒
	   * @return	true:駒を置いた、false:駒を置けない
	   */
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

		 /* 指定された色の駒を置く */
		 m_piece_matrix[x][y] = piece;

		 return true;
	  }

	  /********************************************************************************
	   * @brief	指定された色の駒が置ける候補の位置のリストを取得する
	   * @param [in]	piece_type - 置く駒の種別
	   * @return	ひっくり返せる位置と、ひっくり返せる相手駒の配列のマップ
	   */
	  public HashMap<Point, Vector<Point>> getCandidatePos(ReversiPiece.Type piece_type)
	  {
		 /* ＜駒が置ける場所を探す手順＞
		  *   - 盤面上の全域（８ｘ８）に渡って以下を調べる
		  *     - 調べる位置に駒が置かれていない
		  *       - 調べる位置の上下左右斜めの８方向のいずれかに相手の駒がある
		  *         - 相手の駒の方向に自分の駒がある（端まで順番に見て調べる）
		  *   - ひっくり返せる相手駒の数をカウントして、カウント数の降順に配列をソートする
		  *     （配列の一番最初の要素を打ち手とすれば、一番多く相手駒をひっくり返せる）
		  */

		 /* ひっくり返せる位置と、ひっくり返せる相手駒の配列のマップ */
		 HashMap<Point, Vector<Point>> candidate_pos_map = new HashMap<Point, Vector<Point>>();

		 int x, y;

		 for (y=0; y<HEIGHT; y++)
		 {
			for (x=0; x<WIDTH; x++)
			{
			   ReversiPiece piece_target = m_piece_matrix[x][y];
			   if (piece_target != null)
			   { /* 既に駒が置いてあったら置けない → そこは調べない */
				  continue;
			   }

			   Point pos_candidate = null;

			   for (Point dir : AROUND_8DIR)
			   {
				  /* 指定した位置の隣 */
				  int xx = x + dir.x;
				  int yy = y + dir.y;
				  /* 隣の位置が盤面の外なら調べない */
				  if ((0 <= xx) && (xx < WIDTH) && (0 <= yy) && (yy < HEIGHT))
				  {
					 /* 隣の位置に相手の駒が１つでも存在したら、ひっくり返せる候補の位置とする */
					 ReversiPiece piece_next = m_piece_matrix[xx][yy];
					 if ((piece_next != null) && (piece_next.getType() != piece_type))
					 {
						pos_candidate = new Point(x, y);
						break;
					 }
				  }
			   }

			   /* ひっくり返せる候補が見つかった！ */
			   if (pos_candidate != null)
			   {
				  /* 本当にひっくり返せるところを探して格納する */				  
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
	   * @brief	ひっくり返せる相手駒の位置を取得する
	   * @param [in]	pos   - 自分の駒の位置
	   * @param [in]	piece - 自分の駒の種別
	   * @return	ひっくり返せる相手駒の位置の配列（なければ空の配列）
	   * @note	自分の駒の位置から８方向をスキャンして、ひっくり返せる相手の駒を
	   *        全部リストアップして配列に格納する。
	   * @note	配列の順番は、自分の駒の位置から一方向に捜した順番で格納する。
	   *        （例： 最初は上方向の全部、次に右斜め上方向の全部、その次は右方向の全部・・・）
	   *        こうすることで、駒をひっくり返すアニメーションをする際に
	   *        自分の駒に近いところから一方向にひっくり返るように見せることができる。
	   */
	  private Vector<Point> getTurnPieces(Point pos, ReversiPiece.Type piece_type)
	  {
		 Vector<Point> pos_turn_pieces = new Vector<Point>();

		 /* ８方向すべてについてひっくり返せる相手駒があるか探して配列に格納する */
		 for (Point dir : AROUND_8DIR)
		 {
			Point pos_find = new Point(pos);
			Vector<Point> temp_pos_pieces = new Vector<Point>();

			for (int n=0; n<(8-1); n++)  /* 一方向で調べられる最大数＝(8-1) */
			{
			   /* 一方向ずつひっくり返せる相手駒があるか探す */
			   pos_find.translate(dir.x, dir.y);
			   int xx = pos_find.x;
			   int yy = pos_find.y;

			   if ((0 <= xx) && (xx < WIDTH) && (0 <= yy) && (yy < HEIGHT))
			   {
				  ReversiPiece piece = m_piece_matrix[xx][yy];
				  if (piece == null)
				  {
					 break;	/* 挟めない！ → 次の方向へ */
				  }
				  else
				  {
					 /* ひっくり返せる候補の駒がある？ */
					 if (piece.getType() == piece_type)
					 {/* 自分と同じ色の駒なら挟める
					   * → ひっくり返せる相手駒（の配列）を確定する（※ 空の場合もある） */
						pos_turn_pieces.addAll(temp_pos_pieces);
						break;	/* 一方向を調べ終わった → 次の方向へ */
					 }
					 else
					 { /* ひっくり返せる候補の駒を追加していく */
						temp_pos_pieces.add(new Point(xx, yy));
					 }
				  }
			   }
			   else
			   {
				  break;	/* 挟めない！ → 次の方向へ */
			   }
			}
		 }
		 
		 return pos_turn_pieces;
	  }

	  /********************************************************************************
	   * @brief	盤面を描画する
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

		 /* 盤面を描画する */
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

		 /* 置かれている駒を描画する */
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

	  }
}
