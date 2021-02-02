import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Vector;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;

import static java.nio.file.StandardCopyOption.*;

public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame();
		myFrame.setSize(1080, 970);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setTitle("Mod Installer");

		JPanel myPanel = new JPanel();
		myPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		myPanel.setLayout(null);
		myFrame.add(myPanel);

		File rootFolder = new File("C:\\Users\\primd\\OneDrive\\Desktop\\IA\\ModLoaderModList");
		boolean dirCreated = rootFolder.mkdir();

		JLabel bgStone = new JLabel();
		bgStone.setBounds(0, 0, 1080, 1080);
		bgStone.setIcon(new ImageIcon("stone.png")); 
		myPanel.add(bgStone);
		bgStone.setLayout(null);

		JLabel bgWood = new JLabel();
		bgWood.setBorder(BorderFactory.createLineBorder(Color.black,4));
		bgWood.setBounds(75, 75, 350, 750);
		bgWood.setIcon(new ImageIcon("oak.png")); 
		bgStone.add(bgWood);
		bgWood.setLayout(null);

		//https://stackoverflow.com/questions/8019792/set-background-image-in-jlabel-label-box-with-dynamic-text/32249934

		File mcModsDir = new File("C:\\Users\\primd\\AppData\\Roaming\\.minecraft\\mods");

		JScrollPane sp = new JScrollPane();
		sp.setBorder(BorderFactory.createLineBorder(Color.black,4));
		sp.setBounds(20, 20, 310, 678);

		//https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
		DefaultTableModel availModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}; 

		JTable modDirs = new JTable();
		availModel.addColumn("Available Mods");
		modDirs.setModel(availModel);
		sp.setViewportView(modDirs);
		bgWood.add(sp); 

		loadData(rootFolder, availModel);

		JButton fileBtn = new JButton("List files");
		fileBtn.setBorder(BorderFactory.createLineBorder(Color.black,4));
		fileBtn.setBackground(new Color(102, 51, 0));
		fileBtn.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 14));
		fileBtn.setForeground(Color.WHITE);
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Minecraft mods", "jar", "rar", "zip");
		fc.setFileFilter(filter);
		File root = new File("C:\\Users\\primd\\Downloads");
		fc.setCurrentDirectory(root);
		fileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(myPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selFile = fc.getSelectedFile();
					availModel.addRow(new Object[] {selFile.getName()});
					File dest = new File(rootFolder.toString() + selFile.getName());
					try {
						Files.copy(selFile.toPath(), dest.toPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
		});
		fileBtn.setBounds(20, 705, 120, 35);
		bgWood.add(fileBtn);
		
		//https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
		//https://docs.oracle.com/javase/6/docs/api/javax/swing/JFileChooser.html#addChoosableFileFilter(javax.swing.filechooser.FileFilter

		JLabel bgWood2 = new JLabel();
		bgWood2.setBorder(BorderFactory.createLineBorder(Color.black,4));
		bgWood2.setBounds(625, 75, 350, 750);
		bgWood2.setIcon(new ImageIcon("oak.png")); //https://stackoverflow.com/questions/8019792/set-background-image-in-jlabel-label-box-with-dynamic-text/32249934
		bgStone.add(bgWood2);
		bgWood2.setLayout(null);

		DefaultTableModel instModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}; 
		JScrollPane sp2 = new JScrollPane();
		sp2.setBorder(BorderFactory.createLineBorder(Color.black,4));
		sp2.setBounds(20, 20, 310, 678);

		JTable instMods = new JTable();
		instModel.addColumn("Installed Mods");
		instMods.setModel(instModel);
		sp2.setViewportView(instMods);
		bgWood2.add(sp2); 

		loadData(mcModsDir, instModel);

		JButton installBtn = new JButton("Install Mod");
		installBtn.setBorder(BorderFactory.createLineBorder(Color.black,4));
		installBtn.setBackground(new Color(102, 51, 0));
		installBtn.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 14));
		installBtn.setForeground(Color.WHITE);
		installBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(modDirs.getSelectedRow() == -1 || (modDirs.getSelectedColumn() == -1)) {
					JOptionPane.showMessageDialog(null, "Select a mod", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					File selectedM = new File(rootFolder + "\\" + modDirs.getValueAt(modDirs.getSelectedRow(), modDirs.getSelectedColumn()).toString());
					File dest = new File((mcModsDir.toString()+"\\") + modDirs.getValueAt(modDirs.getSelectedRow(), modDirs.getSelectedColumn()).toString());
					for(int y = 0; y < instMods.getRowCount(); y=y+1) {
						if(modDirs.getValueAt(modDirs.getSelectedRow(), modDirs.getSelectedColumn()).toString().equals(instMods.getValueAt(y, 0).toString())) {
							JOptionPane.showMessageDialog(null, "Mod already installed", "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								Files.copy(selectedM.toPath(), dest.toPath());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							instModel.addRow(new Object[]{modDirs.getValueAt(modDirs.getSelectedRow(), modDirs.getSelectedColumn()).toString()});
							break;
						}
					}
				} 
			}
		});
		installBtn.setBounds(450, 425, 150, 35);
		bgStone.add(installBtn);

		JButton deleteBtn = new JButton("Delete Mod");
		deleteBtn.setBorder(BorderFactory.createLineBorder(Color.black,4));
		deleteBtn.setBackground(new Color(102, 51, 0));
		deleteBtn.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 14));
		deleteBtn.setForeground(Color.WHITE);
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(instMods.getSelectedRow() == -1 || (instMods.getSelectedColumn() == -1)) {
					JOptionPane.showMessageDialog(null, "Select a mod", "Error", JOptionPane.ERROR_MESSAGE);
				} else { 
					File selectedM = new File((mcModsDir.toString()+"\\") + instMods.getValueAt(instMods.getSelectedRow(), instMods.getSelectedColumn()).toString());
					if(selectedM.delete()) {
						instModel.removeRow(instMods.getSelectedRow());
					} else {
						JOptionPane.showMessageDialog(null, "Unable to delete", "Error", JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		//https://www.geeksforgeeks.org/delete-file-using-java/
		deleteBtn.setBounds(20, 705, 150, 35);
		bgWood2.add(deleteBtn);

		JButton downForge = new JButton("Download Forge");
		downForge.setBorder(BorderFactory.createLineBorder(Color.black,4));
		downForge.setBackground(new Color(102, 51, 0));
		downForge.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 14));
		downForge.setForeground(Color.WHITE);
		downForge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URL("http://files.minecraftforge.net/").toURI());
				} catch (Exception e) {}
			}
		});
		downForge.setBounds(30, 850, 150, 35);
		bgStone.add(downForge);

		JButton installForgeBtn = new JButton("Install Forge");
		installForgeBtn.setBorder(BorderFactory.createLineBorder(Color.black,4));
		installForgeBtn.setBackground(new Color(102, 51, 0));
		installForgeBtn.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 14));
		installForgeBtn.setForeground(Color.WHITE);
		installForgeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(myPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selFile = fc.getSelectedFile();
					try {
						Runtime.getRuntime().exec(" java -jar " + selFile.toString()); 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
		});
		//https://stackoverflow.com/a/4936362
		installForgeBtn.setBounds(450, 850, 150, 35);
		bgStone.add(installForgeBtn);

		JButton playBtn = new JButton("Play");
		playBtn.setBorder(BorderFactory.createLineBorder(Color.black,4));
		playBtn.setBackground(new Color(102, 51, 0));
		playBtn.setFont(new Font("MS Reference Sans Serif", Font.PLAIN, 14));
		playBtn.setForeground(Color.WHITE);
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Runtime.getRuntime().exec("C:\\Program Files (x86)\\Minecraft Launcher\\MinecraftLauncher");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		playBtn.setBounds(850, 850, 150, 35);
		bgStone.add(playBtn);

		myFrame.setVisible(true);
	}

	public static void loadData(File filename, DefaultTableModel model) {
		String[] mods = filename.list();
		for (String mod : mods) {
			model.addRow(new Object[] {mod.toString()});
		}
	}
}

//https://stackoverflow.com/questions/7357969/how-to-use-java-code-to-open-windows-file-explorer-and-highlight-the-specified-f
