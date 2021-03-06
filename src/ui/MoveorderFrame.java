package ui;

import importData.ImportData;
import importData.PreStowageInfo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by leko on 2016/1/17.
 */
public class MoveorderFrame extends JFrame{
    private JPanel panelCenter;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTable tableWQL;

    private JLabel jlFilter;
    private JPanel jpFilter;
    private JTextField jfFilter;
    private JButton btn;

    MoveorderFrame() {initComponents();}

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);//居中显示
        {
            jlFilter = new JLabel("按舱位ID查询:");
            jfFilter = new JTextField(10);
            btn = new JButton("查询");

            jpFilter = new JPanel(new FlowLayout());
            jpFilter.add(jlFilter);
            jpFilter.add(jfFilter);
            jpFilter.add(btn);

            this.panelCenter = new JPanel();
            this.panelCenter.setBorder(new TitledBorder(null, "作业序列信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));

            this.contentPane = new JPanel();
            this.contentPane.setLayout(new BorderLayout(0, 0));
            this.contentPane.add(jpFilter, BorderLayout.NORTH);
            this.contentPane.add(this.panelCenter, BorderLayout.CENTER);
            setContentPane(this.contentPane);
            this.panelCenter.setLayout(new BorderLayout(0, 0));
            {
                this.scrollPane = new JScrollPane();
                this.panelCenter.add(this.scrollPane,BorderLayout.CENTER);
                {
                    this.tableWQL = new JTable();
                    this.scrollPane.setViewportView(this.tableWQL);
//                    DefaultTableModel tableModel = new DefaultTableModel();
                    ui.TableModel tableModel = new ui.TableModel();
                    //增加列名
                    ArrayList<String> colList = new ArrayList<String>(Arrays.asList("舱位ID","倍位ID", "层号", "排号","作业序列"));
                    for (String col : colList) {
                        System.out.println(col);
                        tableModel.addColumn(col);
                    }

                    //增加内容
                    ArrayList<PreStowageInfo> preStowageInfoArrayList = ImportData.preStowageInfoArrayList;
                    if(preStowageInfoArrayList != null) {
                        Object[] rowData = new Object[5];
                        for (PreStowageInfo preStowageInfo:preStowageInfoArrayList)
                        {
                            rowData[0] = preStowageInfo.getVHT_ID();
                            rowData[1] = preStowageInfo.getVBY_BAYID();
                            rowData[2] = preStowageInfo.getVTR_TIERNO();
                            rowData[3] = preStowageInfo.getVRW_ROWNO();
                            rowData[4] = preStowageInfo.getMOVE_ORDER();
                            tableModel.addRow(rowData);
                        }
                    }else {
                        JOptionPane.showMessageDialog(this.getContentPane(),
                                "请导入船舶结构数据!", "提示", JOptionPane.INFORMATION_MESSAGE);
                    }

                    this.tableWQL.setModel(tableModel);
                }
            }

            final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
                    tableWQL.getModel());
            tableWQL.setRowSorter(sorter);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = jfFilter.getText();
                    if(text.length() == 0)
                        sorter.setRowFilter(null);
                    sorter.setRowFilter(RowFilter.regexFilter(text, 0));//按表格第一例筛选
                }
            });
        }
    }
}
