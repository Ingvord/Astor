//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  java source code for Tango manager tool..
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013
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


import fr.esrf.TangoDs.TangoConst;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class Description:
 * This class display a dialog with a list of command to be send to
 * the selected server.
 *
 * @author verdier
 */

public class PopupHtml extends JDialog implements TangoConst {

    protected JFrame parent;
    protected ArrayList<URL> histo;
    protected JEditorPane pane;

    protected final static boolean back = false;
    protected final static boolean foward = true;

    private boolean deleteFileAtExit = false;

    //===============================================================
    /*
     * Initializes the Form
     */
    //===============================================================
    public PopupHtml(JFrame parent) {
        super(parent, true);
        initComponents();

        this.parent = parent;
        histo = new ArrayList<URL>();
        pack();

        if (parent != null) {
            if (parent.isVisible() && parent.getWidth() > 0) {
                Point p = parent.getLocationOnScreen();
                p.x += 10;
                p.y += 10;
                setLocation(p);
            }
        }
    }
    //===============================================================
    /*
     * Initializes the Form
     */
    //===============================================================
    public PopupHtml(JFrame parent, boolean deleteFile) {
        this(parent);
        deleteFileAtExit = deleteFile;
    }

    //===============================================================

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    //===============================================================
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        urlText = new javax.swing.JTextArea();
        setBackground(new java.awt.Color(198, 178, 168));
        setTitle("");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.FlowLayout(2, 5, 5));


        //pbar = new JProgressBar();
        //jPanel1.add (pbar);


        jLabel1.setText("                     ");
        jPanel1.add(jLabel1);

        cancelBtn.setText("Dismiss");
        cancelBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dismissBtnActionPerformed(evt);
            }
        });

        jPanel1.add(cancelBtn);


        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);


        backBtn.setText("<<  Back");
        backBtn.setForeground(java.awt.Color.black);
        backBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backBtn.setEnabled(false);

        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        jPanel2.add(backBtn);
        jPanel2.add(new JLabel("  "));

        urlText.setText("");
        urlText.setForeground(java.awt.Color.black);
        urlText.setEditable(false);
        jPanel2.add(urlText);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);
    }

    //======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dismissBtnActionPerformed
        if (histo.size() > 1) {
            int last = histo.size() - 2;
            URL prev_url = histo.get(last);
            //System.out.println("Back to " + prev_url);
            setPage(prev_url, back);
        }
    }

    //======================================================
    public void hyperlinkUpdateClicked(HyperlinkEvent evt) {
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            URL new_url = evt.getURL();
            setPage(new_url, foward);
        }
    }

    //======================================================
    //======================================================
    private class Timeout extends Thread {
        Thread parent;
        long millis;

        public Timeout(Thread parent, long millis) {
            this.parent = parent;
            this.millis = millis;
        }

        public synchronized void run() {
            try {
                wait(millis);
            } catch (Exception e) { /* */ }
            //parent.interrupt();
        }
    }
    //======================================================
    /**
     * Arm a timeout to be interrupted if connection is not possible
     *
     * @param millis timeout in milli seconds
     */
    //======================================================
    private void arm_timeout(long millis) {
        new Timeout(Thread.currentThread(), millis).start();
    }

    //======================================================
    //======================================================
    private boolean isTheSameWebServer(String new_url, String prev_url) {
        String prev_host;
        int start = header.length();
        start = prev_url.indexOf(".", start);

        int end = prev_url.indexOf("/", start);
        if (end < 0)
            prev_host = prev_url.substring(start);
        else
            prev_host = prev_url.substring(start, end);

        //System.out.println(new_url +"\ncontains (?)  " + prev_host);
        return (new_url.indexOf(prev_host) > 0);
    }

    //======================================================
    //======================================================
    private String header = "http://www";
    protected synchronized void setPage(URL url, boolean way) {
        URL prev_url = null;
        try {
            //	Arm a timeout to be interrupted if connection is not possible
            arm_timeout(5000); //	Does not work for connection.

            //	Check if http host has changed
            prev_url = pane.getPage();
            if (prev_url != null) {
                if (url.toString().startsWith(header)) {
                    if (prev_url.toString().startsWith(header))
                        if (!isTheSameWebServer(url.toString(), prev_url.toString())) {
                            Utils.popupError(parent,
                                    "This application does not manage proxies.\n" +
                                            "Please use a web browser to read this url.\n" +
                                            url.toString());
                            return;
                        }
                }
            }

            //	And Try to display page
            pane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            pane.setPage(url);

            //	Display address
            if (url.toString().startsWith(header))
                urlText.setText(url.toString());
            else
                urlText.setText("");

            //noinspection PointlessBooleanExpression
            if (way == back) {
                //  Check if back on error or really back
                URL url0 = histo.get(histo.size() - 1);
                if (url0 != url)
                    histo.remove(histo.size() - 1);    //	remove last index
            } else
                histo.add(url);                    //	Add url for history

            //	And manage Back button
            if (histo.size() > 1)
                backBtn.setEnabled(true);
            else
                backBtn.setEnabled(false);
        } catch (IOException e) {
            String str = "Cannot read:\n" + url.toString() + "\n\n";
            if (url.toString().startsWith("file:"))
                str += e.toString();
            else
                str += "If it is a remote web site, the proxies are not set...\n";
            System.out.println(str);
            Utils.popupError(parent, str);
            if (prev_url != null) {
                setPage(prev_url, back);
            }
        }
        pane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param url an URL address.
     */
    //======================================================
    public void show(URL url) {
        show(url.toString(), 700, 750);
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param url an URL address.
     * @param width  window width
     * @param height window height
     */
    //======================================================
    public void show(URL url, int width, int height) {
        show(url.toString(),width, height);
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param strurl an URL address or HTML page source code.
     */
    //======================================================
    public void show(String strurl) {
        show(strurl, 700, 750);
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param strurl an URL address or HTML page source code.
     * @param width  window width
     * @param height window height
     */
    //======================================================
    public void show(String strurl, int width, int height) {
        //	Get Url and display
        try {
            pane = new JEditorPane();
            pane.setEditable(false);
            //	add an hyperlink listener
            pane.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent evt) {
                    hyperlinkUpdateClicked(evt);
                }
            });
            getContentPane().add(new JScrollPane(pane), java.awt.BorderLayout.CENTER);
            setSize(width, height);

            URL url = new URL(strurl);
            setPage(url, foward);
        } catch (MalformedURLException e) {
            try {
                //	Try if it is a strurl is html code.
                URL url = new URL(buildTmpFile(strurl));
                setPage(url, foward);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }
        setVisible(true);
    }

    //======================================================
    //======================================================
    private String buildTmpFile(String code) {
        String urlstr = null;
        try {
            int random_value = new java.util.Random().nextInt(30000);
            FileOutputStream fidout;
            String tmpdir = System.getProperty("java.io.tmpdir");
            String filename = tmpdir + "/html." + random_value;
            fidout = new FileOutputStream(filename);
            fidout.write(code.getBytes());
            fidout.close();

            urlstr = "file:" + filename;
            deleteFileAtExit = true;
        } catch (Exception e) {
            Utils.popupError(parent, null, e);
            e.printStackTrace();
        }
        return urlstr;
    }

    //======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose();
    }

    //======================================================
    @SuppressWarnings({"UnusedParameters"})
    private void dismissBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dismissBtnActionPerformed
        doClose();
    }

    //======================================================
    private void doClose() {
        setVisible(false);
        if (deleteFileAtExit)
            try {
                URL url = histo.get(0);
                String urlstr = url.toString();
                if (urlstr.startsWith("file:"))
                    urlstr = urlstr.substring("file:".length());
                // Removing temporary file
                if (!new File(urlstr).delete())
                    System.err.println("Cannot delete " + urlstr);
            } catch (Exception e) {
                System.err.println(e);
            }

        dispose();
    }
    //----------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton backBtn;
    private javax.swing.JTextArea urlText;
    // End of variables declaration//GEN-END:variables


    //===============================================================
    //===============================================================
    public static void main(String args[]) {
        String index = "http://www.esrf.fr/computing/cs/tango/tango_doc/ds_doc/index.html";
        //String		index = "file:TangoProgrammerGuide.html";

        if (args.length>0)
            index = args[0];

        new PopupHtml(null).show(index);
    }
}
