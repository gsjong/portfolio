package TP;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
	public static void main(String[] args) throws IOException {
		MyFrame mf = new MyFrame();
	}
}

class MyFrame extends JFrame{
	JButton reset;
	JButton number;
	JButton time;
	JButton record;
	int mine = 10;
	int rest = 10;
	int leftclick = 0;
	int rightclick = 0;
	int x = 9;
	int y = 9;
	int z = 1;
	int board[] = new int[9*9];
	int clicked = 0;
	int clickedboard[] = new int[9*9];
	static ArrayList<JButton> box = new ArrayList<JButton>();
	boolean first = true;
	boolean over = false;
	Random r = new Random();
	MultiThread mt;
	BufferedWriter bw;
	BufferedReader br;
	String easyscore;
	String normalscore;
	String hardscore;
	
	MyFrame() throws IOException, NullPointerException{
		try {
			FileInputStream fis = new FileInputStream(new File("./mine.txt"));
			InputStreamReader isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			easyscore = br.readLine();
			normalscore = br.readLine();
			hardscore = br.readLine();
		} catch (FileNotFoundException i) {
			FileOutputStream fos = new FileOutputStream(new File("./mine.txt"));
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			bw.write("1000000\n");
			bw.write("1000000\n");
			bw.write("1000000");
			bw.flush();
			FileInputStream fis = new FileInputStream(new File("./mine.txt"));
			InputStreamReader isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			easyscore = br.readLine();
			normalscore = br.readLine();
			hardscore = br.readLine();
		}
		FileOutputStream fos = new FileOutputStream(new File("./mine.txt"));
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		bw = new BufferedWriter(osw);
		if(easyscore == null || normalscore == null || hardscore == null) {
			bw.write("1000000\n");
			bw.write("1000000\n");
			bw.write("1000000");
			bw.flush();
		}
		else {
			bw.write(easyscore);
			bw.write("\n");
			bw.write(normalscore);
			bw.write("\n");
			bw.write(hardscore);
			bw.flush();
		}
		
		FileInputStream fis = new FileInputStream(new File("./mine.txt"));
		InputStreamReader isr = new InputStreamReader(fis);
		br = new BufferedReader(isr);
		easyscore = br.readLine();
		normalscore = br.readLine();
		hardscore = br.readLine();
		
		setTitle("Minesweeper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		JButton easy = new JButton("초급");
		JButton normal = new JButton("중급");
		JButton hard = new JButton("상급");
		reset = new JButton("●");
		number = new JButton(Integer.toString(rest));
		time = new JButton("0");
		record = new JButton("기록");
		
		easy.setActionCommand("easy");
		easy.addActionListener(new ButtonClickListener());
		normal.setActionCommand("normal");
		normal.addActionListener(new ButtonClickListener());
		hard.setActionCommand("hard");
		hard.addActionListener(new ButtonClickListener());
		reset.setActionCommand("reset");
		reset.addActionListener(new ButtonClickListener());
		record.setActionCommand("record");
		record.addActionListener(new ButtonClickListener());
		
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				JButton b = new JButton();
				b.setBounds(10 + 40*j, 110 + 40*i, 41, 41);
				b.setName(Integer.toString(i*9 + j));
				b.setBackground(new Color(230, 230, 230));
				b.setActionCommand("click");
				b.addActionListener(new ButtonClickListener());
				b.addMouseListener(new RightClickListener());
				box.add(i*9 + j, b);
				add(b);
			}
		}
		
		easy.setBounds(10, 10, 60, 30);
		normal.setBounds(80, 10, 60, 30);
		hard.setBounds(150, 10, 60, 30);
		reset.setBounds(165, 50, 50, 50);
		number.setBounds(10, 50, 100, 50);
		time.setBounds(272, 50, 100, 50);
		record.setBounds(220, 10, 60, 30);
		
