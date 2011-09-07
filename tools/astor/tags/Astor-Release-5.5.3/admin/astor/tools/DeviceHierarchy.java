//+======================================================================
// $Source:  $
//
// Project:   Tango
//
// Description:  java source code for Tango manager tool..
//
// $Author$
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2010,2011
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

import admin.astor.*;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.DeviceInfo;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoDs.Except;
import fr.esrf.tangoatk.widget.util.ErrorPane;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import java.util.Vector;


public class  DeviceHierarchy  extends JTree implements AstorDefs
{
	private static ImageIcon	root_icon;
	private Astor				astor = null;
	private boolean				running = true;
	private Device[]			devices  = null;
	private String				rootname = null;
	private DefaultMutableTreeNode  	root;
	private DeviceHierarchyPopupMenu	menu;
	private DeviceHierarchyDialog		parent;

	private static final Color	background = new Color(0xf0, 0xf0, 0xf0);
	private static final String	SUB_DEV_PROP_NAME = "__SubDevices";
	private static final boolean CHECK_SUB = true;
	//===============================================================
	//===============================================================
	public DeviceHierarchy(DeviceHierarchyDialog parent, Astor astor, String name) throws DevFailed
	{
		super();
		this.parent = parent;
		this.astor  = astor;
		setBackground(background);

		initNames(name);
		buildTree();
		menu = new DeviceHierarchyPopupMenu(this);
		expandChildren(root);
		setSelectionPath(null);
	 }
	//===============================================================
	private void initNames(String name) throws DevFailed
	{
		StringTokenizer	stk = new StringTokenizer(name, "/");
		Vector	v = new Vector();
		while (stk.hasMoreTokens())
			v.add(stk.nextToken());
		String[]	devnames = new String[0];
		switch(v.size())
		{
		case 2:	//	Is a server name
			rootname = v.get(0) + "/" + v.get(1);
			String		admin = "dserver/" + rootname;
			String[]	tmp = new TangoServer(admin).queryDeviceFromDb();
			devnames = new String[tmp.length+1];
			System.arraycopy(tmp, 0, devnames, 0, tmp.length);
			devnames [tmp.length] = admin; //	Add amdin dev in last position
			break;
		case 3:	//	Is a device name
			rootname = "Device";
			devnames = new String[1];
			devnames[0] = v.get(0) + "/" + v.get(1) + "/" + v.get(2);
			break;
		default:
			Except.throw_exception("BAD_PARAMETER",
					"Bad device or server name",
					"DeviceHierarchy.initNames()");
		}

		devices = new Device[devnames.length];
		for (int i=0 ; i<devnames.length ; i++)
			devices[i] = new Device(null, devnames[i], CHECK_SUB);
		new Refresher().start();
	}
	//===============================================================
	//===============================================================
	private void buildTree()
	{
		//  Create the nodes.
		root = new DefaultMutableTreeNode(rootname);
		createDeviceNodes();

		//	Create the tree that allows one selection at a time.
		getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//	Create Tree and Tree model
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		setModel(treeModel);

		//Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(this);

		//  Set the icon for leaf nodes.
		TangoRenderer renderer = new TangoRenderer();
		setCellRenderer(renderer);

		 //	Listen for collapse tree
		addTreeExpansionListener(new TreeExpansionListener () {
			public void treeCollapsed(TreeExpansionEvent e) {
				//collapsedPerfomed(e);
			}
			public void treeExpanded(TreeExpansionEvent e) {
				expandedPerfomed(e);
			}
		});
		//	Add Action listener
		addMouseListener (new java.awt.event.MouseAdapter () {
			public void mouseClicked (java.awt.event.MouseEvent evt) {
				treeMouseClicked (evt);	//	for tree clicked, menu,...
			}
		});
	}
	//======================================================
	/**
	 *	Manage event on clicked mouse on JTree object.
	 */
	//======================================================
	private void treeMouseClicked (java.awt.event.MouseEvent evt)
	{
		//	Set selection at mouse position
		TreePath	selectedPath = getPathForLocation(evt.getX(), evt.getY());
		if (selectedPath==null)
			return;

		DefaultMutableTreeNode	node =
		(DefaultMutableTreeNode) selectedPath.getPathComponent(selectedPath.getPathCount()-1);
		Object	o = node.getUserObject();
		int mask = evt.getModifiers();

		//  Check button clicked
		if(evt.getClickCount()==2 && (mask & MouseEvent.BUTTON1_MASK)!=0)
		{
		}
		else
		if ((mask & MouseEvent.BUTTON1_MASK)!=0)
		{
			deviceInfo();
		}
		else
		if ((mask & MouseEvent.BUTTON3_MASK)!=0)
		{
			if (node == root)
				 menu.showMenu(evt, (String)o);
			else
			if (o instanceof Device)
				menu.showMenu(evt, (Device)o);
	   }
	}
	//===============================================================
	//===============================================================
	public void expandedPerfomed(TreeExpansionEvent evt)
	{
	}
	//===============================================================
	//===============================================================
	private void createDeviceNodes(DefaultMutableTreeNode parent_node, Device device)
	{
		DefaultMutableTreeNode	node = new DefaultMutableTreeNode(device);
		parent_node.add(node);
		for (int i=0 ; i<device.size() ; i++)
			createDeviceNodes(node, device.getDevice(i));
	}
	//===============================================================
	//===============================================================
	private void createDeviceNodes()
	{
		for (Device device : devices)
			createDeviceNodes(root, device);
	}
	//======================================================
	//======================================================
	private DefaultMutableTreeNode getSelectedNode()
	{
		return (DefaultMutableTreeNode) getLastSelectedPathComponent();
	}

