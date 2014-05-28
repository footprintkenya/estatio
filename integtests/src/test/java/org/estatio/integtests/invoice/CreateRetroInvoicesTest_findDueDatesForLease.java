/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.integtests.invoice;

import java.util.SortedSet;
import javax.inject.Inject;
import org.estatio.dom.asset.Properties;
import org.estatio.dom.invoice.Invoices;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.Leases;
import org.estatio.dom.lease.invoicing.InvoiceCalculationService;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.asset.PropertyForKal;
import org.estatio.fixture.asset.PropertyForOxf;
import org.estatio.fixture.invoice.InvoiceForKalPoison001;
import org.estatio.fixture.invoice.InvoiceForOxfPoison003;
import org.estatio.fixture.lease.*;
import org.estatio.fixture.party.*;
import org.estatio.fixturescripts.CreateRetroInvoices;
import org.estatio.integtests.EstatioIntegrationTest;
import org.estatio.integtests.VT;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateRetroInvoicesTest_findDueDatesForLease extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        scenarioExecution().install(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                execute(new EstatioBaseLineFixture(), executionContext);

                execute(new PersonForJohnDoe(), executionContext);
                execute(new PersonForLinusTorvalds(), executionContext);

                execute(new OrganisationForHelloWorld(), executionContext);
                execute(new PropertyForOxf(), executionContext);

                execute(new OrganisationForAcme(), executionContext);
                execute(new PropertyForKal(), executionContext);

                execute(new OrganisationForTopModel(), executionContext);
                execute(new LeaseBreakOptionsForOxfTopModel001(), executionContext);

                execute(new OrganisationForMediaX(), executionContext);
                execute(new LeaseBreakOptionsForOxfMediax002(), executionContext);

                execute(new OrganisationForPoison(), executionContext);
                execute(new LeaseBreakOptionsForOxfPoison003(), executionContext);
                execute(new LeaseItemAndTermsForKalPoison001(), executionContext);
                execute(new InvoiceForOxfPoison003(), executionContext);
                execute(new InvoiceForKalPoison001(), executionContext);

                execute(new OrganisationForPret(), executionContext);
                execute(new LeaseForOxfPret004(), executionContext);

                execute(new OrganisationForMiracle(), executionContext);
                execute(new LeaseItemAndTermsForOxfMiracl005(), executionContext);

            }
        });
    }

    @Inject
    private Invoices invoices;
    @Inject
    private Properties properties;
    @Inject
    private Leases leases;
    @Inject
    private InvoiceCalculationService invoiceCalculationService;

    private CreateRetroInvoices creator;

    Lease lease;

    @Before
    public void setup() {
        creator = new CreateRetroInvoices();
        creator.leases = leases;
        creator.invoices = invoices;
        creator.properties = properties;
        creator.invoiceCalculationService = invoiceCalculationService;

        lease = leases.findLeaseByReference("OXF-TOPMODEL-001");
    }

    @Test
    public void happyCase() {
        SortedSet<LocalDate> dueDates = creator.findDueDatesForLease(VT.ld(2012, 1, 1), VT.ld(2014, 1, 1), lease);
        assertThat(dueDates.size(), is(10));
    }

}