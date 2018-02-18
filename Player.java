/* @file	Player.java
 * @date	2018-02-18
 * @author	TK303
 */


import java.util.*;
import java.awt.*;


/*********************************************************************************
 * @brief	リバーシのプレイヤー（抽象クラス）
 */
class Player
{
	  protected ReversiBoard m_board;				/* 盤面 */
	  protected ReversiPiece.Type m_piece_type;	/* 自分の駒の色 { BLACK | WHITE }*/

	  /* constructor */
	  public Player(ReversiBoard board, ReversiPiece.Type piece_type)
	  {
		 m_board = board;
		 m_piece_type = piece_type;
	  }


}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（人間）
 * @note	人間が考えて駒を置く場所を教える
 */
class HumanPlayer extends Player
{
	  /* constructor */
	  public HumanPlayer(ReversiBoard board, ReversiPiece.Type piece_type)
	  {
		 super(board, piece_type);
	  }

	  /* 駒を置く場所を教える */
	  public void setPiece()
	  {
		 /* 駒を置ける場所を取得する */
		 Vector<Point> pt = m_board.getAvailablePos(m_piece_type);
	  }
}


/*********************************************************************************
 * @brief	リバーシのプレイヤー（コンピューター）
 * @note	自分で考えて駒を置く
 */
class AutoPlayer extends Player
{
	  /* constructor */
	  public AutoPlayer(ReversiBoard board, ReversiPiece.Type piece_type)
	  {
		 super(board, piece_type);
	  }

	  /* 自分で駒を置く場所を考える */
	  public void think()
	  {
	  }
}
