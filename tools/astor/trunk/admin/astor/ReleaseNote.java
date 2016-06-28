//+======================================================================
// $Source:  $
//
// Project:      Tango Manager
//
// Description:  Astor revisions
//
// $Author$
//
// $Revision$
//
//-======================================================================


package admin.astor;


/**
 *	HTML code to display Release Notes for admin.astor package.
 *	It is generated by java classes from a text file.
 */

public interface ReleaseNote {
	public final String	str = 
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\n" + 
		"<HTML>" +
		"<HEAD>" +
		"<Title> Release Note </Title>" +
		"</HEAD>" +
		"<BODY TEXT=\"#000000\" BGCOLOR=\"#FFFFFF\" LINK=\"#0000FF\" VLINK=\"#7F00FF\" ALINK=\"#FF0000\">" +
		"<P><!-------TITLE------></P>" +

		"<Center>	<h2>Astor Release Notes</h2>" + 
			"(Generated Tue Jun 28 10:51:10 CEST 2016)" + 
		"</Center><br>" + 

		"<br><li><b>Astor-6.7.1 -  28/06/16:</li></b>" +
			"	Fix a little bug in startup level management.<br>" +

		"<br><li><b>Astor-6.7.0 -  27/01/16:</li></b>" +
			"	Starter domain name could be set with a class property.<br>" +
			"	Improve \"Tango release for servers\" feature<br>" +

		"<br><li><b>Astor-6.6.6 -  30/06/15:</li></b>" +
			"	Java-7 compatibility.<br>" +
			"	Manage polling for Tango-9 feature.<br>" +
			"	Improve Tango release for servers.<br>" +
			"	Add a compatibility to read JTango release.<br>" +
			"	Update file headers.<br>" +

		"<br><li><b>Astor-6.6.5 -  08/01/15:</li></b>" +
			"	Improve Server Usage utility<br>" +

		"<br><li><b>Astor-6.6.4 -  08/01/15:</li></b>" +
			"	Improve the statup level change.<br>" +

		"<br><li><b>Astor-6.6.3 -  17/12/14:</li></b>" +
			"	Several invokeLater() methods added.<br>" +

		"<br><li><b>Astor-6.6.2 -  18/11/14:</li></b>" +
			"	ULong management added to event tester.<br>" +

		"<br><li><b>Astor-6.6.1 -  04/11/14:</li></b>" +
			"	Faulty host list added to help menu.<br>" +

		"<br><li><b>Astor-6.6.0 -  01/10/14:</li></b>" +
			"	Server Tango release manage Tango-9 and IDL5<br>" +

		"<br><li><b>Astor-6.5.1 -  05/08/14:</li></b>" +
			"	Improve Last Branches management.<br>" +

		"<br><li><b>Astor-6.5.0 -  19/04/14:</li></b>" +
			"	Manage OFF state for Starter devices.<br>" +
			"	Improve DbPollPanel class.<br>" +

		"<br><li><b>Astor-6.4.6 -  29/01/14:</li></b>" +
			"	Package ctrl_system_info added to the jar file.<br>" +

		"<br><li><b>Astor-6.4.5 -  10/01/14:</li></b>" +
			"	Fix a problem on starter properties (HostUsage and Family)<br>" +
			"	Clean compilation warnings.<br>" +

		"<br><li><b>Astor-6.4.3 -  07/01/14:</li></b>" +
			"	Remove warning on tools launch.<br>" +

		"<br><li><b>Astor-6.4.2 -  29/10/13:</li></b>" +
			"	Try to fix infinite ping loop.<br>" +

		"<br><li><b>Astor-6.4.1 -  25/10/13:</li></b>" +
			"	Replace dancers by official Tango logo.<br>" +
			"	Cleanup part of code.<br>" +

		"<br><li><b>Astor-6.4.0 -  24/09/13:</li></b>" +
			"	Fix a problem in Tango releases help.<br>" +
			"	fix a problem on resize with java-7<br>" +

		"<br><li><b>Astor-6.3.9 -  30/08/13:</li></b>" +
			"	Fix problem in pool threads management using new syntax.<br>" +
			"	Improve sort methods.<br>" +
			"	ServerStatePanel is now an astor.tools class (not from tool_panels package)<br>" +

		"<br><li><b>Astor-6.3.7 -  09/08/13:</li></b>" +
			"	Improve UnAvailableHostsDialog class.<br>" +

		"<br><li><b>Astor-6.3.6 -  12/06/13:</li></b>" +
			"	Server list to be started can now be taken from another host.<br>" +

		"<br><li><b>Astor-6.3.5 -  28/05/13:</li></b>" +
			"	Update splash screen with new logos.<br>" +

