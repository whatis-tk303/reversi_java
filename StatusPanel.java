/* @file	StatusPanel.java
 * @date	2018-02-25
 * @author	TK303
 *
 * @description
 *
 *	class StatusPanel : ゲーム状況を表示する
 *	＜責務＞
 *	  - ゲーム状況を表示する
 *			???				???
 *			???				???
 *			???				???
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/********************************************************************************
 * @brief	ゲームステータスを通知する
 */
class StatusNotifier extends Observable
{
	  /* @brief		
	   */
	  public void notify(ReversiBoard board)
	  {
		 setChanged();
		 notifyObservers((Object)board);
	  }
}


/********************************************************************************
 * @brief	指し手のプレイヤーを通知する
 */
class PlayerTurnNotifier extends Observable
{
	  /* @brief		
	   */
	  public void notify(Player player)
	  {
		 setChanged();
		 notifyObservers((Object)player);
	  }
}



/*********************************************************************************
 * @brief	プレイヤー情報に駒を表示するパネル
 */
class PiecePanel extends JPanel
{
	  private ReversiPiece m_piece;

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public PiecePanel(ReversiPiece piece)
	  {
		 m_piece = piece;
		 Dimension size = m_piece.getImageSize();
		 setSize(size);
		 setPreferredSize(size);
	  }

	  @Override /* JPanel */
	  protected void paintComponent(Graphics g)
	  {
		 Dimension size = getSize();
		 Point pos = new Point(0, 0);
		 m_piece.rendering(g, pos, size);
	  }
}


/*********************************************************************************
 * @brief	プレイヤー情報を表示するパネル
 */
class PlayerInfoPanel extends JPanel
{
	  private Player m_player;
	  private PiecePanel m_piece_panel;
	  private JLabel m_label_name;
	  private JLabel m_label_num;
	  private boolean m_is_my_turn;

	  /********************************************************************************
	   * @brief		constructor
	   */
	  public PlayerInfoPanel(ReversiPiece piece)
	  {
		 Box hbox = new Box(BoxLayout.X_AXIS);

		 m_piece_panel = new PiecePanel(piece);
		 hbox.add(m_piece_panel);

		 hbox.add(Box.createHorizontalStrut(10));

		 Box vbox = new Box(BoxLayout.Y_AXIS);

		 m_label_name = new JLabel("__________");
		 vbox.add(m_label_name);

		 m_label_num = new JLabel("0");
		 vbox.add(m_label_num);

		 hbox.add(vbox);
		 add(hbox);

		 m_is_my_turn = false;
	  }

	  /********************************************************************************
	   * @brief		プレイヤーを設定する
	   */
	  public void setPlayer(Player player)
	  {
		 m_player = player;
		 m_label_name.setText(player.toString());
	  }

	  /********************************************************************************
	   * @brief		駒数を設定する
	   */
	  public void setPieces(int num)
	  {
		 m_label_num.setText(String.format("%d", num));
	  }

	  /********************************************************************************
	   * @brief		自分が指し手であることを設定する
	   * @param [in]	en - true:自分が指し手である
	   */
	  public void setMyTurn(boolean en)
	  {
		 m_is_my_turn = en;
		 repaint();
	  }

	  /********************************************************************************
	   * @brief		プレイヤー情報の表示を更新描画する
	   * @note		指し手を背景色で報知する
	   */
	  @Override /* JPanel */
	  protected void paintComponent(Graphics g)
	  {
		 Dimension size = getSize();
		 g.setColor(m_is_my_turn ? Color.CYAN : Color.LIGHT_GRAY);
		 g.fillRect(0, 0, size.width, size.height);
	  }
}

/*********************************************************************************
 * @brief	ゲーム状況を表示するパネル
 */
public class StatusPanel extends JPanel implements Observer
{
	  private PlayerInfoPanel m_player_black;
	  private PlayerInfoPanel m_player_white;


	  /********************************************************************************
	   * @brief	constructor
	   */
	  public StatusPanel()
	  {
		 Box hbox = new Box(BoxLayout.X_AXIS);
		 
		 m_player_black = new PlayerInfoPanel(new ReversiPiece(ReversiPiece.Type.BLACK));
		 hbox.add(m_player_black);

		 hbox.add(Box.createHorizontalStrut(100));

		 m_player_white = new PlayerInfoPanel(new ReversiPiece(ReversiPiece.Type.WHITE));
		 hbox.add(m_player_white);

		 add(hbox);
	  }

	  /********************************************************************************
	   * @brief		プレイヤー名を設定する
	   * @param [in]	name_1st - 先手（黒）のプレイヤー名
	   * @param [in]	name_2nd - 後手（白）のプレイヤー名
	   */
	  public void setPlayers(Player player_1st, Player player_2nd)
	  {
		 m_player_black.setPlayer(player_1st);
		 m_player_white.setPlayer(player_2nd);
	  }

	  /********************************************************************************
	   * @brief	ゲームステータスが変化した
	   */
	  @Override /* Observer */
	  public void update(Observable notifier, Object arg)
	  {
		 if (notifier instanceof StatusNotifier)
		 {
			ReversiBoard board = (ReversiBoard)arg;
			int num_black = board.countPieces(ReversiPiece.Type.BLACK);
			int num_white = board.countPieces(ReversiPiece.Type.WHITE);

			m_player_black.setPieces(num_black);
			m_player_white.setPieces(num_white);
		 }
		 else if (notifier instanceof PlayerTurnNotifier)
		 {
			Player player = (Player)arg;

			ReversiPiece.Type type = player.getPieceType();
			boolean is_turn_black = (type == ReversiPiece.Type.BLACK);
			m_player_black.setMyTurn(is_turn_black);
			m_player_white.setMyTurn(!is_turn_black);
		 }
	  }
}
