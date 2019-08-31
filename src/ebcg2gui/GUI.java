package ebcg2gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import ebcg2gui.panels.BoardPanel;
import ebcg2gui.panels.InserterPanel;
import ebcg2gui.tools.ConfigLoader;
import ebcg2gui.tools.SaveStateManager;
import ebcg2gui.tools.ServerInfo;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private JPanel contentPane;
	private JPanel serverPanel;
	private JTextField txtBuyNum;
	private JTextField txtCost;
	private JLabel lblServerInfo;
	private JLabel lblPoints;
	private JLabel lblMaxpoints;
	private BoardPanel boardPanel;
	private JTextArea mergeInfoText;
	private InserterPanel inserterPanel;
	
	private ActionListener serverButton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonText = ((JButton) e.getSource()).getText();
			connect(getServerIndex(buttonText));
		}
	};
	
	private ServerInfo[] servers;
	private ServerConnection con;
	private Thread conThread;
	private SaveStateManager ssm;

	public SaveStateManager getSSM() {
		return ssm;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("EBCG2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 20, 700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel panelServerMenu = new JPanel();
		contentPane.add(panelServerMenu, "serverMenu");
		GridBagLayout gbl_panelServerMenu = new GridBagLayout();
		gbl_panelServerMenu.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelServerMenu.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelServerMenu.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelServerMenu.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panelServerMenu.setLayout(gbl_panelServerMenu);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 1;
		gbc_verticalStrut_1.gridy = 0;
		panelServerMenu.add(verticalStrut_1, gbc_verticalStrut_1);
		
		JLabel lblTitle = new JLabel("EBCG2");
		lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 25));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 1;
		gbc_lblTitle.gridy = 1;
		panelServerMenu.add(lblTitle, gbc_lblTitle);
		
		JLabel lblSubtitle = new JLabel("and this horrendous GUI ohmygod why");
		lblSubtitle.setFont(new Font("Serif", Font.PLAIN, 15));
		GridBagConstraints gbc_lblSubtitle = new GridBagConstraints();
		gbc_lblSubtitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubtitle.gridx = 1;
		gbc_lblSubtitle.gridy = 2;
		panelServerMenu.add(lblSubtitle, gbc_lblSubtitle);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 3;
		panelServerMenu.add(verticalStrut, gbc_verticalStrut);
		
		JLabel lblServerSelection = new JLabel("Select a server:");
		lblServerSelection.setFont(new Font("Serif", Font.PLAIN, 15));
		GridBagConstraints gbc_lblServerSelection = new GridBagConstraints();
		gbc_lblServerSelection.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerSelection.gridx = 1;
		gbc_lblServerSelection.gridy = 4;
		panelServerMenu.add(lblServerSelection, gbc_lblServerSelection);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 5;
		panelServerMenu.add(horizontalStrut, gbc_horizontalStrut);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		panelServerMenu.add(scrollPane, gbc_scrollPane);
		
		serverPanel = new JPanel();
		scrollPane.setViewportView(serverPanel);
		serverPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 2;
		gbc_horizontalStrut_1.gridy = 5;
		panelServerMenu.add(horizontalStrut_1, gbc_horizontalStrut_1);
		
		JPanel panelServerButtons = new JPanel();
		GridBagConstraints gbc_panelServerButtons = new GridBagConstraints();
		gbc_panelServerButtons.insets = new Insets(0, 0, 5, 5);
		gbc_panelServerButtons.fill = GridBagConstraints.BOTH;
		gbc_panelServerButtons.gridx = 1;
		gbc_panelServerButtons.gridy = 6;
		panelServerMenu.add(panelServerButtons, gbc_panelServerButtons);
		
		JButton btnAddServer = new JButton("Add a server");
		panelServerButtons.add(btnAddServer);
		
		JButton btnRemoveServer = new JButton("Remove server");
		panelServerButtons.add(btnRemoveServer);
		
		JPanel panelInGame = new JPanel();
		contentPane.add(panelInGame, "inGame");
		GridBagLayout gbl_panelInGame = new GridBagLayout();
		gbl_panelInGame.columnWidths = new int[]{0, 0, 0};
		gbl_panelInGame.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelInGame.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panelInGame.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelInGame.setLayout(gbl_panelInGame);
		
		lblPoints = new JLabel("POINTS");
		GridBagConstraints gbc_lblPoints = new GridBagConstraints();
		gbc_lblPoints.anchor = GridBagConstraints.WEST;
		gbc_lblPoints.insets = new Insets(0, 0, 5, 5);
		gbc_lblPoints.gridx = 0;
		gbc_lblPoints.gridy = 1;
		panelInGame.add(lblPoints, gbc_lblPoints);
		lblPoints.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblMaxpoints = new JLabel("MAXPOINTS");
		GridBagConstraints gbc_lblMaxpoints = new GridBagConstraints();
		gbc_lblMaxpoints.anchor = GridBagConstraints.WEST;
		gbc_lblMaxpoints.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaxpoints.gridx = 0;
		gbc_lblMaxpoints.gridy = 2;
		panelInGame.add(lblMaxpoints, gbc_lblMaxpoints);
		lblMaxpoints.setFont(new Font("Serif", Font.PLAIN, 15));
		
		boardPanel = new BoardPanel();
		GridBagConstraints gbc_boardPanel = new GridBagConstraints();
		gbc_boardPanel.fill = GridBagConstraints.BOTH;
		gbc_boardPanel.insets = new Insets(0, 0, 5, 5);
		gbc_boardPanel.gridx = 0;
		gbc_boardPanel.gridy = 3;
		panelInGame.add(boardPanel, gbc_boardPanel);
		boardPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		mergeInfoText = new JTextArea();
		GridBagConstraints gbc_mergeInfoText = new GridBagConstraints();
		gbc_mergeInfoText.fill = GridBagConstraints.BOTH;
		gbc_mergeInfoText.insets = new Insets(0, 0, 5, 0);
		gbc_mergeInfoText.gridx = 1;
		gbc_mergeInfoText.gridy = 3;
		panelInGame.add(mergeInfoText, gbc_mergeInfoText);
		mergeInfoText.setFont(new Font("FreeMono", Font.PLAIN, 15));
		mergeInfoText.setEditable(false);
		mergeInfoText.setText("00\n---------\n2x = 0000\n3x = 0000\n4x = 0000\n---------\n2x = 0000\n3x = 0000\n4x = 0000");
		
		inserterPanel = new InserterPanel();
		GridBagConstraints gbc_inserterPanel = new GridBagConstraints();
		gbc_inserterPanel.fill = GridBagConstraints.BOTH;
		gbc_inserterPanel.insets = new Insets(0, 0, 5, 5);
		gbc_inserterPanel.gridx = 0;
		gbc_inserterPanel.gridy = 4;
		panelInGame.add(inserterPanel, gbc_inserterPanel);
		inserterPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		JPanel panelShop = new JPanel();
		GridBagConstraints gbc_panelShop = new GridBagConstraints();
		gbc_panelShop.insets = new Insets(0, 0, 5, 0);
		gbc_panelShop.gridx = 1;
		gbc_panelShop.gridy = 4;
		panelInGame.add(panelShop, gbc_panelShop);
		panelShop.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		txtBuyNum = new JTextField();
		txtBuyNum.setFont(new Font("Serif", Font.PLAIN, 15));
		panelShop.add(txtBuyNum);
		txtBuyNum.setColumns(2);
		
		txtCost = new JTextField();
		txtCost.setFont(new Font("Serif", Font.PLAIN, 15));
		txtCost.setEditable(false);
		panelShop.add(txtCost);
		txtCost.setColumns(4);
		
		JButton btnBuy = new JButton("Buy");
		panelShop.add(btnBuy);
		
		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 5;
		panelInGame.add(progressBar, gbc_progressBar);
		progressBar.setToolTipText("You have found the secret tooltip!");
		progressBar.setValue(100);
		
		lblServerInfo = new JLabel("SERVERINFO");
		GridBagConstraints gbc_lblServerInfo = new GridBagConstraints();
		gbc_lblServerInfo.gridwidth = 2;
		gbc_lblServerInfo.insets = new Insets(0, 0, 5, 0);
		gbc_lblServerInfo.gridx = 0;
		gbc_lblServerInfo.gridy = 6;
		panelInGame.add(lblServerInfo, gbc_lblServerInfo);
		lblServerInfo.setFont(new Font("Serif", Font.BOLD, 15));
		
		JPanel panelGameButtons = new JPanel();
		GridBagConstraints gbc_panelGameButtons = new GridBagConstraints();
		gbc_panelGameButtons.gridwidth = 2;
		gbc_panelGameButtons.gridx = 0;
		gbc_panelGameButtons.gridy = 7;
		panelInGame.add(panelGameButtons, gbc_panelGameButtons);
		panelGameButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnSaveGame = new JButton("Save game");
		panelGameButtons.add(btnSaveGame);
		
		JButton btnQuitGame = new JButton("Quit game");
		panelGameButtons.add(btnQuitGame);
		
		JButton btnLoadGame = new JButton("Load game");
		panelGameButtons.add(btnLoadGame);
		
		JButton btnStopServer = new JButton("Stop server");
		panelGameButtons.add(btnStopServer);
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnQuitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopGame("quitgame ");
			}
		});
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopGame("quitgame ");
				JOptionPane.showMessageDialog(null, "TODO"); // TODO list games
			}
		});
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopGame("stopserver ");
			}
		});
		
		loadServers();
		ssm = new SaveStateManager(this);
		
		// Action listeners
		
		btnAddServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String hostname = JOptionPane.showInputDialog("Enter hostname:");
					int port = Integer.parseInt(JOptionPane.showInputDialog("Enter port:"));
					
					ServerInfo server = new ServerInfo(hostname, port);
					ConfigLoader.addServer(server);
					loadServers();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Invalid data!");
				}
				
			}
		});
		btnRemoveServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int index = Integer.parseInt(JOptionPane.showInputDialog("Index to remove (first is 0):"));
					
					if(index >= 0 && index < serverPanel.getComponentCount()) {
						ConfigLoader.removeServer(index);
						loadServers();
					}
				} catch(Exception ex) {JOptionPane.showMessageDialog(null, "Invalid data!");}
			}
		});
		
	}
	
	public void loadServers() {
		ConfigLoader.load();
		servers = ConfigLoader.getServers();
		
		serverPanel.removeAll();
		for(int i=0; i<servers.length; i++) {
			String title = servers[i].hostname + ":" + Integer.toString(servers[i].port);
			
			JButton btn = new JButton(title);
			serverPanel.add(btn);
			btn.addActionListener(serverButton);
		}
		
		serverPanel.updateUI();
	}
	
	public int getServerIndex(String serverDesc) {
		String[] data = serverDesc.split(":");
		int port = Integer.parseInt(data[1]);
		
		for(int i=0; i<servers.length; i++) {
 			if(servers[i].hostname.equals(data[0]) && servers[i].port == port) {
 				return i;
 			}
		}
		
		return -1;
	}
	
	public void connect(int serverIndex) {
		con = new ServerConnection(servers[serverIndex].hostname, servers[serverIndex].port, this);
		con.openConnection();
	}
	
	public void startGame() {
		((CardLayout) contentPane.getLayout()).show(contentPane, "inGame");
		lblPoints.setText("Points: 0");
		lblMaxpoints.setText("Total points: 0");
		mergeInfoText.setText("");
		
		conThread = new Thread(con);
		conThread.start();
	}
	
	public void stopGame(String command) {
		int opt = JOptionPane.showConfirmDialog(null, "Save game?", "Save game", JOptionPane.YES_NO_OPTION);
		if(opt == JOptionPane.YES_NO_OPTION) {
			String name = JOptionPane.showInputDialog("Enter name:");
			if(name.length() > 0) {
				con.send("savegame " + name + " ");
			}
		}
		
		con.send(command);
		con.gracefulExit();
		((CardLayout) contentPane.getLayout()).show(contentPane, "serverMenu");
		contentPane.updateUI();
	}
	
	public JLabel getLblServerInfo() {
		return lblServerInfo;
	}
	public JLabel getLblPoints() {
		return lblPoints;
	}
	public JLabel getLblMaxpoints() {
		return lblMaxpoints;
	}
	public BoardPanel getBoardPanel() {
		return boardPanel;
	}
	public InserterPanel getInserterPanel() {
		return inserterPanel;
	}
	public JTextArea getMergeInfoText() {
		return mergeInfoText;
	}
}
