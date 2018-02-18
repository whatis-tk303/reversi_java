/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	このアプリケーションのウィンドウ
 */
class GameFrame extends JFrame
{
	  public GameFrame(String title)
	  {  
		 super(title);
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
}


/*********************************************************************************
 * @brief	アプリケーション本体
 */
class Reversi
{
	  private static JFrame s_frame;
	  private static ReversiBoard s_board;
	  private static GameManager s_game;
	  private static Players s_players;

	  /* 準備：先手、後手を決める */
	  private static void prepare(Players players)
	  {
		 players.first = new HumanPlayer(s_board, ReversiPiece.Type.BLACK);
		 players.second = new AutoPlayer(s_board, ReversiPiece.Type.WHITE);

		 JOptionPane.showMessageDialog(s_frame,
									   (Object)new JLabel("prepare"),
									   "prepare",
									   JOptionPane.PLAIN_MESSAGE);
	  }

	  /* 結果表示： 勝敗を表示する */
	  private static void showResult(ReversiBoard board, Players players)
	  {
		 Object[] options = {"play next game", "exit game"};
		 int ret = JOptionPane.showOptionDialog(
			s_frame,
			"are you continue game ?",
			"play result",
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

	  /* アプリケーション・メイン*/
	  public static void main(String[] args)
	  {
		 s_frame = new GameFrame("Reversi");
		 s_board = new ReversiBoard();
		 s_game = new GameManager();
		 s_players = new Players();

		 s_frame.add(s_board);
		 s_frame.pack();
		 s_frame.setLocationByPlatform(true);
		 s_frame.setVisible(true);

		 while(true)
		 {
			/* 準備：先手、後手を決める */
			prepare(s_players);
			System.out.println(s_players.first);
			System.out.println(s_players.second);
			//System.exit(1);

			/* ゲーム開始 */
			s_game.start(s_board, s_players);

			/* 結果表示： 勝敗を表示する */
			showResult(s_board, s_players);
		 }
	  }
}
