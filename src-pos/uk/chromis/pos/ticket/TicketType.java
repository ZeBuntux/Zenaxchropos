/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.chromis.pos.ticket;

/**
 *
 * @author janmoxter
 */
public enum TicketType {
    
    NORMAL(0),
    REFUND(1),
    PAYMENT(2),
    NOSALE(3),
    INVOICE(4);

    int id;
    
    TicketType(int _id)
    {
        this.id = _id;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public static TicketType get(int _id)
    {
        TicketType ret = NORMAL;
        for (TicketType type : values()) {
            if (type.getId() == _id) {
                ret = type;
                break;
            }
        }
        return ret;
    }
}
