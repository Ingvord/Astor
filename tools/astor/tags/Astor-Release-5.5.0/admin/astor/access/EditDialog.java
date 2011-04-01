//+======================================================================
// $Source$
//
// Project:   Tango
//
// Description:  Basic Dialog Class to display info
//
// $Author$
//
// $Revision$
// $Log$
// Revision 1.4  2011/01/04 14:34:18  pascal_verdier
// Password added for access control dialog.
//
// Revision 1.3  2008/11/19 10:01:34  pascal_verdier
// New tests done on Access control.
// Allowed commands tree added.
//
// Revision 1.2  2006/10/02 14:09:02  pascal_verdier
// Minor changes.
//
// Revision 1.1  2006/09/19 13:06:47  pascal_verdier
// Access control manager added.
//
//
// Copyleft 2005 by European Synchrotron Radiation Facility, Grenoble, France
//               All Rights Reversed
//-======================================================================
package	admin.astor.access;



import fr.esrf.Tango.DevFailed;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

//===============================================================
/**
 *	Class Description: Basic Dialog Class to display info
 *
 *	@author  Pascal Verdier
 */
//===============================================================


public class EditDialog extends JDialog
{
	private int		retVal = JOptionPane.OK_OPTION;

    static final int	CHECK_ACCESS = 0;
    static final int	EDIT_USER    = 1;
    private int         mode = EDIT_USER;

    private static String[]   titles;
    private static final String[]   check_titles = { "User Name", "IP Address", "Device" };
    private static final String[]   edit_titles = { "User Name", "Allowed Address", "Device" };
    static final int        USER    = 0;
    static final int        ADDRESS = 1;
    static final int        DEVICE  = 2;

