package bsu.rfe.java.group10.lab3.Slavinsky.varC;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.Dimension;


public class MainFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;

	private JPanel contentPane;
	
	private JFileChooser fileChooser = null;
	
	private JMenuItem saveToTextMenuItem;
	private JMenuItem saveToGraphicsMenuItem;
	private JMenuItem searchValueMenuItem;
	private JMenuItem searchRangeMenuItem;
	private JMenuItem aboutProgramMenuItem;
	private JMenuItem saveToCSVMenuItem;

	private Double[] coefficients;
	
	private Box hBoxResult;
	// Визуализатор ячеек таблицы
	private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
	private GornerTableModel data;
	
	private Box parametersBox;
	private Box buttonsBox;
	
	private JLabel labelForFrom;
	private JLabel labelForTo;
	private JLabel labelForStep;
	
	private JTextField textFieldFrom;
	private JTextField textFieldTo;
	private JTextField textFieldStep;
	
	private Image photo = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Если не задано ни одного аргумента командной строки -
		// Продолжать вычисления невозможно, коэффиценты неизвестны
		if (args.length == 0) {
			System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
			System.exit(-1);
		}
		// Зарезервировать места в массиве коэффициентов столько, сколько аргументов командной строки
		Double[] coefficients = new Double[args.length];
		int i = 0;
		try {
			// Перебрать аргументы, пытаясь преобразовать их в Double
			for (String arg: args) {
				coefficients[i++] = Double.parseDouble(arg);
			}
		}
		catch (NumberFormatException ex) {
			// Если преобразование невозможно - сообщить об ошибке и завершиться
			System.out.println("Ошибка преобразования строки '" + args[i] + "' в число типа Double");
			System.exit(-2);
		}
		// Создать экземпляр главного окна, передав ему коэффициенты
		MainFrame frame = new MainFrame(coefficients);
		// Задать действие, выполняемое при закрытии окна
		frame.setVisible(true);

	}

	/**
	 * Create the frame.
	 */
	public MainFrame(Double[] arg) {
		setFont(new Font("Segoe UI", Font.PLAIN, 12));
		setTitle("Табулирование многочлена на отрезке по схеме Горнера");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - WIDTH)/2,
		(kit.getScreenSize().height - HEIGHT)/2);
		
		coefficients = arg;
		
		//меню
		JMenuBar menuBar = new JMenuBar();
		menuBar.setEnabled(false);
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("Файл");
		fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(fileMenu);
		
		//Пункты подменю
		Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				if (fileChooser == null) {
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) ==
				JFileChooser.APPROVE_OPTION)
				saveToTextFile(fileChooser.getSelectedFile());
			}
		};
		saveToTextMenuItem = fileMenu.add(saveToTextAction);
		saveToTextMenuItem.setEnabled(false);
		saveToTextMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
				/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent event) {
					if (fileChooser == null) {
					// Если экземпляр диалогового окна
					// "Открыть файл" ещѐ не создан,
					// то создать его
					fileChooser = new JFileChooser();
					// и инициализировать текущей директорией
					fileChooser.setCurrentDirectory(new File("."));
					}
					// Показать диалоговое окно
					if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);
					// Если результат его показа успешный,
					// сохранить данные в двоичный файл
					saveToGraphicsFile(fileChooser.getSelectedFile());
				} 
			};
		saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
		saveToGraphicsMenuItem.setEnabled(false);
		saveToGraphicsMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				
		Action saveToCSVAction = new AbstractAction("Сохранить в CSV файл") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				if (fileChooser == null) {
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
				}
				if (fileChooser.showSaveDialog(MainFrame.this) ==
				JFileChooser.APPROVE_OPTION)
				saveToCSVFile(fileChooser.getSelectedFile());
			}
		};
		
		saveToCSVMenuItem = fileMenu.add(saveToCSVAction);
		saveToCSVMenuItem.setEnabled(false);
		saveToCSVMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		
		JMenu tableMenu = new JMenu("Таблица");
		tableMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(tableMenu);
		
		Action searchValueAction = new AbstractAction("Найти значение многочлена") {

			private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent event) {
					// Запросить пользователя ввести искомую строку
					String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
							"Поиск значения", JOptionPane.QUESTION_MESSAGE);
					// Установить введенное значение в качестве иголки
					renderer.setNeedle(value);
					// Обновить таблицу
					getContentPane().repaint();
					}
				};
		
		searchValueMenuItem = tableMenu.add(searchValueAction);
		searchValueMenuItem.setEnabled(false);
		searchValueMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		///!!!!!! Поиск по диапазону!!!!!!!
		Action searchRangeAction = new AbstractAction("Найти диапазон значений многочлена") {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				// Запросить пользователя ввести искомую строку
				String values = JOptionPane.showInputDialog(MainFrame.this, "Введите границы поиска",
						"Поиск значений", JOptionPane.QUESTION_MESSAGE);
				
				String[] args = values.split(" ");
				if (args.length == 0) {
					System.out.println("Невозможно распознать параметры");
					return;
				}
				
				double min;
				double max;
				
				if(args.length != 2) {
					JOptionPane.showMessageDialog(MainFrame.this, "Количество аргументов должно быть 2");
					return;
				}
				
				try {
					min = Double.parseDouble(args[0]);
					max = Double.parseDouble(args[1]);
				}
				catch (NumberFormatException ex) {
					// Если преобразование невозможно - сообщить об ошибке и завершиться
					JOptionPane.showMessageDialog(MainFrame.this, 
							"Ошибка преобразования строки в число типа Double");
					return;
				}
				
				if(min > max) {
					double temp = min;
					min = max;
					max = temp;
				}
				
				renderer.setRange(min, max);
				// Обновить таблицу
				getContentPane().repaint();
				}
			};
		
			searchRangeMenuItem = tableMenu.add(searchRangeAction);
			searchRangeMenuItem.setEnabled(false);
			searchRangeMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
			
		///Информация об авторе
		JMenu infoMenu = new JMenu("Справка");
		infoMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(infoMenu);
		
		Action aboutProgramm = new AbstractAction("О программе") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				JDialog programInfo = new JDialog(MainFrame.this, "О программе", true);
				Box hInfoBox = Box.createHorizontalBox();
				Box vAuthorBox = Box.createVerticalBox();
				
				vAuthorBox.add(Box.createVerticalGlue());
				vAuthorBox.add(new JLabel("Автор:"));
				vAuthorBox.add(new JLabel("Славинский Cергей"));
				vAuthorBox.add(new JLabel("10 группа"));
				vAuthorBox.add(Box.createVerticalGlue());
				
							
				hInfoBox.add(Box.createHorizontalStrut(20));
				hInfoBox.add(vAuthorBox);
				
				File file = new File("W:/imagesForLabs/ProgrammingCat.jpg");
				try {
					photo = ImageIO.read(file);
				}
				catch(Exception e) {}
				
				JPanel photoPanel = new JPanel() {
					private static final long serialVersionUID = 1L;

					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(photo, 0, 10, 160, 200, null);
					}
				};
				
				hInfoBox.add(Box.createHorizontalStrut(10));
				hInfoBox.add(photoPanel);
				//hInfoBox.add(Box.createHorizontalGlue());
				programInfo.add(hInfoBox);
				
				programInfo.setSize(350, 260);
				programInfo.setResizable(false);
				programInfo.setVisible(true);
			}
		};
		
		aboutProgramMenuItem = infoMenu.add(aboutProgramm);
		aboutProgramMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		// основное пространство
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//Создание панели задания параметров
		parametersBox = Box.createHorizontalBox();
		parametersBox.setBorder(null);
		contentPane.add(parametersBox, BorderLayout.NORTH);
		
		parametersBox.add(Box.createHorizontalGlue());
		
		labelForFrom = new JLabel("Х изменяется в пределах от");
		parametersBox.add(labelForFrom);
		
		parametersBox.add(Box.createHorizontalStrut(10));
		
		textFieldFrom = new JTextField("0.0", 10);
		parametersBox.add(textFieldFrom);
		textFieldFrom.setColumns(10);
		
		parametersBox.add(Box.createHorizontalStrut(20));
		
		labelForTo = new JLabel("до");
		parametersBox.add(labelForTo);
		
		parametersBox.add(Box.createHorizontalStrut(10));
		
		textFieldTo = new JTextField("1.0", 10);
		parametersBox.add(textFieldTo);
		textFieldTo.setColumns(10);
		
		parametersBox.add(Box.createHorizontalStrut(20));
		
		labelForStep = new JLabel("с шагом");
		parametersBox.add(labelForStep);
		
		parametersBox.add(Box.createHorizontalStrut(10));
		
		textFieldStep = new JTextField("0.1", 10);
		parametersBox.add(textFieldStep);
		textFieldStep.setColumns(10);
		
		parametersBox.add(Box.createHorizontalGlue());
		
		hBoxResult = Box.createHorizontalBox();
		contentPane.add(hBoxResult);
		
		buttonsBox = Box.createHorizontalBox();
		contentPane.add(buttonsBox, BorderLayout.SOUTH);
		
		buttonsBox.add(Box.createHorizontalGlue());
		
		JButton buttonCalc= new JButton("Вычислить"); 
		buttonCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Считать значения начала и конца отрезка, шага
					Double from = Double.parseDouble(textFieldFrom.getText());
					Double to = Double.parseDouble(textFieldTo.getText());
					Double step = Double.parseDouble(textFieldStep.getText());
					// На основе считанных данных создать новый экземпляр модели таблицы
					data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
					// Создать новый экземпляр таблицы
					JTable table = new JTable(data);
					// Установить в качестве визуализатора ячеек для класса Double разработанный визуализатор
					table.setDefaultRenderer(Double.class, renderer);
					// Установить размер строки таблицы в 30 пикселов
					table.setRowHeight(30);
					
					//contentPane
					hBoxResult.removeAll();
					hBoxResult.add( new JScrollPane(table));
					// Обновить область содержания главного окна
					getContentPane().validate();
					// Пометить ряд элементов меню как доступных
					saveToTextMenuItem.setEnabled(true);
					saveToGraphicsMenuItem.setEnabled(true);
					saveToCSVMenuItem.setEnabled(true);
					searchValueMenuItem.setEnabled(true);
					searchRangeMenuItem.setEnabled(true);
					//отменить поиск
					renderer.setRange(null, null);
					renderer.setNeedle(null);
				} catch (NumberFormatException ex) {
					// В случае ошибки преобразования чисел показать сообщение об ошибке
					JOptionPane.showMessageDialog(MainFrame.this,
					"Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
					JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		
		buttonsBox.add(buttonCalc);
		
		JButton buttonReset = new JButton("Очистить поля");
		buttonReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldFrom.setText("0.0");
				textFieldTo.setText("1.0");
				textFieldStep.setText("0.1");
				// Пометить элементы меню как недоступные
				saveToTextMenuItem.setEnabled(false);
				saveToGraphicsMenuItem.setEnabled(false);
				saveToCSVMenuItem.setEnabled(false);
				searchValueMenuItem.setEnabled(false);
				searchRangeMenuItem.setEnabled(false);
				
				// Обновить область содержания главного окна
				getContentPane().validate();
				}
		});
		
		buttonsBox.add(Box.createRigidArea(new Dimension(20, 40)));
		buttonsBox.add(buttonReset);
		
		buttonsBox.add(Box.createHorizontalGlue());
	}
	
	protected void saveToTextFile(File selectedFile) {
		try {
		// Создать новый символьный поток вывода, направленный в указанный файл
			PrintStream out = new PrintStream(selectedFile);
		// Записать в поток вывода заголовочные сведения
			out.println("Результаты табулирования многочлена по схеме Горнера");
			out.print("Многочлен: ");
			
			for (int i=0; i<coefficients.length; i++) {
				out.print(coefficients[i] + "*X^" + (coefficients.length-i-1));
				
				if (i!=coefficients.length-1)
					out.print(" + ");
			}
			
			out.println("");
			out.println("Интервал от " + data.getFrom() + " до " +
					data.getTo() + " с шагом " + data.getStep());
			out.println("====================================================");
		// Записать в поток вывода значения в точках
			for (int i = 0; i<data.getRowCount(); i++) {
				out.println("Значение в точке " + data.getValueAt(i,0)
					+ " равно " + data.getValueAt(i,1));
			}
		// Закрыть поток
			out.close();
		} catch (FileNotFoundException e) {
		// Исключительную ситуацию "ФайлНеНайден" можно не
		// обрабатывать, так как мы файл создаѐм, а не открываем
		}
		}

	protected void saveToGraphicsFile(File selectedFile) {
		try {
			// Создать новый байтовый поток вывода, направленный в указанный файл
			DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
			// Записать в поток вывода попарно значение X в точке, значение многочлена в точке
			for (int i = 0; i<data.getRowCount(); i++) {
				out.writeDouble((Double)data.getValueAt(i,0));
				out.writeDouble((Double)data.getValueAt(i,1));
			}
			// Закрыть поток вывода
			out.close();
			} catch (Exception e) {
			// Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
			// так как мы файл создаѐм, а не открываем для чтения
			}
		}
	
	protected void saveToCSVFile(File selectedFile) {
		try {
			PrintStream out = new PrintStream(selectedFile);
			
			for (int i = 0; i<data.getRowCount(); i++) {
				out.println(data.getValueAt(i,0) + "," + data.getValueAt(i,1) + "," + data.getValueAt(i,2) + 
						"," + data.getValueAt(i,3));
			}
			out.close();
		}
		catch(FileNotFoundException e) {
			// Исключительную ситуацию "ФайлНеНайден" можно не
			// обрабатывать, так как мы файл создаѐм, а не открываем
		}
	}
}
