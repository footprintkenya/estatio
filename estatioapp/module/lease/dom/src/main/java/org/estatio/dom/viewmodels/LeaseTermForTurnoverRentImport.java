package org.estatio.dom.viewmodels;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import org.estatio.dom.Importable;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseTerm;
import org.estatio.dom.lease.LeaseTermForTurnoverRent;
import org.estatio.dom.lease.LeaseTermStatus;

import lombok.Getter;
import lombok.Setter;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "org.estatio.dom.viewmodels.LeaseTermForTurnoverRentImport"
)
public class LeaseTermForTurnoverRentImport extends LeaseTermImportAbstract implements ExcelFixtureRowHandler, Importable {

    private static final Logger LOG = LoggerFactory.getLogger(LeaseTermForTurnoverRentImport.class);

    // turnover rent term fields
    @Getter @Setter
    private String turnoverRentRule;

    @Getter @Setter
    private BigDecimal auditedTurnover;

    @Getter @Setter
    private BigDecimal auditedTurnoverRent;

    // source fields

    static int counter = 0;

    @Programmatic
    @Override
    public List<Object> handleRow(FixtureScript.ExecutionContext executionContext, ExcelFixture excelFixture, Object o) {
        return importData(null);
    }

    // REVIEW: other import view models have @Action annotation here...  but in any case, is this view model actually ever surfaced in the UI?
    public List<Object> importData() {
        return importData(null);
    }

    @Programmatic
    @Override
    public List<Object> importData(Object previousRow) {

        LeaseItem item = initLeaseItem();

        //create term
        LeaseTermForTurnoverRent term = (LeaseTermForTurnoverRent) item.findTermWithSequence(getSequence());
        if (term == null) {
            if (getStartDate() == null) {
                throw new IllegalArgumentException("startDate cannot be empty");
            }
            if (getSequence().equals(BigInteger.ONE)) {
                term = (LeaseTermForTurnoverRent) item.newTerm(getStartDate(), getEndDate());
            } else {
                final LeaseTerm previousTerm = item.findTermWithSequence(getSequence().subtract(BigInteger.ONE));
                if (previousTerm == null) {
                    throw new IllegalArgumentException("Previous term not found");
                }
                term = (LeaseTermForTurnoverRent) previousTerm.createNext(getStartDate(), getEndDate());
            }
            term.setSequence(getSequence());
        }
        term.setStatus(LeaseTermStatus.valueOf(getStatus()));

        //set turnover rent term values
        term.setTurnoverRentRule(turnoverRentRule);
        term.setAuditedTurnover(auditedTurnover);
        term.setAuditedTurnoverRent(auditedTurnoverRent);

        return Lists.newArrayList(term);

    }

    //region > injected services

    //endregion

}
