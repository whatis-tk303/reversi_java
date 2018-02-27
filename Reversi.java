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

import java.awt.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	アプリケーション本体
 */
class Application extends JFrame implements Runnable
{
	  private StatusPanel m_status_panel;
	  private ReversiBoard m_board;
	  private GameManager m_game;
	  private Players m_players;


	  /********************************************************************************
	   * @brief		constructor
	   */
	  public Application(String title)
	  {
		 super(title);
	  }

	  /********************************************************************************
	   * @brief		準備：先手、後手を決める
	   * @param [out]	決定したプレイヤー（先手・後手）を設定して返す
	   */
	  private void prepare(Players players)
	  {
		 /* 先手・後手を決めるダイアログ */
		 Object[] options = {
			"Human(A) vs Human(B)",
			"Human vs Computer",
			"Computer vs Human",
			"Computer(A) vs Computer(B)",
		 };

		 int ret = JOptionPane.showOptionDialog(
			this,
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
			   players.first  = new HumanPlayer(m_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(m_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 1: /* "Human vs Computer" */
			   players.first  = new HumanPlayer(m_board, "Human(B)",    ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(m_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
			case 2: /* "Computer vs Human" */
			   players.first  = new AutoPlayer(m_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new HumanPlayer(m_board, "Human(W)",    ReversiPiece.Type.WHITE);
			   break;
			case 3: /* "Computer(A) vs Computer(B)" */
			   players.first  = new AutoPlayer(m_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			   players.second = new AutoPlayer(m_board,  "Computer(W)", ReversiPiece.Type.WHITE);
			   break;
		 }

		 System.out.printf("ret = %d\n", ret);
	  }

	  /********************************************************************************
	   * @brief		結果表示： 勝敗を表示する
	   * @return	true:再度ゲームを開始する、false:ゲームを終了する
	   */
	  private boolean showResult(ReversiBoard board, Players players)
	  {
		 /* 再度ゲームを開始する、アプリを終了するのを選択するダイアログ */
		 Object[] options = {"play next game", "exit game"};

		 int ret = JOptionPane.showOptionDialog(
			this,
			"are you continue game ?",
			"play result:",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.WARNING_MESSAGE,
			null,
			options,
			options[0]);

		 return (ret == 0);  /* 0:再度ゲームを開始する */
	  }

	  /*********************************************************************************
	   * @brief		ゲームを初期化する
	   */
	  public void init()
	  {
		 m_board = new ReversiBoard();
		 m_status_panel = new StatusPanel();
		 m_game = new GameManager(m_status_panel);
		 m_players = new Players();

		 add(m_status_panel, BorderLayout.NORTH);
		 add(m_board, BorderLayout.CENTER);
		 pack();
	  }

	  /*********************************************************************************
	   * @brief		ゲームループ
	   */
	  @Override  /* Runnable */
	  public void run()
	  {
		 while(true)
		 {
			/* 準備：先手、後手を決める */
			prepare(m_players);
			System.out.println(m_players.first);
			System.out.println(m_players.second);

			/* ゲーム開始 */
			m_game.start(m_board, m_players);

			/* 結果表示： 勝敗を表示する */
			if (!showResult(m_board, m_players))
			{
			   break;  /* ゲームを終了する */
			}
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
		 Application app = new Application("Reversi");
		 app.setLocationByPlatform(true);
		 app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 app.init();
		 app.setVisible(true);

		 Thread thread = new Thread(app);
		 try
		 {
			thread.start();
			thread.join();
		 }
		 catch(Exception e)
		 {
			System.out.println(e);
		 }

		 System.exit(0);
	  }
}
