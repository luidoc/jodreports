package com.ejemplos.jodreports.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ejemplos.jodreports.opendocument.OpenDocumentIO;

import freemarker.template.Configuration;

public class DocumentTemplateFactory {

	private final Configuration freemarkerConfiguration;

	public DocumentTemplateFactory() {
		freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_32);
		freemarkerConfiguration.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		freemarkerConfiguration.setDefaultEncoding(OpenDocumentIO.UTF_8.name());
		freemarkerConfiguration.setOutputEncoding(OpenDocumentIO.UTF_8.name());
	}

	/**
	 * Retrieves the FreeMarker {@link Configuration} for this factory.
	 * <p>
	 * You can use this method to set custom FreeMarker options on the returned
	 * {@link Configuration}, and they will apply to all templates returned by
	 * this factory.
	 * <p>
	 * Any such customizations should be used carefully. 
	 * Only use this method if you know what you are doing. 
	 * Limitation: Do not change the default square-bracket Tag Syntax.  
	 * 
	 * 
	 * @return the FreeMarker {@link Configuration} 
	 */
	public Configuration getFreemarkerConfiguration() {
		return freemarkerConfiguration;
	}

	public DocumentTemplate getTemplate(File file) throws IOException {
		if (file.isDirectory()) {
			return new UnzippedDocumentTemplate(file, freemarkerConfiguration);
		} else {
			return getTemplate(new FileInputStream(file));
		}
	}

	public DocumentTemplate getTemplate(InputStream inputStream) throws IOException {
		return new ZippedDocumentTemplate(inputStream, freemarkerConfiguration);
	}

}
