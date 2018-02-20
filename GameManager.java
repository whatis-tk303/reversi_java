/* @file	GameManager.java
 * @date	2018-02-18
 * @author	TK303
 */


import java.util.*;
import java.awt.*;


/*********************************************************************************
 * @brief	プレイヤー２人
 */
class Players
{
	  public Player first;
	  public Player second;

	  /* constructor */
	  public Players()
	  {
	  }

	  /**/
	  public void setFirst(Player p)
	  {
		 first = p;
	  }

	  /**/
	  public void setSecond(Player p)
	  {
		 second = p;
	  }
}


/*********************************************************************************
 * @brief	ゲーム進行管理
 */
public class GameManager
{
	  private ReversiBoard m_board;
	  private Players m_players;		/* プレイヤー２人 */
	  private Player m_current_player;	/* 現在のプレイヤー */

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public GameManager()
	  {
		 /* do nothing */
	  }

	  /********************************************************************************
	   * @brief	現在のプレイヤーを取得する
	   */
	  public Player getCurrentPlayer()
	  {
		 return m_current_player;
	  }

	  /********************************************************************************
	   * @brief	ゲームを開始する
	   * @note	１ゲームが終わるまで返って来ない
	   */
	  public void start(ReversiBoard board, Players players)
	  {
		 m_board = board;
		 m_players = players;
		 m_current_player = m_players.first;
		 
		 /* 開始時の駒を置く */
		 m_board.reset();
		 m_board.setPiece(3, 3, new ReversiPiece(ReversiPiece.Type.BLACK));
		 m_board.setPiece(4, 3, new ReversiPiece(ReversiPiece.Type.WHITE));
		 m_board.setPiece(3, 4, new ReversiPiece(ReversiPiece.Type.WHITE));
		 m_board.setPiece(4, 4, new ReversiPiece(ReversiPiece.Type.BLACK));
		 m_board.repaint();
		 
		 try
		 {
			int count = 0;
			int count_fail = 0;	/* 駒が置けなかった場合が連続したかを確認するカウンタ */

			System.out.println("starting this game.");
			while(true)
			{
			   Thread.sleep(1000);  /* for debug: */

			   /* 現在のプレイヤーが駒を置くのを待って、プレイヤーを交代する */
			   System.out.println("- - - - - - - - - - - - - - - - - "); /* for debug: */
			   System.out.printf("%s's turn.\n", m_current_player); /* for debug: */
			   boolean success = waitPlaying(m_current_player);
			   if (success)
			   {
				  count_fail = 0;
			   }
			   else
			   { /* 駒を置けなかった！ */
				  count_fail += 1;
				  if (count_fail == 2)
				  { /* 2回連続で駒を置けなかった → 2人とも駒を置けないのでゲーム終了 */
					 break;
				  }
			   }

			   /* 攻守交替 */
			   m_current_player = changePlayer();
			}
		 }
		 catch(Exception e)
		 {
			System.out.println(e);
		 }

		 /* このゲームが終了した */
		 /* TODO: 20180218  このゲームが終了したことを何か表示する？ */
		 System.out.println("ending this game.");
	  }

	  /********************************************************************************
	   * @brief	プレイヤーを交代する
	   */
	  private Player changePlayer()
	  {
		 return (m_current_player == m_players.first)
			? m_players.second
			: m_players.first;
	  }

	  /********************************************************************************
	   * @brief	現在のプレイヤーが駒を置く（あるいは置けないことが確定する）まで待つ
	   */
	  private boolean waitPlaying(Player player)
	  {
		 /* TODO: 20180220  ひっくり返せる位置と、ひっくり返せる相手駒の配列のマップを
		                    ここで取得してから think()に渡す */
		 HashMap<Point, Vector<Point>> candidate_pos_map;
		 candidate_pos_map = m_board.getCandidatePos(player.getPieceType());

/* #if 1 : for debug: 20180220  ひっくり返せる候補を見てみる */
		 System.out.println(candidate_pos_map);
/* #endif : for debug: 20180220  ひっくり返せる候補を見てみる */

		 /* 現在のプレイヤーが駒を置く位置を考える */
		 /* TODO: 20180220  think()はスレッドで実行し、終了するまで待つ */
		 
		 Point pos = player.think(candidate_pos_map);
		 
		 if (pos != null)
		 { /* 指定された位置に自分の駒を置く */
			ReversiPiece piece = new ReversiPiece(player.getPieceType());
			m_board.setPiece(pos.x, pos.y, piece);
			m_board.repaint();

			/* 相手の駒をひっくり返す */
			Vector<Point> pos_turn = candidate_pos_map.get(pos);
			/* TODO: 20180219  ここで駒をひっくり返す（アニメーションも実行する？） */
			return true;	/* 駒が置けた */
		 }
		 else
		 {
			return false;	/* 駒が置けなかった！ */
		 }
	  }
}
