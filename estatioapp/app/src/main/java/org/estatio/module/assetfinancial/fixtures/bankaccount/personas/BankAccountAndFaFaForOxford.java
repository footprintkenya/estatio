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
package org.estatio.module.assetfinancial.fixtures.bankaccount.personas;

import org.estatio.module.asset.fixtures.property.personas.PropertyAndOwnerAndManagerForOxfGb;
import org.estatio.module.assetfinancial.fixtures.BankAccountAndFaFaAbstract;
import org.estatio.module.party.fixtures.organisation.personas.OrganisationForHelloWorldGb;


public class BankAccountAndFaFaForOxford extends BankAccountAndFaFaAbstract {

    public static final String BANK_ACCOUNT_REF = "NL31ABNA0580744432";

    public BankAccountAndFaFaForOxford() {
        this(null, null);
    }

    public BankAccountAndFaFaForOxford(String friendlyName, String localName) {
        super(friendlyName, localName);
    }

    @Override
    protected void execute(ExecutionContext executionContext) {

        // prereqs
        executionContext.executeChild(this, new PropertyAndOwnerAndManagerForOxfGb());
        executionContext.executeChild(this, new OrganisationForHelloWorldGb());

        // exec
        createBankAccountAndOptionallyFixedAssetFinancialAsset(OrganisationForHelloWorldGb.REF, BANK_ACCOUNT_REF, PropertyAndOwnerAndManagerForOxfGb.REF, executionContext);
        
    }

}