		"<br><li><b>Astor-6.3.4 -  16/05/13:</li></b>" +
			"	Minor changes.<br>" +

		"<br><li><b>Astor-6.3.3 -  30/04/13:</li></b>" +
			"	Add a tool to export server from database to another one.<br>" +

		"<br><li><b>Astor-6.3.2 -  03/04/13:</li></b>" +
			"	Add a display for statistics reset by host.<br>" +

		"<br><li><b>Astor-6.3.1 -  19/03/13:</li></b>" +
			"	Fix a problem when starter is killed.<br>" +

		"<br><li><b>Astor-6.3.0 -  06/03/13:</li></b>" +
			"	implement READ_ONLY, DB_READ_ONLY, READ_WRITE modes.<br>" +

		"<br><li><b>Astor-6.2.2 -  21/01/13:</li></b>" +
			"	Fix a bug in adding item on TAC<br>" +

		"<br><li><b>Astor-6.2.1 -  14/01/13:</li></b>" +
			"	Pb on notif icon fixed.<br>" +

		"<br><li><b>Astor-6.2.0 -  07/01/13:</li></b>" +
			"	Group management added on TAC panel.<br>" +

		"<br><li><b>Astor-6.1.2 -  26/10/12:</li></b>" +
			"	Fix a problem when too much devices in SubDevices.<br>" +

		"<br><li><b>Astor-6.1.1 -  26/10/12:</li></b>" +
			"	HostInfoDialog can be run in stand alone.<br>" +

		"<br><li><b>Astor-6.1.0 -  24/10/12:</li></b>" +
			"	Tango release for servers tool added.<br>" +

		"<br><li><b>Astor-6.0.6 -  09/10/12:</li></b>" +
			"	Pb with back slash fixed (?)<br>" +

		"<br><li><b>Astor-6.0.5 -  21/08/12:</li></b>" +
			"	Open Jive on a selected server.<br>" +

		"<br><li><b>Astor-6.0.4 -  24/05/12:</li></b>" +
			"	Distribution release.<br>" +

		"<br><li><b>Astor-6.0.3 -  05/04/12:</li></b>" +
			"	Add a control system name management.<br>" +
			"	User preferences for tango host list added.<br>" +

		"<br><li><b>Astor-6.0.1 -  02/04/12:</li></b>" +
			"	Problem when removing server info fixed.<br>" +

		"<br><li><b>Astor-6.0.0 -  08/02/12:</li></b>" +
			"	Comptibility with TangORB-8.x (ZMQ event system or supposed to be).<br>" +

		"<br><li><b>Astor-5.5.4 -  06/12/11:</li></b>" +
			"	Create a Thread to update splash screen (to boost startup).<br>" +

		"<br><li><b>Astor-5.5.3 -  07/09/11:</li></b>" +
			"	ServerUsage tool added.<br>" +

		"<br><li><b>Astor-5.5.2 -  26/07/11:</li></b>" +
			"	Statistics on one host added.<br>" +

		"<br><li><b>Astor-5.5.1 -  12/04/11:</li></b>" +
			"	Auto start info added to statistics tool.<br>" +

		"<br><li><b>Astor-5.5.0 -  01/04/11:</li></b>" +
			"	Statistics tools added.<br>" +

		"<br><li><b>Astor-5.4.1 -  14/03/11:</li></b>" +
			"	Pb on TangoHost.getFamily() (if not defined) fixed.<br>" +

		"<br><li><b>Astor-5.4.0 -  11/02/11:</li></b>" +
			"	Pb with TAC when adding addresses on \"All Users\" fixed.<br>" +
			"	No reference on app_util classes any more.<br>" +
			"	Change splash screen image.<br>" +

		"<br><li><b>Astor-5.3.7 -  24/01/11:</li></b>" +
			"	Constructor added.<br>" +

		"<br><li><b>Astor-5.3.6 -  13/01/11:</li></b>" +
			"	Pb on black box reader thread fixed.<br>" +

		"<br><li><b>Astor-5.3.5 -  10/01/11:</li></b>" +
			"	TAC is now displayed as database servers.<br>" +
			"	StartServersAtStarteup starter class property management added.<br>" +
			"	Display access mode in Tango Access panel.<br>" +

		"<br><li><b>Astor-5.3.4 -  04/01/11:</li></b>" +
			"	Do not try to subscribe on Starter events if starter device not exported.<br>" +

		"<br><li><b>Astor-5.3.3 -  21/12/10:</li></b>" +
			"	Little tool to un-export devices registred on unreachable hosts added.<br>" +

		"<br><li><b>Astor-5.3.2 -  16/12/10:</li></b>" +
			"	Add a password for AccessControl tool.<br>" +

