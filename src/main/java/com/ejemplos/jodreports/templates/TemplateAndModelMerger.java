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

import com.ejemplos.jodreports.opendocument.ManifestSubDocument;
import com.ejemplos.jodreports.opendocument.OpenDocumentArchive;
import com.ejemplos.jodreports.opendocument.SettingsSubDocument;
import com.ejemplos.jodreports.templates.image.ImageSource;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

/**
 * Merges a pre-processed template with the data model to
 * produce the output document.
 */
class TemplateAndModelMerger {

	private final Configuration freemarkerConfiguration;
	private final String[] xmlEntries;
	private final Map openDocumentSettings;
	private final Map configurations;

	public TemplateAndModelMerger(Configuration freemarkerConfiguration, String[] xmlEntries, 
			Map openDocumentSettings, Map configurations) {
		this.freemarkerConfiguration = freemarkerConfiguration;
		this.xmlEntries = xmlEntries;
		this.openDocumentSettings = openDocumentSettings;
		this.configurations = configurations;
	}

	public void process(OpenDocumentArchive archive, Object model) throws IOException, DocumentTemplateException {
		TemplateFreemarkerNamespace predefinedNamespace = new TemplateFreemarkerNamespace();
		predefinedNamespace.applyConfigurations(configurations);

        for (Object o : archive.getEntryNames()) {
            String entryName = (String) o;
            if (Arrays.binarySearch(xmlEntries, entryName) >= 0) {
                Reader reader = archive.getEntryReader(entryName);
                Writer writer = archive.getEntryWriter(entryName);

                Template template = new Template(entryName, reader, freemarkerConfiguration);

                BeansWrapper beansWrapper = new DefaultObjectWrapperBuilder(new Version("2.3.32")).build();
                try {
                    Environment environment = template.createProcessingEnvironment(model, writer);
                    environment.setGlobalVariable(TemplateFreemarkerNamespace.NAME, beansWrapper.wrap(predefinedNamespace));
                    environment.process();
                } catch (TemplateException templateException) {
                    throw new DocumentTemplateException(templateException);
                } finally {
                    IOUtils.closeQuietly(reader);
                    IOUtils.closeQuietly(writer);
                }
            }
        }
		
		if (!predefinedNamespace.getImages().isEmpty()) { 
			addRequiredImages(archive, predefinedNamespace.getImages());
		}
		if (!openDocumentSettings.isEmpty()) {
			changeOpenDocumentSettings(archive);
		}
	}

	private void addRequiredImages(OpenDocumentArchive archive, Map images) throws IOException {
		InputStream inputStream = archive.getEntryInputStream(OpenDocumentArchive.ENTRY_MANIFEST);
		ManifestSubDocument manifest = new ManifestSubDocument(inputStream);
		inputStream.close();

        for (Object o : images.keySet()) {
            ImageSource imageWriter = (ImageSource) o;
            String imageHref = (String) images.get(imageWriter);
            OutputStream imageOutputStream = archive.getEntryOutputStream(imageHref);
            imageWriter.write(imageOutputStream);
            IOUtils.closeQuietly(imageOutputStream);
            manifest.addFileEntry("image/png", imageHref);
        }

		OutputStream outputStream = archive.getEntryOutputStream(OpenDocumentArchive.ENTRY_MANIFEST);
		manifest.save(outputStream);
		outputStream.close();
	}
	
	private void changeOpenDocumentSettings(OpenDocumentArchive archive) throws IOException{
		InputStream inputStream = archive.getEntryInputStream(OpenDocumentArchive.ENTRY_SETTINGS);
		SettingsSubDocument settings = new SettingsSubDocument(inputStream);
		inputStream.close();

		settings.changeSettings(openDocumentSettings);

		OutputStream outputStream = archive.getEntryOutputStream(OpenDocumentArchive.ENTRY_SETTINGS);
		settings.save(outputStream);
		outputStream.close();
	}
}
