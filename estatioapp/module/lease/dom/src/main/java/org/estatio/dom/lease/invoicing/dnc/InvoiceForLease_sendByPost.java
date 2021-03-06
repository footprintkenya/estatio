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
package org.estatio.dom.lease.invoicing.dnc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.value.Blob;

import org.incode.module.communications.dom.impl.commchannel.PostalAddress;
import org.incode.module.communications.dom.mixins.DocumentConstants;
import org.incode.module.communications.dom.mixins.Document_sendByPost;
import org.incode.module.document.dom.DocumentModule;
import org.incode.module.document.dom.impl.docs.Document;
import org.incode.module.document.dom.impl.docs.DocumentAbstract;
import org.incode.module.document.dom.impl.docs.DocumentState;
import org.incode.module.document.dom.impl.paperclips.Paperclip;

import org.estatio.dom.invoice.DocumentTypeData;
import org.estatio.dom.lease.invoicing.InvoiceForLease;

/**
 * Provides the ability to send an print.
 *
 * TODO: REVIEW: this mixin could in theory be inlined, but maybe we want to keep invoices and documents decoupled?
 */
@Mixin
public class InvoiceForLease_sendByPost extends InvoiceForLease_sendAbstract {

    public InvoiceForLease_sendByPost(final InvoiceForLease invoice) {
        super(invoice);
    }

    public static class ActionDomainEvent extends DocumentModule.ActionDomainEvent<InvoiceForLease_sendByPost> { }

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT,
            domainEvent = ActionDomainEvent.class
    )
    @ActionLayout(
            cssClassFa = "envelope-o",
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(name = "documents", sequence = "4.2")
    public Blob $$(
            final Document document,
            @ParameterLayout(named = "to:")
            final PostalAddress toChannel) throws IOException {

        createPostalCommunicationAsSent(document, toChannel);

        final byte[] mergedBytes = mergePdfBytes(document);

        final String fileName = document.getName();
        return new Blob(fileName, DocumentConstants.MIME_TYPE_APPLICATION_PDF, mergedBytes);
    }

    public String disable$$() {
        if(choices0$$().isEmpty()) {
            return "No documents available to send";
        }
        return null;
    }

    public Document default0$$() {
        final List<Document> documents = choices0$$();
        return documents.size() == 1 ? documents.get(0): null;
    }

    public List<Document> choices0$$() {
        final List<Paperclip> paperclips = paperclipRepository.findByAttachedTo(invoice);
        final List<Document> documents = Lists.newArrayList();
        for (Paperclip paperclip : paperclips) {
            final DocumentAbstract documentAbs = paperclip.getDocument();
            if (!(documentAbs instanceof Document)) {
                continue;
            }
            final Document document = (Document) documentAbs;
            if (document.getState() != DocumentState.RENDERED) {
                continue;
            }
            if (!DocumentTypeData.isPrimaryType(document)) {
                continue;
            }
            final Document_sendByPost document_sendByPost = document_sendByPost(document);
            if(document_sendByPost.disableAct() != null) {
                continue;
            }
            documents.add(document);
        }
        return documents;
    }

    public Set<PostalAddress> choices1$$(final Document document) {
        return document == null ? Collections.emptySet() : document_sendByPost(document).choices0Act();
    }

    // TODO: currently not properly supported by Isis, but does not harm
    public PostalAddress default1$$(final Document document) {
        return document == null ? null : document_sendByPost(document).default0Act();
    }





}
