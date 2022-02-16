package com.application.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.application.jdbc.SqliteUtil;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 4783178871399676541L;

    private static final int BASE_WIDTH = 30;

    private static int startX, defaultX = 10;

    private static int startY, defaultY = 10;

    private JButton btn1, btn2, btn3;
    private JTextArea result;

    public static void main(String[] args) {
        new MainFrame("代码生成工具");
    }

    MainFrame(String str) {
        super(str);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        int width = 3 * BASE_WIDTH;
        int height = BASE_WIDTH;
        btn1 = new JButton("执行SQL");
        btn1.setBounds(startX, startY, width, height);
        startX += width + 20;
        btn1.addActionListener(eventBtn1());
        add(btn1);

        btn2 = new JButton("生成代码");
        btn2.setBounds(startX, startY, width, height);
        startX += width + 20;
        add(btn2);
        btn2.addActionListener(eventBtn2());

        btn3 = new JButton("生成文件夹");
        btn3.setBounds(startX, startY, width, height);
        startX += width + 20;
        add(btn3);
        btn3.addActionListener(eventBtn3());

        //
        startX = defaultX;
        startY = defaultY + BASE_WIDTH + 10;
        result = new JTextArea();
        result.setBounds(startX, startY, 700, 400);
        result.setEditable(false);
        add(result);

        setVisible(true);
    }

    /**
     * 按钮1事件
     */
    private ActionListener eventBtn1() {
        ActionListener event = new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                SqliteUtil thisClass = new SqliteUtil();
                Connection sqliteConn = thisClass.getConnection();
                String query = "select * from servant where id = 'S194';";
                List<Map<String, String>> recoderList = thisClass.excuteSelectList(query, sqliteConn);
                thisClass.displayResult(recoderList);
            }
        };
        return event;
    }

    /**
     * 按钮2事件
     */
    private ActionListener eventBtn2() {
        ActionListener event = new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("BTN2");
            }
        };
        return event;
    }

    /**
     * 按钮3事件
     */
    private ActionListener eventBtn3() {
        ActionListener event = new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        };
        return event;
    }
}
