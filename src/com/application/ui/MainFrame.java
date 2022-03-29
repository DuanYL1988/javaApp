package com.application.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.application.creater.DaoCreater;
import com.application.creater.ModelCreater;
import com.application.creater.PageCreater;
import com.application.creater.ServiceCreater;
import com.application.dto.Table;
import com.application.jdbc.SqliteUtil;
import com.application.service.GetTableInfo;

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

        btn2 = new JButton("生成Java代码");
        btn2.setBounds(startX, startY, width, height);
        startX += width + 20;
        add(btn2);
        btn2.addActionListener(eventBtn2());

        btn3 = new JButton("生成Spring代码");
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
                GetTableInfo service = new GetTableInfo();
                Table table = service.getTableInfoFromFile("resources\\SERVANT.ddl");

                String projectName = "javaApp";
                String basePkg = "com.application";
                boolean createFlag = true;
                boolean springFlag = false;
                //
                ModelCreater creater = new ModelCreater();
                creater.createModel(table, projectName, basePkg, createFlag, springFlag);
                //
                DaoCreater daoC = new DaoCreater(table);
                daoC.createMappingXml(table, projectName, basePkg, createFlag, false);
                daoC.createMybatis(table, projectName, basePkg, createFlag, false);
                // PageCreater page = new PageCreater();
                // page.createController(table, projectName, basePkg, createFlag);
                // //
                // ServiceCreater serviceC = new ServiceCreater();
                // serviceC.createServiceImpl(table, projectName, basePkg, createFlag);
                // serviceC.createService(table, projectName, basePkg, createFlag);
                // //
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

                GetTableInfo service = new GetTableInfo();
                Table table = service.getTableInfoFromFile("resources\\SERVANT.ddl");

                String projectName = "SpringBootProject";
                String basePkg = "com.springboot.demo";
                boolean createFlag = true;
                boolean hibernateFlag = true;
                //
                ModelCreater creater = new ModelCreater();
                creater.createModel(table, projectName, basePkg, createFlag, hibernateFlag);
                //
                DaoCreater daoC = new DaoCreater(table);
                daoC.createMappingXml(table, projectName, basePkg, createFlag, hibernateFlag);
                daoC.createMybatis(table, projectName, basePkg, createFlag, hibernateFlag);
                if (hibernateFlag) {
                    daoC.createHibernate(table, projectName, basePkg, createFlag);
                }
                //
                PageCreater page = new PageCreater();
                page.createController(table, projectName, basePkg, createFlag, hibernateFlag);
                //
                ServiceCreater serviceC = new ServiceCreater();
                serviceC.createServiceImpl(table, projectName, basePkg, createFlag, hibernateFlag);
                serviceC.createService(table, projectName, basePkg, createFlag);
            }
        };
        return event;
    }
}
