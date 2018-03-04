/* @file	InfoDialog.java
 * @date	2018-03-04
 * @author	TK303
 *
 * @description
 *
 *	class InfoDialog : 情報表示ダイアログ
 *	＜責務＞
 *	  - ？？？
 *			???				????
 *			???				????
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/********************************************************************************
 * @brief		ゲーム準備ダイアログの結果を受け渡すインターフェース
 */ 
interface GameSelect
{
	  public enum Type {
		 HUMAN_VS_HUMAN,
		 HUMAN_VS_COMPUTER,
		 COMPUTER_VS_HUMAN,
		 COMPUTER_VS_COMPUTER,
		 EXIT_GAME
	  }

	  public Type getSelected();
}


/********************************************************************************
 * @brief		ゲーム準備ダイアログクラス
 */ 
class PrepareDialog  extends JDialog implements ActionListener, GameSelect
{
	  private GameSelect.Type m_type_selected;	/* 選択されたゲームタイプ */

	  /* ゲームタイプを選択する選択肢のボタン */
	  class GameButton extends JButton
	  {
			public GameSelect.Type m_type;

			/* constructor */
			public GameButton(String label, GameSelect.Type type)
			{
			   super(label);
			   m_type = type;
			}

			/**/
			public GameSelect.Type getType()
			{
			   return m_type;
			}
	  }

	  /********************************************************************************
	   * @brief	constructor
	   */
	  public PrepareDialog(Frame owner, Players players, GameSelect.Type type)
	  {
		 super(owner, true);

		 m_type_selected = (type != null) ? type : GameSelect.Type.EXIT_GAME;

		 Box vbox = new Box(BoxLayout.Y_AXIS);

		 vbox.add(new JLabel("Select play:"));

		 Box box_choice = new Box(BoxLayout.Y_AXIS);
		 ButtonGroup group_btn = new ButtonGroup();

		 /* 先手・後手を決める選択肢 */
		 setSelectButton(group_btn, vbox, "Human(A) vs Human(B)",		GameSelect.Type.HUMAN_VS_HUMAN,       type);
		 setSelectButton(group_btn, vbox, "Human vs Computer",			GameSelect.Type.HUMAN_VS_COMPUTER,    type);
		 setSelectButton(group_btn, vbox, "Computer vs Human",			GameSelect.Type.COMPUTER_VS_HUMAN,    type);
		 setSelectButton(group_btn, vbox, "Computer(A) vs Computer(B)",	GameSelect.Type.COMPUTER_VS_COMPUTER, type);
		 vbox.add(box_choice);

		 /* ゲームを終了するボタン */
		 JButton btn_exit = new JButton("exit game");
		 btn_exit.addActionListener(this);
		 JPanel panel_exit_btn = new JPanel();
		 panel_exit_btn.add(btn_exit);
		 vbox.add(panel_exit_btn);

		 JPanel content = new JPanel();
		 content.add(vbox);

		 setContentPane(content);
		 pack();
		 setLocationRelativeTo(owner);
	  }

	  /* @brief		選択肢ボタンを登録する
	   * @pram [in]	group - ラジオボタングループ
	   * @pram [in]	box   - 画面配置用コンテナ
	   * @pram [in]	str   - 選択肢に表示する文字列
	   * @pram [in]	type  - 設定するゲームタイプ
	   * @pram [in]	selected_type - 前に選択されていたゲームタイプ
	   */
	  private void setSelectButton(ButtonGroup group, Box box, String str, GameSelect.Type type, GameSelect.Type selected_type)
	  {
			GameButton btn_radio = new GameButton(str, type);
			btn_radio.addActionListener(this);
			group.add(btn_radio);
			box.add(btn_radio);

			if (type == selected_type)
			{
			   group.setSelected(btn_radio.getModel(), true);
			   getRootPane().setDefaultButton(btn_radio);
			}
	  }

	  /**/
	  @Override /* ActionListener */
	  public void actionPerformed(ActionEvent e)
	  {
		 Object src = e.getSource();

		 if (src instanceof GameButton)
		 {
			m_type_selected = ((GameButton)src).getType();
			dispose();
		 }
		 else
		 {
			m_type_selected = GameSelect.Type.EXIT_GAME;
			dispose();
		 }
	  }

	  /* 選択されたタイプを取得する */
	  @Override /* GameSelect */
	  public Type getSelected()
	  {
		 return m_type_selected;
	  }
}


/********************************************************************************
 * @brief		ゲーム結果ダイアログクラス
 */
class ResultDialog  extends JDialog
{
	  /********************************************************************************
	   * @brief	constructor
	   */
	  public ResultDialog(Frame owner)
	  {
		 super(owner, true);
	  }
}


/********************************************************************************
 * @brief		情報ダイアログクラス
 */
public class InfoDialog
{
	  /**/
	  public static JDialog createPrepareDialog(Frame owner, Players players, GameSelect.Type type)
	  {
		 return (JDialog)(new PrepareDialog(owner, players, type));
	  }

	  /**/
	  public static JDialog createResultDialog(Frame owner)
	  {
		 return (JDialog)(new ResultDialog(owner));
	  }

}
