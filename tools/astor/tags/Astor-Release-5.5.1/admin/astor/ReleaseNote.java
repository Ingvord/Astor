
package admin.astor;

/**
 *	HTML code to display Release Notes for this package.
 *	It is generated by Pogo classes from a text file.
 */

public interface ReleaseNote
{
	public final String	str = 
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\n" + 
		"<HTML>\n" + 
		"<HEAD>\n" + 
		"<Title> Release Note </Title>\n" + 
		"</HEAD>\n" + 
		"<BODY TEXT=\"#000000\" BGCOLOR=\"#FFFFFF\" LINK=\"#0000FF\" VLINK=\"#7F00FF\" ALINK=\"#FF0000\">\n" + 
		"<P><!-------TITLE------></P>\n" + 
		"<Center>	<h2>Astor Release Note</h2>\n" + 
		"	(Generated Tue Apr 12 14:12:13 CEST 2011)</Center><Br>\n" + 
		"<li><b>Astor-5.5.1 -  12/04/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Auto start info added to statistics tool.<Br>\n" + 
		"<li><b>Astor-5.5.0 -  01/04/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Statistics tools added.<Br>\n" + 
		"<li><b>Astor-5.4.1 -  14/03/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Pb on TangoHost.getFamily() (if not defined) fixed.<Br>\n" + 
		"<li><b>Astor-5.4.0 -  11/02/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Pb with TAC when adding addresses on \"All Users\" fixed.<Br>\n" + 
		"&nbsp; &nbsp; No reference on app_util classes any more.<Br>\n" + 
		"&nbsp; &nbsp; Change splash screen image.<Br>\n" + 
		"<li><b>Astor-5.3.7 -  24/01/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Constructor added.<Br>\n" + 
		"<li><b>Astor-5.3.6 -  13/01/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Pb on black box reader thread fixed.<Br>\n" + 
		"<li><b>Astor-5.3.5 -  10/01/11:</b><Br>\n" + 
		"&nbsp; &nbsp; TAC is now displayed as database servers.<Br>\n" + 
		"&nbsp; &nbsp; StartServersAtStarteup starter class property management added.<Br>\n" + 
		"&nbsp; &nbsp; Display access mode in Tango Access panel.<Br>\n" + 
		"<li><b>Astor-5.3.4 -  04/01/11:</b><Br>\n" + 
		"&nbsp; &nbsp; Do not try to subscribe on Starter events if starter device not exported.<Br>\n" + 
		"<li><b>Astor-5.3.3 -  21/12/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Little tool to un-export devices registred on unreachable hosts added.<Br>\n" + 
		"<li><b>Astor-5.3.2 -  16/12/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Add a password for AccessControl tool.<Br>\n" + 
		"<li><b>Astor-5.3.1 -  02/12/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Constructors added in  PoolThreadsManager to be launched from Jive.<Br>\n" + 
		"<li><b>Astor-5.3.0 -  30/11/10:</b><Br>\n" + 
		"&nbsp; &nbsp; For multi servers command, if the command is done through the starter,<Br>\n" + 
		"&nbsp; &nbsp; it is done by a thread and a delay has been added between servers.<Br>\n" + 
		"<li><b>Astor-5.2.11 -  29/11/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Multi servers command added.<Br>\n" + 
		"&nbsp; &nbsp; Uptime for servers added.<Br>\n" + 
		"<li><b>Astor-5.2.10 -  08/10/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Change default view in Polling Profiler.<Br>\n" + 
		"<li><b>Astor-5.2.9 -  01/09/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Minor change (traces removed and added).<Br>\n" + 
		"<li><b>Astor-5.2.8 -  17/06/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Pb on display startup level dialog in case of many devices fixed.<Br>\n" + 
		"<li><b>Astor-5.2.7 -  17/06/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Start new server can take several servers (multiple selection).<Br>\n" + 
		"<li><b>Astor-5.2.6 -  04/06/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Global command to change startup level added.<Br>\n" + 
		"<li><b>Astor-5.2.5 -  08/04/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Minor bugs fixed.<Br>\n" + 
		"<li><b>Astor-5.2.4 -  05/01/10:</b><Br>\n" + 
		"&nbsp; &nbsp; Best management of subscribe error window at startup.<Br>\n" + 
		"<li><b>Astor-5.2.3 -  18/08/09:</b><Br>\n" + 
		"&nbsp; &nbsp; Bug in Device Dependencies fixed (infinite loop)<Br>\n" + 
		"<li><b>Astor-5.2.2 -  02/06/09:</b><Br>\n" + 
		"&nbsp; &nbsp; Bug on host.check_notifyd fixed.<Br>\n" + 
		"<li><b>Astor-5.2.1 -  15/05/09:</b><Br>\n" + 
		"&nbsp; &nbsp; Remove serialization between HostStateThread and HostInfoDialogVector.<Br>\n" + 
		"<li><b>Astor-5.2.0 -  18/04/09:</b><Br>\n" + 
		"&nbsp; &nbsp; Tango-7 tools accessible.<Br>\n" + 
		"&nbsp; &nbsp; Device dependencies (sub-devices) tool added.<Br>\n" + 
		"&nbsp; &nbsp; MySqlUtil feature added.<Br>\n" + 
		"<li><b>Astor-5.1.3 -  30/01/09:</b><Br>\n" + 
		"&nbsp; &nbsp; Black box management added for database.<Br>\n" + 
		"&nbsp; &nbsp; Black box management tool improved.<Br>\n" + 
		"&nbsp; &nbsp; Find TANGO object by filter added.<Br>\n" + 
		"<li><b>Astor-5.1.2 -  16/01/09:</b><Br>\n" + 
		"&nbsp; &nbsp; Black box management added for host and Server.<Br>\n" + 
		"&nbsp; &nbsp; Starter logging display added for host and server.<Br>\n" + 
		"&nbsp; &nbsp; Splash screen use ATK one.<Br>\n" + 
		"<li><b>Astor-5.1.1 -  17/12/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Add a scroll pane in HostInfoDialog in case of too big dialog.<Br>\n" + 
		"<li><b>Astor-5.1.0 -  09/10/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Pool thread management added but not accessible.<Br>\n" + 
		"&nbsp; &nbsp; New version and tests for access.<Br>\n" + 
		"<li><b>Astor-5.0.5 -  12/09/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Bug in server info (if not running) fixed.<Br>\n" + 
		"<li><b>Astor-5.0.4 -  16/06/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Level trees are now displayed on 2 rows.<Br>\n" + 
		"<li><b>Astor-5.0.3 -  22/05/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Host info dialog servers are managed in a jtree.<Br>\n" + 
		"<li><b>Astor-5.0.2 -  07/05/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Host info dialog line management changed.<Br>\n" + 
		"&nbsp; &nbsp; Browse Database option added in DB popup menu.<Br>\n" + 
		"<li><b>Astor-5.0.1 -  10/04/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Branch info modified.<Br>\n" + 
		"&nbsp; &nbsp; Table of hosts and servers added.<Br>\n" + 
		"<li><b>Astor-5.0.0 -  27/03/08:</b><Br>\n" + 
		"&nbsp; &nbsp; Compatibility with Starter 4.0 and after only !<Br>\n" + 
		"&nbsp; &nbsp; Better management of server list.<Br>\n" + 
		"&nbsp; &nbsp; Server state MOVING managed.<Br>\n" + 
		"&nbsp; &nbsp; Hard kill added on servers.<Br>\n" + 
		"&nbsp; &nbsp; New features on polling profiler.<Br>\n" + 
		"<li><b>Astor-4.5.7 -  12/12/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Reset buttons added in event configuration panel.<Br>\n" + 
		"<li><b>Astor-4.5.6 -  07/11/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Display host info if OSManage DS  is running on host.<Br>\n" + 
		"&nbsp; &nbsp; Display host's state on HotInfoDialog.<Br>\n" + 
		"<li><b>Astor-4.5.5 -  11/09/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Db attribute polling panel added.<Br>\n" + 
		"&nbsp; &nbsp; Bug on repeate error window when refresh tree fixed.<Br>\n" + 
		"<li><b>Astor-4.5.4  -  04/09/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Set attribute polled names in lower case in new starter creation.<Br>\n" + 
		"<li><b>Astor-4.5.3  -  20/08/07:</b><Br>\n" + 
		"&nbsp; &nbsp; ServStatePanel added on HostInfoDialog (Check states option).<Br>\n" + 
		"<li><b>Astor-4.5.2  -  27/04/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Display host panel available for stopped server from Device Browser.<Br>\n" + 
		"<li><b>Astor-4.5.1  -  04/04/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Database attribute properties editor added.<Br>\n" + 
		"<li><b>Astor-4.5.0  -  27/03/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Preferences dialog added.<Br>\n" + 
		"<li><b>Astor-4.4.4  -  08/03/07:</b><Br>\n" + 
		"&nbsp; &nbsp; LastCollections property is managed.<Br>\n" + 
		"<li><b>Astor-4.4.3  -  22/01/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Remove watch dog on host thread.<Br>\n" + 
		"<li><b>Astor-4.4.2  -  17/01/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Html helps added.<Br>\n" + 
		"&nbsp; &nbsp; Startup error message added in view menu.<Br>\n" + 
		"<li><b>Astor-4.4.1  -  08/01/07:</b><Br>\n" + 
		"&nbsp; &nbsp; Disable Start Server button if Starter is MOVING.<Br>\n" + 
		"<li><b>Astor-4.4.0  -  25/09/06:</b><Br>\n" + 
		"&nbsp; &nbsp; Access control tool added.<Br>\n" + 
		"<li><b>Astor-4.3.2:</b><Br>\n" + 
		"&nbsp; &nbsp; Bug fixed in miscellaneous host collection.<Br>\n" + 
		"<li><b>Astor-4.3.1:</b><Br>\n" + 
		"&nbsp; &nbsp; Moving state added for collectionx.<Br>\n" + 
		"&nbsp; &nbsp; In StartAll command a sleep(500) has been added between two hosts.<Br>\n" + 
		"<li><b>Astor-4.3.0:</b><Br>\n" + 
		"&nbsp; &nbsp; Moving state added for startup phase.<Br>\n" + 
		"<li><b>Astor-4.2.3:</b><Br>\n" + 
		"&nbsp; &nbsp; Host info panel modified to use icons.<Br>\n" + 
		"<li><b>Astor-4.2.2:</b><Br>\n" + 
		"&nbsp; &nbsp; Backward compatibilty for jive fixed.<Br>\n" + 
		"<li><b>Astor-4.2.1:</b><Br>\n" + 
		"&nbsp; &nbsp; Icons have been changed.<Br>\n" + 
		"&nbsp; &nbsp; Some minor changes.<Br>\n" + 
		"<li><b>Astor-4.2.0:</b><Br>\n" + 
		"&nbsp; &nbsp; Polling profiler added.<Br>\n" + 
		"<li><b>Astor-4.1.4:</b><Br>\n" + 
		"&nbsp; &nbsp; Open/Save menu added on event tester window.<Br>\n" + 
		"<li><b>Astor-4.1.3:</b><Br>\n" + 
		"&nbsp; &nbsp; History added on event management.<Br>\n" + 
		"&nbsp; &nbsp; The maximum servers displayed in horizontal on HostInfoDialog<Br>\n" + 
		"&nbsp; &nbsp; window has been set to 5.<Br>\n" + 
		"<li><b>Astor-4.1.2:</b><Br>\n" + 
		"&nbsp; &nbsp; Bug fixed in Device browser in member device name.<Br>\n" + 
		"<li><b>Astor-4.1.1:</b><Br>\n" + 
		"&nbsp; &nbsp; Change TANGO_HOST added (needs TangORB-4.7.7 or later).<Br>\n" + 
		"<li><b>Astor-4.1.0:</b><Br>\n" + 
		"&nbsp; &nbsp; DevBrowser and MkStarter utilities added.<Br>\n" + 
		"<li><b>Astor-4.0.9:</b><Br>\n" + 
		"&nbsp; &nbsp; Minor changes for EventTester compatibility<Br>\n" + 
		"<li><b>Astor-4.0.8:</b><Br>\n" + 
		"&nbsp; &nbsp; Screen position modified for dialogs.<Br>\n" + 
		"<li><b>Astor-4.0.7:</b><Br>\n" + 
		"&nbsp; &nbsp; Search if host already exist before creation.<Br>\n" + 
		"<li><b>Astor-4.0.6:</b><Br>\n" + 
		"&nbsp; &nbsp; Search by host name added.<Br>\n" + 
		"<li><b>Astor-4.0.5:</b><Br>\n" + 
		"&nbsp; &nbsp; Can change memorized attribute value.<Br>\n" + 
		"<li><b>Astor-4.0.4:</b><Br>\n" + 
		"&nbsp; &nbsp; Bug in server architecture fixed.<Br>\n" + 
		"<li><b>Astor-4.0.3:</b><Br>\n" + 
		"&nbsp; &nbsp; Minor changes, RemoteCmd property added.<Br>\n" + 
		"<li><b>Astor-4.0.2:</b><Br>\n" + 
		"&nbsp; &nbsp; Server architecture display addded.<Br>\n" + 
		"<li><b>Astor-4.0.1:</b><Br>\n" + 
		"&nbsp; &nbsp; DevWizard calls addded.<Br>\n" + 
		"<li><b>Astor-4.0.0:</b><Br>\n" + 
		"&nbsp; &nbsp; Possibility to controle two database servers added.<Br>\n" + 
		"</Body>\n" + 
		"</Html>\n" + 
		"";
}
