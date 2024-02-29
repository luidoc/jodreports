package com.ejemplos.jodreports.opendocument;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


import org.apache.commons.io.output.ByteArrayOutputStream;

public class OpenDocumentArchive {
	
	public static final String ENTRY_MIMETYPE = "mimetype";
	public static final String ENTRY_STYLES = "styles.xml";
	public static final String ENTRY_CONTENT = "content.xml";
	public static final String ENTRY_MANIFEST = "META-INF/manifest.xml";
	public static final String ENTRY_SETTINGS = "settings.xml";

	private final Map/*<String,byte[]>*/ entries = new LinkedHashMap();

	/**
	 * A {@link ByteArrayOutputStream} that updates the entry data when it get close()d
	 */
	private class EntryByteArrayOutputStream extends ByteArrayOutputStream {
		
		private final String entryName;
		
		public EntryByteArrayOutputStream(String entryName) {
			this.entryName = entryName;
		}
		
		@Override
		public void close() throws IOException {
			entries.put(entryName, toByteArray());
		}
	}

	public Set getEntryNames() {
		return entries.keySet();
	}

	public InputStream getEntryInputStream(String entryName) {
		return new ByteArrayInputStream((byte[]) entries.get(entryName));
	}

	public Reader getEntryReader(String entryName) {
		return OpenDocumentIO.toUtf8Reader(getEntryInputStream(entryName));
	}

	/**
	 * Returns an {@link OutputStream} for writing the content of the given entry
	 * 
	 * @param entryName
	 * @return an {@link OutputStream}
	 */
	public OutputStream getEntryOutputStream(String entryName) {
		return new EntryByteArrayOutputStream(entryName);
	}

	/**
	 * Returns a {@link Writer} for writing the content of the given entry
	 * 
	 * @param entryName
	 * @return a {@link Writer}
	 */
	public Writer getEntryWriter(String entryName) {
		return OpenDocumentIO.toUtf8Writer(getEntryOutputStream(entryName));
	}

	public OpenDocumentArchive createCopy() {
		OpenDocumentArchive archiveCopy = new OpenDocumentArchive();
        for (Object o : entries.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String name = (String) entry.getKey();
            byte[] entryData = (byte[]) entry.getValue();
            byte[] entryDataCopy = new byte[entryData.length];
            System.arraycopy(entryData, 0, entryDataCopy, 0, entryData.length);
            archiveCopy.entries.put(name, entryDataCopy);
        }
		return archiveCopy;
	}
}