	//======================================================
	//======================================================
	Object getSelectedObject()
	{
		DefaultMutableTreeNode	node = getSelectedNode();
		if (node==null)
			return null;
		return node.getUserObject();
	}
	//===============================================================
	//===============================================================
	private void expandChildren(DefaultMutableTreeNode node)
	{
		boolean	level_done = false;
		for (int i=0 ; i<node.getChildCount() ; i++)
		{
			DefaultMutableTreeNode child =
					(DefaultMutableTreeNode) node.getChildAt(i);
			if (child.isLeaf())
			{
				if (!level_done)
				{
					expandNode(child);
					level_done = true;
				}
			}
			else
				expandChildren(child);
		}
	}
	//===============================================================
	//===============================================================
	private void expandNode(DefaultMutableTreeNode node)
	{
		Vector	v = new Vector();
		v.add(node);
		while (node!=root)
		{
			node = (DefaultMutableTreeNode) node.getParent();
			v.insertElementAt(node, 0);
		}
		TreeNode[]    tn = new DefaultMutableTreeNode[v.size()];
		for (int i=0 ; i<v.size() ; i++)
			tn[i] = (TreeNode)v.get(i);
		TreePath      tp = new TreePath(tn);
		setSelectionPath(tp);
		scrollPathToVisible(tp);
	}
	//===============================================================
	//===============================================================
	private void testDevice()
	{
		Object	obj = getSelectedObject();
		if (obj instanceof Device)
		{
			AstorUtil.testDevice(parent, ((Device)obj).name);
		}
	}
	//===============================================================
	//===============================================================
	private void deviceInfo()
	{
		Object	obj = getSelectedObject();
		if (obj instanceof Device)
		{
			try
			{
				String	info = ((Device)obj).getInfo();
				parent.setText(info);
			}
			catch(DevFailed e)
			{
				parent.setText(Except.str_exception(e));
			}
		}
	}
	//===============================================================
	//===============================================================
	private void remoteShell()
	{
		Object	obj = getSelectedObject();
		if (obj instanceof Device)
		{
			try
			{
				String	hostname = ((Device)obj).getHost();
				new RemoteLoginThread(hostname, parent).start();
			}
			catch(DevFailed e)
			{
				parent.setText(Except.str_exception(e));
			}
		}
	}
	//===============================================================
	//===============================================================
	private void hostPanel()
	{
		Object	obj = getSelectedObject();
		if (obj instanceof Device)
		{
			try
			{
				Device	device = (Device) obj;
				astor.tree.displayHostInfoDialog(device.getHost());
			}
			catch(DevFailed e)
			{
				ErrorPane.showErrorMessage(parent, null, e);
			}
		}
	}
	//===============================================================
	//===============================================================
	void stopThread()
	{
		running = false;
	}
	//===============================================================
	//===============================================================
	public String toString()
	{
		StringBuffer	sb = new StringBuffer();
		for (Device dev : devices)
		{
			sb.append(dev.toFullString());
			for (int i=0 ; i<dev.size() ; i++)
				sb.append(dev.getDevice(i).toFullString());
		}
		return sb.toString();
	}
	//===============================================================
	//===============================================================







