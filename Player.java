/* @file	Player.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class Player : ゲームのプレイヤーの抽象クラス
 *	＜責務＞
 *	  - プレイヤーができる操作を担う
 *			(constructor)	プレイヤーの名前と先手・後手を指定してプレイヤーを生成する
 *			think			駒を置く場所を考える
 *			getPieceType	駒の種別を取得する
 *
 *	class HumanPlayer : 手動操作をするプレイヤー
 *	＜責務＞
 *	  - 手動操作プレイヤー（人間）ができる操作を担う
 *			(constructor)	
 *			doThink			駒を置く場所を考える（手動で場所を選択する操作を含む）
 *			setPiece		？？？（外部から駒を置く場所を指示する）
 *
 *	class AutoPlayer : 自動操作をするプレイヤー
 *	＜責務＞
 *	  - 自動操作プレイヤー（コンピューター）ができる操作を担う
 *			(constructor)	
 *			doThink			駒を置く場所を考える（盤面状況から自動で判断する）
 *
 *	class Players : ゲームプレイヤー（２人）を保持する
 *	＜責務＞
 *	  - ゲームプレイヤー（２人）を保持する
 *			(constructor)	
 *			setFirst		先手プレイヤーをセットする
 *			setSecond		後手プレイヤーをセットする
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	リバーシのプレイヤー（抽象クラス）
 */
enum PlayerType
{
   HUMAN("player_human.png"),
   COMPUTER("player_computer.png")
   ;

   private String image_file;
   private Icon icon;

   private PlayerType(String fname)
   {
	  this.image_file = fname;
	  this.icon = new ImageIcon(fname);
   }

   /**/
   public Icon getIcon()
   {
	  return this.icon;
   }
}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（抽象クラス）
 */
abstract class Player
{
	  protected ReversiBoard m_board;			/* 盤面 */
	  protected String m_name;					/* プレイヤーの名前 */
	  //protected Type m_player_type;				/* プレイヤー { HUMAN | COMPUTER} */
	  protected PlayerType m_player_type;				/* プレイヤー { HUMAN | COMPUTER} */
	  protected ReversiPiece.Type m_piece_type;	/* 自分の駒の色 { BLACK | WHITE } ※ 黒が先手 */
	  private   boolean m_canAutoPlay;			/* true:自動プレイヤー（コンピューター） */

	  /********************************************************************************
	   * @brief		constructor
	   */
	  public Player(ReversiBoard board, String name, PlayerType player_type, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_name = name;
		 m_player_type = player_type;
		 m_piece_type = piece_type;
		 m_canAutoPlay = (player_type == PlayerType.COMPUTER);
	  }

	  /********************************************************************************
	   * @brief		駒を置く場所を考える
	   * @return	駒を置く場所
	   * @note		駒が置けない場合には、このメソッドが呼ばれることはない
	   */
	  abstract protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map);

	  /********************************************************************************
	   * @brief		駒を置く場所を考える
	   * @return	駒を置く場所、置けない場合は null
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

	  /* @brief		駒の種別を取得する */
	  public ReversiPiece.Type getPieceType()
	  {
		 return m_piece_type;
	  }

	  /* @brief		プレイヤータイプを取得する */
	  public PlayerType getPlayerType()
	  {
		 return m_player_type;
	  }

	  /* @brief	駒の種別を取得する */
	  @Override /*  */
	  public String toString()
	  {
		 return m_name;
	  }

	  /********************************************************************************
	   * @brief		自動プレイかどうか問い合わせる
	   * @return	true: 自動プレイできます
	   */
	  public boolean canAutoPlay()
	  {
		 return m_canAutoPlay;
	  }
}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（人間）
 * @note	人間が考えて駒を置く場所を教える
 */
class HumanPlayer extends Player
{
	  private Point m_pos;
	  private HashMap<Point, Vector<Point>> m_candidate_pos_map;

	  /* @brief	constructor */
	  public HumanPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, PlayerType.HUMAN, piece_type);
	  }

	  /********************************************************************************
	   * @brief	駒を置く場所を考える
	   * @note		駒が置けない場合には、このメソッドが呼ばれることはない
	   */
	  @Override
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 m_pos = null;
		 m_candidate_pos_map = candidate_pos_map;

		 /* 駒が置かれたかどうかをチェックするループ */
		 while(true)
		 {
			if (getPos() != null)
			{
			   break;
			}
			else
			{ /* 何もしない時は少し寝る（CPU負荷を上げないため） */
			   try { Thread.sleep(1); }
			   catch(Exception e) { System.out.println(e); }
			}
		 }

		 return m_pos;
	  }

	  /********************************************************************************
	   * @brief		指定された場所に駒を置けるかを問い合わせる
	   * @param [in]	x, y - 駒を置きたい場所
	   * @return	true: 駒を置ける、false: 置けない
	   */
	  public boolean isAvailablePos(Point pos_place)
	  {
		 ReversiPiece piece = m_board.getPiece(pos_place.x, pos_place.y);
		 /* 盤面上で駒が置かれてない場所なら、駒を置けるかどうかを確認する */
		 if (piece == null)
		 {
			for (Point pos : m_candidate_pos_map.keySet())
			{
			   if (pos.equals(pos_place))
			   {
				  /* ここには駒を置ける */
				  return true;
			   }
			}
		 }

		 /* 駒を置けなかった */
		 return false;
	  }

	  /********************************************************************************
	   * @brief		指定された場所に駒を置く
	   * @param [in]	x, y - 駒を置きたい場所
	   */
	  synchronized public void setPos(Point pos)
	  {
		 m_pos = pos;
	  }


	  /********************************************************************************
	   * @brief		指定された場所に駒を置く
	   * @param [in]	x, y - 駒を置きたい場所
	   */
	  synchronized private Point getPos()
	  {
		 return m_pos;
	  }
}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（コンピューター）
 * @note	自分で考えて駒を置く
 */
