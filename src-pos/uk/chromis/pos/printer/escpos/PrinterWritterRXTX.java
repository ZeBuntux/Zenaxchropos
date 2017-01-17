//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) (c) 2015-2016
//    http://www.chromis.co.uk
//
//    This file is part of Chromis POS
//
//     Chromis POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Chromis POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>.

package uk.chromis.pos.printer.escpos;

// import javax.comm.*; // Java comm library
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.OutputStream;
import uk.chromis.pos.printer.TicketPrinterException;

/**
 *
 *   
 */
public class PrinterWritterRXTX extends PrinterWritter /* implements SerialPortEventListener */ {
    
    private CommPortIdentifier m_PortIdPrinter;
    private CommPort m_CommPortPrinter;  
    
    private String m_sPortPrinter;
    private OutputStream m_out;
    private int m_flowcontrol;
    
    /** Creates a new instance of PrinterWritterComm
     * @param sPortPrinter
     * @throws uk.chromis.pos.printer.TicketPrinterException */
    public PrinterWritterRXTX(String sPortPrinter) throws TicketPrinterException {
        m_sPortPrinter = sPortPrinter;
        m_out = null;
	m_flowcontrol = 1;
    }
    
    /** Creates a new instance of PrinterWritterComm
     * @param sPortPrinter
     * @throws uk.chromis.pos.printer.TicketPrinterException */
    public PrinterWritterRXTX(String sPortPrinter, int flowcontrol) throws TicketPrinterException {
        m_sPortPrinter = sPortPrinter;
        m_out = null;
	m_flowcontrol = flowcontrol;
    }

    /**
     *
     * @param data
     */
    @Override
    protected void internalWrite(byte[] data) {
        try {  
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortPrinter); // Tomamos el puerto                   
                m_CommPortPrinter = m_PortIdPrinter.open("PORTID", 10000); // Abrimos el puerto       
                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura   
                if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    ((SerialPort)m_CommPortPrinter).setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); // Configuramos el puerto
                    if (m_flowcontrol == 1) {
                	((SerialPort)m_CommPortPrinter).setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);  // this line prevents the printer tmu220 to stop printing after +-18 lines printed
                    }
                }
            }
            m_out.write(data);
            //String str = new String(data, "UTF-8");
            //System.out.println("Display write "+str);
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
            System.err.println(e);
            System.out.println(e);
        }      
    }
    
    /**
     *
     */
    @Override
    protected void internalFlush() {
        try {  
            if (m_out != null) {
                m_out.flush();
            }
        } catch (IOException e) {
            System.err.println(e);
        }    
    }
    
    /**
     *
     */
    @Override
    protected void internalClose() {
        try {  
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
                m_CommPortPrinter = null;
                m_PortIdPrinter = null;
            }
        } catch (IOException e) {
            System.err.println(e);
        }    
    }
}
