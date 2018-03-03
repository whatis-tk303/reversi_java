/* @file	Reversi.java
 * @date	2018-02-18
 * @author	TK303
 *
 * @description
 *
 *	class Reversi : ���o�[�V�Q�[���̃A�v���P�[�V����
 *	���Ӗ���
 *	  - ���o�[�V�Q�[���̖{��
 *			prepare			�����F���A�������߂�
 *			showResult		���ʕ\���F ���s��\������
 *			main			�Q�[����i�s����
 */

import java.awt.*;
import javax.swing.*;


/*********************************************************************************
 * @brief	�A�v���P�[�V�����{��
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
	   * @brief		�����F���A�������߂�
	   * @param [out]	���肵���v���C���[�i���E���j��ݒ肵�ĕԂ�
	   */
	  private void prepare(Players players)
	  {
		 /* ���E�������߂�_�C�A���O */
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
	   * @brief		���ʕ\���F ���s��\������
	   * @return	true:�ēx�Q�[�����J�n����Afalse:�Q�[�����I������
	   */
	  private boolean showResult(ReversiBoard board, Players players)
	  {
		 /* �ēx�Q�[�����J�n����A�A�v�����I������̂�I������_�C�A���O */
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

		 return (ret == 0);  /* 0:�ēx�Q�[�����J�n���� */
	  }

	  /*********************************************************************************
	   * @brief		�Q�[��������������
	   */
	  public void init()
	  {
		 Dimension size_board = new Dimension(400, 400);	/* ��ʏ�̔Ֆʂ̃T�C�Y�i�s�N�Z���j */

		 m_board = new ReversiBoard(size_board);
		 m_status_panel = new StatusPanel();
		 m_game = new GameManager(m_status_panel);
		 m_players = new Players();

		 add(m_status_panel, BorderLayout.NORTH);
		 add(m_board, BorderLayout.CENTER);
		 pack();
	  }

	  /*********************************************************************************
	   * @brief		�Q�[�����[�v
	   */
	  @Override  /* Runnable */
	  public void run()
	  {
		 while(true)
		 {
			/* �����F���A�������߂� */
			prepare(m_players);
			System.out.println(m_players.first);
			System.out.println(m_players.second);

			/* �Q�[���J�n */
			m_game.start(m_board, m_players);

			/* ���ʕ\���F ���s��\������ */
			if (!showResult(m_board, m_players))
			{
			   break;  /* �Q�[�����I������ */
			}
		 }
	  }
}


/*********************************************************************************
 * @brief	���C���N���X
 */
public class Reversi
{
	  /********************************************************************************
	   * @brief		�A�v���P�[�V�����E���C��
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
