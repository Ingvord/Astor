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

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.*;
import fr.esrf.TangoApi.events.*;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;

import java.util.Date;
import java.util.StringTokenizer;
import java.util.ArrayList;

//=========================================================================
/**
 * Signal Object Definition
 */
//=========================================================================
public class SubscribedSignal implements TangoConst {
    final static String defVal = "-----";
    Exception except = null;
    boolean subscribed = false;
    String name;
    String value = defVal;
    String time = defVal;
    String d_time = defVal;
    String d_value = defVal;
    ArrayList<EventHisto> histo = new ArrayList<EventHisto>();

    String devname;
    String attname;
    int mode;
    int cnt = 0;
    int data_type;
    String  source = "";

    private double dt_min = Double.MAX_VALUE;
    private double dt_max = 0.0;
    private double dt_average = 0.0;
    private double[] values = null;
    private double[] prev_val = null;

    private long t0;
    private TangoEventsAdapter adapter;
    private ArchiveEventListener arch_listener = null;
    private ChangeEventListener change_listener = null;
    private PeriodicEventListener periodic_listener = null;

    private EventsTable parent;
    private AttributeInfoEx attinfo;

    //================================================================
    //================================================================
    public SubscribedSignal(String name, int mode) {
        this.name = name;
        //	Split device name and att name
        int pos = name.lastIndexOf("/");
        devname = name.substring(0, pos);
        attname = name.substring(pos + 1);
        this.mode = mode;

        try {
            attinfo = new AttributeProxy(name).get_info_ex();
        } catch (DevFailed e) { /** Nothing to do */}
    }

    //================================================================
    //================================================================
    void subscribe(EventsTable parent) {
        this.parent = parent;
        new SubscribeThread(this).start();
    }

    //================================================================
    //================================================================
    String except_str() {
        String str = "";

        if (except instanceof ConnectionFailed)
            str += ((ConnectionFailed) (except)).getStack();
        else if (except instanceof CommunicationFailed)
            str += ((CommunicationFailed) (except)).getStack();
        else if (except instanceof WrongNameSyntax)
            str += ((WrongNameSyntax) (except)).getStack();
        else if (except instanceof WrongData)
            str += ((WrongData) (except)).getStack();
        else if (except instanceof NonDbDevice)
            str += ((NonDbDevice) (except)).getStack();
        else if (except instanceof NonSupportedFeature)
            str += ((NonSupportedFeature) (except)).getStack();
        else if (except instanceof EventSystemFailed)
            str += ((EventSystemFailed) (except)).getStack();
        else if (except instanceof AsynReplyNotArrived)
            str += ((AsynReplyNotArrived) (except)).getStack();
        else if (except instanceof DevFailed) {
            DevFailed df = (DevFailed) except;
            //	True DevFailed
            for (int i = 0; i < df.errors.length; i++) {
                str += df.toString() + ":\n";
                str += "Tango exception\n";
                str += "Severity -> ";
                switch (df.errors[i].severity.value()) {
                    case ErrSeverity._WARN:
                        str += "WARNING \n";
                        break;

                    case ErrSeverity._ERR:
                        str += "ERROR \n";
                        break;

                    case ErrSeverity._PANIC:
                        str += "PANIC \n";
                        break;

                    default:
                        str += "Unknown severity code";
                        break;
                }
                str += "Desc -> " + df.errors[i].desc + "\n";
                str += "Reason -> " + df.errors[i].reason + "\n";
                str += "Origin -> " + df.errors[i].origin + "\n";

                if (i < df.errors.length - 1)
                    str += "-------------------------------------------------------------\n";
            }
        } else
            str = except.toString();
        return str;
    }

    //================================================================
    //================================================================
    void setData(Exception e) {
        value = SubscribedSignal.defVal;
        //time    = SubscribedSignal.defVal;
        d_time = SubscribedSignal.defVal;
        d_value = SubscribedSignal.defVal;
        except = e;
    }

