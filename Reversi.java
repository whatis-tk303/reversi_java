/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class Reversi : リバーシゲームのアプリケーション
 *	＜責務＞
 *	  - リバーシゲームの本体
 *			prepare			準備：先手、後手を決める
 *			showResult		結果表示： 勝敗を表示する
 *			main			ゲームを進行する
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	アプリケーション本体
 */
class Application implements Runnable
{
	  private static JFrame s_frame;
	  private static StatusPanel s_status_panel;
	  private static ReversiBoard s_board;
	  private static GameManager s_game;
	  private static Players s_players;


	  /********************************************************************************
	   * @brief		準備：先手、後手を決める
	   * @param [out]	決定したプレイヤー（先手・後手）を設定して返す
	   */
	  private static void prepare(Players players)
	  {
		 Object[] options = {
			"Human(A) vs Human(B)",
			"Human vs Computer",
			"Computer vs Human",
			"Computer(A) vs Computer(B)",
		 };

		 int ret = JOptionPane.showOptionDialog(
			s_frame,
			"???",
			"select play type:",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);

		 switch(ret)
		 {
			case 0: /* "Human(A) vs Human(B)" */
			   players.first  = new HumanPlayer(s_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(s_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 1: /* "Human vs Computer" */
			   players.first  = new HumanPlayer(s_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(s_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
			case 2: /* "Computer vs Human" */
			   players.first  = new AutoPlayer(s_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(s_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 3: /* "Computer(A) vs Computer(B)" */
			   players.first  = new AutoPlayer(s_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(s_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
		 }

		 System.out.printf("ret = %d\n", ret);
	  }

	  /********************************************************************************
	   * @brief		結果表示： 勝敗を表示する
	   */
	  private static void showResult(ReversiBoard board, Players players)
	  {
		 Object[] options = {"play next game", "exit game"};
		 int ret = JOptionPane.showOptionDialog(
			s_frame,
			"are you continue game ?",
			"play result:",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);

		 if (ret == 1)
		 { /* "exit game" が選択された → このアプリケーションを終了する */
			System.exit(0);
		 }
	  }

	  /*********************************************************************************
	   * @brief		？？？
	   */
	  @Override  /* Runnable */
	  public void run()
	  {
		 s_frame = new JFrame("Reversi");
		 s_board = new ReversiBoard();
		 s_status_panel = new StatusPanel();
		 s_game = new GameManager(s_status_panel);
		 s_players = new Players();

		 s_frame.setLocationByPlatform(true);
		 s_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 s_frame.add(s_status_panel, BorderLayout.NORTH);
		 s_frame.add(s_board, BorderLayout.CENTER);
		 s_frame.pack();
		 s_frame.setVisible(true);

		 while(true)
		 {
			/* 準備：先手、後手を決める */
			prepare(s_players);
			System.out.println(s_players.first);
			System.out.println(s_players.second);

			/* ゲーム開始 */
			s_game.start(s_board, s_players);

			/* 結果表示： 勝敗を表示する */
			showResult(s_board, s_players);
		 }
	  }
}


/*********************************************************************************
 * @brief	メインクラス
 */
public class Reversi
{

	  /********************************************************************************
	   * @brief		アプリケーション・メイン
	   */
	  public static void main(String[] args)
	  {
		 Thread thread = new Thread(new Application());
		 thread.start();
	  }
}
