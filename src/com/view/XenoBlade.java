package com.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.common.Code;
import com.pojo.Member;
import com.service.XenoBladeService;

public class XenoBlade extends JPanel implements ItemListener,ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	// 窗体
	JFrame mainFrame;
	// 组件
	GridBagConstraints constrains = new GridBagConstraints();
	// 布局管理器
	GridBagLayout layout = new GridBagLayout();
	
	JLabel driverName1,driverName2,driverName3;
	JLabel bladeName1,bladeName2,bladeName3;
	JComboBox member1,member2, member3;
	JComboBox blade1_1,blade1_2,blade1_3,blade2_1,blade2_2,blade2_3,blade3_1,blade3_2,blade3_3;
	JButton search;
	JTextArea result;
	
	List<Member> members = new ArrayList<Member>();
	
	public static void main(String[] args) {
		new XenoBlade("XenoBlade2");
	}
	
	public XenoBlade(String title){
		// 01.新建窗口
		mainFrame = new JFrame("XenoBlade2");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 02.配置布局管理器
		setLayout(layout);
		mainFrame.add(this, BorderLayout.BEFORE_FIRST_LINE);
		mainFrame.setSize(500, 800);
		//
		initWindows();
		//
		mainFrame.setResizable(false);
		mainFrame.setVisible(true); 
	}
	
	public void initWindows(){
		//
		driverName1 = new JLabel("Driver1");
		add(constrains, driverName1, 1, 0, 1, 1);
		driverName2 = new JLabel("Driver2");
		add(constrains, driverName2, 2, 0, 1, 1);
		driverName3 = new JLabel("Driver3");
		add(constrains, driverName3, 3, 0, 1, 1);
		//
		bladeName1 = new JLabel("Blade1");
		add(constrains, bladeName1, 0, 2, 1, 1);
		bladeName2 = new JLabel("Blade2");
		add(constrains, bladeName2, 0, 3, 1, 1);
		bladeName3 = new JLabel("Blade3");
		add(constrains, bladeName3, 0, 4, 1, 1);
		// 设置DRIVER初始list
		initMembers();
		// 添加控件及选择事件
		selectMember();
		// 设置BLADE初始list
		setBlades();
		// 
		search = new JButton("Search");
		search.addActionListener(this);
		add(constrains, search, 4, 4, 1, 1);
		//
		result = new JTextArea(25,35);
		add(constrains, result, 5, 0, 5, 1);
	}
	
	/**
	 * 设置Driver
	 */
	public void selectMember(){
		member1 = new JComboBox();
		member1.addItem("");
		member2 = new JComboBox();
		member2.addItem("");
		member3 = new JComboBox();
		member3.addItem("");
		for(Member mem : members){
			member1.addItem(mem.getName());
			member2.addItem(mem.getName());
			member3.addItem(mem.getName());
		}
		//
		member1.setActionCommand("member01");
		member1.setSelectedIndex(1);
		member1.addActionListener(this);
		member2.setActionCommand("member02");
		member2.setSelectedIndex(3);
		member2.addActionListener(this);
		member3.setActionCommand("member03");
		member3.setSelectedIndex(4);
		member3.addActionListener(this);
		//
		add(constrains, member1, 1, 1, 1, 1);
		add(constrains, member2, 2, 1, 1, 1);
		add(constrains, member3, 3, 1, 1, 1);
	}
	
	/**
	 * 设置Blade
	 */
	public void setBlades(){
		//下拉框中存入对象，显示值和实际传递值分离
		Vector model = new Vector();
		// 对象类中需要重写toString()方法
//		model.addElement(new Blade(Code.BLADE_ATTR_FIRE.split(":")[1],Code.BLADE_ATTR_FIRE.split(":")[0]));
		model.addElement(Code.BLADE_ATTR_FIRE);
		model.addElement(Code.BLADE_ATTR_WATER);
		model.addElement(Code.BLADE_ATTR_WIND);
		model.addElement(Code.BLADE_ATTR_ICE);
		model.addElement(Code.BLADE_ATTR_LIGHT);
		model.addElement(Code.BLADE_ATTR_DARK);
		model.addElement(Code.BLADE_ATTR_EARTH);
		model.addElement(Code.BLADE_ATTR_THUNDER);
		
		blade1_1 = new JComboBox(model);
		blade1_1.setSelectedIndex(4);
		blade1_1.addItemListener(this);
		add(constrains, blade1_1, 1, 2, 1, 1);
		blade1_2 = new JComboBox(model);
		blade1_2.setSelectedIndex(1);
		blade1_2.addItemListener(this);
		add(constrains, blade1_2, 1, 3, 1, 1);
		blade1_3 = new JComboBox(model);
		blade1_3.setSelectedIndex(2);
		blade1_3.addItemListener(this);
		add(constrains, blade1_3, 1, 4, 1, 1);
		blade2_1 = new JComboBox(model);
		blade2_1.setSelectedIndex(3);
		blade2_1.addItemListener(this);
		add(constrains, blade2_1, 2, 2, 1, 1);
		blade2_2 = new JComboBox(model);
		blade2_2.setSelectedIndex(0);
		blade2_2.addItemListener(this);
		add(constrains, blade2_2, 2, 3, 1, 1);
		blade2_3 = new JComboBox(model);
		blade2_3.setSelectedIndex(6);
		blade2_3.addItemListener(this);
		add(constrains, blade2_3, 2, 4, 1, 1);
		blade3_1 = new JComboBox(model);
		blade3_1.setSelectedIndex(0);
		blade3_1.addItemListener(this);
		add(constrains, blade3_1, 3, 2, 1, 1);
		blade3_2 = new JComboBox(model);
		blade3_2.setSelectedIndex(5);
		blade3_2.addItemListener(this);
		add(constrains, blade3_2, 3, 3, 1, 1);
		blade3_3 = new JComboBox(model);
		blade3_3.setSelectedIndex(7);
		blade3_3.addItemListener(this);
		add(constrains, blade3_3, 3, 4, 1, 1);
	}
	
	/**
	 * 
	 */
	public void add(GridBagConstraints constrains, JComponent jc,
			int row, int column, int width, int height){
		constrains.gridx = column;
		constrains.gridy = row;
		constrains.gridwidth = width;
		constrains.gridheight = height;
		//设置间隔属性
		constrains.insets = new Insets(10,0,0,20);
		constrains.ipady = 2;
		constrains.anchor = GridBagConstraints.NORTH;
		add(jc,constrains);
	}
	
	public void initMembers(){
		Member relax = new Member();
		relax.setName(Code.DRIVER_NAME_RELAX);
		Member nia = new Member();
		nia.setName(Code.DRIVER_NAME_NIA);
		Member tiger = new Member();
		tiger.setName(Code.DRIVER_NAME_TIGER);
		Member melaf = new Member();
		melaf.setName(Code.DRIVER_NAME_MELAF);
		Member cige = new Member();
		cige.setName(Code.DRIVER_NAME_CIGE);
		
		members.add(relax);
		members.add(nia);
		members.add(tiger);
		members.add(melaf);
		members.add(cige);
	}
	
	@Override
	public void itemStateChanged(ItemEvent item) {
		// 会执行2次，第一次是选择前状态，第二次是选择后状态
//		System.out.println(item.getItem());
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		String eventName = action.getActionCommand();
		if("member01".equals(eventName) || "member02".equals(eventName) ||"member03".equals(eventName)){
			driverSelect(eventName);
		// 点击【Search】
		}else if("Search".equals(eventName)){
			getCombo();
		}
		
	}
	
	/**
	 * @param action
	 */
	public void driverSelect(String eventName){
		String select01 = member1.getSelectedItem().toString();
		String select02 = member2.getSelectedItem().toString();
		String select03 = member3.getSelectedItem().toString();
		if("member01".equals(eventName) && !"".equals(select01) && 
				(select01.equals(select02) || select01.equals(select03))){
			member1.setSelectedIndex(0);
			JOptionPane.showMessageDialog(mainFrame, Code.MSG_SELECT_ERROE, "标题",JOptionPane.WARNING_MESSAGE);
		}else if ("member02".equals(eventName) && !"".equals(select02) && 
				(select02.equals(select01) || select02.equals(select03))){
			member2.setSelectedIndex(0);
			JOptionPane.showMessageDialog(mainFrame, Code.MSG_SELECT_ERROE, "标题",JOptionPane.WARNING_MESSAGE);
		}else if ("member03".equals(eventName) && !"".equals(select02) && 
				(select03.equals(select01) || select03.equals(select02))){
			member3.setSelectedIndex(0);
			JOptionPane.showMessageDialog(mainFrame, Code.MSG_SELECT_ERROE, "标题",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void getCombo(){
		String startAttr = blade1_1.getSelectedItem().toString();
		String endAttr = blade1_3.getSelectedItem().toString();
		String result = new XenoBladeService().getComboByStart(startAttr, endAttr);
		this.result.setText(result);
	}
	
}