		"<br><li><b>Astor-5.3.1 -  02/12/10:</li></b>" +
			"	Constructors added in  PoolThreadsManager to be launched from Jive.<br>" +

		"<br><li><b>Astor-5.3.0 -  30/11/10:</li></b>" +
			"	For multi servers command, if the command is done through the starter,<br>" +
			"	it is done by a thread and a delay has been added between servers.<br>" +

		"<br><li><b>Astor-5.2.11 -  29/11/10:</li></b>" +
			"	Multi servers command added.<br>" +
			"	Uptime for servers added.<br>" +

		"<br><li><b>Astor-5.2.10 -  08/10/10:</li></b>" +
			"	Change default view in Polling Profiler.<br>" +

		"<br><li><b>Astor-5.2.9 -  01/09/10:</li></b>" +
			"	Minor change (traces removed and added).<br>" +

		"<br><li><b>Astor-5.2.8 -  17/06/10:</li></b>" +
			"	Pb on display startup level dialog in case of many devices fixed.<br>" +

		"<br><li><b>Astor-5.2.7 -  17/06/10:</li></b>" +
			"	Start new server can take several servers (multiple selection).<br>" +

		"<br><li><b>Astor-5.2.6 -  04/06/10:</li></b>" +
			"	Global command to change startup level added.<br>" +

		"<br><li><b>Astor-5.2.5 -  08/04/10:</li></b>" +
			"	Minor bugs fixed.<br>" +

		"<br><li><b>Astor-5.2.4 -  05/01/10:</li></b>" +
			"	Best management of subscribe error window at startup.<br>" +

		"<br><li><b>Astor-5.2.3 -  18/08/09:</li></b>" +
			"	Bug in Device Dependencies fixed (infinite loop)<br>" +

		"<br><li><b>Astor-5.2.2 -  02/06/09:</li></b>" +
			"	Bug on host.check_notifyd fixed.<br>" +

		"<br><li><b>Astor-5.2.1 -  15/05/09:</li></b>" +
			"	Remove serialization between HostStateThread and HostInfoDialogVector.<br>" +

		"<br><li><b>Astor-5.2.0 -  18/04/09:</li></b>" +
			"	Tango-7 tools accessible.<br>" +
			"	Device dependencies (sub-devices) tool added.<br>" +
			"	MySqlUtil feature added.<br>" +

		"<br><li><b>Astor-5.1.3 -  30/01/09:</li></b>" +
			"	Black box management added for database.<br>" +
			"	Black box management tool improved.<br>" +
			"	Find TANGO object by filter added.<br>" +

		"<br><li><b>Astor-5.1.2 -  16/01/09:</li></b>" +
			"	Black box management added for host and Server.<br>" +
			"	Starter logging display added for host and server.<br>" +
			"	Splash screen use ATK one.<br>" +

		"<br><li><b>Astor-5.1.1 -  17/12/08:</li></b>" +
			"	Add a scroll pane in HostInfoDialog in case of too big dialog.<br>" +

		"<br><li><b>Astor-5.1.0 -  09/10/08:</li></b>" +
			"	Pool thread management added but not accessible.<br>" +
			"	New version and tests for access.<br>" +

		"<br><li><b>Astor-5.0.5 -  12/09/08:</li></b>" +
			"	Bug in server info (if not running) fixed.<br>" +

		"<br><li><b>Astor-5.0.4 -  16/06/08:</li></b>" +
			"	Level trees are now displayed on 2 rows.<br>" +

		"<br><li><b>Astor-5.0.3 -  22/05/08:</li></b>" +
			"	Host info dialog servers are managed in a jtree.<br>" +

		"<br><li><b>Astor-5.0.2 -  07/05/08:</li></b>" +
			"	Host info dialog line management changed.<br>" +
			"	Browse Database option added in DB popup menu.<br>" +

		"<br><li><b>Astor-5.0.1 -  10/04/08:</li></b>" +
			"	Branch info modified.<br>" +
			"	Table of hosts and servers added.<br>" +

		"<br><li><b>Astor-5.0.0 -  27/03/08:</li></b>" +
			"	Compatibility with Starter 4.0 and after only !<br>" +
			"	Better management of server list.<br>" +
			"	Server state MOVING managed.<br>" +
			"	Hard kill added on servers.<br>" +
			"	New features on polling profiler.<br>" +

		"<br><li><b>Astor-4.5.7 -  12/12/07:</li></b>" +
			"	Reset buttons added in event configuration panel.<br>" +

		"<br><li><b>Astor-4.5.6 -  07/11/07:</li></b>" +
			"	Display host info if OSManage DS  is running on host.<br>" +
			"	Display host\'s state on HotInfoDialog.<br>" +

