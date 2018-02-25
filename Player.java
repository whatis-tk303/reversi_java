/* @file	Player.java
 * @date	2018-02-18
 * @author	TK303
 */


import java.util.*;
import java.awt.*;


/*********************************************************************************
 * @brief	リバーシのプレイヤー（抽象クラス）
 */
abstract class Player
{
	  public enum Type {
		 HUMAN,
		 COMPUTER
	  }

	  protected ReversiBoard m_board;			/* 盤面 */
	  protected String m_name;					/* プレイヤーの名前 */
	  protected Type m_player_type;				/* プレイヤー { HUMAN | COMPUTER} */
	  protected ReversiPiece.Type m_piece_type;	/* 自分の駒の色 { BLACK | WHITE } ※ 黒が先手 */

	  /* @brief	constructor */
	  public Player(ReversiBoard board, String name, Player.Type player_type, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_name = name;
		 m_player_type = player_type;
		 m_piece_type = piece_type;
	  }

	  /* @brief	駒を置く場所を考える
	   * @return	駒を置く場所、置けない場合は null */
	  public Point think(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 try /* for debug: 20180221 時間のかかる処理（ユーザー入力など）をシミュレート */
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

	  /* @brief	駒の種別を取得する */
	  public ReversiPiece.Type getPieceType()
	  {
		 return m_piece_type;
	  }

	  /* @brief	駒の種別を取得する */
	  @Override /*  */
	  public String toString()
	  {
		 return m_name;
	  }

	  /* @brief	駒を置く場所を考える
	   * @return	駒を置く場所、置けない場合は null */
	  abstract protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map);
}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（人間）
 * @note	人間が考えて駒を置く場所を教える
 */
class HumanPlayer extends Player
{
	  /* @brief	constructor */
	  public HumanPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, Player.Type.HUMAN, piece_type);
	  }

	  /* @brief	駒を置く場所を考える */
	  @Override
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos = null;
		 /* TODO: 20180218  人間が駒を置ける場所を教えるまで処理を返さない（？） */
		 return pos;
	  }

	  /* @brief	（人間が）駒を置く場所を教える */
	  public void setPiece(int x, int y)
	  {
		 /* TODO: 20180220  駒を置ける場所を取得する */
	  }
}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（コンピューター）
 * @note	自分で考えて駒を置く
 */
class AutoPlayer extends Player
{
	  /* @brief	constructor */
	  public AutoPlayer(ReversiBoard board, String name, ReversiPiece.Type piece_type)
	  {
		 super(board, name, Player.Type.COMPUTER, piece_type);
	  }

	  /* @brief	駒を置く場所を考える */
	  @Override
	  protected Point doThink(HashMap<Point, Vector<Point>> candidate_pos_map)
	  {
		 Point pos = null;
		 /* TODO: 20180218  コンピューターは駒を置ける場所を自力で探す
		  * （今はとりあえずランダムで置けるところを選択して置く） */
		 int num = candidate_pos_map.size();
		 int idx = (new Random()).nextInt(num);
		 pos = (Point)(candidate_pos_map.keySet().toArray()[idx]);

		 return pos;
	  }
}
