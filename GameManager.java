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
		 turnPirces(pos_turn_pieces, player.getPieceType());

		 return true;	/* 駒が置けた */
	  }

	  /********************************************************************************
	   * @brief		相手の駒をひっくり返す
	   * @param [in]	pos_turn_pieces - ひっくり返す駒の位置の配列
	   * @param [in]	piece_type      - 指し手の駒の種別
	   * @note		駒をひっくり返すアニメーション描画を行う
	   */
	  private void turnPirces(Vector<Point> pos_turn_pieces, ReversiPiece.Type piece_type)
	  {
		 /* 駒が全部ひっくり返るまでアニメーションする */
		 /* ひっくり返す駒のキュー */
		 LinkedList<ReversiPiece> que_piece = new LinkedList<ReversiPiece>();
		 /* アニメーション中の駒のキュー */
		 LinkedList<ReversiPiece> que_remain = new LinkedList<ReversiPiece>();

		 int idx_posary = 0;
		 int count = 0;
		 boolean isAnimating = true;
		 while(isAnimating)
		 {
		 	try { Thread.sleep(20); }
		 	catch(Exception e) {}

			/* 少し間隔をあけて、ひっくり返す駒を順番に登録していく */
			if ((count % 5 == 0) && (idx_posary < pos_turn_pieces.size()))
			{
			   ReversiPiece piece = new ReversiPiece(piece_type);
			   piece.resetAnimation();
			   que_piece.add(piece);
			   Point pos_turn = pos_turn_pieces.get(idx_posary);
			   m_board.setPiece(pos_turn.x, pos_turn.y, piece);
			   idx_posary += 1;
			}
			/* ひっくり返すアニメーションを進める */
			isAnimating = false;
			for (ReversiPiece piece : que_piece)
			{
			   if (!piece.progressAnimation())
			   {
				  que_remain.add(piece);
				  isAnimating = true;
			   }
			}
			/* アニメーション後のボードを描画する */
		 	m_board.repaint();
			/* まだアニメーションが完了していない駒で繰り返す */
			que_piece = que_remain;
			que_remain = new LinkedList<ReversiPiece>();
			count += 1;
		 }
	  }
}
