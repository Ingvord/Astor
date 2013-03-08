//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  java source code for Tango manager tool..
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011,2012,2013,
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


package admin.astor;

/**
 *	This class is the Astor main panel
 *	containing the Jtree used to display hosts.
 *
 * @author verdier
 */

import admin.astor.statistics.StatisticsPanel;
import admin.astor.tools.*;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.*;
import fr.esrf.TangoDs.Except;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import fr.esrf.tangoatk.widget.util.JSmoothProgressBar;
import fr.esrf.tangoatk.widget.util.Splash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class Astor extends JFrame implements AstorDefs {

    /**
     * Initialized by make jar call and used to display title.
     */
    private static String revNumber =
            "6.3.0  -  Wed Mar 06 13:19:23 CET 2013";
    /**
     * JTree object to display control system.
     */
    public AstorTree tree = null;
    /**
     * JTree state
     */
    private boolean expanded = false;

    /**
     * JTree Container
     */
    private JScrollPane scrollPane;
    /**
     * true if Astor is fully built and displayed.
     */
    static boolean displayed = false;

    public static DevBrowser dev_browser = null;
    private String tango_host = "";
    private MultiServerCommand multiServerCommand = null;
    static long t0;
    public static int rwMode = READ_WRITE;
    //======================================================================
    /**
     * Creates new form Astor
     *
     * @throws DevFailed in case of database connection failed
     */
    //======================================================================
    public Astor() throws DevFailed {
        t0 = System.currentTimeMillis();

        initComponents();
        AstorUtil.getInstance().initIcons();
        customizeMenu();

        setTitle("TANGO Manager - " + revNumber);
        setControlSystemTitle();
        buildTree();
        ImageIcon icon = new ImageIcon(
                getClass().getResource(img_path + "tango_icon.jpg"));
        setIconImage(icon.getImage());

        centerWindow();

        try {
            tango_host = ApiUtil.get_db_obj().get_tango_host();
        } catch (DevFailed e) { /* do nothing */ }

        //	There is some problem between environment and change
        //changeTgHostBtn.setVisible(false);
    }
	//===========================================================
    /**
     * Move the window to the center of the screen
     */
	//===========================================================
    public void centerWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrsize = toolkit.getScreenSize();
        Dimension appsize = getSize();
        Point p = new Point();
        p.x = (scrsize.width - appsize.width) / 2;
        p.y = (scrsize.height - appsize.height) / 2;
        setLocation(p);
    }

    //======================================================================
    //======================================================================
    private void setControlSystemTitle() throws DevFailed {
        //  Get control system name if any
        String  name = AstorUtil.getControlSystemName();
        if (name!=null && name.length()>0) {
            titleLabel.setText(name);
            topPanel.setVisible(true);
        }
        else
            topPanel.setVisible(false);
    }
    //======================================================================
    //======================================================================
    private void buildTree() throws DevFailed {

        //	Build Splash Screen
        String title = "Astor (TANGO Manager)";
        int end = revNumber.indexOf("-");
        if (end > 0)
            title += " - " + revNumber.substring(0, end).trim();

        //	Create a splash window.
        JSmoothProgressBar myBar = new JSmoothProgressBar();
        myBar.setStringPainted(true);
        myBar.setBackground(Color.lightGray);
        myBar.setProgressBarColors(Color.gray, Color.gray, Color.gray);

        ImageIcon icon = new ImageIcon(
                getClass().getResource(img_path + "CollaborationSplash.gif"));
        Splash splash = new Splash(icon, Color.black, myBar);
        splash.setTitle(title);
        splash.setMessage("Starting....");
        splash.setVisible(true);

        try {
            //	Stop threads if already started (updating tree)
            if (tree != null) {
                tree.stopThreads();
                scrollPane.remove(tree);
                remove(scrollPane);
            }

            //	Build tree and start threads to update tree
            tree = new AstorTree(this, splash);
            scrollPane = new JScrollPane();
            scrollPane.setPreferredSize(AstorUtil.getPreferredSize());
            scrollPane.setViewportView(tree);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            expanded = false;
            pack();

            //  Access control management
            manageAccessControlMenu(tree.isAccessControlled());
        } catch (DevFailed e) {
            splash.setVisible(false);
            throw e;
        }
    }

    //======================================================================
    //======================================================================
    Dimension getTreeSize() {
        //return scrollPane.getSize();
        return scrollPane.getPreferredSize();
    }

    //======================================================================
    //======================================================================
    void setTreeSize(Dimension d) {
        scrollPane.setPreferredSize(d);
        pack();
    }

    //======================================================================
    //======================================================================
    private void customizeMenu() {
        //	File menu
        fileMenu.setMnemonic('F');
        exitBtn.setMnemonic('E');
        exitBtn.setAccelerator(KeyStroke.getKeyStroke('Q', Event.CTRL_MASK));

        ctrlPreferenceBtn.setMnemonic('P');
        ctrlPreferenceBtn.setAccelerator(KeyStroke.getKeyStroke('P', Event.CTRL_MASK));
        String s = System.getProperty("NO_PREF");
        if (s != null && s.toLowerCase().equals("true"))
            ctrlPreferenceBtn.setEnabled(false);
        ctrlPreferenceBtn.setEnabled(rwMode==READ_WRITE);
        usePreferenceBtn.setEnabled(rwMode==READ_WRITE);

        changeTgHostBtn.setMnemonic('T');
        changeTgHostBtn.setAccelerator(KeyStroke.getKeyStroke('T', Event.CTRL_MASK));

        //	View menu
        viewMenu.setMnemonic('V');
        newBranchBtn.setMnemonic('N');
        newBranchBtn.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK));

        deviceBrowserBtn.setMnemonic('B');
        deviceBrowserBtn.setAccelerator(KeyStroke.getKeyStroke('B', Event.CTRL_MASK));

        expandBtn.setMnemonic('E');
        expandBtn.setAccelerator(KeyStroke.getKeyStroke('E', Event.CTRL_MASK));

        //	Search menu
        toolsMenu.setMnemonic('T');
        multiServersCmdItem.setAccelerator(KeyStroke.getKeyStroke('M', Event.CTRL_MASK));
        jiveMenuItem.setAccelerator(KeyStroke.getKeyStroke('J', Event.CTRL_MASK));
        logviewerMenuItem.setAccelerator(KeyStroke.getKeyStroke('L', Event.CTRL_MASK));
        multiServersCmdItem.setEnabled(rwMode==READ_WRITE);
        jiveMenuItem.setEnabled(rwMode!=READ_ONLY);
        accessControlBtn.setEnabled(rwMode!=READ_ONLY);

        //	Command menu
        cmdMenu.setMnemonic('C');
        cmdMenu.setEnabled(rwMode!=READ_ONLY);

        newHostBtn.setAccelerator(KeyStroke.getKeyStroke('H', Event.CTRL_MASK));

        nb_def_tools = toolsMenu.getItemCount();
        buildToolsItems();
        buildAdditionnalHelps();

        expandBtn.setVisible(false);

        modeLabel.setText(strMode[rwMode]);
        bottomPanel.setVisible(rwMode!=READ_WRITE);
    }


    //======================================================================
    //======================================================================
    private void manageAccessControlMenu(boolean isAccessControlled) {
        if (isAccessControlled) {
            accessControlBtn.setMnemonic('A');
            accessControlBtn.setAccelerator(KeyStroke.getKeyStroke('A', Event.CTRL_MASK));
            accessControlBtn.setVisible(true);
            System.out.println("AccessControl is active");
        } else {   //  Service does not exist !
            accessControlBtn.setVisible(false);
        }
    }

    //======================================================================
    //======================================================================
    private String[] htmlHelps = null;

    private void buildAdditionnalHelps() {
        htmlHelps = AstorUtil.getHtmlHelps();
        if (htmlHelps == null) return;
        if (htmlHelps.length == 0) return;

        helpMenu.add(new JSeparator());

        for (int i = 0; i < htmlHelps.length / 2; i++) {
            JMenuItem mi = new JMenuItem();
            mi.setText(htmlHelps[2 * i]);
            mi.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    htmlHelpsItemActionPerformed(evt);
                }
            });
            helpMenu.add(mi);
        }
    }

    //======================================================================
    //======================================================================
    private void htmlHelpsItemActionPerformed(java.awt.event.ActionEvent evt) {
        String str = evt.getActionCommand();
        String cmd = null;
        for (int i = 0; i < htmlHelps.length; i++)
            if (str.equals(htmlHelps[i]))
                if (i < htmlHelps.length - 1)
                    cmd = htmlHelps[i + 1];
        if (cmd == null)
            Utils.popupError(this, "No command found for item  \'" + str + "\'");
        System.out.println(cmd);

        AstorUtil.showInHtmBrowser(cmd);
    }
    //======================================================================

    /**
     * Remove Optional tools if any and add new ones.
     */
    //======================================================================
    private void buildToolsItems() {
        //  Remove items if any
        for (int i = toolsMenu.getItemCount(); i > nb_def_tools; i--) {
            JMenuItem mi = toolsMenu.getItem(i - 1);
            mi.removeActionListener(tools_al.get(i - nb_def_tools - 1));
            toolsMenu.remove(i - 1);
        }
        app_tools.clear();
        tools_al.clear();

        //	Add JMenuItem for tools
        String[] str_tools = AstorUtil.getTools();
        if (str_tools != null) {
            for (int i = 0; i < str_tools.length; i += 2) {
                OneTool t = new OneTool(str_tools[i], str_tools[i + 1]);
                app_tools.add(t);
                JMenuItem mi = new JMenuItem();
                mi.setText(t.name);
                ActionListener al;
                mi.addActionListener(al = new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        toolsItemActionPerformed(evt);
                    }
                });
                tools_al.add(al);
                toolsMenu.add(mi);
            }
        }
    }

    private int nb_def_tools = 1;
    private ArrayList<OneTool> app_tools = new ArrayList<OneTool>();
    private ArrayList<ActionListener> tools_al = new ArrayList<ActionListener>();
    //======================================================================
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    //======================================================================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        modeLabel = new javax.swing.JLabel();
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        changeTgHostBtn = new javax.swing.JMenuItem();
        ctrlPreferenceBtn = new javax.swing.JMenuItem();
        usePreferenceBtn = new javax.swing.JMenuItem();
        exitBtn = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        deviceBrowserBtn = new javax.swing.JMenuItem();
        javax.swing.JMenuItem refreshBtn = new javax.swing.JMenuItem();
        expandBtn = new javax.swing.JMenuItem();
        javax.swing.JMenuItem startupErrorBtn = new javax.swing.JMenuItem();
        cmdMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem startServersBtn = new javax.swing.JMenuItem();
        javax.swing.JMenuItem stopServersBtn = new javax.swing.JMenuItem();
        newHostBtn = new javax.swing.JMenuItem();
        newBranchBtn = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        javax.swing.JMenu findMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem findHostItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem findObjectByFilterItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem tangoStatBtn = new javax.swing.JMenuItem();
        multiServersCmdItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem serverUsageMenuItem = new javax.swing.JMenuItem();
        jiveMenuItem = new javax.swing.JMenuItem();
        accessControlBtn = new javax.swing.JMenuItem();
        javax.swing.JMenuItem statisticsBtn = new javax.swing.JMenuItem();
        logviewerMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        principleBtn = new javax.swing.JMenuItem();
        distributionBtn = new javax.swing.JMenuItem();
        stateIconsBtn = new javax.swing.JMenuItem();
        tangorbBtn = new javax.swing.JMenuItem();
        starterEventsItem = new javax.swing.JMenuItem();
        starterNoEventsItem = new javax.swing.JMenuItem();
        releaseNoteBtn = new javax.swing.JMenuItem();
        aboutBtn = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        topPanel.add(titleLabel);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        modeLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bottomPanel.add(modeLabel);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        fileMenu.setText("File");

        changeTgHostBtn.setText("Change Tango Host");
        changeTgHostBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTgHostBtnActionPerformed(evt);
            }
        });
        fileMenu.add(changeTgHostBtn);

        ctrlPreferenceBtn.setText("Ctrl System Preferences");
        ctrlPreferenceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlPreferenceBtnActionPerformed(evt);
            }
        });
        fileMenu.add(ctrlPreferenceBtn);

        usePreferenceBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        usePreferenceBtn.setMnemonic('U');
        usePreferenceBtn.setText("User Preferences");
        usePreferenceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usePreferenceBtnActionPerformed(evt);
            }
        });
        fileMenu.add(usePreferenceBtn);

        exitBtn.setText("Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });
        fileMenu.add(exitBtn);

        menuBar.add(fileMenu);

        viewMenu.setText("View");

        deviceBrowserBtn.setText("Event Manager");
        deviceBrowserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deviceBrowserBtnActionPerformed(evt);
            }
        });
        viewMenu.add(deviceBrowserBtn);

        refreshBtn.setText("Update Tree from database");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });
        viewMenu.add(refreshBtn);

        expandBtn.setText("Expand Tree");
        expandBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandBtnActionPerformed(evt);
            }
        });
        viewMenu.add(expandBtn);

        startupErrorBtn.setText("Startup Errors");
        startupErrorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startupErrorBtnActionPerformed(evt);
            }
        });
        viewMenu.add(startupErrorBtn);

        menuBar.add(viewMenu);

        cmdMenu.setText("Command");

        startServersBtn.setText("Start All Controlled Servers");
        startServersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startServersBtnActionPerformed(evt);
            }
        });
        cmdMenu.add(startServersBtn);

        stopServersBtn.setText("Stop All Controlled Servers");
        stopServersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopServersBtnActionPerformed(evt);
            }
        });
        cmdMenu.add(stopServersBtn);

        newHostBtn.setText("Add a New Host");
        newHostBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newHostBtnActionPerformed(evt);
            }
        });
        cmdMenu.add(newHostBtn);

        newBranchBtn.setText("Add a New Branch");
        newBranchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBranchBtnActionPerformed(evt);
            }
        });
        cmdMenu.add(newBranchBtn);

        menuBar.add(cmdMenu);

        toolsMenu.setText("Tools");

        findMenu.setText("Find");

        findHostItem.setText("Host by Name");
        findHostItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findHostItemActionPerformed(evt);
            }
        });
        findMenu.add(findHostItem);

        findObjectByFilterItem.setText("Device/Server/Class by Filter");
        findObjectByFilterItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findObjectByFilterItemActionPerformed(evt);
            }
        });
        findMenu.add(findObjectByFilterItem);

        toolsMenu.add(findMenu);

        tangoStatBtn.setText("Ctrl System Info");
        tangoStatBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tangoStatBtnActionPerformed(evt);
            }
        });
        toolsMenu.add(tangoStatBtn);

        multiServersCmdItem.setText("Multi Servers Command");
        multiServersCmdItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiServersCmdItemActionPerformed(evt);
            }
        });
        toolsMenu.add(multiServersCmdItem);

        serverUsageMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        serverUsageMenuItem.setText("Server Usage");
        serverUsageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverUsageMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(serverUsageMenuItem);

        jiveMenuItem.setText("Jive");
        jiveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jiveMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(jiveMenuItem);

        accessControlBtn.setText("Access Control");
        accessControlBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accessControlBtnActionPerformed(evt);
            }
        });
        toolsMenu.add(accessControlBtn);

        statisticsBtn.setText("Server Statistics");
        statisticsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statisticsBtnActionPerformed(evt);
            }
        });
        toolsMenu.add(statisticsBtn);

        logviewerMenuItem.setText("LogViewer");
        logviewerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logviewerMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(logviewerMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");

        principleBtn.setText("Principle");
        principleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(principleBtn);

        distributionBtn.setText("Servers Distribution");
        distributionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(distributionBtn);

        stateIconsBtn.setText("State Icons");
        stateIconsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(stateIconsBtn);

        tangorbBtn.setText("TangORB revision");
        tangorbBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(tangorbBtn);

        starterEventsItem.setText("Hosts Controlled on Events");
        starterEventsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(starterEventsItem);

        starterNoEventsItem.setText("Hosts Controlled on Polling");
        starterNoEventsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(starterNoEventsItem);

        releaseNoteBtn.setText("Release Note");
        releaseNoteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(releaseNoteBtn);

        aboutBtn.setText("About");
        aboutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        helpMenu.add(aboutBtn);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void ctrlPreferenceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlPreferenceBtnActionPerformed
        new PreferenceDialog(this).setVisible(true);
        try {
            setControlSystemTitle();    //  Could have changed.
        } catch (DevFailed e) { /* **/ }

    }//GEN-LAST:event_ctrlPreferenceBtnActionPerformed
    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void usePreferenceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usePreferenceBtnActionPerformed
        List<String> knownTangoHosts = AstorUtil.getAllKnownTangoHosts();
        GetTextDialog dlg = new GetTextDialog(this,
                "List of User Tango Hosts", null, knownTangoHosts);
        if (dlg.showDialog() == JOptionPane.OK_OPTION) {
            knownTangoHosts = dlg.getTextLines();
            try {
                AstorUtil.saveUserKownTangoHost(knownTangoHosts);
            }
            catch(DevFailed e) {
                ErrorPane.showErrorMessage(this, null, e);
            }
        }
    }//GEN-LAST:event_usePreferenceBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void startupErrorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startupErrorBtnActionPerformed

        if (tree.subscribeErrWindow == null)
            Utils.popupMessage(this, "No error at startup.");
        else
            tree.subscribeErrWindow.setVisible(true);
    }//GEN-LAST:event_startupErrorBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void accessControlBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accessControlBtnActionPerformed
        tree.startTACpanel();
    }//GEN-LAST:event_accessControlBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void changeTgHostBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeTgHostBtnActionPerformed

        try {
            String newTangoHost;
            List<String> knownTangoHosts = AstorUtil.getAllKnownTangoHosts();
            Selector    tangoHostSelector = new Selector(this,
                        "Tango Host  (e.g.  hal:2001)", knownTangoHosts, tango_host);
            ATKGraphicsUtils.centerDialog(tangoHostSelector);
            newTangoHost = tangoHostSelector.showDialog();

            if (newTangoHost == null || newTangoHost.length() == 0)
                return;

            //	Check if connection OK
            String[] tgh_arr = newTangoHost.split(":");
            if (tgh_arr.length != 2) {
                Utils.popupError(this, "Input syntax error\n" + newTangoHost + "\n is not a valid TANGO_HOST");
                return;
            }
            ApiUtil.get_db_obj(tgh_arr[0], tgh_arr[1]);

            if (tango_host.equals(newTangoHost))
                return;

            //  Set the rw mode for new astor
            String rights;
            if (rwMode==READ_WRITE)
                rights = "-rw";
            else
            if (rwMode==DB_READ_ONLY)
                rights = "-db_ro";
            else
                rights = "-ro";
            String  cmd = "java -DTANGO_HOST=" + newTangoHost + " admin.astor.Astor " + rights;
            AstorUtil.executeShellCmdAndReturn(cmd);
        } catch (Exception e) {
            ErrorPane.showErrorMessage(this,
                    "Cannot change TANGO_HOST", e);
        }
    }//GEN-LAST:event_changeTgHostBtnActionPerformed

    //======================================================================
    //======================================================================
    private fr.esrf.logviewer.Main logviewer = null;

    @SuppressWarnings({"UnusedDeclaration"})
    private void logviewerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logviewerMenuItemActionPerformed
        if (logviewer == null)
            logviewer = new fr.esrf.logviewer.Main(this);
        logviewer.setVisible(true);
        logviewer.toFront();
    }//GEN-LAST:event_logviewerMenuItemActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void jiveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jiveMenuItemActionPerformed
        tree.displayJiveAppli();
    }//GEN-LAST:event_jiveMenuItemActionPerformed

    //======================================================================
    //======================================================================
    static private String searched_host = "";

    @SuppressWarnings({"UnusedDeclaration"})
    private void findHostItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findHostItemActionPerformed

        //	Ask for host's name
        String hostname = (String) JOptionPane.showInputDialog(this,
                "Host Name ?",
                "Input Dialog",
                JOptionPane.INFORMATION_MESSAGE,
                null, null, searched_host);

        //	if host has been typed,
        //	select it on tree and open control panel
        if (hostname != null) {
            try {
                tree.setSelectionRoot();
                tree.setSelectionPath(hostname);
                tree.displayHostInfo();
                searched_host = hostname;
            }
            catch (DevFailed e) {
                ErrorPane.showErrorMessage(this, null, e);
            }
        }

    }//GEN-LAST:event_findHostItemActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void tangoStatBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tangoStatBtnActionPerformed

        //	Ask to confirm
        if (JOptionPane.showConfirmDialog(this,
                "The System Information needs to browse Database\n" +
                        "        and it could take a long time !\n\n" +
                        "Start it any way ?",
                "Confirm Dialog",
                JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {

            new DeviceTreeDialog(this);

            // ToDo
            /*
             * Really too slow And too much threads
            try {
                new TangoReleaseDialog(this).setVisible(true);
            }
            catch (DevFailed e) {
                ErrorPane.showErrorMessage(this, null, e);
            }
            */
        }
    }//GEN-LAST:event_tangoStatBtnActionPerformed

    //======================================================================
    /**
     * Start tools found in Astor/Tools property
     *
     * @param evt mouse event
     */
    //======================================================================
    @SuppressWarnings({"ConstantConditions"})
    private void toolsItemActionPerformed(java.awt.event.ActionEvent evt) {
        String name = evt.getActionCommand();
        for (OneTool app : app_tools) {
            if (app.name.equals(name)) {
                try {
                    System.out.println("Starting " + app.classname);

                    //	Check if tool is already instanced.
                    if (app.jframe == null) {
                        //	Retrieve class name
                        Class clazz = Class.forName(app.classname);

                        //	And build object
                        Class[] param = new Class[1];
                        param[0] = JFrame.class;
                        Constructor constructor = clazz.getConstructor(param);

                        // ----------------- Java 5 -----------------
                        JFrame jf = (JFrame) constructor.newInstance(this);
                        app.setJFrame(jf);
                    }
                    app.jframe.setVisible(true);
                } catch (Exception e) {
                    ErrorPane.showErrorMessage(this, null, e);
                }
            }
        }
    }

    //======================================================================
    //======================================================================
    void removeHost(String hostname) {
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure to want to remove " + hostname,
                "Confirm Dialog",
                JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION)
            return;

        String servname = "Starter/" + hostname;
        String devadmin = "dserver/" + servname;
        String devname = starterDeviceHeader + hostname;

        //	Ask to confirm
        try {
            //	Remove properties
            DeviceProxy dev = new DeviceProxy(devname);
            dev.delete_property("StarteDsPath");
            dev.delete_property(collec_property);
            dev.delete_property(usage_property);
            dev.delete_property("UseEvents");

            //	Remove devices and server
            Database db = ApiUtil.get_db_obj(AstorUtil.getTangoHost());
            db.delete_server(servname);
            db.delete_device(devname);
            db.delete_device(devadmin);

            JOptionPane.showMessageDialog(this,
                    hostname + " has been removed !",
                    "Command Done",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this,
                    "Cannot remove host", e);
        }
        try {
            buildTree();
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void newHostBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newHostBtnActionPerformed
        addNewHost(null);
    }//GEN-LAST:event_newHostBtnActionPerformed

    //======================================================================
    //======================================================================
    void addNewHost(TangoHost h) {
        NewStarterDialog dialog
                = new NewStarterDialog(this, h, tree.getCollectionList(), tree.hosts, true);
        dialog.setVisible(true);
        if (dialog.getValue() == JOptionPane.OK_OPTION) {
            try {
                buildTree();
            } catch (DevFailed e) {
                ErrorPane.showErrorMessage(this, null, e);
            }
        }
    }

    //======================================================================
    //======================================================================
    void editHostProperties(TangoHost h) {
        NewStarterDialog dialog
                = new NewStarterDialog(this, h, tree.getCollectionList(), tree.hosts, false);
        dialog.setVisible(true);
    }

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void stopServersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServersBtnActionPerformed
        new ServerCmdThread(this, tree.hosts, StopAllServers).start();
    }//GEN-LAST:event_stopServersBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void startServersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServersBtnActionPerformed
        new ServerCmdThread(this, tree.hosts, StartAllServers).start();
    }//GEN-LAST:event_startServersBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void newBranchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBranchBtnActionPerformed

        String branch_name = (String) JOptionPane.showInputDialog(this,
                "New Branch Name",
                "Input Dialog",
                JOptionPane.INFORMATION_MESSAGE,
                null, null, "");
        if (branch_name != null) {
            tree.addBranch(branch_name);
        }
    }//GEN-LAST:event_newBranchBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpActionPerformed
        String item = evt.getActionCommand();

        if (item.equals(principleBtn.getText()))
            Utils.popupMessage(this, "", img_path + "principle.gif");
        else if (item.equals(distributionBtn.getText()))
            new HostsScanThread(this, tree.hosts).start();
        else if (item.equals(stateIconsBtn.getText()))
            Utils.popupMessage(this, "", img_path + "astor_state_icons.jpg");
        else if (item.equals(releaseNoteBtn.getText()))
            new PopupHtml(this).show(ReleaseNote.str);
        else if (item.equals(tangorbBtn.getText()))
			displayTangORBversion();
        else if (item.equals(aboutBtn.getText()))
			displayAboutAstor();
        else if (item.equals(starterEventsItem.getText()))
            displaySubscribedHostList(true);
        else if (item.equals(starterNoEventsItem.getText()))
            displaySubscribedHostList(false);
        else
            Utils.popupMessage(this, "Not implemented yet !");
    }//GEN-LAST:event_helpActionPerformed

    //======================================================================
    //======================================================================
	private void displayTangORBversion() {
       TangORBversion tangorb;
        try {
            tangorb = new TangORBversion();
        } catch (Exception e) {
            ErrorPane.showErrorMessage(this,
                    "Cannot check TangORB revision", e);
            return;
        }
        String message = tangorb.jarfile + ":\n\n" + tangorb;
        PopupText txt = new PopupText(this, true);
        txt.setFont(new java.awt.Font("Courier", 1, 14));
        AstorUtil.centerDialog(txt, this);
        txt.show(message);
	}
    //======================================================================
    //======================================================================
	private void displayAboutAstor() {
        String message =
                "           Astor  (Tango Manager) \n\n" +
                        "This programme is used to control, start and stop\n" +
                        "           the TANGO device servers. \n\n" +
                        revNumber +
                        "\n\n" +
                        "Pascal Verdier - Software Engineering Group - ESRF";
        Utils.popupMessage(this, message, img_path + "tango_icon.jpg");
	}
    //======================================================================
    //======================================================================
    private void displaySubscribedHostList(boolean onEvt) {
        ArrayList<String> hostsList = new ArrayList<String>();
        for (TangoHost host : tree.hosts) {
            if (onEvt) {
				if (host.onEvents)
                	hostsList.add(host.getName() + " " + host.eventSource);
			}
            else {
 				if (!host.onEvents)
					hostsList.add(host.getName());
			}
        }

        String  title;
        StringBuilder message = new StringBuilder();
        if (hostsList.size() == 0) {
            title = "There is no host controlled " + TangoHost.controlMethod(onEvt);
        }
        else if (hostsList.size() == tree.hosts.length) {
            title = "All hosts are controlled " + TangoHost.controlMethod(onEvt);
        }
        else {
            title = "On " + tree.hosts.length + " hosts,\n" +
                    hostsList.size() + "  are controlled " + TangoHost.controlMethod(onEvt);

            for (String hostName :  hostsList)
                message.append(hostName).append('\n');
        }
        if (message.length()==0)
            Utils.popupMessage(this, title);
        else {
            PopupText   ppt = new PopupText(this, true);
            ppt.setTitle(title);
            ppt.addText(message.toString());
            ppt.setSize(360, 400);
            ppt.setVisible(true);
        }
    }
    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void expandBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandBtnActionPerformed
        expanded = !expanded;
        tree.expand(expanded);
        if (expanded)
            expandBtn.setText("Collapse Tree");
        else
            expandBtn.setText("Expand Tree");
    }//GEN-LAST:event_expandBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void deviceBrowserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deviceBrowserBtnActionPerformed
        if (dev_browser == null)
            dev_browser = new DevBrowser(this);
        dev_browser.setVisible(true);
    }//GEN-LAST:event_deviceBrowserBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        try {
            buildTree();
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }//GEN-LAST:event_refreshBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        doExit();
    }//GEN-LAST:event_exitBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        doExit();
    }//GEN-LAST:event_exitForm

    //======================================================================
    //======================================================================
    private WideSearchDialog wide_search_dlg = null;

    @SuppressWarnings({"UnusedDeclaration"})
    private void findObjectByFilterItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findObjectByFilterItemActionPerformed

        if (wide_search_dlg == null)
            wide_search_dlg = new WideSearchDialog(this);
        wide_search_dlg.setVisible(true);

    }//GEN-LAST:event_findObjectByFilterItemActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void multiServersCmdItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiServersCmdItemActionPerformed
        try {
            if (multiServerCommand == null) {
                multiServerCommand = new MultiServerCommand(this);
            }
            multiServerCommand.setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }//GEN-LAST:event_multiServersCmdItemActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void statisticsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statisticsBtnActionPerformed
        StatisticsPanel statisticsPanel = new StatisticsPanel(this);
        statisticsPanel.readAndDisplayStatistics(null); //  On all Servers
        statisticsPanel.setVisible(true);
    }//GEN-LAST:event_statisticsBtnActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void serverUsageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverUsageMenuItemActionPerformed
        try {
            new ServerUsageDialog(this).setVisible(true);
        } catch (DevFailed e) {
            ErrorPane.showErrorMessage(this, null, e);
        }
    }//GEN-LAST:event_serverUsageMenuItemActionPerformed

    //======================================================================
    //======================================================================
    @SuppressWarnings({"ConstantConditions"})
    private void stopThreads() {
        System.out.println("Astor exiting....");
        //	Stop all host controled
        if (tree != null && tree.hosts != null) {
            for (TangoHost host : tree.hosts) {
                //	Display a little timer during unsubscribe
                host.stopThread();
                if (host.onEvents) {
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {/* Do nothing */}
                }
            }
        }
        System.out.println(" ");
    }

    //======================================================================
    //======================================================================
    public void doExit() {
        if (dev_browser != null && dev_browser.isVisible())
            setVisible(false);
        else {
            setVisible(false);
            stopThreads();
            System.exit(0);
        }
    }

    //======================================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutBtn;
    private javax.swing.JMenuItem accessControlBtn;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JMenuItem changeTgHostBtn;
    private javax.swing.JMenu cmdMenu;
    private javax.swing.JMenuItem ctrlPreferenceBtn;
    private javax.swing.JMenuItem deviceBrowserBtn;
    private javax.swing.JMenuItem distributionBtn;
    private javax.swing.JMenuItem exitBtn;
    private javax.swing.JMenuItem expandBtn;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem jiveMenuItem;
    private javax.swing.JMenuItem logviewerMenuItem;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JMenuItem multiServersCmdItem;
    private javax.swing.JMenuItem newBranchBtn;
    private javax.swing.JMenuItem newHostBtn;
    private javax.swing.JMenuItem principleBtn;
    private javax.swing.JMenuItem releaseNoteBtn;
    private javax.swing.JMenuItem starterEventsItem;
    private javax.swing.JMenuItem starterNoEventsItem;
    private javax.swing.JMenuItem stateIconsBtn;
    private javax.swing.JMenuItem tangorbBtn;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JPanel topPanel;
    private javax.swing.JMenuItem usePreferenceBtn;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
    //======================================================================

    //======================================================================
    /**
     * @param args the command line arguments
     */
    //======================================================================
    public static void main(String args[]) {

        //	Check if line command
        if (args.length > 0) {
            if (args[0].equals("-ro")) {
                System.out.println("Astor is in READ_ONLY mode !!!");
                rwMode = READ_ONLY;
            }
            else
            if (args[0].equals("-db_ro")) {
                System.out.println("Astor is in DB_READ_ONLY mode !!!");
                rwMode = DB_READ_ONLY;
            }
            else
            if (args[0].equals("-rw")) {
                System.out.println("Astor is in READ_WRITE mode !!!");
                rwMode = READ_WRITE;
            }
            else {
                try {
                    new AstorCmdLine(args);
                } catch (DevFailed e) {
                    Except.print_exception(e);
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }
        //	Else start application


        long t0 = System.currentTimeMillis();
        //	First time, Open a simple Tango window
        //noinspection ErrorNotRethrown
        try {
            Astor astor = new Astor();
            astor.setVisible(true);
            Astor.displayed = true;
        } catch (DevFailed e) {
            System.out.println(e);
            if (e.errors[0].desc.indexOf("Controlled access service defined in Db but unreachable") > 0)
                e.errors[0].desc = "Controlled access service defined in Db but unreachable\n" +
                        "Astor cannot be configured from database !";

            ErrorPane.showErrorMessage(new JFrame(), null, e);
            System.exit(-1);
        } catch (java.lang.InternalError e) {
            System.out.println(e);
        } catch (java.awt.HeadlessException e) {
            System.out.println(e);
        }
        long t1 = System.currentTimeMillis();
        System.out.println("Build  GUI :" + (t1 - t0) + " ms");
    }
    //===============================================================
    //===============================================================





    //===============================================================
    /**
     * A thread class to execute a hosts scan
     */
    //===============================================================
    class HostsScanThread extends Thread {
        private JFrame parent;
        private TangoHost[] hosts;

        //===============================================================
        HostsScanThread(JFrame parent, TangoHost[] hosts) {
            this.parent = parent;
            this.hosts = hosts;
        }

        //===============================================================
        public void run() {
            String[][] list = new String[hosts.length][];
            String message = "Scanning hosts...";
            Monitor monitor = new Monitor(parent, message);
            double ratio = 0.01;
            monitor.setProgressValue(ratio, "Starting...");
            try {
                sleep(500);
            } catch (InterruptedException e) { /** */}
            try {
                int nb_serv = 0;
                for (int i = 0; i < hosts.length; i++) {
                    ratio = (1 + 1.0 * i) / hosts.length;
                    monitor.setProgressValue(ratio,
                            "Reading " + hosts[i].getName());

                    String[] servers = hosts[i].getServerAttribute();

                    list[i] = new String[2];
                    list[i][0] = hosts[i].getName();
                    list[i][1] = "" + servers.length;
                    nb_serv += servers.length;
                }

                //	Format results
                String title = nb_serv + " servers   on " +
                        hosts.length + " hosts";
                String[] cols = new String[]{"Names", "Nb Servers"};

                PopupTable table = new PopupTable(parent, title, cols, list);
                table.setColumnWidth(new int[]{200, 100});
                table.setVisible(true);
            } catch (DevFailed e) {
                ErrorPane.showErrorMessage(parent, "", e);
            }
        }
    }
}
