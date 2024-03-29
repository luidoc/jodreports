//
// JOOReports - The Open Source Java/OpenOffice Report Engine
// Copyright (C) 2004-2006 - Mirko Nasato <mirko@artofsolving.com>
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// http://www.gnu.org/copyleft/lesser.html
//
package com.ejemplos.jodreports.templates;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.ejemplos.jodreports.opendocument.OpenDocumentArchive;
import com.ejemplos.jodreports.templates.DocumentTemplate.ContentWrapper;
import com.ejemplos.jodreports.templates.xmlfilters.XmlEntryFilter;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;

import org.apache.commons.io.output.ByteArrayOutputStream;

class TemplatePreProcessor {

	private static final String UTF_8 = "UTF-8";

	private String[] xmlEntries;
	private XmlEntryFilter[] xmlEntryFilters;
	private ContentWrapper contentWrapper;
	private final Map configurations;
	
	public TemplatePreProcessor(String[] xmlEntries, XmlEntryFilter[] xmlEntryFilters, ContentWrapper contentWrapper, Map configurations) {
		this.xmlEntries = xmlEntries;
		this.xmlEntryFilters = xmlEntryFilters;
		this.contentWrapper = contentWrapper;
		this.configurations = configurations;
	}

	public void process(OpenDocumentArchive archive) throws DocumentTemplateException, IOException {
        for (Object o : archive.getEntryNames()) {
            String entryName = (String) o;
            if (Arrays.binarySearch(xmlEntries, entryName) >= 0) {
                InputStream inputStream = archive.getEntryInputStream(entryName);
                OutputStream outputStream = archive.getEntryOutputStream(entryName);
                applyXmlFilters(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
            }
        }
	}

	private void applyXmlFilters(InputStream input, OutputStream output) throws DocumentTemplateException, IOException {
		Builder builder = new Builder();
		Document document = null;
		try {
			document = builder.build(input);
		} catch (ParsingException parsingException) {
			throw new DocumentTemplateException(parsingException);
		}

        for (XmlEntryFilter xmlEntryFilter : xmlEntryFilters) {
            xmlEntryFilter.applyConfigurations(configurations);
            xmlEntryFilter.doFilter(document);
        }
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		Serializer serializer = new Serializer(byteArrayOutputStream, UTF_8);
		serializer.write(document);

		output.write(contentWrapper.wrapContent(byteArrayOutputStream.toString(UTF_8)).getBytes(UTF_8));
	}
}