    //================================================================
    //================================================================
    void setData(DeviceAttribute attr) throws DevFailed {
        long t1 = System.currentTimeMillis();
        double dt = (double) (t1 - t0) / 1000;
        String date = getStrDate(attr.getTimeValMillisSec());

        //	Update main fields
        cnt++;
        time = "" + date;
        setValue(attr);

        if (cnt > 1) {
            //	Update delta fields
            d_time = "" + dt + " sec.";
            setDeltaValue();
        }
        if (cnt > 2) {
            //	Update statistic fields
            if (dt > dt_max) dt_max = dt;
            if (dt < dt_min) dt_min = dt;
            dt_average = (dt_average * (cnt - 3) + dt) / (cnt - 2);
        }

        //	store values for next event.
        t0 = t1;
        if (values != null) {
            histo.add(new EventHisto(attr.getTimeValMillisSec(), values, d_value, d_time));
            prev_val = values;
        }
        except = null;
    }

    //================================================================
    //================================================================
    private void setDeltaValue() {
        //	Compare all values and get delta maxi
        double dv_max = 0.0;
        double relative = 0.0;
        if (values != null && prev_val != null) {
            for (int i = 0; i < values.length && i < prev_val.length; i++) {
                double dv = Math.abs(values[i] - prev_val[i]);
                if (dv > dv_max) {
                    dv_max = dv;
                    if (prev_val[i] != 0)
                        relative = dv / prev_val[i];
                }
            }
            //	format the display (depends on value
            d_value = formatValue(dv_max);

            //	Add relative delta
            relative = (double) ((int) (10000.0 * relative)) / 100.0;
            d_value += "  (" + relative + " %)";
        }
    }

    //================================================================
    //================================================================
    static String formatValue(double val) {
        String str;
        if (val >= 20.0)
            str = "" + (int) val;
        else {
            str = "" + val;
            int idx = str.indexOf(".");
            if (val >= 1.0 && idx > 0)
                str = str.substring(0, idx + 2);
            else if (val >= 0.1 && idx > 0)
                str = str.substring(0, idx + 3);
            else if (val >= 0.01 && idx > 0)
                str = str.substring(0, idx + 4);
            else if (val >= 0.001 && idx > 0)
                str = str.substring(0, idx + 5);
            else if (val >= 0.0001 && idx > 0)
                str = str.substring(0, idx + 6);
        }
        return str;
    }

    //================================================================
    //================================================================
    void unsubscribe() {
        if (subscribed && adapter != null)
            try {
                if (arch_listener != null)
                    adapter.removeTangoArchiveListener(arch_listener, attname);
                if (change_listener != null)
                    adapter.removeTangoChangeListener(change_listener, attname);
                if (periodic_listener != null)
                    adapter.removeTangoPeriodicListener(periodic_listener, attname);
                System.out.println("unsubscribe event for " + name);
            } catch (DevFailed e) {
                System.out.println("Failed to unsubscribe event for " + name);
                fr.esrf.TangoDs.Except.print_exception(e);
            }
    }

    //================================================================
    //================================================================
    public String toString() {
        return name + "  [" + EventsTable.strMode[mode] + "]";
    }

    //================================================================
    //================================================================
    public String getTimes() {
        if (cnt <= 2)
            return null;
        int tmp = (int) (1000.0 * dt_average);
        return "dt minimum = " + dt_min + " sec.\n" +
                "dt average   = " + (double) tmp / 1000 + " sec.\n" +
                "dt maximum = " + dt_max + " sec.";
    }

    //================================================================
    //================================================================
    public String status() {
        String status = name + ":\n" +
                Tango_CmdArgTypeName[data_type] + "\n\n";
        if (subscribed) {
            status += "Is  Subscribed On " +
                    EventsTable.strMode[mode] + " mode\n";
            status += "Value:\n" + value + "\n\n";
            status += "Receive  " + cnt + " event";
            if (cnt > 1) status += "s";
            status += "  at " + getStrDate();

            if (cnt > 2)
                status += "\n" + getTimes();
        } else if (except != null) {
            if (except instanceof DevFailed) {
                DevFailed df = (DevFailed) except;
                status += df.errors[0].desc;
            } else
                status += except.toString();
        } else
            status += "? ? ?";

        return status;
    }


    //=====================================================================
    //=====================================================================
    static String getStrDate(long ms) {
        StringTokenizer st = new StringTokenizer(new Date(ms).toString());
        ArrayList<String> v = new ArrayList<String>();
        while (st.hasMoreTokens())
            v.add(st.nextToken());

        return v.get(3) + "  " + v.get(2) + " " + v.get(1);
    }

