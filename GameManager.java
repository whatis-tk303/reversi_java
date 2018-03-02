/* @file	GameManager.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class GameManager : ゲームの進行を管理する
 *	＜責務＞
 *	  - ゲームの進行を管理する
 *			(constructor)	
 *			getCurrentPlayer	現在のプレイヤーを取得する
 *			start				ゲームを開始する
 *			changePlayer		プレイヤーを交代する
 *			waitPlaying			現在のプレイヤーが駒を置く（あるいは置けないことが確定する）まで待つ
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


/*********************************************************************************
 * @brief	ゲーム進行管理
 */
public class GameManager
{
	  private ReversiBoard m_board;				/** 盤面                       */
	  private Players m_players;				/** プレイヤー２人             */
	  private Player m_current_player;			/** 現在のプレイヤー           */
	  private StatusNotifier m_status_notifier;	/** ゲームステータスを通知する */

	  /********************************************************************************
	   * @brief	constructor
	   * @param [in]	obs - ゲーム進行を監視するオブジェクト
	   */
	  public GameManager(Observer obs)
	  {
		 m_status_notifier = new StatusNotifier();
		 m_status_notifier.addObserver(obs);
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
		 
		 /* ゲームのループ */
		 doLoop();

		 /* このゲームが終了した */
		 /* TODO: 20180218  このゲームが終了したことを何か表示する？ */
		 /* ゲーム終了時のゲームステータスを通知する */
		 m_status_notifier.notify(m_board);
		 System.out.println("ending this game.");
	  }

	  /********************************************************************************
	   * @brief		ゲームのループ
	   */
	  public void doLoop()
	  {
		 int count_fail = 0;	/* 駒が置けなかった場合が連続したかを確認するカウンタ */

		 /* ゲーム中のループ */
		 while(true)
		 {
			/* 人間の指し手の（自動プレイできない）場合はハンドを表示する */
			boolean visible_hand = !m_current_player.canAutoPlay();
			m_board.enableVisibleHand(visible_hand);

			/* 現在のゲームステータスを通知する */
			m_status_notifier.notify(m_board);

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
	   * @brief		現在のプレイヤーが駒を置く（あるいは置けないことが確定する）
	   * @param [in]	player - 現在のプレイヤー
	   * @return		true:駒を置けた、false:駒を置けなかった
	   */
	  private boolean waitPlaying(Player player)
	  {
		 /* ひっくり返せる位置と、ひっくり返せる相手駒の配列のマップをここで
		  * 取得してから think()に渡す */
		 HashMap<Point, Vector<Point>> candidate_pos_map;
		 candidate_pos_map = m_board.getCandidatePos(player.getPieceType());

		 System.out.println(candidate_pos_map); /* for debug: 20180220  ひっくり返せる候補 */

		 /* 現在のプレイヤーが駒を置く位置を考える */
		 m_board.setNotifier(player);
		 Point pos = player.think(candidate_pos_map);
		 System.out.println("done.");  /* for debug: 20180221 */
		 
		 if (pos == null)
		 {
			return false;	/* 駒が置けなかった！ */
		 }

		 /* 指定された位置に自分の駒を置く */
		 ReversiPiece piece = new ReversiPiece(player.getPieceType());
		 m_board.setPiece(pos.x, pos.y, piece);
		 m_board.repaint();

		 /* 相手の駒をひっくり返す */
		 Vector<Point> pos_turn_pieces = candidate_pos_map.get(pos);
		 /* TODO: 20180219  ここで駒をひっくり返す（アニメーションも実行する？）
		  *
		  *	＜アニメーション方法（案）＞
		  *  - 準備として、盤面上のひっくり返す駒すべてについて：
		  *    - 駒タイプを変更する（内部的にひっくり返す）
		  *    - アニメーション属性をに設定する
		  *      （ Rate：0％、From：現在の駒タイプ、To：ひっくり返した後の駒タイプ ）
		  *
		  *  - アニメーションループ：
		  *    - 盤面上のひっくり返す駒すべてについて：
		  *      - アニメーション Rateをインクリメントする（+10％とか）
		  *      - 盤面を再描画する
		  *        - 駒の描画処理にて、アニメーション Rateに応じた描画をする
		  *          （Rateが 50％なら半分ひっくり返したような絵を描画する）
		  *        - 上記をアニメーション Rateが 100％になるまで繰り返す
		  */
		 //for (Point pos_turn : pos_turn_pieces)
		 //{
		 //	m_board.setPiece(pos_turn.x, pos_turn.y, new ReversiPiece(player.getPieceType()));
		 //	m_board.repaint();
		 //	try { Thread.sleep(200); }
		 //	catch(Exception e) {}
		 //}

		 for (Point pos_turn : pos_turn_pieces)
		 {
			piece = new ReversiPiece(player.getPieceType());
			piece.resetAnimation();
		 	m_board.setPiece(pos_turn.x, pos_turn.y, piece);
		 }
		 
		 boolean isAnimating = true;
		 while(isAnimating)
		 {
		 	try { Thread.sleep(100); }
		 	catch(Exception e) {}

			isAnimating = true;
			for (Point pos_turn : pos_turn_pieces)
			{
			   piece = m_board.getPiece(pos_turn.x, pos_turn.y);
			   if (piece.progressAnimation())
			   {
				  isAnimating = false;
			   }
			}

		 	m_board.repaint();
		 }

		 return true;	/* 駒が置けた */
	  }
}