		reset.setBackground(new Color(255, 255, 0));
		number.setEnabled(false);
		number.setBackground(new Color(255, 255, 255));
		number.setFont(new Font("Dialog", Font.BOLD, 30));
		time.setEnabled(false);
		time.setBackground(Color.white);
		time.setFont(new Font("Dialog", Font.BOLD, 30));
		
		add(easy);
		add(normal);
		add(hard);
		add(reset);
		add(number);
		add(time);
		add(record);
		
		setSize(397, 520);
		setVisible(true);
	}
	
	class ButtonClickListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			String command = e.getActionCommand();
			JButton a = (JButton) e.getSource();
			if(command.equals("easy")) {
				over = true;
				for(int i = 0; i < box.size(); i++) {
					remove(box.get(i));
				}
				box.removeAll(box);
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 9; j++) {
						JButton b = new JButton();
						b.setBounds(10 + 40*j, 110 + 40*i, 41, 41);
						b.setName(Integer.toString(i*9 + j));
						b.setBackground(new Color(230, 230, 230));
						b.setActionCommand("click");
						b.addActionListener(new ButtonClickListener());
						b.addMouseListener(new RightClickListener());
						box.add(i*9 + j, b);
						add(b);
					}
				}
				first = true;
				over = false;
				leftclick = 0;
				rightclick = 0;
				clicked = 0;
				clickedboard = new int[9*9];
				board = new int[9*9];
				x = 9;
				y = 9;
				z = 1;
				mine = 10;
				rest = 10;
				number.setText(Integer.toString(rest));
				reset.setLocation(165, 50);
				reset.setBackground(new Color(255, 255, 0));
				time.setBounds(272, 50, 100, 50);
				time.setText("0");
				setSize(397, 520);
				try {
					FileInputStream fis = new FileInputStream(new File("./mine.txt"));
					InputStreamReader isr = new InputStreamReader(fis);
					br = new BufferedReader(isr);
					easyscore = br.readLine();
					normalscore = br.readLine();
					hardscore = br.readLine();
				} catch (IOException i) {}
			}
			else if(command.equals("normal")) {
				over = true;
				for(int i = 0; i < box.size(); i++) {
					remove(box.get(i));
				}
				box.removeAll(box);
				for(int i = 0; i < 16; i++) {
					for(int j = 0; j < 16; j++) {
						JButton b = new JButton();
						b.setBounds(10 + 40*j, 110 + 40*i, 41, 41);
						b.setName(Integer.toString(i*16 + j));
						b.setBackground(new Color(230, 230, 230));
						b.setActionCommand("click");
						b.addActionListener(new ButtonClickListener());
						b.addMouseListener(new RightClickListener());
						box.add(i*16 + j, b);
						add(b);
					}
				}
				first = true;
				over = false;
				leftclick = 0;
				rightclick = 0;
				clicked = 0;
				clickedboard = new int[16*16];
				board = new int[16*16];
				x = 16;
				y = 16;
				z = 2;
				mine = 40;
				rest = 40;
				number.setText(Integer.toString(rest));
				reset.setLocation(305, 50);
				reset.setBackground(new Color(255, 255, 0));
				time.setBounds(552, 50, 100, 50);
				time.setText("0");
				setSize(677, 800);
				try {
					FileInputStream fis = new FileInputStream(new File("./mine.txt"));
					InputStreamReader isr = new InputStreamReader(fis);
					br = new BufferedReader(isr);
					easyscore = br.readLine();
					normalscore = br.readLine();
					hardscore = br.readLine();
				} catch (IOException i) {}
			}
			else if(command.equals("hard")) {
				over = true;
				for(int i = 0; i < box.size(); i++) {
					remove(box.get(i));
				}
				box.removeAll(box);
				for(int i = 0; i < 16; i++) {
					for(int j = 0; j < 30; j++) {
						JButton b = new JButton();
						b.setBounds(10 + 40*j, 110 + 40*i, 41, 41);
						b.setName(Integer.toString(i*30 + j));
						b.setBackground(new Color(230, 230, 230));
						b.setActionCommand("click");
						b.addActionListener(new ButtonClickListener());
						b.addMouseListener(new RightClickListener());
						box.add(i*30 + j, b);
						add(b);
					}
				}
				first = true;
				over = false;
				leftclick = 0;
				rightclick = 0;
				clicked = 0;
				clickedboard = new int[30*16];
				board = new int[30*16];
				x = 30;
				y = 16;
				z = 3;
				mine = 99;
				rest = 99;
				number.setText(Integer.toString(rest));
				reset.setLocation(585, 50);
				reset.setBackground(new Color(255, 255, 0));
				time.setBounds(1112, 50, 100, 50);
				time.setText("0");
				setSize(1237, 800);
				try {
					FileInputStream fis = new FileInputStream(new File("./mine.txt"));
					InputStreamReader isr = new InputStreamReader(fis);
					br = new BufferedReader(isr);
					easyscore = br.readLine();
					normalscore = br.readLine();
					hardscore = br.readLine();
				} catch (IOException i) {}
			}
			else if(command.equals("reset")) {
				over = true;
				for(int i = 0; i < y; i++) {
					for(int j = 0; j < x; j++) {
						box.get(i*x + j).setBackground(new Color(230, 230, 230));
						box.get(i*x + j).setText("");
					}
				}
				first = true;
				over = false;
				leftclick = 0;
				rightclick = 0;
				clicked = 0;
				clickedboard = new int[x*y];
				board = new int[x*y];
				if(z == 1) {
					mine = 10;
					rest = 10;
				}
				else if(z == 2) {
					mine = 40;
					rest = 40;
				}
				else if(z == 3) {
					mine = 99;
					rest = 99;
				}
				number.setText(Integer.toString(rest));
				reset.setBackground(new Color(255, 255, 0));
				time.setText("0");
				try {
					FileInputStream fis = new FileInputStream(new File("./mine.txt"));
					InputStreamReader isr = new InputStreamReader(fis);
					br = new BufferedReader(isr);
					easyscore = br.readLine();
					normalscore = br.readLine();
					hardscore = br.readLine();
				} catch (IOException i) {}
			}
			else if(command.equals("record")) {
				System.out.println("기록");
				System.out.print("초급 : " + Integer.parseInt(easyscore) / 1000 + ".");
				if(Integer.parseInt(easyscore) % 1000 < 100)
					System.out.print("0");
				else if(Integer.parseInt(easyscore) % 1000 < 10)
					System.out.print("00");
				System.out.println(Integer.parseInt(easyscore) % 1000 + " 초");
				System.out.print("중급 : " + Integer.parseInt(normalscore) / 1000 + ".");
				if(Integer.parseInt(normalscore) % 1000 < 100)
					System.out.print("0");
				else if(Integer.parseInt(normalscore) % 1000 < 10)
					System.out.print("00");
				System.out.println(Integer.parseInt(normalscore) % 1000 + " 초");
				System.out.print("상급 : " + Integer.parseInt(hardscore) / 1000 + ".");
				if(Integer.parseInt(hardscore) % 1000 < 100)
					System.out.print("0");
				else if(Integer.parseInt(hardscore) % 1000 < 10)
					System.out.print("00");
				System.out.println(Integer.parseInt(hardscore) % 1000 + " 초");
			}
			else if(command.equals("click")) {
				if(over == false) {
					int button = Integer.parseInt(a.getName());
					leftclick += 1;
					if(first == true) {
						for(int i = 0; i < mine; i++) {
							int j = r.nextInt(board.length);
							if(board[j] == 9 || j == button) {
								i--;
							}
							else {
								board[j] = 9;
							}
						}
						for(int i = 0; i < board.length; i++) {
							if(board[i] == 0) {
								if((i / x > 0) && (i % x > 0)) {
									if(board[i - x - 1] == 9) {
										board[i] += 1;
									}
								}
								if(i / x > 0) {
									if(board[i - x] == 9) {
										board[i] += 1;
									}
								}
								if((i / x > 0) && (i % x < x - 1)) {
									if(board[i - x + 1] == 9) {
										board[i] += 1;
									}
								}
								if(i % x < x - 1) {
									if(board[i + 1] == 9) {
										board[i] += 1;
									}
								}
								if((i / x < y - 1) && (i % x < x - 1)) {
									if(board[i + x + 1] == 9) {
										board[i] += 1;
									}
								}
								if(i / x < y - 1) {
									if(board[i + x] == 9) {
										board[i] += 1;
									}
								}
								if((i / x < y - 1) && (i % x > 0)) {
									if(board[i + x - 1] == 9) {
										board[i] += 1;
									}
								}
								if(i % x > 0) {
									if(board[i - 1] == 9) {
										board[i] += 1;
									}
								}
							}
						}
						for(int i = 0; i < board.length; i++) {
							if(board[i] == 1) {
								box.get(i).setForeground(Color.blue);
							}
							else if(board[i] == 2) {
								box.get(i).setForeground(new Color(0, 150, 0));
							}
							else if(board[i] == 3) {
								box.get(i).setForeground(Color.red);
							}
							else if(board[i] == 4) {
								box.get(i).setForeground(new Color(0, 0, 150));
							}
							else if(board[i] == 5) {
								box.get(i).setForeground(new Color(150, 0, 0));
							}
							else if(board[i] == 6) {
								box.get(i).setForeground(new Color(0, 100, 50));
							}
							else if(board[i] == 7) {
								box.get(i).setForeground(Color.black);
							}
							else if(board[i] == 8) {
								box.get(i).setForeground(Color.gray);
							}
							else {
								box.get(i).setForeground(Color.black);
							}
						}
						mt = new MultiThread();
						mt.start();
						first = false;
					}
					if(board[button] == 9) {
						box.get(button).setBackground(new Color(255, 0, 0));
						reset.setBackground(new Color(255, 0, 0));
						for(int i = 0; i < board.length; i++) {
							if(board[i] == 9) {
								box.get(i).setText("x");
							}
							if(board[i] >= 10 && board[i] != 19) {
								box.get(i).setBackground(new Color(255, 200, 200));
							}
						}
						over = true;
					}
					else if(board[button] < 10){
						if(clickedboard[button] == 0) {
							if(board[button] != 0) {
								box.get(button).setText(Integer.toString(board[button]));
							}
							box.get(button).setBackground(new Color(200, 200, 200));
							clickedboard[button] = 1;
							clicked += 1;
							while(true) {
								int n = 0;
								for(int i = 0; i < board.length; i++) {
									if(clickedboard[i] == 1 && board[i] == 0) {
										if((i / x > 0) && (i % x > 0)) {
											if(clickedboard[i - x - 1] == 0 && board[i - x - 1] < 10) {
												n += 1;
												clickedboard[i - x - 1] = 1;
												clicked += 1;
												if(board[i - x - 1] != 0) {
													box.get(i - x - 1).setText(Integer.toString(board[i - x - 1]));
												}
												box.get(i - x - 1).setBackground(new Color(200, 200, 200));
											}
										}
										if(i / x > 0) {
											if(clickedboard[i - x] == 0  && board[i - x] < 10) {
												n += 1;
												clickedboard[i - x] = 1;
												clicked += 1;
												if(board[i - x] != 0) {
													box.get(i - x).setText(Integer.toString(board[i - x]));
												}
												box.get(i - x).setBackground(new Color(200, 200, 200));
											}
										}
										if((i / x > 0) && (i % x < x - 1)) {
											if(clickedboard[i - x + 1] == 0  && board[i - x + 1] < 10) {
												n += 1;
												clickedboard[i - x + 1] = 1;
												clicked += 1;
												if(board[i - x + 1] != 0) {
													box.get(i - x + 1).setText(Integer.toString(board[i - x + 1]));
												}
												box.get(i - x + 1).setBackground(new Color(200, 200, 200));
											}
										}
										if(i % x < x - 1) {
											if(clickedboard[i + 1] == 0 && board[i + 1] < 10) {
												n += 1;
												clickedboard[i + 1] = 1;
												clicked += 1;
												if(board[i + 1] != 0) {
													box.get(i + 1).setText(Integer.toString(board[i + 1]));
												}
												box.get(i + 1).setBackground(new Color(200, 200, 200));
											}
										}
										if((i / x < y - 1) && (i % x < x - 1)) {
											if(clickedboard[i + x + 1] == 0 && board[i + x + 1] < 10) {
												n += 1;
												clickedboard[i + x + 1] = 1;
												clicked += 1;
												if(board[i + x + 1] != 0) {
													box.get(i + x + 1).setText(Integer.toString(board[i + x + 1]));
												}
												box.get(i + x + 1).setBackground(new Color(200, 200, 200));
											}
										}
										if(i / x < y - 1) {
											if(clickedboard[i + x] == 0 && board[i + x] < 10) {
												n += 1;
												clickedboard[i + x] = 1;
												clicked += 1;
												if(board[i + x] != 0) {
													box.get(i + x).setText(Integer.toString(board[i + x]));
												}
												box.get(i + x).setBackground(new Color(200, 200, 200));
											}
										}
										if((i / x < y - 1) && (i % x > 0)) {
											if(clickedboard[i + x - 1] == 0 && board[i + x - 1] < 10) {
												n += 1;
												clickedboard[i + x - 1] = 1;
												clicked += 1;
												if(board[i + x - 1] != 0) {
													box.get(i + x - 1).setText(Integer.toString(board[i + x - 1]));
												}
												box.get(i + x - 1).setBackground(new Color(200, 200, 200));
											}
										}
										if(i % x > 0) {
											if(clickedboard[i - 1] == 0 && board[i - 1] < 10) {
												n += 1;
												clickedboard[i - 1] = 1;
												clicked += 1;
												if(board[i - 1] != 0) {
													box.get(i - 1).setText(Integer.toString(board[i - 1]));
												}
												box.get(i - 1).setBackground(new Color(200, 200, 200));
											}
										}
									}
								}
								if(n == 0)
									break;
							}
						}
						else {
							int m = 0;
							if((button / x > 0) && (button % x > 0)) {
								if(board[button - x - 1] > 9) {
									m += 1;
								}
							}
							if(button / x > 0) {
								if(board[button - x] > 9) {
									m += 1;
								}
							}
							if((button / x > 0) && (button % x < x - 1)) {
								if(board[button - x + 1] > 9) {
									m += 1;
								}
							}
							if(button % x < x - 1) {
								if(board[button + 1] > 9) {
									m += 1;
								}
							}
							if((button / x < y - 1) && (button % x < x - 1)) {
								if(board[button + x + 1] > 9) {
									m += 1;
								}
							}
							if(button / x < y - 1) {
								if(board[button + x] > 9) {
									m += 1;
								}
							}
							if((button / x < y - 1) && (button % x > 0)) {
								if(board[button + x - 1] > 9) {
									m += 1;
								}
							}
							if(button % x > 0) {
								if(board[button - 1] > 9) {
									m += 1;
								}
							}
							if(board[button] == m) {
								if((button / x > 0) && (button % x > 0)) {
									if(clickedboard[button - x - 1] == 0 && board[button - x - 1] < 10) {
										if(board[button - x - 1] == 9) {
											box.get(button - x - 1).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button - x - 1] != 0) {
												box.get(button - x - 1).setText(Integer.toString(board[button - x - 1]));
											}
											box.get(button - x - 1).setBackground(new Color(200, 200, 200));
											clickedboard[button - x - 1] = 1;
											clicked += 1;
										}
									}
								}
								if(button / x > 0) {
									if(clickedboard[button - x] == 0 && board[button - x] < 10) {
										if(board[button - x] == 9) {
											box.get(button - x).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button - x] != 0) {
												box.get(button - x).setText(Integer.toString(board[button - x]));
											}
											box.get(button - x).setBackground(new Color(200, 200, 200));
											clickedboard[button - x] = 1;
											clicked += 1;
										}
									}
								}
								if((button / x > 0) && (button % x < x - 1)) {
									if(clickedboard[button - x + 1] == 0 && board[button - x + 1] < 10) {
										if(board[button - x + 1] == 9) {
											box.get(button - x + 1).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button - x + 1] != 0) {
												box.get(button - x + 1).setText(Integer.toString(board[button - x + 1]));
											}
											box.get(button - x + 1).setBackground(new Color(200, 200, 200));
											clickedboard[button - x + 1] = 1;
											clicked += 1;
										}
									}
								}
								if(button % x < x - 1) {
									if(clickedboard[button + 1] == 0 && board[button + 1] < 10) {
										if(board[button + 1] == 9) {
											box.get(button + 1).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button + 1] != 0) {
												box.get(button + 1).setText(Integer.toString(board[button + 1]));
											}
											box.get(button + 1).setBackground(new Color(200, 200, 200));
											clickedboard[button + 1] = 1;
											clicked += 1;
										}
									}
								}
								if((button / x < y - 1) && (button % x < x - 1)) {
									if(clickedboard[button + x + 1] == 0 && board[button + x + 1] < 10) {
										if(board[button + x + 1] == 9) {
											box.get(button + x + 1).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button + x + 1] != 0) {
												box.get(button + x + 1).setText(Integer.toString(board[button + x + 1]));
											}
											box.get(button + x + 1).setBackground(new Color(200, 200, 200));
											clickedboard[button + x + 1] = 1;
											clicked += 1;
										}
									}
								}
								if(button / x < y - 1) {
									if(clickedboard[button + x] == 0 && board[button + x] < 10) {
										if(board[button + x] == 9) {
											box.get(button + x).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button + x] != 0) {
												box.get(button + x).setText(Integer.toString(board[button + x]));
											}
											box.get(button + x).setBackground(new Color(200, 200, 200));
											clickedboard[button + x] = 1;
											clicked += 1;
										}
									}
								}
								if((button / x < y - 1) && (button % x > 0)) {
									if(clickedboard[button + x - 1] == 0 && board[button + x - 1] < 10) {
										if(board[button + x - 1] == 9) {
											box.get(button + x - 1).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button + x - 1] != 0) {
												box.get(button + x - 1).setText(Integer.toString(board[button + x - 1]));
											}
											box.get(button + x - 1).setBackground(new Color(200, 200, 200));
											clickedboard[button + x - 1] = 1;
											clicked += 1;
										}
									}
								}
								if(button % x > 0) {
									if(clickedboard[button - 1] == 0 && board[button - 1] < 10) {
										if(board[button - 1] == 9) {
											box.get(button - 1).setBackground(new Color(255, 0, 0));
											reset.setBackground(new Color(255, 0, 0));
											for(int i = 0; i < board.length; i++) {
												if(board[i] == 9) {
													box.get(i).setText("x");
												}
												if(board[i] >= 10 && board[i] != 19) {
													box.get(i).setBackground(new Color(255, 200, 200));
												}
											}
											over = true;
										}
										else {
											if(board[button - 1] != 0) {
												box.get(button - 1).setText(Integer.toString(board[button - 1]));
											}
											box.get(button - 1).setBackground(new Color(200, 200, 200));
											clickedboard[button - 1] = 1;
											clicked += 1;
										}
									}
								}
								while(true) {
									int n = 0;
									for(int i = 0; i < board.length; i++) {
										if(clickedboard[i] == 1 && board[i] == 0) {
											if((i / x > 0) && (i % x > 0)) {
												if(clickedboard[i - x - 1] == 0 && board[i - x - 1] < 10) {
													n += 1;
													clickedboard[i - x - 1] = 1;
													clicked += 1;
													if(board[i - x - 1] != 0) {
														box.get(i - x - 1).setText(Integer.toString(board[i - x - 1]));
													}
													box.get(i - x - 1).setBackground(new Color(200, 200, 200));
												}
											}
											if(i / x > 0) {
												if(clickedboard[i - x] == 0  && board[i - x] < 10) {
													n += 1;
													clickedboard[i - x] = 1;
													clicked += 1;
													if(board[i - x] != 0) {
														box.get(i - x).setText(Integer.toString(board[i - x]));
													}
													box.get(i - x).setBackground(new Color(200, 200, 200));
												}
											}
											if((i / x > 0) && (i % x < x - 1)) {
												if(clickedboard[i - x + 1] == 0  && board[i - x + 1] < 10) {
													n += 1;
													clickedboard[i - x + 1] = 1;
													clicked += 1;
													if(board[i - x + 1] != 0) {
														box.get(i - x + 1).setText(Integer.toString(board[i - x + 1]));
													}
													box.get(i - x + 1).setBackground(new Color(200, 200, 200));
												}
											}
											if(i % x < x - 1) {
												if(clickedboard[i + 1] == 0 && board[i + 1] < 10) {
													n += 1;
													clickedboard[i + 1] = 1;
													clicked += 1;
													if(board[i + 1] != 0) {
														box.get(i + 1).setText(Integer.toString(board[i + 1]));
													}
													box.get(i + 1).setBackground(new Color(200, 200, 200));
												}
											}
											if((i / x < y - 1) && (i % x < x - 1)) {
												if(clickedboard[i + x + 1] == 0 && board[i + x + 1] < 10) {
													n += 1;
													clickedboard[i + x + 1] = 1;
													clicked += 1;
													if(board[i + x + 1] != 0) {
														box.get(i + x + 1).setText(Integer.toString(board[i + x + 1]));
													}
													box.get(i + x + 1).setBackground(new Color(200, 200, 200));
												}
											}
											if(i / x < y - 1) {
												if(clickedboard[i + x] == 0 && board[i + x] < 10) {
													n += 1;
													clickedboard[i + x] = 1;
													clicked += 1;
													if(board[i + x] != 0) {
														box.get(i + x).setText(Integer.toString(board[i + x]));
													}
													box.get(i + x).setBackground(new Color(200, 200, 200));
												}
											}
											if((i / x < y - 1) && (i % x > 0)) {
												if(clickedboard[i + x - 1] == 0 && board[i + x - 1] < 10) {
													n += 1;
													clickedboard[i + x - 1] = 1;
													clicked += 1;
													if(board[i + x - 1] != 0) {
														box.get(i + x - 1).setText(Integer.toString(board[i + x - 1]));
													}
													box.get(i + x - 1).setBackground(new Color(200, 200, 200));
												}
											}
											if(i % x > 0) {
												if(clickedboard[i - 1] == 0 && board[i - 1] < 10) {
													n += 1;
													clickedboard[i - 1] = 1;
													clicked += 1;
													if(board[i - 1] != 0) {
														box.get(i - 1).setText(Integer.toString(board[i - 1]));
													}
													box.get(i - 1).setBackground(new Color(200, 200, 200));
												}
											}
										}
									}
									if(n == 0)
										break;
								}
							}
						}
					}
					if(clicked == board.length - mine) {
						reset.setBackground(new Color(0, 255, 0));
						for(int i = 0; i < board.length; i++) {
							if(board[i] == 9) {
								board[i] += 10;
								box.get(i).setText("~");
							}
						}
						rest = 0;
						number.setText(Integer.toString(rest));
						over = true;
						if(z == 1) {
							System.out.println("초급");
							if(Integer.parseInt(easyscore) > mt.ntime) {
								System.out.println("신기록!");
								try {
									FileOutputStream fos = new FileOutputStream(new File("./mine.txt"));
									OutputStreamWriter osw = new OutputStreamWriter(fos);
									bw = new BufferedWriter(osw);
									easyscore = Integer.toString(mt.ntime);
									bw.write(easyscore);
									bw.write("\n");
									bw.write(normalscore);
									bw.write("\n");
									bw.write(hardscore);
									bw.flush();
								} catch (IOException i) {}
							}
						}
						else if(z == 2) {
							System.out.println("중급");
							if(Integer.parseInt(normalscore) > mt.ntime) {
								System.out.println("신기록!");
								try {
									FileOutputStream fos = new FileOutputStream(new File("./mine.txt"));
									OutputStreamWriter osw = new OutputStreamWriter(fos);
									bw = new BufferedWriter(osw);
									normalscore = Integer.toString(mt.ntime);
									bw.write(easyscore);
									bw.write("\n");
									bw.write(normalscore);
									bw.write("\n");
									bw.write(hardscore);
									bw.flush();
								} catch (IOException i) {}
							}
						}
						else if(z == 3) {
							System.out.println("상급");
							if(Integer.parseInt(hardscore) > mt.ntime) {
								System.out.println("신기록!");
								try {
									FileOutputStream fos = new FileOutputStream(new File("./mine.txt"));
									OutputStreamWriter osw = new OutputStreamWriter(fos);
									bw = new BufferedWriter(osw);
									hardscore = Integer.toString(mt.ntime);
									bw.write(easyscore);
									bw.write("\n");
									bw.write(normalscore);
									bw.write("\n");
									bw.write(hardscore);
									bw.flush();
								} catch (IOException i) {}
							}
						}
						System.out.print("시간 : " + mt.ntime / 1000 + ".");
						if(mt.ntime % 1000 < 100)
							System.out.print("0");
						else if(mt.ntime % 1000 < 10)
							System.out.print("00");
						System.out.println(mt.ntime % 1000 + " 초");
						System.out.println("클릭 수 : " + leftclick + " + " + rightclick);
					}
				}
			}
		}
	}
	
	class RightClickListener implements MouseListener{
		public void mouseClicked(MouseEvent e) {

		}
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == 3) {
				JButton a = (JButton) e.getSource();
				int button = Integer.parseInt(a.getName());
				if(over == false && first == false) {
					if(clickedboard[button] == 0) {
						rightclick += 1;
						if(board[button] < 10) {
							board[button] += 10;
							rest -= 1;
							box.get(button).setText("~");
							box.get(button).setForeground(Color.black);
						}
						else if(board[button] >= 10) {
							board[button] -= 10;
							rest += 1;
							box.get(button).setText("");
							if(board[button] == 1) {
								box.get(button).setForeground(Color.blue);
							}
							else if(board[button] == 2) {
								box.get(button).setForeground(new Color(0, 150, 0));
							}
							else if(board[button] == 3) {
								box.get(button).setForeground(Color.red);
							}
							else if(board[button] == 4) {
								box.get(button).setForeground(new Color(0, 0, 150));
							}
							else if(board[button] == 5) {
								box.get(button).setForeground(new Color(150, 0, 0));
							}
							else if(board[button] == 6) {
								box.get(button).setForeground(new Color(0, 100, 50));
							}
							else if(board[button] == 7) {
								box.get(button).setForeground(Color.black);
							}
							else if(board[button] == 8) {
								box.get(button).setForeground(Color.gray);
							}
						}
					}
					number.setText(Integer.toString(rest));
				}
			}
		}
		public void mouseReleased(MouseEvent e) {
			
		}
		public void mouseEntered(MouseEvent e) {
			
		}
		public void mouseExited(MouseEvent e) {
			
		}
	}
	
	class MultiThread extends Thread{
		int ntime;
		MultiThread(){
			
		}
		public void run() {
			try {
				int stime = (int) System.currentTimeMillis();
				while(over == false) {
					ntime = (int) System.currentTimeMillis() - stime;
					time.setText(Integer.toString(ntime / 1000));
				}
			}catch(Exception e) {}
		}
	}
}