		"<br><li><b>Astor-4.5.5 -  11/09/07:</li></b>" +
			"	Db attribute polling panel added.<br>" +
			"	Bug on repeate error window when refresh tree fixed.<br>" +

		"<br><li><b>Astor-4.5.4  -  04/09/07:</li></b>" +
			"	Set attribute polled names in lower case in new starter creation.<br>" +

		"<br><li><b>Astor-4.5.3  -  20/08/07: </li></b>" +
			"	ServStatePanel added on HostInfoDialog (Check states option).<br>" +

		"<br><li><b>Astor-4.5.2  -  27/04/07: </li></b>" +
			"	Display host panel available for stopped server from Device Browser.<br>" +

		"<br><li><b>Astor-4.5.1  -  04/04/07: </li></b>" +
			"	Database attribute properties editor added.<br>" +

		"<br><li><b>Astor-4.5.0  -  27/03/07:</li></b>" +
			"	Preferences dialog added.<br>" +

		"<br><li><b>Astor-4.4.4  -  08/03/07:</li></b>" +
			"	LastCollections property is managed.<br>" +

		"<br><li><b>Astor-4.4.3  -  22/01/07:</li></b>" +
			"	Remove watch dog on host thread.<br>" +

		"<br><li><b>Astor-4.4.2  -  17/01/07:</li></b>" +
			"	Html helps added.<br>" +
			"    Startup error message added in view menu.<br>" +

		"<br><li><b>Astor-4.4.1  -  08/01/07:</li></b>" +
			"	Disable Start Server button if Starter is MOVING.<br>" +

		"<br><li><b>Astor-4.4.0  -  25/09/06:</li></b>" +
			"	Access control tool added.<br>" +

		"<br><li><b>Astor-4.3.2:</li></b>" +
			"	Bug fixed in miscellaneous host collection.<br>" +

		"<br><li><b>Astor-4.3.1:</li></b>" +
			"	Moving state added for collection.<br>" +
			"    In StartAll command a sleep(500) has been added between two hosts.<br>" +

		"<br><li><b>Astor-4.3.0:</li></b>" +
			"	Moving state added for startup phase.<br>" +

		"<br><li><b>Astor-4.2.3:</li></b>" +
			"	Host info panel modified to use icons.<br>" +

		"<br><li><b>Astor-4.2.2:</li></b>" +
			"	Backward compatibilty for jive fixed.<br>" +

		"<br><li><b>Astor-4.2.1:</li></b>" +
			"	Icons have been changed.<br>" +
			"	Some minor changes.<br>" +

		"<br><li><b>Astor-4.2.0:</li></b>" +
			"	Polling profiler added.<br>" +

		"<br><li><b>Astor-4.1.4:</li></b>" +
			"	Open/Save menu added on event tester window.<br>" +

		"<br><li><b>Astor-4.1.3:</li></b>" +
			"	History added on event management.<br>" +
			"    The maximum servers displayed in horizontal on HostInfoDialog window has been set to 5.<br>" +

		"<br><li><b>Astor-4.1.2:</li></b>" +
			"	Bug fixed in Device browser in member device name.<br>" +

		"<br><li><b>Astor-4.1.1:</li></b>" +
			"	Change TANGO_HOST added (needs TangORB-4.7.7 or later).<br>" +

		"<br><li><b>Astor-4.1.0:</li></b>" +
			"	DevBrowser and MkStarter utilities added.<br>" +

		"<br><li><b>Astor-4.0.9:</li></b>" +
			"	Minor changes for EventTester compatibility<br>" +

		"<br><li><b>Astor-4.0.8:</li></b>" +
			"	Screen position modified for dialogs.<br>" +

		"<br><li><b>Astor-4.0.7:</li></b>" +
			"	Search if host already exist before creation.<br>" +

		"<br><li><b>Astor-4.0.6:</li></b>" +
			"	Search by host name added.<br>" +

		"<br><li><b>Astor-4.0.5:</li></b>" +
			"	Can change memorized attribute value.<br>" +

		"<br><li><b>Astor-4.0.4:</li></b>" +
			"	Bug in server architecture fixed.<br>" +

		"<br><li><b>Astor-4.0.3:</li></b>" +
			"	Minor changes, RemoteCmd property added.<br>" +

		"<br><li><b>Astor-4.0.2:</li></b>" +
			"	Server architecture display addded.<br>" +

		"<br><li><b>Astor-4.0.1:</li></b>" +
			"	DevWizard calls addded.<br>" +

		"<br><li><b>Astor-4.0.0:</li></b>" +
			"	Possibility to controle two database servers added.<br>" 	;
}
