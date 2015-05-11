//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  java source code for Tango manager tool..
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,
//						European Synchrotron Radiation Facility
//                      BP 220, Grenoble 38043
//                      FRANCE
//
// This file is part of Tango.
//
// Tango is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// Tango is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
// $Revision$
//
//-======================================================================


package admin.astor.tools;

import admin.astor.Astor;
import admin.astor.ManagePollingDialog;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;

import javax.swing.*;
import java.awt.event.MouseEvent;


//=======================================================
/**
 * Class Description: JFrame extention Class to display
 * browsing and  accessing devices
 *
 * @author Pascal Verdier
 */
//=======================================================
@SuppressWarnings("MagicConstant")
public class DevBrowser extends JFrame {
    private JFrame parent;
    private DevBrowserTree tree;

    private static EventsTable eventsTable;
    //=======================================================
    /**
     * Creates new form DevBrowser
     * @param parent parent frame instance
     */
    //=======================================================
    public DevBrowser(JFrame parent) {
        super();
        this.parent = parent;
        initComponents();

        //  Create  Tree
        try {
            tree = new DevBrowserTree(this);
            treeScrollPane.setViewportView(tree);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
        customizeMenu();
        pack();

        ATKGraphicsUtils.centerFrameOnScreen(this);
        jive.MultiLineToolTipUI.initialize();
    }

    //======================================================================
    //======================================================================
    private void customizeMenu() {
        //	File menu
        fileMenu.setMnemonic('F');
        exitBtn.setMnemonic('E');
        exitBtn.setAccelerator(KeyStroke.getKeyStroke('Q', MouseEvent.CTRL_MASK));
        viewMenu.setMnemonic('V');
        eventsDlgBtn.setMnemonic('E');
        eventsDlgBtn.setAccelerator(KeyStroke.getKeyStroke('E', MouseEvent.CTRL_MASK));
    }

    //===============================================================
    //===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
    void displayEventProperties(String attname) {
        tree.displayEventProperties(attname);
    }

    //=======================================================

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    //=======================================================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JScrollPane textScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        treeScrollPane = new javax.swing.JScrollPane();
        javax.swing.JMenuBar jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitBtn = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        eventsDlgBtn = new javax.swing.JMenuItem();
        javax.swing.JMenuItem astorBtn = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        textScrollPane.setPreferredSize(new java.awt.Dimension(400, 400));

        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Courier New", 1, 12));
        textScrollPane.setViewportView(textArea);

        jPanel1.add(textScrollPane, java.awt.BorderLayout.CENTER);

        treeScrollPane.setPreferredSize(new java.awt.Dimension(350, 400));
        jPanel1.add(treeScrollPane, java.awt.BorderLayout.WEST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        exitBtn.setText("Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });
        fileMenu.add(exitBtn);

        jMenuBar1.add(fileMenu);

        viewMenu.setText("View");

        eventsDlgBtn.setText("Events Panel");
        eventsDlgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventsDlgBtnActionPerformed(evt);
            }
        });
        viewMenu.add(eventsDlgBtn);

        astorBtn.setText("Astor Panel");
        astorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                astorBtnActionPerformed(evt);
            }
        });
        viewMenu.add(astorBtn);

        jMenuBar1.add(viewMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //=======================================================
    //=======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void astorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_astorBtnActionPerformed

        if (parent instanceof Astor) {
            Astor astor = (Astor) parent;
            astor.setVisible(true);
        }
    }//GEN-LAST:event_astorBtnActionPerformed

    //=======================================================
    //=======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void eventsDlgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventsDlgBtnActionPerformed

        try {
            if (eventsTable== null)
                eventsTable = new EventsTable(this);
            eventsTable.setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }//GEN-LAST:event_eventsDlgBtnActionPerformed

    //=======================================================
    //=======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        doClose();
    }//GEN-LAST:event_exitBtnActionPerformed

    //=======================================================
    //=======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        doClose();
    }//GEN-LAST:event_exitForm

    //===============================================================
    //===============================================================
    private void doClose() {
        setVisible(false);
        if (parent instanceof Astor) {
            if (parent.isVisible())
                dispose();
            else
                ((Astor) parent).doExit();
        } else
            dispose();
    }

    //===============================================================
    //===============================================================
    void setText(String str) {
        textArea.setText(str);
        textArea.setCaretPosition(0);
    }

    //===============================================================
    //===============================================================
    public void add(String signalName, int mode) {
        //  Build dialog if not already done.
        if (eventsTable== null) {
            try {
                eventsTable = new EventsTable(this);
            } catch (DevFailed e) {
                ErrorPane.showErrorMessage(this, null, e);
                return;
            }
            //ATKGraphicsUtils.centerDialog(eventsTable);
        }
        eventsTable.add(signalName, mode);
    }

    //======================================================
    //======================================================
    void displayHostPanel(String deviceName) {
        if (parent instanceof Astor) {
            Astor astor = (Astor) parent;
            astor.tree.displayHostInfo(deviceName);
        }
    }

    //======================================================
    //======================================================
    @SuppressWarnings({"UnusedDeclaration"})
    void managePolling(SubscribedSignal sig) {
        try {
            new ManagePollingDialog(this, sig.devname, sig.attname).setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
    }

    //======================================================
    //======================================================
    void managePolling(String devname) {
        try {
            new ManagePollingDialog(this, devname, "").setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
    }

    //======================================================
    //======================================================
    void managePolling(DeviceProxy dev, String attname) {
        try {
            new ManagePollingDialog(
                    this, dev.name(), attname).setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
    }
    //=======================================================

    /**
     * @param args the command line arguments
     */
    //=======================================================
    public static void main(String args[]) {
        try {
            Astor astor = new Astor();
            DevBrowser db = new DevBrowser(astor);
            db.setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(new JFrame(), null, e);
        }
    }

    //=======================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem eventsDlgBtn;
    private javax.swing.JMenuItem exitBtn;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JTextArea textArea;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
    //=======================================================

}