    //=====================================================================
    //=====================================================================
    static String getStrDate() {
        return getStrDate(System.currentTimeMillis());
    }

    //=====================================================================
    //=====================================================================
    private void setValue(DeviceAttribute attr) throws DevFailed {
        value = "";
        String format = null;
        if (attinfo != null && !attinfo.format.equals("Not specified"))
            format = attinfo.format;
        switch (data_type = attr.getType()) {
            case Tango_DEV_BOOLEAN: {
                boolean[] tmp = attr.extractBooleanArray();
                if (tmp.length == 1)
                    value = "" + tmp[0];
                else
                    for (int i = 0; i < tmp.length; i++) {
                        value += ((tmp[i]) ? "1" : "0");
                        if (((i + 1) % 4) == 0) value += " ";
                    }
            }
            break;
            case Tango_DEV_UCHAR: {
                short[] tmp = attr.extractUCharArray();
                for (int i = 0; i < tmp.length; i++) {
                    value += "" + tmp[i];
                    if (i < tmp.length - 1) value += "\n";
                }
            }
            break;
            case Tango_DEV_SHORT: {
                short[] tmp = attr.extractShortArray();
                values = new double[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (format == null)
                        value += "" + tmp[i];
                    else {
                        value += String.format(format, tmp[i]);
                    }
                    if (i < tmp.length - 1) value += "\n";
                    values[i] = (double) tmp[i];
                }
            }
            break;
            case Tango_DEV_USHORT: {
                int[] tmp = attr.extractUShortArray();
                values = new double[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (format == null)
                        value += "" + tmp[i];
                    else {
                        value += String.format(format, tmp[i]);
                    }
                    if (i < tmp.length - 1) value += "\n";
                    values[i] = (double) tmp[i];
                }
            }
            break;
            case Tango_DEV_LONG: {
                int[] tmp = attr.extractLongArray();
                values = new double[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (format == null)
                        value += "" + tmp[i];
                    else {
                        value += String.format(format, tmp[i]);
                    }
                    if (i < tmp.length - 1) value += "\n";
                    values[i] = (double) tmp[i];
                }
            }
            break;
            case Tango_DEV_ULONG: {
                long [] tmp = attr.extractULongArray();
                values = new double[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (format == null)
                        value += "" + tmp[i];
                    else {
                        value += String.format(format, tmp[i]);
                    }
                    if (i < tmp.length - 1) value += "\n";
                    values[i] = (double) tmp[i];
                }
            }
            break;
            case Tango_DEV_FLOAT: {
                float[] tmp = attr.extractFloatArray();
                values = new double[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (format == null)
                        value += "" + tmp[i];
                    else {
                        value += String.format(format, tmp[i]);
                    }
                    if (i < tmp.length - 1) value += "\n";
                    values[i] = (double) tmp[i];
                }
            }
            break;
            case Tango_DEV_DOUBLE: {
                double[] tmp = attr.extractDoubleArray();
                values = new double[tmp.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (format == null)
                        value += "" + tmp[i];
                    else {
                        value += String.format(format, tmp[i]);
                    }
                    if (i < tmp.length - 1) value += "\n";
                    values[i] = tmp[i];
                }
            }
            break;
            case Tango_DEV_STRING: {
                String[] tmp = attr.extractStringArray();
                for (int i = 0; i < tmp.length; i++) {
                    value += tmp[i];
                    if (i < tmp.length - 1) value += "\n";
                }
            }
            break;
            case Tango_DEV_STATE: {
                DevState state = attr.extractState();
                value = ApiUtil.stateName(state);
            }
            break;
            default:
                value = "" + data_type + " ?  - Unsuported Type";
        }
    }
    //===============================================================
    //===============================================================


    //=========================================================================

    /**
     * A class to define an history element
     */
    //=========================================================================
    class EventHisto {
        long time;
        double[] values;
        String d_value;
        String d_time;
        Exception except;

        //=========================================================================
        EventHisto(long time, double[] values, String d_value, String d_time) {
            this.time = time;
            this.values = values;
            this.d_value = d_value;
            this.d_time = d_time;
            this.except = null;
        }

    }


    //=========================================================================

    /**
     * Subscribing Thread : loop until subscribe done.
     */
    //=========================================================================
    class SubscribeThread extends Thread {
        SubscribedSignal signal;

        //==================================================
        //==================================================
        SubscribeThread(SubscribedSignal signal) {
            this.signal = signal;
        }