    private AccessProxy     access_dev;
    private	JTextField[]	textFields;
    private JLabel          check_result;
    private JLabel[]        labels;
    //===============================================================
	/**
	 *	Creates new form EditDialog
     * @param parent    JFrame parent instance
     * @param user      default name for users
     * @param address   default address
     */
	//===============================================================
	public EditDialog(JFrame parent, String user, String address)
	{
		super(parent, true);
        mode = EDIT_USER;
        titles = edit_titles;
        initComponents();
		initOwnComponents(titles);

        labels[DEVICE].setVisible(false);
        textFields[DEVICE].setVisible(false);
        textFields[USER].setText(user);
        textFields[USER].setText(user);
        textFields[ADDRESS].setText(address);

        titleLabel.setText("");
		pack();
		
		//	Center dialog
		Point	p = parent.getLocationOnScreen();
		p.x += ((parent.getWidth() - this.getWidth())  / 2);
		p.y += ((parent.getHeight() - this.getHeight())  / 2);
        if (p.y<=0) p.y=20;
        if (p.x<=0) p.x=20;
		this.setLocation(p);
	}
    //===============================================================
	/**
	 *	Creates new form EditDialog
     * @param parent JFrame parent instance
     * @param access_dev access device parameters
	 */
	//===============================================================
	public EditDialog(JFrame parent, AccessProxy access_dev)
	{
		super(parent, true);
        mode = CHECK_ACCESS;
        titles = check_titles;
        this.access_dev = access_dev;
        initComponents();
		initOwnComponents(titles);

		titleLabel.setText("");
		pack();

		//	Center dialog
		Point	p = parent.getLocationOnScreen();
		p.x += ((parent.getWidth() - this.getWidth())  / 2);
		p.y += ((parent.getHeight() - this.getHeight())  / 2);
        if (p.y<=0) p.y=20;
        if (p.x<=0) p.x=20;
		this.setLocation(p);
	}
	//===============================================================
	//===============================================================
    private void initOwnComponents(String[] titles)
	{
        GridBagConstraints gbc = new GridBagConstraints ();

		textFields = new JTextField[titles.length];
		labels = new JLabel[titles.length];
        for (int i=0 ; i<titles.length ; i++)
		{
			labels[i] = new JLabel(titles[i] + ":  ");
        	gbc.gridx = 0;
        	gbc.gridy = i;
			gbc.fill  = GridBagConstraints.HORIZONTAL;
        	jPanel3.add (labels[i], gbc);

            textFields[i] = new JTextField();
            textFields[i].setColumns(20);
            textFields[i].addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    textKeyPressed(evt);
                }
             });
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.fill  = GridBagConstraints.HORIZONTAL;
            jPanel3.add (textFields[i], gbc);
		}
	}
	//===============================================================
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
	//===============================================================
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

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

        jPanel3.setLayout(new java.awt.GridBagLayout());

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	//===============================================================
	//===============================================================
    private boolean checkInputs()
    {
        String  user = textFields[USER].getText().trim().toLowerCase();
        String  add  = textFields[ADDRESS].getText().trim();
        String  dev  = textFields[DEVICE].getText().trim().toLowerCase();
        if (user.length()==0 || add.length()==0 || (mode==CHECK_ACCESS && dev.length()==0))
        {
            admin.astor.tools.Utils.popupError(this, "Please fill all fields !");
            return false;
        }
        //  set after trim
        textFields[USER].setText(user);
        textFields[DEVICE].setText(dev);
        textFields[ADDRESS].setText(add);

		//	Check if host name as address
		try {
	 		java.net.InetAddress	iadd = 
				java.net.InetAddress.getByName(add);

			//	If found replace by address
			add = iadd.getHostAddress();
			textFields[ADDRESS].setText(add);
		}
		catch(Exception e) { /* */ }

        //  Check dev name
        Vector<String>  v = new Vector<String>();
        if (mode==CHECK_ACCESS) {
 			//	Try to split with '.' separator
           StringTokenizer stk = new StringTokenizer(dev, "/");
            while (stk.hasMoreTokens())
                v.add(stk.nextToken());
            if (v.size()>3){
                admin.astor.tools.Utils.popupError(this, "Incorrect device name  (too many members)");
                return false;
            }
            if (v.size()<3) {
                admin.astor.tools.Utils.popupError(this, "Incorrect device name  (not enough members)");
                return false;
            }
         }
        //  check IP add name
        StringTokenizer stk1 = new StringTokenizer(add, ".");
        v.clear();
        while (stk1.hasMoreTokens())
            v.add(stk1.nextToken());
        if (v.size()>4) {
            admin.astor.tools.Utils.popupError(this, "Incorrect IP address  (Too many members)");
            return false;
        }
        else
        if (v.size()<4) {
            admin.astor.tools.Utils.popupError(this, "Incorrect IP address  (not enougth members)");
            return false;
        }
		//	rebuild add string to be sure that there is no too much '.'
		//		like xxx.xxx....xx....xx
		add = v.get(0) + "." + v.get(1) + "." + v.get(2) + "." +v.get(3);
		textFields[ADDRESS].setText(add);	

        for (int i=0 ; i<v.size() ; i++) {
            //  Check if numbers
            try {
                Short.parseShort((String)v.get(i));
            }
            catch(NumberFormatException e) {
                //  Or if wildcard
                if (!v.get(i).equals("*"))
                {
                    admin.astor.tools.Utils.popupError(this, "Incorrect IP address  (member #" +
                                (i+1) + " (" + v.get(i) + ") is not a number)");
                    return false;
                }
            }
        }
        return true;
     }
    //===============================================================
	//===============================================================
    private void checkAccess()
    {
        try {
            //  Check security
            String  result = access_dev.getAccess(getInputs());
            check_result.setText(result);
            if (result.equals("read"))
                check_result.setIcon(UsersTree.read_icon);
            else
                check_result.setIcon(UsersTree.write_icon);
        }
        catch(DevFailed e)  {
            fr.esrf.tangoatk.widget.util.ErrorPane.showErrorMessage(this, "Cannot check TANGO Access", e);
        }
    }
    //===============================================================
    //===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void textKeyPressed(java.awt.event.KeyEvent evt) {
        int c = (int)evt.getKeyChar();
        //System.out.println("textInputChanged() " + c);
        switch (c)
        {
        case 27:
            //  Equivalent to cancel
            retVal = JOptionPane.CANCEL_OPTION;
            doClose();
            break;
        case 10:
            switch(mode)
            {
            case EDIT_USER:
                //  Equivalent to okBtn
                if (checkInputs())
                {
                    retVal = JOptionPane.OK_OPTION;
                    doClose();
                }
                break;
            case CHECK_ACCESS:
                if (checkInputs())
                    checkAccess();
                break;
            }
            break;
        default:
            if (mode==CHECK_ACCESS)
            {
                check_result.setText("...");
                check_result.setIcon(null);
            }
        }
    }
    //===============================================================
	//===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
	private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
		if (checkInputs())
        {
            retVal = JOptionPane.OK_OPTION;
            //  do not close if test mode
            if (mode==EDIT_USER)
                doClose();
            else
                checkAccess();
        }
    }//GEN-LAST:event_okBtnActionPerformed

	//===============================================================
	//===============================================================
    @SuppressWarnings({"UnusedDeclaration"})
	private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
		retVal = JOptionPane.CANCEL_OPTION;
		doClose();
	}//GEN-LAST:event_cancelBtnActionPerformed

	//===============================================================
	//===============================================================
	@SuppressWarnings({"UnusedDeclaration"})
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
		retVal = JOptionPane.CANCEL_OPTION;
		doClose();
	}//GEN-LAST:event_closeDialog

	//===============================================================
	/**
	 *	Closes the dialog
	 */
	//===============================================================
	private void doClose()
	{
		setVisible(false);
		dispose();
	}
	//===============================================================
	//===============================================================
	public String[] getInputs()
	{
		String[]	val = new String[textFields.length];
		for (int i=0 ; i<textFields.length ; i++)
			val[i] = textFields[i].getText().trim();
		return val;
	}
    //===============================================================
    //===============================================================
    public int showDialog()
    {
        if (mode==CHECK_ACCESS)  {
            try  {
                textFields[USER].setText(System.getProperty("user.name").toLowerCase());
                textFields[ADDRESS].setText(InetAddress.getLocalHost().getHostAddress());
                okBtn.setText("Check");

                //  Add a button to display check result
                GridBagConstraints gbc = new GridBagConstraints ();
                check_result = new JLabel("...");
                gbc.gridx = 1;
                gbc.gridy = titles.length;
                gbc.fill  = GridBagConstraints.HORIZONTAL;
                jPanel3.add (check_result, gbc);
                pack();
            }
            catch (UnknownHostException e)  {
                admin.astor.tools.Utils.popupError(this, null, e);
            }
        }
        setVisible(true);
        return retVal;
    }


	//===============================================================
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton okBtn;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
	//===============================================================

}
