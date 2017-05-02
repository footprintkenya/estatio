package org.estatio.capex.dom.invoice.rule.transitions;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;

import org.estatio.capex.dom.invoice.IncomingInvoice;
import org.estatio.capex.dom.invoice.rule.IncomingInvoiceTransition;

@Mixin
public class IncomingInvoice_approveAsTreasurer extends IncomingInvoice_transitionAbstract {

    public IncomingInvoice_approveAsTreasurer(IncomingInvoice incomingInvoice) {
        super(incomingInvoice, IncomingInvoiceTransition.APPROVE_AS_TREASURER);
    }

    @Action()
    @MemberOrder(sequence = "4")
    public IncomingInvoice $$() {
        return super.$$();
    }

}
