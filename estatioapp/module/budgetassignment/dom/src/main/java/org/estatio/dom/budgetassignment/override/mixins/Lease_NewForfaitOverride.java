package org.estatio.dom.budgetassignment.override.mixins;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.estatio.dom.budgetassignment.override.BudgetOverrideForFlatRate;
import org.estatio.dom.budgetassignment.override.BudgetOverrideRepository;
import org.estatio.dom.budgetassignment.override.BudgetOverrideType;
import org.estatio.dom.budgeting.budgetcalculation.BudgetCalculationType;
import org.estatio.dom.charge.Charge;
import org.estatio.dom.charge.ChargeRepository;
import org.estatio.dom.lease.Lease;

/**
 * This cannot be inlined because Lease doesn't know about BudgetOverrideRepository.
 */
@Mixin
public class Lease_NewForfaitOverride {

    private final Lease lease;
    public Lease_NewForfaitOverride(Lease lease){
        this.lease = lease;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ACTION)
    @MemberOrder(name="budgetOverrides", sequence = "1")
    public BudgetOverrideForFlatRate newForfait(
            final BigDecimal valueM2,
            final BigDecimal weightedArea,
            @Nullable
            final LocalDate startDate,
            @Nullable
            final LocalDate endDate,
            final Charge invoiceCharge,
            @Nullable
            final Charge incomingCharge,
            @Nullable
            final BudgetCalculationType type
    ) {
        return budgetOverrideRepository.newBudgetOverrideForFlatRate(valueM2, weightedArea, lease,startDate,endDate,invoiceCharge,incomingCharge,type, BudgetOverrideType.FLATRATE.reason);
    }

    public List<Charge> choices4NewForfait() {
        return chargeRepository.allOutgoing();
    }

    public List<Charge> choices5NewForfait() {
        return chargeRepository.allIncoming();
    }

    public String validateNewForfait(
            final BigDecimal valueM2,
            final BigDecimal weightedArea,
            final LocalDate startDate,
            final LocalDate endDate,
            final Charge invoiceCharge,
            final Charge incomingCharge,
            final BudgetCalculationType type
    ){
        return budgetOverrideRepository.validateNewBudgetOverride(lease, startDate, endDate, invoiceCharge, incomingCharge, type, BudgetOverrideType.FLATRATE.reason);
    }

    @Inject
    private BudgetOverrideRepository budgetOverrideRepository;

    @Inject
    private ChargeRepository chargeRepository;

}