class AutoPlayer extends Player
{
	  /********************************************************************************
	  * @brief		constructor
	  */
	  public AutoPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, PlayerType.COMPUTER, piece_type);
	  }

	  /********************************************************************************
	   * @brief		駒を置く場所を考える
	   *			コンピューターは駒を置ける場所を自力で探す
	   * @note		駒が置けない場合には、このメソッドが呼ばれることはない
	   */
	  @Override
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 try { Thread.sleep(500); }
		 catch(Exception e) {}

		 Point pos = null;

		 /* 置いたら有利になるところを調べて、可能なら置く */
		 /* TODO: 20180228  処理が冗長なのでもう少し効率的な書き方にする？ */
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

		 /* 中割りできる（中央の４つを優先的にひっくり返せる）ところ */
		 pos = checkSplitCenter(candidate_pos_map);
		 if (pos != null) { return pos; }

		 /* ４隅とその隣以外で置けるところ */
		 pos = checkAvoidAroundCorner(candidate_pos_map);
		 if (pos != null) { return pos; }

		 /* 良いところに置けない → ランダムで置けるところを選択して置く */
		 int num = candidate_pos_map.size();
		 int idx = (new Random()).nextInt(num);
		 pos = (Point)(candidate_pos_map.keySet().toArray()[idx]);

		 return pos;
	  }

	  /********************************************************************************
	   * @brief		対照軸の駒の位置を作る（4箇所）
	   * @return	対照軸の駒の位置（計4箇所）
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
	   * @brief		駒を置くところを調べる：00：４隅
	   * @return	true: 置ける
	   */
	  private boolean checkAvailablePos_00(Point pos_trgt)
	  {
		 Vector<Point> pos_ary = makeContrastPos(new Point(0, 0));
		 for (Point pos : pos_ary)
		 {
			if (pos_trgt.equals(pos))
			{
			   return true;   /* 駒を置けた */
			}
		 }

		 return false;  /* 駒を置けるところはなかった */
	  }


	  /********************************************************************************
	   * @brief		駒を置くところを調べる：01：４隅の２個隣
	   * @return	true: 置ける
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
			   return true;   /* 駒を置けた */
			}
		 }

		 return false;  /* 駒を置けるところはなかった */
	  }

	  /********************************************************************************
	   * @brief		駒を置くところを調べる：02：？？？
	   * @return	true: 置ける
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
			   return true;   /* 駒を置けた */
			}
		 }

		 return false;  /* 駒を置けるところはなかった */
	  }

	  /********************************************************************************
	   * @brief		中割りできる（中央の４つを優先的にひっくり返せる）ところを調べる
	   * 			なるべく多くひっくり返せるところを選択する
	   * @return	 置ける位置
	   */
	  private Point checkSplitCenter(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos_desided = null;	/* 置ける候補 */
		 Vector<Point> pos_ary = new Vector<Point>();
		 pos_ary.addAll(makeContrastPos(new Point(3, 3)));
		 pos_ary.addAll(makeContrastPos(new Point(3, 1)));
		 pos_ary.addAll(makeContrastPos(new Point(1, 3)));

		 int max_num_place = 0;  /* たくさん置ける数を数えるカウンタ */

		 for (Point pos_candidate : candidate_pos_map.keySet())
		 {
			int num_place = 0;
			for (Point pos_center : pos_ary)
			{
			   for (Point pos_reverse : candidate_pos_map.get(pos_candidate))
			   {
				  System.out.printf("*** 返せる？ {%2d,%2d}=?{%2d,%2d}\n",
									pos_reverse.x, pos_reverse.y, pos_center.x, pos_center.y);
				  if (pos_center.equals(pos_reverse))
				  {
					 num_place += 1;
					 if (max_num_place < num_place)
					 {
						max_num_place = num_place;
						pos_desided = pos_candidate;
					 }
				  }
			   }
			}
		 }

		 return pos_desided;  /* 駒を置きたいところ（nullなら置けない） */
	  }


	  /********************************************************************************
	   * @brief		４隅とその隣には置きたくない
	   * @return	４隅以外で置ける位置
	   */
	  private Point checkAvoidAroundCorner(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos_desided = null;	/* 置ける候補 */
		 Vector<Point> pos_ary = new Vector<Point>();  /* 置きたくない位置  */
		 pos_ary.addAll(makeContrastPos(new Point(1, 1)));
		 pos_ary.addAll(makeContrastPos(new Point(1, 0)));
		 pos_ary.addAll(makeContrastPos(new Point(0, 1)));

		 int max_num_place = 0;  /* たくさん置ける数を数えるカウンタ */

		 for (Point pos_candidate : candidate_pos_map.keySet())
		 {
			int num_place = 0;
			for (Point pos_corner : pos_ary)
			{
			   if (pos_corner.equals(pos_candidate))
			   { /* 置きたくないので置かない */
				  continue;
			   }

			   for (Point pos_reverse : candidate_pos_map.get(pos_candidate))
			   {
				  num_place += 1;
				  if (max_num_place < num_place)
				  {
					 max_num_place = num_place;
					 pos_desided = pos_candidate;
				  }
			   }
			}
		 }

		 return pos_desided;  /* 駒を置きたいところ（nullなら置けない） */
	  }
}


/*********************************************************************************
 * @brief	プレイヤー２人
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
