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


/*********************************************************************************
 * @brief	ゲーム状況を表示するパネル
 */
public class StatusPanel extends JPanel implements Observer
{
	  private JLabel m_label_status;

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public StatusPanel()
	  {
		 add(new JLabel("Game Status:"));

		 m_label_status = new JLabel("???");
		 add(m_label_status);
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

			String str = String.format("B:%d, W:%d", num_black, num_white);
			m_label_status.setText(str);
		 }
	  }

}