        //==================================================
        //==================================================
        public void run() {
            while (!signal.subscribed) {
                System.out.println("Trying to subscribe on " + name);
                try {
                    signal.adapter = new TangoEventsAdapter(devname);
                    if (mode == EventsTable.SUBSCRIBE_ARCHIVE) {
                        arch_listener = new ArchiveEventListener(signal);
                        adapter.addTangoArchiveListener(
                                arch_listener, attname, new String[0]);
                    } else if (mode == EventsTable.SUBSCRIBE_CHANGE) {
                        change_listener = new ChangeEventListener(signal);
                        adapter.addTangoChangeListener(
                                change_listener, attname, new String[0]);
                    } else if (mode == EventsTable.SUBSCRIBE_PERIODIC) {
                        periodic_listener = new PeriodicEventListener(signal);
                        adapter.addTangoPeriodicListener(
                                periodic_listener, attname, new String[0]);
                    } else
                        Except.throw_exception("",
                                "Unknown event subscribtion mode (" + mode + ")",
                                "SubscribedSignal.SubscribeThread.run()");

                    subscribed = true;
                    System.out.println("subscribeEvent() done for " + name);
                    except = null;
                } catch (DevFailed e) {
                    except = e;
                    Except.print_exception(e);
                } catch (Exception e) {
                    except = e;
                    e.printStackTrace();
                }

                try {
                    sleep(2000);
                } catch (Exception ex) {/** Nothing to do */}
            }
        }
    }


    //=========================================================================

    /**
     * Change event listener
     */
    //=========================================================================
    class ChangeEventListener implements ITangoChangeListener {
        SubscribedSignal signal;

        //=====================================================================
        //=====================================================================
        ChangeEventListener(SubscribedSignal signal) {
            this.signal = signal;
        }

        //=====================================================================
        //=====================================================================
        public void change(TangoChangeEvent event) {
            try {
                if (source.length()==0)
                    source = (event.isZmqEvent()) ? "ZMQ" : "Notifd";
            }
            catch (Exception e) {
                source = "? ?"; //  TangORB too old
            }
            try {
                //	Get the attribute value
                DeviceAttribute attr = event.getValue();
                signal.setData(attr);
            } catch (DevFailed e) {
                signal.setData(e);
            } catch (Exception e) {
                e.printStackTrace();
                signal.setData(e);
            }
            parent.updateTable();
        }
    }


    //=========================================================================

    /**
     * Archive event listener
     */
    //=========================================================================
    class ArchiveEventListener implements ITangoArchiveListener {
        SubscribedSignal signal;

        //=====================================================================
        //=====================================================================
        ArchiveEventListener(SubscribedSignal signal) {
            this.signal = signal;
        }

        //=====================================================================
        //=====================================================================
        public void archive(TangoArchiveEvent event) {
            try {
                if (source.length()==0)
                    source = (event.isZmqEvent()) ? "ZMQ" : "Notifd";
            }
            catch (Exception e) {
                source = "? ?"; //  TangORB too old
            }
            try {
                //	Get the attribute value
                DeviceAttribute attr = event.getValue();
                signal.setData(attr);
            } catch (DevFailed e) {
                signal.setData(e);
            } catch (Exception e) {
                e.printStackTrace();
                signal.setData(e);
            }
            parent.updateTable();
        }
    }
    //=========================================================================

    /**
     * Periodic event listener
     */
    //=========================================================================
    class PeriodicEventListener implements ITangoPeriodicListener {
        SubscribedSignal signal;

        //=====================================================================
        //=====================================================================
        PeriodicEventListener(SubscribedSignal signal) {
            this.signal = signal;
        }

        //=====================================================================
        //=====================================================================
        public void periodic(TangoPeriodicEvent event) {
            try {
                if (source.length()==0)
                    source = (event.isZmqEvent()) ? "ZMQ" : "Notifd";
            }
            catch (Exception e) {
                source = "? ?"; //  TangORB too old
            }
            try {
                //	Get the attribute value
                DeviceAttribute attr = event.getValue();
                signal.setData(attr);
            } catch (DevFailed e) {
                signal.setData(e);
            } catch (Exception e) {
                e.printStackTrace();
                signal.setData(e);
            }
            parent.updateTable();
        }
    }
}
