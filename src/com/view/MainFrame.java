package com.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.common.Code;
import com.util.FileReadWriteUtil;
import com.util.JDBCUtil;
import com.util.PropertyUtil;
import com.util.SecurityUtil;
import com.util.TextUtil;

public class MainFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static PropertyUtil propUtil = new PropertyUtil();

    private final SecurityUtil security = new SecurityUtil();

    private final FileReadWriteUtil fileUtil = new FileReadWriteUtil();

    /**
     * 是 Swing 当中最灵活也是最复杂的布局管理器，可对控件在容器中的位置进行比较灵活的调整
     */
    GridBagLayout g = new GridBagLayout();
    /**
     * 组件
     */
    GridBagConstraints c = new GridBagConstraints();

    JLabel title, websit, userAccount, password, bankKbn, saveKbn;
    JTextField textWebsit, textUserAccount, textUserPassword;
    JButton select;
    JButton update;
    JRadioButton bank, notbank, file, database;
    JTextArea result;

    public static void main(String args[]) {
        String version = propUtil.getParamByKey(Code.VERSION, null, Code.MODE_PARAM);
        version = "个人密码管理  " + version;
        new MainFrame(version);
    }

    MainFrame(String str) {
        super(str);
        setSize(300, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(g);
        // 调用方法
        addComponent();
        select.addActionListener(this);
        update.addActionListener(this);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    // 在这个方法中将会添加所有的组件;
    public void addComponent() {
        // 注册网站
        websit = new JLabel("注册网站 :");
        add(g, c, websit, 0, 0, 1, 1);
        // 输入框
        textWebsit = new JTextField(15);
        add(g, c, textWebsit, 0, 1, 2, 1);
        // 用户名
        userAccount = new JLabel("用户账户名：");
        add(g, c, userAccount, 1, 0, 1, 1);
        // 用户名输入框
        textUserAccount = new JTextField(15);
        add(g, c, textUserAccount, 1, 1, 2, 1);
        // 密码：
        password = new JLabel("密码：");
        add(g, c, password, 2, 0, 1, 1);
        // 密码输入框
        textUserPassword = new JTextField(15);
        add(g, c, textUserPassword, 2, 1, 2, 1);
        // 账户加密
        bankKbn = new JLabel("账户加密");
        add(g, c, bankKbn, 3, 0, 1, 1);
        bank = new JRadioButton("卡号加密");
        notbank = new JRadioButton("不加密");

        ButtonGroup bankflg = new ButtonGroup();
        bankflg.add(bank);
        bankflg.add(notbank);
        // 保存方式
        saveKbn = new JLabel("保存方式");
        add(g, c, saveKbn, 4, 0, 1, 1);
        file = new JRadioButton("本地保存");
        database = new JRadioButton("DB保存");
        // 设置默认值
        setDefaultSelect(bank, notbank, file, database);

        add(g, c, bank, 3, 1, 1, 1);
        add(g, c, notbank, 3, 2, 1, 1);
        add(g, c, database, 4, 1, 1, 1);
        add(g, c, file, 4, 2, 1, 1);

        ButtonGroup saveflg = new ButtonGroup();
        saveflg.add(file);
        saveflg.add(database);
        // select按钮
        select = new JButton("查找");
        add(g, c, select, 5, 0, 1, 1);
        // update按钮
        update = new JButton("登陆");
        add(g, c, update, 5, 1, 1, 1);
        // 显示结果
        result = new JTextArea(15, 20);
        result.setEditable(false);
        add(g, c, result, 6, 0, 3, 4);
    }

    public void add(GridBagLayout g, GridBagConstraints c, JComponent jc, int row, int column, int width, int height) {
        // 列
        c.gridx = column;
        // 行
        c.gridy = row;
        // 宽度
        c.gridwidth = width;
        // 高度
        c.gridheight = height;
        // 多出部分排列方向
        c.anchor = GridBagConstraints.NORTH;
        g.setConstraints(jc, c);
        add(jc);
    }

    /**
     * 主处理
     */
    @Override
    public void actionPerformed(ActionEvent action) {
        String websit = textWebsit.getText();
        String account = textUserAccount.getText();
        String password = textUserPassword.getText();
        boolean bank_kbn = bank.isSelected();
        boolean db = database.isSelected();
        String kbn = action.getActionCommand();
        StringBuffer resultText = new StringBuffer();
        // 入力チェック
        String msg = "";
        msg = validate(kbn, websit, account, password, bank_kbn);
        if (!msg.equals(Code.MSG_VALDATE_SUCCESS)) {
            result.setText(msg);
            return;
        }
        // DB mode
        if (db) {
            JDBCUtil util = new JDBCUtil();
            if ("查找".equals(kbn)) {
                String result = util.dbConnection(websit, account, bank_kbn);
                if (!TextUtil.isNotEmpty(result)) {
                    resultText.append(Code.MSG_SELECT_MARRING);
                } else {
                    resultText.append(result);
                }
            } else if ("登陆".equals(kbn)) {
                password = security.enCrypt(password);
                if (bank_kbn) {
                    account = security.enCrypt(account);
                }
                resultText.append(util.updateDb(websit, account, password));
            }
            // file mode
        } else {
            if ("查找".equals(kbn)) {
                // 选卡号加密的情况下，对输入的卡号进行加密验证
                if (bank_kbn && TextUtil.isNotEmpty(account)) {
                    account = security.enCrypt(account);
                }
                String pswd = propUtil.getParamByKey(websit, account, Code.MODE_DATE);
                if (TextUtil.isNotEmpty(pswd)) {
                    resultText.append(Code.MSG_SELECT_SUCCESS + "\n");
                    resultText.append("密码:" + security.deCrypt(pswd));
                } else {
                    // 未入力账户的情况下进行模糊查询
                    if (TextUtil.isNotEmpty(account)) {
                        resultText.append(Code.MSG_SELECT_MARRING + "\n");
                    } else {
                        resultText.append(fileUtil.getData(websit, fileUtil.getLocalPath(), bank_kbn));
                    }
                }
            } else {
                String pswd = propUtil.getParamByKey(websit, account, Code.MODE_DATE);
                if (TextUtil.isNotEmpty(pswd)) {
                    resultText.append(Code.MSG_UPDATE_MARRING);
                } else {
                    password = security.enCrypt(password);
                    if (bank_kbn) {
                        account = security.enCrypt(account);
                    }
                    resultText.append(propUtil.saveDate(websit, account, password));
                }
            }
        }
        result.setText(resultText.toString());
    }

    /**
     * 输入检查
     *
     * @param mode
     *            卡号加密模式
     */
    private String validate(String action, String websit, String account, String password, boolean mode) {
        String message = "";
        if ("查找".equals(action)) {
            if (mode) {
                if (!TextUtil.isNotEmpty(websit, account)) {
                    message = Code.MSG_VALDATE_BANKKBN;
                    message = message + Code.MSG_VALDATE_ACCOUNT;
                    return message;
                }
            } else {
                if (!TextUtil.isNotEmpty(websit)) {
                    message = message + Code.MSG_VALDATE_WEBSITE;
                    return message;
                }
            }
        } else if ("登陆".equals(action)) {
            if (!TextUtil.isNotEmpty(websit, account, password)) {
                message = message + Code.MSG_VALDATE_UPDATE;
                return message;
            }
        }
        return Code.MSG_VALDATE_SUCCESS;

    }

    /**
     * 取得默认值
     */
    private void setDefaultSelect(JRadioButton bank, JRadioButton notbank, JRadioButton file, JRadioButton database) {
        // 保存模式
        String param_save = propUtil.getParamByKey(Code.SAVE_KBN, null, Code.MODE_PARAM);
        if (param_save.equals("0")) {
            file.setSelected(true);
        } else {
            database.setSelected(true);
        }
        // 账号加密
        String param_bank = propUtil.getParamByKey(Code.BANK_KBN, null, Code.MODE_PARAM);
        if (param_bank.equals("1")) {
            bank.setSelected(true);
        } else {
            notbank.setSelected(true);
        }
    }
}
