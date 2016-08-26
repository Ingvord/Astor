//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  java source code for Tango manager tool..
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,2014,2015,
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

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.*;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


//===============================================================

/**
 * Class Description: Basic Dialog Class to display info
 *
 * @author Pascal Verdier
 */
//===============================================================


public class DevPropertyDialog extends JDialog {
    private JDialog parent;
    private DbDevice dev;
    private DbServerArchitecture.TangoAtt att;

    //===============================================================
    /*
      *	Creates new form DevPropertyDialog
      */
    //===============================================================
    public DevPropertyDialog(JDialog parent, DbDevice dev,
                             DbServerArchitecture.TangoAtt att) {
        super(parent, true);
        this.parent = parent;
        this.dev = dev;
        this.att = att;
        initComponents();
        initOwnComponents();

        titleLabel.setText(dev.name() + "/" + att.name + "  Properties");
        pack();
        ATKGraphicsUtils.centerDialog(this);
    }

    //===============================================================
    //===============================================================
    private JTextField[] txt;
    private JButton[] btn;

    private void initOwnComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        txt = new JTextField[att.prop.length];
        btn = new JButton[att.prop.length];
        for (int i = 0; i < att.prop.length; i++) {
            int x = 0;
            //	Label to display property name
            gbc.gridx = x++;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(new JLabel(att.prop[i].name + " :   "), gbc);

            //	Text to edit value
            gbc.gridx = x++;
            gbc.gridy = i;
            txt[i] = new JTextField();
            txt[i].setColumns(25);
            txt[i].setText(att.prop[i].strval);
            panel.add(txt[i], gbc);

            //	Dummy label for separator
            gbc.gridx = x++;
            gbc.gridy = i;
            panel.add(new JLabel("   "), gbc);

            //	Button to read history
            gbc.gridx = x;
            gbc.gridy = i;
            btn[i] = new JButton("Histo");
            btn[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    histoActionPerformed(evt);
                }
            });
            panel.add(btn[i], gbc);
        }
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);
        okBtn.setText("Apply");
    }
    //===============================================================

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    //===============================================================
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okBtn.setText("OK");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        jPanel1.add(okBtn);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        jPanel1.add(cancelBtn);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        titleLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        titleLabel.setText("Dialog Title");
        jPanel2.add(titleLabel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    //===============================================================
    //===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < txt.length; i++) {
            String stringValue = txt[i].getText();
            //	Check if it has been changed
            if (!stringValue.equals(att.prop[i].strval)) {
                try {
                    //	Write it in database
                    DbAttribute att1 = new DbAttribute(att.name);
                    DbDatum datum = new DbDatum(att.prop[i].name);
                    datum.insert(stringValue);
                    att1.add(datum);

                    dev.put_attribute_property(att1);

                    //	Update message to be displayed
                    lines.add(att.prop[i].name + ":    " +
                            att.prop[i].strval + " --> " + stringValue);

                    //	Update local value
                    att.prop[i].strval = stringValue;
                } catch (DevFailed e) {
                    ErrorPane.showErrorMessage(this, "att.prop[i]", e);
                }
            }
        }
        String message = "";
        if (lines.size() > 0) {
            for (String s : lines)
                message += s;
        } else
            message = "Nothing changed !";
        Utils.popupMessage(this, message);

        doClose();
    }//GEN-LAST:event_okBtnActionPerformed

    //===============================================================
    //===============================================================
    private void histoActionPerformed(java.awt.event.ActionEvent evt) {

        //	Retreive index
        JButton b = (JButton) evt.getSource();
        int idx = -1;
        for (int i = 0; i < btn.length; i++)
            if (b == btn[i])
                idx = i;
        if (idx < 0) {
            Utils.popupError(this, "Cannot retreive Device, attribute...");
            return;
        }

        try {
            String propname = att.prop[idx].name;
            Database db = ApiUtil.get_db_obj();
            DbHistory[] histo = db.get_device_attribute_property_history(
                    dev.name(), att.name, propname);

            String[] strhisto = new String[histo.length];
            int nb = histo.length;
            if (nb == 0) {
                Utils.popupError(this, "No history found !");
                return;
            }

            for (int i = 0; i < histo.length; i++) {
                strhisto[nb - i - 1] = histo[i].getDate() +
                        ":      " + histo[i].getValue();
            }

            String choice;
            if ((choice = (String) JOptionPane.showInputDialog(parent,
                    dev.name() + "/" + att.name + "-" + propname + " :", "",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    strhisto, strhisto[0])) != null) {
                //	Set only value to text field
                int start = choice.indexOf(":   ") + 1;
                txt[idx].setText(choice.substring(start).trim());
            }
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, "Attribute Property History", e);
        }
    }

    //===============================================================
    //===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        doClose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    //===============================================================
    //===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose();
    }//GEN-LAST:event_closeDialog

    //===============================================================

    /**
     * Closes the dialog
     */
    //===============================================================
    private void doClose() {
        setVisible(false);
        dispose();
    }

    //===============================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
    //===============================================================

    //===============================================================
    //===============================================================
/*
	private void get_property_history(String deviceName, String attributeName, String propname)
	{
		DbHistory[] 	history =
			get_device_attribute_property_history(deviceName, attributeName, propname);
	}
*/
}
