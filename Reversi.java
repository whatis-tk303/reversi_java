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
	  private GameSelect.Type m_selected_game;

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
		 JDialog dlg = InfoDialog.createPrepareDialog(this, players, m_selected_game);
		 dlg.setVisible(true);

		 m_selected_game = ((GameSelect)dlg).getSelected();

		 if (m_selected_game == GameSelect.Type.EXIT_GAME)
		 { /* ゲーム終了 */
			System.exit(0);
		 }
		 else if (m_selected_game == GameSelect.Type.HUMAN_VS_HUMAN)
		 { /* "Human(A) vs Human(B)" */
			players.first  = new HumanPlayer(m_board, "Human(B)",    ReversiPiece.Type.BLACK);
			players.second = new HumanPlayer(m_board, "Human(W)",    ReversiPiece.Type.WHITE);
		 }
		 else if (m_selected_game == GameSelect.Type.HUMAN_VS_COMPUTER)
		 { /* "Human vs Computer" */
			players.first  = new HumanPlayer(m_board, "Human(B)",    ReversiPiece.Type.BLACK);
			players.second = new AutoPlayer(m_board,  "Computer(W)", ReversiPiece.Type.WHITE);
		 }
		 else if (m_selected_game == GameSelect.Type.COMPUTER_VS_HUMAN)
		 { /* "Computer vs Human" */
			players.first  = new AutoPlayer(m_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			players.second = new HumanPlayer(m_board, "Human(W)",    ReversiPiece.Type.WHITE);
		 }
		 else if (m_selected_game == GameSelect.Type.COMPUTER_VS_COMPUTER)
		 { /* "Computer(A) vs Computer(B)" */
			players.first  = new AutoPlayer(m_board,  "Computer(B)", ReversiPiece.Type.BLACK);
			players.second = new AutoPlayer(m_board,  "Computer(W)", ReversiPiece.Type.WHITE);
		 }

		 return;
	  }

	  /********************************************************************************
	   * @brief		結果表示： 勝敗を表示する
	   * @return	true:再度ゲームを開始する、false:ゲームを終了する
	   */
	  private boolean showResult(ReversiBoard board, Players players)
	  {
		 /* 再度ゲームを開始する、アプリを終了するのを選択するダイアログ */

		 /* 選択肢｛ 0:再度ゲームを開始する、1:ゲームをやめる ｝ */
		 Object[] options = {"play next game", "exit game"};

		 int num_black = board.countPieces(ReversiPiece.Type.BLACK);
		 int num_white = board.countPieces(ReversiPiece.Type.WHITE);
		 String str_msg;
		 if (num_black == num_white)
		 { /* 引き分け */
			str_msg = String.format("draw");
		 }
		 else
		 {
			str_msg = String.format("%s player won !", (num_white < num_black) ? "BLACK" : "WHITE");
		 }
		 str_msg += "\nDo you continue game ?";

		 int ret = JOptionPane.showOptionDialog(
			this,
			str_msg,
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
		 Dimension size_board = new Dimension(400, 400);	/* 画面上の盤面のサイズ（ピクセル） */

		 m_board = new ReversiBoard(size_board);
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
			m_status_panel.setPlayers(m_players.first, m_players.second);
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
