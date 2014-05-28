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
package org.estatio.integtests.financial;

import javax.inject.Inject;
import org.estatio.dom.financial.FinancialAccount;
import org.estatio.dom.financial.FinancialAccounts;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.asset.PropertyForKal;
import org.estatio.fixture.asset.PropertyForOxf;
import org.estatio.fixture.financial.*;
import org.estatio.fixture.invoice.InvoiceForKalPoison001;
import org.estatio.fixture.invoice.InvoiceForOxfPoison003;
import org.estatio.fixture.lease.*;
import org.estatio.fixture.party.*;
import org.estatio.integtests.EstatioIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FinancialAccountsTest_findAccountByReference extends EstatioIntegrationTest {

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
                execute(new BankAccountAndMandateForHelloWorld(), executionContext);

                execute(new OrganisationForAcme(), executionContext);
                execute(new PropertyForKal(), executionContext);
                execute(new BankAccountAndMandateForAcme(), executionContext);

                execute(new OrganisationForTopModel(), executionContext);
                execute(new LeaseBreakOptionsForOxfTopModel001(), executionContext);
                execute(new BankAccountAndMandateForTopModel(), executionContext);

                execute(new OrganisationForMediaX(), executionContext);
                execute(new LeaseBreakOptionsForOxfMediax002(), executionContext);
                execute(new BankAccountAndMandateForMediaX(), executionContext);

                execute(new OrganisationForPoison(), executionContext);
                execute(new LeaseBreakOptionsForOxfPoison003(), executionContext);
                execute(new LeaseItemAndTermsForKalPoison001(), executionContext);
                execute(new BankAccountAndMandateForPoison(), executionContext);
                execute(new InvoiceForOxfPoison003(), executionContext);
                execute(new InvoiceForKalPoison001(), executionContext);

                execute(new OrganisationForPret(), executionContext);
                execute(new LeaseForOxfPret004(), executionContext);
                execute(new BankAccountAndMandateForPret(), executionContext);

                execute(new OrganisationForMiracle(), executionContext);
                execute(new LeaseItemAndTermsForOxfMiracl005(), executionContext);
                execute(new BankAccountAndMandateForMiracle(), executionContext);


            }
        }.withTracing());
    }
    @Inject
    private FinancialAccounts financialAccounts;

    @Test
    public void forAccount() {
        // when
        FinancialAccount account = financialAccounts.findAccountByReference("NL31ABNA0580744435"); // Associated with TOPMODEL
        // then
        assertThat(account, is(notNullValue()));
    }

}