	//===============================================================
	/**
	 *	Renderer Class
	 */
	//===============================================================
	private class TangoRenderer extends DefaultTreeCellRenderer
	{
		private Font[]		fonts;

		private final int	TITLE  = 0;
		private final int	COLLEC = 1;
		private final int	LEAF   = 2;

		//===============================================================
		//===============================================================
		public TangoRenderer()
		{
			Utils	utils = Utils.getInstance();
			root_icon      = utils.getIcon("network5.gif");

			fonts = new Font[LEAF+1];
			fonts[TITLE]  = new Font("Dialog", Font.BOLD, 18);
			fonts[COLLEC] = new Font("Dialog", Font.BOLD, 12);
			fonts[LEAF]   = new Font("Dialog", Font.PLAIN, 12);
		}

		//===============================================================
		//===============================================================
		Cursor getNodeCursor(String filename)
		{
			java.net.URL	url =
				getClass().getResource(Utils.img_path + filename);
			Image	image = Toolkit.getDefaultToolkit().getImage(url);
			return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), filename);
		}
		//===============================================================
		//===============================================================
		public Component getTreeCellRendererComponent(
							JTree tree,
							Object obj,
							boolean sel,
							boolean expanded,
							boolean leaf,
							int row,
							boolean hasFocus) {

			super.getTreeCellRendererComponent(
							tree, obj, sel,
							expanded, leaf, row,
							hasFocus);

			setBackgroundNonSelectionColor(background);
			setForeground(Color.black);
			setBackgroundSelectionColor(Color.lightGray);
			if (row==0)
			{
				//	ROOT
				setFont(fonts[TITLE]);
				setIcon(root_icon);
			}
			else
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)obj;

				if (node.getUserObject() instanceof Device)
				{
					setFont(fonts[COLLEC]);
					Device device = (Device) node.getUserObject();
					setIcon(AstorUtil.state_icons[device.state]);
					if (device.too_old)
						setBackgroundNonSelectionColor(Color.yellow);
				}
			}
			return this;
		}
	}
	//==============================================================================
	//	End of Renderer Class
	//==============================================================================




	//==============================================================================
	/*
	 *	The Popup menu class
	 */
	//==============================================================================
	static private final int	UPDATE        = 0;
	static private final int	TEST_DEVICE   = 1;
	static private final int	HOST_PANEL    = 2;
	static private final int	REM_LOGIN     = 3;
	static private final int	OFFSET = 2;	//	Label And separator

	static private String[]	menuLabels = {
								"Update Dependencies",
								"Test Device",
								"Host Panel",
								"Remote Login",
						  };
	private class DeviceHierarchyPopupMenu extends JPopupMenu
	{
		private JTree	tree;
		private JLabel	title;
		//=======================================================
		/**
		 *	Create a Popup menu
		 */
		//=======================================================
		private DeviceHierarchyPopupMenu(JTree tree)
		{
			this.tree = tree;
			buildBtnPopupMenu();
		}
		//=======================================================
		//=======================================================
		private void buildBtnPopupMenu()
		{
			title = new JLabel();
			title.setFont(new java.awt.Font("Dialog", 1, 16));
			add(title);
			add(new JPopupMenu.Separator());

			for (String menuLabel : menuLabels)
			{
				if (menuLabel == null)
					add(new Separator());
				else
				{
					JMenuItem btn = new JMenuItem(menuLabel);
					btn.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent evt)
						{
							deviceActionPerformed(evt);
						}
					});
					add(btn);
				}
			}
		}
		//======================================================
		/**
		 *	Show menu on root
		 */
		//======================================================
		public void showMenu(MouseEvent evt, String name)
		{
			//	Set selection at mouse position
			TreePath	selectedPath =
				tree.getPathForLocation(evt.getX(), evt.getY());
			if (selectedPath==null)
				return;
			tree.setSelectionPath(selectedPath);

			title.setText(name);

			//	Reset all items
			for (int i=0 ; i<menuLabels.length ; i++)
				getComponent(OFFSET+i).setVisible(false);

			getComponent(OFFSET /* +UPDATE*/).setVisible(true);

			show(tree, evt.getX(), evt.getY());
		}
		//======================================================
		/**
		 *	Show menu on Collection
		 */
		//======================================================
		public void showMenu(MouseEvent evt, Device dev)
		{
			//	Set selection at mouse position
			TreePath	selectedPath =
				tree.getPathForLocation(evt.getX(), evt.getY());
			if (selectedPath==null)
				return;
			tree.setSelectionPath(selectedPath);
			deviceInfo();

			//	Display menu only if new DeviceProxy did not failed.
			if (dev.failed==null)
			{
				title.setText(dev.toString());

				//	Reset all items
				getComponent(OFFSET /* + UPDATE*/).setVisible(false);
				for (int i=REM_LOGIN ; i<menuLabels.length ; i++)
					getComponent(OFFSET+i).setEnabled(false);

				getComponent(OFFSET+REM_LOGIN).setEnabled(AstorUtil.osIsUnix());
				getComponent(OFFSET+HOST_PANEL).setEnabled(astor!=null);
				getComponent(OFFSET+TEST_DEVICE).setEnabled(dev.state==all_ok);
				show(tree, evt.getX(), evt.getY());
			}
		}
		//======================================================
		private void deviceActionPerformed(ActionEvent evt)
		{
			 //	Check component source
			Object	obj = evt.getSource();
			int     cmdidx = 0;
			for (int i=0 ; i<menuLabels.length ; i++)
				if (getComponent(OFFSET+i)==obj)
					cmdidx = i;

			switch (cmdidx)
			{
			case UPDATE:
				parent.update();
				break;
			case REM_LOGIN:
				if (AstorUtil.osIsUnix())
					remoteShell();
				break;
			case HOST_PANEL:
				hostPanel();
				break;
			case TEST_DEVICE:
				testDevice();
				break;
			}
		}
	}


	//===============================================================
	/*
	 *	Device object definition, containing its subdevices
	 */
	//===============================================================
	private class Device extends Vector<Device>
	{
		String		name;
		DeviceProxy	proxy;
		DevFailed	failed = null;
		boolean		too_old = false;
		short		state = unknown;
		Device		parent;
		//===========================================================
		private Device(Device parent, String name, boolean check_sub)
		{
			AstorUtil.increaseSplashProgress(5, "checking for " + name);
			try
			{
				this.parent = parent;
				this.name   = name;
				proxy = new DeviceProxy(name);

				if (check_sub)
				{
					//	Try to ask to the admin device
					//	noinspection NestedTryStatement
					try
					{
						DeviceData	argout = proxy.get_adm_dev().command_inout("QuerySubDevice");
						String[]	dependencies = argout.extractStringArray();
						for (String dependency : dependencies)
						{
							//	get sub device only for this device
							int idx = dependency.indexOf(' ');
							if (idx>0)
							{
								String	org = dependency.substring(0, idx).trim();
								boolean	exists = (parent!=null &&parent.alreadyHave(dependency.substring(idx).trim()));
								if (org.toLowerCase().equals(name.toLowerCase()))
									add(new Device(this, dependency.substring(idx).trim(), !exists));
							}
							else
							{
								//	This case is sub-devices not attached to device
								//	(e.g. in case of custum thread has sub-devices)
								//	Attach it on admin device
								if (name.startsWith("dserver/"))
									add(new Device(this, dependency, CHECK_SUB));
							}
						}
					}
					catch (DevFailed e)
					{
						if (e.errors[0].reason.equals("API_CommandNotFound"))
						{
							too_old = true;
						}
						else
						{
							//	If failed, check on property
							DbDatum datum = proxy.get_property(SUB_DEV_PROP_NAME);
							if (!datum.is_empty())
							{
								String[]	dependencies = datum.extractStringArray();
								for (String dependency : dependencies)
									if (parent==null)
										add(new Device(this, dependency, CHECK_SUB));
									else
									{
										//	to do not have a non ending loop
										boolean	exists = parent.alreadyHave(dependency);
										add(new Device(this, dependency, !exists));
									}
							}
						}
					}
				}
				new StateManager().start();
			}
			catch (DevFailed e)
			{
				System.err.println(e.errors[0].desc);
				failed = e;
			}
		}
		//===========================================================
		private boolean alreadyHave(String name)
		{
			//System.out.println(parent + " ->	" + this.name  + " == " + name);
			//	Check itself
			if (this.name.equals(name))
				return true;

			//	Check it's children
			/*
			for (Device dev : this)
				if (dev.name.equals(name))
					return true;
			*/

			//	Check it's own parent
			if (parent==null)
				return false;
			else
				return parent.alreadyHave(name);
		}
		//===========================================================
		private Device getDevice(int i)
		{
			return get(i);
		}
		//===========================================================
		private String toFullString()
		{
			StringBuffer	sb = new StringBuffer(name);
			sb.append('\n');
			for (int i=0 ; i<size() ; i++)
			{
				Device	d = getDevice(i);
				sb.append('\t').append(d.name).append('\n');
			}
			sb.append("------------------------------------------------");
			return sb.toString();
		}
		//===========================================================
		private class StateManager extends Thread
		{
			public void run()
			{
				while (running)
				{
					int	previous = state;
					try	{
						proxy.ping();
						state = all_ok;
					}
					catch(DevFailed e) {
						state = faulty;
					}
					if (state!=previous)
						refresh = true;
					try { sleep(2000); } catch (InterruptedException e){}
				}
			}
		}
		//===========================================================
		private String getHost() throws DevFailed
		{
			DeviceInfo	info = proxy.get_info();
			return info.hostname;
		}
		//===========================================================
		private String getInfo() throws DevFailed
		{
			if (failed!=null)
				return Except.str_exception(failed);
			else
			{
				StringBuffer	sb =  new StringBuffer();
				if (too_old)
					sb.append("   WARNING:  Too Old TANGO Release \n")
					  .append("             To get dependencies !!!\n")
				      .append("\n========================================================\n");
				sb.append(proxy.get_info().toString());
				sb.append("\n========================================================\n");

				try
				{
					String	s = proxy.status();
					sb.append(s);
				}
				catch (DevFailed e)
				{
					sb.append(Except.str_exception(e));
				}
				return sb.toString();
			}
		}
		//===========================================================
		public String toString()
		{
			return name;
		}
		//===========================================================
	}
	//===========================================================
	//	End of Device class
	//===========================================================

	//===========================================================
	/*
	 *	A little thread to repaint the tree
	 */
	//===========================================================
	private boolean	refresh = false;
	private class Refresher extends Thread
	{
		public void run()
		{
			while (running)
			{
				if (refresh)
				{
					refresh = false;
					repaint();
				}
				try { sleep(2000); } catch (InterruptedException e){}
			}
		}
	}

}
